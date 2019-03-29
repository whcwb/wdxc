import app from './app.ui'
import '#/static/css/box.less'
import '#/static/css/color.less'

let options = {
  app: app,
  beforeEach(to,from,next) {
    console.log('去',to)
    console.log('来',from)
    next()
  }
}

ui.extend({
  // 微信js---初始化
  getWxJs(){
    // 微信js初始化 
    var script = document.createElement("script")
    script.type = "text/javascript"
    script.src="./static/utils/jweixin-1.2.0.js"
    document.body.appendChild(script)

    script.onload = function(){ // 微信js初始化 回调函数
        console.log('*****wx',wx)
        
        // 微信js初始化成功后引用 微信功能方法
        ui.getApp().wxUtil = require('./static/ajax/wechatUtil.js').default

        //获取Code 直
        let authCode = ui.getApp().wxUtil.getQueryString("code");
        console.log('获取code',authCode)
        
        if(authCode){
          
          // 获取Openid
          ui.getApp().wxUtil.vueParent = this;
          ui.getApp().wxUtil.getOpenid(authCode,(res)=>{
              console.log('openid-------',res)
              localStorage.setItem("openid",res);//存储openid
              ui.getApp().wxUtil.initConfig();//执行 微信 config
          });
        }else{
          return 
        }
    }
  },
  //ajax数据请求 基于 ui.request()方法 二次封装
  //参数传入 method    -->"请求方式：'POST','GET',……"
  //        url       -->"网络数据请求地址"
  //        data      -->"网络数据请求的参数传入 ：{key : val} *** 无参数传入时 {} "
  //        callback  -->"回调函数 网络数据返回"
  $http(method,url,data,callback){//网路数据请求

    ui.request({
      // ui.getApp().Ajax.url+':'+ui.getApp().Ajax.port+
      url: url, //仅为示例，并非真实的接口地址
      data: data,
      header: {'Content-Type': 'application/x-www-form-urlencoded'},
      method:method,
      success: function (res) {
        console.log('请求成功')
        if(res.data.code ==403){
          ui.showToast({ title: res.data.message})
          ui.redirectTo({
            url: '/pages/login'
          })
        }else{
          callback && callback(res.data);
        }
      },
      fail:function(err){
        console.log('请求失败')
        callback && callback(err.data);
      },
      complete:function(mess){
        console.log('请求结果')
        // callback && callback(mess.data);
      }
    })
  },

  pageHeight(val){
    return  ui.DEFAULT_CONTENT_HEIGHT + val
  },
})

ui.start(options)