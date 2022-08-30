import { isValid } from '@/utils/jwtutils';
import { getItem } from '@/utils/storage';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/', name: 'Home', component: () => import('@/views/Index.vue'),
    meta: { title: '预付卡', keepAlive: true }
  },
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('@/views/Register.vue') },
  { path: '/card/:id', name: 'Card', component: () => import('@/views/Card.vue') },
  { path: '/setting', name: 'Setting', component: () => import('@/views/Setting.vue') },
  {
    path: '/record', name: 'Record', component: () => import('@/views/Record.vue'),
    meta: { title: '消费记录', keepAlive: true }
  },
  { path: '/balance', name: 'Balance', component: () => import('@/views/Balance.vue') },
  { path: '/mycard/:id/:payCardId', name: 'MyCard', component: () => import('@/components/MyCard/MyCardPayCardInfo.vue'), },
  { path: '/mycard/:id/:payCardId/pay', name: 'Pay', component: () => import('@/components/MyCard/MyCardPayCardInfoPay.vue'), },
  { path: '/consume/id/:consumeId', name: 'Consume', component: () => import('@/views/Consume.vue'), },
  { path: '/scan', name: 'Scan', component: () => import('@/views/Scan.vue') },
  { path: '/refund', name: 'Refund', component: () => import('@/views/Refund.vue') },
  // { path: '/:catchAll(.*)', redirect: '/404' },
  { path: '/:catchAll(.*)', component: () => import('@/views/404.vue') },
  { path: '/401', name: '401', component: () => import('@/views/401.vue') },
  { path: '/404', name: '404', component: () => import('@/views/404.vue') },]

const router = createRouter({
  history: createWebHistory(), routes
})

//配置拦截器
const noauth = ['/login', '/register', '/', '/401', '/404'];
router.beforeEach((to, from, next) => {
  if (noauth.includes(to.path)) { next(); } else {
    const token = getItem("token");
    if (token === null || token === '' || !isValid(token)) {
      next('/401');
    } else {
      document.title = "智能预付卡"
      next();
    }
  }
});



export default router
