<template>
  <div style="display: flex;height: 100vh;width: 99%">
    <aside-tab>

    </aside-tab>
    <div style="width: 100%">

      <head-bar/>
      <router-view/>
    </div>
  </div>
</template>

<script>
import AsideTab from "@/components/asideTab";
import HeadBar from "@/components/headBar";
export default {
  name: "App",
  components: {
    AsideTab,
    HeadBar
  },
  data() {
    return {}
  },

}
</script>

<style scoped>

</style>
