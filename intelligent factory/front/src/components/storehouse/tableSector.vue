<!--这是库区管理-->
<template>

  <div style="width: 88%;height:80%;margin: 15px auto 0;overflow-x:hidden;">
    <div style="width:100%;height: 40px;display: flex;justify-content: flex-start">
    <div style="width: 100%;height: auto;display: flex;justify-content: start">
        <el-form :model="input" ref="input" label-width="100px" class="demo-dynamic">
          <el-form-item
              prop="input"
              label="库区编号:"
              :rules="[
                        { required: true, message: '请输入库区编号', trigger: 'blur' },
                      ]">
            <el-input v-model.trim="input.input"></el-input>
          </el-form-item>

        </el-form>
        <el-button type="primary" style="margin-left: 20px" @click="submitForm('input')">搜索</el-button>
      </div>
    </div>
    <div  style="width: auto;height: auto;margin-top: 15px;">
<!--card展示-->
      <el-row class="el-row1">
        <el-tooltip effect="transparent" placement="bottom"
                    v-for="item in storeForm"
                    :key="item.id">
          <el-card class="card" bodyStyle="padding:10px" shadow="hover">
            <div class="info">
              <div class="clearfix" slot="header" >
                <span style="float: left">库区编号：{{ item.id}}</span>
                <span>
                <i class="el-icon-delete" @click="handleDelete(item)"></i>
              </span>
                <span style="float: right;margin-right: 10px">
                <i class="el-icon-document" @click="handleSearch(item)"></i>
              </span>
              </div>
              <div class="text item">
                <el-divider></el-divider>
                <p>创建时间：{{ item.created_time}}</p>
                <p>创建人：{{ item.created_by}}</p>
                <p>货架数量：{{ item.shelf_Num}}</p>
              </div>
              <!--            <el-divider></el-divider>-->
              <!--            <i class="el-icon-edit" @click="editDefect(item)"></i>-->
              <!--            <i class="el-icon-delete" @click="deleteDefect(item.defectCode)"></i>-->
            </div>
          </el-card>
        </el-tooltip>
        <store-form @onSubmit="getData()" ref="edit"></store-form>
      </el-row>

<!--      分页处理-->
      <div class="block">
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
    </div>

  </div>
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";
import {request} from "@/network/request";

export default {
  name: "tableSector",

  data() {
    return {
      userMsg:'',
      storeForm: [],
      loading:false,
      page:{
        pages: 8,
        current: 1,
      },
      total:1,
      show:false,
      incrementShelfFlag: false,
      addShelfInfo:{
        shelf_id:''
      },
      input: {
        input:''
      },

    }
  },

  created() {
    this.userMsg=JSON.parse(sessionStorage.getItem('userinfo'))
    console.log(this.userMsg)
    console.log('sector----',this.$route.query)
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
      console.log(this.page.current)
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)
    },
    curChange(current){
      this.page.current=current
      console.log(this.page.current)
      this.loading = true
      setTimeout(()=>{
        this.getData()
        this.loading=false
      },1000)
    },
    //初始界面调取数据
    getData(){
      let req= {
        current: this.page.current,  //页数从1开始
        pages: this.page.pages, // 每页多少数据
        company_id:this.userMsg.company_id
      }
      console.log(this.page.current)
      this.loading = true
      my_request({
        url: 'storage/storageItem',
        methods:'get',
        params: req
      }).then(res=>{
        console.log('tableSector的getData：res',res)
        if (res.data.success===true){
          console.log('???')
          // this.tableData = res.data.result.records;
          this.total = res.data.result.total
          this.storeForm =  res.data.result.records;
          for (let item of this.storeForm) {
            item.created_time = this.formatDate(item.created_time)
          }
        }else {
          this.$message.error('查询失败')
        }
        this.show=true
      }).catch(err=>{
        console.log('store查询所有信息 getdata：err',err)
        this.$message.error('服务器异常')
      })
      this.loading = false
    },
    //查看库区详情
    handleSearch(item) {
      this.$router.push({
        path: '/storehouse/storageMain/ShelfInfo',
        query:{
          storage_id: item.id,
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
    //删除库区
    handleDelete(item) {
      console.log(item);
      let req = {
        storage_id:item.id,
        user:this.$store.getters.userinfo.id,
        // 或者是 company_id:item.company_id
        company_id:this.userMsg.company_id,
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

    //搜索库区
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.input.input==='')
          {
            this.getData()
          }else {
            let req = {
              storage_id: this.input.input,
              company_id:this.userMsg.company_id,
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
                this.storeForm = o
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

.el-row1{
  width: auto;
  height: auto;
  display:flex;
  flex-wrap: wrap;
  align-items: flex-start;
}
/*.el-row1:after{*/
/*  content: '';*/
/*  flex-grow: 99999;*/
/*}*/
.card{
  width: 22%;
  max-height: max-content;
  float: left;
  margin-right: 20px;
  margin-bottom: 20px;
}

img {
  width: 115px;
  height: 172px;
  /*margin: 0 auto;*/
}
.title {
  font-size: 14px;
  text-align: left;
}

.info {
  /*display: flex;*/
}

.el-icon-delete,.el-icon-edit,.el-icon-document{
  cursor: pointer;
  font-size: 20px;
  float: right;
}

a {
  text-decoration: none;
}

a:link, a:visited, a:focus {
  color: #3377aa;
}

.text {
  font-size: 14px;
  text-align: left;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>