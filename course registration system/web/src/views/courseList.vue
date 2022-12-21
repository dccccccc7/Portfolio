<template>
  <div>
    <header style="margin: 5px">
      <el-button type="primary" @click="this.form={};dialogVisible=true">新增课程</el-button>
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
        size="mini">
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
          prop="week"
          label="周次">
        <template #default="scope">
          <el-tag size="mini" v-if="scope.row.arranges.length>0" hit>第{{scope.row.arranges[0].beginWeek}}~{{scope.row.arranges[0].endWeek}}周</el-tag>
          <el-tag size="mini" type="info" v-else hit>无</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="arranges"
          label="安排">
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
      <el-table-column label="编辑">
        <template #default="scope">
          <el-button @click="toSetting(scope.row.cid,scope.row.cname)" type="primary">编辑</el-button>
          <el-button @click="deletePerform(scope.row.cid)" type="danger">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <el-dialog title="新建课程" v-model="dialogVisible">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item prop="cid" label="课序号">
          <el-input v-model="form.cid" placeholder="建议使用数字"></el-input>
        </el-form-item>
        <el-form-item prop="cname" label="课程名">
          <el-input v-model="form.cname" placeholder="请输入课程名"></el-input>
        </el-form-item>
        <el-form-item prop="total" label="总课余量">
          <el-input-number v-model="form.total" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item prop="credit" label="学分">
          <el-input-number v-model="form.credit"
                           :precision="1" :step="0.5"
                           :min="0"></el-input-number>
        </el-form-item>
        <el-form-item prop="describes" label="描述">
          <el-input v-model="form.describes" placeholder="请描述你的课程" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="addPerform">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import qs from "qs";
import request from "@/utils/request";

export default {
  name: "courseList",
  components:{

  },
  data(){
    let cidNoRepeat = (rule,value,callback)=>{
      if(!value){
        return callback(new Error("课序号不能为空"))
      }
      request.get("/course/courseExit",{params:{cid:value}}).then(res=>{
        if(res.code=="1"){
          callback(new Error("该课序号已被占用"))
        }
        else{
          callback()
        }
      })
    }
    return{
      userInfo:{},
      currentPage:1,
      pageSize:10,
      total:0,
      tableData:[],
      loading:false,
      dialogVisible:false,
      form:{},
      rules:{
        cid:[{validator:cidNoRepeat,trigger: 'blur'}],
        cname:[{required:true,message:'课程名不能为空',trigger:'blur'}],
        total:[{required:true,message:'总课余量不能为空',trigger:'blur'}],
        credit:[{required:true,message:'学分不能为空',trigger:'blur'}]
      },
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
  created() {
    let userInfo = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    this.userInfo = userInfo
    this.load()
  },
  methods:{
    load(){
      this.loading = true
      request.get("/course/getTeacherCourseFull",{
        params:{
          tid:this.userInfo.id,
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
    toSetting(cid,cname){
      if(cid!=null){
        this.$router.push("/courseSet?cid="+cid+"&&cname="+cname)
      }
      else{
        this.$message({
          type:"error",
          message:"课序号异常"
        })
      }
    },
    deletePerform(cid){
      request.delete("/course/deleteCourse",{data:cid}).then(res=>{
        if(res.code=="0"){
          this.$message({
            type:"success",
            message:"课程信息删除成功"
          })
        }
        else{
          this.$message({
            type:"error",
            message:res.msg
          })
        }
        this.load()
      })
    },
    addPerform(){
      this.form.tid = this.userInfo.id
      this.$refs["form"].validate((valid)=>{
        if(valid){
          request.post("/course/insertCourse",this.form).then(res=>{
            if(res.code=="0"){
              this.$message({
                type:"success",
                message:"课程新建成功"
              })
              this.dialogVisible = false
            }
            else{
              this.$message({
                type:"error",
                message:res.msg
              })
            }
            this.load()
          })
        }
      })
    },


    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(currentPage){
      this.currentPage = currentPage
      this.load()
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