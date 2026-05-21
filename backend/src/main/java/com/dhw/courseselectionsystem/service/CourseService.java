package com.dhw.courseselectionsystem.service;

import com.dhw.courseselectionsystem.pojo.dto.CourseDTO;
import com.dhw.courseselectionsystem.pojo.vo.CourseStudentVO;
import com.github.pagehelper.PageInfo;
import com.dhw.courseselectionsystem.pojo.vo.CourseVO;

import java.util.List;

public interface CourseService {
    PageInfo<CourseVO> listCourses(Integer pageNum, Integer pageSize, String name, Long teacherId, Long semesterId);

    List<CourseVO> getTeacherCourses(Long teacherId, String courseName, String schedule);

    List<CourseStudentVO> getCourseStudents(Long courseId, Long teacherId);

    PageInfo<CourseVO> listForAdmin(Integer pageNum, Integer pageSize, String name, Long teacherId, Long semesterId);
    void addCourse(CourseDTO dto);
    void updateCourse(CourseDTO dto);
    void deleteCourse(Long id);
}