<!--主界面上方的信息条，包含登录退出功能-->
<template>
  <div id="HeaderBar">
    <div id="left">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <!--        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>-->
        <transition-group mode="out-in" name="breadcrumb">
          <el-breadcrumb-item v-for="(item,index) in this.menuRoutes" :key="item">{{ item }}</el-breadcrumb-item>
        </transition-group>
      </el-breadcrumb>
    </div>
    <div id="right">
      <el-menu default-active="用户" class="user-bar" mode="horizontal" @select="handleSelect">
        <el-submenu index="用户" id="login_item">
          <template slot="title">用户：{{ this.$store.getters.userinfo.name }}</template>
          <el-menu-item index="个人信息">个人信息</el-menu-item>
          <el-menu-item index="登出">登出</el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
  </div>
</template>

<script>
export default {
  name: "HeaderBar",
  data() {
    return {
      menuRoutes: []
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      switch (key) {
        case "登出":
          this.$store.commit("login/LOG_OUT");
          break;
        case "个人信息":
          this.$message.info("待有缘人完善代码")
          break;
      }
    },
  },
  created() {
    let {meta} = this.$route;
    let {title} = meta;
    if (title) {
      this.menuRoutes = title.split("-").reverse();
    }
  },
  watch: {
    $route(val, oldVal) {
      let {meta} = val;
      let {title} = meta;
      if (title) {
        this.menuRoutes = title.split("-").reverse();
      }
    },
  }
}
</script>

<style scoped>
#HeaderBar {
  padding: 8px 20px 8px 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 42px;
  -webkit-box-shadow: 0px 0px 22px 9px rgba(237, 237, 237, 0.62);
  -moz-box-shadow: 0px 0px 22px 9px rgba(237, 237, 237, 0.62);
  box-shadow: 0px 0px 22px 9px rgba(237, 237, 237, 0.62);
}

#left {

}

.user-bar {
  height: 100%;
}

#right {
  /*border: 1px #ff7300 solid;*/
  text-align: center;
  width: 10%;
  height: 100%;
}
/deep/ .el-submenu {
  height: 40px;
}

/deep/ .el-submenu__title {
  line-height: 40px !important;
  height: 40px !important;
}
</style>
