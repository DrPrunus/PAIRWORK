package com.dhw.courseselectionsystem.service.impl;

import com.dhw.courseselectionsystem.exception.BusinessException;
import com.dhw.courseselectionsystem.mapper.CourseMapper;
import com.dhw.courseselectionsystem.mapper.CourseSelectionMapper;
import com.dhw.courseselectionsystem.pojo.dto.CourseDTO;
import com.dhw.courseselectionsystem.pojo.entity.Course;
import com.dhw.courseselectionsystem.pojo.vo.CourseStudentVO;
import com.dhw.courseselectionsystem.pojo.vo.CourseVO;
import com.dhw.courseselectionsystem.service.CourseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Override
    public PageInfo<CourseVO> listCourses(Integer pageNum, Integer pageSize, String name, Long teacherId, Long semesterId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> courseList = courseMapper.list(name, teacherId, semesterId);
        List<CourseVO> voList = courseList.stream().map(course -> {
            CourseVO vo = new CourseVO();
            BeanUtils.copyProperties(course, vo);
            // 手动处理字段名不一致（teacherName, semesterName 已在entity中）
            vo.setTeacherName(course.getTeacherName());
            vo.setSemesterName(course.getSemesterName());
            return vo;
        }).collect(Collectors.toList());
        return new PageInfo<>(voList);
    }


    @Override
    public List<CourseVO> getTeacherCourses(Long teacherId, String courseName, String schedule) {
        List<Course> courses = courseMapper.findByTeacherId(teacherId, courseName, schedule);
        return courses.stream().map(course -> {
            CourseVO vo = new CourseVO();
            BeanUtils.copyProperties(course, vo);
            vo.setTeacherName(course.getTeacherName());
            vo.setSemesterName(course.getSemesterName());
            return vo;
        }).collect(Collectors.toList());
    }


    @Override
    public List<CourseStudentVO> getCourseStudents(Long courseId, Long teacherId) {
        // 检查课程是否存在且教师是否任教该课程
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        if (!course.getTeacherId().equals(teacherId)) {
            throw new BusinessException("您不是该课程的任课教师，无法查看学生名单");
        }
        return courseSelectionMapper.findStudentsByCourseId(courseId);
    }

    @Override
    public PageInfo<CourseVO> listForAdmin(Integer pageNum, Integer pageSize, String name, Long teacherId, Long semesterId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> courses = courseMapper.listForAdmin(name, teacherId, semesterId);
        List<CourseVO> voList = courses.stream().map(course -> {
            CourseVO vo = new CourseVO();
            BeanUtils.copyProperties(course, vo);
            vo.setTeacherName(course.getTeacherName());
            vo.setSemesterName(course.getSemesterName());
            return vo;
        }).collect(Collectors.toList());
        return new PageInfo<>(voList);
    }

    @Override
    @Transactional
    public void addCourse(CourseDTO dto) {
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        course.setCurrentStudents(0);
        int rows = courseMapper.insertCourse(course);
        if (rows == 0) throw new BusinessException("添加课程失败");
    }

    @Override
    @Transactional
    public void updateCourse(CourseDTO dto) {
        Course exist = courseMapper.findById(dto.getId());
        if (exist == null) throw new BusinessException("课程不存在");
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        int rows = courseMapper.updateCourse(course);
        if (rows == 0) throw new BusinessException("更新课程失败");
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseMapper.findById(id);
        if (course == null) throw new BusinessException("课程不存在");
        int count = courseMapper.countSelectionsByCourseId(id);
        if (count > 0) throw new BusinessException("该课程已有学生选课，无法删除");
        int rows = courseMapper.deleteCourseById(id);
        if (rows == 0) throw new BusinessException("删除课程失败");
    }


}