<template>
  <div>
    <el-table
        v-loading="loading"
        element-loading-text="努力加载中>﹏<"
        :data="tableData"
        border>
      <el-table-column
          prop="cid"
          label="课程id"
          align="center"
      ></el-table-column>
      <el-table-column
          prop="cname"
          label="课程名"
      ></el-table-column>
      <el-table-column
          prop="tname"
          label="执教教师"
      ></el-table-column>
      <el-table-column
          prop="year"
          label="所属学期">
        <template #default="scope">
          <el-tag>
            {{scope.row.year.split("-")[0]}}学年
          </el-tag>
          <el-tag>
            第{{scope.row.semester}}学期
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="credit"
          label="学分">
      </el-table-column>
      <el-table-column
          label="描述">
        <template #default="scope">
          <el-button type="text" @click="showDescribe(scope.row)">描述</el-button>
        </template>
      </el-table-column>
      <el-table-column label="成绩">
        <template #default="scope">
          <el-tag size="mini" type="info" v-if="scope.row.score==null">成绩未确定</el-tag>
          <el-tag size="mini" v-else-if="scope.row.score >=85">{{scope.row.score}}</el-tag>
          <el-tag size="mini" type="success" v-else-if="scope.row.score >=75">{{scope.row.score}}</el-tag>
          <el-tag size="mini" type="warning" v-else-if="scope.row.score >=60">{{scope.row.score}}</el-tag>
          <el-tag size="mini" type="danger" v-else>{{scope.row.score}}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "HistoryCourse",
  data(){
    return{
      userInfo:{},
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
      request.get("/course/getHistoryCourse",{
        params:{
          sid:this.userInfo.id
        }
      }).then(res=>{
        this.tableData = res.data
        this.loading = false
      })
    },
    showDescribe(row){
      this.$msgbox({
        title:row.cname,
        message:row.describes
      })
    }
  }
}
</script>

<style scoped>

</style>