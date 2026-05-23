package com.dhw.courseselectionsystem.service.impl;

import com.dhw.courseselectionsystem.exception.BusinessException;
import com.dhw.courseselectionsystem.mapper.SemesterMapper;
import com.dhw.courseselectionsystem.pojo.entity.Semester;
import com.dhw.courseselectionsystem.pojo.vo.SemesterVO;
import com.dhw.courseselectionsystem.service.SemesterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterMapper semesterMapper;

    @Override
    public List<SemesterVO> listAll() {
        List<Semester> list = semesterMapper.findAll();
        return list.stream().map(s -> {
            SemesterVO vo = new SemesterVO();
            BeanUtils.copyProperties(s, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public SemesterVO getById(Long id) {
        Semester semester = semesterMapper.findById(id);
        if (semester == null) {
            throw new BusinessException("学期不存在");
        }
        SemesterVO vo = new SemesterVO();
        BeanUtils.copyProperties(semester, vo);
        return vo;
    }

    @Override
    @Transactional
    public void addSemester(Semester semester) {
        // 如果当前学期标记为1，需要先清除其他学期的当前标记
        if (semester.getIsCurrent() == 1) {
            semesterMapper.clearCurrentFlag();
        }
        int rows = semesterMapper.insert(semester);
        if (rows == 0) {
            throw new BusinessException("添加学期失败");
        }
    }

    @Override
    @Transactional
    public void updateSemester(Semester semester) {
        Semester exist = semesterMapper.findById(semester.getId());
        if (exist == null) {
            throw new BusinessException("学期不存在");
        }
        // 如果需要设置为当前，则先清除其他学期的当前标记
        if (semester.getIsCurrent() == 1) {
            semesterMapper.clearCurrentFlag();
        }
        int rows = semesterMapper.update(semester);
        if (rows == 0) {
            throw new BusinessException("更新学期失败");
        }
    }

    @Override
    @Transactional
    public void deleteSemester(Long id) {
        Semester semester = semesterMapper.findById(id);
        if (semester == null) {
            throw new BusinessException("学期不存在");
        }
        // 如果该学期是当前学期，不允许删除（可选）
        if (semester.getIsCurrent() == 1) {
            throw new BusinessException("当前学期不允许删除");
        }
        // 可选：检查该学期下是否有课程关联，如果有则禁止删除（可以后续实现）
        int rows = semesterMapper.deleteById(id);
        if (rows == 0) {
            throw new BusinessException("删除学期失败");
        }
    }
}
