# Security Specification - Campus Study Room Reservation System

## Data Invariants
1. 预约 (Reservation) 不能在过去的时间点。
2. 同一研讨室 (Room) 在同一时间段内不能有重叠的预约。
3. 用户不能修改他人的预约（除非是管理员）。
4. 签到状态必须由系统逻辑或研讨室现场设备触发（此处前端模拟，规则需限制 userId 匹配）。

## The "Dirty Dozen" Payloads (Attack Vectors)
1. **Identity Spoofing**: 尝试将 `userId` 设置为他人的 UID。
2. **Time Travel**: 创建一个过去时间的预约。
3. **Double Booking**: 故意发送一个重叠时间段的预约。
4. **Status Hijack**: 手动将 `status` 从 `pending` 改为 `checked_in` 而不通过验证。
5. **Credit Injection**: 尝试修改自己的 `creditScore`。
6. **Resource Poisoning**: 在 `roomId` 中注入巨大的字符串。
7. **Orphaned Record**: 预约一个不存在的 `roomId`。
8. **Malicious Query**: 尝试拉取所有用户的 `creditScore`。
9. **Update Gap**: 在更新预约时间时偷偷修改 `userId`。
10. **Shadow Field**: 在提交预约时加入 `isAdmin: true`。
11. **Negative Duration**: 设置 `endTime` 早于 `startTime`。
12. **Mass Delete**: 尝试删除他人的预约记录。

## Relation Mapping
- `Reservations` 属于 `Rooms` (逻辑上)。
- `Reservations` 的所有权由 `userId` 决定。
- `Rooms` 是公共可读的，但只有管理员能写。
