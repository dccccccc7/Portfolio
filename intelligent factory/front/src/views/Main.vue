<!--主界面视图-->
<template>
  <div id="Main">
    <!--父容器-->
    <div id="container">
      <!--左侧导航栏容器-->
      <div id="left">
        <keep-alive>
          <NavigationMenu @routes="setMenuRoutes"></NavigationMenu>
        </keep-alive>
      </div>
      <!--右侧容器-->
      <div id="right">
        <!--头部-->
        <div id="header">
          <HeaderBar></HeaderBar>
        </div>
        <!--其他视图内容-->
        <div id="content">
          <transition mode="out-in" name="fade-transform">
<!--            <keep-alive>-->
              <router-view v-if="isReloadAlive"></router-view>
<!--            </keep-alive>-->
          </transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavigationMenu from "@/components/NavigationMenu";
import HeaderBar from "@/components/HeaderBar";

export default {
  name: "Main",
  components: {HeaderBar, NavigationMenu},
  provide() {
    return {
      reload: this.reload
    }
  },
  data() {
    return {
      menuRoutes: [],
      isReloadAlive : true

    }
  },
  methods: {
    setMenuRoutes(routes) {
      this.menuRoutes = routes;
    },
    reload() {
      this.isReloadAlive = false;
      this.$nextTick(function(){
        this.isReloadAlive = true;
      })
    }
  }

}
</script>

<style scoped>
#container {
  display: flex;
  height: 100vh;
  width: 100vw;
}

#left {
  width: 12%;
  height: 100%;
  flex-shrink: 0;
}

#right {
  display: flex;
  flex-direction: column;
  /*flex-grow: 1;*/
  width: 88%;
}

#content {

}

.v-enter {
  opacity: 0;
}

.v-enter-to {
  opacity: 1;
}

.v-enter-active {
  transition: 0.3s;
}

.v-leave {
  opacity: 1;
}

.v-leave-to {
  opacity: 0;
}

.v-leave-active {
  transition: 0.1s;

}

</style>
