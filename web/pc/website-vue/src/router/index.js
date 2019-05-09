import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'

Vue.use(Router)

export default new Router({
  routes: [
    // {
    //   path: '/',
    //   name: 'login',
    //   component: () => import('@/components/login.vue')
    // },
    {
      path: '/',
      name: 'home',
      component: () => import('@/components/home.vue')
    },
    {
      path: '/cpjs',
      name: 'cpjs',
      component: () => import('@/components/cpjs.vue')
    },
    {
      path: '/jjfa',
      name: 'jjfa',
      component: () => import('@/components/jjfa.vue')
    },
    {
      path: '/cgal',
      name: 'cgal',
      component: () => import('@/components/cgal.vue')
    },
    {
      path: '/lxwm',
      name: 'lxwm',
      component: () => import('@/components/lxwm.vue')
    }
  ]
})
