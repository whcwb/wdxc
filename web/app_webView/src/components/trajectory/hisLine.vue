<template>
  <div class="box_col">
    <div class="titbox mapAppTitSty">
      <div class="tit box_row">
        <div class="box_row_100 TITleft" @click="goback">
          <i class="iconfont icon-jiantouarrowhead7"></i>
        </div>
        <div class="box_row_100 TITMess">
          轨迹详细
        </div>
        <div class="box_row_100 TITRight">
        </div>
      </div>
    </div>
    <div class="box_col_100" style="position: relative">
      <div id="hisMap" style="height: 100%;width: 100%"></div>
    </div>
    <!--<div style="width: 99%;height: 160px;padding-top: 20px">-->
        <!--<div id="trackLineChart" style="width: 99%;height: 140px"></div>-->
    <!--</div>-->
    <div class="box_row" style="text-align: center;padding: 10px 0">
        <div class="box_row_100">
          <div class="playBut" @click="kuaitui">
            <i class="iconfont icon-kuaitui" style="font-size: 24px"></i>
          </div>
        </div>
        <div class="box_row_100">
          <div class="playBut" v-if="playAndStopBtnGroup.play" @click="play">
            <i class="iconfont icon-xiaochengxuzititubiao-" style="font-size: 26px;"></i>
          </div>
          <div class="playBut" v-else @click="stopAnimation">
            <i class="iconfont icon-tingzhi" style="font-size: 26px;"></i>
          </div>
        </div>
        <div class="box_row_100">
          <div class="playBut" @click="kuaijin">
            <i class="iconfont icon-kuaijin" style="font-size: 24px"></i>
          </div>
        </div>
    </div>
  </div>
</template>

<script>
  import echarts from 'echarts'
  import codeList from './code'

  export default {
    name: "hisLine",
    data() {
      return {
        codeList: [],
        stationList:[],
        speedList: [],
        movingLushu: {},
        speeds:[],
        map: '',
        playAndStopBtnGroup: {
          //默认动画间隔时长，单位：秒
          timer: 1000,
          //默认动画播放位置和stationList数据对象同步
          playIndex: 0,
          //动画对象
          playTimer: -1,
          //播放按钮是否显示
          play: true,
          //停止按钮是否显示
          stop: false,
          speed: 20
        },
        params: null
      }
    },
    created() {
      this.params = JSON.parse(localStorage.getItem('params'))
      console.log('阐述',localStorage.getItem('params'));
      console.log('阐述',this.params);
      this.getLineCode()

    },
    mounted() {
      this.$nextTick(() => {
        var v = this
        document.addEventListener( "plusready", function(){
          try {
            plus.key.addEventListener('backbutton',function(){
              console.log('back');
              v.$router.back()
            },false);
          }catch (e) {}
        }, false );

        setTimeout(()=>{
          this.BuildMap()
        },500)
      })
    },
    methods: {
      getLineCode() {//获取轨迹数据
        if(this.params == null){
          return
        }
        this.$http.post('/app/device/yyguiji', this.params).then(res => {
          console.log('轨迹数据', res);
          if(res.code == 200){
            this.codeList = res.result
          }
        }).catch(err => {})
      },

      getBdData() {
        this.stationList = this.codeList;
        var point = new BMap.Point(
          this.stationList[0].longitude,
          this.stationList[0].latitude
        );
        // this.map.panTo(point, 15);
        for (let r of this.stationList) {
          let date = new Date(r.loc_time);
          let speed = parseInt(r.speed);
          this.speedList.push([r.loc_time, speed]);
          this.speeds[date.getTime()] = speed;
        }

        this.line();
      },
      goback() {
        this.$router.back()
      },
      BuildMap() {
        let v = this;
        // 百度地图API功能
        this.map = new BMap.Map("hisMap"); // 创建Map实例
        //添加地图类型控件
        this.map.addControl(new BMap.MapTypeControl({
          mapTypes: [
            BMAP_NORMAL_MAP
          ]
        }));
        this.map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
        this.map.addControl(new BMap.ScaleControl()); // 添加比例尺控件
        this.map.addControl(new BMap.OverviewMapControl()); //添加缩略地图控件
        this.map.addControl(new BMap.NavigationControl()); // 添加平移缩放控件
        this.map.centerAndZoom(new BMap.Point(this.codeList[0].longitude, this.codeList[0].latitude), this.zoom); // 初始化地图,设置中心点坐标和地图级别

        // setTimeout(() => {
        // v.line();
        // }, 100)

        this.getBdData();
      },
      line() {
        let v = this;
        var pois = [];
        for (let r of v.stationList) {
          pois.push(new BMap.Point(r.longitude, r.latitude));
        }
        v.map.setViewport(pois);
        var polyline = new BMap.Polyline(pois, {
          enableEditing: false, //是否启用线编辑，默认为false
          enableClicking: false, //是否响应点击事件，默认为true
          // icons:[icons],
          strokeWeight: "8", //折线的宽度，以像素为单位
          strokeOpacity: 0.8, //折线的透明度，取值范围0 - 1
          strokeColor: "#18a45b" //折线颜色
        });
        this.map.addOverlay(polyline); //增加折线

        // 增加起点
        var pt1 = new BMap.Point(
          v.stationList[0].longitude,
          v.stationList[0].latitude
        );
        var myIcon1 = new BMap.Icon(
          "http://47.98.39.45:9092/icon/map_line_begin.png",
          new BMap.Size(37, 62),
          { anchor: new BMap.Size(19, 62) }
        );
        var marker1 = new BMap.Marker(pt1, { icon: myIcon1 }); // 创建标注
        this.map.addOverlay(marker1);

        //初始化动画marker对象
        var moveIcon = new BMap.Icon(
          "http://47.98.39.45:9092/icon/ic_car_online.png",
          new BMap.Size(32, 32),
          { anchor: new BMap.Size(16, 32) }
        );
        this.movingMarker = new BMap.Marker(pt1, { icon: moveIcon });
        // this.map.addOverlay(this.movingMarker);

        // 增加终点
        var pt2 = new BMap.Point(
          v.stationList[v.stationList.length - 1].longitude,
          v.stationList[v.stationList.length - 1].latitude
        );
        var myIcon2 = new BMap.Icon(
          "http://47.98.39.45:9092/icon/map_line_end.png",
          new BMap.Size(37, 62),
          { anchor: new BMap.Size(19, 62) }
        );
        var marker2 = new BMap.Marker(pt2, { icon: myIcon2 }); // 创建标注
        this.map.addOverlay(marker2);
        //画轨迹线

        var util = require('./LuShu.js');
        this.movingLushu = new BMapLib.LuShu(this.map, pois, {
          autoView: true, //是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
          // icon: new BMap.Icon(this.apis.filePath+"/icon/ic_car_online.png", new BMap.Size(32,32), {anchor: new BMap.Size(16,32),}),
          icon: new BMap.Icon(
            "http://47.98.39.45:9092/icon/ic_car_online.png",
            new BMap.Size(32, 32),
            { anchor: new BMap.Size(16, 32) }
          ),
          speed: this.playAndStopBtnGroup.speed
        });
      },
      kuaitui() {
        this.playAndStopBtnGroup.speed -= 20;
        if (this.movingLushu != null) {
          this.movingLushu._setOptions({
            speed: this.playAndStopBtnGroup.speed
          });
          this.movingLushu.pause();
          this.movingLushu.start();
        }
      },
      kuaijin() {
        this.playAndStopBtnGroup.speed += 20;
        if (this.movingLushu != null) {
          this.movingLushu._setOptions({
            speed: this.playAndStopBtnGroup.speed
          });
          this.movingLushu.pause();
          this.movingLushu.start();
        }
      },
      play() {
        if (this.movingLushu == null) {
          return;
        }
        this.playAndStopBtnGroup.speed = 20;
        this.movingLushu.start();
        this.animationDot();
      },
      animationDot() {
          this.playAndStopBtnGroup.play = false;
          this.playAndStopBtnGroup.stop = true;
          if (this.movingMarker != null) {
            this.playAndStopBtnGroup.playIndex++;
            if (this.playAndStopBtnGroup.playIndex == this.codeList.length) {
              //循环执行完成后，重置播放轨迹点
              this.playAndStopBtnGroup.playIndex = 0;
              this.playAndStopBtnGroup.timer = 1000;
              this.playAndStopBtnGroup.speed = 20;
              //重新开始
              this.stopAnimation();
              return;
            }
            try {
              //取出下一个动画节点
              var moveData = this.stationList[this.playAndStopBtnGroup.playIndex];
              //更新地图移动轨迹
              var movePoint = new BMap.Point(moveData.longitude, moveData.latitude);
              this.movingMarker.setPosition(movePoint);

              //给chart补充完数据后，再开启该方法
              this.movingChartOptions.animation = false;
              //更新chart移动轨迹
              // this.movingChartOptions.xAxis.axisPointer.value = this.speedList[this.playAndStopBtnGroup.playIndex][0];
              this.movingChartOptions.xAxis.axisPointer.value = this.speedList[
                this.playAndStopBtnGroup.playIndex][0];
              //重置chart属性来实现自动移动
              this.movingChart.setOption(this.movingChartOptions);

            } catch (e) {
              //防止动画执行报错，进行容错控制，防止界面卡死
            }
            //循环执行，直到循环完数据
            this.playAndStopBtnGroup.playTimer = setTimeout(() => {
              this.animationDot();
            }, this.playAndStopBtnGroup.timer);
          }
      },
      stop() {
        if (this.movingLushu != null) {
          this.movingLushu.stop();
          this.playAndStopBtnGroup.playIndex = 0;
          this.playAndStopBtnGroup.timer = 1000;
          this.playAndStopBtnGroup.speed = 20;
        }
      },
      stopAnimation() {
        this.stop();
        this.playAndStopBtnGroup.play = true;
        this.playAndStopBtnGroup.stop = false;
        if (this.playAndStopBtnGroup.playTimer != -1) {
          clearTimeout(this.playAndStopBtnGroup.playTimer);
        }
      }
    }
  }
</script>

<style lang="less">
.playBut{
  background-color: #2b85e4;
  width: 35px;height: 35px;
  margin: auto;border-radius: 35px;
  position: relative;
  i{
    color: #fff;
    position: absolute;
    left: 0px;
    top: 0px;
    transform: translate(5px,-2px);
  }
}
</style>
