<template>
  <div class="course-page-container">
    <div class="course-list">
      <!-- 搜索卡片 -->
      <div class="card search-card">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="课程名称">
            <el-input v-model="searchForm.name" placeholder="课程名称" clearable size="large" />
          </el-form-item>
          <el-form-item class="search-btn-right">
            <el-button type="primary" @click="handleSearch" size="large">查询</el-button>
            <el-button @click="resetSearch" size="large">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 课程卡片网格 -->
      <div class="course-grid" v-loading="loading">
        <div v-for="course in tableData" :key="course.id" class="course-card">
          <div class="course-header">
            <span class="course-name">{{ course.name }}</span>
            <span class="course-credit">{{ course.credit }} 学分</span>
          </div>
          <div class="course-info">
            <div><span class="label">教师：</span>{{ course.teacherName }}</div>
            <div><span class="label">时间：</span>{{ course.schedule }}</div>
            <div><span class="label">容量：</span>
              <span :class="{ 'full': course.currentStudents >= course.maxStudents }">
                {{ course.currentStudents }} / {{ course.maxStudents }}
              </span>
            </div>
          </div>
          <div class="course-actions">
            <el-button
              v-if="!course.selected"
              type="primary"
              :disabled="course.currentStudents >= course.maxStudents"
              @click="handleSelect(course.id)"
            >选课</el-button>
            <el-button
              v-else
              type="danger"
              plain
              @click="handleDrop(course.id)"
            >退课</el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页：固定在内容区底部、居中、全中文 -->
    <div class="pagination-bottom">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[6, 12, 24]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="fetchCourses"
        @current-change="fetchCourses"
        background

        total-text="共 {{ total }} 条"
        prev-text="上一页"
        next-text="下一页"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCourses, selectCourse, dropCourse, getMySelections } from '@/api/student'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(12)
const searchForm = reactive({ name: '' })
const selectedCourseIds = ref(new Set())

const fetchMySelections = async () => {
  try {
    const res = await getMySelections()
    if (res.code === 1) {
      selectedCourseIds.value.clear()
      res.data.forEach(item => {
        selectedCourseIds.value.add(item.courseId)
      })
      tableData.value.forEach(course => {
        course.selected = selectedCourseIds.value.has(course.id)
      })
    }
  } catch (error) {
    console.error(error)
  }
}

const fetchCourses = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      name: searchForm.name || undefined
    }
    const res = await getCourses(params)
    if (res.code === 1) {
      const pageInfo = res.data
      tableData.value = pageInfo.list || []
      total.value = pageInfo.total || 0
      tableData.value.forEach(course => {
        course.selected = selectedCourseIds.value.has(course.id)
      })
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSelect = async (courseId) => {
  try {
    const res = await selectCourse(courseId)
    if (res.code === 1) {
      ElMessage.success('选课成功')
      await fetchMySelections()
      await fetchCourses()
    }
  } catch (error) {}
}

const handleDrop = async (courseId) => {
  try {
    await ElMessageBox.confirm('确定退选该课程吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText:'取消',
      type:'warning'
    })
    const res = await dropCourse(courseId)
    if (res.code === 1) {
      ElMessage.success('退课成功')
      await fetchMySelections()
      await fetchCourses()
    }
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchCourses()
}

const resetSearch = () => {
  searchForm.name = ''
  handleSearch()
}

onMounted(() => {
  fetchMySelections()
  fetchCourses()
})
</script>

<style scoped>
/* 最外层容器，让分页贴在内容区底部 */
.course-page-container {
  max-width: 1400px;
  margin: 0 auto;
  min-height: calc(100vh - 80px);
  display: flex;
  flex-direction: column;
}

.course-list {
  flex: 1;
}

.search-card {
  background: white;
  border-radius: 24px;
  border: 1px solid #edf2f7;
  padding: 20px 24px;
  margin-bottom: 24px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 16px;
}

/* 按钮右对齐 */
.search-btn-right {
  margin-left: auto !important;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}

.course-card {
  background: white;
  border-radius: 24px;
  border: 1px solid #edf2f7;
  padding: 20px;
  transition: all 0.2s;
}

.course-card:hover {
  box-shadow: 0 8px 20px rgba(0,0,0,0.04);
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f1f5f9;
}

.course-name {
  font-size: 18px;
  font-weight: 600;
  color: #0f172a;
}

.course-credit {
  font-size: 14px;
  color: #64748b;
  background: #f1f5f9;
  padding: 2px 10px;
  border-radius: 20px;
}

.course-info {
  margin-bottom: 20px;
}

.course-info div {
  margin-bottom: 8px;
  font-size: 14px;
  color: #334155;
}

.label {
  color: #64748b;
  width: 40px;
  display: inline-block;
}

.full {
  color: #ef4444;
  font-weight: 500;
}

.course-actions {
  display: flex;
  justify-content: flex-end;
}

/* 分页：贴在内容区底部、居中、不飘、全中文 */
.pagination-bottom {
  padding: 20px 0;
  display: flex;
  justify-content: center;
  width: 100%;
}
</style>
