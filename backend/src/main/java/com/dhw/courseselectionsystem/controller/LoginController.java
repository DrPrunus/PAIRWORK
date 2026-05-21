package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.LoginDTO;
import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> data = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return Result.success(data);
    }
}
