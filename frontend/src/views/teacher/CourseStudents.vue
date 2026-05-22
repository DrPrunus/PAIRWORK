<template>
    <div class="course-students">
        <div class="card-header">
            <el-button type="default" @click="goBack" :icon="ArrowLeft">返回</el-button>
            <h2>{{ courseName }} - 学生名单</h2>
        </div>
        <div class="card">
            <el-table :data="students" v-loading="loading" stripe>
                <el-table-column prop="studentNo" label="学号" width="150" />
                <el-table-column prop="studentName" label="姓名" width="120" />
                <el-table-column prop="major" label="专业" />
                <el-table-column prop="className" label="班级" />
                <el-table-column label="成绩" width="150">
                    <template #default="{ row }">
                        <el-input-number
                            v-model="row.score"
                            :min="0"
                            :max="100"
                            :precision="1"
                            size="small"
                            controls-position="right"
                            style="width: 100px"
                            placeholder="未录入"
                        />
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" @click="saveScore(row)" :loading="row.saving">保存</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getCourseStudents, updateScore } from '@/api/teacher'

const route = useRoute()
const router = useRouter()
const courseId = ref(route.params.courseId)
const courseName = ref('')
const students = ref([])
const loading = ref(false)

const fetchStudents = async () => {
    loading.value = true
    try {
        const res = await getCourseStudents(courseId.value)
        if (res.code === 1) {
            students.value = (res.data || []).map(s => ({
                ...s,
                score: s.score !== null ? s.score : null,
                saving: false,
                originalScore: s.score
            }))
            if (students.value.length > 0) {
                courseName.value = students.value[0].courseName || `课程ID ${courseId.value}`
            }
        }
    } catch (error) {
        ElMessage.error('获取学生名单失败')
    } finally {
        loading.value = false
    }
}

const saveScore = async (row) => {
    if (row.score === row.originalScore) {
        ElMessage.info('成绩未修改')
        return
    }
    row.saving = true
    try {
        const res = await updateScore({
            courseId: courseId.value,
            studentId: row.studentId,
            score: row.score
        })
        if (res.code === 1) {
            ElMessage.success('成绩保存成功')
            row.originalScore = row.score
        } else {
            row.score = row.originalScore
        }
    } catch (error) {
        row.score = row.originalScore
    } finally {
        row.saving = false
    }
}

const goBack = () => {
    router.push('/teacher/courses')
}

onMounted(() => {
    fetchStudents()
})
</script>

<style scoped>
.course-students {
    max-width: 1400px;
    margin: 0 auto;
}
.card-header {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 24px;
}
.card-header h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
}
.card {
    background: white;
    border-radius: 24px;
    border: 1px solid #edf2f7;
    padding: 20px;
    overflow: auto;
}
</style>
