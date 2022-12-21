<template>
  <header>
    <el-input v-model="courseName" placeholder="输入课程名"
              style="width: 250px;margin:0px 5px 0px 0px" clearable></el-input>
    <el-select v-model="weekday" placeholder="选择允许的上课日" multiple
               style="width: 365px;margin:0px 5px 0px 0px" clearable>
      <el-option
        v-for="item in weekOptions"
        :key="item.option"
        :label="item.label"
        :value="item.option">
      </el-option>
    </el-select>
    <el-select v-model="order" placeholder="选择允许的节次" multiple
               style="width: 350px;margin: 0px 5px 0px 0px" clearable>
      <el-option
        v-for="item in orderOption"
        :key="item.option"
        :label="item.label"
        :value="item.option">
      </el-option>
    </el-select>
    <el-button type="primary" style="margin:5px" @click="load">查询</el-button>
  </header>
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
          <div style="margin:0px 0px 1px 0px" v-for="item in scope.row.arranges">
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
          prop="left"
          label="课余量">
        <template #default="scope">
          <el-tag size="mini" hit v-if="scope.row.left==0" type="danger">
            {{scope.row.left}}
          </el-tag>
          <el-tag size="mini" hit v-else-if="scope.row.left<=scope.row.total/2" type="warning">
            {{scope.row.left}}
          </el-tag>
          <el-tag size="mini" hit v-else type="success">
            {{scope.row.left}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right">
        <template #default="scope">
          <el-button type="text" size="medium" v-if="scope.row.left==0" disabled>已满</el-button>
          <el-button type="text" size="medium" @click="handleSelection(scope.row.cid)" v-else>选课</el-button>
        </template>
      </el-table-column>
    </el-table>

</template>

<script>
import request from "@/utils/request";
import qs from 'qs'

export default {
  name: "allCourseTable",
  components:{

  },
  data(){
    return{
      userInfo:{},
      courseName:"",
      weekday:[],
      order:[],
      loading:false,
      currentPage:1,
      pageSize:10,
      total:0,
      tableData:[],
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
  emits:["OverallLoad"],
  created() {
    let userInfo = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    this.userInfo = userInfo
    this.load()
  },
  methods:{
    load(){
      this.loading = true
      request.get("/course/getAllCourse",{
        params:{
          courseName:this.courseName,
          weekday:this.weekday,
          order:this.order,
          pageSize:this.pageSize,
          pageNum:this.currentPage
        },
        paramsSerializer:params => {
          return qs.stringify(params,{indices:false})
        }
      }).then(res=>{
        this.tableData = res.data.records
        this.total = res.data.total
        this.loading = false
      })
    },
    handleSelection(cid){
      request.post("/course/selection",{
        sid:this.userInfo.id,
        cid:cid
      }).then(res=>{
        if(res.code=="0"){
          this.$alert(res.msg,"选课成功")
          this.$emit("OverallLoad")
        }
        else{
          this.$alert(res.msg,"选课失败")
        }
      })
    },
    showDescribe(row){
      this.$msgbox({
        title:row.cname,
        message:row.describes
      })
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