package com.dhw.courseselectionsystem.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CourseVO {
    private Long id;
    private String courseCode;
    private String name;
    private BigDecimal credit;
    private Integer maxStudents;
    private Integer currentStudents;
    private String teacherName;
    private String semesterName;
    private String schedule;
    private Integer status;
}