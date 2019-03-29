<template>
	<div style="width:100%;height:100%;position: relative;">
		<div style="position: absolute;top:0;right: 0;z-index: 1000">
			<ButtonGroup size="small" shape="circle">
		        <Button :type="day == '月' ? 'primary' : 'default'" @click="changeDays(30)">月</Button>
		        <Button :type="day == '周' ? 'primary' : 'default'" @click="changeDays(7)">周</Button>
		    </ButtonGroup>
		</div>
		<div style="width:100%;height:100%;" :id="Eid"></div>
		<div v-if="SpinShow" style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 100;">
			<Spin fix>
	            <Icon type="load-c" :size=loading.size class="demo-spin-icon-load"></Icon>
	            <div style="font-size: 30px;">{{loading.text}}</div>
	        </Spin>
		</div>
	</div>
</template>

<script>
	import echarts from 'echarts';

	export default {
		name: 'dataSourceLine',
		data() {
			return {
				SpinShow:true,
				loading:this.$store.state.app.loading,
				result:{},
				form:{
				    days:30,
                    type:'aqjs'
				},
                day:'月'
			};
		},
		props: {
			Eid: {
				type: String,
				default: 'eEline'
			}
		},
		created(){
			setTimeout(() => {
                this.SpinShow = false;
            }, 1000);
		},
		mounted() {
		    this.getData();
		},
		methods:{
            changeDays(d){
                this.form.days = d;
                switch(d){
                    case 30:
                        this.day = '月';
                        break;
                    case 7:
                        this.day = '周';
                        break;
                    default:
                        this.day = '月';
                        break;
                }
                this.getData();
            },
		    initChart(){
                var v = this
                this.$nextTick(() => {
                    var dataSourceLine = echarts.init(document.getElementById(v.Eid));
                    const option = {
                        title: {
                            text: '安全驾驶('+this.day+')',
                            textStyle: {
                            }
                        },
                        tooltip: {
                            axisPointer: {
                                type: 'cross',
                                label: {
                                    backgroundColor: '#6a7985'
                                }
                            },
                            trigger: 'axis'
                        },
                        legend:{
//						x: "right",
//				        y: "top",
                            orient: "vertical",
                            data: ['鄂A2345']
                        },
                        grid: {
                            // left: '3%',
                            // right: '4%',
                            // bottom: '3%',
                            containLabel: true
                        },
                        xAxis: [{
//						type: 'category',
                            boundaryGap: false,
                            data: this.result.driverNames,
                        }],
                        yAxis: [{
                            type: 'value'
                        }],
                        series: [
                            {
                                name: '急加速',
                                type: 'bar',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top'
                                    }
                                },
                                areaStyle: {
                                    normal: {}
                                },
                                data: this.result.speedUpMap.yAxis,
                            },
                            {
                                name: '急减速',
                                type: 'bar',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top'
                                    }
                                },
                                areaStyle: {
                                    normal: {}
                                },
                                data: this.result.breakMap.yAxis,
                            },
                            {
                                name: '急转弯',
                                type: 'bar',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top'
                                    }
                                },
                                areaStyle: {
                                    normal: {}
                                },
                                data: this.result.wheelMap.yAxis,
                            },
                            {
                                name: '超速',
                                type: 'bar',
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top'
                                    }
                                },
                                areaStyle: {
                                    normal: {}
                                },
                                data: this.result.overSpeedMap.yAxis,
                            },
                        ],
                        color: ["#6495ed", "#da70d6", "#ff69b4", "#ba55d3", "#cd5c5c", "#ffa500", "#40e0d0", "#1e90ff", "#ff6347", "#7b68ee", "#00fa9a", "#ffd700", "#6699FF", "#ff6666", "#3cb371", "#b8860b", "#30e0e0"]
                    };
                    dataSourceLine.setOption(option);
                    window.addEventListener('resize', function() {
                        dataSourceLine.resize();
                    });
                });
			},
		    getData(){
		        this.$http.get(this.apis.CHART_DATA.SAFE_DRIVING,{params:this.form}).then((res)=>{
		            if (res.code == 200){
		                this.result = res.result;
						this.initChart();
                    }
				})
			}
		}
	};
</script>
