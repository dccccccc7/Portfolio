<template>

  <div id="theBaseMainBox">
    <div style="width: 80%;height: auto;;margin-bottom: 20px;margin-left: 70px">
      <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          :router="true"
          :unique-opened="true"
          @select="handleSelect">

        <el-menu-item index="/work/apply/workshopInfo">车间信息</el-menu-item>
        <el-menu-item index="/work/apply/lineInfo">产线信息</el-menu-item>
        <el-menu-item index="/work/apply/stationInfo">工位信息</el-menu-item>
      </el-menu>
    </div>

    <div id="theBaseMain">

        <router-view></router-view>

    </div>
  </div>

</template>

<script>
import {my_request} from "@/network/requests/workshop_request";

export default {
  name: "applyPro",
  data() {
    return {

      activeIndex: '1',
      loading:false,
      //是否展示当前页面， 作为一个表示

      path: ''
    }
  },
  activated() {
    this.$router.push(this.path);
    this.getData();
  },
  created() {
    this.getData()
  },
  beforeRouteLeave(to, from, next){
    this.path = this.$route.path;
    next();
  },
  methods: {
    handleSelect(key, keyPath) {
      this.$emit('routes', keyPath);
    },
    //搜索方法
    getData(){
      my_request({
        url:'workshop/apply',
        methods: 'get'
      }).then(res=>{

        // 返回值：
        // this.tableData = [
        // {
        // applyType:'新建',
        //     applyDate:'2001-201-201',
        //     applyUser:'黃河',
        //     info:{
        //   workshopId:'2332',
        //       lineId: '213',
        //   stationId:'123',
        // },
        // ]、
        // this.tableData = res.



      }).catch(err=>{

      })
    },

    preclick(curpage){
      this.page.curpage=curpage
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)
    },
    nextclick(curpage){
      this.page.curpage=curpage
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)

    },
    curChange(curpage){
      this.page.curpage=curpage
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)
    },


  },

}
</script>

<style scoped>

#theBaseMainBox {
  /*display: flex;*/
  /*background-color: gray;*/
  height: 80%;
  /*flex-direction: column;*/
  /*justify-content: center;*/
  /*margin-top: 30px;*/

  width: 80%;
}



#theBaseMain {
  /*background-color: beige;*/
  height: 800px;
  width: 100%;
  padding: 0 100px;
  overflow: auto;
}


#theMainTable th {
  padding: 10px 20px !important;
  /*background-color: aliceblue;*/
  font-size: large;
  /*border: 1px solid red;*/
}

/deep/ #theMainTable td {
  padding: 6px 20px !important;
  /*border: 1px solid blue;*/

}


#theSubTable th {
  padding: 10px 20px !important;
  /*background-color: aliceblue;*/
  font-size: large;
  /*border: 1px solid red;*/
}

/deep/ #theSubTable td {
  padding: 6px 20px !important;
  /*border: 1px solid blue;*/

}



</style>