<template>
  <div>
    <el-divider content-position="left" style="margin: 10px 0px 30px 0px">
      <span style="font-size: 20px;font-family: 黑体">
        选课控制
      </span>
    </el-divider>
    <el-card shadow="never" style="margin: 10px 0px 30px 0px">
      <div style="font-family: 仿宋;font-weight: bold;font-size: medium;color: #545c64">
        在这里，您可以开启或关闭整个系统的选课功能。<br />
        在您开启选课功能的同时，需要输入选课的当前学期。默认为上一次关闭选课系统时的学期的下一个学期，自主选择学期可能会造成错误，请提前沟通。<br />
        在您关闭选课功能的同时，学生在选课阶段选择的所有课程都会被正式录入，同时关闭学生的选课与退课入口，请不要在规定时间前关闭选课。<br />
        当前系统学期：{{currentSemesterInfo.year==null ? "":currentSemesterInfo.year}}~{{currentSemesterInfo.year==null ? "":parseInt(currentSemesterInfo.year)+1}}学年
        第{{currentSemesterInfo.semester}}学期
      </div>
    </el-card>
    <el-button @click="this.visible=true" type="primary" v-if="!CHOOSING" :loading="loading">开启选课</el-button>
    <el-button @click="toUNCHOOSING" type="danger" v-else :loading="loading">关闭选课</el-button>
  </div>
  <el-dialog v-model="visible" @open="semesterLoad" title="设置学期">
    <el-form ref="form" :model="formData" :rules="rules">
      <el-form-item prop="year" label="学年">
        <el-date-picker v-model="formData.year" type="year" placeholder="选择学年" style="width: 150px" clearable></el-date-picker>
        <span>~</span>
        <el-input prefix-icon="el-icon-date" :model-value="formData.year==null ? '':formData.year.getFullYear()+1" placeholder="选择学年" style="width: 150px" disabled></el-input>
      </el-form-item>
      <el-form-item prop="semester" label="学期">
        <el-input-number
            v-model="formData.semester"
            :min="1"
            :max="3"
        ></el-input-number>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="toCHOOSING" type="primary">开启选课</el-button>
    </template>
  </el-dialog>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "choosingSwitch",
  data(){
    return{
      CHOOSING:undefined,
      visible:false,
      formData:{},
      loading:false,
      currentSemesterInfo:{},
      rules: {
        year:[{
          required:true,message:"请输入学年",trigger:"blur"
        }],
        semester:[{
          required:true,message:"请输入学期",trigger:"blur"
        }],
      }
    }
  },
  created() {
    let userInfo = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    this.userInfo = userInfo
    this.load()
  },
  methods:{
    load(){
      this.loading = true
      request.get("/course/getCHOOSING").then(res=>{
        if(res.code=="0"){
          this.currentSemester()
          this.CHOOSING = res.data.flag
          this.loading = false
        }
        else{
          this.$message({
            type:"error",
            message:"错误"
          })
        }
      })
    },
    currentSemester(){
      request.get("/course/getSEMESTER").then(res=>{
        if(res.code=="0"){
          this.currentSemesterInfo = res.data
        }
      })
    },
    semesterLoad(){
      request.get("/course/getSEMESTER").then(res=>{
        if(res.code=="0"){
          if(res.data.semester > 1){
            res.data.year = new Date(parseInt(res.data.year)+1,1,1)
            res.data.semester = 1
          }
          else{
            res.data.year = new Date(res.data.year)
            res.data.semester++
          }
          this.formData = res.data
        }
      })
    },
    toCHOOSING(){
      this.$refs["form"].validate((valid)=>{
        if(valid){
          request.post("/course/toCHOOSING", {
            year:this.formData.year.getFullYear(),
            semester:this.formData.semester
          }).then(res=>{
            if(res.code=="0"){
              this.visible = false
              this.$message({
                type: "success",
                message: "选课开启"
              })
            }
            this.load()
          })
        }
      })
    },
    toUNCHOOSING(){
      request.post("/course/toUnCHOOSING").then(res=>{
        if(res.code=="0"){
          this.$message({
            type: "success",
            message: "选课关闭"
          })
          this.load()
        }
        else{
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })

    }


  }

}
</script>

<style scoped>

</style>