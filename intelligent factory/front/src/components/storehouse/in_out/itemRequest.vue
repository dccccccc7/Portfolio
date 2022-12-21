<template>
<div style="margin: 30px">
  <template >
<!--    搜索栏-->
    <el-form :inline="true" :model="itemRequest" :rules="rulesForm" ref="itemRequest" class="demo-form-inline" style="width: 100%">
      <el-form-item label="起始时间" prop="time1" style="float: left;" >
        <el-date-picker type="date" placeholder="选择起始时间" v-model="itemRequest.time1"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerBeginDateBefore"
                style="width: 190px;"></el-date-picker>

      </el-form-item>
      <el-form-item label="终止时间" prop="time2" style="float: left">
        <el-date-picker type="date" placeholder="选择终止时间" v-model="itemRequest.time2"
                        format="yyyy 年 MM 月 dd 日"
                        value-format="yyyy-MM-dd"
                        :picker-options="pickerBeginDateBefore"
                        style="width: 190px;"></el-date-picker>

      </el-form-item>
      <el-form-item label="编号" prop="goods_id" style="float: left">
        <el-input style="float: left;width: 90px" placeholder="编号" clearable v-model.trim="itemRequest.goods_id"></el-input>
      </el-form-item>
      <el-form-item label="名称" prop="goods_name"  style="float: left">
        <el-input style="float: left;width: 90px" placeholder="名称" clearable v-model.trim="itemRequest.goods_name"></el-input>
      </el-form-item>
      <el-form-item label="种类" prop="types"  style="float: left">
        <el-select style="float: left;width: 90px" v-model="itemRequest.types" placeholder="种类" >
          <el-option label="物料" value="material"></el-option>
          <el-option label="设备" value="equipment"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item style="float: right">
        <el-button type="primary" @click="getData()">查询</el-button>
        <el-button @click="resetForm('itemRequest')">重 置</el-button>
      </el-form-item>
    </el-form>

    <el-table
        :header-cell-style="{'text-align':'center'}"
        :cell-style="{'text-align':'center'}"
        v-loading="loading"
        :data="tableData"
        style="width: 100%">
      <el-table-column
          prop="workshop_id"
          label="车间编号">
      </el-table-column>
      <el-table-column
          prop="goods_id"
          label="物品编号">
      </el-table-column>
      <el-table-column
          prop="goods_name"
          label="物品名称">
      </el-table-column>
      <el-table-column
          prop="quantity"
          label="申请数量">
      </el-table-column>
      <el-table-column
          :min-width="110"
          prop="created_time"
          label="申请时间">
      </el-table-column>
      <el-table-column
          prop="created_by"
          label="申请人">
      </el-table-column>
      <el-table-column
          prop="quantity_sum"
          label="库存数量">
      </el-table-column>
      <el-table-column
          prop="in_out"
          label="出入库">
      </el-table-column>
      <el-table-column
          label="操作">
        <template slot-scope="scope" >
          <el-button @click="inOutApply(scope.$index,scope.row)" :disabled="scope.row.quantity_sum < scope.row.quantity && scope.row.types=='出库' " type="text" size="small">出入库申请</el-button>
        </template>
      </el-table-column>

    </el-table>
  </template>
  <div class="block" style="padding: 10px;margin-top: 20px">
    <el-pagination
        @prev-click="preclick"
        @next-click="nextclick"
        @current-change="curChange"
        :hide-on-single-page=true
        :total="total"
        background
        layout="total,prev, pager, next, jumper"
        :page-size="page.pages">
    </el-pagination>
  </div>
</div>
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";

export default {
  name: "itemRequest",
  data(){
    return {
      userMsg:'',
      enough:true,
      itemRequest:{
        goods_id:'', //编号
        goods_name:'', //名称
        types:'',
        time1:'2022-01-01', //起始时间
        time2:'2022-01-01', //终止时间
      },
      tableData:[

      ],
      rulesForm:{
      },
      loading:false,
      total: 1,
      page:{
        pages: 8,
        current: 1,
      },
      pickerBeginDateBefore:{
        disabledDate(time) {
          return time.getTime() > Date.now();
        }
      },
    }
  },
  created() {
    this.userMsg=JSON.parse(sessionStorage.getItem('userinfo'))
    console.log(this.userMsg)
    this.getData()
  },
  methods:{
    //时间格式转换
    formatDate(value) {
      var year = value.substr(0, 4)
      var month = value.substr(5, 2)
      var day = value.substr(8, 2)
      var hour = value.substr(11, 2)
      var min = value.substr(14, 2)
      var second = value.substr(17,2)
      return year + "-" + month + "-" + day + " " + hour + ":" + min + ":"+second
    },

    preclick(current){
      this.page.current=current
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)
    },
    nextclick(current){
      this.page.current=current
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)

    },
    curChange(current){
      this.page.current=current
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)
    },

    getData(){
      let req = {
        current: this.page.current,
        pages:this.page.pages,
        goods_id: this.itemRequest.goods_id,
        goods_name: this.itemRequest.goods_name,
        type: this.itemRequest.types,
        time1: this.itemRequest.time1,
        time2: this.itemRequest.time2,
        company_id:this.userMsg.company_id
      }
      console.log('req:',req)

      my_request({
        url:'goods/searchApply',
        method:'get',
        params:req
      }).then(res=>{
        console.log(res)
        if (res.data.success === true){
          this.tableData = res.data.result.records
          this.total = res.data.result.total
          for (let item of this.tableData) {
            item.created_time = this.formatDate(item.created_time)
          }
        }else {
          this.$message.error('查询异常')
        }
      }).catch(err=>{
        console.log('请求数据getdata的返回值err:',err)
        this.$message.error('服务器异常')
      })

    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    inOutApply(index,row){
      let  req = {
        uuid : row.uuid,
        user:this.$store.getters.userinfo.id,
        // 也可能是 company_id : row.company_id,
        company_id:this.userMsg.company_id
        // user:1
      }
      console.log('申请出库的req',req)
      this.loading = true

      my_request({
        url:'goods/setApply',
        params:req,
        method:'get'
      }).then(res=>{
        console.log('发出出库申请的res',res)
        if(res.data.success===true){
          this.$message({
            type:'success',
            message:'申请成功'
          })
          this.getData()
          setTimeout(()=>{
            this.loading=false
          },1000)
        }else {
          this.$message.error('申请失败')
        }
      }).catch(err=>{
        this.$message.error('服务器异常')
      })

    }
  }
}
</script>

<style scoped>
/deep/ td {
  padding:3px !important;

  height: 6px;
  overflow: hidden;
}
</style>