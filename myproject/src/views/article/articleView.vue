<template>
  <div class="view">
    <div>
      <div class="bar">
        <span class="box">文章管理 / 文章详情</span>
      </div>
    </div>
    <div class="articleDetail">
      <h3 style="padding-top:50px;">{{ article.title }}</h3>
      <p style="font-size:14px;">作者: {{ article.author }}</p>
      <p style="font-size:14px;">发布时间: {{ article.createDate }}</p>
      <hr />
      <div v-html="article.content"></div>
    </div>
    <!-- <div class="goback" style="float:right;padding-top:30px;">
      <el-button @click="goback" type="primary">返回上一级</el-button>
    </div> -->
  </div>
</template>
<script>
export default {
  name: "articleView",
  data() {
    return {
      article: {}
    };
  },
  created() {
    this.article = this.$route.params.data;
  },
  methods: {
      goback() {
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
  margin-top: -18px;
}
.bar span {
  float: left;
  vertical-align: middle;
  font-size: 14px;
  padding-top: 15px;
  padding-left: 2%;
}
</style>
