package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.pojo.dto.StudentDTO;
import com.dhw.courseselectionsystem.pojo.vo.StudentVO;
import com.dhw.courseselectionsystem.service.StudentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
public class AdminStudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Result<PageInfo<StudentVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String studentNo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String className) {
        PageInfo<StudentVO> page = studentService.pageQuery(pageNum, pageSize, studentNo, name, major, className);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> add(@RequestBody StudentDTO dto) {
        studentService.addStudent(dto);
        return Result.success("添加学生成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody StudentDTO dto) {
        if (dto.getId() == null) {
            return Result.error("学生ID不能为空");
        }
        studentService.updateStudent(dto);
        return Result.success("修改学生成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return Result.success("删除学生成功");
    }

    @PostMapping("/{id}/reset-password")
    public Result<?> resetPassword(@PathVariable Long id, @RequestParam(defaultValue = "123456") String newPassword) {
        studentService.resetPassword(id, newPassword);
        return Result.success("密码已重置为: " + newPassword);
    }
}