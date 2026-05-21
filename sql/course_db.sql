-- =============================================
-- 学生选课管理系统 - 数据库初始化脚本（含大量测试数据）
-- 数据库：MySQL 8.0+
-- 字符集：utf8mb4
-- =============================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `course_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `course_db`;

-- =============================================
-- 1. 学生表
-- =============================================
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  `student_no` varchar(20) NOT NULL COMMENT '学号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` tinyint DEFAULT NULL COMMENT '性别 0-女 1-男',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  UNIQUE KEY `uk_student_no` (`student_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- =============================================
-- 2. 教师表
-- =============================================
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  `teacher_no` varchar(20) NOT NULL COMMENT '工号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `title` varchar(50) DEFAULT NULL COMMENT '职称',
  `dept` varchar(100) DEFAULT NULL COMMENT '院系',
  UNIQUE KEY `uk_teacher_no` (`teacher_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师表';

-- =============================================
-- 3. 学期表
-- =============================================
DROP TABLE IF EXISTS `semester`;
CREATE TABLE `semester` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '学期名称',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `is_current` tinyint DEFAULT 0 COMMENT '是否当前学期 0-否 1-是',
  `enrollment_start` datetime DEFAULT NULL COMMENT '选课开始时间',
  `enrollment_end` datetime DEFAULT NULL COMMENT '选课结束时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学期表';

-- =============================================
-- 4. 课程表
-- =============================================
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  `course_code` varchar(20) NOT NULL COMMENT '课程编号',
  `name` varchar(100) NOT NULL COMMENT '课程名称',
  `credit` decimal(3,1) NOT NULL COMMENT '学分',
  `max_students` int NOT NULL COMMENT '最大选课人数',
  `current_students` int DEFAULT 0 COMMENT '当前已选人数',
  `teacher_id` bigint DEFAULT NULL COMMENT '任课教师ID',
  `semester_id` bigint DEFAULT NULL COMMENT '所属学期ID',
  `schedule` varchar(200) DEFAULT NULL COMMENT '上课时间地点',
  `status` tinyint DEFAULT 0 COMMENT '课程状态 0-未开始 1-进行中 2-已结束',
  FOREIGN KEY (`teacher_id`) REFERENCES `teacher`(`id`) ON DELETE SET NULL,
  FOREIGN KEY (`semester_id`) REFERENCES `semester`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- =============================================
-- 5. 选课记录表（已移除唯一索引，允许退课后重选）
-- =============================================
DROP TABLE IF EXISTS `course_selection`;
CREATE TABLE `course_selection` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `selection_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  `status` tinyint DEFAULT 0 COMMENT '状态 0-已选 1-退课',
  `score` decimal(5,2) DEFAULT NULL COMMENT '成绩',
  FOREIGN KEY (`student_id`) REFERENCES `student`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课记录表';

-- =============================================
-- 6. 用户表（统一登录）
-- =============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名（学号/工号/admin）',
  `password` varchar(255) NOT NULL COMMENT '密码（MD5加密）',
  `role` enum('student','teacher','admin') NOT NULL COMMENT '角色',
  `ref_id` bigint DEFAULT NULL COMMENT '关联 student.id 或 teacher.id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =============================================
-- 插入大量测试数据
-- =============================================

-- 1. 学生（增加至 20 人）
INSERT INTO `student` (`student_no`, `name`, `gender`, `major`, `class_name`, `phone`) VALUES
('S001', '张三', 1, '计算机科学', '计科1班', '13800000001'),
('S002', '李四', 1, '计算机科学', '计科1班', '13800000002'),
('S003', '王芳', 2, '计算机科学', '计科1班', '13800000003'),
('S004', '赵磊', 1, '软件工程', '软工1班', '13800000004'),
('S005', '陈静', 2, '软件工程', '软工1班', '13800000005'),
('S006', '周涛', 1, '大数据', '大数据1班', '13800000006'),
('S007', '吴迪', 2, '大数据', '大数据1班', '13800000007'),
('S008', '郑爽', 2, '计算机科学', '计科2班', '13800000008'),
('S009', '孙阳', 1, '计算机科学', '计科2班', '13800000009'),
('S010', '林晨', 1, '软件工程', '软工2班', '13800000010'),
('S011', '郭敏', 2, '软件工程', '软工2班', '13800000011'),
('S012', '唐雅', 2, '大数据', '大数据2班', '13800000012'),
('S013', '沈强', 1, '大数据', '大数据2班', '13800000013'),
('S014', '宋阳', 1, '计算机科学', '计科1班', '13800000014'),
('S015', '秦岚', 2, '计算机科学', '计科2班', '13800000015');

-- 2. 教师（增加至 6 人）
INSERT INTO `teacher` (`teacher_no`, `name`, `title`, `dept`) VALUES
('T001', '陈老师', '副教授', '计算机学院'),
('T002', '刘老师', '讲师', '人工智能学院'),
('T003', '王教授', '教授', '计算机学院'),
('T004', '李博士', '讲师', '软件学院'),
('T005', '张老师', '副教授', '信息学院'),
('T006', '赵老师', '讲师', '计算机学院');

-- 3. 学期（增加至 3 个）
INSERT INTO `semester` (`name`, `start_date`, `end_date`, `is_current`, `enrollment_start`, `enrollment_end`) VALUES
('2024-2025学年第一学期', '2024-09-01', '2025-01-15', 0, '2024-08-20 00:00:00', '2024-09-10 23:59:59'),
('2024-2025学年第二学期', '2025-02-20', '2025-07-10', 0, '2025-02-10 00:00:00', '2025-02-25 23:59:59'),
('2025-2026学年第一学期', '2025-09-01', '2026-01-15', 1, '2025-08-20 00:00:00', '2025-09-15 23:59:59');

-- 4. 课程（每个学期至少 6 门，共 18 门）
-- 学期1的课程 (id 1-6)
INSERT INTO `course` (`course_code`, `name`, `credit`, `max_students`, `current_students`, `teacher_id`, `semester_id`, `schedule`, `status`) VALUES
('CS101', 'Java程序设计', 4.0, 40, 25, 1, 1, '周一 第1-2节 教学楼A101', 2),
('CS102', '数据结构', 3.5, 35, 28, 3, 1, '周二 第3-4节 教学楼A102', 2),
('CS103', '操作系统', 3.0, 30, 30, 5, 1, '周三 第5-6节 教学楼A103', 2),
('SE101', '软件工程', 2.5, 35, 20, 4, 1, '周四 第1-2节 教学楼B101', 2),
('AI101', '人工智能导论', 3.0, 40, 32, 2, 1, '周五 第3-4节 教学楼C101', 2),
('DS101', '数据库原理', 3.5, 35, 33, 1, 1, '周一 第5-6节 教学楼A104', 2);

-- 学期2的课程 (id 7-12)
INSERT INTO `course` (`course_code`, `name`, `credit`, `max_students`, `current_students`, `teacher_id`, `semester_id`, `schedule`, `status`) VALUES
('CS201', 'SpringBoot开发', 3.5, 35, 15, 3, 2, '周一 第1-2节 教学楼A201', 2),
('CS202', '微服务架构', 3.0, 30, 12, 1, 2, '周二 第3-4节 教学楼A202', 2),
('CS203', '前端框架', 2.5, 30, 10, 5, 2, '周三 第5-6节 教学楼A203', 2),
('SE201', '项目管理', 2.0, 35, 8, 4, 2, '周四 第1-2节 教学楼B201', 2),
('AI201', '机器学习', 3.5, 40, 20, 2, 2, '周五 第3-4节 教学楼C201', 2),
('DS201', '大数据技术', 3.0, 35, 18, 6, 2, '周一 第5-6节 教学楼A204', 2);

-- 学期3的课程（当前学期）(id 13-18)
INSERT INTO `course` (`course_code`, `name`, `credit`, `max_students`, `current_students`, `teacher_id`, `semester_id`, `schedule`, `status`) VALUES
('CS301', '云原生开发', 3.0, 35, 5, 3, 3, '周一 第1-2节 教学楼A301', 1),
('CS302', '容器技术', 2.5, 30, 0, 1, 3, '周二 第3-4节 教学楼A302', 1),
('CS303', 'DevOps实践', 2.5, 30, 2, 5, 3, '周三 第5-6节 教学楼A303', 1),
('SE301', '敏捷开发', 2.0, 35, 8, 4, 3, '周四 第1-2节 教学楼B301', 1),
('AI301', '深度学习', 3.5, 40, 12, 2, 3, '周五 第3-4节 教学楼C301', 1),
('DS301', '数据挖掘', 3.0, 35, 7, 6, 3, '周一 第5-6节 教学楼A304', 1);

-- 更新当前学期的已选人数（模拟已有选课记录）
UPDATE `course` SET `current_students` = 8 WHERE `id` = 13;
UPDATE `course` SET `current_students` = 15 WHERE `id` = 16;

-- 5. 选课记录（为每个学期创建丰富的选课数据）
-- 学期1的选课记录（很多已结课，有成绩）
INSERT INTO `course_selection` (`student_id`, `course_id`, `selection_time`, `status`, `score`) VALUES
-- 学生 S001 ~ S010 选课并已有成绩
(1,1, '2024-08-25 10:00:00', 0, 88.5),
(1,2, '2024-08-25 10:05:00', 0, 92.0),
(1,3, '2024-08-25 10:10:00', 0, 78.0),
(2,1, '2024-08-25 11:00:00', 0, 85.0),
(2,2, '2024-08-25 11:05:00', 0, 90.0),
(2,4, '2024-08-25 11:10:00', 0, 88.0),
(3,1, '2024-08-26 09:00:00', 0, 76.5),
(3,5, '2024-08-26 09:05:00', 0, 89.0),
(4,2, '2024-08-26 10:00:00', 0, 84.0),
(4,3, '2024-08-26 10:05:00', 0, 91.0),
(4,6, '2024-08-26 10:10:00', 0, 87.5),
(5,1, '2024-08-27 14:00:00', 0, 79.0),
(5,4, '2024-08-27 14:05:00', 0, 85.0),
(5,5, '2024-08-27 14:10:00', 0, 93.0),
(6,2, '2024-08-28 09:30:00', 0, 82.0),
(6,3, '2024-08-28 09:35:00', 0, 88.5),
(7,1, '2024-08-28 10:00:00', 0, 90.0),
(7,6, '2024-08-28 10:05:00', 0, 84.0),
(8,4, '2024-08-29 11:00:00', 0, 77.0),
(8,5, '2024-08-29 11:05:00', 0, 89.5),
(9,1, '2024-08-29 14:00:00', 0, 95.0),
(9,2, '2024-08-29 14:05:00', 0, 88.0),
(10,3, '2024-08-30 10:00:00', 0, 70.0),
(10,6, '2024-08-30 10:05:00', 0, 82.0);

-- 学期2的选课记录（部分结课有成绩，部分未结课无成绩）
INSERT INTO `course_selection` (`student_id`, `course_id`, `selection_time`, `status`, `score`) VALUES
(1,7, '2025-02-15 10:00:00', 0, 91.0),
(1,8, '2025-02-15 10:05:00', 0, 87.5),
(2,7, '2025-02-15 11:00:00', 0, 85.0),
(2,9, '2025-02-15 11:05:00', 0, 88.0),
(3,7, '2025-02-16 09:00:00', 0, 79.0),
(3,10, '2025-02-16 09:05:00', 0, 92.0),
(4,8, '2025-02-16 10:00:00', 0, 86.5),
(4,11, '2025-02-16 10:05:00', 0, 90.0),
(5,7, '2025-02-17 14:00:00', 0, 88.0),
(5,12, '2025-02-17 14:05:00', 0, 84.0),
(6,9, '2025-02-17 15:00:00', 0, 91.5),
(6,10, '2025-02-17 15:05:00', 0, 87.0),
(7,7, '2025-02-18 09:30:00', 0, 90.0),
(7,11, '2025-02-18 09:35:00', 0, 85.0),
(8,8, '2025-02-18 10:00:00', 0, 77.0),
(8,12, '2025-02-18 10:05:00', 0, 89.0),
(9,7, '2025-02-19 11:00:00', 0, 94.0),
(9,9, '2025-02-19 11:05:00', 0, 88.5),
(10,10, '2025-02-19 14:00:00', 0, 73.0),
(10,11, '2025-02-19 14:05:00', 0, 86.0),
(11,7, '2025-02-20 10:00:00', 0, NULL),   -- 未出成绩
(11,8, '2025-02-20 10:05:00', 0, NULL),
(12,9, '2025-02-20 11:00:00', 0, NULL),
(12,12, '2025-02-20 11:05:00', 0, NULL),
(13,7, '2025-02-21 09:00:00', 0, NULL),
(13,10, '2025-02-21 09:05:00', 0, NULL),
(14,8, '2025-02-21 10:00:00', 0, NULL),
(14,11, '2025-02-21 10:05:00', 0, NULL),
(15,9, '2025-02-22 14:00:00', 0, NULL),
(15,12, '2025-02-22 14:05:00', 0, NULL);

-- 学期3的选课记录（当前学期，大部分无成绩）
INSERT INTO `course_selection` (`student_id`, `course_id`, `selection_time`, `status`, `score`) VALUES
(1,13, '2025-08-22 10:00:00', 0, NULL),
(1,14, '2025-08-22 10:05:00', 0, NULL),
(2,13, '2025-08-22 11:00:00', 0, NULL),
(2,15, '2025-08-22 11:05:00', 0, NULL),
(3,13, '2025-08-23 09:00:00', 0, NULL),
(3,16, '2025-08-23 09:05:00', 0, NULL),
(4,14, '2025-08-23 10:00:00', 0, NULL),
(4,17, '2025-08-23 10:05:00', 0, NULL),
(5,13, '2025-08-24 14:00:00', 0, NULL),
(5,18, '2025-08-24 14:05:00', 0, NULL),
(6,14, '2025-08-24 15:00:00', 0, NULL),
(6,15, '2025-08-24 15:05:00', 0, NULL),
(7,13, '2025-08-25 09:30:00', 0, NULL),
(7,16, '2025-08-25 09:35:00', 0, NULL),
(8,14, '2025-08-25 10:00:00', 0, NULL),
(8,17, '2025-08-25 10:05:00', 0, NULL),
(9,13, '2025-08-26 11:00:00', 0, NULL),
(9,18, '2025-08-26 11:05:00', 0, NULL),
(10,15, '2025-08-26 14:00:00', 0, NULL),
(10,16, '2025-08-26 14:05:00', 0, NULL),
(11,13, '2025-08-27 10:00:00', 0, NULL),
(11,14, '2025-08-27 10:05:00', 0, NULL),
(12,15, '2025-08-27 11:00:00', 0, NULL),
(12,16, '2025-08-27 11:05:00', 0, NULL),
(13,13, '2025-08-28 09:00:00', 0, NULL),
(13,17, '2025-08-28 09:05:00', 0, NULL),
(14,14, '2025-08-28 10:00:00', 0, NULL),
(14,18, '2025-08-28 10:05:00', 0, NULL),
(15,13, '2025-08-29 14:00:00', 0, NULL),
(15,15, '2025-08-29 14:05:00', 0, NULL);

-- 6. 用户表（为学生和教师创建账户，密码均为 123456 的 MD5）
-- 已存在的学生 S001~S015
INSERT INTO `user` (`username`, `password`, `role`, `ref_id`) VALUES
('S001', 'e10adc3949ba59abbe56e057f20f883e', 'student', 1),
('S002', 'e10adc3949ba59abbe56e057f20f883e', 'student', 2),
('S003', 'e10adc3949ba59abbe56e057f20f883e', 'student', 3),
('S004', 'e10adc3949ba59abbe56e057f20f883e', 'student', 4),
('S005', 'e10adc3949ba59abbe56e057f20f883e', 'student', 5),
('S006', 'e10adc3949ba59abbe56e057f20f883e', 'student', 6),
('S007', 'e10adc3949ba59abbe56e057f20f883e', 'student', 7),
('S008', 'e10adc3949ba59abbe56e057f20f883e', 'student', 8),
('S009', 'e10adc3949ba59abbe56e057f20f883e', 'student', 9),
('S010', 'e10adc3949ba59abbe56e057f20f883e', 'student', 10),
('S011', 'e10adc3949ba59abbe56e057f20f883e', 'student', 11),
('S012', 'e10adc3949ba59abbe56e057f20f883e', 'student', 12),
('S013', 'e10adc3949ba59abbe56e057f20f883e', 'student', 13),
('S014', 'e10adc3949ba59abbe56e057f20f883e', 'student', 14),
('S015', 'e10adc3949ba59abbe56e057f20f883e', 'student', 15),
-- 教师
('T001', 'e10adc3949ba59abbe56e057f20f883e', 'teacher', 1),
('T002', 'e10adc3949ba59abbe56e057f20f883e', 'teacher', 2),
('T003', 'e10adc3949ba59abbe56e057f20f883e', 'teacher', 3),
('T004', 'e10adc3949ba59abbe56e057f20f883e', 'teacher', 4),
('T005', 'e10adc3949ba59abbe56e057f20f883e', 'teacher', 5),
('T006', 'e10adc3949ba59abbe56e057f20f883e', 'teacher', 6),
-- 管理员
('admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin', NULL);

COMMIT;

-- 检查数据量
SELECT 'student' as table_name, COUNT(*) FROM student
UNION SELECT 'teacher', COUNT(*) FROM teacher
UNION SELECT 'semester', COUNT(*) FROM semester
UNION SELECT 'course', COUNT(*) FROM course
UNION SELECT 'course_selection', COUNT(*) FROM course_selection
UNION SELECT 'user', COUNT(*) FROM user;