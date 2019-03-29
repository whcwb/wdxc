import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: () => import('@/components/HelloWorld')
    },{
      path: '/carMess',
      name: 'carMess',
      component: () => import('@/components/carMess')
    },{
      path: '/file',
      name: 'file',
      component: () => import('@/components/file')
    },{
      path: '/trajectory',
      name: 'trajectory',
      component: () => import('@/components/trajectory')
    },{
      path: '/hisLine',
      name: 'hisLine',
      component: () => import('@/components/trajectory/hisLine.vue')
    },{//新增围栏，选着车辆
      path: '/addCrawl',
      name: 'addCrawl',
      component: () => import('@/components/crawl/addCrawl.vue')
    },{//围栏列表
      path: '/crawlList',
      name: 'crawlList',
      component: () => import('@/components/crawl/crawlList.vue')
    },{//创建围栏地图
      path: '/crawl',
      name: 'crawl',
      component: () => import('@/components/crawl/crawl.vue')
    },{//查看围栏地图
      path: '/ShowCrawl',
      name: 'ShowCrawl',
      component: () => import('@/components/crawl/ShowCrawl.vue')
    }

  ]
})
