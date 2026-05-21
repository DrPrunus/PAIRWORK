package com.dhw.courseselectionsystem.pojo.dto;

import lombok.Data;

@Data
public class TeacherDTO {
    private Long id;            // 修改时使用
    private String teacherNo;   // 工号
    private String name;
    private String title;
    private String dept;
    private String newPassword; // 重置密码时使用
}