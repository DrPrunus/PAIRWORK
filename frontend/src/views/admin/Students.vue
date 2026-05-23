<template>
    <div class="students">
        <div class="card">
            <div class="card-header">
                <h2>学生管理</h2>
                <el-button type="primary" @click="openDialog()">新增学生</el-button>
            </div>

            <!-- 搜索栏 -->
            <div class="search-bar">
                <el-form :inline="true" :model="searchForm" class="search-form">
                    <el-form-item label="学号">
                        <el-input 
                            v-model="searchForm.studentNo" 
                            placeholder="学号" 
                            clearable 
                            style="width: 160px"
                        />
                    </el-form-item>
                    <el-form-item label="姓名">
                        <el-input 
                            v-model="searchForm.name" 
                            placeholder="姓名" 
                            clearable 
                            style="width: 160px"
                        />
                    </el-form-item>
                    <el-form-item label="专业">
                        <el-input 
                            v-model="searchForm.major" 
                            placeholder="专业" 
                            clearable 
                            style="width: 160px"
                        />
                    </el-form-item>
                    <el-form-item label="班级">
                        <el-input 
                            v-model="searchForm.className" 
                            placeholder="班级" 
                            clearable 
                            style="width: 160px"
                        />
                    </el-form-item>

                    <!-- 按钮组 居右 -->
                    <el-form-item class="btn-group">
                        <el-button type="primary" @click="handleSearch">查询</el-button>
                        <el-button @click="resetSearch">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>

            <!-- 表格 -->
            <el-table :data="tableData" v-loading="loading" stripe>
                <el-table-column prop="studentNo" label="学号" width="120" />
                <el-table-column prop="name" label="姓名" width="100" />
                <el-table-column prop="gender" label="性别" width="60">
                    <template #default="{ row }">
                        {{ row.gender === 1 ? '男' : '女' }}
                    </template>
                </el-table-column>
                <el-table-column prop="major" label="专业" />
                <el-table-column prop="className" label="班级" />
                <el-table-column prop="phone" label="电话" />
                <el-table-column label="操作" width="220" fixed="right">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" plain @click="openDialog(row)">编辑</el-button>
                        <el-button type="warning" size="small" plain @click="handleResetPwd(row)">重置密码</el-button>
                        <el-button type="danger" size="small" plain @click="handleDelete(row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination">
                <el-pagination
                    v-model:current-page="pageNum"
                    v-model:page-size="pageSize"
                    :page-sizes="[5, 10, 20]"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
                    @size-change="fetchData"
                    @current-change="fetchData"
                />
            </div>
        </div>

        <!-- 新增/编辑弹窗 -->
        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
                <el-form-item label="学号" prop="studentNo">
                    <el-input v-model="form.studentNo" placeholder="学号" />
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="form.name" placeholder="姓名" />
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-radio-group v-model="form.gender">
                        <el-radio :label="1">男</el-radio>
                        <el-radio :label="0">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="专业" prop="major">
                    <el-input v-model="form.major" placeholder="专业" />
                </el-form-item>
                <el-form-item label="班级" prop="className">
                    <el-input v-model="form.className" placeholder="班级" />
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="form.phone" placeholder="电话" />
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
import { getStudents, addStudent, updateStudent, deleteStudent, resetStudentPassword } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const searchForm = reactive({ studentNo: '', name: '', major: '', className: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('新增学生')
const submitting = ref(false)
const formRef = ref()

const form = reactive({
    id: null,
    studentNo: '',
    name: '',
    gender: 1,
    major: '',
    className: '',
    phone: ''
})

const rules = {
    studentNo: [{ required: true, message: '请输入学号', trigger: 'blur' }],
    name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
    major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
    className: [{ required: true, message: '请输入班级', trigger: 'blur' }]
}

const fetchData = async () => {
    loading.value = true
    try {
        const params = {
            pageNum: pageNum.value,
            pageSize: pageSize.value,
            ...searchForm
        }
        const res = await getStudents(params)
        if (res.code === 1) {
            tableData.value = res.data.list || []
            total.value = res.data.total || 0
        }
    } catch (error) {
        console.error(error)
    } finally {
        loading.value = false
    }
}

const openDialog = (row = null) => {
    if (row) {
        dialogTitle.value = '编辑学生'
        Object.assign(form, {
            id: row.id,
            studentNo: row.studentNo,
            name: row.name,
            gender: row.gender,
            major: row.major,
            className: row.className,
            phone: row.phone
        })
    } else {
        dialogTitle.value = '新增学生'
        Object.assign(form, {
            id: null,
            studentNo: '',
            name: '',
            gender: 1,
            major: '',
            className: '',
            phone: ''
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
            res = await updateStudent(form)
        } else {
            res = await addStudent(form)
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
        await ElMessageBox.confirm('确定删除该学生吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const res = await deleteStudent(id)
        if (res.code === 1) {
            ElMessage.success('删除成功')
            await fetchData()
        }
    } catch (error) {
        if (error !== 'cancel') console.error(error)
    }
}

const handleResetPwd = async (row) => {
    try {
        const { value } = await ElMessageBox.prompt('请输入新密码', '重置密码', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputValue: '123456',
            inputPattern: /^.{6,20}$/,
            inputErrorMessage: '密码长度6-20位'
        })
        const res = await resetStudentPassword(row.id, value)
        if (res.code === 1) {
            ElMessage.success(`密码已重置为 ${value}`)
        }
    } catch (error) {
        if (error !== 'cancel') console.error(error)
    }
}

const handleSearch = () => {
    pageNum.value = 1
    fetchData()
}

const resetSearch = () => {
    searchForm.studentNo = ''
    searchForm.name = ''
    searchForm.major = ''
    searchForm.className = ''
    handleSearch()
}

onMounted(() => {
    fetchData()
})
</script>

<style scoped>
.students {
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
.search-bar {
    margin-bottom: 20px;
}

/* 关键样式：按钮右对齐 + 搜索框同行 */
.search-form {
    display: flex;
    align-items: center;
    width: 100%;
}
.btn-group {
    margin-left: auto !important;
}

.pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}
</style>
