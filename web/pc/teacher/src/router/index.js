import Vue from 'vue'
import cok from 'js-cookie'
import Router from 'vue-router'

Vue.use(Router)
// export default new Router({
const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/home')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login')
    },
    {
      path: '/center',
      name: 'center',
      component: () => import('@/views/myCenter')
    },
    {
      path: '/feedBack',
      name: 'feedBack',
      component: () => import('@/views/feedback')
    },
    {
      path: '/feedbackHis',
      name: 'feedbackHis',
      component: () => import('@/views/feedbackHis')
    },
    {
      path: '/list',
      name: 'list',
      component: () => import('@/views/pageList')
    },
    {
      path: '/Ycar',
      name: 'Ycar',
      component: () => import('@/views/Ycar')
    },
    {
      path: '/evaluate',
      name: 'evaluate',
      component: () => import('@/views/evaluate')
    }
    ,
    {
      path: '/messList',
      name: 'messList',
      component: () => import('@/views/comp/lineMess')
    }
  ]
})
router.beforeEach((to, from, next) => {
    if (to.name === 'login'){
        next();
        return;
    }
    if (cok.get('result')){
        next();
        return;
    }else{
        next({ name: 'login'});
    }
})
export default router
