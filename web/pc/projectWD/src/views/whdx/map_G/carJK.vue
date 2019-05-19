<!--
车辆监控

-->
<style type="text/css">
      #allmap {
            height: 100%;
            width: 100%;
      }

      #tcBtn {
            bottom: auto !important;
            top: 8px !important;
            left: 23px !important;
            right: auto !important;
            z-index: 9999;
      }

      #tcWrap {
            display: none;
            bottom: auto !important;
            top: 435px !important;
            left: 20px !important;
            right: auto !important;
            z-index: 9999;
      }
</style>
<!--地图选点-->
<template>
      <div style="height: 100%">
            <!--<car-info ref="carInfoButton"></car-info>-->
            <div id="allmap"></div>
      </div>
</template>

<script>

    // import carInfo from '../OperationMonitoring/VehicleMonitoring/carInfo'

    export default {
        name: 'getmapdot',
        // components: {
        //     carInfo
        // },
        data() {
            return {
                componentName: '',
                choosedItem: null,
                map: '',
                mapcenter: {
                    lng: 114.3712668464,
                    lat: 30.5440310131
                },
                zoom: 16,
                carList: [],
                zoomDot: [],
                fancePoints: [
                    {lng: 114.27226, lat: 30.608123},
                    {lng: 114.157277, lat: 30.544446},
                    {lng: 114.418288, lat: 30.526529},
                ],

                MarkerList: []
            }
        },
        created() {
        },
        mounted() {
            this.Buildmap()
            this.init();
        },
        methods: {
            showFance(carId) {
                this.fancePoints = [];
                var v = this
                this.$http.get(this.apis.DZWL.GET_BY_CAR_ID + "?clId=" + carId).then((res) => {
                    if (res.code === 200) {
                        let s = res.result.dlxxzb;
                        let ps = s.split(";");
                        for (let r of ps) {
                            let point = r.split(",");
                            this.fancePoints.push({lng: point[1], lat: point[0]})
                        }
                        this.addArea(this.fancePoints);
                    }
                })
            },
            showPath(carId) {
                this.addLine(this.fancePoints);
            },
            update() {
                var v = this
                v.carList = v.$parent.mapCarList;
                v.moveMap();
                v.showCarPosition();
            },
            moveMap() {
                if (this.carList.length == 0) return;
                var v = this
                if (this.carList.length == 1) {
                    this.map.setCenter({lat: parseFloat(this.carList[0].lat), lng: parseFloat(this.carList[0].lng)});
                    this.map.setZoom(this.map.getZoom());
                }
            },
            init() {
                this.carList = this.$parent.mapCarList;
                if (this.carList.length === 1) {
                    this.car = this.carList[0];
                    this.chooseCar(this.car);
                } else if (this.carList.length > 0) {
                    this.car = null;
                    this.map.setCenter({lat: parseFloat(this.mapcenter.lat), lng: parseFloat(this.mapcenter.lng)});
                    this.map.setZoom(this.map.getZoom());
                } else {
                    this.car = null;
                }
                this.moveMap();
                this.showCarPosition();
            },
            Buildmap() {
                // 百度地图API功能
                const v = this
                this.map = new google.maps.Map(document.getElementById('allmap'), {
                    // center: {lat: -34.397, lng: 150.644},
                    center: {lat: v.mapcenter.lat, lng: v.mapcenter.lng},
                    zoom: v.zoom,
                    zoomControl: false,
                    streetViewControl: false,//街景小人
                    fullscreenControl: false,//全屏控件
                    mapTypeControl: false,//地图类型控件
                });
            },
            //撒点
            showCarPosition() {
                console.log('****MarkerList',this.MarkerList.length);
                this.clear()
                var v = this
                console.log('#$%^&*()', this.carList.length);

                for (let r of this.carList) {
                    var point = {lat: r.lat, lng: r.lng};
                    this.addMarker(r, point);
                    // this.addLabel(r, point);
                }
            },
            addLabel(item, point) {
                let html = '<div style="width: 160px;height: 28px;padding:4px;">' +
                    '<span>[' + item.cph + ']</span> ' +
                    '<span style="float: right">' + item.speed + ' km/h</span>' +
                    '</div>'
                var myLabel = new BMap.Label(html,     //为lable填写内容
                    {
                        offset: new BMap.Size(-80, -70),                  //label的偏移量，为了让label的中心显示在点上
                        position: point
                    });                                //label的位置
                myLabel.setStyle({                                   //给label设置样式，任意的CSS都是可以的
                    // color:"red",                   //颜色
                    fontSize: "16px",               //字号
                    // opacity:0.5,
                    'background-color': 'rgba(255,255,255,0.6)',
                    // border:"none",                    //边
                    'border-radius': '4px',
                    // height:"120px",                //高度
                    // width:"125px",                 //宽
                    // textAlign:"center",            //文字水平居中显示
                    // lineHeight:"120px",            //行高，文字垂直居中显示
                    // background:"url(http://cdn1.iconfinder.com/data/icons/CrystalClear/128x128/actions/gohome.png)",    //背景图片，这是房产标注的关键！
                    // cursor:"pointer"
                });
                myLabel.setTitle("");               //为label添加鼠标提示
                this.map.addOverlay(myLabel);
            },
            addLine(points) {
                let ps = [];
                // console.log("百度经纬度 : " , points);
                for (let r of points) {
                    ps.push(new BMap.Point(r.bdjd, r.bdwd))
                }
                // console.log("ps " , ps);
                var polygon = new BMap.Polyline(ps, {strokeColor: "red", strokeWeight: 5, strokeOpacity: 0.5});  //创建多边形

                this.map.addOverlay(polygon);
            },
            addArea(points) {
                let ps = [];
                for (let r of points) {
                    ps.push(new BMap.Point(r.lng, r.lat))
                }
                var polygon = new BMap.Polygon(ps, {strokeColor: "red", strokeWeight: 2, strokeOpacity: 0.5});  //创建多边形
                this.map.addOverlay(polygon);
            },
            addMarker(item, point) {
                var v = this

                var image = {
                    // url: 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png',
                    url: this.getIcon(item),
                    size: new google.maps.Size(32, 32),
                    origin: new google.maps.Point(0, 0),
                    anchor: new google.maps.Point(16, 32)
                };
                var marker = new google.maps.Marker({
                    position: {lat: parseFloat(point.lat), lng: parseFloat(point.lng)},
                    map: this.map,
                    icon: image
                });

                this.MarkerList.push(marker)

                console.log('MarkerList',this.MarkerList.length);


                // marker.addEventListener("click",(code)=>{
                //     v.$emit('codeEvent',item)
                //     v.addLabel(item, point);
                // });
                // this.map.addOverlay(marker);
                // this.addClickHandler(item, marker);
            },
            getIcon(car) {
                switch (car.status) {
                    case 1:
                        return this.apis.STATIC_PATH + 'icon/running.png';
                    case 2:
                        return this.apis.STATIC_PATH + 'icon/ic_car.png';
                    default:
                        return this.apis.STATIC_PATH + 'icon/ic_car_offline.png'
                }
            },
            addClickHandler(item, marker) {
                var v = this
                marker.addEventListener("click", function (e) {
                    v.choosedItem = item;
                })
            },
            chooseCar(item) {
            },
            clear() {
                this.MarkerList.forEach((it, index) => {
                    it.setMap(null)
                })
                this.MarkerList = []
                // this.map.clearOverlays();//清楚数据点
                // this.map.historicalOverlay.setMap(null);
            }
        }
    }
</script>
