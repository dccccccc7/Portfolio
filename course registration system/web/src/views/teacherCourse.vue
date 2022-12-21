<template>
  <div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total">
    </el-pagination>
    <el-table
        v-loading="loading"
        element-loading-text="努力加载中>﹏<"
        :data="tableData"
        border
        size="mini">
      <el-table-column prop="cid" label="课序号"></el-table-column>
      <el-table-column prop="cname" label="课程名"></el-table-column>
      <el-table-column prop="num" label="学生总数"></el-table-column>
      <el-table-column label="名册">
        <template #default="scope">
          <el-button @click="toRoster(scope.row.cid,scope.row.cname)">查看名册</el-button>
        </template>
      </el-table-column>
      <el-table-column label="评分">
        <template #default="scope">
          <el-button @click="toSetting(scope.row.cid,scope.row.cname)" type="primary">进行评分</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "teacherCourse",
  data(){
    return{
      userInfo:{},
      currentPage:1,
      pageSize:10,
      total:0,
      loading:false,
      tableData:[]
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
      request.get("/course/getTeacherCourseMini",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          tid:this.userInfo.id,
        }
      }).then(res=>{
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading = false
      })
    },
    toSetting(cid,cname){
      if(cid!=null){
        this.$router.push("/resultSet?cid="+cid+"&&cname="+cname)
      }
      else{
        this.$message({
          type:"error",
          message:"课序号异常"
        })
      }
    },
    toRoster(cid,cname){
      if(cid!=null){
        this.$router.push("/roster?cid="+cid+"&&cname="+cname)
      }
      else{
        this.$message({
          type:"error",
          message:"课序号异常"
        })
      }
    },


    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(currentPage){
      this.currentPage = currentPage
      this.load()
    }
  }
}
</script>

<style scoped>

</style>