<template>
  <div class="back" :style="bg">
    <div style="width:100%;height:100vh;overflow:hidden">
      <div style="width:400px;margin:150px auto">
        <div style="color: black;font-size:30px;font-weight:bold;text-align: center;padding: 15px">
          登录选课系统
        </div>
        <el-form ref="form" :model="form" size="normal" :rules="rules">
          <el-form-item prop="id">
            <el-input prefix-icon="el-icon-user-solid" placeholder="ID" v-model="form.id"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="密码" v-model="form.password" show-password></el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-radio-group v-model="form.role" size="medium">
              <el-radio-button label="student">学生</el-radio-button>
              <el-radio-button label="teacher">教师</el-radio-button>
              <el-radio-button label="root">管理员</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button style="width:100%" type="primary" @click="login">登录</el-button>
          </el-form-item>
          <el-form-item>
            <el-button style="width:100%" type="primary" @click="$router.push('/register')">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import backGroundImg from '../assets/山大.jpg'
import request from "@/utils/request";
import router from "@/router";
export default {
  name: "Login",
  components:{

  },
  data(){
    return{
      backGroundImg,
      bg:{
        background:`url(${backGroundImg}`,
        backgroundRepeat:"no-repeat",
        backgroundSize:"100% 100%",
      },
      form:{},
      rules:{//表单验证的规则通过data实现
        id:[{
          required:true,message:"请输入ID",trigger:"blur"
        },],
        password:[{
          required:true,message:"请输入密码",trigger:"blur"
        },],
        role:[{
          required:true,message:"请选择角色",trigger:"blur"
        },]
      },
    }
  },
  created() {
    if(sessionStorage.getItem("userToken")=="-1"){
      this.$message({
        type:"error",
        message:"请重新登录"
      })
    }
    sessionStorage.removeItem("userToken")
  },
  methods:{
    login(){
      this.$refs["form"].validate((valid)=>{
        if(valid){
          request.post("/user/login",this.form).then(res=>{
            if(res.code=="0"){
              sessionStorage.setItem("userToken", res.data)
              if(this.form.role=="student"){
                router.push("/choose")
              }
              else if(this.form.role=="teacher"){
                router.push("/teacherCourse")
              }
              else if(this.form.role=="root"){
                router.push("/configuration")
              }
            }
            else{
              if(res.code=="-1"){
                this.$message({
                  type:"error",
                  message:res.msg
                })
              }
              else if(res.code=="-2"){
                this.$message({
                  type:"error",
                  message:res.msg
                })
              }
              else{
                this.$message({
                  type:"error",
                  message:"登陆失败"
                })
              }
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>