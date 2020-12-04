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
import empty from '@/components/empty'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: HelloWorld,
      redirect:'/login'
    },{
      path: '/login',
      name: 'login',
      component: login
    },{
      path:"/emppty",
      name:"empty",
      component: empty
    },
    {
      path: '/home',
      name: 'home',
      component: home,
      redirect: '/productList',
      children:[{
        path: '/imcode',
        name: 'imcode',
        component: imcoder
      },{
        path: '/articleManager',
        name: 'articleManager',
        component: articleManager
      },{
        path: '/articleView',
        name: 'articleView',
        component: articleView
      },
      {
        path:'/pictureManager',
        name:'pictureManager',
        component: pictureManager
      },{
        path: '/addPicture',
        name: 'addPicture',
        component: addPicture
      },{
        path: '/productCategory',
        name: 'productCategory',
        component: productCategory
      },{
        path: '/addCategory',
        name: 'addCategory',
        component: addCategory
      },{
        path: '/productList',
        name: 'productList',
        component: productList
      },{
        path: '/addProduct',
        name: 'addProduct',
        component: addProduct
      }]
    }
  ]
})
