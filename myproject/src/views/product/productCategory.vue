<template>
  <div class="category">
    <div class="bar">
      <span class="box">产品类别</span>
    </div>
    <div class="publish_category">
      <el-button type="danger" style="float:right;background:#ff3300;margin-top:30px;"
        @click="addcategory">添加类别</el-button>
    </div>
    <div class="category_list">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="类别编号" align="center" fix> </el-table-column>
        <el-table-column prop="categoryName" label="类别名称" align="center" fix>
        </el-table-column>
        <el-table-column prop="order" label="排序" align="center" fix>
        </el-table-column>
        <el-table-column prop="operation" label="操作" align="center" fix>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row)"
            >修改</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini"  @click="remove(scope.row)"
            >删除</el-button
          >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination">
      <el-pagination background layout="prev, pager, next" 
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "categoryManager",
  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 4,
      total: 1,
      token:""
    };
  },
  created(){
    this.token = localStorage.getItem("token");
    if (this.token) {
      this.change(this.currentPage,this.pageSize)
    }else {
      this.$message({
        type:"error",
        message:"身份过期，请重新登录!"
      });
      this.$router.push("/login")
    }
  },
  methods:{
      addcategory() {
          this.$router.push({path:"/addCategory"})
      },
      remove(data) {
        if (data === null || data.id === null) {
           this.$message({
             type:"error",
             message:"删除失败!"
           });
           return false;
        }else {
          this.$http.get("/api/deleteCategory?id=" + data.id,{headers:{"token":this.token}})
          .then(res=>{
            if (res.data.code === 200) {
              this.$notify({
              type:"success",
              message:"删除成功!"
              });
              this.change(this.currentPage,this.pageSize);
            }else if (res.data.code === 405) {
              this.$message({
                type:"error",
                message:"身份失效，请重试！"
              });
              this.$router.push("/login");
            }else {
              this.$message({
              type:"error",
              message:"删除失败!"
           });
            }
          }).catch(err=>{
            this.$message({
              type:"error",
              message:"网络异常，请重试!"
            });
          })
        }
      },
      edit(data) {
        this.$router.push({name:"addCategory",params:{data:data}})
      },
      // 处理分页
    handleCurrentChange(page) {
      this.change(page, this.pageSize);
    },
      //请求分页
    change(currentPage, pageSize) {
      this.currentPage = currentPage;
      //alert(this)
      this.$http
        .get(
          "/api/getCategory?currentPage=" + currentPage + "&pageSize=" + pageSize,{headers:{"token":this.token}}
        )
        .then(res => {
           if (res.data.code === 200) {
             this.tableData = res.data.list;
             this.total = res.data.count;
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
.publish_category {
}
.category_list {
  padding-top: 130px;
  margin-left: 20px;
}
.pagination {
    padding-top: 25px;
}
</style>
