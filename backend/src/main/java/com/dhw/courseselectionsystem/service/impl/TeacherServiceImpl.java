package com.dhw.courseselectionsystem.service.impl;

import com.dhw.courseselectionsystem.exception.BusinessException;
import com.dhw.courseselectionsystem.mapper.CourseMapper;
import com.dhw.courseselectionsystem.mapper.TeacherMapper;
import com.dhw.courseselectionsystem.mapper.UserMapper;
import com.dhw.courseselectionsystem.pojo.dto.TeacherDTO;
import com.dhw.courseselectionsystem.pojo.entity.Teacher;
import com.dhw.courseselectionsystem.pojo.entity.User;
import com.dhw.courseselectionsystem.pojo.vo.TeacherVO;
import com.dhw.courseselectionsystem.service.TeacherService;
import com.dhw.courseselectionsystem.utils.MD5Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public PageInfo<TeacherVO> pageQuery(Integer pageNum, Integer pageSize, String teacherNo, String name, String dept) {
        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> list = teacherMapper.findAll(teacherNo, name, dept);
        List<TeacherVO> voList = list.stream().map(teacher -> {
            TeacherVO vo = new TeacherVO();
            BeanUtils.copyProperties(teacher, vo);
            return vo;
        }).collect(Collectors.toList());
        return new PageInfo<>(voList);
    }

    @Override
    @Transactional
    public void addTeacher(TeacherDTO dto) {
        // 检查工号是否已存在
        Teacher exist = teacherMapper.findByTeacherNo(dto.getTeacherNo());
        if (exist != null) {
            throw new BusinessException("工号已存在");
        }
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(dto, teacher);
        teacherMapper.insert(teacher); // 插入后 teacher.getId() 有值

        // 创建对应的 user 账号
        User user = new User();
        user.setUsername(dto.getTeacherNo());
        user.setPassword(MD5Utils.md5("123456")); // 默认密码
        user.setRole("teacher");
        user.setRefId(teacher.getId());
        userMapper.insertTeacherUser(user);
    }

    @Override
    @Transactional
    public void updateTeacher(TeacherDTO dto) {
        Teacher teacher = teacherMapper.findById(dto.getId());
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }
        // 如果修改了工号，需要检查新工号是否已被其他教师使用
        if (!teacher.getTeacherNo().equals(dto.getTeacherNo())) {
            Teacher byNo = teacherMapper.findByTeacherNo(dto.getTeacherNo());
            if (byNo != null && !byNo.getId().equals(dto.getId())) {
                throw new BusinessException("工号已存在");
            }
            // 同步修改 user 的 username
            userMapper.updateUsernameByRefId(dto.getId(), "teacher", dto.getTeacherNo()); // 需在 UserMapper 中补充
        }
        BeanUtils.copyProperties(dto, teacher);
        teacherMapper.update(teacher);
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherMapper.findById(id);
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }
        // 检查是否有关联课程
        int count = courseMapper.countByTeacherId(id);
        if (count > 0) {
            throw new BusinessException("该教师仍有任课课程，无法删除");
        }
        // 删除 user 记录
        userMapper.deleteByRefIdAndRole(id, "teacher");
        // 删除 teacher 记录
        teacherMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void resetPassword(Long id, String newPassword) {
        Teacher teacher = teacherMapper.findById(id);
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }
        String encrypted = MD5Utils.md5(newPassword);
        int rows = userMapper.updatePasswordByTeacherRefId(id, encrypted);
        if (rows == 0) {
            throw new BusinessException("重置密码失败");
        }
    }
}