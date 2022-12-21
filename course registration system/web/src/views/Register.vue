<template>
  <div class="back" :style="bg">
    <div style="width:100%;height:100vh;overflow:hidden">
      <div style="width:400px;margin:20px auto">
        <div style="color: black;font-size:30px;font-weight:bold;text-align: center;padding: 15px">
          注册
        </div>
        <el-form ref="form" :model="form" size="small" :rules="rules">
          <el-form-item prop="name">
            <el-input prefix-icon="el-icon-user-solid" placeholder="输入真实姓名" v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input prefix-icon="el-icon-phone" placeholder="输入电话" v-model="form.phone"></el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input prefix-icon="el-icon-message" placeholder="输入邮箱" v-model="form.email"></el-input>
          </el-form-item>
          <el-form-item prop="address">
            <el-input prefix-icon="el-icon-s-home" placeholder="输入地址" v-model="form.address"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="密码" v-model="form.password" show-password></el-input>
          </el-form-item>
          <el-form-item prop="confirm">
            <el-input prefix-icon="el-icon-lock" placeholder="再次输入密码" v-model="form.confirm" show-password></el-input>
          </el-form-item>
          <el-form-item prop="birthday">
            <el-date-picker
                v-model="form.birthday"
                type="date"
                placeholder="选择出生日期"
                value-format="YYYY-MM-DD"
                style="width:100%">
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="role">
            <el-radio-group v-model="form.role" size="medium">
              <el-radio-button label="student">学生</el-radio-button>
              <el-radio-button label="teacher">教师</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button style="width:100%" type="primary" @click="register">注册</el-button>
          </el-form-item>
          <el-form-item>
            <el-button style="width:100%" type="primary" @click="$router.push('/login')">返回</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import backGroundImg from "@/assets/山东大学.jpg";
import request from "@/utils/request";

export default {
  name: "Register",
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
        name:[{
          required:true,message:"请输入姓名",trigger:"blur"
        }],
        phone:[{
          required:true,message:"请输入电话",trigger:"blur"
        }],
        email:[{
          required:true,message:"请输入电子邮件地址",trigger:"blur"
        }],
        address:[{
          required:true,message:"请输入住址",trigger:"blur"
        }],
        password:[{
          required:true,message:"请输入密码",trigger:"blur"
        }],
        confirm:[{
          required:true,message:"请再次输入密码",trigger:"blur"
        }],
        birthday:[{
          required:true,message:"请输入出生日期",trigger:"blur"
        }],
        role:[{
          required:true,message:"请选择角色",trigger:"blur"
        }]
      },
    }
  },
  methods:{
    register(){
      this.$refs["form"].validate((valid)=>{
        if(valid){
          if(this.form.password!=this.form.confirm){
            this.$message({
              type:"error",
              message:"两次密码输入不一致"
            })
            return
          }
          if(this.form.phone.length!=11 && this.form.phone.length!=8){
            this.$message({
              type:"error",
              message:"这可能不是一个电话号码"
            })
            return
          }
          let day = new Date(this.form.birthday)
          let nowDay = new Date()
          if(day.getTime() > nowDay.getTime()){
            this.$message({
              type:"error",
              message:"出生日期不合理"
            })
            return
          }
          let reg = /[a-zA-Z0-9.]+@[a-zA-Z0-9.]+/
          if(!reg.test(this.form.email)){
            this.$message({
              type:"error",
              message:"邮箱格式可能不正确"
            })
            return
          }
          request.post("/user/register",this.form).then(res=>{
            if(res.code=="0"){
              this.$router.push("/login")
              this.$alert("您的id是"+res.data,"注册成功")
            }
            else{
              this.$message({
                type:"error",
                message:res.msg
              })
            }
          })
        }R
      })
    }
  }}
</script>

<style scoped>

</style>