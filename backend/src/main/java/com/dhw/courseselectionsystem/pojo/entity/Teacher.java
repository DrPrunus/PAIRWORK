package com.dhw.courseselectionsystem.pojo.entity;

import lombok.Data;

@Data
public class Teacher {
    private Long id;
    private String teacherNo;
    private String name;
    private String title;
    private String dept;
}