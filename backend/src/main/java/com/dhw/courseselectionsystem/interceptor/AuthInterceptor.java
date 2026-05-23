package com.dhw.courseselectionsystem.interceptor;

import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.utils.JwtUtils;
import com.dhw.courseselectionsystem.utils.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            writeError(response, "请先登录");
            return false;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Claims claims = JwtUtils.parseJwt(token);
            Long userId = claims.get("userId", Long.class);
            String role = claims.get("role", String.class);
            Long refId = claims.get("refId", Long.class);
            UserContext.UserInfo userInfo = new UserContext.UserInfo(userId, role, refId);
            UserContext.setUser(userInfo);

            // ========== 角色路径权限控制 ==========
            String uri = request.getRequestURI();
            if (uri.startsWith("/api/admin")) {
                if (!"admin".equals(role)) {
                    writeError(response, "权限不足，需要管理员角色");
                    return false;
                }
            } else if (uri.startsWith("/api/teacher")) {
                if (!"teacher".equals(role)) {
                    writeError(response, "权限不足，需要教师角色");
                    return false;
                }
            }
            // 其他路径（如 /api/courses, /api/selections 等）不做角色限制，由具体接口内部判断
            return true;
        } catch (Exception e) {
            writeError(response, "token无效或已过期");
            return false;
        }
    }

    private void writeError(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(Result.error(msg)));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
