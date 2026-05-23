[README.md](https://github.com/user-attachments/files/28172531/README.md)
# 学生选课管理系统 - 前端

## 项目简介
基于 Vue 3 + Element Plus 构建的学生选课管理系统前端，提供学生、教师、管理员三端界面，支持选课/退课、成绩录入/查询、后台管理等核心功能。

## 技术栈
- Vue 3 (Composition API)
- Vue Router 4
- Pinia (状态管理)
- Element Plus (UI 组件库)
- Axios (HTTP 请求)
- Day.js (日期格式化)
- Vite (构建工具)

## 环境要求
- Node.js 18+
- npm 9+

## 快速开始

### 1. 安装依赖
```bash
npm install
```

### 2. 配置后端地址
在项目根目录创建 `.env.development` 文件：
```env
VITE_API_BASE_URL=http://localhost:8080
```

### 3. 启动开发服务器
```bash
npm run dev
```
访问 `http://localhost:5173`

### 4. 生产构建
```bash
npm run build
```

## 项目结构
```
src/
├── api/                # API 请求封装
│   ├── auth.js         # 登录
│   ├── student.js      # 学生端接口
│   ├── teacher.js      # 教师端接口
│   └── admin.js        # 管理员端接口
├── assets/             # 静态资源
├── components/         # 公共组件（待扩展）
├── layouts/            # 布局组件（侧边栏+内容区）
├── router/             # 路由配置（含角色权限守卫）
├── stores/             # Pinia 状态管理（用户 token、角色）
├── utils/              # 工具函数（axios 拦截器）
├── views/              # 页面组件
│   ├── Login.vue
│   ├── student/        # 学生端页面
│   ├── teacher/        # 教师端页面
│   └── admin/          # 管理员端页面
├── App.vue
└── main.js
```

## 测试账号
同后端，登录后自动根据角色跳转对应界面。

## 主要页面

| 角色   | 页面路径              | 功能说明                         |
|--------|----------------------|----------------------------------|
| 学生   | `/student/courses`    | 可选课程列表，支持选课/退课        |
|        | `/student/my`         | 我的选课及成绩                   |
|        | `/student/scores`     | 成绩查询                         |
| 教师   | `/teacher/courses`    | 我的课程列表（支持名称/时间筛选）  |
|        | `/teacher/courses/:id/students` | 课程学生名单，录入成绩          |
| 管理员 | `/admin/semesters`    | 学期管理（CRUD）                 |
|        | `/admin/teachers`     | 教师管理（CRUD，重置密码）        |
|        | `/admin/courses`      | 课程管理（CRUD，关联教师/学期）   |
|        | `/admin/students`     | 学生管理（CRUD，重置密码）        |

## 路由守卫与权限
- 未登录自动跳转登录页
- 登录后根据角色限制访问路径（学生不能进入 `/admin`）
- 退出登录清除本地存储并跳回登录页
