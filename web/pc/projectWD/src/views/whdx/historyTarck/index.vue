<style lang="less">
      @import "../../../styles/common.less";

      .carNumber {
            .select {
                  text-align: center;
                  .ivu-select-selection {
                        background-color: #262743;
                        color: #fff;
                        .ivu-select-input {
                              color: #fff;
                        }
                  ;
                  }
            ;
                  .ivu-select-dropdown {
                        background-color: #262743;
                        ul {
                              li {
                                    color: #fff;
                                    border: solid #fff 1px;
                                    border-bottom: none;
                              }
                              li:hover {
                                    color: #262743;
                                    border: solid #262743 1px;
                              }
                              li:last-child {
                                    border-bottom: solid #fff 1px;
                              }
                        }
                        .ivu-select-item-selected, .ivu-select-item-selected:hover {
                              background-color: #8397ace6;
                        }
                  }
                  input {
                        text-align: center;
                  }
            }
      }

      .sTime {
            input {
                  text-align: center;
                  background-color: #262743;
                  color: #fff;
            }
      }

      .carMessH {
            text-align: center;
            .tit {
                  color: #9b9b9b;
                  font-size: 16px;
                  font-weight: 600;
            }
            .mess {
                  color: #fff;
                  font-size: 16px;
            }

      }

      .pageListCarH {
            background-color: #303250;
            border: solid 1px #303250;
            padding: 4px;
            color: #fff;
            margin-top: 6px;
            .pageMessH {
                  font-size: 14px;
                  margin-left: 4px;
            }
      }

      .pageListCarH:hover {
            border: solid 1px #fff;
      }

      .choosed {
            border: 1px solid white;
            background-color: #929292;
      }
</style>
<template>
      <div class="box-row" style="background-color: #fff">
            <div class="body-F">
                  <Row>
                        <Col span="24">
                              <div v-show="showMap" id="allmap" style="width: 100%;height: 500px;"></div>
                              <div v-show="!showMap"
                                   style="width: 100%;height: 500px;text-align: center;padding-top: 30%"><h1>
                                    暂无轨迹信息......</h1></div>
                        </Col>
                  </Row>
                  <Row v-if="showMap">
                        <Col span="2">
                              <ButtonGroup vertical style="margin-top: 120px">
                                    <Button type="primary" shape="circle" icon="md-play" size="large"
                                            @click="animationDot" v-show="playAndStopBtnGroup.play"></Button>
                                    <Button type="error" shape="circle" icon="ios-square" size="large"
                                            @click="stopAnimation" v-show="playAndStopBtnGroup.stop"></Button>
                                    <Button type="warning" shape="circle" icon="md-skip-forward" size="large"
                                            @click="playAndStopBtnGroup.timer = 200"></Button>
                                    <Button type="warning" shape="circle" icon="md-skip-backward" size="large"
                                            @click="playAndStopBtnGroup.timer = 1000"></Button>
                              </ButtonGroup>
                        </Col>
                        <Col span="22" v-if="showMap">
                              <div id="trackLineChart" style="width: 90%;height: 400px;"></div>
                        </Col>
                  </Row>
            </div>
            <div style="width: 260px;background-color: #262743">
                  <div class="box">
                        <div style="padding: 8px">
                              <div class="carNumber">
                                    <Select class="select" v-model="formItem.zdbh" filterable>
                                          <Option v-for="item in carList" :value="item.zdbh">{{item.cph}}</Option>
                                    </Select>
                              </div>
                              <div class="sTime" style="margin-top: 6px">
                                    <DatePicker v-model="timeRange"
                                                format="yyyy/MM/dd"
                                                type="daterange"
                                                placement="bottom-end"
                                                placeholder="请选择时间段" style="width:100%"></DatePicker>
                              </div>
                              <div style="margin-top: 6px;text-align: center">
                                    <Button type="info"
                                            size="small"
                                            style="width: 60px" @click="formItemList">查询
                                    </Button>
                              </div>
                              <div class="carMessH" style="margin-top: 6px">
                                    <div class="box-row-nh">
                                          <!--<div class="body-1" style="margin: 4px;">-->
                                          <!--<div class="tit">-->
                                          <!--行驶里程(km)-->
                                          <!--</div>-->
                                          <!--<div class="mess">-->
                                          <!--200-->
                                          <!--</div>-->
                                          <!--</div>-->
                                          <div class="body-1" style="margin: 4px;">
                                                <div class="tit">
                                                      驾驶时长(min)
                                                </div>
                                                <div class="mess">
                                                      {{getMinute(totalTime) | GLmess}}
                                                </div>
                                          </div>
                                          <div class="body-1" style="margin: 4px;">
                                                <div class="tit">
                                                      驾驶次数
                                                </div>
                                                <div class="mess">
                                                      {{pathList.length | GLmess}}
                                                </div>
                                          </div>
                                          <!--<div class="body-1" style="margin: 4px">-->
                                          <!--<div class="tit">-->
                                          <!--最高时速(km/h)-->
                                          <!--</div>-->
                                          <!--<div class="mess">-->
                                          <!--150-->
                                          <!--</div>-->
                                          <!--</div>-->
                                    </div>
                              </div>
                        </div>
                        <div class="body" style="padding: 8px;margin-top: 8px">
                              <div class="pageListCarH" v-for="(item,index) in pathList" @click="itemClick(item,index)"
                                   :class="{'choosed':choosedIndex == index}">
                                    <div>
                                          <Icon type="ios-location"
                                                color="#00c3c1" size="22"></Icon>
                                          <span class="pageMessH">
								{{item.startAddress}}——{{item.endAddress}}
							</span>
                                    </div>
                                    <div>
                                          <Icon type="ios-clock"
                                                color="#2fa2d7" size="20"></Icon>
                                          <span class="pageMessH">
								{{item.kssj}}
							</span><br>
                                          <Icon type="ios-clock"
                                                color="#2fa2d7" size="20"></Icon>
                                          <span class="pageMessH">
								{{item.jssj}}
							</span>
                                    </div>
                                    <div class="box-row-nh">
                                          <!--<div class="body-1" style="margin: 4px;">-->
                                          <!--<div class="tit">-->
                                          <!--里程-->
                                          <!--</div>-->
                                          <!--<div class="mess">-->
                                          <!--200-->
                                          <!--</div>-->
                                          <!--</div>-->
                                          <div class="body-1" style="margin: 4px;">
                                                <div class="tit">
                                                      耗时 {{dateFormat(item.sc)}}
                                                </div>
                                                <!--<div class="mess">-->

                                                <!--</div>-->
                                          </div>
                                          <!--<div class="body-1" style="margin: 4px;">-->
                                          <!--<div class="tit">-->
                                          <!--油耗-->
                                          <!--</div>-->
                                          <!--<div class="mess">-->
                                          <!--200-->
                                          <!--</div>-->
                                          <!--</div>-->
                                          <!--<div class="body-1" style="margin: 4px;">-->
                                          <!--<div class="tit">-->
                                          <!--最高时速-->
                                          <!--</div>-->
                                          <!--<div class="mess">-->
                                          <!--3600-->
                                          <!--</div>-->
                                          <!--</div>-->
                                    </div>
                              </div>
                        </div>
                  </div>
            </div>
      </div>
</template>

<script>
    import bkshow from '../map/mapBK_show'

    import echarts from 'echarts'
    import mixins from '@/mixins'

    Date.prototype.format = function (format) {
        var o = {
            "M+": this.getMonth() + 1, //month
            "d+": this.getDate(),    //day
            "h+": this.getHours(),   //hour
            "H+": this.getHours(),   //hour
            "m+": this.getMinutes(), //minute
            "s+": this.getSeconds(), //second
            "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
            "S": this.getMilliseconds() //millisecond
        }
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length == 1 ? o[k] :
                    ("00" + o[k]).substr(("" + o[k]).length));
        return format;
    }
    export default {
        name: 'historyTarck',
        mixins: [mixins],
        filters: {
            GLmess(val) {
                if (val) return val
                return '0'
            }
        },
        components: {
            bkshow
        },
        watch: {
            timeRange: function (newQuestion, oldQuestion) {
                if (typeof newQuestion[0] === 'string') {
                    return;
                }
                if (newQuestion.length > 0 && newQuestion[0] != '') {
                    this.formItem.startTime = this.getdateParaD(newQuestion[0]) + " 00:00:00";
                    this.formItem.endTime = this.getdateParaD(newQuestion[1]) + " 23:59:59";
                } else {
                    this.formItem.startTime = this.getTodayDate() + " 00:00:00";
                    this.formItem.endTime = this.getTodayDate() + " 23:59:59";
                }
            },
        },
        data() {
            return {
                v: this,
                showMap: false,
                map: '',
                //控制动画播放属性
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
                    stop: false
                },
                //动画marker点
                movingMarker: null,
                //动画速度轨迹chart对象
                movingChart: null,
                movingChartOptions: null,
                //动画轨迹线参数对象
                mapcenter: {
                    lng: 114.357527,
                    lat: 30.550822
                },
                zoom: 14,
                speedList: [],
                stationList: [],
                formItem: {},
                carCode: '',
                carList: [],
                timeRange: [],
                pathList: [],
                formItem: {
                    startTime: '',
                    endTime: '',
                    zdbh: '',
                    ignition: 50,
                    brennschluss: '60'
                },
                item: {},
                totalTime: 0,
                speeds: {},
                choosedIndex: 0,
            }
        },
        mounted() {
            this.formItem.startTime = this.getTodayDate() + " 00:00:00";
            this.formItem.endTime = this.getTodayDate() + " 23:59:59";
            this.formItem.zdbh = this.$route.params.zdbh;
            this.timeRange = [this.formItem.startTime, this.formItem.endTime];
            this.choosedIndex = 0;
            this.getCarList();
        },
        methods: {
            itemClick(item, index) {
                this.item = item;
                this.choosedIndex = index;
                this.getBdData();
                this.stopAnimation();
                // this.getDbData();
            },
            getMinute(longTypeDate) {
                if (!longTypeDate) return;
                let min = parseInt(longTypeDate / (1000 * 60));
                return min;
            },
            dateFormat(longTypeDate) {
                if (!longTypeDate) return;
                let hour = parseInt(longTypeDate / (1000 * 60 * 60));
                let min = parseInt((longTypeDate - hour * 1000 * 60 * 60) / (1000 * 60));
                let sec = parseInt((longTypeDate - hour * 1000 * 60 * 60 - min * 1000 * 60) / (1000));
                let s = '';
                if (hour > 0) {
                    s += hour + '小时';
                }
                if (min > 0) {
                    s += min + '分';
                }
                if (sec > 0) {
                    s += sec + '秒';
                }
                return s;
            },
            getCarList() {
                this.$http.get(this.apis.CLGL.QUERY, {params: {pageSize: 1000}}).then((res) => {
                    if (res.code === 200 && res.page.list) {
                        this.carList = res.page.list;
                        if (this.carList.length != 0) {
                            this.formItemList();
                        }
                    }
                })
            },
            getTodayDate() {
                let now = new Date();
                return now.format("yyyy-MM-dd");
            },
            formItemList() {
                let startTime = this.formItem.startTime;
                let endTime = this.formItem.endTime;
                if (typeof startTime === 'object') {
                    startTime = startTime.format('yyyy-MM-dd hh:mm:ss');
                }
                if (typeof endTime === 'object') {
                    endTime = endTime.format('yyyy-MM-dd hh:mm:ss');
                }
                let p = {
                    startTime: startTime,
                    endTime: endTime,
                    zdbh: this.formItem.zdbh,
                    ignition: this.formItem.ignition,
                    brennschluss: this.formItem.brennschluss
                }
                this.totalTime = 0;
                this.pathList = [];
                this.item = {};
                this.showMap = false;
                this.$http.post(this.apis.CLGL.GPS_HITSOR, p).then((res) => {
                    if (res.code === 200 && res.result) {
                        var geoc = new BMap.Geocoder();
                        for (let r of res.result) {
                            if (!r.ksjps || !r.jsjps) continue
                            let ksgps = r.ksjps.split(',');
                            let jsgps = r.jsjps.split(',');
                            r.kswd = ksgps[1];
                            r.ksjd = ksgps[0];
                            r.jswd = jsgps[1];
                            r.jsjd = jsgps[0];
                            r.ksdz = '出发地';
                            r.jsdz = '目的地';
                            this.totalTime += r.sc;

                            console.log(r);
                            //解析开始地址
                            geoc.getLocation(new BMap.Point(r.ksjd, r.kswd), (rs) => {
                                var addComp = rs.addressComponents;
                                r.ksdz = addComp.street;
                            });
                            //解析结束地址
                            geoc.getLocation(new BMap.Point(r.jsjd, r.jswd), (rs) => {
                                var addComp = rs.addressComponents;
                                r.jsdz = addComp.street;
                            });
                        }
                        this.pathList = res.result;
                        if (this.pathList.length > 0) {
                            this.itemClick(this.pathList[0], 0);
                        }
                    }
                })
            },
            getBdData() {
                let p = {
                    zdbh: this.formItem.zdbh,
                    startTime: this.item.kssj,
                    endTime: this.item.jssj,
                }
                this.speedList = [];
                this.speeds = {};
                let v = this;

                this.$http.post(this.apis.CLGL.GPS_HITSOR_GPS_BD, p).then((res) => {
                    if (res.code === 200 && res.result) {
                        this.stationList = res.result;
                        for (let r of this.stationList) {
                            let date = new Date(r.loc_time);
                            let speed = parseInt(r.speed);
                            this.speedList.push([r.loc_time, speed]);
                            this.speeds[date.getTime()] = speed;
                        }
                        v.Buildmap()
                    }
                })
            },
            getDbData() {
                let p = {
                    zdbh: this.formItem.zdbh,
                    startTime: this.item.kssj,
                    endTime: this.item.jssj,
                }
                this.speedList = [];
                this.speeds = {};
                let v = this;

                this.$http.post(this.apis.CLGL.GPS_HITSOR_GPS, p).then((res) => {
                    if (res.code === 200 && res.result) {
                        this.stationList = res.result;
                        for (let r of this.stationList) {
                            let date = new Date(r.cjsj);
                            let speed = parseInt(r.yxsd);
                            this.speedList.push([r.cjsj, speed]);
                            this.speeds[date.getTime()] = speed;
                            r.longitude = r.bdjd;
                            r.latitude = r.bdwd;
                        }
                        v.Buildmap()
                    }
                })
            },
            formatDate(s) {
                let date = new Date(s);
                let min = date.getMinutes()
                let sec = date.getSeconds()
                if (min < 10) min = '0' + min;
                if (sec < 10) sec = '0' + sec;
                var texts = (date.getMonth() + 1) + '-' + date.getDate() + ' ' + min + ':' + sec;
                return texts;
            },
            Buildmap() {
                let v = this;
                this.showMap = true;
                // 百度地图API功能
                this.map = new BMap.Map("allmap"); // 创建Map实例
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
                this.map.centerAndZoom(new BMap.Point(this.stationList[0].longitude, this.stationList[0].latitude), this.zoom); // 初始化地图,设置中心点坐标和地图级别

                setTimeout(() => {
                    v.line();
                }, 100)
            },
            line() {
                this.showMap = true;
                let v = this;
                var pois = [];
                for (let r of v.stationList) {
                    pois.push(new BMap.Point(r.longitude, r.latitude));
                }
                v.map.setViewport(pois)
                var polyline = new BMap.Polyline(pois, {
                    enableEditing: false,//是否启用线编辑，默认为false
                    enableClicking: false,//是否响应点击事件，默认为true
                    // icons:[icons],
                    strokeWeight: '8',//折线的宽度，以像素为单位
                    strokeOpacity: 0.8,//折线的透明度，取值范围0 - 1
                    strokeColor: "#18a45b" //折线颜色
                });
                this.map.addOverlay(polyline);          //增加折线

                // 增加起点
                var pt1 = new BMap.Point(v.stationList[0].longitude, v.stationList[0].latitude);
                var myIcon1 = new BMap.Icon("http://47.98.39.45:9092/icon/map_line_begin.png", new BMap.Size(37, 62), {anchor: new BMap.Size(19, 62),});
                var marker1 = new BMap.Marker(pt1, {icon: myIcon1});  // 创建标注
                this.map.addOverlay(marker1);

                //初始化动画marker对象
                var moveIcon = new BMap.Icon("http://47.98.39.45:9092/icon/ic_car_online.png", new BMap.Size(32, 32), {anchor: new BMap.Size(16, 32),});
                this.movingMarker = new BMap.Marker(pt1, {icon: moveIcon});
                this.map.addOverlay(this.movingMarker);

                // 增加终点
                var pt2 = new BMap.Point(v.stationList[v.stationList.length - 1].longitude, v.stationList[v.stationList.length - 1].latitude);
                var myIcon2 = new BMap.Icon("http://47.98.39.45:9092/icon/map_line_end.png", new BMap.Size(37, 62), {anchor: new BMap.Size(19, 62),});
                var marker2 = new BMap.Marker(pt2, {icon: myIcon2});  // 创建标注
                this.map.addOverlay(marker2);
                //画轨迹线
                setTimeout(() => {
                    v.drawLineChart();
                }, 100)
            },
            drawLineChart() {
                let v = this;
                // 基于准备好的dom，初始化echarts实例
                this.movingChart = echarts.init(document.getElementById('trackLineChart'));
                //运行轨迹chart数据集合，格式:['日期时间（格式化成MM-dd HH:mm，分两行显示日期和时间，界面会美观）','速度值']
                var data2 = this.speedList;
                // var data2 = [["2016-03-29", 45], ["2016-03-30", 30], ["2016-03-31", 20], ["2016-04-01", 10], ["2016-04-02", 0]];
                this.movingChartOptions = {
                    animation: true,
                    tooltip: {
                        triggerOn: 'none',
                        position: function (pt) {
                            return [pt[0], 130];
                        }
                    },
                    xAxis: {
                        type: 'time',
                        axisLabel: {
                            // interval:0,//横轴信息全部显示
                            // rotate:-90,//-90度角倾斜显示
                            // 使用函数模板，函数参数分别为刻度数值（类目），刻度的索引
                            formatter: function (value, index) {
                                // 格式化成月/日，只在第一个刻度显示年份
                                var date = new Date(value);
                                let texts = date.format('MM-dd HH:mm');
                                return texts;
                            }
                        },
                        axisPointer: {
                            animation: true,
                            //给出pointer显示的位置点，和数据参数关联
                            // value: '2016-03-29',
                            snap: true,
                            triggerTooltip: false,
                            lineStyle: {
                                color: '#004E52',
                                opacity: 0.5,
                                width: 2
                            },
                            //格式化显示运动展示内容
                            label: {
                                show: true,
                                formatter: function (params) {
                                    let time = params.value;
                                    let speed = v.speeds[time];
                                    var value = speed;
                                    return "车辆瞬时速度：" + value + " Km/h";
                                },
                                backgroundColor: '#004E52'
                            },
                            handle: {
                                show: true,
                                color: '#004E52'
                            }
                        },
                        splitLine: {
                            show: false
                        }
                    },
                    yAxis: {
                        type: 'value',
                        axisTick: {
                            inside: true
                        },
                        splitLine: {
                            show: false
                        },
                        axisLabel: {
                            inside: true,
                            formatter: '{value}\n'
                        },
                        z: 10
                    },
                    grid: {
                        y2: 150,
                        // top: 110,
                        // left: 25,
                        // right: 25,
                        // height: 160
                    },
                    series: [
                        {
                            name: '速度',
                            type: 'line',
                            smooth: true,
                            stack: 'a',
                            symbol: 'circle',
                            symbolSize: 5,
                            sampling: 'average',
                            itemStyle: {
                                normal: {
                                    color: '#d68262'
                                }
                            },
                            data: data2
                        }
                    ]
                };
                // 使用刚指定的配置项和数据显示图表。
                this.movingChart.setOption(this.movingChartOptions);
            },
            animationDot() {
                this.playAndStopBtnGroup.play = false;
                this.playAndStopBtnGroup.stop = true;
                if (this.movingMarker != null) {
                    this.playAndStopBtnGroup.playIndex++;
                    if (this.playAndStopBtnGroup.playIndex == this.stationList.length) {
                        //循环执行完成后，重置播放轨迹点
                        this.playAndStopBtnGroup.playIndex = 0;
                        this.playAndStopBtnGroup.timer = 1000;
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
                        this.movingChartOptions.xAxis.axisPointer.value = this.speedList[this.playAndStopBtnGroup.playIndex][0];
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
            stopAnimation() {
                this.playAndStopBtnGroup.play = true;
                this.playAndStopBtnGroup.stop = false;
                this.playAndStopBtnGroup.playIndex = 0;
                if (this.playAndStopBtnGroup.playTimer != -1) {
                    clearTimeout(this.playAndStopBtnGroup.playTimer);
                }
            }
        }
    }
</script>

<style>
</style>
