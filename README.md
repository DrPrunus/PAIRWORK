# 学生选课管理系统

## 系统部署说明

| 学号 | 姓名 |
|------|------|
| 233401010116 | 张溯峻 |
| 233401010108 | 蒋佳瑶 |

**项目地址**：https://github.com/DrPrunus/PAIRWORK

---

## 技术栈声明

- **后端**：Spring Boot 2.7 + MyBatis + MySQL 8.0 + JWT (jjwt 0.11.5) + Maven
- **前端**：Vue 3 (Composition API) + Vite 4 + Element Plus + Pinia + Vue Router 4 + Axios

---

## 本地部署步骤

### 1. 数据库初始化

```sql
CREATE DATABASE IF NOT EXISTS course_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE course_db;

-- 执行建表及测试数据脚本
source sql/course_db.sql;

-- 核心表：user、student、teacher、course、selection、department
-- 测试数据：admin 管理员、T001 教师、S001 学生及若干课程
```

### 2. 后端启动

```bash
cd backend

# 修改 src/main/resources/application.yml 中的数据库连接信息
# 需修改: spring.datasource.url, spring.datasource.username, spring.datasource.password
# 可选: 修改 JwtUtils.java 中的 SIGN_KEY 自定义JWT密钥

mvn clean install -DskipTests  # Maven 编译打包（跳过测试）

mvn spring-boot:run  # 启动 Spring Boot 应用（默认端口 8080）

# 或打包运行:
# java -jar target/course-selection-system-0.0.1-SNAPSHOT.jar
```

### 3. 前端启动

```bash
cd frontend

npm install  # 安装前端依赖（需 Node.js 18+，npm 9+）

npm run dev  # 启动 Vite 开发服务器（http://localhost:5173）

# 访问 http://localhost:5173 进入登录页面
# 可在 .env.development 中修改 VITE_API_BASE_URL 指向后端地址
```

### 4. 测试账号

| 角色 | 账号 | 密码 | 说明 |
|------|------|------|------|
| 管理员 | `admin` | `123456` | 系统内置管理员，可管理课程、选课统计、用户 |
| 教师 | `T001` | `123456` | `sql/course_db.sql` 预置，可管理课程和统计选课 |
| 学生 | `S001` | `123456` | `sql/course_db.sql` 预置，可浏览课程和进行选课 |
