package com.dhw.courseselectionsystem.service;

import com.dhw.courseselectionsystem.pojo.entity.Semester;
import com.dhw.courseselectionsystem.pojo.vo.SemesterVO;
import java.util.List;

public interface SemesterService {
    List<SemesterVO> listAll();
    SemesterVO getById(Long id);
    void addSemester(Semester semester);
    void updateSemester(Semester semester);
    void deleteSemester(Long id);
}