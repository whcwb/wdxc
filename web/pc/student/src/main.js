import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router';
import router from './router'
import store from './store';
import iView from 'iview';
import 'iview/dist/styles/iview.css';

import apis from './axios/config';
import http from './axios/index';

Vue.prototype.apis = apis;
Vue.prototype.$http = http;

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
