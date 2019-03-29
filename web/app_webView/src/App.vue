<template>
  <div id="app" style="height: 100%;">
    <router-view/>
  </div>
</template>
<!--119.23.242.234:9195-->
<script>
export default {
  name: 'App',
  mounted(){
    this.$nextTick(()=>{
      document.addEventListener('UniAppJSBridgeReady', function () {
        console.log('uniApp__api 初始化成功');
      })

      // 监听plusready事件
      document.addEventListener( "plusready", function(){
        console.log('plus_初始化成功');
      }, false );
    })
  },
  methods:{

  }
}
</script>

<style lang="less">
  @import "./assets/font/iconfont.css";
  /*@import "//at.alicdn.com/t/font_959231_pplegvirxah.css";*/
  .IconLoading{
    /*padding: 10px 50px!important;*/
    .icon-jiazaizhong{
      /*font-size: 80px!important;*/
      animation: loading 3s ease infinite;
    }
    /*.mint-toast-text{*/
      /*font-size: 26px*/
    /*}*/
  }
  @keyframes loading
  {
    from {transform:rotate(0deg);}
    to {transform:rotate(360deg);}
  }
</style>
