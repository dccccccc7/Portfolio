<template>
  <div style="text-align: center">
    <el-form ref="form" :model="userInfo" style="width: 30%;margin:50px auto" :rules="rules" label-width="90px">
      <el-form-item prop="id" label="id">
        <el-input v-model="userInfo.id" disabled></el-input>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="userInfo.name"></el-input>
      </el-form-item>
      <el-form-item prop="birthday" label="出生日期">
        <el-date-picker
            v-model="userInfo.birthday"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width:100%">
        </el-date-picker>
      </el-form-item>
      <el-form-item prop="phone" label="手机">
        <el-input v-model="userInfo.phone"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="userInfo.email"></el-input>
      </el-form-item>
      <el-form-item prop="address" label="住址">
        <el-input v-model="userInfo.address"></el-input>
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input v-model="userInfo.password" show-password></el-input>
      </el-form-item>
      <el-form-item prop="role" label="身份">
        <el-input v-model="userInfo.role" disabled></el-input>
      </el-form-item>
      <el-form-item prop="mid" label="专业" v-if="userInfo.role==='student'">
        <el-select v-model="userInfo.mid" style="width: 100%">
          <el-option v-for="item in majorList"
                     :key="item.mid"
                     :label="item.mname"
                     :value="item.mid">
            <span style="float: left">{{item.mname}}</span>
            <span style="float: right; color: var(--el-text-color-secondary); font-size: 13px">{{item.mid}}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="grade" label="年级" v-if="userInfo.role==='student'">
        <el-input v-model="userInfo.grade"></el-input>
      </el-form-item>
      <el-form-item prop="classnum" label="班级号" v-if="userInfo.role==='student'">
        <el-input-number v-model="userInfo.classnum" :min="1" style="width: 100%"></el-input-number>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button type="primary" @click="update">保存</el-button>
      <el-button @click="$router.push('/choose')">返回</el-button>
    </div>
  </div>

</template>

<script>
import request from "@/utils/request";

export default {
  name: "userInfo",
  data(){
    return{
      userInfo:{},
      majorList:[],
      rules:{
        id:[{
          required:true,message:"请输入ID",trigger:"blur"
        }],
        name:[{
          required:true,message:"请输入姓名",trigger:"blur"
        }],
        birthday:[{
          required:true,message:"请输入出生日期",trigger:"blur"
        }],
        phone:[{
          required:true,message:"请输入电话",trigger:"blur"
        }],
        address:[{
          required:true,message:"请输入住址",trigger:"blur"
        }],
        password:[{
          required:true,message:"请输入密码",trigger:"blur"
        }],
        role:[{
          required:true,message:"请选择身份",trigger:"blur"
        }]
      }
    }
  },
  created() {
    let user = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
    request.get("/user/getMajorList").then(res=>{
      if(res.code=="0"){
        this.majorList = res.data
      }
    })
    request.get("/user/getUser",{
      params:{
        id:user.id,
        role:user.role
      }
    }).then(res=>{
      if(res.code=="0"){
        this.userInfo=res.data
        this.userInfo.grade = this.userInfo.grade.split("-")[0]
      }
      else{
        this.$message({
          type: "error",
          message: res.msg
        })
      }
    })
  },
  methods:{
    update(){
      this.$refs["form"].validate((valid)=>{
        if(valid){
          if(this.userInfo.phone.length!=11 && this.userInfo.phone.length!=8){
            this.$message({
              type:"error",
              message:"这可能不是一个电话号码"
            })
            return
          }
          let day = new Date(this.userInfo.birthday)
          let nowDay = new Date()
          if(day.getTime() > nowDay.getTime()){
            this.$message({
              type:"error",
              message:"出生日期不合理"
            })
            return
          }
          let reg = /[a-zA-z0-9]+@[a-zA-z0-9.]+/
          if(!reg.test(this.userInfo.email)){
            this.$message({
              type:"error",
              message:"邮箱格式可能不正确"
            })
            return
          }


          if(this.userInfo.role=="student"){
            request.get("/user/classCheck",{
              params:{
                mid:this.userInfo.mid,
                grade:this.userInfo.grade,
                classnum:this.userInfo.classnum
              }}).then(res=>{
              if(res.code=="-1"){
                this.$message({
                  type:"error",
                  message:res.msg
                })
                return
              }
              else{
                request.put("/user/change",this.userInfo).then(res=>{
                  if(res.code=="0"){
                    sessionStorage.setItem("userToken", res.data)
                    let user = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
                    this.$message({
                      type: "success",
                      message: "更新成功"
                    })
                    request.get("/user/getUser",{
                      params:{
                        id:user.id,
                        role:user.role
                      }
                    }).then(res=>{
                      if(res.code=="0"){
                        this.userInfo=res.data
                        this.userInfo.grade = this.userInfo.grade.split("-")[0]
                        this.$message({
                          type: "success",
                          message: "新信息加载成功"
                        })
                      }
                      else{
                        this.$message({
                          type: "error",
                          message: res.msg
                        })
                      }
                    })
                  }
                  else{
                    this.$message({
                      type: "error",
                      message: res.msg
                    })
                  }
                })
              }
            })
          }
          else{
            request.put("/user/change",this.userInfo).then(res=>{
              if(res.code=="0"){
                sessionStorage.setItem("userToken", res.data)
                let user = JSON.parse(decodeURIComponent(escape(window.atob(sessionStorage.getItem("userToken").split(".")[1]))))
                this.$message({
                  type: "success",
                  message: "更新成功"
                })
                request.get("/user/getUser",{
                  params:{
                    id:user.id,
                    role:user.role
                  }
                }).then(res=>{
                  if(res.code=="0"){
                    this.userInfo=res.data
                    this.userInfo.grade = this.userInfo.grade.split("-")[0]
                    this.$message({
                      type: "success",
                      message: "新信息加载成功"
                    })
                  }
                  else{
                    this.$message({
                      type: "error",
                      message: res.msg
                    })
                  }
                })
              }
              else{
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }


        }
      })

    }
  }
}
</script>

<style scoped>

</style>