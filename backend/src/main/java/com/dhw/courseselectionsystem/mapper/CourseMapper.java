package com.dhw.courseselectionsystem.mapper;

import com.dhw.courseselectionsystem.pojo.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {
    /**
     * 分页条件查询课程列表
     * @param name 课程名称（模糊）
     * @param teacherId 教师ID
     * @param semesterId 学期ID
     * @return 课程列表
     */
    List<Course> list(@Param("name") String name,
                      @Param("teacherId") Long teacherId,
                      @Param("semesterId") Long semesterId);

    // 根据ID查询课程（带教师和学期信息，建议返回Course，但已关联）
    Course findById(@Param("id") Long id);

    // 查询学生已选的所有课程（用于时间冲突检测）
    List<Course> findSelectedCoursesByStudentId(@Param("studentId") Long studentId);

    // 乐观锁：课程人数+1（条件：未满）
    int incrementCurrentStudents(@Param("id") Long id);

    // 退课：课程人数-1
    int decrementCurrentStudents(@Param("id") Long id);

    // 检查课程是否由该教师任教
    @Select("SELECT COUNT(*) FROM course WHERE id = #{courseId} AND teacher_id = #{teacherId}")
    int countByCourseAndTeacher(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId);


    /**
     * 查询教师任教的课程列表
     * @return 课程列表
     */
    List<Course> findByTeacherId(@Param("teacherId") Long teacherId,
                                 @Param("courseName") String courseName,
                                 @Param("schedule") String schedule);

    @Select("SELECT COUNT(*) FROM course WHERE teacher_id = #{teacherId}")
    int countByTeacherId(Long teacherId);

    // 分页条件查询（管理员用）
    List<Course> listForAdmin(@Param("name") String name,
                              @Param("teacherId") Long teacherId,
                              @Param("semesterId") Long semesterId);

    // 新增课程
    int insertCourse(Course course);

    // 更新课程
    int updateCourse(Course course);

    // 删除课程
    int deleteCourseById(Long id);

    // 检查课程下是否有选课记录
    @Select("SELECT COUNT(*) FROM course_selection WHERE course_id = #{courseId}")
    int countSelectionsByCourseId(Long courseId);


}