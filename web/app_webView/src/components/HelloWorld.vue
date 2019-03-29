<template>
  <div class="box_col web">
    <div class="titbox mapAppTitSty">
      <div class="tit box_row">
        <div v-if="carList.length>0" @click="carListShow()"
             class="box_row_100 TITleft">
          <i class="iconfont icon-weibiaoti"></i>
          <span style="font-size: 14px">{{gpsCode.cph}}</span>
        </div>

        <div v-else class="box_row_100 TITleft" @click="addCar" style="position: relative">
          <i class="iconfont icon-add3"></i>
          <span>新增车辆</span>
        </div>
        <div class="box_row_100 TITMess">
          车辆
        </div>
        <div class="box_row_100 TITRight" @click="carMess">
          <i class="iconfont icon-message"></i>
        </div>
      </div>
    </div>

    <div class="carListBoxSty" v-if="carListBoxStyShow" @click="carListBoxStyShow=false">
      <div class="listBox">
        <car-list-b :mess="carList" @itemClick="getCarMess"></car-list-b>
      </div>
    </div>

    <div class="OBDerrList" v-if="errTextListShow">
      <div class="OBDbox box_col">
        <div class="box_col_auto errList">
          <div v-if="!errTextList.errText">
            车辆暂无异常!
          </div>
          <div v-else class="errItem box_row" v-for="(item,index) of errTextList.errText" :key="index">
            <i class="iconfont icon-error6" style="font-size: 26px;color: #ed4014"></i>
            <div class="box_row_100 text">
                {{item}}
            </div>
          </div>
        </div>
        <div style="padding-top: 8px">
          <Button type="info" long size="large" @click="errTextListShow = false">完成</Button>
        </div>
      </div>
    </div>

    <div class="carMess box_row" v-if="carList.length>0">
      <div class="box_row_100 SBType">
        <span style="margin-right: 25px">设备状态：</span>
        <span v-if="gpsCode.zxzt == '00'">
          <i class="iconfont icon-cc-dot" style="color: #47cb89"></i>点火
        </span>
        <span v-else-if="gpsCode.zxzt == '10'">
          <i class="iconfont icon-cc-dot" style="color: #ed4014"></i>熄火
        </span>
        <!--<span v-else-if="gpsCode.zxzt == '20'">-->
          <!--<i class="iconfont icon-cc-dot" style="color: #717171"></i>离线-->
        <!--</span>-->
        <span v-else>
          <i class="iconfont icon-cc-dot" style="color: #717171"></i>离线
        </span>
      </div>
      <div class="box_row_100" style="">
        <Row>
          <Col span="12" align="center">
            <Row>
              <Col span="24">
                <span style="font-size: 18px">
                  <span v-if="gpsCode.durartion && gpsCode.zxzt == '00'">{{parseInt(parseInt(gpsCode.durartion)/60000)}}</span>
                  <span v-else>0</span>
                </span>min
              </Col>
            </Row>
            <Row>
              <Col span="24">行驶时间</Col>
            </Row>
          </Col>
          <Col span="12" align="center">
            <Row>
              <Col span="24">
                <span style="font-size: 18px">
                  <span v-if="gpsCode.speed && gpsCode.zxzt == '00'">{{gpsCode.speed}}</span>
                  <span v-else>0</span>
                </span>km/h
              </Col>
            </Row>
            <Row>
              <Col span="24">当前时速</Col>
            </Row>
          </Col>
        </Row>
      </div>
    </div>
    <div class="box_col_100 mapBox">
      <div class="carEvent">
        <div class="eventComp" @click="carEvent('event','photo')" v-if="gpsCode.zdLx && gpsCode.zdLx=='10'">
          <i class="iconfont icon-iconset0239"></i>
          <div class="text">
            拍照
          </div>
        </div>

        <div class="eventComp" @click="carEvent('event','video')" v-if="gpsCode.zdLx && gpsCode.zdLx=='10'">
          <i class="iconfont icon-luxiang"></i>
          <div class="text">
            视频
          </div>
        </div>

        <div class="eventComp" @click="carEvent('url','file')" v-if="gpsCode.zdLx && gpsCode.zdLx=='10'">
          <i class="iconfont icon-buoumaotubiao37"></i>
          <div class="text">
            文件
          </div>
        </div>

        <div class="eventComp" style="padding-top: 4px" @click="inspect" v-if="gpsCode.zdLx && gpsCode.zdLx=='20'">
          <i class="iconfont icon-weixiu"></i>
          <!--<Icon type="ios-construct-outline" size="28"/>-->
          <div class="text">
            自检
          </div>
        </div>

      </div>
      <div id="allmap" style="height: 100%"></div>
    </div>
  </div>
</template>

<script>
  import {Toast} from 'mint-ui';
  import carListB from './comp/carLIstModal'
  import Stomp from '@stomp/stompjs';
  import SockJS from 'sockjs-client';

  export default {
    name: 'HelloWorld',
    components: {carListB},
    data() {
      return {
        messText:'卡萨浪蝶狂蜂建安龙卷风拉水电',
        errTextListShow:false,
        errTextList:{
          zdbh:'',
          errText:false
        },
        carListBoxStyShow: false,
        map: '',
        gpsCode: {bdjd:116.404,bdwd: 39.915,fxj: 0,zxzt:'10',cph:'',zdbh:''},

        MyGps: {bdjd:116.404,bdwd: 39.915},//初始化定位
        carList: [],//车辆列表
        carItem: null,//车辆列表 选中车辆

        // WecSgps: {//选中车辆的数据
        //   fxj: 0
        // },
        socket: new SockJS('http://119.23.242.234:8080/gps'),
        subscribes: []
      }
    },
    computed:{
    },
    watch: {
      carListBoxStyShow: function (n, o) {
        console.log(n);
      },
      gpsCode:function (n,o) {
        console.log(n.zxzt,'世界真奇妙');
      }
    },
    created() {
      // let a = '101601190101-{"P0123":"节气门/踏板位置传感器/开关A -电路输入电压高","P0013":"凸轮轴位置 - 执行器电路开路 （组1）"}'
      // console.log('666',a.indexOf('{'));
      // console.log(a.substring(0, a.indexOf('{')-1));
      // console.log(JSON.parse(a.substring(a.indexOf('{'), a.indexOf('}')+1)));
      // this.errTextList.errText =JSON.parse(a.substring(a.indexOf('{'), a.indexOf('}')+1))



      localStorage.setItem('UserID', this.getUrlParams('userID'))//从url上获取 userID并存储
      // localStorage.setItem('UserID','524325403838382080')//从url上获取 userID并存储

      let routerType = this.getUrlParams('R_Type')

      if(routerType !== 'home' && routerType !== null && routerType !==undefined ){
        this.$router.push({name:routerType})
        return
      }
      this.getCarList()
    },
    mounted() {
      this.$nextTick(() => {
        setTimeout(() => {
          this.BuildMap()
        }, 200)

        var v = this
        let code = null
        this.getCarList(()=>{
          document.addEventListener( "plusready", function(){
            try {
              plus.geolocation.getCurrentPosition(function(p){
                code = {}
                code.bdjd = p.coords.longitude
                code.bdwd = p.coords.latitude
                if(this.carList.length == 0) {
                  v.gpsCode = v.gps_ZH(code.bdjd, code.bdwd)
                }
                v.MyGps = v.gps_ZH(code.bdjd, code.bdwd)

                v.BmapClear()
                v.addMarker(v.gpsCode.bdjd, v.gpsCode.bdwd)
                v.map.setViewport([new BMap.Point(v.gpsCode.bdjd, v.gpsCode.bdwd)]);
              }, function(e){
                code = '{"bdjd":116.404,"bdwd": 39.915}'
                v.gpsCode = JSON.parse(code)
                v.MyGps = JSON.parse(code)
                v.BmapClear()
                v.addMarker(v.gpsCode.bdjd, v.gpsCode.bdwd)
                v.map.setViewport([new BMap.Point(v.gpsCode.bdjd, v.gpsCode.bdwd)]);
              } );
            }catch (e) {
              code = '{"bdjd":116.404,"bdwd": 39.915}'
              v.gpsCode = JSON.parse(code)
              v.MyGps = JSON.parse(code)
              v.BmapClear()
              v.addMarker(v.gpsCode.bdjd, v.gpsCode.bdwd)
              v.map.setViewport([new BMap.Point(v.gpsCode.bdjd, v.gpsCode.bdwd)]);
            }
          }, false );
        })


      })
    },
    beforeDestroy() {
      try {
        for (let r of this.subscribes) {
          r.unsubscribe();
        }

        this.stompClient.disconnect(function(){});
      } catch (e) {

      }
    },
    methods: {
      sco() {
        //数据推送
        var v = this
        v.socket.onopen = function () {
        };
        v.socket.onmessage = function (e) {
        };
        v.socket.onclose = function () {
        };
        this.stompClient = Stomp.over(v.socket);
        this.stompClient.connect({}, function (frame) {
          v.wsconnect();
        });
      },
      wsconnect() {
        var v = this
        if (!this.carList && this.carList.length == 0) {
          return
        }
        let showCarList = this.carList

        for (let r of showCarList) {
          this.subscribes[r.zdbh] = this.stompClient.subscribe('/topic/sendgps-' + r.zdbh, function (data) { //订阅消息
            if(JSON.parse(data.body).zdbh == v.gpsCode.zdbh){
              // v.WecSgps = JSON.parse(data.body)
              v.gpsCode = JSON.parse(data.body)
              v.BmapClear()
              v.addMarker(v.gpsCode.bdjd, v.gpsCode.bdwd)
              v.map.setViewport([new BMap.Point(v.gpsCode.bdjd, v.gpsCode.bdwd)]);
            }
          });

          this.subscribes[r.zdbh] = this.stompClient.subscribe('/topic/check-' + r.zdbh, function (data) { //订阅消息
            console.log('车辆自检',data);
            let a = '101601190101-{"P0123":"节气门/踏板位置传感器/开关A -电路输入电压高","P0013":"凸轮轴位置 - 执行器电路开路 （组1）"}'
            this.errTextList.zdbh =a.substring(0, a.indexOf('{')-1)
            let b= a.substring(a.indexOf('{'), a.indexOf('}')+1)
            if(b.length == 2){
              this.errTextList.errText =false
            }else {
              this.errTextList.errText =JSON.parse(b)
            }
          });
        }
      },
      carListShow(){
        this.carListBoxStyShow = !this.carListBoxStyShow
      },
      addCar() {
        uni.navigateTo({
          url: '/pages/main/comp/addCar'
        });
      },
      getCarList(callback) {//获取车辆列表
        this.$http.post('/app/device/InitClGps').then(res => {
          if (res.code == 200 && res.result) {
            if (res.result.length > 0) {

              let LcarMess = JSON.parse(localStorage.getItem('carMess'))
              if(LcarMess){
                res.result.forEach((it,index)=>{
                  if(it.zdbh == LcarMess.zdbh){
                    // this.carItem = it
                    this.gpsCode = it
                  }else {
                    if(index == res.result-1){
                      // this.carItem = res.result[0]
                      this.gpsCode = res.result[0]
                      localStorage.setItem('carMess', JSON.stringify(res.result[0]))
                      this.SetSeverMess('carMess',JSON.stringify(item))//网络数据存储
                    }
                  }
                })
              }else {//没有 缓存 取列表第一个
                // this.carItem = res.result[0]
                this.gpsCode = res.result[0]
                localStorage.setItem('carMess', JSON.stringify(res.result[0]))
                this.SetSeverMess('carMess',JSON.stringify(item))//网络数据存储
              }

              this.carList = res.result
              this.sco()
              this.BmapClear()
              this.addMarker(this.gpsCode.bdjd, this.gpsCode.bdwd)
              this.map.setViewport([new BMap.Point(this.gpsCode.bdjd, this.gpsCode.bdwd)]);

            }
          } else {
            localStorage.removeItem('carMess')
          }
          callback && callback()
        }).catch(err => {
        })
      },
      getCarMess(item) {//车辆信息
        console.log(item);
        this.carListBoxStyShow = false
        if (item === 'add') {
          this.addCar()
          return
        }
        // this.carItem = item
        this.gpsCode = item
        localStorage.removeItem('carMess')
        localStorage.setItem('carMess', JSON.stringify(item))
        this.SetSeverMess('carMess',JSON.stringify(item))//网络数据存储
        this.BuildMap()
      },
      SetSeverMess(key,val){
        this.$http.post('app/user/putData',{key:key,val:val}).then(res=>{

        }).catch(err=>{})

      },
      gps_ZH(lng, lat) {
        var x_PI = 3.14159265358979324 * 3000.0 / 180.0;
        var PI = 3.1415926535897932384626;
        var a = 6378245.0;
        var ee = 0.00669342162296594323;

        var z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
        var theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
        var bd_lng = z * Math.cos(theta) + 0.0065;
        var bd_lat = z * Math.sin(theta) + 0.006;
        return {
          bdjd: bd_lng,
          bdwd: bd_lat
        }
      },
      getUrlParams(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        let href = window.location.href;
        var r = href.substr(href.indexOf('?') + 1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;

        // var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        // var r = window.location.search.substr(1).match(reg);
        // if (r != null) return unescape(r[2]);
        // return null;

      },

      carMess() {
        this.$router.push({name: 'carMess', params: {urls: true}})
      },

      carEvent(type, val) {
        if (type == 'event') {

          if (val === 'photo') {
            this.$http.post('/app/device/takePhoto', {deviceId: this.gpsCode.zdbh, cmd: '1-10'}).then(res => {
              if (res.code == 200) {
                Toast({
                  className: 'ToastSty',
                  duration: 3 * 1000,
                  message: '指令下发成功',
                  iconClass: 'iconfont icon-success1f'
                });
              } else {
                Toast({
                  className: 'ToastSty',
                  duration: 1 * 1000,
                  message: res.message
                });
              }
            }).catch(err => {
            })
          } else if (val === 'video') {
            this.$http.post('/app/device/takeVideo', {deviceId: this.gpsCode.zdbh, cmd: '1-10'}).then(res => {
              if (res.code == 200) {
                Toast({
                  className: 'ToastSty',
                  duration: 3 * 1000,
                  message: '指令下发成功',
                  iconClass: 'iconfont icon-success1f'
                });
              } else {
                Toast({
                  className: 'ToastSty',
                  duration: 1 * 1000,
                  message: res.message
                });
              }
            }).catch(err => {
            })
          }
        } else if (type == 'url') {
          this.$router.push({name: val})
          localStorage.setItem('zdbh', this.gpsCode.zdbh)
        }
      },
      inspect(){//车辆自检
          this.$http.post('/app/device/check', {zdbh: this.zdbh}).then(res => {
            if (res.code == 200) {
              this.Toast({
                message: "车辆检测中……",
                duration: 5000
              });
              setTimeout(()=>{
                if(this.gpsCode.zdbh == this.errTextList.zdbh){
                  this.Toast({
                    message: "检测完成",
                    duration: 1500
                  });
                  this.errTextListShow = true
                }else {
                  this.Toast({
                    message: "检测失败,请重试！",
                    duration: 2500
                  });
                }
              },5000)
            } else {
              this.Toast({
                message: '终端不在线！',
                className:'errTost',
                iconClass: 'iconfont icon-error5',
                duration: 3000
              });
            }
          }).catch(err => {})

      },
      BuildMap() {
        var v = this
        if (v.gpsCode && v.gpsCode.bdjd == -1 && v.gpsCode.bdwd == -1) {
          v.gpsCode.bdjd = v.MyGps.bdjd
          v.gpsCode.bdwd = v.MyGps.bdwd
        }
        this.map = new BMap.Map("allmap");    // 创建Map实例
        let point = new BMap.Point(v.gpsCode.bdjd, v.gpsCode.bdwd)
        this.map.centerAndZoom(point, 15);  // 初始化地图,设置中心点坐标和地图级别
        //添加地图类型控件
        this.map.addControl(new BMap.MapTypeControl({
          mapTypes: [
            BMAP_NORMAL_MAP,
            BMAP_HYBRID_MAP
          ]
        }));
        this.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
        this.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件
        this.map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
        this.map.addControl(new BMap.NavigationControl()); 				// 添加平移缩放控件
        if (v.gpsCode.bdjd != -1 && v.gpsCode.bdwd != -1) {
          this.addMarker(v.gpsCode.bdjd, v.gpsCode.bdwd)
        }
      },
      addMarker(bdjd, bdwd) {
        var myIcon = ''
        if (this.carList && this.carList.length > 0) {
          myIcon = new BMap.Icon(this.apis.bdiconing + "/baiduCarMin.png", new BMap.Size(60, 30), {anchor: new BMap.Size(30, 15)});
        } else {
          myIcon = new BMap.Icon(this.apis.bdiconing + "/gps.png", new BMap.Size(30, 32));
        }
        let point = new BMap.Point(bdjd, bdwd)
        var marker = new BMap.Marker(point, {icon: myIcon});  // 创建标注
        if (this.gpsCode.fxj && this.carList && this.carList.length > 0) {
          marker.setRotation(this.gpsCode.fxj - 90)
        } else {
          // marker.setRotation(270)
        }
        this.map.addOverlay(marker);               // 将标注添加到地图中
      },
      BmapClear() {//清除图层
        this.map.clearOverlays();
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less">
  .errTost{
    .icon-error5{
      font-size: 26px!important;
    }
  }
  .web {
    .OBDerrList{
      position: fixed;
      left: 0;
      top: 0;
      right: 0;
      bottom: 0;
      z-index: 2000;
      background-color: #fff0;
      .OBDbox{
        padding: 20px;
        background-color: #fff;
        width: 65%;
        max-height: 240px;
        position: absolute;
        left: 50%;
        top: 50%;
        border: solid 1px #a5a5a5;
        border-radius: 10px;
        transform: translate(-50%,-50%);
        box-shadow: 0px 0px 10px #888888;
        .errList{
          .errItem{
            .text{
              line-height: 38px;
            }
          }
        }
      }
    }
    .carListBoxSty {
      position: fixed;
      left: 0;
      top: 0;
      right: 0;
      bottom: 0;
      z-index: 500;
      background-color: #fff0;
      .listBox {
        padding-top: 92px;
        left: 10px;
        position: absolute;
        z-index: 350;
      }
    }
    .titbox {
      padding-top: 30px;
      background-color: #007AFF;
    }
    .carMess {
      padding: 10px 20px;
      background-color: #fff;
      vertical-align: middle;
      color: #999999;
      .SBType {
        padding: 10px 0;
        font-size: 16px;
        position: relative;
        .icon-cc-dot {
          font-size: 24px;
          color: #52c41a;
          position: absolute;
          left: 50%;
          top: 50%;
          transform: translate(-50%, -53%);
        }
      }
    }

    .mapBox {
      position: relative;
      .carEvent {
        position: absolute;
        z-index: 100;
        right: 0;
        top: 50px;
        padding: 0 12px;
        min-height: 100px;
        .eventComp {
          padding: 0 16px 16px 16px;
          background-color: #fff;
          position: relative;
          border-radius: 5px;
          box-shadow: -2px 4px 5px #888888;
          margin-bottom: 10px;
          i {
            font-size: 28px;
          }
          .text {
            position: absolute;
            bottom: 3px;
            left: 50%;
            transform: translateX(-50%);
          }
        }
      }
    }
  }
</style>
