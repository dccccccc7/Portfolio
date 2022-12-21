<!--
设计瑕疵分类侧边栏，
-->

<template>
  <div id="theBaseMainBox">

    <el-menu
        class="el-menu-demo"
        mode="horizontal"
        default-active="0"
        @select="handleSelect">
      <el-menu-item index="0">
        <span slot="title">全部瑕疵</span>
      </el-menu-item>
      <el-menu-item index="1">
        <span slot="title">安全与监管</span>
      </el-menu-item>
      <el-menu-item index="2">
        <span slot="title">材料</span>
      </el-menu-item>
      <el-menu-item index="3">
        <span slot="title">清洁度</span>
      </el-menu-item>
      <el-menu-item index="4">
        <span slot="title">制作</span>
      </el-menu-item>
      <el-menu-item index="5">
        <span slot="title">组件和修剪</span>
      </el-menu-item>
      <el-menu-item index="6">
        <span slot="title">洗涤和整理</span>
      </el-menu-item>
      <el-menu-item index="7">
        <span slot="title">包装检验</span>
      </el-menu-item>
      <el-menu-item index="8">
        <span slot="title">测量</span>
      </el-menu-item>
      <el-menu-item index="9">
        <span slot="title">其他</span>
      </el-menu-item>
    </el-menu>

    <router-view></router-view>

    <div id="theBaseMain">

    </div>
  </div>
</template>

<script>
export default {
  name: 'defectSideMenu',
  data() {
    return {
      cid: '',
      activeIndex: '1',
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
#theBaseMainBox {
  display: flex;
  /*background-color: gray;*/
  height: 600px;
  flex-direction: column;
  width: 1500px;
  margin: 25px;

}
</style>

