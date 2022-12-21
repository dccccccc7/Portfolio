<template>




  <div style="width: 80%;height:80%;margin: 10px auto 0;">

    <div style="width: 100%;height: 40px;margin-bottom: 10px;display: flex;justify-content: flex-start">
      <div style="width: 100%;height: auto;display: flex;justify-content: start">
<!--        ------------------->
        <el-form :model="input" ref="input" label-width="100px" class="demo-dynamic">
          <el-form-item
              prop="input"
              label="库区编号"
              :rules="[
                        // { required: true, message: '请输入库区编号', trigger: 'blur' },
                      ]">
            <el-input v-model.trim="input.input"></el-input>
          </el-form-item>

        </el-form>
        <el-button  @click="submitForm('input')">搜索</el-button>
        <!--        ------------------->
      </div>
    </div>
    <div  style="width: auto;height: auto">
      <el-table
          :data="tableData"
          v-loading="loading"
          style="width: 100%">

        <el-table-column
            label="库区编号"
            prop="id">
        </el-table-column>
        <el-table-column
            label="创建时间"
            prop="created_time">
        </el-table-column>
        <el-table-column
            label="创建人"
            prop="created_by">
        </el-table-column>
        <el-table-column
            label="货架数量"
            prop="shelf_Num">
        </el-table-column>


        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="success"
                @click="handleSearch(scope.$index, scope.row)">查看
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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

  </div>
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";
import {request} from "@/network/request";

export default {
  name: "tableSector",
  data(){
    return {
      loading:false,
      page:{
        pages: 10,
        current: 1,
      },
      total:1,
      show:false,
      // input:'',
      incrementShelfFlag: false,
      addShelfInfo:{
        shelf_id:''
      },
      input: {
        input:''
      },


      tableData: [
        {
          id: '12987121',
          shelf_Num: '129',
          created_time: '2018-29-29',
          created_by: '高某人',
        },
      ],
    }
  },
  created() {
    console.log('sector----',this.$route.query)
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
      let req= {
        current: this.page.current,  //页数 从1开始
        pages: this.page.pages  // 每页多少数据
      }
      this.loading = true
      my_request({
        url: 'storage/storageItem',
        methods:'get',
        params: req
      }).then(res=>{
        console.log('tableSector的getData：res',res)
        if (res.data.success===true){
          console.log('???')
          this.tableData = res.data.result.records;
          this.total = res.data.result.total

        }else {
          this.$message.error('查询失败')
        }
      }).catch(err=>{
        console.log('store查询所有信息 getdata：err',err)
        this.$message.error('服务器异常')
      })

      this.loading = false

    },
    // table chakan
    handleSearch(index, row) {
      this.$router.push({
        path: '/storehouse/storageMain/ShelfInfo',
        query:{
          storage_id: row.id,
        }
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
    },
    handleDelete(index, row) {
      console.log(index, row);
      let req = {
        storage_id:row.id,
        user:this.$store.getters.userinfo.id
        // user:1
      }
      this.loading = true
      console.log(req,'req')
      //如果shelf内的物料军为空，则可以删除
      my_request({
        url: 'storage/delete',
        methods: 'get',
        params: req
      }).then(res=>{
        console.log('删除的res',res)
        if (res.data.success===true){
          this.$message({
            type:'success',
            message:"删除成功"
          })
        }else {
          this.$message.error('删除失败')
        }

        //删除所有库区内所有货架，删除库区
        //删除成功？ 权限不足？ 库区内仍有物料
        this.getData()
      }).catch(err=>{
        console.log('删除库区的err',err)
        this.$message.error('服务器异常')
      })
      this.loading = false

    },

    //-----
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.input.input==='')
          {
            this.getData()
          }else {
            let req = {
              storage_id: this.input.input
            }
            this.loading = true

            console.log('查询某条库区的req：',req)

            my_request({
              url: 'storage/search',
              method: 'get',
              params: req

            }).then(res => {
              console.log('查询某条库区的res：',res)
              if (res.data.success === true){
                let o = []
                o.push(res.data.result)
                this.tableData = o
                this.$message({
                  type:'success',
                  message:'查询成功'
                })
              }else {
                this.$message.error('查询失败')
              }
              this.show = true
            }).catch(err => {
              console.log('查询某条库区的err',err)
                this.$message.error('服务器异常')
            })
            //如果搜索成功
            this.show = true
            this.loading = false

          }
        } else {
          console.log('error submit!!');
          return false;
        }

      });
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

.el-select .el-input {
  width: 230px;
}

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.mainAdd .el-input .el-input__inner {
  width: 30%;
}

.mainAdd {
  width: 40%;
  height: auto;
  margin-top: 20px;
}

.mainAdd .el-input {
  width: 80%;
  margin-right: 20px;
}

.mainAdd .upupup {
  margin-right: 90px;
}

#theBaseMainBox {
  display: flex;
  /*background-color: gray;*/
  height: 600px;
  flex-direction: column;
  width: 1500px;
  margin: 80px;

}

#theBaseHeader {
  /*background-color: aliceblue;*/
  height: 15%;
  width: 100%;
  display: flex;
  justify-content: space-between;
  /*align-items: center;*/
}

#theBaseMain {
  /*background-color: lightgreen;*/
  height: 80%;

  width: 100%;

}
</style>