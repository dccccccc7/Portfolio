<template>
  <el-container>
    <el-main >
      <quality-echarts></quality-echarts>
    </el-main>
  </el-container>
</template>

<script>
import qualityEcharts from "@/components/qualityManagement/quality/qualityEcharts";
import {request} from "@/network/request";

export default {
  name: 'qualityManagementComponent',
  components: {qualityEcharts},
  data() {
    return {
      showWhat: "qualityEcharts"
    }
  },
  methods: {
    listByCategory() {
      var _this = this
      var cid = _this.$refs.sideMenu.cid
      var url = 'categories/' + cid + '/books'
      request({
        url: 'data/books',
        method: 'get',
      }).then(res => {
        let {code, data} = res.data;
        _this.$refs.formArea.qualityForm = data.books;
      }).catch(err => {
        this.$message.error('网络出错，请稍后再试');
      });
    }
  }
}
</script>

<style scoped>
/*.form-area {*/
/*  width: fit-content;*/
/*  width: -moz-fit-content;*/
/*  margin-left: auto;*/
/*  margin-right: auto;*/
/*}*/
</style>

