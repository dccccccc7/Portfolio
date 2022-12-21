<template>

  <div style="width: 88%;height:80%;margin: 20px auto 0;">

  <div style="width: 100%;height: 40px;display: flex;justify-content: flex-end">
    <el-button  icon="el-icon-arrow-left" @click="goOff()">返回</el-button>
    <el-button type="primary" @click="dialogVisible1 = true">转移所有物品</el-button>
  </div>
   <template>
    <el-table
        :data="tableData"
        :header-cell-style="{'text-align':'center'}"
        :cell-style="{'text-align':'center'}"
        style="width: 100%;">
      <el-table-column
          prop="id"
          label="库位信息">
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
          prop="types"
          label="物品类型">
      </el-table-column>

      <el-table-column
          prop="quantity"
          label="数量">
      </el-table-column>

      <el-table-column label="操作" >
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="primary"
              @click="transfer(scope.$index, scope.row)">转移
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </template>
    <div class="block" style="padding: 10px;margin-top: 20px">
      <el-pagination
          @prev-click="preclick"
          @next-click="nextclick"
          @current-change="curChange"
          :hide-on-single-page=false
          :total="total"
          background
          layout="total,prev, pager, next, jumper"
          :page-size="page.pages">
      </el-pagination>
    </div>
    <el-dialog
        title="转移后位置信息"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">

      <el-form :model="info" ref="info" label-width="100px" class="demo-ruleForm">
        <el-form-item
            label="库区编号"
            prop="storage_id"
            :rules="[
                              { required: true, message: '库区编号不能为空'},
                            ]">
          <el-input v-model.trim="info.storage_id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
            label="货架编号"
            prop="shelf_id"
            :rules="[
                              { required: true, message: '货架编号不能为空'},
                            ]">
          <el-input v-model.trim.trim="info.shelf_id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
            label="库位信息"
            prop="location"
            :rules="[
                              { required: true, message: '库位信息不能为空'},
                            ]">
          <el-input v-model.trim="info.location" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('info')">提交</el-button>
          <el-button @click="resetForm('info')">重置</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>
    <el-dialog
        title="转移后位置信息"
        :visible.sync="dialogVisible1"
        width="30%"
        :before-close="handleClose">

      <el-form :model="info" ref="info" label-width="100px" class="demo-ruleForm">
        <el-form-item
            label="库区编号"
            prop="storage_id"
            :rules="[
                              { required: true, message: '库区编号不能为空'},
                            ]">
          <el-input v-model.trim="info.storage_id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
            label="货架编号"
            prop="shelf_id"
            :rules="[
                              { required: true, message: '货架编号不能为空'},
                            ]">
          <el-input v-model.trim="info.shelf_id" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm1('info')">提交</el-button>
          <el-button @click="resetForm('info')">重置</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>

</div>
</template>


<script>
import {my_request} from "@/network/requests/workshop_request";
import {request} from "@/network/request";
export default {
  name: "shelfInfo",
  data(){
    return {
      userMsg:'',
      page:{
        pages: 7,
        current: 1,
      },
      total:1,
      item :{
        index:'',
        row:''
      },
      info:{
        storage_id: '',
        shelf_id:'',
        location:'',
      },
      dialogVisible:false,
      dialogVisible1:false,
      tableData:[
        {
          id: '库位信息',
          types: '物品',
          goods_id:'si213',
          quantity:'shu量'
        },

      ]
    }
  },
  created() {
    this.userMsg=JSON.parse(sessionStorage.getItem('userinfo'))
    console.log(this.userMsg)
    this.getData()
  },
  methods:{
    //返回上一级
    goOff(){
      this.$router.go(-1);
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
        storage_id: this.$route.query.storage_id,
        current: this.page.current,  //页数 从1开始
        pages: this.page.pages, // 每页多少数据
        id:this.$route.query.id,
        company_id:this.userMsg.company_id
      }
      my_request({
        url:'goods/search',
        params:req,
        method: 'get'
      }).then(res=> {
        console.log('.asdgetData的res,',res)
        if (res.data.success===true){
          this.tableData = res.data.result

        }else (
            this.$message.error('查询失败')
        )

        //
        // 返回值
        //  {
        //    total:''数据总条数
        //    tableDate:[
        //     {
        //        location: '库位信息',
        //        item: '物品',
        //        itemID:'物品编号'
        //        quantity:'shu量'
        //      },
        //    ]
      //   }
      //   this.total = res.
      //   this.tableData = res.
      }).catch(err=>{
        console.log('getData的err',err)
        this.$message.error('服务器异常')
      })
    },

    transfer(index, row) {
       this.item = {
         index:index,
         row:row
       }
      console.log(this.item)
       this.dialogVisible = true;
    },
    submitForm1(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          let  req = {
            storage_id:this.$route.query.storage_id,
            shelf_id:this.$route.query.id,
            newStorage_id: this.info.storage_id,
            newShelf_id: this.info.shelf_id,
            company_id:this.userMsg.company_id
          }
          console.log('转移整个货架的req',req)

          my_request({
            url: 'shelf/move',
            methods:'get',
            params:req
          }).then(res=>{
            console.log('转移整个货架的res',res)

            if (res.data.success===true){
              this.$message({
                type:'success',
                message:'转移成功'
              })
            }else {
              this.$message.error('转移失败')
            }
            this.dialogVisible = false
            this.getData()

            this.dialogVisible1 =false;
            this.$refs[formName].resetFields();

          }).catch(err=>{
            console.log('转移整个货架的err',err)
            this.$message.error('服务器异常')
          })
        } else {
          console.log('error submit!!');
          return false;
        }
        this.dialogVisible1 =false;
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          let  req = {
            storage_id:this.$route.query.storage_id,
            shelf_id:this.$route.query.id,
            location: this.item.row.id,
            newStorage_id: this.info.storage_id,
            newShelf_id: this.info.shelf_id,
            newLocation: this.info.location,
            company_id:this.userMsg.company_id

          }
          console.log('转移物品的req',req)
          my_request({
            url: 'goods/move',
            methods:'get',
            params:req
          }).then(res=>{

            console.log('转移物品的res',res)
            this.dialogVisible = false
            this.getData()
            if (res.data.success===true){
              this.$message({
                type:'success',
                message:'转移成功'
              })
            }else {
              this.$message.error('转移失败')
            }

            this.dialogVisible =false;

          }).catch(err=>{
            console.log('转移物品的err',err)
            this.$message.error('服务器异常')
          })
        } else {
          console.log('error submit!!');
          return false;
        }
        this.$refs[formName].resetFields();
        this.dialogVisible =false;
      });
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>
/deep/  td {
  padding: 6px 20px !important;
  /*border: 1px solid blue;*/

}
</style>