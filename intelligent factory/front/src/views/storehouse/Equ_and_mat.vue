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
      <el-menu-item index="/storehouse/equAndMatMain/equipmentInfo">设备管理</el-menu-item>
      <el-menu-item index="/storehouse/equAndMatMain/materialInfo">物料管理</el-menu-item>
      <el-menu-item index="/storehouse/equAndMatMain/searchItem">查询</el-menu-item>
    </el-menu>

    <router-view></router-view>

    <div id="theBaseMain">

    </div>
  </div>
</template>

<script>
export default {
  name: "Equ_and_mat",
  data(){
    return {
      activeIndex: '1',
    }
  },
  created() {

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
  height: 600px;
  flex-direction: column;
  width: 1500px;
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