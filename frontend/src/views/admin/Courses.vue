<template>
    <div class="courses">
        <div class="card">
            <div class="card-header">
                <h2>课程管理</h2>
                <el-button type="primary" @click="openDialog()">新增课程</el-button>
            </div>

            <!-- 搜索栏 -->
            <div class="search-bar">
                <el-form :inline="true" :model="searchForm" class="search-form">
                    <el-form-item label="课程名称">
                        <el-input v-model="searchForm.name" placeholder="课程名称" clearable style="width: 180px"/>
                    </el-form-item>
                    <el-form-item label="教师">
                        <el-select v-model="searchForm.teacherId" placeholder="全部教师" clearable style="width: 220px">
                            <el-option
                                v-for="item in teacherList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="学期">
                        <el-select v-model="searchForm.semesterId" placeholder="全部学期" clearable style="width: 220px">
                            <el-option
                                v-for="item in semesterList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            />
                        </el-select>
                    </el-form-item>
                    <el-form-item class="btn-group">
                        <el-button type="primary" @click="handleSearch">查询</el-button>
                        <el-button @click="resetSearch">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>

            <!-- 表格 -->
            <el-table :data="tableData" v-loading="loading" stripe>
                <el-table-column prop="courseCode" label="课程编号" width="120" />
                <el-table-column prop="name" label="课程名称" width="180" />
                <el-table-column prop="credit" label="学分" width="80" />
                <el-table-column prop="teacherName" label="教师" width="120" />
                <el-table-column prop="semesterName" label="学期" width="180" />
                <el-table-column prop="schedule" label="上课时间" />
                <el-table-column prop="maxStudents" label="容量" width="80" />
                <el-table-column prop="currentStudents" label="已选" width="80" />
                <el-table-column label="状态" width="80">
                    <template #default="{ row }">
                        <el-tag :type="row.status === 1 ? 'success' : 'info'">
                            {{ row.status === 1 ? '进行中' : '未开始' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180" fixed="right">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" plain @click="openDialog(row)">编辑</el-button>
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
        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="课程编号" prop="courseCode">
                    <el-input v-model="form.courseCode" placeholder="如 CS101" />
                </el-form-item>
                <el-form-item label="课程名称" prop="name">
                    <el-input v-model="form.name" placeholder="课程名称" />
                </el-form-item>
                <el-form-item label="学分" prop="credit">
                    <el-input-number v-model="form.credit" :min="0.5" :step="0.5" :precision="1" />
                </el-form-item>
                <el-form-item label="容量" prop="maxStudents">
                    <el-input-number v-model="form.maxStudents" :min="1" :max="200" />
                </el-form-item>
                <el-form-item label="任课教师" prop="teacherId">
                    <el-select v-model="form.teacherId" placeholder="请选择教师" style="width: 100%">
                        <el-option
                            v-for="item in teacherList"
                            :key="item.id"
                            :label="`${item.name} (${item.teacherNo})`"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="学期" prop="semesterId">
                    <el-select v-model="form.semesterId" placeholder="请选择学期" style="width: 100%">
                        <el-option
                            v-for="item in semesterList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="上课时间" prop="schedule">
                    <el-input v-model="form.schedule" placeholder="如：周一 第1-2节 教学楼A101" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-radio-group v-model="form.status">
                        <el-radio :label="1">进行中</el-radio>
                        <el-radio :label="0">未开始</el-radio>
                        <el-radio :label="2">已结束</el-radio>
                    </el-radio-group>
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
import { getCourses, addCourse, updateCourse, deleteCourse } from '@/api/admin'
import { getSemesters } from '@/api/admin'
import { getTeachers } from '@/api/admin'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const searchForm = reactive({ name: '', teacherId: null, semesterId: null })
const teacherList = ref([])
const semesterList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增课程')
const submitting = ref(false)
const formRef = ref()

const form = reactive({
    id: null,
    courseCode: '',
    name: '',
    credit: 2.0,
    maxStudents: 30,
    teacherId: null,
    semesterId: null,
    schedule: '',
    status: 1
})

const rules = {
    courseCode: [{ required: true, message: '请输入课程编号', trigger: 'blur' }],
    name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
    credit: [{ required: true, message: '请输入学分', trigger: 'blur' }],
    maxStudents: [{ required: true, message: '请输入容量', trigger: 'blur' }],
    teacherId: [{ required: true, message: '请选择教师', trigger: 'change' }],
    semesterId: [{ required: true, message: '请选择学期', trigger: 'change' }],
    schedule: [{ required: true, message: '请输入上课时间', trigger: 'blur' }]
}

const fetchTeachers = async () => {
    try {
        const res = await getTeachers({ pageNum: 1, pageSize: 100 })
        if (res.code === 1) {
            teacherList.value = res.data.list || []
        }
    } catch (error) {
        console.error(error)
    }
}

const fetchSemesters = async () => {
    try {
        const res = await getSemesters()
        if (res.code === 1) {
            semesterList.value = res.data || []
        }
    } catch (error) {
        console.error(error)
    }
}

const fetchData = async () => {
    loading.value = true
    try {
        const params = {
            pageNum: pageNum.value,
            pageSize: pageSize.value,
            ...searchForm
        }
        const res = await getCourses(params)
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
        dialogTitle.value = '编辑课程'
        Object.assign(form, {
            id: row.id,
            courseCode: row.courseCode,
            name: row.name,
            credit: row.credit,
            maxStudents: row.maxStudents,
            teacherId: row.teacherId,
            semesterId: row.semesterId,
            schedule: row.schedule,
            status: row.status
        })
    } else {
        dialogTitle.value = '新增课程'
        Object.assign(form, {
            id: null,
            courseCode: '',
            name: '',
            credit: 2.0,
            maxStudents: 30,
            teacherId: null,
            semesterId: null,
            schedule: '',
            status: 1
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
            res = await updateCourse(form)
        } else {
            res = await addCourse(form)
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
        await ElMessageBox.confirm('确定删除该课程吗？如果已有选课记录将无法删除。', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const res = await deleteCourse(id)
        if (res.code === 1) {
            ElMessage.success('删除成功')
            await fetchData()
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
    searchForm.name = ''
    searchForm.teacherId = null
    searchForm.semesterId = null
    handleSearch()
}

onMounted(() => {
    fetchTeachers()
    fetchSemesters()
    fetchData()
})
</script>

<style scoped>
.courses {
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
