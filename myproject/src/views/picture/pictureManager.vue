<template>
  <div class="picture">
    <div class="bar">
      <span class="box">图片管理</span>
    </div>
    <div class="search">
      <div style="float:left;margin-top:30px;">
        <span style="font-size:14px;">图片位置&nbsp;&nbsp;</span>
        <span
          ><el-select
            v-model="value"
            filterable
            placeholder="请选择"
            style="width:450px"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :value="item.label"
            >
            </el-option> </el-select
        ></span>
        <span style="padding-left:20px;"
          ><el-button
            type="danger"
            @click="search"
            style="background:#ff3300;width:100px;"
            >搜索</el-button
          ><el-button type="info" style="width:100px;" plain @click="reset"
            >重置</el-button
          ></span
        >
      </div>
    </div>
    <div class="publish_picture">
      <el-button
        type="danger"
        style="float:right;background:#ff3300;margin-top:100px;"
        @click="addpicture"
        >添加图片</el-button
      >
    </div>
    <div class="artilce_list">
      <el-table :data="bannerData" border style="width: 100%">
        <el-table-column prop="id" label="序号" align="center" fix> </el-table-column>
        <el-table-column prop="bannerPosition" label="图片位置" align="center" fix>
        </el-table-column>
        <el-table-column prop="bannerName" label="图片名称" align="center" fix>
        </el-table-column>
        <el-table-column prop="bannerUrl" label="图片" align="center" fix>
          <template slot-scope="scope">
            <el-image
              style="width: 50px; height: 50px"
              :src="scope.row.bannerUrl" 
              :preview-src-list="scope.row.imgList"
              fit="contains"
          ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="order" label="排序" width="100">
        </el-table-column>
        <el-table-column prop="operation" label="操作" align="center" fix>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row)"
            >修改</el-button
          >
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="remove(scope.row)"
            >删除</el-button
          >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination">
      <el-pagination
        background
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
  name: "pictureManager",
  inject: ["reload"],
  data() {
    return {
      bannerData: [],
      currentPage: 1,
      pageSize: 4,
      total: 1,
      flag:false,
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
      preList: [
        "https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg"
      ]
    };
  },
  created() {
    this.token = localStorage.getItem("token");
    if (this.token) {
      //this.getAll();
      this.change(this.currentPage, this.pageSize,this.flag,this.value);
    } else {
      this.$router.push({ path: "/login" });
    }
  },
  methods: {
    refresh() {
      this.reload()
    },
    addpicture() {
      this.$router.push({ path: "/addPicture" });
    },
    search() {
      if (this.value === null) {
        this.$message({
          type:"error",
          message:"请输入搜索值!"
        })
        return false;
      }else {
        this.flag = true;
        this.change(this.currentPage,this.pageSize,this.flag,this.value);
      }
    },
    reset() {
      this.value = "";
      this.flag = false;
      this.change(this.currentPage,this.pageSize,this.flag,this.value);
    },
    remove(data) {
      this.$confirm("确定删除吗?").then(_=>{
        if (typeof(data) === undefined || data === null || data.id === null) {
          this.$message({
            type:"error",
            message:"删除失败"
          });
        }else {
          this.$http.get("/api/deleteBannerById?id=" + data.id,{headers:{"token":this.token}}).then(res=>{
            if (res.data.code === 200) {
              this.$notify({
                type:"success",
                message:"删除成功!"
              });
              //this.$router.push("/pictureManager");
             //this.refresh()
             //this.$router.push("/empty")
             this.change(this.currentPage,this.pageSize,this.flag,this.value);
            }else if(res.data.code === 405){
              this.$message({
                type:"error",
                message:"身份失效，请重新登录!"
              });
              this.$router.push("/login")
            } else {
              this.$message({
                type:"error",
                message:"删除失败!"
              });
            }
          }).catch(err=>{
            this.$message({
                type:"error",
                message:"网络错误!"
              });
          })
        }
      })
    },
    edit(data) {
      this.$router.push({name: "addPicture",params:{data:data}});
    },
    // 处理分页
    handleCurrentChange(page) {
      this.change(page, this.pageSize,this.flag,this.value);
    },
    //请求分页
    change(currentPage, pageSize,flag,imgPosition) {
      this.currentPage = currentPage;
      //alert(this)
      if (!flag) {
         this.$http
        .get(
          "/api/getBanner?currentPage=" + currentPage + "&pageSize=" + pageSize,{headers:{"token":this.token}}
        )
        .then(res => {
          this.bannerData = res.data.list;
          for (let i=0;i<this.bannerData.length;i++) {
            let imgList = [];
            imgList.push(this.bannerData[i].bannerUrl);
            this.bannerData[i].imgList = imgList;
          }
          this.total = res.data.count;
          console.log(this.bannerData[0].bannerUrl)
          //alert(this.total)
        });
      }else {
        this.$http.get("/api/searchImg?currentPage="+currentPage+"&pageSize="+pageSize+"&imgPosition="+imgPosition,{headers:{"token":this.token}})
        .then(res=>{
          if (res.data.code === 200) {
            this.total = res.data.count;
            this.bannerData = res.data.list;
            console.log(this.bannerData.length)
          }else if(res.data.code === 405){
            this.$message({
              type:"error",
              message:"身份失效，请重新登录!"
            });
            this.$router.push("/login")
          }  else{
            this.$message({
              type:"success",
              message:"暂无数据"
            });
            this.change(this.currentPage,this.pageSize,false,"");
          }
        });
      }
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
.search {
  padding-left: 2%;
}
.publish_picture {
  padding-top: 50px;
}
.artilce_list {
  margin-top: 150px;
  margin-left: 20px;
}
.pagination {
  padding-top: 25px;
}
</style>
