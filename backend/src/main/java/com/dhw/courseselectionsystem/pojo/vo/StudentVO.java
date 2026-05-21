package com.dhw.courseselectionsystem.pojo.vo;

import lombok.Data;

@Data
public class StudentVO {
    private Long id;
    private String studentNo;
    private String name;
    private Integer gender;
    private String major;
    private String className;
    private String phone;
}