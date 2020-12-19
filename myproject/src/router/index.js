import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import imcoder from '@/views/article/imcoder-tinymce'
import home from '@/components/home'
import login from '@/components/login'
import articleManager from '@/views/article/articleManager';
import articleView from '@/views/article/articleView';
import pictureManager from '@/views/picture/pictureManager';
import addPicture from '@/views/picture/addPicture';
import productCategory from '@/views/product/productCategory'
import addCategory from '@/views/product/addCategory'
import productList from '@/views/product/productList'
import addProduct from '@/views/product/addProduct'
import addVideo from '@/views/video/addVideo'
import videoManager from '@/views/video/videoManager'
Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: login,
      redirect:'/login',
      hidden: true,
      meta: {
        requireAuth: false
      },
    },{
      path: '/login',
      name: 'login',
      component: login,
      hidden: true,
      meta: {
        requireAuth: false
      },
    },
    {
      path: '/home',
      name: 'home',
      component: home,
      redirect: '/productList',
      hidden: true,
      meta: {
        requireAuth: false
      },
      children:[{
        path: '/imcode',
        name: 'imcode',
        component: imcoder,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/articleManager',
        name: 'articleManager',
        component: articleManager,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/articleView',
        name: 'articleView',
        component: articleView,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },
      {
        path:'/pictureManager',
        name:'pictureManager',
        component: pictureManager,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/addPicture',
        name: 'addPicture',
        component: addPicture,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/productCategory',
        name: 'productCategory',
        component: productCategory,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/addCategory',
        name: 'addCategory',
        component: addCategory,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/productList',
        name: 'productList',
        component: productList,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/addProduct',
        name: 'addProduct',
        component: addProduct,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/addVideo',
        name: 'addVideo',
        component: addVideo,
        hidden: true,
        meta: {
          requireAuth: false
        },
      },{
        path: '/videoManager',
        name: 'videoManager',
        component: videoManager,
        hidden: true,
        meta: {
          requireAuth: false
        },
      }],
      scrollBehavior (to, from, savedPosition) {
        return {
          x: 0,
          y: 0
        }
      }
    }
  ]
})
