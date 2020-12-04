function findAllCategory(options,token){
    this.$http.get("/api/findAllCategory",{headers:{"token":token}})
    .then(res=>{
      if (res.data.code === 200) {
         options = res.data.data;
      }else if (res.data.code === 405) {
        this.$message({
          type:"error",
          message:"身份失效，请重新登录!"
        });
        this.$router.push("/login");
      }
    })
 }
 export default findAllCategory;