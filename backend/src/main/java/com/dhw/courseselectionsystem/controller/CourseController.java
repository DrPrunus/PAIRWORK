package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.pojo.vo.CourseVO;
import com.dhw.courseselectionsystem.service.CourseService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public Result<PageInfo<CourseVO>> listCourses(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long semesterId) {
        PageInfo<CourseVO> pageInfo = courseService.listCourses(pageNum, pageSize, name, teacherId, semesterId);
        return Result.success(pageInfo);
    }
}