<style lang="less">
  #cgal {
    .copyRight {
      height: 74px;
      background-image: url("../assets/img/copyRight.png");
    }
    .centerBG {
      height: 10.60rem;
      background-image: url("../assets/img/cgalbg.png");
    }
  }
</style>
<template>
  <div id="cgal">
    <div class="pagerTiT">
      <div class="text" @click="go('home')" style="left: 33.3%">
        首页
      </div>
      <div class="text" @click="go('cpjs')" style="left: 40.1%">
        产品与技术
      </div>
      <div class="text" @click="go('jjfa')" style="left: 49%">
        解决方案
      </div>
      <div class="text" @click="go('cgal')" style="left: 57.1%">
        成功案例
      </div>
      <div class="text" @click="go('lxwm')" style="left: 65.3%">
        联系我们
      </div>
      <div class="text" @click="login"
           style="left: 71.1%;border-left: 1px solid #008dd5;padding-left: 32px">
        登录
      </div>
      <img src="../assets/img/SJ.png"
           style="width:29px;height: 10px;position: absolute;bottom: 0;left: 58.1%" alt="">
    </div>
    <div class="centerBG bgImg"></div>
    <div class="copyRight bgImg"></div>
  </div>
</template>

<script>
  export default {
    name: "cgal",
    methods: {
      go(goName) {
        this.$router.push({
          name: goName
        })
      },
      login(){
        window.location.href = 'http://www.168car.net:9090'
        // window.open('http://www.baidu.com')
      }
    }
  }
</script>

<style scoped>

</style>
