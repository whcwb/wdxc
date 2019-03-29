<template>
  <div class="box_col fileImg" style="background-color: #efeff4">
    <div class="box_col_auto" style="padding-top: 3px;position: relative">
      <!--:bottom-method="loadBottom"-->
      <mt-loadmore :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">

        <div v-if="imgList.length == 0" style="position: relative">
          <div style="text-align: center;margin-top: 20%">
            <img src="static/img/wsj.png" alt="">
            <div style="font-size: 30px;font-weight: 600;color: #b3b3b3">
              暂无图片数据
            </div>
          </div>
        </div>

        <div v-else class="fileBox" v-for="(it,index) in imgList" :key="index">
          <div class="dateTit">日期：{{it.date}}</div>
          <div class="fileList">
            <div class="box_row_list">
              <div class="fileItem" v-for="(item,key) in it.list" :key="key">
                <!--<img :src="static/img/gir.jpg" alt="">-->
                <img :src="'http://www.168car.net:9091/test/'+item.url" :preview="index" :preview-text="it.date" alt="">
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
    name: "file",
    components: {},
    data() {
      return {
        allLoaded: false,//是否开启上拉加载
        imgList: []
      }
    },
    created() {
      this.getImg()
    },
    methods: {
      loadTop() {//下拉
        this.$refs.loadmore.onTopLoaded();
        this.getImg()
        console.log('loadTop');
      },
      goback() {
        this.$router.back()
      },
      // /app/device/videoGroup
      getImg() {
        this.$http.get('/app/device/photoGroup', {
          params: {
            pageNum: 1,
            pageSize: 30,
            zdbh: localStorage.getItem('zdbh')
          }
        }).then(res => {
          if (res.code == 200) {
            this.imgList = res.result
            this.$previewRefresh()
          }
        }).catch(err => {
        })
      }
    }
  }
</script>

<style lang="less">
  .fileImg {
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
          width: 33.33%;
          padding: 0 6px;
          margin-top: 6px;
          img {
            width: 100%;
          }
        }
      }
    }
  }

</style>
