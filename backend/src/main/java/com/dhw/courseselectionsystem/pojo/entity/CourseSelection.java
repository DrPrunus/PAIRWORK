package com.dhw.courseselectionsystem.pojo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseSelection {
    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDateTime selectionTime;
    private Integer status;
    private BigDecimal score;
}