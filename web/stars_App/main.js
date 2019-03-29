import Vue from 'vue'
import App from './App'

import store from './store'
// import swipeCell from 'vue-swipe-cell';
// Vue.use(swipeCell);

// import Mint from 'mint-ui';
// import 'mint-ui/lib/style.css'
// Vue.use(Mint);

// uni 自定义组件
import uniNavBar from "./components/uni-nav-bar.vue"
Vue.component('titBar', uniNavBar)
import pageHead from './components/page-head.vue'
import pageFoot from './components/page-foot.vue'
Vue.component('page-head', pageHead)
Vue.component('page-foot', pageFoot)

// uni 自定义组件--end

import http from './ajax/index.js'
Vue.prototype.$http = http;

import apis from './ajax/apis.js'
Vue.prototype.apis = apis;

import lib from './common/libs.js'
Vue.prototype.libs = lib;

//内封装字典存储与获取
import dictUtil from './libs/dictUtil'
Vue.prototype.dictUtil = dictUtil;

Vue.config.productionTip = false

Vue.prototype.$store = store

App.mpType = 'app'

const app = new Vue({
	store,
	...App
})
app.$mount()
