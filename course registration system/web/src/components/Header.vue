<template>
  <div style="display: flex;background-color:#545c64">
    <el-menu
        router
        class="el-menu"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        style="flex: 1">
<!--      学生版块-->
      <el-menu-item index="choose" v-if="this.userInfo.role=='student'">学生选课</el-menu-item>
      <el-menu-item index="timeTable" v-if="this.userInfo.role=='student'">课表</el-menu-item>
      <el-menu-item index="historyCourse" v-if="this.userInfo.role=='student'">历史课程与成绩</el-menu-item>
<!--      教师版块-->
      <el-menu-item index="teacherCourse" v-if="this.userInfo.role=='teacher'">课程列表</el-menu-item>
      <el-menu-item index="courseList" v-if="this.userInfo.role=='teacher'">设立课程</el-menu-item>
<!--      ROOT版块-->
      <el-menu-item index="configuration" v-if="this.userInfo.role=='root'">选课系统配置</el-menu-item>
    </el-menu>
    <span style="margin: auto;color: white">
      当前系统学期：{{currentSemesterInfo.year==null ? "":currentSemesterInfo.year}}~{{currentSemesterInfo.year==null ? "":parseInt(currentSemesterInfo.year)+1}}学年
      第{{currentSemesterInfo.semester}}学期
    </span>
    <el-menu
        router
        class="el-menu"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        style="width: 65px">
      <el-menu-item index="userInfo">{{this.userInfo.name}}</el-menu-item>
      <el-menu-item index="login">退出登录</el-menu-item>
    </el-menu>
  </div>


</template>

<script>
import request from "@/utils/request";

export default {
  name: "Header",
  props:{

  },
  data(){
    return{
      userInfo:{},
      currentSemesterInfo:{}
    }
  },
  created() {
    let userInfo = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    this.userInfo = userInfo
    this.currentSemester()
  },
  methods:{
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