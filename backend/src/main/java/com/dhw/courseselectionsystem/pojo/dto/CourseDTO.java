package com.dhw.courseselectionsystem.pojo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CourseDTO {
    private Long id;
    private String courseCode;
    private String name;
    private BigDecimal credit;
    private Integer maxStudents;
    private Long teacherId;
    private Long semesterId;
    private String schedule;
    private Integer status;   // 0未开始 1进行中 2已结束
}