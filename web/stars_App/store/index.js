import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
import libs from '../common/libs.js'
import createLogger from 'vuex/dist/logger';
const debug = true
const store = new Vuex.Store({
	plugins: debug ? [createLogger()] : [],
	state: {
		dictMap:()=>{return new Map()},
		/**
		 * 是否需要强制登录
		 */
		forcedLogin: false,
		hasLogin: true,
		userName: "",
		userInfo: null,
		userID:null
	},
	mutations: {
		setMap(state,data){
			state.dictMap = data
		},
		userID(state,data){
			state.userID = data
		},
		userInfo(state) {
			console.log('Info')
			libs.getUserInfo()
		},
// 		login(state, userName) {
// 		},
		setUser(state,data){
			state.userName = data
		},
		logout(state) {
			state.userID = null
		}
	}
})

export default store
