<template>
  <div class="login-container">
    <div class="login-card">
      <div class="logo">📖 EduSelect</div>
      <div class="input-group">
        <label>账号</label>
        <el-input
          v-model="form.username"
          placeholder="学号/工号"
          size="large"
          clearable
        />
      </div>
      <div class="input-group">
        <label>密码</label>
        <el-input
          v-model="form.password"
          type="password"
          placeholder="密码"
          size="large"
          show-password
        />
      </div>
      <el-button type="primary" size="large" @click="handleLogin" :loading="loading" style="width: 100%; margin-top: 8px;">
        登录
      </el-button>
      <div class="demo">
        <div>🎓 学生: S001 / 123456 &nbsp;|&nbsp; 👨‍🏫 教师: T001 / 123456</div>
        <div>⚙️ 管理员: admin / 123456</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  // 在 Login.vue 的 handleLogin 中添加
  console.log('username:', form.username)
  console.log('password:', form.password)
  if (!form.username || !form.password) {
    ElMessage.warning('请填写用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await login(form)
    if (res.code === 1) {
      const { token, role, refId } = res.data
      userStore.setToken(token)
      userStore.setUserInfo({ role, refId, username: form.username })
      ElMessage.success('登录成功')
      if (role === 'student') router.push('/student/courses')
      else if (role === 'teacher') router.push('/teacher/courses')
      else if (role === 'admin') router.push('/admin/semesters')
      else router.push('/')
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: #f8fafc;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-card {
  background: white;
  border-radius: 32px;
  padding: 40px 32px;
  width: 400px;
  box-shadow: 0 20px 35px -12px rgba(0,0,0,0.08);
  border: 1px solid #eef2f6;
}
.logo {
  font-size: 28px;
  font-weight: 700;
  color: #0f172a;
  text-align: center;
  margin-bottom: 32px;
}
.input-group {
  margin-bottom: 24px;
}
.input-group label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
  color: #334155;
}
.demo {
  margin-top: 28px;
  background: #f1f5f9;
  padding: 12px;
  border-radius: 20px;
  font-size: 13px;
  text-align: center;
  color: #475569;
}
</style>
