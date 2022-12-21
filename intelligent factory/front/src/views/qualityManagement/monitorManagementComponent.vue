<template>
  <el-container>
        <el-header style="margin-top: 2%">
          <el-menu
              class="categories"
              default-active="0"
              active-text-color="red"
              mode="horizontal">
            <el-menu-item index="0" @click="showWhat = 'EditForm'">
              <i class="el-icon-menu"></i>
              <span slot="title">录入质检结果</span>
            </el-menu-item>
            <el-menu-item index="1" @click="showWhat = 'qualityResults'">
              <i class="el-icon-menu"></i>
              <span slot="title">查看质检结果</span>
            </el-menu-item>
<!--            <el-menu-item index="2" @click="showWhat = 'xcxc'">-->
<!--              <i class="el-icon-menu"></i>-->
<!--              <span slot="title">瑕疵统计</span>-->
<!--            </el-menu-item>-->
          </el-menu>
        </el-header>
<!--    <el-aside style="width: 50%;margin-top: 10%;">-->
<!--      <switch></switch>-->
<!--      <SideMenu @indexSelect="listByCategory" ref="sideMenu"></SideMenu>-->
<!--      <edit-form></edit-form>-->
<!--    </el-aside>-->
    <el-main style="margin-top: 1%;">
      <!--动态切换显隐，组件-->
          <component :is="showWhat" class="form-area" ref="formArea"></component>
<!--      <quality-results class="form-area" ref="formArea"></quality-results>-->
    </el-main>
  </el-container>
</template>

<script>
import EditForm from "@/components/qualityManagement/EditForm";
import qualityResults from "@/components/qualityManagement/qualityResults";
import SideMenu from "@/components/qualityManagement/SideMenu";
import {request} from "@/network/request";

export default {
  name: 'monitorManagementComponent',
  components: {EditForm, qualityResults, SideMenu},
  data() {
    return {
      showWhat: "EditForm"
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

