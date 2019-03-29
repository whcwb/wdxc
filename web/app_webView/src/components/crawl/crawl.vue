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
        <div style="color: #2b85e4" @click="popupVisible=!popupVisible">{{Range}}米  <i class="iconfont icon-jiantou"></i> </div>
      </mt-cell>
    </div>

    <div class="box_col_100" style="position: relative">
      <div id="mapCrawl" style="width: 100%;height: 100%;background-color: #2b85e4"></div>
    </div>
    <div style="padding: 8px 12px;background-color: #f5f5f5;border-top: solid 2px #d9d9d9">
      <mt-button type="primary" style="width: 100%" @click="setCrawlName">完 成</mt-button>
    </div>
    <mt-popup
      v-model="popupVisible"
      popup-transition="popup-fade">
      <div style="width: 300px">
        <mt-radio
          title="围栏范围"
          align="right"
          v-model="Range"
          :options="RangeList">
        </mt-radio>
      </div>
    </mt-popup>
  </div>
</template>

<script>
  import { MessageBox } from 'mint-ui';
  export default {
    name: "crawl",
    components:{},
    data() {
      return {
        popupVisible:false,
        RangeList:['500','1000','1500','2000','2500','3000'],
        zict:{'500':16,'1000':15,'1500':14,'2000':14,'2500':14,'3000':13},
        Range:'1000',
        map: '',
        zoom:15,
        crawlCode: {
          lng:114.308136,
          lat:30.603689
        },//地图移动的中心
        codeMess: null,//地图中心点的位置信息
        wlmc:'',//围栏名称
      }
    },

    watch: {
      Range:function(n,o){
        this.popupVisible = false
        this.zoom = this.zict[n]
        this.BmapClear()
        this.BuildMap()
      },
      crawlCode: function (n, o) {
        this.BmapClear()
        this.addMarkerCode(new BMap.Point(this.crawlCode.lng, this.crawlCode.lat))
        this.addMarkerCrawl(new BMap.Point(this.crawlCode.lng, this.crawlCode.lat), this.Range)
        this.getMapAddress(n).then(res => {
          this.codeMess = res
        }).catch(err => {
        })
      }
    },
    created() {
      console.log('车辆666',this.$route.params.carIds);
    },
    mounted() {
      this.$nextTick(() => {
        this.BuildMap()
      })
    },
    methods: {
      //v.map.setViewport(dotList);
      setCrawlName(){
        var v = this
        MessageBox.prompt('给围栏起个名字吧？').then(({ value, action }) => {
          if(value && action=='confirm'){
            v.wlmc = value
            v.goCrawlList()
          }else if(!value && action=='confirm') {
            this.Toast({
              message: '没有名字的围栏,不能被创建呦!',
              duration: 4000
            });
          }
        });
      },
      goCrawlList(){
        this.$http.post('/app/dzwl/save',{
          wlmc:this.wlmc,
          ksjd:this.crawlCode.lng,
          kswd:this.crawlCode.lat,
          wlfw:this.Range,
          dlxxzb:this.codeMess.address,
          clIds:this.$route.params.carIds
        }).then(res=>{
          if(res.code == 200){
            this.Toast({
              message: '围栏添加成功',
              duration: 2000
            });
            this.$router.push({name:'crawlList'})
          }
        }).catch(err=>{})
      },
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
        // this.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件
        // this.map.addControl(new BMap.OverviewMapControl());       //添加缩略地图控件
        this.map.addControl(new BMap.NavigationControl()); 				// 添加平移缩放控件


        this.getMapAddress(v.map.getCenter()).then(res => {
          this.codeMess = res
        }).catch(err => {
        })

        this.addMarkerCrawl(point, this.Range)
        this.addMarkerCode(point)

        this.BmapEvent(['zoomend', 'moving'], v, (res) => {
          v.BmapClear()
          v.crawlCode = res.code
          v.addMarkerCode(new BMap.Point(res.code.lng, res.code.lat))
        })

      },
      BmapEvent(eventType, v, callback) {
        //dragging__拖拽地图过程中触发
        //dragend
        //moving____地图移动过程中触发此事件
        //zoomend___缩放

        let mapMess = {}
        eventType.forEach((it, index) => {
          v.map.addEventListener(it, function () {
            mapMess.zoom = v.map.getZoom()
            mapMess.code = v.map.getCenter()
            callback && callback(mapMess)
          });
        })
      },
      addMarkerCode(point) {//标点
        let marker = new BMap.Marker(point);
        this.map.addOverlay(marker);               // 将标注添加到地图中
        marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
      },
      addMarkerCrawl(point, radii) {//画圆
        let circle = new BMap.Circle(point, radii, {strokeColor: "blue", strokeWeight: 2, strokeOpacity: 0.5}); //创建圆
        this.map.addOverlay(circle);            //增加圆
      },
      getMapAddress(code) {
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
