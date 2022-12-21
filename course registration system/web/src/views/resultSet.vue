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
          <el-button size="medium" style="margin: 0px 0px 0px 30px" @click="this.$router.push('/teacherCourse')">返回</el-button>
        </el-col>
      </el-row>
      <el-table
          v-loading="loading"
          element-loading-text="努力加载中>﹏<"
          :data="tableData"
          border
          size="mini">
        <el-table-column prop="sid" label="学生id"></el-table-column>
        <el-table-column prop="sname" label="学生姓名"></el-table-column>
        <el-table-column label="评分">
          <template #default="scope">
            <el-input v-model="scope.row.score" style="width: 55px;margin: 0px 5px 0px 0px"></el-input>
            <el-button @click="scoreUpdate(scope.row.sid,scope.row.score)">更新评分</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "resultSet",
  created() {
    let userInfo = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    this.userInfo = userInfo
    this.load()
  },
  data(){
    return{
      loading:false,
      userInfo:{},
      tableData:[]
    }
  },
  methods:{
    load(){
      this.loading = true
      request.get("/course/getCourseStudents",{
        params:{
          cid:this.$route.query.cid
        }
      }).then(res=>{
        this.tableData = res.data
        this.loading = false
      })
    },
    beginScore(){
      this.scoreable = !this.scoreable
      this.$forceUpdate()
    },
    scoreUpdate(sid,score){
      request.put("/course/setScore", {
        sid:sid,
        cid:this.$route.query.cid,
        score:score
      }).then(res=>{
        if(res.code=="0"){
          this.$message({
            type: "success",
            message: res.msg
          })
        }
        else{
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>