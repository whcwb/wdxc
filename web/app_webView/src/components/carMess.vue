<template>
  <div class="box_col carMess">
    <div class="titbox mapAppTitSty">
      <div class="tit box_row">
        <div class="box_row_100 TITleft" @click="goback">
          <i class="iconfont icon-jiantouarrowhead7"></i>
        </div>
        <div class="box_row_100 TITMess">
          消息
        </div>
        <div class="box_row_100 TITRight">
          <!--<i class="iconfont icon-shanchu"></i>-->
        </div>
      </div>
    </div>
    <div class="box_col_auto" style="background-color: #efeff4;overflow-y: auto">
      <div style="text-align: center;margin-top: 20%">
        <img src="static/img/wsj.png" alt="">
        <div style="font-size: 30px;font-weight: 600;color: #b3b3b3">
          暂无消息通知
        </div>
      </div>
      <div v-if="false" class="messItem" v-for="(it,index) in 3">
        <div class="time">
          时间
        </div>
        <div class="boxMess">
          <div class="tit">流量即将耗尽提醒</div>
          <div class="mess">
            您的设备流量即将耗尽，戳我购买>>
          </div>
          <div class="remove">
            <i class="iconfont icon-shanchu"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    name: "carMess",
    data(){
      return{
        paramsUrl:false
      }
    },
    created(){
      if(this.$route.params.urls){
        this.paramsUrl = this.$route.params.urls
      }
    },
    mounted(){
      // this.plusReady()
      this.$nextTick(()=>{
        var v = this
        document.addEventListener( "plusready", function(){
          console.log('plus_初始化成功');
          try {
            console.log('**********back');
            plus.key.addEventListener('backbutton',function(){
              console.log('back');
              if(v.paramsUrl){
                v.$router.back()
              }else {
                uni.switchTab({
                  url: '/pages/user/user'
                });
              }
            },false);
          }catch (e) {}
        }, false );
      })
    },
    methods:{
      plusReady(){
        console.log('**********back');
        // Android处理返回键
        var v = this
        try {
          plus.key.addEventListener('backbutton',function(){
            console.log('back');
            if(v.paramsUrl){
              v.$router.back()
            }else {
              uni.switchTab({
                url: '/pages/user/user'
              });
            }
          },false);
        }catch (e) {}
      },
      goback(){
        if(this.$route.params.urls){
          this.$router.back()
        }else {
          uni.switchTab({
            url: '/pages/user/user'
          });
        }
      }
    }
  }
</script>

<style lang="less">
.carMess{

  .messItem{
    padding: 0 15px;
    .time{
      padding: 8px 0;
      text-align: center;
      font-size: 14px;
    }
    .boxMess{
      overflow-x: auto;
      padding: 10px 14px;
      background-color: #fff;
      border-radius: 10px;
      position: relative;
      .tit{
        font-size: 17px;
        font-weight: 600;
      }
      .mess{
        padding: 4px 0;
        color: #999999;
        font-size: 14px;
      }
      .remove{
        background-color: #f16643;
        position: absolute;
        border-radius: 10px;
        top: 0;
        right: 0;
        right: -89px;
        height: 70px;
        width: 70px;
        text-align: center;
        line-height: 70px;
        i{
          color: #fff;
          font-size: 50px;
        }
      }
    }
  }
}

.goLeft{
}
@keyframes goLeft {
from { transform: translateX(1)}
to   { transform: translateX(-90)}
}
</style>
