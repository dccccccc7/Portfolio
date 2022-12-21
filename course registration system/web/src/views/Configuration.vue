<template>
  <el-tabs tab-position="left" type="border-card" style="height: 90vh">
    <el-tab-pane label="选课开关">
      <choosing-switch></choosing-switch>
    </el-tab-pane>
    <el-tab-pane label="全局结算">
      <global-set></global-set>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import ChoosingSwitch from "@/components/choosingSwitch";
import globalSet from "@/components/globalSet";
export default {
  name: "Configuration",
  components: {ChoosingSwitch,globalSet}
}
</script>

<style scoped>

</style>