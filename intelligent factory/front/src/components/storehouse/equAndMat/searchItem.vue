<template>
  <div style=" ;width: 80%;height:80%;margin: 10px auto 0;">
    <div style="width: 100%;height: 40px;margin: 10px 0;margin-bottom: 20px ;display: flex;justify-content: space-between">
      <div style="width: 30%;height: 40px; ;display: flex;justify-content: flex-start">
        <div style="width: 80%;height: 80px;display: flex;justify-content: space-between">
          <el-form :model="input" ref="input" label-width="100px" class="demo-ruleForm" style="display: flex;justify-content: space-around">
            <el-form-item
                label="仓库编号"
                prop="storage_id"
                :rules="[
                        { required: true, message: '仓库编号不能为空'},
                      ]">
              <el-input style="width: 200px" v-model.trim="input.storage_id" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item
                label="货架编号"
                prop="shelf_id"
                :rules="[
                        { required: true, message: '货架编号不能为空'},
                      ]">
              <el-input style="width: 200px" v-model.trim="input.shelf_id" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
        </div>

      </div>

      <div style="width: 48%;height: 40px;justify-content: flex-start;display: flex "> <el-button  @click="submitForm('input')">搜索</el-button></div>

    </div>
    <template >
      <el-table stripe
          v-loading="loading"
          :data="tableData"
          style="width: 100%">
        <el-table-column
            prop="goods_id"
            label="物品编号">
        </el-table-column>
        <el-table-column
            prop="goods_name"
            label="物品名称">
        </el-table-column>
        <el-table-column
            prop="types"
            label="物品类型">
        </el-table-column>
        <el-table-column
            prop="id"
            label="库位信息">
        </el-table-column>
        <el-table-column
            prop="quantity"
            label="物品数量">
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
  name: "searchItem",
  data(){
    return {
      userMsg:'',
      updateFlag:false,
      updateInfo: {
        storage_id:'',//仓库号
        shelf_id:'',//货架号
        location:'',//位置
        id:'',//编号
        quantity:'',//库存
        update:'',
        type:'',
        user:'',
      },
      tableData:[


      ],
      input :{
        storage_id:'',
        shelf_id:'',
      },
      loading:false,
      total: 1,
      page:{
        pages: 13,
        current: 1,
      },
    }
  },
  created(){
    this.userMsg=JSON.parse(sessionStorage.getItem('userinfo'))
    console.log(this.userMsg)

  },
  methods:{
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.getData()
        }
      });
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
        company_id:this.userMsg.company_id,
        storage_id: this.input.storage_id,
        shelf_id: this.input.shelf_id,
        current: this.page.current,
        pages:this.page.pages
      }
      console.log('搜索信息的ereq',req)
      my_request({
        url:'/goods/goodsItem',
        method:'get',
        params : req
      }).then(res=>{
        console.log('res:',res)
        if(res.data.success===true){
          this.tableData = res.data.result.records
          this.total = res.data.total
          this.$message({
            type:'success',
            message:'查询成功'
          })
        }else {
          this.$message.error('无该条信息')
        }
      }).catch(err=>{
        this.$message.error('服务器异常')
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
  }
}
</script>

<style scoped>
/deep/ td {
  padding:3px !important;

  height: 40px;
  overflow: hidden;
}
</style>