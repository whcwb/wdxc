import Vue from 'vue';
import iView from 'iview';
import {router} from './router/index';
import {appRouter} from './router/router';
import store from './store';
import App from './app.vue';
import '@/locale';
import 'iview/dist/styles/iview.css';
import VueI18n from 'vue-i18n';
import http from './axios/index';
import util from './libs/util';
import apis from './axios/api';
import dictUtil from './libs/dictUtil';
import websocketUtil from './libs/websocketUtil';
import session from './libs/session';
import './styles/common.less'

import swal from 'sweetalert2'

Vue.use(VueI18n);
Vue.use(iView);

//替换原始网络框架
Vue.prototype.session = session
Vue.prototype.$http = http;
Vue.prototype.util = util;
Vue.prototype.apis = apis;
Vue.prototype.dictUtil = dictUtil;
Vue.prototype.websocketUtil = websocketUtil;
Vue.prototype.swal = swal;



Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length == 1 ? o[k] :
                ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}
new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App),
//  data: {
//      currentPageName: ''
//  },
    mounted () {
        this.currentPageName = this.$route.name;
        // 显示打开的页面的列表
//      this.$store.commit('setOpenedList');
//      this.$store.commit('initCachepage');
        // 权限菜单过滤相关
//      this.$store.commit('updateMenulist');
        // iview-admin检查更新
//      util.checkUpdate(this);
    },
    created () {
        let tagsList = [];
        appRouter.map((item) => {
            if (item.children.length <= 1) {
                tagsList.push(item.children[0]);
            } else {
                tagsList.push(...item.children);
            }
        });
        this.$store.commit('setTagsList', tagsList);
    }
});
