<template>
  <div class="addvideo">
    <div>
      <div class="bar">
        <span class="box">视频列表 / {{ operation }}视频</span>
      </div>
    </div>
    <div class="form" v-loading="loading"
    element-loading-text="提交中....."
    element-loading-spinner="el-icon-loading">
      <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="100px"
      class="demo-ruleForm"
    >
      <el-form-item label="视频名称" prop="videoName">
        <el-input v-model="ruleForm.videoName" placeholder="请输入视频名称"></el-input>
      </el-form-item>
      <el-form-item label="封面标题" prop="title">
          <el-input
            v-model="ruleForm.title"
            placeholder="最多可输入35个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="封面描述" prop="desc" style="width:80%;">
          <el-input
            v-model="ruleForm.desc"
            placeholder="最多可输入35个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="封面图片" prop="videoImg">
          <el-input v-model="ruleForm.videoImg" v-show="false"></el-input>
          <el-upload
            class="upload-demo"
            action="/api/uploadPicture"
            :on-change="getFile"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-success="onSuccess"
            :with-credentials="true"
            list-type="picture"
            multiple
            :limit="1"
            :on-exceed="handleExceed"
            :file-list="imgs"
          >
            <el-button size="small" type="primary" style="margin-left:-300px;"
              >点击上传</el-button
            >
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过2M
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="上传视频" prop="video">
          <div class="album albumvideo">
            <div>
              <div class="pic_img">
                <div class="pic_img_box">
                  <el-upload
                    class="upload-demo"
                    drag
                    action="/api/uploadVideo"
                    :on-progress="uploadVideoProcess"
                    :on-success="handleVideoSuccess"
                    :before-upload="beforeUploadVideo"
                    :show-file-list="true"
                  >
                    <video
                      v-if="ruleForm.videoPath != '' && !videoFlag"
                      :src="ruleForm.videoPath"
                      class="avatar video-avatar"
                      controls="controls" style="width:100%;height:200px;"
                    >
                      您的浏览器不支持视频播放
                    </video>
                    <i
                      class="el-icon-upload"
                      v-else-if="ruleForm.videoPath == '' && !videoFlag"
                    ></i>
                    <div class="el-upload__text">
                      将文件拖到此处，或<em>点击上传</em>
                    </div>
                    <div class="el-upload__tip" slot="tip">
                      只能上传视频文件，且不超过50M
                    </div>
                    <el-progress
                      v-if="videoFlag == true"
                      type="circle"
                      :percentage="videoUploadPercent"
                      style="margin-top:7px;"
                    ></el-progress>
                  </el-upload>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item style="float:right;margin-right:20%;">
          <el-button
            type="danger"
            @click="submitForm('ruleForm')"
            style="background:#ff3300;width:100px;"
            >确定</el-button
          >
          <el-button
            type="info"
            plain
            @click="cancel('ruleForm')"
            style="width:100px;"
            >取消</el-button
          >
        </el-form-item>
    </el-form>
    </div>
  </div>
</template>
<script>
export default {
  name: "addVideo",
  data() {
    return {
      ruleForm: {
          videoName: "",
          title:"",
          desc:"",
          videoImg:"",
          videoCover:"",
          videoPath: ""
        },
       rules: {
          videoName: [
            { required: true, message: '请输入视频名称', trigger: 'blur' },
            { min: 1,  message: '视频名称不能为空', trigger: 'blur' }
          ],
          title: [
            { required: true, message: '请输入标题', trigger: 'blur' },
            { min: 1,  message: '标题不能为空', trigger: 'blur' }
          ]

        },
      videoFlag: false,
      //是否显示进度条
      videoUploadPercent: "",
      //进度条的进度，
      isShowUploadVideo: false,
      //显示上传按钮
      operation: "添加",
      imgs: [],
      loading:false,
      url:'http://106.52.108.173:8081/',
    };
  },
  created() {
    let data = this.$route.params.data;
    if (data !== undefined && null !== data) {
      this.operation = "更新";
      this.ruleForm = data;
      this.imgs.push({
        name: this.ruleForm.videoCover,
        url: this.ruleForm.videoImg
      });
    }
  },
  mounted() {
    $(".el-upload-list").css({ width: "50%", "padding-left": "10%" });
    $(".el-upload__tip").css({ "margin-left": "-35%" });
    $(".el-upload-dragger").css({ "margin-left": "-37%" });
  },
  methods: {
    //上传前回调
    beforeUploadVideo(file) {
      var fileSize = file.size / 1024 / 1024 < 70;
      if (
        [
          "video/mp4",
          "video/ogg",
          "video/flv",
          "video/avi",
          "video/wmv",
          "video/rmvb",
          "video/mov"
        ].indexOf(file.type) == -1
      ) {
        alert("请上传正确的视频格式");
        return false;
      }
      if (!fileSize) {
        alert("视频大小不能超过50MB");
        return false;
      }
      this.isShowUploadVideo = false;
    },
    //进度条
    uploadVideoProcess(event, file, fileList) {
      this.videoFlag = true;
      this.videoUploadPercent = file.percentage.toFixed(0) * 1;
      this.ruleForm.videoPath = this.url + file.name;
    },
    //上传成功回调
    handleVideoSuccess(res, file) {
      this.isShowUploadVideo = true;
      this.videoFlag = false;
      this.videoUploadPercent = 0;

      //后台上传地址
      if (res.code === 200) {
        this.ruleForm.videoPath = res.data.path;
        //alert(this.videoForm.videoPath)
      } else {
        console.log(res.Message, "--------------");
      }
    },
    getFile(file, fileList) {
      this.ruleForm.videoCover = file.name;
      this.ruleForm.videoImg = this.url + file.name;
    },
    // 将图片转化为base64位
    getBase64(file) {
      return new Promise(function(resolve, reject) {
        let reader = new FileReader();
        let imgResult = "";
        reader.readAsDataURL(file);
        reader.onload = function() {
          imgResult = reader.result;
        };
        reader.onerror = function(error) {
          reject(error);
        };
        reader.onloadend = function() {
          resolve(imgResult);
        };
      });
    },
    handleRemove(file, fileList) {
      this.imgs.pop();
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    onSuccess(response, file, fileList) {
      this.ruleForm.videoImg = response.data.path;
      $(".el-upload-list").css({ width: "50%", "padding-left": "10%" });
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        let token = localStorage.getItem("token");
        this.loading = true;
        if (token) {
          this.$http
            .post("/api/insertVideo", JSON.stringify(this.ruleForm), {
              headers: { token: token }
            })
            .then(res => {
              if (res.data.code === 200) {
                this.loading = false;
                this.$notify({
                  type: "success",
                  message: this.operation + "成功!"
                });
                this.$router.push("/videoManager");
              } else if (res.data.code === 405) {
                this.loading = false;
                this.$message({
                  type: "error",
                  message: "身份失效,请重新登录!"
                });
                this.$router.push("/login");
              } else {
                this.loading = false;
                this.$message({
                  type: "error",
                  message: "操作失败"
                });
              }
            });
        } else {
          this.$message({
            type: "error",
            message: "身份失效,请重新登录!"
          });
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
</style>
