<template>
  <div class="box_col">
    <div class="titbox mapAppTitSty">
      <div class="tit box_row">
        <div class="box_row_100 TITleft" @click="goback">
          <i class="iconfont icon-jiantouarrowhead7"></i>
        </div>
        <div class="box_row_100 TITMess">
          电子围栏
        </div>
        <div class="box_row_100 TITRight">
          {{wlmc}}
          <!--<i class="iconfont icon-shanchu"></i>-->
        </div>
      </div>
    </div>
    <div class="codeMessSty">
      <div class="address">
        <img src="static/img/GPS.png" width="30px" alt="">
        <div class="text" v-if="codeMess">
          {{codeMess.address}}
        </div>
        <div class="text" v-else>
          暂无位置信息
        </div>
      </div>
      <mt-cell title="围栏范围" class="RangeSty">
        <div style="color: #2b85e4">{{Range}}米</div>
      </mt-cell>
    </div>

    <div class="box_col_100" style="position: relative">
      <div id="mapCrawl" style="width: 100%;height: 100%;background-color: #2b85e4"></div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "crawl",
    components:{},
    data() {
      return {
        zict:{'500':16,'1000':15,'1500':14,'2000':14,'2500':14,'3000':13},
        Range:null,
        map: '',
        zoom:15,
        crawlCode: {
          lng:null,
          lat:null
        },//地图移动的中心
        codeMess: null,//地图中心点的位置信息
        wlmc:''
      }
    },
    watch: {
      Range:function(n,o){
        this.zoom = this.zict[n]
        this.BuildMap()
      }
    },
    created() {
      console.log('四季de交替,信息的传递',this.$route.params);
      this.Range = this.$route.params.Range
      this.crawlCode.lng = this.$route.params.lng
      this.crawlCode.lat = this.$route.params.lat
      this.wlmc = this.$route.params.wlmc

    },
    mounted() {
      this.$nextTick(() => {
        // this.BuildMap()
      })
    },
    methods: {
      goback() {
        this.$router.back()
      },
      BuildMap() {
        let v = this
        // let point = new BMap.Point(v.gpsCode.lng, v.gpsCode.lat)
        this.map = new BMap.Map("mapCrawl", {enableMapClick: false});    // 创建Map实例
        let point = new BMap.Point(this.crawlCode.lng, this.crawlCode.lat);
        this.map.centerAndZoom(point, this.zoom);  // 初始化地图,设置中心点坐标和地图级别
        //添加地图类型控件
        this.map.addControl(new BMap.MapTypeControl({
          mapTypes: [
            BMAP_NORMAL_MAP,
            BMAP_HYBRID_MAP
          ]
        }));
        this.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
        this.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件
        this.map.addControl(new BMap.OverviewMapControl());       //添加缩略地图控件
        this.map.addControl(new BMap.NavigationControl()); 				// 添加平移缩放控件


        this.getMapAddress(v.map.getCenter()).then(res => {//获取点的位置信息
          this.codeMess = res
        }).catch(err => {
        })

        this.addMarkerCrawl(point, this.Range)
        this.addMarkerCode(point)

      },
      addMarkerCode(point) {//标点
        let marker = new BMap.Marker(point);
        this.map.addOverlay(marker);               // 将标注添加到地图中
        marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
      },
      addMarkerCrawl(point, radii) {//画圆
        let circle = new BMap.Circle(point, radii, {strokeColor:"blue",strokeWeight:2, strokeOpacity:1}); //fillColor:"red",fillOpacity:1 创建圆
        this.map.addOverlay(circle);            //增加圆
      },
      getMapAddress(code) {//获取点的位置信息
        return new Promise(function (resolve, reject) {
          new BMap.Geocoder().getLocation(new BMap.Point(code.lng, code.lat), (address) => {
            resolve(address)
            reject(address)
          });
        });
      },
      BmapClear() {//清除图层
        this.map.clearOverlays();
      }
    }
  }
</script>

<style lang="less">
  .codeMessSty {
    .address {
      padding: 4px 8px 8px 4px;
      font-size: 16px;
      color: #333;
      position: relative;
      min-height: 30px;
      border-bottom: solid #b3b3b3 1px;
      img {
        position: absolute;
        left: 3px;
        top: 0;
      }
      .text {
        text-indent: 30px;
      }
    }
    .RangeSty {

    }
  }
</style>
