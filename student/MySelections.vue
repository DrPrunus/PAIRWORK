<template>
  <div class="my-selections">
    <div class="card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="teacherName" label="教师" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="schedule" label="上课时间" />
        <el-table-column prop="selectionTime" label="选课时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.selectionTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="score" label="成绩" width="80">
          <template #default="{ row }">
            <span v-if="row.score !== null" style="font-weight:500;">{{ row.score }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="danger" size="small" plain @click="handleDrop(row.courseId)">退课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMySelections, dropCourse } from '@/api/student'
import dayjs from 'dayjs'

const loading = ref(false)
const tableData = ref([])

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMySelections()
    if (res.code === 1) {
      tableData.value = res.data || []
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const formatDate = (date) => {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

const handleDrop = async (courseId) => {
  try {
    await ElMessageBox.confirm('确定退选该课程吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await dropCourse(courseId)
    if (res.code === 1) {
      ElMessage.success('退课成功')
      await fetchData()
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.my-selections {
  max-width: 1400px;
  margin: 0 auto;
}
.card {
  background: white;
  border-radius: 24px;
  border: 1px solid #edf2f7;
  padding: 20px;
  overflow: auto;
}
</style>