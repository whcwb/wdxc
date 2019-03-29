import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
import createLogger from 'vuex/dist/logger';
const debug = true
const store = new Vuex.Store({
	plugins: debug ? [createLogger()] : [],
	state: {
	  carMess:{}
	},
	mutations: {
		setCarMess(state,data){
			state.carMess = data
		}
	}
})

export default store
