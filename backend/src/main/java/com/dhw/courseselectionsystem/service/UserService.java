package com.dhw.courseselectionsystem.service;

import java.util.Map;

public interface UserService {
    Map<String, Object> login(String username, String password);
}