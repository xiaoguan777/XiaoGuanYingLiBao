import {createRouter, createWebHistory} from 'vue-router'
import IndexView from "@/views/IndexView.vue";

const routes = [
  {
    path: '/',
    name: 'home',
    component: IndexView
  },
  {
    path: '/page/product/list',
    name: 'ProductList',
    component: () => import('../views/ProductList.vue')
  },
  {
    path: '/page/product/detail',
    name: 'ProductDetail',
    component: () => import('../views/ProductDetail.vue')
  },
  {
    path: '/page/user/login',
    name: 'LoginView',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/page/user/register',
    name: 'RegisterView',
    component: () => import('../views/RegisterView.vue')
  },
  {
    path: '/page/user/realName',
    name: 'RealNameView',
    component: () => import('../views/RealNameView.vue')
  },
  {
    path: '/page/user/userCenter',
    name: 'UserCenterView',
    component: () => import('../views/UserCenterView.vue')
  },
  {
    path: '/page/user/userPay',
    name: 'UserPayView',
    component: () => import('../views/UserPayView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,

})


export default router
