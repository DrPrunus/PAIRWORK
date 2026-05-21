package com.dhw.courseselectionsystem.service.impl;

import com.dhw.courseselectionsystem.exception.BusinessException;
import com.dhw.courseselectionsystem.mapper.UserMapper;
import com.dhw.courseselectionsystem.pojo.entity.User;
import com.dhw.courseselectionsystem.service.UserService;
import com.dhw.courseselectionsystem.utils.JwtUtils;
import com.dhw.courseselectionsystem.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户名不存在");
        }
        String md5Password = MD5Utils.md5(password);
        if (!user.getPassword().equals(md5Password)) {
            throw new BusinessException("密码错误");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        claims.put("refId", user.getRefId());
        String token = JwtUtils.generateJwt(claims);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("role", user.getRole());
        result.put("refId", user.getRefId());
        return result;
    }
}