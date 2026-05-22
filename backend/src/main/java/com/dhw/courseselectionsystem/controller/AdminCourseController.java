package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.CourseDTO;
import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.pojo.vo.CourseVO;
import com.dhw.courseselectionsystem.service.CourseService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Result<PageInfo<CourseVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long semesterId) {
        PageInfo<CourseVO> page = courseService.listForAdmin(pageNum, pageSize, name, teacherId, semesterId);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> add(@RequestBody CourseDTO dto) {
        courseService.addCourse(dto);
        return Result.success("添加课程成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody CourseDTO dto) {
        if (dto.getId() == null) return Result.error("课程ID不能为空");
        courseService.updateCourse(dto);
        return Result.success("修改课程成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.success("删除课程成功");
    }
}