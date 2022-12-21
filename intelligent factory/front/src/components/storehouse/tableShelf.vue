<!--这是货架管理-->
<template>
  <div style="width: 88%;height:80%;margin: 15px auto 0;">

    <div style="width: 100%;height: 40px;display: flex;flex-direction:row;">
      <el-button  icon="el-icon-arrow-left" @click="goOff()">返回</el-button>
      <span style="font-family: 宋体 ;width: 20%;font-size: larger;color: #2c3e50;line-height: 40px">库区编号: {{this.$route.query.storage_id}}</span>
      <div style="width: 70%;height: 40px;display: flex;justify-content: flex-start">
        <div style="width: 100%;height: auto;display: flex;justify-content: start">
          <el-form :model="input" ref="input" label-width="100px" class="demo-dynamic">
            <el-form-item
                prop="input"
                label="货架编号"
                :rules="[
                        { required: true, message: '请输入货架编号', trigger: 'blur' },
                      ]">
              <el-input v-model.trim="input.input"></el-input>
            </el-form-item>

          </el-form>
          <el-button type="primary" style="margin-left: 20px" @click="submitForm1('input')">搜索</el-button>
        </div>
      </div>
      <div style="width: 30%;height: 40px;display: flex;justify-content: flex-start;">
        <el-button @click="deleteStorage">删除库区</el-button>
        <el-button  @click="incrementShelfFlag = true">增加货架</el-button>
        <el-dialog
            title="提示"
            :visible.sync="incrementShelfFlag"
            width="25%"
            :before-close="handleClose">

          <el-form :model="shelf" ref="shelf" label-width="100px" class="demo-ruleForm">
            <el-form-item
                label="货架编号"
                prop="id"
                :rules="[
                        { required: true, message: '货架编号不能为空'},

                      ]">
              <el-input v-model.trim="shelf.id" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="addShelf('shelf')">提交</el-button>

            </el-form-item>
          </el-form>
        </el-dialog>
      </div>
    </div>

    <div  style="width: auto;height: auto;margin-top: 15px">
      <!--    卡片式展示-->
      <el-row class="el-row1">
        <el-tooltip effect="transparent" placement="bottom"
                    v-for="(item,index) in shelfForm"
                    :key="item.id">
          <el-card class="card" bodyStyle="padding:10px" shadow="hover">
            <div class="info">
              <div class="clearfix" slot="header" >
                <span style="float: left">货架编号：{{ item.id}}</span>
                <span>
                  <i class="el-icon-delete" @click="deleteShelf(item)"></i>
                </span>
                <span style="float: right;margin-right: 10px">
                  <i class="el-icon-document" @click="see(item)"></i>
                </span>

              </div>
              <div class="text item">
                <el-divider></el-divider>
                <p>创建时间：{{ item.created_time}}</p>
                <p>创建人：{{ item.created_by}}</p>
              </div>
            </div>
          </el-card>
        </el-tooltip>
        <shelf-form @onSubmit="getData()" ref="edit"></shelf-form>
      </el-row>
    </div>

    <el-dialog
        title="转移后位置信息"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">

              <el-form :model="info" ref="info" label-width="100px" class="demo-ruleForm">
                <el-form-item
                    label="库区编号"
                    prop="sectorId"
                    :rules="[
                              { required: true, message: '库区编号不能为空'},
                            ]">
                  <el-input v-model="info.sectorId" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item
                    label="货架编号"
                    prop="shelfId"
                    :rules="[
                              { required: true, message: '货架编号不能为空'},
                            ]">
                  <el-input v-model="info.shelfId" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item
                    label="库位信息"
                    prop="location"
                    :rules="[
                              { required: true, message: '库位信息不能为空'},
                            ]">
                  <el-input v-model="info.location" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitForm('info')">提交</el-button>
                  <el-button @click="resetForm('info')">重置</el-button>
                </el-form-item>
              </el-form>

    </el-dialog>
<!--    分页-->
    <div class="block" >
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
</template>

<script>
import {my_request} from "@/network/requests/workshop_request";
import {request} from "@/network/request";
export default {
  name: "tableShelf",

  data(){
    return {
      userMsg:'',
      page:{
        pages: 8,
        current: 1,
      },
      total:1,

      dialogVisible:false,
      shelf:{id:''},
      //  增加货架
      incrementShelfFlag: false,
      addShelfInfo:{
        shelfId:''
      },
      input:{input:''},
      shelfForm: [
      ],
    //  物品转移信息
      item: '',
      info:{
        sectorId: '',
        shelfId:'',
        location:'',
      }
    }
  },
  created() {
    this.userMsg=JSON.parse(sessionStorage.getItem('userinfo'))
    console.log(this.userMsg)
    console.log(this.$route.query,'shelf')
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
    //返回上一级
    goOff(){
      this.$router.go(-1)
    },
    //初始界面读取数据
    getData(){
      let req = {
        storage_id:this.$route.query.storage_id,
        current: this.page.current,  //页数 从1开始
        pages: this.page.pages, // 每页多少数据
        user:this.$store.getters.userinfo.id,
        company_id:this.userMsg.company_id

        // user:1
      };
      console.log('getData的req，',req)

      request({
        url:'shelf/shelfItemById',
        params: req,
        method:'get'
      }).then(res=>{
        console.log('getData的res，',res)
        this.shelfForm = res.data.result.records
        this.total = res.data.result.total
        for (let item of this.shelfForm) {
          item.created_time = this.formatDate(item.created_time)
        }
        if(res.data.success===true){
          for(let i = 0; i< this.shelfForm.length; i++){
            if(this.shelfForm[i].types===null){
              this.shelfForm[i].types = '无'
            }
          }
          console.log('货架',this.shelfForm)

        }else {
          this.$message.error('查询失败')
        }
      }).catch(err=>{
        console.log('getData的err，',err)
        this.$message.error('服务器异常')
      })
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

    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },

    editShelf(index,row){
      this.item = {
        index:index,
        row:row
      }
      this.dialogVisible = true;

    },
    //删除货架
    deleteShelf(item){
        let req = {
          storage_id : this.$route.query.storage_id,
          id: item.id,
          user:this.$store.getters.userinfo.id,
          // 或者是 company_id:item.company_id
          company_id:this.userMsg.company_id
          // user:1
        }
      console.log('删除或加的req',req)

      my_request({
        url:'shelf/delete',
        params:req,
        method:'get'
      }).then(res=>{
        console.log('删除或加的res',res)
        if(res.data.success===true){
          this.$message({
            type:'success',
            message:'删除成功'
          })
          this.getData()
        }else {
          this.$message.error('删除失败')
        }
      //  返回 failed 删除失败（物料山仍有货架未清理） 、success  删除成功
      }).catch(err=>{
        console.log('删除或加的err',err)
        this.$message.error('服务器异常')

      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {

          let  req = {
            sectorId:this.$route.query.sectorId,
            shelfId:this.$route.query.shelfId,
            itemId:this.item.row.id,// 物品id
            newSectorId: this.info.sectorId,
            newShelfId: this.info.shelfId,
            newLocation: this.info.location,
            company_id:this.userMsg.company_id

        }
          console.log('req---------------------',req)
          console.log(this.item)
          request({
            url: 'storehouse/moveItem',
            methods:'get',
            params:'get'
          }).then(res=>{


            this.dialogVisible = false
            this.getData()
            this.$message({
              type:'success',
              message:'转移成功'
            })
          }).catch(err=>{
            this.$message.error('转移失败')
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    submitForm1(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.input.input==='')
          {
            this.getData()
          }else {
            let req = {
              storage_id:this.$route.query.storage_id,
              id: this.input.input,
              company_id:this.userMsg.company_id
            }
            console.log('查询某个货架的req',req)
            my_request({
              url: 'shelf/search',
              method: 'get',
              params: req

            }).then(res => {

              console.log('查询某个货架的res',res)
              this.shelfForm = res.data.result
              if(res.data.success===true){
                this.$message({
                  type:'success',
                  message:'查询成功'
                })
              }else {
                this.$message.error('查询失败')
              }
              // this.tableData = req.
              //如果搜索成功
              this.show = true
            }).catch(err => {
              console.log('查询某个货架的err',err)
              this.$message.error('服务器异常')
            })
            //如果搜索成功
            this.show = true
            this.shelfForm = [
              {
                id: '1',
                         category: 'asd',
                         created_time:'2017-23-19',
                         created_by:'asdw',
                          quantity: '1000'
                // 加一个 货架数量？
                //shelf_Num:''221
                // 后面三个有的话就写 没有就控制
              }
            ]
          }
        } else {
          console.log('error submit!!');
          return false;
        }

      });
    },
  // 删除库区
    deleteStorage(){

      let req = {
        storage_id: this.$route.query.storage_id,
        user:this.$store.getters.userinfo.id,
        company_id:this.userMsg.company_id
        // user:1
      }
      console.log(req,'reerer')
      //如果shelf内的物料军为空，则可以删除
      my_request({
        url: 'storage/delete',
        methods: 'get',
        params: req
      }).then(res=>{
        if (res.data.success===true){

          this.$message({
            type:'success',
            message:"删除成功"
          })
          this.$router.go(-1)
        }else {
          this.$message.error('删除失败')
        }
        //删除所有库区内所有货架，删除库区
        //删除成功？ 权限不足？ 库区内仍有物料
        this.getData()
      }).catch(err=>{
        this.$message.error('服务器异常')
      })
    },

  //  增加货架
    addShelf(formName ){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let req  = {
            storage_id:this.$route.query.storage_id,
            id: this.shelf.id,
            user:this.$store.getters.userinfo.id,
            company_id:this.userMsg.company_id
            // user:1
          }
          my_request({
            url:'shelf/insert',
            params: req,
            method:'get'
          }).then(res=>{
            console.log('增加货架的res：',res)
            if(res.data.success===true){
              this.$message({
                type:"success",
                message:'增加成功'
              })
              this.getData()
            }else {
              this.$message.error('增加失败')
            }

            this.$refs[formName].resetFields();
          }).catch(err=>{
            console.log('增加货架的err：',err)
            this.$message.error('服务器异常')
          })
          this.incrementShelfFlag = false
        } else {
          console.log('error submit!!');
          return false;
        }
      });



    },

  //  查看货架信息
    see(item){
      this.$router.push({
        path: '/storehouse/storageMain/shelfInfoDetail',
        query:{
          storage_id: this.$route.query.storage_id,
          id:item.id
        }
      })
    }
  }
}
</script>

<style scoped>
/deep/  td {
  padding: 6px 20px !important;
  /*border: 1px solid blue;*/

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

.searchBar {
  margin-top: 2%;
  margin-bottom: 3%;
  left: 30%
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