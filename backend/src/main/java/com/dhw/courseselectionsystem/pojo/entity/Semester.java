package com.dhw.courseselectionsystem.pojo.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Semester {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer isCurrent;
    private LocalDateTime enrollmentStart;
    private LocalDateTime enrollmentEnd;
}