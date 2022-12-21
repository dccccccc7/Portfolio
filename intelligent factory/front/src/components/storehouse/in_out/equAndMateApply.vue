<template>
  <div style="margin:30px 50px ;">
    <template >
      <el-table
          v-loading="loading"
          :data="tableData"
          style="width: 100%">
        <el-table-column
            fixed
            width="150"
            prop="storage_id"
            label="仓库编号">
        </el-table-column>
        <el-table-column
            prop="shelf_id"
            width="150"
            label="货架编号">
        </el-table-column>
        <el-table-column
            prop="location"
            width="150"
            label="库位信息">
        </el-table-column>
        <el-table-column
            prop="goods_id"
            width="150"
            label="物品编号">
        </el-table-column>
        <el-table-column
            prop="goods_name"
            width="150"
            label="物品名称">
        </el-table-column>
        <el-table-column
            prop="types"
            width="150"
            label="物品类型">
        </el-table-column>

        <el-table-column
            prop="quantity"
            width="150"
            label="数量">
        </el-table-column>
        <el-table-column
            prop="created_by"
            width="150"
            label="申请人">
        </el-table-column>
        <el-table-column
            prop="created_by"
            width="150"
            label="申请时间">
        </el-table-column>
        <el-table-column
            prop="in_out"
            width="150"
            label="出入库">
        </el-table-column>
        <el-table-column
            fixed="right"
            width="100"
            label="操作">
          <template slot-scope="scope">
            <el-button @click="approval(scope.$index,scope.row)" type="text" size="small">批准</el-button>
            <el-button @click="deny(scope.$index,scope.row)" type="text" size="small">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
    <div class="block" style="padding: 10px;margin-top: 10px">
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
  name: "equAndMateApply",
  data(){
    return {
      tableData:[],
      loading:false,
      total: 1,
      page:{
        pages: 7,
        current: 1,
      },
    }
  },
  created() {
    this.getData()
  },
  methods:{
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
        pages:this.page.pages
      }
      console.log('物料设备请求数据getdata的参数req:',req)
      my_request({
        url:'goods/addItem',
        method:'get',
        params:req
      }).then(res=>{
        console.log('物料设备请求数据getdata的返回值res123:',res)

        if (res.data.success === true){
          this.tableData = res.data.result.records
          this.total = res.data.result.total
        }else {
          this.$message.error('查询异常')
        }
      }).catch(err=>{
        console.log('请求数据getdata的返回值err:',err)
        this.$message.error('服务器异常')
      })

    },
    approval(index,row){
      let req = {
        uuid: row.uuid,
        message: 1,  //1为同意 0 为拒绝
        user :this.$store.getters.userinfo.id
        // user :1
      }
      console.log('批准的req',req)
      my_request({
        url:'goods/addConfirm',
        method:'get',
        params: req
      }).then(res=>{
        console.log('同意的res',res)
        if (res.data.success===true){
          this.$message({
            type:'success',
            message:'批准成功'
          })
          this.getData()
        }else {
          this.$message.error('批准失败')
        }
      }).catch(err=>{
        console.log('统一的err:',err)
        this.$message.error('服务器异常')
      })


    },
    deny(index,row){
      let req = {
        uuid: row.uuid,
        message: 0,  //1为同意 0 为拒绝
        user :this.$store.getters.userinfo.id
        // user :1
      }
      console.log('批准的req',req)
      my_request({
        url:'goods/addConfirm',
        method:'get',
        params: req
      }).then(res=>{
        console.log('拒绝的res',res)
        if (res.data.success===true){
          this.$message({
            type:'success',
            message:'拒绝成功'
          })
          this.getData()

        }else {
          this.$message.error('拒绝失败')
        }
      }).catch(err=>{
        console.log('拒绝的err:',err)
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