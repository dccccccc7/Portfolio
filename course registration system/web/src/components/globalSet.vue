<template>
  <div>
    <el-divider content-position="left" style="margin: 10px 0px 30px 0px">
      <span style="font-size: 20px;font-family: 黑体">
        全局结算
      </span>
    </el-divider>
    <el-card shadow="never" style="margin: 10px 0px 30px 0px">
      <div style="font-family: 仿宋;font-weight: bold;font-size: medium;color: #545c64">
        此功能会将当前的课程信息全部转入历史记录，不再被视为当前课程。这些课程信息包括：课程信息（包括教师安排）、学生选择与成绩<br />
        课程的时间和地点安排会被直接删除，且结算引起的信息转移与删除不可逆<br />
        请在学期结束时使用此功能，结算前请确定本学期的学生成绩已经全部完成录入<br />
        备注：此功能还会清空当前选课阶段的信息也一并删除，尽管在学期末时应该不存在正在选课的数据<br />
        当前系统学期：{{currentSemesterInfo.year==null ? "":currentSemesterInfo.year}}~{{currentSemesterInfo.year==null ? "":parseInt(currentSemesterInfo.year)+1}}学年
        第{{currentSemesterInfo.semester}}学期
      </div>
    </el-card>
    <el-button type="danger" @click="globalSetPerform">全局结算</el-button>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "globalSet",
  data(){
    return{
      currentSemesterInfo:{},
    }
  },
  created() {
    let userInfo = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    this.userInfo = userInfo
    this.load()
  },
  methods:{
    load(){
      this.currentSemester()
    },
    globalSetPerform(){
      request.post("/course/globalSet").then(res=>{
        if(res.code=="0"){
          this.$message({
            type:"success",
            message:"已结算"
          })
          this.load()
        }
        else{
          this.$message({
            type:"error",
            message:res.msg
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
    }
  }
}
</script>

<style scoped>

</style>