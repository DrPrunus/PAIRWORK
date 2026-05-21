package com.dhw.courseselectionsystem.pojo.entity;

import lombok.Data;

@Data
public class Student {
    private Long id;
    private String studentNo;
    private String name;
    private Integer gender;
    private String major;
    private String className;
    private String phone;
}