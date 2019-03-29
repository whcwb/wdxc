<!--
校巴监控
运营监控-校巴监控-地图展示

-->


<style type="text/css">
    #allmap {
        height: 100%;
        width: 100%;
    }

    .claer {
        position: absolute;
        width: 64px;
        height: 47px;
        top: 5px;
        right: 57px;
        z-index: 100;
        background-color: rgb(255, 255, 255);
        text-align: center;
        line-height: 45px;
        border: solid 1px #696666;
        border-radius: 7px;
    }
</style>

<template>
    <div style="height: 100%;position: relative;">
                    <span>
                        <CheckboxGroup v-model="choosedLineIndexs" @on-change="lineChange">
                            <Checkbox v-for="(item,index) in lineList" :label="index">{{item.xlmc}}</Checkbox>
                        </CheckboxGroup>
                    </span>
        <div id="allmap"></div>
        <!--<div class="claer">
             <Button type="success" size="small" @click="clear">清除</Button>
        </div>-->
    </div>
</template>

<script>
    import $ from 'jquery'

    export default {
        name: '',
        data() {
            return {
                map: '',
                mapcenter: {
                    lng: 114.368383,
                    lat: 30.551134
                },
                lineList: [],
                choosedLineIndexs: [],
                zoom: 16,
                zoomDot: [],
                scoketMess: [],
                allDeviceList: [],
                stationIconUrl: this.apis.STATIC_PATH+'icon/running.png',
                colors: ['#4876FF', '#FF0000', '#5CACEE', '#EE00EE', '#00BFFF', '#BF3EFF'],
                colorIndex: 0,
                yxjlList:[]
            }
        },
        created() {
        },
        mounted() {
            //点布控
            var v = this
            // 百度地图API功能
            this.map = new BMap.Map("allmap");    // 创建Map实例
            this.mapCenterF()
            this.getAllDevice()
            this.getLineList()
        },
        methods: {
            initData(){
                this.$http.get(this.apis.clyxjl.QUERY,{params:{pageSize:100}}).then((res)=>{
                    if (res.code == 200 && res.page.list){
                        this.yxjlList = res.page.list;
                        for(let r of this.yxjlList){
                            for (let l of this.lineList){
                                if (l.id == r.xlId){
                                    let item = {
                                        cph:r.cphm,
                                        bdjd:r.jd,
                                        bdwd:r.wd,
                                        stationNumber:r.zdbh,
                                        zxzt:r.zt == 'off' ? 20 : 30,
                                        zdbh:r.szdbh
                                    };
                                    if (l.carList){
                                        l.carList.push(item)
                                    }else{
                                        l.carList = [item]
                                    }
                                }
                            }
                        }
                        this.showAllCars();
                    }
                })
            },
            showRoute(line) {
                // 显示线路
                this.showLine(line);
                // 显示站点
                this.showStations(line);
                // 显示车辆
                this.showCars(line);
            },
            showLine(line) {
                if (!line.points) return;
                let ps = [];
                for (let r of line.points) {
                    ps.push(new BMap.Point(r.lng, r.lat));
                }
                var polyline = new BMap.Polyline(ps,
                    {strokeColor: line.color, strokeWeight: 6, strokeOpacity: 0.9}
                );
                this.map.addOverlay(polyline);
            },
            getColor() {
                if (this.colorIndex >= this.colors.length) {
                    this.colorIndex = 0;
                }
                return this.colors[this.colorIndex++];
            },
            showStations(line) {
                let stationList = line.stationList;
                if (!stationList) return;
                let c = 0;
                for (let r of stationList) {
                    this.addStation(r, ++c);
                }
            },
            showAllCars() {
                for (let r of this.lineList) {
                    this.showCars(r);
                }
            },
            showCars(line) {
                let carList = line.carList;
                if (!carList) return;
                let c = 0;
                for (let r of carList) {
                    this.addCar(r);
                }
            },
            // 获取线路列表
            getLineList() {
                var v = this
                this.$http.post(this.apis.XL.QUERY, {'lx': 30, pageSize: 1000}).then((res) => {
                    if (res.code == 200 && res.page.list) {
                        this.lineList = res.page.list;
                        for (let i in this.lineList) {
                            this.lineList[i].color = this.getColor();
                            this.getLineStations(i);
                        }
                        this.initData();
                    }
                })
            },
            // 获取线路上的站点
            getLineStations(i) {
                var v = this
                this.lineList[i].stationList = [];
                this.$http.post(this.apis.ZD.GET_BY_ROUTE_ID, {'xlId': this.lineList[i].id}).then((res) => {
                    if (res.code == 200 && res.result) {
                        this.lineList[i].stationList = res.result;
                        this.getLinePoints(i);
                    }
                })
            },
            // 获取一条线路的途径点
            getLinePoints(index) {
                let line = this.lineList[index];
                if (!line || !line.stationList) {
                    return;
                }
                let stationList = line.stationList;
                let startPoint = new BMap.Point(stationList[0].wd, stationList[0].jd);
                let endPoint = new BMap.Point(stationList[stationList.length - 1].wd, stationList[stationList.length - 1].jd);
                let waypoints = '';
                for (let i = 1; i <= stationList.length - 2; i++) {
                    let station = stationList[i];
                    waypoints += station.wd + ',' + station.jd;
                    if (i < stationList.length - 2) {
                        waypoints += '|';
                    }
                }
                let url = 'http://api.map.baidu.com/direction/v2/driving?origin=' + stationList[0].wd + ',' + stationList[0].jd + '&destination=' + stationList[stationList.length - 1].wd + ',' + stationList[stationList.length - 1].jd + '&ak=evDHwrRoILvlkrvaZEFiGp30';
                url += '&waypoints=' + waypoints;
                let points = [];
                let v = this;
                $.ajax({
                    url: url,
                    type: "get",
                    dataType: 'JSONP',
                    success: function (res) {
                        if (res.status == 0) {
                            let route = res.result.routes[0];
                            points.push({lat: route.origin.lat, lng: route.origin.lng});
                            for (let step of route.steps) {
                                points.push({lng: step.start_location.lng, lat: step.start_location.lat});
                                let paths = step.path.split(";");
                                for (let path of paths) {
                                    if (path === '') continue
                                    let point = path.split(",");
                                    points.push({lng: point[0], lat: point[1]});
                                }
                                points.push({lng: step.end_location.lng, lat: step.end_location.lat});
                            }
                            line.points = points;
                        }
                    }
                })
            },
            // 线路复选框选中值发生变化时触发此事件
            lineChange(e) {
                this.map.clearOverlays();
                if (e) {
                    this.choosedLineIndexs = e;
                }
                if (!e || e.length == 0) {
                    this.showAllCars();
                    return;
                }
                for (let i of this.choosedLineIndexs) {
                    this.showRoute(this.lineList[i]);
                }
            },
            // 获取设备信息
            getAllDevice() {
                this.$http.get(this.apis.ZDGL.QUERY + '?pageSize=10000').then((res) => {
                    if (res.code == 200 && res.page.list) {
                        this.addDeviceList = res.page.list;
                        this.checkWebsocket();
                    }
                })
            },
            checkWebsocket() {
                this.websocketUtil.onConnected(()=>this.subscribe())
            },
            //订阅消息
            subscribe() {
                for (let r of v.addDeviceList) {
                    this.websocketUtil.subscribe('/topic/sendgps-' + r.zdbh, function (data) { //订阅消息
                        let weksocketBody = JSON.parse(data.body)
                        if (weksocketBody.cx === "30") {//校巴
                            let xlId = weksocketBody.xlId;
                            v.lineList.forEach((item, index) => {
                                if (item.id === xlId) {
                                    if (!item.carList) {
                                        item.carList = [weksocketBody];
                                    } else {
                                        let index = -1;
                                        for (let i in item.carList) {
                                            if (item.carList[i].zdbh === weksocketBody.zdbh) {
                                                index = i
                                                break;
                                            }
                                        }
                                        if (index >= 0) {
                                            item.carList.splice(index, 1, weksocketBody);
                                        } else {
                                            item.carList.push(weksocketBody);
                                        }
                                    }
                                }
                            })
                            v.lineChange(v.choosedLineIndexs);
                        }
                    });
                }
            },
            // 站点详情
            addStation(item, i) {
                var myIcon = new BMap.Icon(this.getIcon(item), new BMap.Size(32, 32), {anchor: new BMap.Size(16, 32)});
                var marker = new BMap.Marker(new BMap.Point(item.jd, item.wd));
                marker.setLabel(this.getNumberLabel(i));
                this.map.addOverlay(marker);

                let html = '<div style="width: 120px;height: 28px;padding:4px;text-align: center">' +
                    '<span>' + item.mc +'</span> ' +
                    '</div>';
                let point = new BMap.Point(item.jd, item.wd);
                var myLabel = new BMap.Label(html,     //为lable填写内容
                    {
                        offset: new BMap.Size(-80, -70),                  //label的偏移量，为了让label的中心显示在点上
                        position: point
                    });                                //label的位置
                myLabel.setStyle({                                   //给label设置样式，任意的CSS都是可以的
                    fontSize: "16px",               //字号
                    'background-color': 'rgba(255,255,255,0.6)',
                    'border-radius': '4px',
                });
                myLabel.setTitle("我是文本标注label");               //为label添加鼠标提示
                this.map.addOverlay(myLabel);
            },
            addCar(item) {
                var myIcon = new BMap.Icon(this.getIcon(item), new BMap.Size(32, 32), {anchor: new BMap.Size(16, 32)});
                var marker = new BMap.Marker(new BMap.Point(item.bdjd, item.bdwd), {icon: myIcon});
                this.map.addOverlay(marker);
                let html = '<div style="width: 120px;height: 28px;padding:4px;text-align: center">' +
                    '<span>' + item.cph + '</span> ' +
                    '</div>';
                let point = new BMap.Point(item.bdjd, item.bdwd);
                var myLabel = new BMap.Label(html,     //为lable填写内容
                    {
                        offset: new BMap.Size(-65, -70),                  //label的偏移量，为了让label的中心显示在点上
                        position: point
                    });                                //label的位置
                myLabel.setStyle({                                   //给label设置样式，任意的CSS都是可以的
                    // color:"red",                   //颜色
                    fontSize: "16px",               //字号
                    'background-color': 'rgba(255,255,255,0.6)',
                    'border-radius': '4px',
                });
                myLabel.setTitle(item.cph);               //为label添加鼠标提示
                this.map.addOverlay(myLabel);
            },
            getNumberLabel(number) {
                var offsetSize = new BMap.Size(0, 0);
                var labelStyle = {
                    color: "#fff",
                    backgroundColor: "0.05",
                    border: "0"
                };

                //不同数字长度需要设置不同的样式。
                switch ((number + '').length) {
                    case 1:
                        labelStyle.fontSize = "14px";
                        offsetSize = new BMap.Size(4, 2);
                        break;
                    case 2:
                        labelStyle.fontSize = "12px";
                        offsetSize = new BMap.Size(2, 4);
                        break;
                    case 3:
                        labelStyle.fontSize = "10px";
                        offsetSize = new BMap.Size(-2, 4);
                        break;
                    default:
                        break;
                }

                var label = new BMap.Label(number, {
                    offset: offsetSize
                });
                label.setStyle(labelStyle);
                return label;
            },
            getIcon(car) {
                switch (car.zxzt) {
                    case 20:
                        return this.apis.STATIC_PATH+'icon/ic_car_offline.png';
                    case 10:
                        return this.apis.STATIC_PATH+'icon/ic_car.png';
                    default:
                        return this.apis.STATIC_PATH+'icon/running.png'
                }
            },
            //地图级别中心
            mapCenterF() {
                var v = this
                var point = new BMap.Point(v.mapcenter.lng, v.mapcenter.lat);
                this.map.centerAndZoom(point, v.zoom);// 初始化地图,设置中心点坐标和地图级别
                this.map.addControl(new BMap.MapTypeControl({
                    mapTypes: [
                        BMAP_NORMAL_MAP
                    ]
                }));
                //添加地图类型控件
                this.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
                this.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件
                this.map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
                this.map.addControl(new BMap.NavigationControl()); 				// 添加平移缩放控件
            },
            //清除层
            clear() {
                this.map.clearOverlays()
            },
        }
    }
</script>

<style>
</style>
