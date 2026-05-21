package com.dhw.courseselectionsystem.service.impl;

import com.dhw.courseselectionsystem.exception.BusinessException;
import com.dhw.courseselectionsystem.mapper.*;
import com.dhw.courseselectionsystem.pojo.entity.Course;
import com.dhw.courseselectionsystem.pojo.entity.CourseSelection;
import com.dhw.courseselectionsystem.pojo.entity.Semester;
import com.dhw.courseselectionsystem.pojo.entity.Student;
import com.dhw.courseselectionsystem.pojo.vo.MySelectionVO;
import com.dhw.courseselectionsystem.pojo.vo.ScoreVO;
import com.dhw.courseselectionsystem.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private SemesterMapper semesterMapper;

    @Override
    @Transactional
    public void selectCourse(Long studentId, Long courseId) {
        // 1. 检查学生是否存在
        Student student = studentMapper.findById(studentId);
        if (student == null) {
            throw new BusinessException("学生信息不存在");
        }

        // 2. 检查课程是否存在且有效
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        if (course.getStatus() != 1) { // 1=进行中
            throw new BusinessException("课程未开始或已结束");
        }

        // 3. 检查选课时间段
        Semester currentSemester = semesterMapper.findCurrentSemester();
        if (currentSemester == null) {
            throw new BusinessException("未找到当前学期配置");
        }
        LocalDateTime now = LocalDateTime.now();
        if (currentSemester.getEnrollmentStart() == null || currentSemester.getEnrollmentEnd() == null) {
            throw new BusinessException("选课时间未配置");
        }
        if (now.isBefore(currentSemester.getEnrollmentStart()) || now.isAfter(currentSemester.getEnrollmentEnd())) {
            throw new BusinessException("不在选课时间段内");
        }

        // 4. 检查是否已选（status=0）
        CourseSelection active = courseSelectionMapper.findActiveByStudentAndCourse(studentId, courseId);
        if (active != null) {
            throw new BusinessException("您已经选过该课程，不可重复选课");
        }

        // 5. 容量预检
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            throw new BusinessException("课程人数已满");
        }

        // 6. 时间冲突检测
        List<Course> selectedCourses = courseMapper.findSelectedCoursesByStudentId(studentId);
        for (Course selected : selectedCourses) {
            if (selected.getSchedule() != null && selected.getSchedule().equals(course.getSchedule())) {
                throw new BusinessException("时间冲突：与已选课程《" + selected.getName() + "》时间相同");
            }
        }

        // 7. 检查是否存在已退课记录（status=1）
        CourseSelection any = courseSelectionMapper.findAnyByStudentAndCourse(studentId, courseId);
        if (any != null && any.getStatus() == 1) {
            // 复活：更新 status=0，并更新选课时间
            int reactivated = courseSelectionMapper.reactivate(studentId, courseId, LocalDateTime.now());
            if (reactivated == 0) {
                throw new BusinessException("选课失败，复活记录失败");
            }
        } else {
            // 插入新记录
            CourseSelection selection = new CourseSelection();
            selection.setStudentId(studentId);
            selection.setCourseId(courseId);
            selection.setSelectionTime(LocalDateTime.now());
            selection.setStatus(0);
            courseSelectionMapper.insert(selection);
        }

        // 8. 乐观锁更新人数
        int updated = courseMapper.incrementCurrentStudents(courseId);
        if (updated == 0) {
            throw new BusinessException("选课失败，课程人数已满，请稍后重试");
        }
    }

    @Override
    @Transactional
    public void cancelCourse(Long studentId, Long courseId) {
        // 检查选课记录是否存在且有效
        CourseSelection exist = courseSelectionMapper.findActiveByStudentAndCourse(studentId, courseId);
        if (exist == null) {
            throw new BusinessException("未找到该选课记录");
        }
        // 退课：更新状态为1
        int cancelled = courseSelectionMapper.cancelByStudentAndCourse(studentId, courseId);
        if (cancelled == 0) {
            throw new BusinessException("退课失败");
        }
        // 课程人数减一
        courseMapper.decrementCurrentStudents(courseId);
    }

    @Override
    public List<MySelectionVO> getMySelections(Long studentId) {
        return courseSelectionMapper.findSelectedCoursesByStudentId(studentId);
    }


    @Override
    public List<ScoreVO> getMyScores(Long studentId) {
        return courseSelectionMapper.findScoresByStudentId(studentId);
    }

    @Override
    @Transactional
    public void inputScore(Long teacherId, Long courseId, Long studentId, BigDecimal score) {
        // 1. 校验成绩范围
        if (score == null || score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessException("成绩必须在0-100之间");
        }

        // 2. 验证教师是否任教该课程
        int count = courseMapper.countByCourseAndTeacher(courseId, teacherId);
        if (count == 0) {
            throw new BusinessException("您不是该课程的任课教师，无法录入成绩");
        }

        // 3. 验证选课记录是否存在且状态为已选（status=0）
        CourseSelection selection = courseSelectionMapper.findActiveByStudentAndCourse(studentId, courseId);
        if (selection == null) {
            throw new BusinessException("该学生未选修该课程或已退课");
        }

        // 4. 更新成绩
        int updated = courseSelectionMapper.updateScore(courseId, studentId, score);
        if (updated == 0) {
            throw new BusinessException("成绩录入失败");
        }
    }


}