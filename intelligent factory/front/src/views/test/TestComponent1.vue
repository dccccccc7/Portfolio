<template>
  <div id="container">
    <div id="building">

      <div style="text-align: left;padding-left: 50px">
        <h1>欢迎使用车间管理系统!</h1>

      </div>
    </div>
  </div>


</template>

<script>
export default {
  name: "TestComponent1"
}
</script>

<style scoped>
#tc1 {
  text-align: left;
  padding-left: 50px;

}
#container{
  width: 100%;
  height: 100%;
}
#building{
  width: 100%;
  height: 100%;
  position:fixed;
  margin-top: -0px;/*上边距*/
  margin-left: 20px;/*左边距*/
  background: url("~@/assets/pic/bg2.png");
}
</style>
