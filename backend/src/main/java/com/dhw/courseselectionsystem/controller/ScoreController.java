package com.dhw.courseselectionsystem.controller;

import com.dhw.courseselectionsystem.pojo.dto.Result;
import com.dhw.courseselectionsystem.pojo.dto.ScoreInputDTO;
import com.dhw.courseselectionsystem.service.CourseSelectionService;
import com.dhw.courseselectionsystem.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ScoreController {

    @Autowired
    private CourseSelectionService courseSelectionService;

    @PutMapping("/scores")
    public Result<?> inputScore(@RequestBody ScoreInputDTO scoreInput) {
        UserContext.UserInfo user = UserContext.getUser();
        if (user == null || !"teacher".equals(user.getRole())) {
            return Result.error("只有教师可以录入成绩");
        }
        Long teacherId = user.getRefId();  // 对应 teacher 表的 id
        courseSelectionService.inputScore(teacherId, 
                scoreInput.getCourseId(), 
                scoreInput.getStudentId(), 
                scoreInput.getScore());
        return Result.success("成绩录入成功");
    }
}