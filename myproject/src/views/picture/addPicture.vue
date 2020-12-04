<template>
  <div class="addpicture">
    <div>
      <div class="bar">
        <span class="box">图片管理 / {{operation}}图片</span>
      </div>
    </div>
    <div class="form">
      <el-form
        :model="pictureForm"
        :rules="rules"
        ref="pictureForm"
        label-width="100px"
        class="demo-pictureForm"
      >
        <el-form-item label="图片名称" prop="bannerName">
          <el-input
            v-model="pictureForm.bannerName"
            placeholder="最多可输入35个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="图片位置" prop="bannerPosition">
          <span
            ><el-select
              v-model="pictureForm.bannerPosition" 
              filterable
              placeholder="请选择图片位置"
              style="width:100%"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :value="item.label"
              >
              </el-option> </el-select
          ></span>
        </el-form-item>
        <el-form-item label="上传图片" prop="bannerUrl">
          <el-upload v-model="pictureForm.bannerUrl"
            class="upload-demo"
            action="/api/setFileUpload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-success="onSuccess"
            list-type="picture"
            multiple
            :limit="1"
            :on-exceed="handleExceed"
            :file-list="imgs"
          >
            <el-button size="small" type="primary" style="margin-left:-300px;">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过2M
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="链接类型" prop="linkeType">
          <el-radio-group v-model="pictureForm.linkeType" class="linkWay">
            <el-radio label="文章"></el-radio>
            <el-radio label="视频"></el-radio>
            <el-radio label="其他"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="跳转链接" prop="jumpLink">
          <el-input
            v-model="pictureForm.jumpLink"
            placeholder="http://www.link.com"
          ></el-input>
        </el-form-item>
        <el-form-item label="链接方式" prop="linkWay">
          <el-radio-group v-model="pictureForm.linkWay" class="linkWay">
            <el-radio label="当前页面"></el-radio>
            <el-radio label="新页面"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图片排序" prop="order">
          <el-input
            v-model="pictureForm.order"
            placeholder="1"
          ></el-input>
        </el-form-item>
        <el-form-item style="float:right;margin-right:20%;">
          <el-button type="danger" @click="submitForm('pictureForm')" style="background:#ff3300;width:100px;"
            >确定</el-button
          >
          <el-button type="info" plain @click="cancel('pictureForm')" style="width:100px;">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import imcoderTinymce from '../article/imcoder-tinymce.vue';
import $ from 'jquery'
export default {
  name: "addPicture",
  data() {
    return {
      operation:"添加",
      flag:false,
      pictureForm: {
        bannerName: "",
        bannerPosition: "",
        bannerUrl:"",
        linkeType: "",
        jumpLink: "",
        linkWay: "",
        order: ""
      },
      imgs: [],
      img:"",
      rules: {
        bannerName: [
          { required: true, message: "请输入图片名称", trigger: "blur" },
          { min: 3, max: 35, message: "长度在 3 到 35 个字符", trigger: "blur" }
        ],
        bannerPosition: [
          { required: true, message: "请选择图片位置", trigger: "blur" }
        ],
        
        bannerUrl: [
          { required: true, message: "请上传", trigger: "change" }
        ],
        linkeType: [
          { required: true, message: "请选择链接类型", trigger: "change" }
        ],
        jumpLink: [
          { required: true, message: "请输入链接地址", trigger: "change" }
        ],
        linkWay: [
          { required: true, message: "请输入链接方式", trigger: "change" }
        ],
        order: [
          { required: true, message: "请输入图片排序", trigger: "change" }
        ]
      },
      options: [
        {
          value: "选项1",
          label: "首页banner"
        },
        {
          value: "选项2",
          label: "首页导航"
        },
        {
          value: "选项3",
          label: "首页创意空间"
        }
      ],
      value: "",
    };
  },
  created() {
    var token = localStorage.getItem("token");
    console.log("token",token)
    if (!token) {
      this.$router.push("/login")
    }
    let data = this.$route.params.data;
    //alert(typeof(data))
    if (data === undefined || data === null) {
       this.operation = "添加";
    }else {
      this.flag = true; //代表是更新图片
      this.operation = "更新";
      this.pictureForm = data;
      this.imgs.push({name:this.pictureForm.bannerUrl.substring(this.pictureForm.bannerUrl.lastIndexOf("/")+1),url:this.pictureForm.bannerUrl});
    }
  },
  mounted() {
    $('.el-upload-list').css({"width":"50%","padding-left":"40%"});
  },
  methods: {
    handleRemove(file, fileList) {
        this.imgs.pop();
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      onSuccess(response,file,fileList) {
        this.pictureForm.bannerUrl = response.data;
        this.imgs.push({name:file.name,url:response.data.split(",")[0]});
        $('.el-upload-list').css({"width":"50%","padding-left":"40%"});
      },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
          let token  = localStorage.getItem("token");
          if (token) {
            if (!this.flag) {
              this.$http.post("/api/saveBanner",JSON.stringify(this.pictureForm),{headers:{"token":token}}
             ).then((res=>{
               if (res.data.code === 200 ){
                 this.$notify({
                   type: "success",
                   message: "添加成功!"
                 });
                 this.$router.push("/pictureManager");
               }else if(res.data.code === 405){
                 this.$message({
                   type: "error",
                   message: "身份失效，请重新登录!"
                 });
                 this.$router.push("/login");
               } else {
                  this.$message({
                   type: "error",
                   message: "添加失败!"
                 });
               }
             })).catch((err=>{
               this.$message({
                   type: "error",
                   message: "网络异常!"
                 });
             }));
            }else {
              this.$http.post("/api/updateBanner",JSON.stringify(this.pictureForm),{headers:{"token":token}}
             ).then((res=>{
               if (res.data.code == 200 ){
                 this.$notify({
                   type: "success",
                   message: "更新成功!"
                 });
                 this.$router.push("/pictureManager");
               }else if(res.data.code === 405) {
                 this.$message({
                   type: "error",
                   message: "身份失效，请重新登录!"
                 });
                 this.$router.push("/login");
               }else {
                  this.$message({
                   type: "error",
                   message: "更新失败!"
                 });
               }
             })).catch((err=>{
               this.$message({
                   type: "error",
                   message: "网络异常!"
                 });
             }));
            }
          }else {
            this.$message({
              type:"error",
              message:"身份过期，请重新登录!"
            })
            this.$router.push("/login");
          }
      });
    },
    cancel(formName) {
      this.$refs[formName].resetFields();
      window.history.go(-1);
    }
  }
};
</script>
<style scoped>
.bar {
  float: left;
  background: #f2f2f2;
  width: 104%;
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
