<template>
  <div>
    <el-row>
      <el-col :span="18">
        <el-divider style="background-color: black" content-position="left">
          课序号：{{this.$route.query.cid}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          课程名：{{this.$route.query.cname}}
        </el-divider>
      </el-col>
      <el-col :span="2">
        <el-button size="medium" style="margin: 0px 0px 0px 30px" @click="reBeginCheck" type="primary">新的点名</el-button>
      </el-col>
      <el-col :span="2">
        <el-button size="medium" style="margin: 0px 0px 0px 30px" @click="saveCheck" type="primary">保存点名</el-button>

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
      <el-table-column label="签到">
        <template #default="scope">
          <el-checkbox v-model="scope.row.present"></el-checkbox>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "roster",
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
      request.get("/course/getPresent",{
        params:{
          cid:this.$route.query.cid
        }
      }).then(res=>{
        if(res.code=="0"){
          this.tableData = res.data
          this.loading = false
        }
      })
    },
    reBeginCheck(){
      request.put("/course/refreshPresent",this.$route.query.cid).then(res=>{
        if(res.code=="0"){
          this.load()
          this.$message({
            type:"success",
            message:"刷新成功"
          })
        }
        else{
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    },
    saveCheck(){
      request.put("/course/savePresent",this.tableData).then(res=>{
        if(res.code=="0"){
          this.load()
          this.$message({
            type:"success",
            message:"保存成功"
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