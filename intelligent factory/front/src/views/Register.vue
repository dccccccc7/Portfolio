<template>
  <div class="login_container" v-if="!isLogin">
    <div class="bg"></div>
    <div class="box_item">
      <div class="login_box">
        <div class="title">
          欢迎入驻
        </div>
        <div id="bar"></div>
        <div class="form">
          <el-form ref="form" :model="form" :rules="rules" label-width="0px">
            <el-form-item class="input_content" prop="company_name">
              <img class="icon" src="@/assets/icon/company.svg" alt="">
              <div class="bar2"></div>
              <el-input v-model="form.company_name" size="large" placeholder="请输入公司名称"></el-input>
            </el-form-item>
            <el-form-item class="input_content" prop="mail">
              <img class="icon" src="@/assets/icon/email.svg" alt="">
              <div class="bar2"></div>
              <el-input v-model="form.mail" size="large" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label-width="0px" id="login_button">
              <el-button @click="register" type="primary">注册</el-button>
            </el-form-item>
            <el-form-item label-width="0px" id="login_button">
              已有帐号？<router-link to="/login"><el-link type="primary" :underline="false">登录</el-link></router-link>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <div class="tip_box">
      <h1>您已经登录</h1>
      <h1>正在跳转…… {{ countNumber }}</h1>
      <router-link to="main">点击立刻跳转</router-link>
    </div>
  </div>
</template>

<script>

import {my_request} from "@/views/systemManagement/utils";

export default {
  name: "Login",
  data() {
    return {
      form: {
        company_name: '',
        mail: '',
        request:'register',
        user:'100000'
      },
      rules: {
        company_name: [
          {required: true, message: "请输入公司名称", trigger: 'blur'}
        ],
        mail: [
          {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ],

      },
      countNumber: 4,
      timer: {},
      isLogin: false,
    }
  },
  methods: {
    sleep1(numberMillis){
      var now = new Date();
      var exitTime = now.getTime() + numberMillis;
      while (true) {
        now = new Date();
        if (now.getTime() > exitTime) return;
      }
    },
    register() {
      this.$refs.form.validate((valid) => {
        if (!valid) return;
        return my_request(this, {
          url: 'data/companyManagement/companyUpdate',
          method: 'post',
          data: this.form,
        }).then(res => {
          if (res.data.company_id) {
            this.$message.success({
              dangerouslyUseHTMLString: true,
              message: "申请已提交！系统管理员将在24小时内处理申请，届时会发送相关邮件，请查收！",
              duration: 5000
            });
            this.$router.push("/login");

          } else {
            this.$message.error(res.data.flag)
          }
        }).catch(err => {
          this.reset();
          this.$message.error("出现错误！")
        });
      });
    },
    reset() {
      this.$refs.form.resetFields();
    }
  },
  activated() {
    let login = window.sessionStorage.getItem("userinfo") != null;
    this.countNumber = 4;
    if (login) {
      this.timer = setInterval(() => {
        this.countNumber--;
        if (this.countNumber === 0) {
          this.$router.push("/main");
        }
      }, 1000)
    }
    this.isLogin = login;
  },
  beforeRouteLeave(to, from, next) {
    clearInterval(this.timer);
    next()
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }
}
</script>

<style>
.login_container {
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
}

.login_box {
  width: 600px;
  height: 550px;
  background: #FFFFFF;
  border-radius: 5px;
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
  flex-direction: column;

}

.login_container .bg {
  flex-basis: 60%;
  height: 100%;
  background: url("../assets/pic/login_background.jpg");
}

.login_container .box_item {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-basis: 40%;
}


.login_box .title {
  margin-top: 50px;
  font-size: 22px;
  font-weight: bold;
}

.login_box #bar {
  width: 150px;
  height: 2px;
  background: #868686;
  margin-top: 5px;
}

.login_box .form {
  margin-top: 80px;
  width: 100%;
}

.login_box .icon {
  display: block;
  width: 40px;
  margin-right: 10px;
}

#login_button .el-button {
  margin-top: 30px;
  width: 420px;
  height: 45px;
  font-size: medium;

}

.login_container .el-form-item {
  margin-bottom: 45px;
  position: relative;
}

.login_container .el-form-item__content {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: nowrap;
}

.login_container .el-input {

  width: 350px;
  padding: 0;
  font-size: large;
}

.login_container .el-form-item__error {
  padding-top: 6px;
  left: 28%;

}

.tip_box {
  margin-top: 60px;
}

.login_container .bar2 {
  width: 2px;
  height: 25px;
  background: rgba(19, 19, 19, 0.47);
  margin-right: 20px;
}


</style>
