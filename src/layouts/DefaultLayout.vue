<template>
  <div class="app">
    <aside class="sidebar">
      <div class="logo-area">
        <div class="logo">📖 EduSelect</div>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        router
        background-color="#ffffff"
        text-color="#334155"
        active-text-color="#2563eb"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </aside>
    <main class="main">
      <div class="page-header">
        <h1 class="page-title">{{ currentTitle }}</h1>
        <div class="user-area">
          <span class="user-name">{{ userName }}</span>
          <el-button type="danger" size="small" plain @click="logout">退出</el-button>
        </div>
      </div>
      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import {
  List,
  Document,
  Notebook,
  Setting,
  User,
  School
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const role = userStore.userInfo?.role

// 根据角色动态生成菜单
const menuItems = computed(() => {
  if (role === 'student') {
    return [
      { path: '/student/courses', title: '可选课程', icon: List },
      { path: '/student/my', title: '我的选课', icon: Document },
      { path: '/student/scores', title: '成绩查询', icon: Notebook }
    ]
  } else if (role === 'teacher') {
    return [
      { path: '/teacher/courses', title: '我的课程', icon: List }
    ]
  } else if (role === 'admin') {
    return [
      { path: '/admin/semesters', title: '学期管理', icon: Setting },
      { path: '/admin/teachers', title: '教师管理', icon: User },
      { path: '/admin/courses', title: '课程管理', icon: List },
      { path: '/admin/students', title: '学生管理', icon: School }
    ]
  }
  return []
})

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => {
  const matched = route.matched.find(item => item.meta?.title)
  return matched ? matched.meta.title : '首页'
})

const userName = computed(() => {
  return userStore.userInfo?.username || (role === 'student' ? '同学' : role)
})

const logout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.app {
  display: flex;
  min-height: 100vh;
  background: #ffffff;
}
/* 侧边栏 方案三风格 */
.sidebar {
  width: 240px;
  background: #fafbfc;
  border-right: 1px solid #eef2f6;
  display: flex;
  flex-direction: column;
}
.logo-area {
  padding: 32px 20px 24px 20px;
  border-bottom: 1px solid #eef2f6;
  margin-bottom: 20px;
}
.logo {
  font-size: 22px;
  font-weight: 700;
  color: #0f172a;
  text-align: center;
}
.el-menu-vertical {
  border-right: none;
  background: transparent;
}
.el-menu-item {
  margin: 4px 12px;
  border-radius: 12px;
  height: 44px;
  line-height: 44px;
}
.el-menu-item.is-active {
  background: #ebf4ff;
  color: #2563eb;
}
.el-menu-item:hover:not(.is-active) {
  background: #f1f5f9;
  color: #1e293b;
}
/* 主内容区 */
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #ffffff;
}
.page-header {
  padding: 28px 32px 0 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #edf2f7;
  margin-bottom: 24px;
}
.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #0f172a;
  margin: 0;
}
.user-area {
  display: flex;
  align-items: center;
  gap: 16px;
}
.user-name {
  font-weight: 500;
  color: #475569;
}
.content {
  padding: 0 32px 32px 32px;
  flex: 1;
}
</style>