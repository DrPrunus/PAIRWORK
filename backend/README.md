[README.md](https://github.com/user-attachments/files/28093961/README.md)
# 学生选课管理系统 - 后端

## 项目简介
基于 Spring Boot + MyBatis + MySQL 的学生选课管理系统后端，提供学生、教师、管理员三端的 RESTful API，支持选课/退课、成绩管理、后台管理等功能。

## 技术栈
- Java 17
- Spring Boot 2.7.x
- MyBatis
- MySQL 8.0
- JWT (jjwt 0.11.5)
- PageHelper 分页
- Lombok
- Maven

## 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

## 快速开始

### 1. 克隆项目
```bash
git clone https://gitee.com/deng-hao-wen/course-selection-system.git
cd course-selection-system/backend
```

### 2. 创建数据库
```sql
CREATE DATABASE course_db CHARACTER SET utf8mb4;
```
执行 `docs/schema.sql` 创建表并导入测试数据（见项目 `docs/` 目录）。

### 3. 修改配置
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/course_db?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```
> 如需修改 JWT 密钥，请在 `JwtUtils.java` 中更改 `SIGN_KEY`。

### 4. 启动项目
```bash
mvn spring-boot:run
```
默认端口 `8080`。

## 项目结构
```
src/main/java/com/dhw/courseselectionsystem/
├── controller/      # REST API 控制器（按角色划分）
├── service/         # 业务逻辑层
├── mapper/          # MyBatis Mapper 接口及 XML
├── pojo/            # 实体类、DTO、VO
├── config/          # 跨域配置、Web 配置
├── interceptor/     # JWT 认证拦截器
├── exception/       # 全局异常处理
├── utils/           # 工具类（JWT、MD5）
└── CourseSelectionSystemApplication.java
```

## API 文档
完整 API 接口列表请查看 [API.md](./API.md)（或 Swagger 地址：`http://localhost:8080/swagger-ui/index.html`）。  
主要接口分类：
- 公共：`POST /api/login`
- 学生端：`/api/courses`、`/api/selections/*`、`/api/scores/my`
- 教师端：`/api/teacher/courses`、`/api/teacher/courses/{id}/students`、`PUT /api/scores`
- 管理员端：`/api/admin/semesters`、`/api/admin/teachers`、`/api/admin/courses`、`/api/admin/students`

## 测试账号
| 角色   | 用户名  | 密码   |
|--------|--------|--------|
| 学生   | S001   | 123456 |
| 教师   | T001   | 123456 |
| 管理员 | admin  | 123456 |

## 核心功能
- **学生**：浏览课程、选课/退课、查看已选课程及成绩
- **教师**：查看任课课程、查看选课学生名单、录入/修改成绩
- **管理员**：管理学期、教师、课程、学生信息，重置密码

## 关键设计
- JWT 无状态认证，拦截器统一鉴权
- 选课使用乐观锁防止超卖，支持退课后重选（复活记录）
- 时间冲突检测（按上课时间字符串）
- 全局异常处理与统一响应格式
