<template>
  <div class="box_col fileList" style="background-color: #efeff4;position: relative">
    <!--<div style="position: absolute;left: 0;top: 0;right: 0;bottom: 0;z-index: 9999;background-color: #2b85e4">-->
      <!--<mt-swipe>-->
        <!--<mt-swipe-item style="background-color: #b3b3b3;position: relative">-->
          <!--<img style="width: 100%;position: absolute;top: 50%;transform: translateY(-50%)" src="http://www.168car.net:9091/test/865923030038753\2018-12-25\images\car_8659230300387531545725123F2018_12_25_160516.jpg" alt="">-->
        <!--</mt-swipe-item>-->
      <!--</mt-swipe>-->
    <!--</div>-->
    <div class="titbox mapAppTitSty">
      <div class="tit box_row">
        <div class="box_row_100 TITleft" @click="goback">
          <i class="iconfont icon-jiantouarrowhead7"></i>
        </div>
        <div class="box_row_100 TITMess">
          文件
        </div>
        <div class="box_row_100 TITRight">
        </div>
      </div>
    </div>
    <div>
      <mt-navbar class="mtNavbar" v-model="selected">
        <mt-tab-item id="1">图片</mt-tab-item>
        <mt-tab-item id="2">视频</mt-tab-item>
      </mt-navbar>
    </div>
    <div class="box_col_auto" style="padding-top: 3px">
      <file-img v-if="selected=='1'"></file-img>
      <file-video v-else-if="selected=='2'"></file-video>
    </div>
  </div>
</template>

<script>
  import fileImg from './FileComp/FileImg'
  import fileVideo from './FileComp/FileVideo'
  export default {
    name: "file",
    components: {fileImg,fileVideo},
    data() {
      return {
        selected: '1'
      }
    },
    watch:{
      selected:function (n,o) {
        console.log(n);
      }
    },
    created() {

    },
    methods: {
      goback() {
        this.$router.back()
      }
    }
  }
</script>

<style lang="less">
  .fileList {

  }

  .mtNavbar {
    .mint-tab-item-label {
      font-size: 16px;
    }
  }
</style>
