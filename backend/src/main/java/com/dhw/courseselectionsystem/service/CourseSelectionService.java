package com.dhw.courseselectionsystem.service;

import com.dhw.courseselectionsystem.pojo.vo.MySelectionVO;
import com.dhw.courseselectionsystem.pojo.vo.ScoreVO;

import java.math.BigDecimal;
import java.util.List;

public interface CourseSelectionService {
    void selectCourse(Long studentId, Long courseId);
    void cancelCourse(Long studentId, Long courseId);
    List<MySelectionVO> getMySelections(Long studentId);
    List<ScoreVO> getMyScores(Long studentId);
    void inputScore(Long teacherId, Long courseId, Long studentId, BigDecimal score);
}