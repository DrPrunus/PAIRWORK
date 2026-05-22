package com.dhw.courseselectionsystem.service.impl;

import com.dhw.courseselectionsystem.exception.BusinessException;
import com.dhw.courseselectionsystem.mapper.CourseSelectionMapper;
import com.dhw.courseselectionsystem.mapper.StudentMapper;
import com.dhw.courseselectionsystem.mapper.UserMapper;
import com.dhw.courseselectionsystem.pojo.dto.StudentDTO;
import com.dhw.courseselectionsystem.pojo.entity.Student;
import com.dhw.courseselectionsystem.pojo.entity.User;
import com.dhw.courseselectionsystem.pojo.vo.StudentVO;
import com.dhw.courseselectionsystem.service.StudentService;
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
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Override
    public PageInfo<StudentVO> pageQuery(Integer pageNum, Integer pageSize, String studentNo, String name, String major, String className) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> list = studentMapper.findAll(studentNo, name, major, className);
        List<StudentVO> voList = list.stream().map(student -> {
            StudentVO vo = new StudentVO();
            BeanUtils.copyProperties(student, vo);
            return vo;
        }).collect(Collectors.toList());
        return new PageInfo<>(voList);
    }

    @Override
    @Transactional
    public void addStudent(StudentDTO dto) {
        // 检查学号是否已存在
        Student exist = studentMapper.findByStudentNo(dto.getStudentNo());
        if (exist != null) {
            throw new BusinessException("学号已存在");
        }
        Student student = new Student();
        BeanUtils.copyProperties(dto, student);
        studentMapper.insert(student);  // 插入后 student.getId() 有值

        // 创建对应的 user 账号，默认密码123456
        User user = new User();
        user.setUsername(dto.getStudentNo());
        user.setPassword(MD5Utils.md5("123456"));
        user.setRole("student");
        user.setRefId(student.getId());
        userMapper.insertStudentUser(user);
    }

    @Override
    @Transactional
    public void updateStudent(StudentDTO dto) {
        Student student = studentMapper.findById(dto.getId());
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        // 如果修改了学号，需要检查新学号是否已被其他学生使用
        if (!student.getStudentNo().equals(dto.getStudentNo())) {
            Student byNo = studentMapper.findByStudentNo(dto.getStudentNo());
            if (byNo != null && !byNo.getId().equals(dto.getId())) {
                throw new BusinessException("学号已存在");
            }
            // 同步修改 user 的 username
            userMapper.updateUsernameByStudentRefId(dto.getId(), dto.getStudentNo());
        }
        BeanUtils.copyProperties(dto, student);
        studentMapper.update(student);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        // 检查是否有选课记录
        int count = courseSelectionMapper.countSelectionsByStudentId(id);
        if (count > 0) {
            throw new BusinessException("该学生仍有选课记录，无法删除");
        }
        // 删除 user 记录
        userMapper.deleteByRefIdAndRole(id, "student");
        // 删除 student 记录
        studentMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void resetPassword(Long id, String newPassword) {
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        String encrypted = MD5Utils.md5(newPassword);
        int rows = userMapper.updatePasswordByStudentRefId(id, encrypted);
        if (rows == 0) {
            throw new BusinessException("重置密码失败");
        }
    }
}