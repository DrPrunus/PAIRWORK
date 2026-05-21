package com.dhw.courseselectionsystem.utils;

public class UserContext {
    private static final ThreadLocal<UserInfo> CURRENT_USER = new ThreadLocal<>();

    public static void setUser(UserInfo userInfo) {
        CURRENT_USER.set(userInfo);
    }

    public static UserInfo getUser() {
        return CURRENT_USER.get();
    }

    public static void removeUser() {
        CURRENT_USER.remove();
    }

    public static class UserInfo {
        private Long userId;
        private String role;
        private Long refId;  // student.id 或 teacher.id

        public UserInfo(Long userId, String role, Long refId) {
            this.userId = userId;
            this.role = role;
            this.refId = refId;
        }
        // getter / setter
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getRefId() { return refId; }
        public void setRefId(Long refId) { this.refId = refId; }
    }
}