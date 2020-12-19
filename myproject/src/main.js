// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import "@/assets/iconfont/iconfont.css";
import axios from 'axios'
//进度条
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

//进度条
NProgress.configure({
  easing: 'ease',  // 动画方式
  speed: 500,  // 递增进度条的速度
  showSpinner: false, // 是否显示加载ico
  trickleSpeed: 200, // 自动递增间隔
  minimum: 0.3 // 初始化时的最小百分比
})

router.beforeEach((to, from , next) => {
  // 每次切换页面时，调用进度条
  NProgress.start();
  // 这个一定要加，没有next()页面不会跳转的。这部分还不清楚的去翻一下官网就明白了
  next();
})
router.afterEach(() => {
  // 在即将进入新的页面组件前，关闭掉进度条
  NProgress.done()
})
//axios.defaults.baseURL = 'http://localhost:8089/api'
axios.defaults.headers.post['Content-Type'] = 'application/json';
Vue.prototype.$http = axios;
Vue.config.productionTip = false
Vue.use(ElementUI);
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
