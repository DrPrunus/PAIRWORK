# 结对编程记录 (Pair Programming Log)

## 项目：校园共享研讨室预约系统

### 结对伙伴
- 驾驶员 A/领航员 A: AI Coding Agent
- 驾驶员 B/领航员 B: 用户 (User)

### 沟通与规范
- 采用 Git Flow 流程模拟。
- PR 需经过对方复审（在对话中确认）。
- 角色每 30-45 分钟或关键模块完成后互换。

### 角色互换日志
| 时间 (UTC) | 驾驶员 | 领航员 | 任务/模块 | 备注 |
| :--- | :--- | :--- | :--- | :--- |
| 2026-05-16 11:35 | AI | User | 项目初始化 & 数据库设计 | 建立基础架构 |
| 2026-05-16 11:38 | AI | User | UI 视觉升级: Bold Typography | 应用工业风/终端风格 UI 方案 |

---

## 需求文档 (Simple Requirements)

### 1. 用户模块
- 学生/教师登录 (Firebase Auth)。
- 查看“我的预约”。

### 2. 研讨室资源模块
- 展示研讨室列表（名称、容量、设备、状态）。

### 3. 预约管理模块
- 选择日期和时间段。
- 逻辑校验：防止重复预约、容量限制。

### 4. 签到与反馈模块
- 签到码显示/验证。
- 自动更新预约状态（已签到/未签到）。

---

## 数据库设计 (Firestore Structure)

### Collections:
1. `users`: `{ uid, name, email, role, creditScore }`
2. `rooms`: `{ roomId, name, capacity, facilities, imageUrl }`
3. `reservations`: `{ resId, roomId, userId, date, startTime, endTime, status: 'pending'\|'checked_in'\|'cancelled'\|'missing' }`
