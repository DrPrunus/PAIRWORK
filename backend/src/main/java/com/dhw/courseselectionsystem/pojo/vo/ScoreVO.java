package com.dhw.courseselectionsystem.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ScoreVO {
    private String courseName;
    private BigDecimal credit;
    private BigDecimal score;
    private String semesterName;
}
