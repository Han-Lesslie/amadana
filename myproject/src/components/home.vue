<template>
  <div style="background-color: #EBEBEB;max-height:500px">
    <div
      style="width:100%;background-color:black; overflow: hidden; height:60px;"
    >
      <span
        class="demonstration"
        style="float:left;padding-top:-10px;color:white;margin-left:3%;"
      >
        <h4 style="color:white;">
          amadana管理后台
        </h4>
      </span>
      <span
        class="demonstration"
        style="float:right;margin-top:18px;margin-right:1%;font-size:14px;">
        
        <div style="float:left">
            <span><el-avatar :size="20" :src="circleUrl"></el-avatar></span>
        </div>
        <div style="float:left;padding-left:5px;">
          <span style="color:white;">
            {{ username }}超级管理员&nbsp;&nbsp;&nbsp;
          </span>
          <span style="color:#ff3300;" @click="loginout">退出</span>
        </div>
        <!-- <el-dropdown trigger="click" style="margin-top:10px;">
          <span class="el-dropdown-link" style="color:white;">
            {{username}}含笑半步癫，您好!
            <i class="el-icon-caret-bottom el-icon--right"></i>
          </span>
          <span style="color:white" @click="exit">注销</span>
          <el-dropdown-menu slot="dropdown" @select="handleClick">
            <el-dropdown-item @click.native="editInfo">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="updatePass">更新密码</el-dropdown-item>
            <el-dropdown-item @click.native="loginout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown> -->
      </span>
    </div>

    <div>
      <el-row :span="24">
        <el-col :xs="4" :sm="4" :md="4" :lg="4">
          <div>
            <el-menu
              default-active="1-1"
              class="el-menu-vertical-demo"
              style="min-height:660px;"
              background-color="rgba(51, 51, 51, 1)"
              text-color="white"
              active-text-color="rgb(255, 51, 0)"
              @select="handleSelect"
            >
              <el-submenu index="1">
                <template slot="title">
                  <i class="el-icon-shopping-bag-1"></i>
                  <span slot="title">产品管理</span>
                </template>
                <el-menu-item-group >
                  <el-menu-item index="1-1" style="padding-left:70px;">
                    <i class="el-icon-menu"></i>  
                    <span slot="title">产品列表</span>
                  </el-menu-item>
                </el-menu-item-group>
                 <el-menu-item-group>
                  <el-menu-item index="1-2" style="padding-left:70px;">
                    <i class="el-icon-menu"></i> 
                    <span slot="title">产品类别</span>
                  </el-menu-item>
                </el-menu-item-group>
              </el-submenu>

              <el-menu-item index="2">
                <template slot="title">
                  <i class="el-icon-picture"></i>
                  <span slot="title">图片管理</span>
                </template>
              </el-menu-item>
              <el-menu-item index="3">
                <template slot="title">
                  <i class="el-icon-document"></i>
                  <span slot="title">文章管理</span>
                </template>
              </el-menu-item>
            </el-menu>
          </div>
        </el-col>
        <el-col :xs="20" :sm="20" :md="20" :lg="20" style="width:80%">
          <div style="margin-top:0px">
            <router-view></router-view>
          </div>
        </el-col>
      </el-row>
    </div>
    <!-- <div style="width:100%;background-color:#143f6d; overflow: hidden;height:100px;">
      <span class="demonstration" style="float:left;padding-top:15px;color:white;margin-left:50%;">
        <div class="copyright" style="color:white;margin-top:22px;">Copyright&copy;Amadana</div>
      </span> -->
  </div>
</template>
<script type="text/ecmascript-6">
export default {
  name: "Home",
  data() {
    return {
      searchCriteria: "",
      breadcrumbItems: ["文章管理"],
      username: "",
      show:false,
      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
    };
  },
  created() {
    var token = localStorage.getItem("token");
    if (token) {
      this.$router.push("/home");
      
    } else {
      this.$router.push("login");
    }
  },
  mounted() {},
  methods: {
    loginout() {
      this.$confirm("确定退出吗?")
        .then(_ => {
          var token = localStorage.getItem("token");
          if (token) {
            this.$http({
              methods: "get",
              url: "/api/loginout",
              headers: { "token": token }
            })
              .then(response => {
                if (response.data.code == 200) {
                  localStorage.removeItem("token");
                  this.$notify({
                    type: "success",
                    message: "退出成功!"
                  });
                }
                  this.$router.push("/login");
              })
              .catch(error => {
                this.$router.push("/login");
              });
          } else {
            this.$router.push("/login");
          }
        })
        .catch(_ => {});
    },
   
    handleClick(key, keyPath) {
      switch (key) {
        case "1":
          console.log("info");
          break;
        case "2":
          this.loginout();
          break;
      }
    },

    handleSelect(key, keyPath) {
      switch (key) {
        case "1-1":
          this.$router.push("/productList");
          this.breadcrumbItems = ["产品列表"];
          break;
        case "1-2":
          this.$router.push("/productCategory");
          this.breadcrumbItems = ["产品类别"];
          break;
        case "2":
          this.$router.push("/pictureManager");
          this.breadcrumbItems = ["图片管理"];
          break;
        case "3":
          this.$router.push("/articleManager");
          this.breadcrumbItems = ["文章管理"];
          break;
      }
    }
  }
};
</script>
<style scoped>
.el-row {
  background: white;
}
.el-input--prefix .el-input__inner {
  padding-left: 30px;
}
.message a:link {
  color: white;
}
a:active {
  color: white;
}
a {
  text-decoration: none;
}
</style>
