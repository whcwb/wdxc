import axios from 'axios';
import qs from 'qs';
import ajaxUrl from './api'
import { Toast } from 'mint-ui';
import CryptoJS from 'crypto-js'
// 订单分配权限
let url = ajaxUrl.url

let httpInstance = axios.create({
  baseURL: url,
  timeout: 30000,
  headers: {'Content-Type':'application/x-www-form-urlencoded'},
  withCredentials: true
});


httpInstance.url = url;

let clientId = "app"
let secret = '12341234'
function encryptByDES(message, key){
  const keyHex = CryptoJS.enc.Utf8.parse(key);
  const encrypted = CryptoJS.DES.encrypt(message, keyHex, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });
  return encrypted.toString();
}
function genToken(){
  let s = clientId + '-' + new Date().getTime();
  console.log(s)
  return encryptByDES(s,secret);
}

let instanceSTAR = null
let instanceOVER = null

// 添加请求拦截器 数据请求之前
httpInstance.interceptors.request.use((config) => {
  console.log('前',config);

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


  config.headers.token = genToken();
  if(localStorage.getItem('UserID')){
    config.headers.user_id=localStorage.getItem('UserID')
  }
  // this.instanceSTAR = Toast({
  //   message: '加载中……',
  //   className:"IconLoading"
  // });
  return config;
}, function (error) {
  console.log(error);
  this.instanceSTAR.close();

  this.instanceOVER = Toast({
    message: '网络异常s',
  });
  setTimeout(() => {
    this.instanceOVER.close();
  }, 500);
  console.log(error);
  // 对请求错误做些什么
  return Promise.reject(error);
});

// 添加响应拦截器 数据响应之后
httpInstance.interceptors.response.use((response) => {
  // this.instanceSTAR.close();
  return response.data;;
}, function (error) {
  console.log(error);
  this.instanceOVER = Toast({
    message: '网络异常',
  });
  setTimeout(() => {
    this.instanceOVER.close();
  },500);
  return Promise.reject(error);
});
export default httpInstance;
