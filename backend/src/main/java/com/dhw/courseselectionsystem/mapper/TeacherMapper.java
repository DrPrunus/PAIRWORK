package com.dhw.courseselectionsystem.mapper;

import com.dhw.courseselectionsystem.pojo.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {

    // 分页条件查询
    @Select("<script>" +
            "SELECT * FROM teacher" +
            " <where>" +
            "   <if test='teacherNo != null and teacherNo != \"\"'> AND teacher_no LIKE CONCAT('%', #{teacherNo}, '%') </if>" +
            "   <if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%') </if>" +
            "   <if test='dept != null and dept != \"\"'> AND dept LIKE CONCAT('%', #{dept}, '%') </if>" +
            " </where>" +
            " ORDER BY id DESC" +
            "</script>")
    List<Teacher> findAll(@Param("teacherNo") String teacherNo,
                          @Param("name") String name,
                          @Param("dept") String dept);

    @Select("SELECT * FROM teacher WHERE id = #{id}")
    Teacher findById(Long id);

    @Select("SELECT * FROM teacher WHERE teacher_no = #{teacherNo}")
    Teacher findByTeacherNo(String teacherNo);

    @Insert("INSERT INTO teacher (teacher_no, name, title, dept) VALUES (#{teacherNo}, #{name}, #{title}, #{dept})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Teacher teacher);

    @Update("UPDATE teacher SET teacher_no = #{teacherNo}, name = #{name}, title = #{title}, dept = #{dept} WHERE id = #{id}")
    int update(Teacher teacher);

    @Delete("DELETE FROM teacher WHERE id = #{id}")
    int deleteById(Long id);
}