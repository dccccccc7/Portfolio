<template>
  <el-container>
    <div style="width: 100px">
      <defect-side-menu  @indexSelect="listByCategory" ref="sideMenu"></defect-side-menu>
    </div>
  <div style="margin-top: 7%">
    <!--动态切换显隐，组件-->
    <!--      <component :is="showWhat" class="form-area" ref="formArea"></component>-->
    <defect-all class="form-area" ref="formArea"></defect-all>
  </div>
  </el-container>


</template>

<script>
import defectAll from "@/components/qualityManagement/defect/defectAll";
import defectSideMenu from "@/components/qualityManagement/defect/defectSideMenu"
import {request} from "@/network/request";

export default {
  name: 'defectManagementComponent',
  components: {defectAll, defectSideMenu},
  data() {
    return {
      // showWhat: ""
    }
  },
  methods: {
    listByCategory() {
      const _this = this;
      const cid = _this.$refs.sideMenu.cid;

      let keyword = "";
      switch (cid){
        case '0':
          keyword = "全部瑕疵";
          break;
        case '1':
          keyword = "安全与监管";
          break;
        case '2':
          keyword = "材料";
          break;
        case '3':
          keyword = "清洁度";
          break;
        case '4':
          keyword = "制作";
          break;
        case '5':
          keyword = "组件和修剪";
          break;
        case '6':
          keyword = "洗涤和整理";
          break;
        case '7':
          keyword = "包装检验";
          break;
        case '8':
          keyword = "测量";
          break;
        case '9':
          keyword = "其他";
          break;
      }

      const url = '/defect/' + keyword + '/defects';
      let req = {
        company_id:this.$store.getters.userinfo.company_id,
      };
      request({
        url: url,
        method: 'get',
        params: req,
      }).then(res => {
        let data = res.data;
        _this.$refs.formArea.defectForm = data;
      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    }
  }
}
</script>

<style scoped>

</style>

