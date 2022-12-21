<template>
  <el-table
      v-loading="loading"
      element-loading-text="努力加载中>﹏<"
      :data="tableData"
      border
      size="mini"
      height="40vh">
    <el-table-column
        prop="cid"
        label="课序号"
        align="center"
    ></el-table-column>
    <el-table-column
        prop="cname"
        label="课程名"
    ></el-table-column>
    <el-table-column
        prop="tname"
        label="任课教师"
    ></el-table-column>
    <el-table-column
        prop="week"
        label="周次">
      <template #default="scope">
        <el-tag size="mini" v-if="scope.row.arranges.length>0" hit>第{{scope.row.arranges[0].beginWeek}}~{{scope.row.arranges[0].endWeek}}周</el-tag>
        <el-tag size="mini" type="info" v-else hit>无</el-tag>
      </template>
    </el-table-column>
    <el-table-column
        prop="arranges"
        label="安排"
        width="180px">
      <template #default="scope">
        <el-tag size="mini" type="info" v-if="scope.row.arranges.length==0" hit>无</el-tag>
        <div style="margin:0px 0px 5px 0px" v-for="item in scope.row.arranges">
          <el-tag size="mini" hit>
            {{ this.weekOptions[item.weekday-1].label }}
            {{ this.orderOption[item.dayorder-1].label }}</el-tag>
          <el-tag size="mini" type="info" hit>{{ item.place }}</el-tag>
        </div>
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
    <el-table-column
        fixed="right"
        v-if="CHOOSING">
      <template #default="scope">
        <el-button type="text" size="medium" @click="handleDrop(scope.row.cid,scope.row.total==null)">{{scope.row.total==null ? "退课(已上课)" : "退课"}}</el-button>
      </template>
    </el-table-column>
    <el-table-column v-else label="成绩(点击查看排名)">
      <template #default="scope">
        <el-tag size="mini" type="info" v-if="scope.row.score==null">成绩未确定</el-tag>
        <el-tag size="mini" v-else-if="scope.row.score >=85" @click="showRank(scope.row.cid)">{{scope.row.score}}</el-tag>
        <el-tag size="mini" type="success" v-else-if="scope.row.score >=75" @click="showRank(scope.row.cid)">{{scope.row.score}}</el-tag>
        <el-tag size="mini" type="warning" v-else-if="scope.row.score >=60" @click="showRank(scope.row.cid)">{{scope.row.score}}</el-tag>
        <el-tag size="mini" type="danger" v-else @click="showRank(scope.row.cid)">{{scope.row.score}}</el-tag>
      </template>
    </el-table-column>
  </el-table>
  <el-dialog v-model="dialogVisible" width="35%" title="课程成绩排名">
    <el-descriptions
        :column="1"
        size="medium"
        border>
      <el-descriptions-item label="全课程排名">{{rankInfo.typeZero}}</el-descriptions-item>
      <el-descriptions-item label="同专业排名">{{rankInfo.typeOne}}</el-descriptions-item>
      <el-descriptions-item label="同班级排名">{{rankInfo.typeTwo}}</el-descriptions-item>
    </el-descriptions>
  </el-dialog>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "myCourseTable",
  emits:["OverallLoad"],
  created() {
    let userInfo = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    this.userInfo = userInfo
    this.load()
  },
  data(){
    return{
      userInfo:{},
      tableData:[],
      CHOOSING:false,
      loading:false,
      dialogVisible:false,
      rankInfo:{},
      weekOptions:[
        {option: 1,label: "周一"},
        {option: 2,label: "周二"},
        {option: 3,label: "周三"},
        {option: 4,label: "周四"},
        {option: 5,label: "周五"},
        {option: 6,label: "周六"},
        {option: 7,label: "周日"}
      ],
      orderOption:[
        {option: 1,label: "第一大节"},
        {option: 2,label: "第二大节"},
        {option: 3,label: "第三大节"},
        {option: 4,label: "第四大节"},
        {option: 5,label: "第五大节"}
      ]
    }
  },
  methods:{
    load(){
      this.loading = true
      request.get("/course/getCHOOSING").then(res=>{
        this.CHOOSING = res.data.flag
        if(this.CHOOSING){
          request.get("/course/getMyCourse",{
            params:{
              sid:this.userInfo.id
            }
          }).then(res=>{
            this.tableData = res.data
            this.loading = false
          })
        }
        else{
          request.get("/course/getMyTrueCourse",{
            params:{
              sid:this.userInfo.id
            }
          }).then(res=>{
            this.tableData = res.data
            this.loading = false
          })
        }
      })
    },
    handleDrop(cid,choosed){
      request.post("/course/drop/"+choosed,{
        sid:this.userInfo.id,
        cid:cid
      }).then(res=>{
        if(res.code=="0"){
          this.$alert(res.msg,"退课成功")
          this.$emit("OverallLoad")
        }
        else{
          this.$alert(res.msg,"退课失败")
        }
      })
    },
    showDescribe(row){
      this.$msgbox({
        title:row.cname,
        message:row.describes
      })
    },
    showRank(cid){
      request.get("/user/getUser",{
        params:{
          id:this.userInfo.id,
          role:this.userInfo.role
        }
      }).then(res=>{
        if(res.code=="0"){
          request.post("/course/getRank",res.data,{
            params:{
              cid:cid,
              typeCode:0
            }
          }).then(res=>{
            if(res.code=="0"){
              this.rankInfo.typeZero = res.data
            }
          })
          request.post("/course/getRank",res.data,{
            params:{
              cid:cid,
              typeCode:1
            }
          }).then(res=>{
            if(res.code=="0"){
              this.rankInfo.typeOne = res.data
            }
          })
          request.post("/course/getRank",res.data,{
            params:{
              cid:cid,
              typeCode:2
            }
          }).then(res=>{
            if(res.code=="0"){
              this.rankInfo.typeTwo = res.data
            }
          })
        }
        this.dialogVisible = true
      })
    }
  }

}
</script>

<style scoped>

</style>