<template>
<div id="UserApply">
  <el-tabs tab-position="left" :stretch="true" @tab-click="currentTime = new Date()" style="margin-top: 30px;">

    <el-tab-pane label="人员变动审核" :lazy="true">
      <div slot="label">
        <el-badge :value="statusCount" class="item" v-if="statusCount">
          <span>人员变动审核</span>
        </el-badge>
        <div class="item" v-else>
          <span>人员变动审核</span>
        </div>
      </div>
      <DepartmentChangeManagement
          @getCount="getCount"
          :table-do-layout="currentTime"></DepartmentChangeManagement>
    </el-tab-pane>
    <el-tab-pane label="其他审批" style="padding-left: 20px;">
      <div style="width: inherit;  padding: 20px 120px 20px 20px;">

      </div>
    </el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
import DepartmentChangeManagement from "@/views/systemManagement/DepartmentChangeManagement";
import {request} from "@/network/request";
export default {
  name: "UserApply",
  components: {DepartmentChangeManagement},
  data() {
    return {
      statusCount: 0,
      currentTime:"",
    }
  },
  methods:{
    getCount() {
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
      };
      request({
        url: 'data/applyManagement/toBeAddress',
        method: 'post',
        params: req,
      }).then(res => {
        this.statusCount = res.data;
      });
    },
  },
  activated() {
    this.getCount();
  }
}
</script>

<style scoped>

</style>
