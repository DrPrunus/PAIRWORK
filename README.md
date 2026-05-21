## 学生选课管理系统（Course Selection System）

基于前后端分离架构的高校选课管理平台，支持学生选课/退课、教师成绩录入、管理员后台管理等核心功能，适配高校教学管理场景，界面简洁易用、功能完善稳定。

---

### 项目信息

- **项目时间**：2026年5月

- **技术栈**：

- 后端：Spring Boot 2.7.x、MyBatis、MySQL 8.0、JWT (jjwt 0.11.5)、PageHelper、Lombok、Maven

- 前端：Vue 3 (Composition API)、Vue Router 4、Pinia、Element Plus、Axios、Day.js、Vite

- 工具：Git、Postman、Maven 3.6+、JDK 17、Node.js 18+、npm 9+

---



### 核心功能与亮点

- **架构设计**：采用前后端分离架构，后端分层设计（Controller、Service、Mapper），前端按角色模块化开发，保证代码高内聚低耦合，便于维护与二次迭代。

- **安全认证**：采用 JWT + 拦截器实现无状态登录与接口鉴权，区分学生、教师、管理员三类角色，防止越权访问；自定义全局异常处理器，统一响应格式与错误处理。

- **选课机制**：实现选课/退课功能，采用乐观锁防止课程超选，支持退课后重选，同时实现上课时间冲突检测，提升选课合理性。

- **权限管控**：基于角色的权限控制，不同角色对应不同操作权限与页面访问权限，管理员可全面管理系统，教师专注成绩录入，学生聚焦选课与成绩查询。

- **交互优化**：前端采用纯净白+卡片分层样式，界面简洁清晰；所有列表支持分页、多条件筛选，操作流程流畅，适配各类终端显示。

- **接口联调**：前后端接口严格对接，前端统一封装请求拦截，后端提供规范RESTful API，通过Apifox完成全接口自测，确保交互稳定无异常。

---

### 快速运行

#### 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+
- Node.js 18+（建议 16+）
- npm 9+

#### 后端启动

1. 创建数据库（例如 course_db），执行 sql/course_db.sql 建表脚本，导入测试数据。

2. 修改 backend/src/main/resources/application.yml 中的数据库连接参数（url、username、password）。

3. 如需修改 JWT 密钥，可在 backend/src/main/java/com/dhw/courseselectionsystem/utils/JwtUtils.java 中更改 SIGN_KEY。

4. 运行 CourseSelectionSystemApplication.java 主类，默认端口 8080。

#### 前端启动

1. 进入前端目录：cd frontend

2. 安装依赖：npm install

3. 在前端根目录修改 .env.development 文件，配置后端接口地址：VITE_API_BASE_URL=http://localhost:8080

4. 启动开发服务器：npm run dev（默认端口 5173）

确保后端已启动，前端即可正常请求接口，使用测试账号登录系统。

---

### 作者

- **邓浩文**

- Gitee：[https://gitee.com/deng-hao-wen/course-selection-system](https://gitee.com/deng-hao-wen/course-selection-system)