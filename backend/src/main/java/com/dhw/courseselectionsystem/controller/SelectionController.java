package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.pojo.dto.SelectionDTO;
import com.dhw.courseselectionsystem.pojo.vo.MySelectionVO;
import com.dhw.courseselectionsystem.pojo.vo.ScoreVO;
import com.dhw.courseselectionsystem.service.CourseSelectionService;
import com.dhw.courseselectionsystem.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;

    @PostMapping("/selections")
    public Result<?> selectCourse(@RequestBody SelectionDTO selectionDTO) {
        UserContext.UserInfo user = UserContext.getUser();
        if (user == null || !"student".equals(user.getRole())) {
            return Result.error("只有学生可以选课");
        }
        Long studentId = user.getRefId();  // 对应 student 表的 id
        courseSelectionService.selectCourse(studentId, selectionDTO.getCourseId());
        return Result.success("选课成功");
    }

    @DeleteMapping("/selections/{courseId}")
    public Result<?> cancelCourse(@PathVariable Long courseId) {
        UserContext.UserInfo user = UserContext.getUser();
        if (user == null || !"student".equals(user.getRole())) {
            return Result.error("只有学生可以退课");
        }
        Long studentId = user.getRefId();
        courseSelectionService.cancelCourse(studentId, courseId);
        return Result.success("退课成功");
    }



    @GetMapping("/selections/my")
    public Result<List<MySelectionVO>> getMySelections() {
        UserContext.UserInfo user = UserContext.getUser();
        if (user == null || !"student".equals(user.getRole())) {
            return Result.error("只有学生可以查看选课列表");
        }
        Long studentId = user.getRefId();
        List<MySelectionVO> list = courseSelectionService.getMySelections(studentId);
        return Result.success(list);
    }


    @GetMapping("/scores/my")
    public Result<List<ScoreVO>> getMyScores() {
        UserContext.UserInfo user = UserContext.getUser();
        if (user == null || !"student".equals(user.getRole())) {
            return Result.error("只有学生可以查看成绩");
        }
        Long studentId = user.getRefId();
        List<ScoreVO> list = courseSelectionService.getMyScores(studentId);
        return Result.success(list);
    }

}
