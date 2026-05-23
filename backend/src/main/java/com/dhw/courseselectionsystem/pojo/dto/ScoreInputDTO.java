package com.dhw.courseselectionsystem.pojo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ScoreInputDTO {
    private Long courseId;
    private Long studentId;
    private BigDecimal score;
}
