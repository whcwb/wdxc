import Vue from 'vue'
import Router from 'vue-router'
import store from '../store';
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
    },
    {
      path: '/XCmess',
      name: 'XCmess',
      component: () => import('@/views/XCmess')
    },
    {
      path: '/okModal',
      name: 'okModal',
      component: () => import('@/views/pageIndex/comp/comp/okModal')
    }
  ]
})

router.beforeEach(function(to, from, next){
  if(store.state.app.user=='null'){
    next({
      name:'login'
    })
  }else {
    next()
  }
//	if(to.path!='/login'&&(state.user.name==''||state.user.password=='')){
//		next({
//	  			path: '/login'
//	  		})
//	}else{
//			next()
//		}
})

export default router
