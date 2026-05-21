package com.dhw.courseselectionsystem.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MySelectionVO {
    private Long courseId;
    private String courseName;
    private String teacherName;
    private BigDecimal credit;
    private String schedule;
    private LocalDateTime selectionTime;
    private BigDecimal score;  // 可能为 null
}