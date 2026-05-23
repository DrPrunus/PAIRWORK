<template>
    <div class="semesters">
        <div class="card">
            <div class="card-header">
                <h2>学期管理</h2>
                <el-button type="primary" @click="openDialog()">新增学期</el-button>
            </div>

            <el-table :data="tableData" v-loading="loading" stripe>
                <el-table-column prop="name" label="学期名称" />
                <el-table-column prop="startDate" label="开始日期" width="120">
                    <template #default="{ row }">
                        {{ formatDate(row.startDate) }}
                    </template>
                </el-table-column>
                <el-table-column prop="endDate" label="结束日期" width="120">
                    <template #default="{ row }">
                        {{ formatDate(row.endDate) }}
                    </template>
                </el-table-column>
                <el-table-column label="当前学期" width="100">
                    <template #default="{ row }">
                        <el-tag v-if="row.isCurrent === 1" type="success">是</el-tag>
                        <el-tag v-else type="info">否</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="enrollmentStart" label="选课开始" width="160">
                    <template #default="{ row }">
                        {{ formatDateTime(row.enrollmentStart) }}
                    </template>
                </el-table-column>
                <el-table-column prop="enrollmentEnd" label="选课结束" width="160">
                    <template #default="{ row }">
                        {{ formatDateTime(row.enrollmentEnd) }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180" fixed="right">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" plain @click="openDialog(row)">编辑</el-button>
                        <el-button type="danger" size="small" plain @click="handleDelete(row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 新增/编辑弹窗 -->
        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="学期名称" prop="name">
                    <el-input v-model="form.name" placeholder="如：2025-2026学年第一学期" />
                </el-form-item>
                <el-form-item label="开始日期" prop="startDate">
                    <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
                </el-form-item>
                <el-form-item label="结束日期" prop="endDate">
                    <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
                </el-form-item>
                <el-form-item label="当前学期" prop="isCurrent">
                    <el-switch v-model="form.isCurrent" :active-value="1" :inactive-value="0" active-text="是" inactive-text="否" />
                </el-form-item>
                <el-form-item label="选课开始时间" prop="enrollmentStart">
                    <el-date-picker v-model="form.enrollmentStart" type="datetime" placeholder="选择日期时间" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
                </el-form-item>
                <el-form-item label="选课结束时间" prop="enrollmentEnd">
                    <el-date-picker v-model="form.enrollmentEnd" type="datetime" placeholder="选择日期时间" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSemesters, addSemester, updateSemester, deleteSemester } from '@/api/admin'
import dayjs from 'dayjs'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增学期')
const submitting = ref(false)
const formRef = ref()

const form = reactive({
    id: null,
    name: '',
    startDate: '',
    endDate: '',
    isCurrent: 0,
    enrollmentStart: '',
    enrollmentEnd: ''
})

const rules = {
    name: [{ required: true, message: '请输入学期名称', trigger: 'blur' }],
    startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
    endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
}

const fetchData = async () => {
    loading.value = true
    try {
        const res = await getSemesters()
        if (res.code === 1) {
            tableData.value = res.data || []
        }
    } catch (error) {
        console.error(error)
    } finally {
        loading.value = false
    }
}

const openDialog = (row = null) => {
    if (row) {
        dialogTitle.value = '编辑学期'
        Object.assign(form, {
            id: row.id,
            name: row.name,
            startDate: row.startDate,
            endDate: row.endDate,
            isCurrent: row.isCurrent,
            enrollmentStart: row.enrollmentStart,
            enrollmentEnd: row.enrollmentEnd
        })
    } else {
        dialogTitle.value = '新增学期'
        Object.assign(form, {
            id: null,
            name: '',
            startDate: '',
            endDate: '',
            isCurrent: 0,
            enrollmentStart: '',
            enrollmentEnd: ''
        })
    }
    dialogVisible.value = true
}

const submitForm = async () => {
    await formRef.value.validate()
    submitting.value = true
    try {
        let res
        if (form.id) {
            res = await updateSemester(form)
        } else {
            res = await addSemester(form)
        }
        if (res.code === 1) {
            ElMessage.success(form.id ? '修改成功' : '添加成功')
            dialogVisible.value = false
            await fetchData()
        }
    } catch (error) {
        console.error(error)
    } finally {
        submitting.value = false
    }
}

const handleDelete = async (id) => {
    try {
        await ElMessageBox.confirm('确定删除该学期吗？删除后无法恢复。', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const res = await deleteSemester(id)
        if (res.code === 1) {
            ElMessage.success('删除成功')
            await fetchData()
        }
    } catch (error) {
        if (error !== 'cancel') console.error(error)
    }
}

const formatDate = (date) => {
    if (!date) return ''
    return dayjs(date).format('YYYY-MM-DD')
}

const formatDateTime = (date) => {
    if (!date) return ''
    return dayjs(date).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
    fetchData()
})
</script>

<style scoped>
.semesters {
    max-width: 1400px;
    margin: 0 auto;
}
.card {
    background: white;
    border-radius: 24px;
    border: 1px solid #edf2f7;
    padding: 20px;
}
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}
.card-header h2 {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
}
</style>
