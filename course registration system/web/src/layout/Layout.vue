<template>
  <div>
    <!-- 头部-->
    <Header :test="1"></Header>
    <!-- 主体-->
    <div style="display: flex">
      <!-- 侧边栏-->
      <!-- 内容区域-->
      <router-view style="flex:1"/>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "Layout",
  components:{
    Header
  }
}
</script>

<style scoped>

</style>