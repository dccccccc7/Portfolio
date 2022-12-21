<template>
  <div id="theBaseMainBox">

    <el-menu
        :default-active="activeIndex"
        :router="true"
        :unique-opened="true"
        class="el-menu-demo"
        mode="horizontal"
        default-active="1"
        @select="handleSelect">
      <el-menu-item index="/storehouse/inOut/apply">申请信息</el-menu-item>
      <el-menu-item index="/storehouse/inOut/itemRequest">请求信息</el-menu-item>
      <!--<el-menu-item index="/storehouse/inOut/itemManage">出入库操作</el-menu-item>-->
    </el-menu>

    <router-view></router-view>

    <div id="theBaseMain">

    </div>
  </div>
</template>

<script>
export default {
  name: "in_out_storage",
  data(){
    return {
      activeIndex: '1',
    }
  },
  methods:{
    handleSelect(key, keyPath) {

    }
  }
}
</script>

<style scoped>
.el-select .el-input {
  width: 230px;
}

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.mainAdd .el-input .el-input__inner {
  width: 30%;
}

.mainAdd {
  width: 40%;
  height: auto;
  margin-top: 20px;
}

.mainAdd .el-input {
  width: 80%;
  margin-right: 20px;
}

.mainAdd .upupup {
  margin-right: 90px;
}

#theBaseMainBox {
  display: flex;
  /*background-color: gray;*/
  /*height: 600px;*/
  flex-direction: column;
  width: 95%;
  margin: 25px;

}

#theBaseHeader {
  /*background-color: aliceblue;*/
  height: 15%;
  width: 100%;
  display: flex;
  justify-content: space-between;
  /*align-items: center;*/
}

#theBaseMain {
  /*background-color: lightgreen;*/
  height: 80%;

  width: 100%;

}
#el-menu {
  /*width: 20%;*/
  /*height: 20%;*/
  /*text-align: left;*/
}
</style>