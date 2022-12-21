<template>
  <div>
    <el-row>
      <el-col :span="22">
        <el-divider style="background-color: black" content-position="left">
          课序号：{{this.$route.query.cid}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          课程名：{{this.$route.query.cname}}
        </el-divider>
      </el-col>
      <el-col :span="2">
        <el-button size="medium" style="margin: 0px 0px 0px 30px" @click="this.$router.push('/courseList')">返回</el-button>
      </el-col>
    </el-row>
    <el-table
        v-loading="loading"
        element-loading-text="努力加载中>﹏<"
        :data="tableData"
        style="width: 65%;margin: 30px auto"
        :header-cell-style="headStyle"
        :row-style="rowStyle"
        border>
      <el-table-column width="50px" label="节次" align="center">
        <template #default="scope">
          {{scope.$index+1}}
        </template>
      </el-table-column>
      <el-table-column v-for="i in 7" :label=this.weekOption[i] align="center">
        <template #default="scope">
          <el-popover trigger="hover" placement="right" width="250px" v-if="scope.row[i]!=null">
            <template #reference>
              <el-button type="text" style="color: dodgerblue;font-weight: bold"
                         @click="changeVisible(scope.row[i])">有课·编辑</el-button>
            </template>
            <el-descriptions direction="vertical" border size="mini" :column=2>
              <el-descriptions-item label="上课地点">
                {{ scope.row[i]!=null ? scope.row[i].place : "" }}
              </el-descriptions-item>
              <el-descriptions-item label="上课周次">
                第{{ scope.row[i]!=null ? scope.row[i].beginWeek : "" }}~{{ scope.row[i]!=null ? scope.row[i].endWeek : "" }}周
              </el-descriptions-item>
            </el-descriptions>
          </el-popover>
          <el-button type="text" style="color: darkgray" v-else
                     @click="addVisible(scope.column.no,scope.$index+1)">无课·添加</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-card style="width: 500px;margin: auto" v-loading="cardLoading">
      <el-form ref="card" :model="card" :rules="cardRules" label-width="125px">
        <el-form-item prop="cid" label="课序号">
          <el-input v-model="card.cid" placeholder="建议使用数字" disabled></el-input>
        </el-form-item>
        <el-form-item prop="cname" label="课程名">
          <el-input v-model="card.cname" placeholder="请输入课程名"></el-input>
        </el-form-item>
        <el-form-item prop="total" label="最大课余量">
          <el-input-number v-model="card.total"></el-input-number>
        </el-form-item>
        <el-form-item prop="credit" label="学分">
          <el-input-number v-model="card.credit"
                           :precision="1" :step="0.5"
                           :min="0"></el-input-number>
        </el-form-item>
        <el-form-item prop="describes" label="描述">
          <el-input v-model="card.describes" placeholder="请描述你的课程" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="updateCourseInfo">更新基础信息</el-button>
      </div>
    </el-card>
    <el-dialog title="设置课程" v-model="dialogVisible">
      <el-form ref="form" :model="form" :rules="rules" label-width="75px" :inline="true">
        <el-form-item prop="cid" label="课序号">
          <el-input v-model="form.cid" style="width: 100px" disabled></el-input>
        </el-form-item>
        <el-form-item prop="weekday" label="日期">
          <el-select prop="weekday" v-model="form.weekday" style="width: 100px" disabled>
            <el-option
                v-for="(item,index) in weekOption.slice(1)"
                :key="item"
                :label="item"
                :value="index+1">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="dayorder" label="节次" style="width: 100px">
          <el-input-number v-model="form.dayorder" disabled></el-input-number>
        </el-form-item>
        <el-form-item prop="place" label="地点">
          <el-input v-model="form.place" style="width: 500px"></el-input>
        </el-form-item>
        <el-form-item prop="beginWeek" label="起始周">
          <el-input-number v-model="form.beginWeek"></el-input-number>
        </el-form-item>
        <el-form-item prop="endWeek" label="结束周">
          <el-input-number v-model="form.endWeek"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="danger" @click="deletePerform" v-if="this.changing">删 除</el-button>
        <el-button type="primary" @click="arrangeChange">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "courseSet",
  created() {
    let userInfo = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    this.userInfo = userInfo
    this.load()
  },
  data(){
    let isNum = (str) => {
      var reg = /^[0-9]+$/
      if(reg.test(str)){
        return 1
      }
      return 0
    };
    let cidRule = (rule, value, callback) => {
      if(!value){
        callback(new Error("课序号不能为空"))
      }
      else if(!isNum(value)){
        callback(new Error("课序号应为数字"))
      }
      else{
        callback()
      }
    };
    let weekdayRule = (rule,value,callback)=>{
      if(!value){
        callback(new Error("日期选择不能为空"))
      }
      else if(value < 1 || value > 7){
        callback(new Error("日期选择不合理"))
      }
      else{
        callback()
      }
    };
    let dayorderRule = (rule,value,callback)=>{
      if(!value){
        callback(new Error("节次不能为空"))
      }
      else if(value < 1 || value > 5){
        callback(new Error("节次不合理"))
      }
      else{
        callback()
      }
    };
    let beginWeekRule = (rule,value,callback)=>{
      if(!value){
        callback(new Error("起始周不能为空"))
      }
      else if(value < 1 || value > 17){
        callback(new Error("周次不合理"))
      }
      else if(this.form.endWeek!=null && value > this.form.endWeek){
        callback(new Error("起始周应不大于结束周"))
      }
      else{
        callback()
      }
    };
    let endWeekRule = (rule,value,callback)=>{
      if(!value){
        callback(new Error("结束周不能为空"))
      }
      else if(value < 1 || value > 17){
        callback(new Error("周次不合理"))
      }
      else if(this.form.beginWeek!=null && value < this.form.beginWeek){
        callback(new Error("结束周应不小于起始周"))
      }
      else{
        callback()
      }
    };
    let totalRule = (rule,value,callback)=>{
      if(!value){
        return callback(new Error("总课余量不能为空"))
      }
      if(this.card.cid!=null){
        request.get("/course/getCoursePrecise",{params:{cid:this.card.cid}}).then(res=>{
          if(value < res.data.num){
            callback("总课余量不能低于现选课量")
          }
          else{
            callback()
          }
        })
      }
    }
    return{
      userInfo:{},
      tableData:[],
      headStyle:{
        "background-color":"#3B82FF25"
      },
      rowStyle:{
        "background-color":"#F0FFFF"
      },
      loading:false,
      cardLoading:false,
      weekOption:["节次","周一","周二","周三","周四","周五","周六","周日"],
      dialogVisible:false,
      form:{},
      card:{},
      rules: {
        cid:[{validator:cidRule,trigger: 'blur'}],
        weekday:[{validator:weekdayRule,trigger:'blur'}],
        dayorder:[{validator:dayorderRule,trigger:'blur'}],
        place:[{required:true,message:'上课地点不能为空',trigger:'blur'}],
        beginWeek:[{validator:beginWeekRule,trigger:'blur'}],
        endWeek:[{validator:endWeekRule,trigger:'blur'}]
      },
      cardRules:{
        cid:[{required:true,message:'课序号不能为空',trigger:'blur'}],
        cname:[{required:true,message:'课程名不能为空',trigger:'blur'}],
        total:[{validator:totalRule,trigger:'blur'}],
        credit:[{required:true,message:'学分不能为空',trigger:'blur'}]
      },
      changing:false
    }
  },
  methods:{
    load(){
      this.loading = true
      this.cardLoading = true
      request.get("/course/getCoursePrecise",{
        params:{
          cid:this.$route.query.cid
        }
      }).then(res=>{
        this.card = res.data
        this.cardLoading = false
      })
      request.get("/course/getCourseArrangeTable",{
        params:{
          cid:this.$route.query.cid
        }
      }).then(res=>{
        this.tableData = res.data
        this.loading = false
      })
    },
    changeVisible(viewBlock){
      this.form.cid=this.$route.query.cid
      this.form.weekday=viewBlock.weekday
      this.form.dayorder=viewBlock.dayorder
      this.form.place=viewBlock.place
      this.form.beginWeek=viewBlock.beginWeek
      this.form.endWeek=viewBlock.endWeek
      this.changing = true
      this.dialogVisible = true
    },
    addVisible(weekday,dayorder){
      this.form.cid=this.$route.query.cid
      this.form.weekday=weekday
      this.form.dayorder=dayorder
      this.changing = false
      this.dialogVisible = true
    },
    deletePerform(){
      if(!this.form.cid || !this.form.weekday || !this.form.dayorder){
        this.$message({
          type:"error",
          message:"必要信息丢失"
        })
      }
      else{
        request.delete("/course/deleteArrange", {data:this.form}).then(res=>{
          if(res.code=="0"){
            this.$message({
              type:"success",
              message:"删除成功"
            })
            this.dialogVisible = false
          }
          else{
            this.$message({
              type:"error",
              message:res.msg
            })
            this.dialogVisible = false
          }
          this.load()
        })
      }
    },
    arrangeChange(){
      if(this.changing){
        this.$refs["form"].validate((valid)=>{
          if(valid){
            request.put("/course/updateArrange",this.form).then(res=>{
              if(res.code=="0"){
                this.$message({
                  type:"success",
                  message:"修改成功"
                })
              }
              else{
                this.$message({
                  type:"error",
                  message:res.msg
                })
              }
              this.load()
              this.dialogVisible = false
            })
          }
        })
      }
      else{
        this.$refs["form"].validate((valid)=>{
          if(valid){
            request.post("/course/insertArrange",this.form).then(res=>{
              if(res.code=="0"){
                this.$message({
                  type:"success",
                  message:"插入成功"
                })
              }
              else{
                this.$message({
                  type:"error",
                  message:res.msg
                })
              }
              this.load()
              this.dialogVisible = false
            })
          }
        })

      }
    },
    updateCourseInfo(){
      this.card.tid = this.userInfo.id
      request.put("/course/updateCourse",this.card).then(res=>{
        if(res.code=="0"){
          this.$message({
            type:"success",
            message:"更新成功"
          })
        }
        else{
          this.$message({
            type:"error",
            message:res.msg
          })
        }
        this.$router.push("/courseSet?cid="+this.card.cid+"&&cname="+this.card.cname)
        this.load()
      })
    }
  }


}
</script>

<style scoped>

</style>