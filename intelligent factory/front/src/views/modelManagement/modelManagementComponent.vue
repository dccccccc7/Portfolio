<template>
  <el-container>
    <el-aside style="width: 200px;margin-top: 2%">
      <switch></switch>
      <model-side-menu @indexSelect="listByCategory()" ref="sideMenu"></model-side-menu>
    </el-aside>
    <el-main style="margin-left: 5%">
      <!--动态切换显隐，组件-->
      <!--      <component :is="showWhat" class="form-area" ref="formArea"></component>-->
      <model-all class="form-area" ref="formArea"></model-all>
    </el-main>
  </el-container>
</template>

<script>
import modelAll from "@/components/modelManagement/modelAll";
import modelSideMenu from "@/components/modelManagement/modelSideMenu"
import {request} from "@/network/request";

export default {
  name: 'modelManagementComponent',
  components: {modelAll, modelSideMenu},
  data() {
    return {
      company_id:'',
      // showWhat: ""
      classification:'',
    }
  },
  methods: {
    listByCategory() {
      let url='/template/material/all';
      const cid = this.$refs.sideMenu.cid;

      let keyword = "";
      switch (cid){

        case '1':
          keyword = "物料模版";
          break;
        case '2':
          keyword = "设备模版";
          break;
        // case '3':
        //   keyword = "产品模版";
        //   break;
      }

      if (keyword==="物料模版"){
        url="/template/material/all"
        this.classification='material'
      }
      if (keyword==="设备模版"){
        url="/template/equipment/all"
        this.classification='equipment'
      }
      // if (keyword==="产品模版"){
      //   url="/template/product/all"
      // }
      this.$refs.formArea.classification=this.classification
      request({
        url: url,
        method: 'get',
        params: {
          company_id:this.$store.getters.userinfo.company_id ,
        }
      }).then(res => {
        let data = res.data;

        // if
        if (keyword==='物料模版'){
          this.$refs.formArea.modeldata = data.materials;
        }
        if (keyword==='设备模版'){
          this.$refs.formArea.modeldata = data.equipments;

        }
        if (keyword==='产品模版'){
          this.$refs.formArea.modeldata = data.materials;
        }

      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    }
  }
}
</script>

<style scoped>

</style>







