<template>
  <div class="addproduct">
    <div>
      <div class="bar">
        <span class="box">产品列表 / {{ operation }}产品</span>
      </div>
    </div>
    <div class="form" v-loading="loading"
      element-loading-text="提交中....."
      element-loading-spinner="el-icon-loading">
      <el-form
        :model="productForm"
        :rules="rules"
        ref="productForm"
        label-width="100px"
        class="demo-productForm"
      >
        <el-form-item label="产品名称" prop="productName">
          <el-input
            v-model="productForm.productName"
            placeholder="最多可输入35个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="产品编号" prop="productNumber">
          <el-input
            v-model="productForm.productNumber"
            placeholder="最多可输入35个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="产品型号" prop="productModel">
          <el-input
            v-model="productForm.productModel"
            placeholder="最多可输入35个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="产品类别" prop="productCategory">
          <span
            ><el-select
              v-model="productForm.productCategory"
              filterable
              placeholder="请选择产品类别"
              style="width:100%"
              fix
            >
              <el-option
                v-for="item in options"
                :key="item.categoryName"
                :value="item.categoryName"
              >
              </el-option> </el-select
          ></span>
        </el-form-item>
        <el-form-item label="产品描述" prop="productDesc" style="width:80%;">
          <el-input
            type="textarea"
            :rows="4"
            v-model="productForm.productDesc"
            placeholder="最多可输入200个字"
          ></el-input>
        </el-form-item>
        <el-form-item label="产品icon" prop="productIcon">
          <el-upload
            v-model="productForm.productIcon"
            class="upload-demo"
            action="/api/setFileUpload"
            :on-change="getIconFile"
            :with-credentials="true"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            multiple
            list-type="picture"
            :limit="1"
            :on-exceed="handleExceed"
            :file-list="imgs1"
          >
            <el-button size="small" type="primary" style="margin-left:-300px;"
              >点击上传</el-button
            >
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过2M
            </div>
          </el-upload>
        </el-form-item>

        <el-form-item label="封面图片" prop="productImg">
          <el-upload
            v-model="productForm.productImg"
            class="upload-demo"
            action="/api/setFileUpload"
            :with-credentials="true"
            :on-change="getProductFile"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            multiple
            :limit="1"
            list-type="picture"
            :on-exceed="handleExceed"
            :file-list="imgs2"
          >
            <el-button size="small" type="primary" style="margin-left:-300px;"
              >点击上传</el-button
            >
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过2M
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="展示图片" prop="displayImg">
          <el-upload
            v-model="productForm.displayImg"
            class="upload-demo"
            action="/api/setFileUpload"
            :with-credentials="true"
            :on-change="getDisplayFile"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            multiple
            :limit="1"
            :on-exceed="handleExceed"
            list-type="picture"
            :file-list="imgs3"
          >
            <el-button size="small" type="primary" style="margin-left:-300px;"
              >点击上传</el-button
            >
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过2M
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="产品价格" prop="price">
          <el-input v-model="productForm.price" placeholder="元"></el-input>
        </el-form-item>
        <!-- 动态增加项目 -->
        <!-- 不止一个项目，用div包裹起来 -->
        <div>
          <div
            v-for="(item, index) in productForm.productDetails"
            :key="index"
            style="background:#f2f2f2;width:80%"
          >
            <el-form-item> </el-form-item>
            <div>
              <div style="float:right;">
                <i
                  v-if="index != 0"
                  class="el-icon-circle-close"
                  @click="deleteItem(item, index)"
                  style="padding-right:10px;"
                ></i>
              </div>
              <div>
                <el-form-item
                  label="图片详情"
                  :prop="''"
                  :rules="{
                    required: true,
                    message: '请填写图片详情',
                    trigger: 'blur'
                  }"
                >
                  <el-upload
                    v-model="item.imgName"
                    class="upload-demo"
                    action="/api/setFileUpload"
                    :with-credentials="true"
                    :on-change="getDetailsFile"
                    :on-preview="handlePreview"
                    :on-remove="handleRemove1"
                    :before-remove="beforeRemove"
                    :on-success="onDetailSuccess"
                    multiple
                    :limit="1"
                    :on-exceed="handleExceed"
                    :file-list="item.imgsdetail"
                    list-type="picture"
                  >
                    <el-button
                      size="small"
                      type="primary"
                      style="margin-left:-300px;"
                      >点击上传</el-button
                    >
                    <div slot="tip" class="el-upload__tip">
                      只能上传jpg/png文件，且不超过2M
                    </div>
                  </el-upload>
                </el-form-item>
              </div>
            </div>
            <el-form-item
              style="width:80%;"
              label="标题"
              :prop="'请填写图片名称'"
              :rules="[
                { required: false, message: '请填写图片名称', trigger: 'blur' }
              ]"
            >
              <el-input v-model="item.detailName"></el-input>
            </el-form-item>
            <el-form-item
              style="width:80%;"
              label="图片描述"
              :prop="'请填写图片描述'"
              :rules="[
                { required: false, message: '请填写图片描述', trigger: 'blur' }
              ]"
            >
              <el-input v-model="item.imgDesc"></el-input>
            </el-form-item>
            <el-form-item
              style="width:80%;"
              label="链接详情"
              :prop="'请填写链接详情'"
              :rules="[
                { required: false, message: '请填写链接详情', trigger: 'blur' }
              ]"
            >
              <el-input v-model="item.detailLink"></el-input>
            </el-form-item>
            <el-form-item> </el-form-item>
          </div>
        </div>
        <div class="addItem">
          <el-button
            @click="addItem"
            type="danger"
            style="width:80%;background:#ff6600;margin-bottom:20px;margin-right:20%;"
            >+添加</el-button
          >
        </div>
        <el-form-item style="float:right;margin-right:20%;">
          <el-button
            type="danger"
            @click="submitForm('productForm')"
            style="background:#ff3300;width:100px;"
            >确定</el-button
          >
          <el-button
            type="info"
            plain
            @click="cancel('productForm')"
            style="width:100px;"
            >取消</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import $ from "jquery";
export default {
  name: "addProduct",
  data() {
    return {
      productForm: {
        id: "",
        productName: "",
        productDesc: "",
        productCategory: "",
        category: { categoryName: "" },
        productIcon: "",
        iconName: "",
        productImg: "",
        productImgName: "",
        displayImg: "",
        displayName: "",
        price: "",
        productNumber: "",
        productModel: "",
        productDetails: [
          {
            id: "",
            detailImg: "",
            detailName: "",
            imgName: "",
            imgDesc: "",
            detailLink: "",
            imgsdetail: []
          }
        ]
      },
      imgs1: [],
      imgs2: [],
      imgs3: [],
      details: [],
      token: "",
      operation: "添加",
      isUpdate: false,
      rules: {
        productName: [
          { required: true, message: "请输入产品名称", trigger: "blur" },
          { min: 3, max: 35, message: "长度在 3 到 35 个字符", trigger: "blur" }
        ],
        productNumber: [
          { required: true, message: "请输入产品编号", trigger: "blur" },
          { min: 3, max: 35, message: "长度在 3 到 35 个字符", trigger: "blur" }
        ],
        productModel: [
          { required: true, message: "请输入产品型号", trigger: "blur" },
          { min: 3, max: 35, message: "长度在 3 到 35 个字符", trigger: "blur" }
        ],
        productCategory: [
          { required: true, message: "请选择产品类别", trigger: "blur" }
        ],
        productIcon: [
          { required: true, message: "请上传产品icon", trigger: "change" }
        ],
        productImg: [
          { required: true, message: "请上传封面图片", trigger: "change" }
        ],
        displayImg: [
          { required: true, message: "请上传展示图片", trigger: "change" }
        ],
        price: [{ required: true, message: "输入产品价格", trigger: "change" }]
      },
      options: [],
      value: "",
      loading: false,
      url: "http://106.52.108.173:8081/"
    };
  },
  created() {
    this.token = localStorage.getItem("token");
    if (!this.token) {
      this.$router.push("/login");
    }
    let data = this.$route.params.data;
    if (data !== null && data !== undefined) {
      this.operation = "更新";
      this.isUpdate = true;
      //this.findByProductId(id);
      this.productForm = data;
      this.productForm.productCategory = this.productForm.category.categoryName;
      this.imgs1.push({name:this.productForm.iconName,url:this.productForm.productIcon});
      this.imgs2.push({name:this.productForm.displayName,url:this.productForm.displayImg});
      this.imgs3.push({name:this.productForm.productImgName,url:this.productForm.productImg});
      let details = this.productForm.productDetails;
      for (var i=0;i<details.length;i++) {
        this.productForm.productDetails[i].imgsdetail = [];
        this.productForm.productDetails[i].imgsdetail.push({name:details[i].imgName,url:details[i].detailImg});
      }
    }
    this.findAllCategory();
  },
  mounted() {
    $(".el-upload-list").css({ width: "50%", "padding-left": "40%" });
  },
  methods: {
    addItem() {
      this.productForm.productDetails.push({
        id: "",
        detailImg: "",
        detailName: "",
        imgName: "",
        imgDesc: "",
        detailLink: "",
        imgsdetail: []
      });
    },
    deleteItem(item, index) {
      if (index != 0) {
        if (this.isUpdate) {
          this.$confirm("确定删除吗?").then(_ => {
            this.$http
              .get("/api/deleteDetailById?id=" + item.id, {
                headers: { token: this.token }
              })
              .then(res => {
                if (res.data.code == 200) {
                  this.$notify({
                    type: "success",
                    message: "移除成功!"
                  });
                  this.productForm.productDetails.splice(index, 1);
                }
              });
          });
        } else {
          this.productForm.productDetails.splice(index, 1);
        }
      }
    },
    getIconFile(file, fileList) {
      this.productForm.iconName = file.name;
      this.productForm.productIcon = this.url + file.name;
      /*this.getBase64(file.raw).then(res => {
        this.productForm.productIcon = res;
      });*/
    },
    getProductFile(file, fileList) {
      this.productForm.productImgName = file.name;
      this.productForm.productImg = this.url + file.name;
    },
    getDisplayFile(file, fileList) {
      this.productForm.displayName = file.name;
      this.productForm.displayImg = this.url + file.name;
    },
    getDetailsFile(file, fileList) {
      $(".el-upload-list").css({ width: "50%", "padding-left": "40%" });
    },
    onDetailSuccess(response, file, fileList) {
      let obj = {};
      obj.name = file.name;
      obj.url = this.url + file.name;
      this.details.push(obj);
      console.log(this.details.length,'---------------------');
      $(".el-upload-list").css({ width: "50%", "padding-left": "40%" });
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

    findByProductId(id) {
      this.loading = true;
      this.$http
        .get("/api/getProductById?id=" + id, { headers: { token: this.token } })
        .then(res => {
          if (res.data.code === 200) {
            this.loading = false;
            this.productForm = res.data.data;
            this.productForm.productCategory = this.productForm.category.categoryName;
            this.imgs1.push({
              name: this.productForm.iconName,
              url: this.productForm.productIcon
            });
            this.imgs2.push({
              name: this.productForm.productImgName,
              url: this.productForm.productImg
            });
            this.imgs3.push({
              name: this.productForm.displayName,
              url: this.productForm.displayImg
            });
            let details = this.productForm.productDetails;

            for (var i = 0; i < details.length; i++) {
              this.productForm.productDetails[i].imgsdetail = []; //
              this.productForm.productDetails[i].imgsdetail.push({
                name: details[i].imgName,
                url: details[i].detailImg
              });
            }
          } else if (res.data.code === 405) {
            this.$message({
              type: "error",
              message: "身份失效，请重新登录！"
            });
            this.$router.push("/login");
          }
        })
        .catch(err => {
          console.log(err);
          this.$message({
            type: "error",
            message: "网络异常，请重试！"
          });
        });
    },

    findAllCategory() {
      this.$http
        .get("/api/findAllCategory", { headers: { token: this.token } })
        .then(res => {
          if (res.data.code === 200) {
            this.options = res.data.data;
          } else if (res.data.code === 405) {
            this.$message({
              type: "error",
              message: "身份失效，请重新登录!"
            });
            this.$router.push("/login");
          }
        });
    },

    // onIconSuccess(response,file,fileList) {
    //   this.productForm.productIcon = file.name;

    //   $(".el-upload-list").css({"width":"50%","padding-left":"40%"});
    // },
    // onOverSuccess(response,file,fileList) {
    //   this.productForm.productImg = file.name;
    //   //this.imgs2.push({name:file.name,url:response.data.split(",")[0]})
    //    $(".el-upload-list").css({"width":"50%","padding-left":"40%"});
    // },
    // onDisplaySuccess(response,file,fileList) {
    //   this.productForm.displayImg = file.name
    //   this.imgs3.push({name:file.name,url:response.data.split(",")[0]})
    //    $(".el-upload-list").css({"width":"50%","padding-left":"40%"});
    // },

    handleRemove(file, fileList) {
      console.log(file, fileList);
    },

    handleRemove1(file, fileList) {
      var name = file.name;
      let index;
      for (var i = 0; i < this.details.length; i++) {
        if (name === this.details[i].name) {
          index = i;
          break;
        }
      }
      this.details.splice(index, 1);
    },
    handlePreview(file) {
      //$(".el-upload-list").css({"width":"50%","padding-left":"40%"});
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
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (!this.isUpdate) {
          if (true) {
            this.productForm.category.categoryName = this.productForm.productCategory;
            for (var i = 0; i < this.details.length; i++) {
              this.productForm.productDetails[i].detailImg = this.details[i].url;
              this.productForm.productDetails[i].imgName = this.details[i].name;
            }
            this.loading = true;
            this.$http
              .post("/api/saveProduct", JSON.stringify(this.productForm), {
                headers: { token: this.token }
              })
              .then(res => {
                if (res.data.code === 200) {
                  this.loading = false;
                  this.$notify({
                    type: "success",
                    message: "添加成功！"
                  });
                  this.$router.push("/productList");
                } else if (res.data.code === 405) {
                  this.loading = false;
                  this.$message({
                    type: "error",
                    message: "身份失效，请重新登录！"
                  });
                  this.$router.push("/login");
                } else {
                  this.loading = false;
                  this.$message({
                    type: "error",
                    message: "添加失败！"
                  });
                }
              })
              .catch(err => {
                this.$message({
                  type: "error",
                  message: "网络错误，请重试！"
                });
              });
          } else {
            console.log("error submit!!");
            return false;
          }
        } else {
          let len = this.productForm.productDetails.length - 1;
          for (var i = 0; i < this.details.length; i++) {
            this.productForm.productDetails[i + len].detailImg = this.details[i].url;
            this.productForm.productDetails[i + len].imgName = this.details[i].name;
          }
          this.productForm.productCategory = this.productForm.category.categoryName;
          this.loading = true;
          this.$http
            .post("/api/updateProduct", JSON.stringify(this.productForm), {
              headers: { token: this.token }
            })
            .then(res => {
              if (res.data.code === 200) {
                this.loading = false;
                this.$notify({
                  type: "success",
                  message: "更新成功!"
                });
                this.$router.push("/productList");
              } else if (res.data.code === 405) {
                this.loading = false;
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
                message: "网络错误!"
              });
            });
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
li .is-success {
  padding-left: 40%;
}
ul .el-upload-list {
  width: 50%;
  padding-left: 40%;
}
</style>
