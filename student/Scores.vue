<template>
  <div class="scores">
    <div class="card">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="score" label="成绩" width="100">
          <template #default="{ row }">
            <span v-if="row.score !== null" class="score-value">{{ row.score }}</span>
            <span v-else>未公布</span>
          </template>
        </el-table-column>
        <el-table-column prop="semesterName" label="学期" width="200" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyScores } from '@/api/student'

const loading = ref(false)
const tableData = ref([])

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMyScores()
    if (res.code === 1) {
      tableData.value = res.data || []
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.scores {
  max-width: 1200px;
  margin: 0 auto;
}
.card {
  background: white;
  border-radius: 24px;
  border: 1px solid #edf2f7;
  padding: 20px;
}
.score-value {
  font-weight: 600;
  color: #2563eb;
}
</style>