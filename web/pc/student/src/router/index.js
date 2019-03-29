import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  routes: [
  	{
      path: '/login',
      name: 'login',
      component: () => import('@/views/login')
    },
    {
      path: '/',
      name: 'main',
      component: () => import('@/views/main'),
      children: [
	      {
	      	path:'/',
	      	name:'center',
	      	component: () => import('@/views/pageIndex'),
	      },
	      {
	      	path:'/myCenter',
	      	name:'myCenter',
	      	component: () => import('@/views/myCenter'),
	      },
	      {
	       	path:'/feedback',
	       	name:'feedback',
	       	component: () => import('@/views/feedback'),
	      }
      ]
    },
    {
      path: '/lineMess',
      name: 'lineMess',
      component: () => import('@/views/pageIndex/comp/lineMess')
    },
    {
      path: '/mapMess',
      name: 'mapMess',
      component: () => import('@/views/pageIndex/comp/mapMess')
    }
  ]
})

router.beforeEach(function(to, from, next){
//	if(to.path!='/login'&&(state.user.name==''||state.user.password=='')){
//		next({
//	  			path: '/login'
//	  		})
//	}else{
//			next()
//		}
next()
})

export default router
