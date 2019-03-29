import $ from 'jquery';
import urls from './url'

// import wx from '#/static/ajax/jweixin-1.2.0.js'
// var wx = require('#/static/ajax/jweixin-1.2.0.js')
// import router from '@/router'
let wechatUtil = {}

wechatUtil.now = new Date();
wechatUtil.timestamp = parseInt(wechatUtil.now.getTime()/1000);
wechatUtil.appId = 'wxb01394ea85904296';
wechatUtil.token = '';
wechatUtil.sign = '';
wechatUtil.code = '';
wechatUtil.openid = '';
wechatUtil.nonceStr = randomString(16);
wechatUtil.baseUrl = urls.url+':8080/biz/';
wechatUtil.authLoginUrl = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid='+wechatUtil.appId+'&redirect_uri='+urls.url+'/wx&response_type=code&scope=snsapi_userinfo&state=debug&connect_redirect=1#wechat_redirect';

wechatUtil.afterReady = '';
//存储Vue对象，用来在微信方法中，可以调用vue内容
wechatUtil.vueParent = null;
wechatUtil.getQueryString = function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
};

wechatUtil.getCode = ()=>{
    window.location.href = wechatUtil.authLoginUrl;
};
wechatUtil.getOpenid = (code,callback)=>{
    $.ajax({
        // url:wechatUtil.baseUrl+urls.wechat.getOpenid+"?code="+code,
        url:urls.url+':8080/biz'+urls.wechat.getOpenid+"?code="+code,
        type:'get',
        success:function(res){
            console.log(res);
            if (res.code == 200){
                wechatUtil.openid = res.message;
                callback && callback(res.message)
            }else {
              alert('ID获取失败！！！')
            }
        }
    })
};

wechatUtil.initConfig = ()=>{
    let curl = location.href.split('#')[0];
  // let url = wechatUtil.baseUrl+urls.wechat.getJsApiSign+"?&timestamp="+wechatUtil.timestamp+"&url="+encodeURIComponent(curl)+'&nonceStr='+wechatUtil.nonceStr;
  let url = urls.url+':8080/biz'+urls.wechat.getJsApiSign+"?&timestamp="+wechatUtil.timestamp+"&url="+encodeURIComponent(curl)+'&nonceStr='+wechatUtil.nonceStr;
  $.ajax({
    url:url,
    type:'get',
    success:function(res){
      if (res.code == 200){
        wechatUtil.sign = res.message;
        wechatUtil.config();
      }
    }
  })
};


wechatUtil.config = function(){
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: wechatUtil.appId, // 必填，公众号的唯一标识
        timestamp: wechatUtil.timestamp, // 必填，生成签名的时间戳
        nonceStr: wechatUtil.nonceStr, // 必填，生成签名的随机串
        signature: wechatUtil.sign,// 必填，签名
        jsApiList: ['scanQRCode','chooseImage','uploadImage','previewImage','chooseWXPay'] // 必填，需要使用的JS接口列表
    });
    wechatUtil.refreshNonceStr();
};
wechatUtil.refreshNonceStr = ()=>{
    wechatUtil.nonceStr = randomString(16);
};

wechatUtil.pay = function(prepay,callback){
    wx.chooseWXPay({
        appId:prepay.appId,
        timestamp: prepay.timeStamp,// 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
        nonceStr: prepay.nonceStr, // 支付签名随机串，不长于 32 位
        package: prepay.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=\*\*\*）
        signType: prepay.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
        paySign: prepay.paySign, // 支付签名
        success: function (res) {
          callback && callback(res)
// 支付成功后的回调函数
        }
    });
};
wechatUtil.checkJsApi = ()=>{
  wx.checkJsApi({
    jsApiList: ['scanQRCode','chooseImage','uploadImage','previewImage','chooseWXPay'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
    success: function(res) {
      console.log(res);
      // 以键值对的形式返回，可用的api值true，不可用为false
      // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
    }
  });
};
wx.ready(function(){
    localStorage.setItem("projectType",true)//微信js加载成功
    if (typeof wechatUtil.afterReady == 'function'){
        wechatUtil.afterReady('wx');
        return;
    }
    //微信jsapi使用，不允许页面切换，所以这里使用router来做页面跳转，不再使用location强制跳页面
    if (wechatUtil.vueParent){
    //   wechatUtil.vueParent.$router.push({name:'indexName'});
    console.log('页面跳转')
      return;
    }
    wechatUtil.checkJsApi();
    //window.location.href = "/wx/";
    // chooseImage();
    // wechatUtil.qrScan();
    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
});

wx.error(function(res){
    console.log('error',res);
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
});
//--------------------------------------------------------------------------
//以上功能方法 是调用微信开发功能的前期准备*******调用wechatUtil.getAccessToken()
//---------------------------------------------------------------------------
wechatUtil.qrScan = (callback)=>{//打开微信扫码功能
    wx.scanQRCode({
        needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
        scanType: ["qrCode"], // 可以指定扫二维码还是一维码，默认二者都有
        success: function (res) {
            // alert(res)
            var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
            // alert(result);
            callback && callback(result);
        },
        fail : function(res) {
            console.log(res)
            // alert(JSON.stringify(res));
        }
    });
};
wechatUtil.chooseImage = (callback)=>{//拍照或从手机相册中选图接口
    console.log('chooseImage');
    wx.chooseImage({
        count: 1, // 默认9
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
            var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            console.log(res);
            callback && callback(localIds);
        }
    });
};
wechatUtil.previewImage = ()=>{//预览图片接口
  wx.previewImage({
    current: '', // 当前显示图片的http链接
    urls: [] // 需要预览的图片http链接列表
  })
};
wechatUtil.uploadImage = (id , callback)=>{//上传图片接口
    wx.uploadImage({
        localId: id, // 需要上传的图片的本地ID，由chooseImage接口获得
        isShowProgressTips: 1, // 默认为1，显示进度提示
        success: function (res) {
          console.log(res);
          var serverId = res.serverId; // 返回图片的服务器端ID
          callback && callback(res)
        }
    });
};
wechatUtil.getMapCode = (callback)=>{
    wx.getLocation({
        type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
        success: function (res) {
            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
            var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
            var speed = res.speed; // 速度，以米/每秒计
            var accuracy = res.accuracy; // 位置精度
            callback && callback(res)
        }
    });
};
function randomString(len) {
    len = len || 32;
    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
    var maxPos = $chars.length;
    var pwd = '';
    for (let i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}

export default wechatUtil;