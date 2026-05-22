package com.dhw.courseselectionsystem.mapper;

import com.dhw.courseselectionsystem.pojo.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StudentMapper {

    // 分页条件查询
    List<Student> findAll(@Param("studentNo") String studentNo,
                          @Param("name") String name,
                          @Param("major") String major,
                          @Param("className") String className);

    // 根据ID查询
    Student findById(@Param("id") Long id);

    // 根据学号查询
    Student findByStudentNo(String studentNo);

    // 新增
    int insert(Student student);

    // 修改
    int update(Student student);

    // 删除
    int deleteById(Long id);
}