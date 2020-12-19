<template>
  <div class="product">
    <div class="bar">
      <span class="box">产品列表</span>
    </div>
    <div class="search">
      <div style="float:left;margin-top:30px;">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item label="产品名称">
            <el-input
              v-model="searchForm.productName"
              placeholder="产品名称"
              style="width:270px;"
            ></el-input>
          </el-form-item>
          <el-form-item label="产品类别">
            <el-select
              v-model="searchForm.categoryName"
              filterable
              placeholder="请选择产品类别"
              style="width:270px"
            >
              <el-option
                v-for="item in options"
                :key="item.categoryName"
                :value="item.categoryName"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
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
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="publish_product">
      <el-button
        type="danger"
        style="float:right;background:#ff3300; margin-top:35px;"
        @click="addproduct"
        >添加商品</el-button
      >
    </div>
    <div class="artilce_list">
      <el-table :data="productData" border style="width: 100%">
        <el-table-column prop="productNumber" label="产品编号" align="center" fix>
        </el-table-column>
        <el-table-column prop="productName" label="产品名称" align="center" fix>
        </el-table-column>
        <el-table-column prop="category.categoryName" label="产品类别" align="center" fix>
        </el-table-column>
        <el-table-column prop="productModel" label="型号" align="center" fix>
        </el-table-column>
        <el-table-column prop="createTime" label="添加时间" align="center" fix>
        </el-table-column>
        <el-table-column prop="operation" label="操作" align="center" fix>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row)">
            编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="remove(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination">
      <el-pagination background layout="prev, pager, next" @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "productManager",
  data() {
    return {
      productData: [],
      currentPage: 1,
      pageSize: 10,
      total: 1,
      token:"",
      options: [
        
      ],
      value: "",
      searchForm: {
        productName: "",
        categoryName: ""
      }
    };
  },
  created() {
    this.token = localStorage.getItem("token");
    if (!this.token) {
      this.$router.push("/login");
    }
    this.findAllCategory();
    this.change(this.currentPage,this.pageSize);
  },
  methods: {
    findAllCategory(){
       this.$http.get("/api/findAllCategory",{headers:{"token":this.token}})
       .then(res=>{
         if (res.data.code === 200) {
           this.options = res.data.data;
         }else if (res.data.code === 405) {
           this.$message({
             type:"error",
             message:"身份失效，请重新登录!"
           });
           this.$router.push("/login");
         }
       })
    },
    addproduct() {
      this.$router.push({ path: "/addProduct" });
    },
    search() {
      this.change(this.currentPage,this.pageSize);
    },
    reset() {
      this.searchForm = {};
      this.change(this.currentPage,this.pageSize);
    },
    edit(data) {
      this.$router.push({name:"addProduct",params:{data:data}});
    },
    remove(data) {
      this.$confirm("确定删除吗").then(_=>{
        if (data === null || data === undefined){
        this.$message({
          type:"error",
          message:"删除失败!"
        });
        return false;
      }else {
        this.$http.post("/api/deleteProduct",JSON.stringify(data),{headers:{"token":this.token}})
        .then(res=>{
          if (res.data.code === 200) {
            this.$notify({
              type:"success",
              message:"删除成功！"
            });
            this.change(this.currentPage,this.pageSize);
          }else if (res.data.code === 405) {
            this.$message({
              type:"error",
              message:"身份失效，请重新登录！"
            });
            this.$router.push("/login");
          }else {
            this.$message({
              type:"error",
              message:"删除失败！"
            });
          }
        }).catch(err=>{
          this.$message({
              type:"error",
              message:"网络异常，请重试！"
            });
        })
      }
      })
     },
    // 处理分页
    handleCurrentChange(page) {
      this.change(page, this.pageSize);
    },
    //请求分页
    change(currentPage, pageSize) {
       let map = {};
       map.currentPage = currentPage;
       map.pageSize = pageSize;
       map.productName = this.searchForm.productName;
       map.categoryName = this.searchForm.categoryName;

        this.$http
        .post(
          "/api/findProduct",JSON.stringify(map),{headers:{"token":this.token}}
        )
        .then(res => {
           if (res.data.code === 200) {
             this.productData = res.data.list;
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
.search {
  padding-left: 2%;
}
.publish_product {
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
