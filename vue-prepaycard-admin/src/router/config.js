import TabsView from '@/layouts/tabs/TabsView'
// import BlankView from '@/layouts/BlankView'
// import PageView from '@/layouts/PageView'

// 路由配置
const options = {
  routes: [
    {
      path: '/login',
      name: '登录页',
      component: () => import('@/pages/login')
    },
    // //  todo 运营方登录
    // {
    //   path: '/operatorLogin',
    //   name: '运营方登录页',
    //   component: () => import('@/pages/operatorLogin'),
    // },
    // {
    //   path: '/operator',
    //   name: '运营方首页',
    //   component: TabsView,
    //   children: [
    //     {
    //       path: 'operatorDemo',
    //       name: '运营方审批页',
    //       meta: {
    //         icon: 'dashboard',
    //       },
    //       component: () => import('@/pages/operator/operatorDemo')
    //     },
    //   ]
    // },
    {
      path: '*',
      name: '404',
      component: () => import('@/pages/exception/404'),
    },
    {
      path: '/403',
      name: '403',
      component: () => import('@/pages/exception/403'),
    },
    {
      path: '/',
      name: '首页',
      component: TabsView,
      redirect: '/login',
      children: [
        {
          path: 'ReviewResult',
          name: '审核结果',
          meta: {
            icon: 'dashboard',
          },
          component: () => import('@/pages/ReviewResult'),  // 具体加载的组件
        },
        {
          path: 'SubmitReview',  // url 显示的路径
          name: '审核提交',  // 页面名字
          meta: {
            icon: 'form',
          },
          component: () => import('@/pages/SubmitReview'),  // 具体加载的组件
        },
        {
          path: 'CardDetail',
          name: '预付卡总览',
          meta: {
            icon: 'dashboard',
          },
          component: () => import('@/pages/CardDetail'),  // 具体加载的组件
        },
        {
          path: 'RefundReview',
          name: '退卡审核',
          meta: {
            icon: 'dashboard',
          },
          component: () => import('@/pages/RefundReview'),  // 具体加载的组件
        },
        // {
        //   path: 'MerchantDataStatistic',
        //   name: '数据统计',
        //   meta: {
        //     icon: 'dashboard',
        //   },
        //   component: () => import('@/pages/MerchantDataStatistic'),
        // },
        // {
        //   path: 'demo',
        //   name: 'demo演示页',
        //   meta: {
        //     icon: 'file-ppt'
        //   },
        //   component: () => import('@/pages/demo')
        // }
      ]
    }
  ]
}

export default options
