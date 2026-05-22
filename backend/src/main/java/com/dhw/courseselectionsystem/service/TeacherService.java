package com.dhw.courseselectionsystem.service;

import com.dhw.courseselectionsystem.pojo.dto.TeacherDTO;
import com.dhw.courseselectionsystem.pojo.vo.TeacherVO;
import com.github.pagehelper.PageInfo;

public interface TeacherService {
    PageInfo<TeacherVO> pageQuery(Integer pageNum, Integer pageSize, String teacherNo, String name, String dept);
    void addTeacher(TeacherDTO dto);
    void updateTeacher(TeacherDTO dto);
    void deleteTeacher(Long id);
    void resetPassword(Long id, String newPassword);
}