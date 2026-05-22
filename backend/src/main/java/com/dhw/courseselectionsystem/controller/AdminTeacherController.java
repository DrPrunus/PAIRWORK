package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.pojo.dto.TeacherDTO;
import com.dhw.courseselectionsystem.pojo.vo.TeacherVO;
import com.dhw.courseselectionsystem.service.TeacherService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/teachers")
public class AdminTeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public Result<PageInfo<TeacherVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String teacherNo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String dept) {
        PageInfo<TeacherVO> page = teacherService.pageQuery(pageNum, pageSize, teacherNo, name, dept);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> add(@RequestBody TeacherDTO dto) {
        teacherService.addTeacher(dto);
        return Result.success("添加教师成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody TeacherDTO dto) {
        if (dto.getId() == null) {
            return Result.error("教师ID不能为空");
        }
        teacherService.updateTeacher(dto);
        return Result.success("修改教师成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return Result.success("删除教师成功");
    }

    @PostMapping("/{id}/reset-password")
    public Result<?> resetPassword(@PathVariable Long id, @RequestParam(defaultValue = "123456") String newPassword) {
        teacherService.resetPassword(id, newPassword);
        return Result.success("密码已重置为: " + newPassword);
    }
}