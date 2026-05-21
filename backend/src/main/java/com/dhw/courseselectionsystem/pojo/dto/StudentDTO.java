package com.dhw.courseselectionsystem.pojo.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private Long id;
    private String studentNo;
    private String name;
    private Integer gender;   // 0女1男
    private String major;
    private String className;
    private String phone;
    private String newPassword;  // 重置密码时使用
}