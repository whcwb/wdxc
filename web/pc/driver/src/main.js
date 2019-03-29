import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router';
import router from './router'

import iView from 'iview';
import 'iview/dist/styles/iview.css';
import store from './store';


import http from './axios/index';
import apis from './axios/config';
Vue.prototype.$http = http;
Vue.prototype.apis = apis;

Vue.use(VueRouter);
Vue.use(iView);
Vue.config.productionTip = true
const RouterConfig = {
    routes: router
};
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: router,
  store:store,
  components: { App },
  template: '<App/>'
})
