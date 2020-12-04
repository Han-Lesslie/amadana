<template>
  <div class="main-login">
    <div><h2 class="title">Amadana管理平台</h2></div>
    <div class="bg-login">
    <div class="middle-boxlogin loginscreen bg-white container">
      <div class="login-title text-center">
      </div>
      <el-form
        :model="loginForm"
        :rules="loginRules"
        ref="loginForm"
        class="login-Form"
        label-width="100px"
        method="post"
        style="margin-left:-45px"
      >
        <el-form-item label="账号:" prop="account" >
          <el-input 
            status-icon
            v-model="loginForm.account"
            placeholder="请输入账号名称/手机号"
            class="fix-login"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码:" prop="password">
          <el-input
            :type="passwordType"
            @keyup.enter.native="doLogin"
            v-model="loginForm.password"
            placeholder="请输入密码"
            auto-complete="on"
            class="fix-login"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="验证码:" prop="checkCode">
          <el-input
            @keyup.enter.native="nextStep"
            v-model="loginForm.checkCode"
            placeholder="请输入验证码"
            auto-complete="on"
            id="fg-code"
          >
          </el-input>
          <div class="divIdentifyingCode" @click="getIdentifyingCode(true)">
            <img
              id="imgIdentifyingCode"
              style="height:40px;cursor:pointer;"
              alt="点击更换"
              title="点击更换"
            />
          </div>
        </el-form-item>
        <el-form-item label="">
          <el-button
            type="success"
            :loading="loading"
            @click.native.prevent="doLogin"
            style="width:400px;margin-left:-55px;"
            >登录</el-button
          >
        </el-form-item>
      </el-form>
      <!-- <div class="register_forget">
        <div class="register">
          <router-link to="/Register"> <a href="#">注册账户</a></router-link>
        </div>
        <div class="forget_password">
          <router-link to="/Forget"><a href="#">忘记密码</a></router-link>
        </div>
      </div> -->
    </div>
  </div>
  </div>
</template>

<script>
//import store from '../store/index.js'
export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        account: "",
        password: "",
        checkCode: ""
      },
      loginRules: {
        account: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "密码长度必须6位以上", trigger: "blur" }
        ],
        checkCode: [
          { required: true, message: "请输入验证码", trigger: "blur" },
        ]
      },
      loading: false,
      passwordType: "password",
      token: ""
    };
  },
  watch: {
    token: {
      handler: function(token) {
        localStorage.save(token);
      },
      deep: true
    }
  },
  mounted() {
    this.getIdentifyingCode(true);
  },
  methods: {
    getIdentifyingCode(bRefresh) {
      let identifyCodeSrc = "/api/createImageCode";
      if (bRefresh) {
        identifyCodeSrc = "/api/createImageCode?" + Math.random();
      }
      let objs = document.getElementById("imgIdentifyingCode");
      objs.src = identifyCodeSrc;
    },
    doLogin: function() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.$http
            .get("/api/validate?checkCode=" + this.loginForm.checkCode)
            .then(res => {
              if (res.data.code === 200) {
                this.loading = true;
                let obj = {};
                obj.account = this.loginForm.account;
                obj.password = this.loginForm.password
                this.$http
                  .post("/api/login", JSON.stringify(obj), {
                    headers: { "Content-Type": "application/json" }
                  })
                  .then(res => {
                    if (res.data.code == 200) {
                      this.$notify({
                        type: "success",
                        message: "登录成功！"
                      });
                      localStorage.setItem("token", res.data.data);
                      this.loading = false;
                      this.$router.push("/home");
                    } else {
                      this.$message({
                        type: "error",
                        message: "账户或密码错误！"
                      });
                      //刷新验证码
                      this.getIdentifyingCode(true);
                      this.loading = false;
                    }
                  })
                  .catch(err => {
                    this.$message({
                      type: "error",
                      message: "网络错误，请重试!"
                    });
                    this.loading = false;
                  });
              }else {
                  this.getIdentifyingCode(true);
                  this.$message({
                      type:"error",
                      message:res.data.message
                  });
                  this.loading = false;
              }
            }).catch((err=>{
                this.getIdentifyingCode(true)
            }));
        }
      });
    }
  }
};
</script>

<style scoped>
.main-login {
  background: url("../assets/mountains.jpg") no-repeat center fixed;
  background-size: cover;
  background-size: 100% 100%;
 height: 100%;
 position: fixed;
 width: 100%
}
.title {
  color:	black;
  margin-top:12%;
  margin-bottom:-2%;
  font-family:STXingkai;
  font-size:30px;
}
/* .bg-login {
  margin-top: 65px;
} */
.container {
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: auto;
}
.el-form-item__error--inline {
  display: block;
}

.login-Form {
  margin-top: -30px;
}
.bg-white {
  /*background-color: #ffffff;*/
  opacity: 0.8;
}
.middle-boxlogin {
  max-width: 400px;
  z-index: 100;
  margin: 0 auto;
  padding-bottom: 10px;
  /*height: 400px;*/
}
.el-form-item__content {
  line-height: 10px;
}
.el-form {
  padding-top: 0;
}
.loginscreen .middle-box {
  width: 300px;
}
.login-title {
  height: 20px;
  padding: 30px;
  font-weight: bold;
}
.login-header {
  height: 150px;
}
.login-logo {
  margin-top: 40px;
}
.login-logo-zh,
.login-logo-en {
  font-weight: bold;
  color: #3b3127;
}
.login-logo-zh {
  font-size: 45px;
  font-family: "宋体";
}
.login-logo-en {
  font-size: 21px;
  font-family: "Helvetica";
}
.el-row {
  background: white;
}
/*
.middle-box{
    .el-input {
        display: inline-block;
    }
    .el-button {
        display: inline-block;
        width:100%;
    }
    .el-input__inner {
        text-align: center;
    }
}*/
.el-form-item__label {
  color:	white !important;
}
.divIdentifyingCode {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 5;
  width: 150px;
  height: 40px;
  background: #e2e2e2;
  margin: 0;
}
.middle-box {
  height: 370px;
}
.show-pwd {
  position: absolute;
  right: 10px;
  top: 2px;
  font-size: 16px;
  color: #889aa4;
  cursor: pointer;
  user-select: none;
}
.register {
  float: left;
  font-size: 14px;
}
.forget_password {
  float: right;
  font-size: 14px;
}

a {
  text-decoration: none;
}
a:link {
  color: blue;
}
a:visited {
  color: #409eff;
}
a:hover {
  color: red;
  font-size: 17px;
}
a:active {
  color: lightskyblue;
}
.fix-login {
  width: 345px !important;
}
</style>
