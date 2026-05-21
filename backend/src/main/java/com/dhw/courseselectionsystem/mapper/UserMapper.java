package com.dhw.courseselectionsystem.mapper;

import com.dhw.courseselectionsystem.pojo.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);


    // 根据 refId 和 role 删除用户
    @Delete("DELETE FROM user WHERE ref_id = #{refId} AND role = #{role}")
    int deleteByRefIdAndRole(@Param("refId") Long refId, @Param("role") String role);

    // 更新教师账号密码
    @Update("UPDATE user SET password = #{password} WHERE ref_id = #{refId} AND role = 'teacher'")
    int updatePasswordByTeacherRefId(@Param("refId") Long refId, @Param("password") String password);

    // 插入教师用户
    @Insert("INSERT INTO user (username, password, role, ref_id) VALUES (#{username}, #{password}, 'teacher', #{refId})")
    int insertTeacherUser(User user);

    @Update("UPDATE user SET username = #{username} WHERE ref_id = #{refId} AND role = #{role}")
    int updateUsernameByRefId(@Param("refId") Long refId, @Param("role") String role, @Param("username") String username);



    // 更新学生账号密码
    @Update("UPDATE user SET password = #{password} WHERE ref_id = #{refId} AND role = 'student'")
    int updatePasswordByStudentRefId(@Param("refId") Long refId, @Param("password") String password);

    // 插入学生用户
    @Insert("INSERT INTO user (username, password, role, ref_id) VALUES (#{username}, #{password}, 'student', #{refId})")
    int insertStudentUser(User user);

    // 更新用户名（当学号修改时）
    @Update("UPDATE user SET username = #{username} WHERE ref_id = #{refId} AND role = 'student'")
    int updateUsernameByStudentRefId(@Param("refId") Long refId, @Param("username") String username);


}