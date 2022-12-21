<template>
  <el-menu
      style="width:170px"
      active-text-color="#ffd04b"
      background-color="#545c64"
      class="el-menu-vertical-demo"
      default-active="2"
      text-color="#fff"
      @open="handleOpen"
      @close="handleClose"
  >
    <el-sub-menu index="1">
      <template #title>
        <el-icon>
          <location/>
        </el-icon>
        <span>车间管理</span>
      </template>
    </el-sub-menu>
    <el-menu-item index="2">
      <el-icon>
        <icon-menu/>
      </el-icon>
      <span>仓库管理</span>
    </el-menu-item>
    <el-menu-item index="3">
      <el-icon>
        <document/>
      </el-icon>
      <span>生产计划管理</span>
    </el-menu-item>
    <el-menu-item index="4">
      <el-icon>
        <setting/>
      </el-icon>
      <span>生产过程管理</span>
    </el-menu-item>
    <el-menu-item index="5">
      <el-icon>
        <setting/>
      </el-icon>
      <span>产品质量管理</span>
    </el-menu-item>
    <el-menu-item index="6">
      <el-icon>
        <setting/>
      </el-icon>
      <span>系统基础管理</span>
    </el-menu-item>
    <el-menu-item index="7">
      <el-icon>
        <setting/>
      </el-icon>
      <span>模板管理</span>
    </el-menu-item>
    <el-menu-item index="8">
      <el-icon>
        <setting/>
      </el-icon>
      <span>数据分析报表</span>
    </el-menu-item>
    <el-menu-item index="8">
      <el-icon>
        <setting/>
      </el-icon>
      <span>订单管理</span>
    </el-menu-item>

  </el-menu>
</template>

<script>

export default {
  name: "asideTab",
  data() {
    return {}
  },
  component: {}
}
</script>

<style scoped>

</style>
