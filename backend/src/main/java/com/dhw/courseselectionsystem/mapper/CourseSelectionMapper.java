package com.dhw.courseselectionsystem.mapper;

import com.dhw.courseselectionsystem.pojo.entity.CourseSelection;
import com.dhw.courseselectionsystem.pojo.vo.CourseStudentVO;
import com.dhw.courseselectionsystem.pojo.vo.MySelectionVO;
import com.dhw.courseselectionsystem.pojo.vo.ScoreVO;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CourseSelectionMapper {

    // 查询该学生该课程的“已选”状态记录（status=0）
    @Select("SELECT * FROM course_selection WHERE student_id = #{studentId} AND course_id = #{courseId} AND status = 0")
    CourseSelection findActiveByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    // 查询该学生该课程的任意状态记录（包括已退课）
    @Select("SELECT * FROM course_selection WHERE student_id = #{studentId} AND course_id = #{courseId} LIMIT 1")
    CourseSelection findAnyByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    // 插入新选课记录
    @Insert("INSERT INTO course_selection (student_id, course_id, selection_time, status) VALUES (#{studentId}, #{courseId}, #{selectionTime}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CourseSelection selection);

    // 复活已退课的记录：更新 status 为 0，并更新选课时间
    @Update("UPDATE course_selection SET status = 0, selection_time = #{selectionTime} WHERE student_id = #{studentId} AND course_id = #{courseId} AND status = 1")
    int reactivate(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("selectionTime") LocalDateTime selectionTime);

    // 退课：将 status 从 0 改为 1
    @Update("UPDATE course_selection SET status = 1 WHERE student_id = #{studentId} AND course_id = #{courseId} AND status = 0")
    int cancelByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);


    // 查询学生的已选课程列表（status=0），关联课程和教师信息
    List<MySelectionVO> findSelectedCoursesByStudentId(@Param("studentId") Long studentId);

    List<ScoreVO> findScoresByStudentId(@Param("studentId") Long studentId);

    // 更新选课记录的成绩（仅当该课程由该教师任教时才允许更新，联合验证在 Service 层做）
    int updateScore(@Param("courseId") Long courseId,
                    @Param("studentId") Long studentId,
                    @Param("score") BigDecimal score);


    List<CourseStudentVO> findStudentsByCourseId(@Param("courseId") Long courseId);

    @Select("SELECT COUNT(*) FROM course_selection WHERE student_id = #{studentId}")
    int countSelectionsByStudentId(Long studentId);
}