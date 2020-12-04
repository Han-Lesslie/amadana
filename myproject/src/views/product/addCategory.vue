<template>
  <div class="addcategory">
    <div>
      <div class="bar">
        <span class="box">产品类别 / {{operation}}产品类别</span>
      </div>
    </div>
    <div class="form">
      <el-form
        :model="categoryForm"
        :rules="rules"
        ref="categoryForm"
        label-width="100px"
        class="demo-categoryForm"
      >
        <el-form-item label="类别名称" prop="categoryName">
          <el-input
            v-model="categoryForm.categoryName"
            placeholder="最多可输入35个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="类别排序" prop="categoryName">
          <el-input
            v-model="categoryForm.order"
            placeholder="最多可输入35个字"
          ></el-input>
        </el-form-item>
        <el-form-item style="float:right;margin-right:20%;">
          <el-button type="danger" @click="submitForm('categoryForm')" style="background:#ff3300;width:100px;"
            >确定</el-button
          >
          <el-button type="info" plain @click="cancel('categoryForm')" style="width:100px;">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import validateNumber from '@/utils/validateNumber'
export default {
  name: "addCategory",
  data() {
    return {
      categoryForm: {
        categoryName: "",
        order:""
      },
      flag:false,
      operation:"添加",
      token:"",
      rules: {
        categoryName: [
          { required: true, message: "请输入类别名称", trigger: "blur" },
          { min: 2, max: 35, message: "长度在 2 到 35 个字符", trigger: "blur" }
        ],
        categoryName: [
          { required: true, message: "请输入类别排序", trigger: "change" },
          { min: 1, max: 100, message: "值在 1 到 100 之间", trigger: "change" }
        ]
      },
      
    };
  },
  created() {
    this.token = localStorage.getItem("token");
    if (this.token) {
      let data = this.$route.params.data;
      if (data) {
        this.flag = true;
        this.operation = "更新";
        this.categoryForm = data;
      }
    }else {
      this.$router.push("/login")
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
           if (!this.flag) {
             this.$http.post("/api/saveCategory",JSON.stringify(this.categoryForm),{headers:{"token":this.token}})
             .then(res=>{
               if (res.data.code === 200) {
                  this.$notify({
                    type:"success",
                    message:"添加成功!"
                  });
                  this.$router.push("/productCategory")
               }else if (res.data.code === 405) {
                 this.$message({
                    type:"error",
                    message:"身份失效，请重新登录!"
                  });
                  this.$router.push("/login")
               }else {
                 this.$message({
                    type:"error",
                    message:"添加失败!"
                  });
                  this.$router.push("/productCategory")
               }
             }).catch(err=>{
               this.$message({
                    type:"error",
                    message:"网络异常，请重试!"
                  });
             });
           }else {
             this.$http.post("/api/updateCategory",JSON.stringify(this.categoryForm),{headers:{"token":this.token}})
             .then(res=>{
               if (res.data.code === 200) {
                  this.$notify({
                    type:"success",
                    message:"更新成功!"
                  });
                  this.$router.push("/productCategory")
               }else if (res.data.code === 405) {
                 this.$message({
                    type:"error",
                    message:"身份失效，请重新登录!"
                  });
                  this.$router.push("/login")
               }else {
                 this.$message({
                    type:"error",
                    message:"更新失败!"
                  });
                  this.$router.push("/productCategory")
               }
             }).catch(err=>{
               this.$message({
                    type:"error",
                    message:"网络异常，请重试!"
                  });
             });
           }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancel(formName) {
      this.$refs[formName].resetFields();
      window.history.go(-1)
    }
  }
};
</script>
<style scoped>
.bar {
  float: left;
  background: #f2f2f2;
  width: 105%;
  height: 50px;
  overflow: hidden;
  text-align: center;
}
.bar span {
  float: left;
  vertical-align: middle;
  font-size: 14px;
  padding-top: 15px;
  padding-left: 2%;
}
.form {
  padding-top: 90px;
  padding-left: 20%;
}
div .is-required {
  width: 80%;
}
.upload-demo {
    margin-left: -300px;
}
.linkWay {
  padding-left: 20px !important;
  float: left;
  padding-top: 10px;
}
</style>
