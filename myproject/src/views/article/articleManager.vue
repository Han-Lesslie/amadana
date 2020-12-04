<template>
  <div class="article">
    <div class="bar">
      <span class="box">文章管理</span>
    </div>
    <div class="publish_article">
      <el-button
        type="danger"
        style="float:right;background:#ff3300;margin-top:30px;"
        @click="addArticle"
        >添加文章</el-button
      >
    </div>
    <div class="artilce_list">
      <el-table :data="articleData" border style="width: 100%">
        <el-table-column prop="id" label="序号" align="center" fix> </el-table-column>
        <el-table-column prop="title" label="文章名称" align="center" fix>
        </el-table-column>
        <el-table-column prop="author" label="作者" align="center" fix>
        </el-table-column>
        <el-table-column prop="createDate" label="发布时间" align="center" fix>
        </el-table-column>
        <el-table-column prop="operation" label="操作" align="center" fix>
          <template slot-scope="scope">
            <el-button
              type="success"
              icon="el-icon-view"
              size="mini"
              @click="view(scope.row)"
              ></el-button
            >
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="edit(scope.row)"
              >修改</el-button
            >
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="del(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination">
      <el-pagination
        layout="prev, pager, next"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
      >
      </el-pagination>
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
          <h3>{{ article.title }}</h3>
          <p>作者: {{ article.author }}</p>
          <p>发布时间: {{ article.createDate }}</p>
          <hr />
          <div v-html="article.content" class="content-detail"></div>
        </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "articleManager",
  inject: ['reload'],
  data() {
    return {
      articleData: [],
      currentPage: 1,
      pageSize: 5,
      total: 1,
      token: "",
      dialogVisible: false,
      article:{
        title:"",
        author:"",
        createDate: "",
        content: ""
      },
      height:"100px"
    };
  },
  created() {
    this.token = localStorage.getItem("token");
    if (this.token) {
      //this.getAll();
      this.change(this.currentPage, this.pageSize);
    } else {
      this.$router.push({ path: "/login" });
    }
  },
  methods: {
    // 添加文章
    addArticle() {
      this.$router.push({ path: "/imcode" });
    },
    view(data) {
      //this.$router.push({ name: "articleView", params: { data: data } });
      this.dialogVisible = true;
      this.article = data;
      
      this.$refs.tk.$el.firstChild.style.height = '240%';
      //this.height = window.innerHeight + "px";
    },
    handleClose(done) {
            done();
      },
    // 编辑文章
    edit(data) {
      this.$router.push({ name: "imcode", params: { data: data } });
    },
    // 删除文章
    del(data) {
      this.$confirm("确定删除吗?").then(_ => {
        this.$http
          .get("/api/deleteArticle?id=" + data.id,{headers:{"token":this.token}})
          .then(r => {
            if (r.data.code == 200) {
              this.$notify({
                type: "success",
                message: "删除成功!"
              });
              //重新加载页面
              this.change(this.currentPage,this.pageSize);
            }else if (r.data.code === 405){
              this.$message({
                type:"error",
                message:"身份失效，请重新登录!"
              })
              this.$router.push("/login");
            } else {
              this.$message({
                type: "error",
                message: "删除失败!"
              });
            }
          })
          .catch(e => {
            this.$message({
              type: "error",
              message: "网络错误，请重试!"
            });
          });
      });
    },
    // 处理分页
    handleCurrentChange(page) {
      this.change(page, this.pageSize);
    },
    //请求分页
    change(currentPage, pageSize) {
      this.loading = true;
      this.$http
        .get(
          "/api/getPage?currentPage=" + currentPage + "&pageSize=" + pageSize,{headers:{"token":this.token}}
        )
        .then(res => {
           if (res.data.code === 200) {
             this.articleData = res.data.list;
             this.total = res.data.count;
             this.loading = false;
           }else if (res.data.code === 405) {
             this.$message({
               type:"error",
               message:"身份失效，请重新登录！"
             });
             this.$router.push("/login")
           }
        });
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
.publish_article {
}
.artilce_list {
  padding-top: 130px;
  margin-left: 20px;
}
.pagination {
  padding-top: 25px;
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
