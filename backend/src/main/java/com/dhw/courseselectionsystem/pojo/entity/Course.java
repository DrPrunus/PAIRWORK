package com.dhw.courseselectionsystem.pojo.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Course {
    private Long id;
    private String courseCode;
    private String name;
    private BigDecimal credit;
    private Integer maxStudents;
    private Integer currentStudents;
    private Long teacherId;
    private Long semesterId;
    private String schedule;
    private Integer status;  // 0未开始 1进行中 2已结束

    // 以下为非数据库字段，用于联查展示
    private String teacherName;
    private String semesterName;
}