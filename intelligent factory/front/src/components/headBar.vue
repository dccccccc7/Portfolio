<template>
  <div style="height: 45px;border-bottom: 1px solid">
    <el-row :gutter="20">
      <el-col :span="8" style="font-size: x-large;font-weight: bolder;color:dodgerblue">&nbsp;&nbsp;&nbsp;后台管理</el-col>
      <el-col :span="8"></el-col>
      <el-col :span="5"></el-col>
      <el-col :span="3"><el-dropdown>
    <span class="el-dropdown-link">
<!--此处用{{}}绑定值-->
      你好，xxx！
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>个人信息</el-dropdown-item>
            <el-dropdown-item>管理</el-dropdown-item>
            <el-dropdown-item>登出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown></el-col>
    </el-row>
  </div>

</template>

<script>
export default {
  name: "headBar"
}
</script>

<style scoped>

</style>