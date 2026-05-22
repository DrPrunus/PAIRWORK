<template>
    <div class="teacher-courses">
        <!-- 搜索卡片 -->
        <div class="card search-card">
            <el-form :inline="true" class="search-form" @submit.prevent>
                <el-form-item label="课程名称">
                    <el-input 
                        v-model="courseName" 
                        placeholder="课程名称" 
                        clearable 
                        @clear="handleSearch"
                        style="width: 200px"
                    />
                </el-form-item>
                <el-form-item label="上课时间">
                    <el-input 
                        v-model="schedule" 
                        placeholder="上课时间（如：周一 第1-2节）" 
                        clearable 
                        @clear="handleSearch"
                        style="width: 320px"
                    />
                </el-form-item>
                
                <!-- 按钮组：自动靠右 -->
                <el-form-item class="btn-group">
                    <el-button type="primary" @click="handleSearch">查询</el-button>
                    <el-button @click="resetSearch">重置</el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- 课程卡片网格 -->
        <div class="course-grid" v-loading="loading">
            <div v-for="course in courses" :key="course.id" class="course-card">
                <div class="course-header">
                    <span class="course-name">{{ course.name }}</span>
                    <span class="course-credit">{{ course.credit }} 学分</span>
                </div>
                <div class="course-info">
                    <div><span class="label">课程编号：</span>{{ course.courseCode }}</div>
                    <div><span class="label">上课时间：</span>{{ course.schedule }}</div>
                    <div><span class="label">选课人数：</span>{{ course.currentStudents }} / {{ course.maxStudents }}</div>
                    <div><span class="label">学期：</span>{{ course.semesterName }}</div>
                </div>
                <div class="course-actions">
                    <el-button type="primary" @click="viewStudents(course.id)">查看学生名单</el-button>
                </div>
            </div>
            <el-empty v-if="!loading && courses.length === 0" description="暂无课程" />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTeacherCourses } from '@/api/teacher'

const router = useRouter()
const loading = ref(false)
const courses = ref([])
const courseName = ref('')
const schedule = ref('')

// 获取课程列表
const fetchCourses = async () => {
    loading.value = true
    try {
        const params = {}
        if (courseName.value) params.courseName = courseName.value
        if (schedule.value) params.schedule = schedule.value
        const res = await getTeacherCourses(params)
        if (res.code === 1) {
            courses.value = res.data || []
        }
    } catch (error) {
        ElMessage.error('获取课程列表失败')
    } finally {
        loading.value = false
    }
}

// 查询
const handleSearch = () => {
    fetchCourses()
}

// 重置筛选条件
const resetSearch = () => {
    courseName.value = ''
    schedule.value = ''
    fetchCourses()
}

// 跳转到学生名单页面
const viewStudents = (courseId) => {
    router.push(`/teacher/courses/${courseId}/students`)
}

onMounted(() => {
    fetchCourses()
})
</script>

<style scoped>
.teacher-courses {
    max-width: 1400px;
    margin: 0 auto;
}
.search-card {
    background: white;
    border-radius: 24px;
    border: 1px solid #edf2f7;
    padding: 20px 24px;
    margin-bottom: 24px;
}

/* 关键：让表单自动占满宽度 */
.search-form {
    display: flex;
    align-items: center;
    width: 100%;
}

/* 按钮组自动靠右 */
.btn-group {
    margin-left: auto !important;
}

.course-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
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
    width: 70px;
    display: inline-block;
}
.course-actions {
    display: flex;
    justify-content: flex-end;
}
</style>
