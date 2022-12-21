<template>
  <div>
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
          <el-popover trigger="hover" placement="right" width="250px">
            <template #reference>
              <div style="line-height: 1.3">
                {{ scope.row[i]!=null ? scope.row[i].cname : "" }}
              </div>
            </template>
            <el-descriptions direction="vertical" border size="mini" :column=2>
              <el-descriptions-item label="课序号">
                {{ scope.row[i]!=null ? scope.row[i].cid : "" }}
              </el-descriptions-item>
              <el-descriptions-item label="课程名">
                {{ scope.row[i]!=null ? scope.row[i].cname : "" }}
              </el-descriptions-item>
              <el-descriptions-item label="上课地点">
                {{ scope.row[i]!=null ? scope.row[i].place : "" }}
              </el-descriptions-item>
              <el-descriptions-item label="任教老师">
                {{ scope.row[i]!=null ? scope.row[i].tname : "" }}
              </el-descriptions-item>
              <el-descriptions-item label="上课周次">
                第{{ scope.row[i]!=null ? scope.row[i].beginWeek : "" }}~{{ scope.row[i]!=null ? scope.row[i].endWeek : "" }}周
              </el-descriptions-item>
              <el-descriptions-item label="学分">
                {{ scope.row[i]!=null ? scope.row[i].credit : "" }}
              </el-descriptions-item>
              <el-descriptions-item label="成绩" v-if="!this.CHOOSING">
                {{ scope.row[i]!=null ? scope.row[i].score : "" }}
              </el-descriptions-item>
            </el-descriptions>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "TimeTableCore",
  components:{
  },
  data(){
    return{
      userInfo:{},
      CHOOSING:false,
      tableData:[],
      headStyle:{
        "background-color":"#3B82FF25"
      },
      rowStyle:{
        "background-color":"#F0FFFF"
      },
      loading:false,
      weekOption:["节次","周一","周二","周三","周四","周五","周六","周日"]
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
        this.CHOOSING = res.data.flag
        request.get("/course/getTable",{
          params:{
            sid:this.userInfo.id
          }
        }).then(res=>{
          console.log(res.data)
          this.tableData=res.data
          this.loading = false
        })
      })

    }
  }
}
</script>

<style scoped>

</style>