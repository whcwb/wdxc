<template>
  <div class="box_col fileVideo" style="background-color: #efeff4">
    <div class="box_col_auto" style="padding-top: 3px">
      <!--:bottom-method="loadBottom"-->
      <mt-loadmore :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <div v-if="videoList.length == 0" style="position: relative">
          <div style="text-align: center;margin-top: 20%">
            <img src="static/img/wsj.png" alt="">
            <div style="font-size: 30px;font-weight: 600;color: #b3b3b3">
              暂无视频数据
            </div>
          </div>
        </div>
        <div v-else class="fileBox" v-for="(it,index) in videoList" :key="index">
          <div class="dateTit">日期：{{it.date}}</div>
          <div class="fileList">
            <div class="box_row_list">
              <div class="fileItem" v-for="(item,key) in it.list" :key="key">
                <div style="width: 100%">
                  <div v-if="!item.autoplay" style="width: 100%;position: relative"
                       @click="item.autoplay = !item.autoplay">
                    <img :src="'http://www.168car.net:9091/test/'+item.imgdz" alt="">
                    <div class="modalCar"></div>
                    <i class="iconfont icon-triangle-right"></i>
                  </div>
                  <div v-else style="position: relative">
                    <div @click="item.autoplay = !item.autoplay"
                         style="color: red;position: absolute;top:0px;right: 0px;z-index: 888;font-size: 18px;font-weight: 600">
                      关闭
                    </div>
                    <video autoplay controls :src="'http://www.168car.net:9091/test/'+item.url"></video>
                  </div>

                </div>
                <div style="text-align: center">
                  {{item.cjsj.substring(11,19)}}
                </div>
              </div>
            </div>
          </div>
        </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
  export default {
    name: "FileVideo",
    components: {},
    data() {
      return {
        allLoaded: false,//是否开启上拉加载
        videoList: []
      }
    },
    created() {
      this.getVideo()
    },
    methods: {
      loadTop() {//下拉
        this.$refs.loadmore.onTopLoaded();
        this.getVideo()
        console.log('loadTop');
      },
      goback() {
        this.$router.back()
      },
      videoPlay(it) {
        it = !it
      },
      getVideo() {
        this.$http.get('/app/device/videoGroup', {
          params: {
            pageNum: 1,
            pageSize: 30,
            zdbh: localStorage.getItem('zdbh')
          }
        }).then(res => {
          if (res.code == 200) {
            res.result.forEach((it, index) => {
              if (it.list) {
                it.list.forEach((item, val) => {
                  item.autoplay = false
                  item.imgdz = item.url.replace('video', 'cache');
                  item.imgdz = item.imgdz.replace('mp4', 'jpg')
                })
              }
            })

            this.videoList = res.result
          }
        }).catch(err => {
        })
      }
    }
  }
</script>

<style lang="less">
  .fileVideo {
    .fileBox {
      background-color: #fff;
      margin: 8px 0;
      padding: 2px 14px;
      .dateTit {
        font-size: 14px;
        font-weight: 600;
      }
      .fileList {
        overflow: auto;
        .fileItem {
          width: 100%;
          padding: 0 6px;
          margin-top: 6px;
          .modalCar {
            position: absolute;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: #0000004d;
            z-index: 100;
          }
          .icon-triangle-right {
            position: absolute;
            top: 50%;
            left: 50%;
            z-index: 101;
            transform: translate(-50%, -50%);
            font-size: 30px;
            color: #9e9e9e;
          }
          img {
            width: 100%;
            min-height: 180px;
          }
          video {
            width: 100%;
            min-height: 180px;
          }
        }
      }
    }
  }
</style>
