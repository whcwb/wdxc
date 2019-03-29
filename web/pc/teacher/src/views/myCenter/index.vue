<style lang="less">
  @import "./center.less";
</style>
<template>
	<div class="myCenter box">
    <div class="body" style="margin-top: 0.2rem">
      <div style="text-align: center">
        <img
          style="width: 1.6rem"
          src="/static/logo.png" alt="">
      </div>
      <div class="Flist" @click="pageList">
        <i class="iconfont icon-list"></i>
        <span>
           订单
        </span>
      </div>
      <div class="Flist" @click="feedback()">
        <i class="iconfont icon-ybbfeedback"></i>
        <span>
          建议反馈
          <!--<a href="tel:18620202022"></a>-->
        </span>
      </div>
      <!--<div class="Flist">-->
        <!--<i class="iconfont icon-setting"></i>-->
        <!--<span>-->
           <!--设置-->
        <!--</span>-->
      <!--</div>-->
    </div>
    <div class="LoginClose" @click="esc">
        <button>退出登录</button>
    </div>
	</div>
</template>

<script>

	export default{
		name:'',
		components: {
		},
    data(){
		  return{

      }
    },
    methods:{
      pageList(){
        this.$router.push({
          name:'list'
        })
      },
      feedback(){//信息反馈
        this.$router.push({
          name: 'feedBack'
        });
      },
      esc(){
        this.cok.set("token",'');
        this.cok.set('result','')
        this.$router.push({
          name: 'login'
        });
      }

    }
	}
</script>

<style>
</style>
