package com.dhw.courseselectionsystem.pojo.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SemesterDTO {
    private Long id;               // 修改时传入，新增时为空
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer isCurrent;     // 0或1
    private LocalDateTime enrollmentStart;
    private LocalDateTime enrollmentEnd;
}