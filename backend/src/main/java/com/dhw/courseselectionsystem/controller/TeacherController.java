package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.pojo.vo.CourseStudentVO;
import com.dhw.courseselectionsystem.pojo.vo.CourseVO;
import com.dhw.courseselectionsystem.service.CourseService;
import com.dhw.courseselectionsystem.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取当前教师的课程列表
     * @return 课程列表
     */
    @GetMapping("/courses")
    public Result<List<CourseVO>> getMyCourses(
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) String schedule) {
        UserContext.UserInfo user = UserContext.getUser();
        if (user == null || !"teacher".equals(user.getRole())) {
            return Result.error("只有教师可以查看");
        }
        Long teacherId = user.getRefId();
        List<CourseVO> courses = courseService.getTeacherCourses(teacherId, courseName, schedule);
        return Result.success(courses);
    }

    @GetMapping("/courses/{courseId}/students")
    public Result<List<CourseStudentVO>> getCourseStudents(@PathVariable Long courseId) {
        UserContext.UserInfo user = UserContext.getUser();
        if (user == null || !"teacher".equals(user.getRole())) {
            return Result.error("只有教师可以查看");
        }
        Long teacherId = user.getRefId();
        List<CourseStudentVO> students = courseService.getCourseStudents(courseId, teacherId);
        return Result.success(students);
    }
}
