package com.dhw.courseselectionsystem.pojo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
    private Long refId;
    private LocalDateTime createTime;
}