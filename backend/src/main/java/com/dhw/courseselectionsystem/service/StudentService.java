package com.dhw.courseselectionsystem.service;

import com.dhw.courseselectionsystem.pojo.dto.StudentDTO;
import com.dhw.courseselectionsystem.pojo.vo.StudentVO;
import com.github.pagehelper.PageInfo;

public interface StudentService {
    PageInfo<StudentVO> pageQuery(Integer pageNum, Integer pageSize, String studentNo, String name, String major, String className);
    void addStudent(StudentDTO dto);
    void updateStudent(StudentDTO dto);
    void deleteStudent(Long id);
    void resetPassword(Long id, String newPassword);
}