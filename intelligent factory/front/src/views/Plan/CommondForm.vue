<template>
  <div id="commond-form">
    <h1>需求单处理</h1>
    <el-form :inline="true" label-width="90px">
      <search ref="soushuo">
 
        <el-form-item label="产品品牌">
          <el-input type="text" v-model="productBrand"></el-input>
        </el-form-item>

        <el-form-item label="需求单单号">
          <el-input type="text" v-model="demandNo"></el-input>
        </el-form-item>

        <el-button @click="find()" slot="button" type="primary">搜索</el-button>
      </search>
    </el-form>
    <div>
      <el-button @click="updateNo">申请</el-button>
      <router-view></router-view>
    </div>
    
    <div>
      <el-table :data="result" border style="margin-left:50px;width:1270px">
        <el-table-column prop="no" label="单号"></el-table-column>
        <el-table-column prop="createdTime" label="创建时间" :formatter="timeFormat"></el-table-column>
        <el-table-column prop="product.brand" label="产品品牌"></el-table-column>
        <el-table-column prop="product.style" label="产品样式"></el-table-column>
        <el-table-column prop="product.color" label="产品颜色"></el-table-column>
        <el-table-column prop="quantity" label="预期数量"></el-table-column>
        <el-table-column prop="expectedDate" label="交付时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button v-if="scope.row.waiting" @click="distribute(scope.row)" size="small">
                分配
            </el-button>
            <el-button @click="apply(scope.row.no)" size="small">申请</el-button>
          </template>  
        </el-table-column>
      </el-table>

      
    </div>
    <div>
      <page-bar :all="all" :page="page"></page-bar>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import request from "../../network/Plan/CommondForm.js";
import Search from "../../components/Plan/search/Search.vue";
import PageBar from "../../components/Plan/pageBar/PageBar.vue";

export default {
  components: {
    Search,
    PageBar,
  },
  name: "CommondForm",
  data() {
    return {
      userId: "",
      www:[{
        no:123,
        product:{
          style:5
        }
      }],
      result: [],
      all: 10,
      page: 1,
      demandNo: "",
      productBrand: "",
      begintime: "",
      overtime: "",
      no: "",
      isShow: {},
    };
  },

  activated() {
    request({
      url: "/demandForm/getDemandFormPageByCriteria",
      params:{
        pageSize: 10,
        company_id:this.$store.getters.userinfo.company_id
      }
    })
      .then((res) => {
        console.log(res);
        let result = res.data.result;
        this.result = result.records;
        this.page = result.current;
        this.all = result.pages;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    timeFormat(row,column){
      if(row.createdTime){
        return row.createdTime.slice(0,10)
      }else{
        return row.createdTime
      }
      },
    apply(val){
      this.$router.push('/commondform/apply/'+ val)
    },
    distribute(item) {
      // this.$parent.distribute(item);
      this.$router.push({
        path:'/distribution',
        query:item
      })
    },

    step() {
      this.$router.push("/commondform")
      this.$router.back(-1)
      this.$router.go(0)
    },
    updateNo() {
      let date = new Date();
      this.no =
        "17" +
        date.getFullYear() +
        (date.getMonth() + 1).toString().padStart(2, "0") +
        date.getDate().toString().padStart(2, "0") +
        Date.now().toString().slice(-8);
      this.$router.push("/commondform/apply/" + this.no);
    },
    find() {
      request({
        url: "/demandForm/getDemandFormPageByCriteria",
        params: {
          beginTimestamp:
            this.begintime == "" ? null : this.begintime + " 08:00:00",
          endTimestamp:
            this.overtime == "" ? null : this.overtime + " 08:00:00",
          no: !this.demandNo ? null : this.demandNo.trim(),
          "product.brand": !this.productBrand ? null : this.productBrand.trim(),
          pageNo: this.page,
          pageSize: 10,
          company_id:this.$store.getters.userinfo.company_id
        },
      })
        .then((res) => {
          let result = res.data.result;
          this.result = result.records;
          this.page = result.current;
          this.all = result.pages;
        })
        .catch((err) => {
          console.log("错误");
        });

      // console.log(this.result);
    },
    changePage(val) {
      this.page = val;
    },
    changeAll(val) {
      this.all = val;
    },
  },
};
</script>

<style>

</style>