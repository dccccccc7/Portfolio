<!--
设计模块分类侧边栏，
-->

<template>
  <el-menu
      class="categories"
      default-active="0"
      @select="handleSelect"
      active-text-color="red">

    <el-menu-item index="1">
      <span slot="title">物料模版</span>
    </el-menu-item>
    <el-menu-item index="2">
      <span slot="title">设备模版</span>
    </el-menu-item>
<!--    <el-menu-item index="3">-->
<!--      <span slot="title">产品模版</span>-->
<!--    </el-menu-item>-->
  </el-menu>
</template>

<script>
export default {
  name: 'modelSideMenu',
  data() {
    return {
      cid: ''
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      this.cid = key
      this.$emit('indexSelect')
    }
  }
}
</script>

<style scoped>
.categories {
  flex-wrap: wrap;
  margin-left: 30%;
  width: auto;
  float: right;
}
.categories el-menu-item{
  display: inline-block;
  text-align: left;
}
</style>

