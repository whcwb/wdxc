import Vue from 'vue';
import axios from 'axios'
import api from './api'
import Cookies from 'js-cookie';
import router from '../router/index';
import qs from 'qs';
//订单分配权限
let httpInstance = axios.create({
	// baseURL: 'http://192.168.31.181:9099',
	baseURL: 'http://47.98.39.45:9099',
    timeout: 300000,
    headers: {'Content-Type':'application/x-www-form-urlencoded'},
    withCredentials:true
});
// 添加请求拦截器 数据请求之前
httpInstance.interceptors.request.use((config) => {

    var headers = config.headers;
    var contentType = headers['Content-Type'];
    if (contentType == "application/x-www-form-urlencoded"){
        config.data = qs.stringify(config.data);
        try{
            //如果是数组对象，将转换出来的数组字符串中的[]关键字替换，这样方便后台接收数据
            config.data = config.data.replace(/(%5B\d%5D)/g,"");
        }catch(e){

        }
    }
    // 在发送请求之前做些什么
   if(Cookies.get('token')){
    config.headers.token = Cookies.get('token');
   }
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器 数据响应之后
httpInstance.interceptors.response.use((response) => {
		var v = this
//     对响应数据做点什么
    	return response.data;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});
export default httpInstance;
