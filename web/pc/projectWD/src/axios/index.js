import Vue from 'vue';
import axios from 'axios'
import api from './api'
import Cookies from 'js-cookie';
import {router} from '../router/index';
import qs from 'qs';
import store from '@/store';
import swal from 'sweetalert2'
import iView from 'iview';
import util from "../libs/util";
import ajaxUrl from './api'
// 订单分配权限

// let url = 'http://119.23.242.234:8080'; // 生产环境(测试时不要改动这里)
// let url = 'http://119.23.242.234:8080'; //
let url = ajaxUrl.url

let httpInstance = axios.create({
    baseURL: url,
    timeout: 300000,
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    withCredentials: true
});
httpInstance.url = url;
// 添加请求拦截器 数据请求之前
httpInstance.interceptors.request.use((config) => {

    if (config.url == "/api/clzd/getzdcl/"
        || '/api/clsbyxsjjl/historygps' == config.url
        || '/api/clsbyxsjjl/pager' == config.url
        // || '/api/cl/carAccStatistics' == config.url
        || '/api/dd/ddzTj' == config.url
        || '/api/dd/ccTTj' == config.url
        || '/api/cl/nianshenbt' == config.url
        || '/api/pb/xbkb' == config.url
        || '/api/zdgl/getWebsocketInfo' == config.url
        || '/api/spk/pager' == config.url
    ) {
    } else {
        // iView.Spin.show({
        //     render: (h) => {
        //         return h('div', [
        //             h('Icon', {
        //                 'class': 'demo-spin-icon-load',
        //                 props: {
        //                     type: 'ios-loading',
        //                     size: 50
        //                 }
        //             }),
        //             h('div', '数据加载中')
        //         ])
        //     }
        // })
    }
// debugger
    var headers = config.headers;
    var contentType = headers['Content-Type'];
    if (contentType == "application/x-www-form-urlencoded") {
        config.data = qs.stringify(config.data);
        try {
            //如果是数组对象，将转换出来的数组字符串中的[]关键字替换，这样方便后台接收数据
            config.data = config.data.replace(/(%5B\d%5D)/g, "");
        } catch (e) {

        }
    }
    // 在发送请求之前做些什么
    if (Cookies.get('accessToken')) {
        let accessToken = JSON.parse(Cookies.get('accessToken'));
        config.headers.token = accessToken.token;
        config.headers.userid = accessToken.userId;
    }
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
    iView.Spin.hide()
});

// 添加响应拦截器 数据响应之后
httpInstance.interceptors.response.use((response) => {
    var v = this
    // 对响应数据做点什么
    iView.Spin.hide()
    if (response.status === 200) {
        if (response.data.code === 999) {
            Cookies.remove('user');
            Cookies.remove('password');
            Cookies.remove('access');
            // 清空打开的页面等数据，但是保存主题数据
            let theme = '';
            if (localStorage.theme) {
                theme = localStorage.theme;
            }
            localStorage.clear();
            if (theme) {
                localStorage.theme = theme;
            }
            swal({
                text: "授权认证失败，请重新登录",
                type: "warning",
                showCancelButton: true,
                confirmButtonText: '确认',
            }).then((isConfirm) => {
                if (isConfirm.value) {
                    router.push({name: 'login'});
                }
            });
        } else if (response.data.code === 200) {
            return response.data;
        } else {
            iView.Message.error({
                content: response.data.message,
                top: 100,
                duration: 4
            });
            return response.data;
        }
    } else {
        iView.Message.error('网络连接异常，请重试！');
    }
}, function (error) {
    // 对响应错误做点什么
    if (!Cookies.get('result')) {
        iView.Message.error('网络连接异常，请重试！');
    } else if (Cookies.get('result')) {
        iView.Message.error('网络连接异常，请重试！');
    }
    return Promise.reject(error);
});
export default httpInstance;
