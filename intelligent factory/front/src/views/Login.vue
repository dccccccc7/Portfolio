<template>
  <div class="login_container" v-if="!isLogin">
    <div class="bg"></div>
    <div class="box_item">
      <div class="login_box">
        <div class="title">
          欢迎登录
        </div>
        <div id="bar"></div>
        <div class="form">
          <el-form ref="form" :model="form" :rules="rules" label-width="0px">
            <el-form-item class="input_content" prop="username">
              <img class="icon" src="@/assets/icon/user2.svg" alt="">
              <div class="bar2"></div>
              <el-input v-model="form.username" size="large"
                               placeholder="点我选择测试账号">

<!--                <template slot-scope="{ item }">-->
<!--                  <div class="name">{{ item.name + "  " + item.value }}</div>-->
<!--                </template>-->

              </el-input>
            </el-form-item>
            <el-form-item class="input_content" prop="password">
              <img class="icon" src="@/assets/icon/lock.svg" alt="">
              <div class="bar2"></div>
              <el-input v-model="form.password" @keyup.enter.native="login" show-password size="large"
                        placeholder="点击上面测试账号自动填入密码"></el-input>
            </el-form-item>
            <el-form-item label-width="0px" id="login_button">
              <el-button @click="login" type="primary">登录</el-button>
            </el-form-item>

            <el-form-item label-width="0px" id="login_button">
              欢迎新公司入驻，点我<router-link to="/register"><el-link type="primary" :underline="false">入驻系统</el-link></router-link>
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
        username: '',
        password: '',
      },
      rules: {
        username: [
          {required: true, message: "请输入用户名", trigger: 'blur'}
        ],
        password: [
          {required: true, message: "请输入密码", trigger: 'blur'}
        ]
      },
      countNumber: 4,
      timer: {},
      isLogin: false,
    }
  },
  methods: {
    login() {
      this.$refs.form.validate((valid) => {
        if (!valid) return;
        return my_request(this, {
          url: 'data/login/login',
          method: 'post',
          data: {
            user_name: this.form.username,
            password: this.form.password,
          },
        }).then(res => {
          this.$message.success("登录成功！");

          this.$store.commit("login/SET_USERINFO", res.data.userinfo);
          console.log(this.$store.getters.userinfo);
          window.sessionStorage.setItem("userinfo", JSON.stringify(res.data.userinfo));
          console.log(this.$store.getters.userinfo.company_id);
          if (this.$store.getters.userinfo) {
            this.$router.push('/main')
          }
        }).catch(err => {
          this.reset();
          this.$message.error("用户名或密码错误！")
        });
      });
    },
    reset() {
      this.$refs.form.resetFields();
    }
    ,
    // handleSelect(item) {
    //   this.form.password = "100001"
    // }
    // ,
    // querySearch(queryString, cb) {
    //   cb([
    //
    //       {"value": "100002", "name": "分析报表权限"},
    //       {"value": "100003", "name": "生产过程权限"},
    //       {"value": "100004", "name": "质量管理权限"},
    //   ]);
    // }
    // ,

  },
  // created() {
  //   let login = window.sessionStorage.getItem("userinfo") != null;
  //   this.countNumber = 4;
  //   if (login) {
  //     this.timer = setInterval(() => {
  //       this.countNumber--;
  //       if (this.countNumber === 0) {
  //         this.$router.push("/main");
  //       }
  //     }, 1000)
  //   }
  //   this.isLogin = login;
  // },
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
  beforeRouteLeave(to,from ,next) {
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
  display: flex;
  background: url("../assets/pic/ganyu.jpg");
}

.login_container .box_item {

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
