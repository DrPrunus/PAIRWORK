package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.pojo.dto.SemesterDTO;
import com.dhw.courseselectionsystem.pojo.entity.Semester;
import com.dhw.courseselectionsystem.pojo.vo.SemesterVO;
import com.dhw.courseselectionsystem.service.SemesterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/semesters")
public class AdminSemesterController {

    @Autowired
    private SemesterService semesterService;

    @GetMapping
    public Result<List<SemesterVO>> list() {
        List<SemesterVO> list = semesterService.listAll();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<SemesterVO> getById(@PathVariable Long id) {
        SemesterVO vo = semesterService.getById(id);
        return Result.success(vo);
    }

    @PostMapping
    public Result<?> add(@RequestBody SemesterDTO dto) {
        Semester semester = new Semester();
        BeanUtils.copyProperties(dto, semester);
        semesterService.addSemester(semester);
        return Result.success("添加学期成功");
    }

    @PutMapping
    public Result<?> update(@RequestBody SemesterDTO dto) {
        if (dto.getId() == null) {
            return Result.error("学期ID不能为空");
        }
        Semester semester = new Semester();
        BeanUtils.copyProperties(dto, semester);
        semesterService.updateSemester(semester);
        return Result.success("修改学期成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return Result.success("删除学期成功");
    }
}