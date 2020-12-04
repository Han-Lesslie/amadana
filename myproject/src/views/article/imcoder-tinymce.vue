<template>
  <div>
    <div class="bar">
      <span class="box">文章管理 / 添加文章</span>
    </div>
    <!-- 富文本 -->
    <div class="full-text">
      <el-form
        :model="articleForm"
        :rules="rules"
        ref="articleForm"
        label-width="100px"
        class="demo-articleForm"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="articleForm.title"
            placeholder="最多可输入55个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input
            v-model="articleForm.author"
            placeholder="默认为amadana(为空的时候官网显示)"
          ></el-input>
        </el-form-item>
        <el-form-item label="文章内容" prop="content">
          <editor
            v-model="articleForm.content"
            :init="init"
            :disabled="disabled"
            placeholder="支持富文本，可以控制行距，文本对齐，插入图片，字号大小，文本颜色等；最多可添加3000个字，超出不能输入"
          ></editor>
        </el-form-item>
        <el-form-item style="float:right">
          <el-button v-if="articleForm != null"
            type="primary"
            size="medium"
            @click="view"
            >预览</el-button>
          <el-button
            style="background:#ff3300"
            type="danger"
            size="medium"
            @click="submitForm('articleForm')"
            >发布</el-button
          >
          <el-button
            type="info"
            plain
            size="medium"
            @click="cancel('articleForm')"
            >取消</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div>
      <el-dialog
        title="文章详情"
        :visible.sync="dialogVisible"
        width="90%"
        :before-close="handleClose"
        ref="tk"
        top="2vh"
      >
        <div class="view">
          <div style="margin-left:0px;">
          <div class="bar" style="width:100%;">
            <span class="box">文章管理 / 文章详情</span>
          </div>
        </div>
        <div class="articleDetail">
          <h3>{{ articleForm.title }}</h3>
          <p>作者: {{ articleForm.author }}</p>
          <p>发布时间: {{ articleForm.createDate }}</p>
          <hr />
          <div v-html="articleForm.content" class="content-detail"></div>
        </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import tinymce from "tinymce/tinymce";
import Editor from "@tinymce/tinymce-vue";
import "tinymce/icons/default/icons";
import "tinymce/themes/silver";
import "tinymce/plugins/image";
import "tinymce/plugins/media";
import "tinymce/plugins/table";
import "tinymce/plugins/lists";
import "tinymce/plugins/contextmenu";
import "tinymce/plugins/wordcount";
import "tinymce/plugins/colorpicker";
import "tinymce/plugins/textcolor";
import "tinymce/plugins/preview";
import "tinymce/plugins/code";
import "tinymce/plugins/link";
import "tinymce/plugins/advlist";
import "tinymce/plugins/codesample";
import "tinymce/plugins/hr";
import "tinymce/plugins/fullscreen";
import "tinymce/plugins/textpattern";
import "tinymce/plugins/searchreplace";
import "tinymce/plugins/autolink";
import "tinymce/plugins/directionality";
import "tinymce/plugins/visualblocks";
import "tinymce/plugins/visualchars";
import "tinymce/plugins/template";
import "tinymce/plugins/charmap";
import "tinymce/plugins/nonbreaking";
import "tinymce/plugins/insertdatetime";
import "tinymce/plugins/imagetools";
import "tinymce/plugins/autosave";
import "tinymce/plugins/autoresize";
// 扩展插件

import "tinymce/plugins/lineheight/plugin";
import "tinymce/plugins/bdmap/plugin";
import handleDate from "@/utils/handleDate";
export default {
  components: {
    Editor
  },
  props: {
    value: {
      type: String,
      default: ""
    },
    disabled: {
      type: Boolean,
      default: false
    },
    plugins: {
      type: [String, Array],
      default:
        "preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media template code codesample table charmap hr nonbreaking insertdatetime advlist lists wordcount imagetools textpattern autosave bdmap autoresize lineheight"
    },
    toolbar: {
      type: [String, Array],
      default:
        "code undo redo restoredraft | cut copy paste pastetext | forecolor backcolor bold italic underline strikethrough link codesample | alignleft aligncenter alignright alignjustify outdent indent lineheight formatpainter | \
    styleselect formatselect fontselect fontsizeselect | bullist numlist | blockquote subscript superscript removeformat | \
    table image media charmap hr pagebreak insertdatetime | bdmap fullscreen preview"
    }
  },
  data() {
    return {
      articleForm: {
        id: "",
        title: "",
        author: "amadana",
        content: "",
        status: 1,
        article_url: "",
        createDate: "",
        updateDate: ""
      },
      dialogVisible:false,
      isUpdate: false, //是否为更新,
      rules: {
        title: [
          { required: true, message: "请输入文章标题", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
        ],
        author: [{ required: true, message: "请输入作者名", trigger: "blur" }],
        content: [
          { required: true, message: "请填写文章内容", trigger: "blur" }
        ]
      },
      //初始化配置
      init: {
        language_url: "../static/tinymce/langs/zh_CN.js",
        language: "zh_CN",
        skin_url: "../static/tinymce/skins/ui/oxide",
        height: 500,
        min_height: 500,
        max_height: 600,
        toolbar_mode: "wrap",
        plugins: this.plugins,
        toolbar: this.toolbar,
        content_style: "p {margin: 5px 0;}",
        fontsize_formats: "12px 14px 16px 18px 24px 36px 48px 56px 72px",
        font_formats:
          "微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;",
        branding: false,
        //此处为图片上传处理函数，这个直接用了base64的图片形式上传图片，
        //如需ajax上传可参考https://www.tiny.cloud/docs/configure/file-image-upload/#images_upload_handler
        images_upload_handler: (blobInfo, success, failure) => {
          const img = "data:image/jpeg;base64," + blobInfo.base64();
          success(img);
        }
      },
      content: this.value
    };
  },
  mounted() {
    tinymce.init({});
  },
  created() {
    let data = this.$route.params.data;
    if (typeof data != undefined && data != null) {
      console.log(data);
      this.articleForm = data;
      this.isUpdate = true;
    }
  },
  methods: {
    getArticle() {
      var token = localStorage.getItem("token");
      console.log("token ===== " + token);
      this.$http
        .get("/api/getArticleByid?id=" + 1, { headers: { "token": token } })
        .then(res => {
          if (res.data.code === 200 && res.data.data !== null) {
            console.log(res.data.data);
            this.articleForm.title = res.data.data.title;
            this.articleForm.content = res.data.data.content;
          }else if (res.data.code === 405) {
            this.$message({
              type:"error",
              message:"身份失效，请重新登录!"
            });
            this.$router.push("/login")
          }
        });
    },
    view() {
      this.dialogVisible = true;
      this.$refs.tk.$el.firstChild.style.height = '220%';
    },
    handleClose(done) {
          done();
      },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let token = localStorage.getItem("token");
          if (token) {
            //this.articleForm.title = "<h2>" + this.articleForm.title + "</h2>"
            if (!this.isUpdate) {
              this.$http
                .post("/api/publish", JSON.stringify(this.articleForm), {
                  headers: { "token": token }
                })
                .then(res => {
                  if (res.data.code === 200) {
                    this.$notify({
                      type: "success",
                      message: "发布成功！"
                    });
                    this.$router.push("/articleManager");
                  } else if (res.data.code === 1023) {
                    this.$notify({
                      type: "error",
                      message: "发布失败！"
                    });
                  }else if (res.data.code === 405) {
                    this.$message({
                      type: "error",
                      message: "身份失效，请重新登录!"
                    });
                    this.$router.push("/login");
                  }
                })
                .catch(err => {
                  this.$message({
                    type: "error",
                    message: "网络错误，请重试！"
                  });
                });
            }else {
              this.$http.post("/api/updateArticle",JSON.stringify(this.articleForm),
              {headers:{"token":token}})
              .then((res=>{
                if (res.data.code == 200) {
                   this.$notify({
                     type:"success",
                     message:"更新成功！"
                   })
                   this.$router.push("/articleManager")
                }else if(res.data.code === 405){
                  this.$message({
                     type:"error",
                     message:"身份失效,请重新登录!"
                   });
                   this.$router.push("/login");
                } else {
                  this.$notify({
                     type:"error",
                     message:"更新失败！"
                   })
                }
              }))
            }
          } else {
            this.$message({
              type: "error",
              message: "身份过期，请重新登录！"
            });
            this.$router.push("/login");
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    cancel(formName) {
      this.$refs[formName].resetFields();
      window.history.go(-1);
    }
  },
  watch: {}
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
.full-text {
  padding-top: 100px;
}
h3 {
  padding-top: 100px;
}
p {
  font-size: 14px;

}
.content-detail {
  padding-top: 25px;
}
</style>
