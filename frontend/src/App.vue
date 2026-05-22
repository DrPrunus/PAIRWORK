<template>
  <router-view />
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: system-ui, -apple-system, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  background: #ffffff;
  color: #1a202c;
}

/* 覆盖 Element Plus 部分默认样式 */
.el-message {
  border-radius: 16px !important;
}
.el-button {
  border-radius: 40px !important;
}
.el-card {
  border-radius: 24px !important;
  border: 1px solid #edf2f7 !important;
  box-shadow: none !important;
}
.el-table {
  border-radius: 20px !important;
  overflow: hidden;
}
.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>