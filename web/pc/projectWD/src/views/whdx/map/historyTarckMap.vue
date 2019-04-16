<!--
历史轨迹
车辆历史轨迹 地图展示界组件
-->

<template>
	<div style="width: 100%;height: 100%;background-color: #00FFFF;">
		<Modal v-model="showModal" width='900' :closable="false" @on-cancel="close" @on-ok="close">
			<Row>
				<Col span="24">
					<div id="allmap" style="width: 100%;height: 500px;"></div>
				</Col>
			</Row>
			<Row>
				<Col span="2">
					<ButtonGroup vertical style="margin-top: 120px">
						<Button type="primary" shape="circle" icon="md-play" size="large" @click="animationDot" v-show="playAndStopBtnGroup.play"></Button>
						<Button type="error" shape="circle" icon="ios-square" size="large" @click="stopAnimation" v-show="playAndStopBtnGroup.stop"></Button>
						<Button type="warning" shape="circle" icon="md-skip-forward" size="large" @click="playAndStopBtnGroup.timer = 200"></Button>
						<Button type="warning" shape="circle" icon="md-skip-backward" size="large" @click="playAndStopBtnGroup.timer = 1000"></Button>
					</ButtonGroup>
				</Col>
				<Col span="22">
					<div id="trackLineChart" style="width: 100%;height: 400px;"></div>
				</Col>
			</Row>
		</Modal>
	</div>
</template>

<script>

	import echarts from 'echarts'
	export default {
		name: 'historyTarckMap',
		data() {
			return {
                showModal:true,
				map: '',
				//控制动画播放属性
				playAndStopBtnGroup:{
                    //默认动画间隔时长，单位：秒
                    timer:1000,
					//默认动画播放位置和stationList数据对象同步
                    playIndex:0,
					//动画对象
					playTimer:-1,
					//播放按钮是否显示
                  	play:true,
					//停止按钮是否显示
					stop:false
				},
				//动画marker点
				movingMarker:null,
				//动画速度轨迹chart对象
				movingChart:null,
                movingChartOptions:null,
				//动画轨迹线参数对象
				mapcenter: {
					lng: 114.357527,
					lat: 30.550822
				},
				zoom: 14,
				speedList:[],
				stationList: [
				{
					id: "435383685965938688",
					dz: null,
					mc: "校大门",
					jd: 114.36439,
					wd: 30.545721,
				},{
					id: "435383685965938688",
					dz: null,
					mc: "校大门",
					jd: 114.36439,
					wd: 30.545721,
				},{
					id: "435383835257995264",
					dz: null,
					mc: "桂园",
					jd: 114.367372,
					wd: 30.546219,
				},{
					id: "435383897451134976",
					dz: null,
					mc: "明珠园",
					jd: 114.366259,
					wd: 30.546126,
				},{
					id: "435388661425504256",
					dz: null,
					mc: "自强超市",
					jd: 114.373086,
					wd: 30.545628,
				},{
					id: "435388816090464256",
					dz: null,
					mc: "主教学楼",
					jd: 114.37384,
					wd: 30.546095,
				},{
					id: "435389037470023680",
					dz: null,
					mc: "学生五舍",
					jd: 114.372583,
					wd: 30.54314,
				},{
					id: "435389106743148544",
					dz: null,
					mc: "学生十五舍",
					jd: 114.376607,
					wd: 30.542704,
				},{
					id: "435389271692541952",
					dz: null,
					mc: "体育馆",
					jd: 114.367013,
					wd: 30.544633,
				}],
                formItem:{}
			}
		},
		created() {

		},
		mounted() {
		    let item = this.$parent.choosedItem
            this.formItem = {
		        startTime : item.kssj,
		        endTime : item.jssj,
				zdbh : this.$parent.formItem.zdbh
			}

            this.getData();
            // let vi = 0
            // if(vi==this.stationList.length){
            // 	vi = 0
            // }
            // this.animationDot(this.stationList[vi])
            // setInterval(()=>{
            // 	vi ++
            // },500)
		},
		methods: {
            close(){
                this.showModal = false;
                setTimeout(() => {
                    this.$parent.$data.componentName = "";
                }, 200)
            },
            getData(){
                var v = this
				delete this.formItem.ignition
				delete this.formItem.brennschluss
                this.$http.post(this.apis.CLGL.GPS_HITSOR_GPS,this.formItem).then((res) =>{
                    if (res.code === 200){
                        this.stationList = res.result;
                        for(let r of res.result){
                            this.speedList.push([r.cjsj,r.yxsd]);
						}
                        this.Buildmap()
                    }
                })
            },
			Buildmap() {
                var v = this
				// 百度地图API功能
				this.map = new BMap.Map("allmap"); // 创建Map实例
				this.map.centerAndZoom(new BMap.Point(this.stationList[0].bdwd, this.stationList[0].bdjd), this.zoom); // 初始化地图,设置中心点坐标和地图级别
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
                this.line()
			},
			line(){
				var v = this
				var pois = [];
                for(let r of v.stationList){
                    pois.push(new BMap.Point(r.bdjd,r.bdwd));
				}
                v.map.setViewport(pois)
                // var sy = new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_OPEN_ARROW, {
                //     scale: 0.6,//图标缩放大小
                //     strokeColor:'#fff',//设置矢量图标的线填充颜色
                //     strokeWeight: '2',//设置线宽
                // });
                // var icons = new BMap.IconSequence(sy, '10', '20');
                var polyline = new BMap.Polyline(pois, {
                    enableEditing: false,//是否启用线编辑，默认为false
                    enableClicking: false,//是否响应点击事件，默认为true
                    // icons:[icons],
                    strokeWeight:'8',//折线的宽度，以像素为单位
                    strokeOpacity: 0.8,//折线的透明度，取值范围0 - 1
                    strokeColor:"#18a45b" //折线颜色
                });
                this.map.addOverlay(polyline);          //增加折线

				// 增加起点
                log((v.stationList[0].bdjd, v.stationList[0].bdwd));
                var pt1 = new BMap.Point(v.stationList[0].bdjd, v.stationList[0].bdwd);
                var myIcon1 = new BMap.Icon("http://119.23.242.234:9092/icon/map_line_begin.png", new BMap.Size(37,62), {anchor: new BMap.Size(19,62),});
                var marker1 = new BMap.Marker(pt1,{icon:myIcon1});  // 创建标注
                this.map.addOverlay(marker1);

                //初始化动画marker对象
                var moveIcon = new BMap.Icon("http://119.23.242.234:9092/icon/ic_car_online.png", new BMap.Size(32,32), {anchor: new BMap.Size(16,32),});
                this.movingMarker = new BMap.Marker(pt1, {icon:moveIcon});
                this.map.addOverlay(this.movingMarker);

				// 增加终点
                var pt2 = new BMap.Point(v.stationList[v.stationList.length-1].bdjd, v.stationList[v.stationList.length-1].bdwd);
                var myIcon2 = new BMap.Icon("http://119.23.242.234:9092/icon/map_line_end.png", new BMap.Size(37,62), {anchor: new BMap.Size(19,62),});
                var marker2 = new BMap.Marker(pt2,{icon:myIcon2});  // 创建标注
                this.map.addOverlay(marker2);
                //画轨迹线
				this.drawLineChart();
			},
			drawLineChart(){
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
                        axisPointer: {
                            animation: true,
                            //给出pointer显示的位置点，和数据参数关联
                            value: '2016-03-29',
                            snap: true,
                            triggerTooltip:false,
                            lineStyle: {
                                color: '#004E52',
                                opacity: 0.5,
                                width: 2
                            },
							//格式化显示运动展示内容
                            label: {
                                show: true,
                                formatter: function (params) {
									var value = 0;
									if (params.seriesData.length == 1){
                                        value = params.seriesData[0].data[1];
									}
                                    return "车辆瞬时速度："+value+" Km/h";
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
                        top: 110,
                        left: 15,
                        right: 15,
                        height: 160
                    },
                    series: [
                        {
                            name:'速度',
                            type:'line',
                            smooth:true,
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
			animationDot(){
			    this.playAndStopBtnGroup.play = false;
                this.playAndStopBtnGroup.stop = true;
                if (this.movingMarker != null){
                    this.playAndStopBtnGroup.playIndex++;
                    if (this.playAndStopBtnGroup.playIndex == this.stationList.length){
                        //循环执行完成后，重置播放轨迹点
                        this.playAndStopBtnGroup.playIndex = 0;
                        this.playAndStopBtnGroup.timer = 1000;
                        //重新开始
                        this.stopAnimation();
                        return;
					}
					try{
                        //取出下一个动画节点
                        var moveData = this.stationList[this.playAndStopBtnGroup.playIndex];
                        //更新地图移动轨迹
                        var movePoint = new BMap.Point(moveData.bdjd, moveData.bdwd);
                        this.movingMarker.setPosition(movePoint);

						 //给chart补充完数据后，再开启该方法
						 this.movingChartOptions.animation = false;
						 //更新chart移动轨迹
                        log('index');
                        log(this.playAndStopBtnGroup.playIndex);
                        log(this.speedList[this.playAndStopBtnGroup.playIndex][0]);
                        this.movingChartOptions.xAxis.axisPointer.value = this.speedList[this.playAndStopBtnGroup.playIndex][0];
						 //重置chart属性来实现自动移动
						 this.movingChart.setOption(this.movingChartOptions);

					}catch(e){
                        //防止动画执行报错，进行容错控制，防止界面卡死
					}
                    //循环执行，直到循环完数据
                    this.playAndStopBtnGroup.playTimer = setTimeout(()=>{
                        this.animationDot();
					}, this.playAndStopBtnGroup.timer);
                }
            },
            stopAnimation(){
                this.playAndStopBtnGroup.play = true;
                this.playAndStopBtnGroup.stop = false;
                if (this.playAndStopBtnGroup.playTimer != -1){
                    clearTimeout(this.playAndStopBtnGroup.playTimer);
                }
            }
		}
	}
</script>

<style>

</style>
