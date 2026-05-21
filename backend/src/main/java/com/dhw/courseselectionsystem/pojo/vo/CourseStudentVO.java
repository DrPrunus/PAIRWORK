package com.dhw.courseselectionsystem.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CourseStudentVO {
    private Long studentId;
    private String studentNo;   // 学号
    private String studentName;
    private String major;       // 专业
    private String className;   // 班级
    private BigDecimal score;   // 可能为null
}