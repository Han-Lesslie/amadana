<template>
  <div class="video">
    <div class="bar">
      <span class="box">视频管理</span>
    </div>
    <div class="publish_video">
      <el-button
        type="danger"
        style="float:right;background:#ff3300;margin-top:30px;"
        @click="addvideo"
        >上传视频</el-button
      >
    </div>
    <div class="artilce_list">
      <el-table :data="videoData" border style="width: 100%">
        <el-table-column prop="id" label="序号" align="center" fix> </el-table-column>
        <el-table-column prop="videoName" label="视频名称" align="center" fix>
        </el-table-column>
        <el-table-column prop="title" label="视频标题" align="center" fix>
        </el-table-column>
        <el-table-column prop="videoImg" label="视频封面" align="center" fix>
          <template slot-scope="scope">
            <el-image
              style="width: 50px; height: 50px"
              :src="scope.row.videoImg" 
              :preview-src-list="scope.row.imgList"
              fit="contains"
          ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" align="center" fix>
        </el-table-column>
        <el-table-column prop="operation" label="操作" align="center" fix>
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="edit(scope.row)"
              >编辑</el-button
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
  </div>
</template>

<script>
export default {
  name: "videoManager",
  inject: ['reload'],
  data() {
    return {
      videoData: [],
      currentPage: 1,
      pageSize: 5,
      total: 1,
      token: "",
      dialogVisible: false,
      video:{
        videoName: "",
        title: "",
        desc: "",
        videoImg: "",
        videoCover: "",
        videoPath: ""
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
    // 添加视频
    addvideo() {
      this.$router.push({ path: "/addVideo" });
    },
    view(data) {
      //this.$router.push({ name: "videoView", params: { data: data } });
      this.dialogVisible = true;
      this.video = data;
      
      //this.$refs.tk.$el.firstChild.style.height = 'calc(100% - 80px)';
      $(".el-dialog__body").css("overflow","auto");
      $(".el-dialog__body").css("height","calc(100% - 120px)");
      //this.height = window.innerHeight + "px";
    },
    handleClose(done) {
            done();
      },
    // 编辑视频
    edit(data) {
      this.$router.push({ name: "addVideo", params: { data: data } });
    },
    // 删除视频
    del(data) {
      this.$confirm("确定删除吗?").then(_ => {
        this.$http
          .get("/api/deleteVideoById?id=" + data.id,{headers:{"token":this.token}})
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
          "/api/getVideos?currentPage=" + currentPage + "&pageSize=" + pageSize,{headers:{"token":this.token}}
        )
        .then(res => {
           if (res.data.code === 200) {
             this.videoData = res.data.list;
             for (let i=0;i<this.videoData.length;i++) {
            let imgList = [];
            imgList.push(this.videoData[i].videoImg);
            this.videoData[i].imgList = imgList;
          }
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
.publish_video {
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
