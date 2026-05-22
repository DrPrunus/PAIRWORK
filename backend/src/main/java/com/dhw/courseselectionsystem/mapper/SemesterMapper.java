package com.dhw.courseselectionsystem.mapper;

import com.dhw.courseselectionsystem.pojo.entity.Semester;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SemesterMapper {

    // 查询所有学期
    @Select("SELECT * FROM semester ORDER BY id DESC")
    List<Semester> findAll();

    // 根据ID查询学期
    @Select("SELECT * FROM semester WHERE id = #{id}")
    Semester findById(Long id);

    // 新增学期
    @Insert("INSERT INTO semester (name, start_date, end_date, is_current, enrollment_start, enrollment_end) " +
            "VALUES (#{name}, #{startDate}, #{endDate}, #{isCurrent}, #{enrollmentStart}, #{enrollmentEnd})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Semester semester);

    // 修改学期
    @Update("UPDATE semester SET name = #{name}, start_date = #{startDate}, end_date = #{endDate}, " +
            "is_current = #{isCurrent}, enrollment_start = #{enrollmentStart}, enrollment_end = #{enrollmentEnd} " +
            "WHERE id = #{id}")
    int update(Semester semester);

    // 删除学期
    @Delete("DELETE FROM semester WHERE id = #{id}")
    int deleteById(Long id);

    // 清除所有“当前学期”标记
    @Update("UPDATE semester SET is_current = 0")
    void clearCurrentFlag();

    // 设置指定ID为“当前学期”
    @Update("UPDATE semester SET is_current = 1 WHERE id = #{id}")
    void setCurrentFlag(Long id);

    // ==================== 你新加的方法，直接加在这里 ====================
    // 查询当前正在使用的学期
    @Select("SELECT * FROM semester WHERE is_current = 1 LIMIT 1")
    Semester findCurrentSemester();
}