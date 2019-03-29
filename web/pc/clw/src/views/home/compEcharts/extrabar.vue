<style lang="less">
	@import "../../../styles/common.less";
</style>
<template>
	<div style="width:100%;height:100%;position: relative;">
		<div style="position: absolute;top:0;right: 0;z-index: 1000">
			<ButtonGroup size="small" shape="circle">
		        <Button :type="day == '三天' ? 'primary' : 'default'" @click="changeDays(3)">三天</Button>
		        <Button :type="day == '五天' ? 'primary' : 'default'" @click="changeDays(5)">五天</Button>
		        <Button :type="day == '七天' ? 'primary' : 'default'" @click="changeDays(7)">七天</Button>
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
		name: 'dataSourceBar',
		data() {
			return {
				SpinShow:true,
				loading:this.$store.state.app.loading,
				result:{},
				form:{
					days:3,
					type:'cstj'
				},
				day:'三天'
			};
		},
		props: {
			Eid: {
				type: String,
				default: 'eBar'
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
					case 3:
					    this.day = '三天';
					    break;
					case 5:
					    this.day = '五天';
					    break;
					case 7:
					    this.day = '七天';
					    break;
					default:
                        this.day = '三天';
                        break;
				}
		        this.getData();
			},
            getData(){
                this.$http.get(this.apis.CHART_DATA.SAFE_DRIVING,{params:this.form}).then((res)=>{
                    if (res.code == 200){
                        this.result = res.result;
                        this.initChart();
                    }
                })
            },
            initChart(){
                var v = this
                this.$nextTick(() => {
                    var dataSourceBar = echarts.init(document.getElementById(v.Eid));

                    const option = {
                        title: {
                            text: "超速统计("+this.day+')',
                            textStyle: {
                            }
                        },
                        color: ['#3398DB'],
                        tooltip: {
                            trigger: 'axis',
                            formatter: '{a} <br/>{b} : {c}次',
                            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: [{
                            type: 'category',
                            data: this.result.carNumbers,
                            axisTick: {
                                alignWithLabel: true
                            }
                        }],
                        yAxis: [{
                            type: 'value'
                        }],
                        series: [{
                            name: '超速次数',
                            type: 'bar',
                            label: {
                                normal: {
                                    show: true,
                                    position: 'top'
                                }
                            },
                            barWidth: '60%',
                            data: this.result.overSpeedMap.yAxis
                        }],
                        color: ["#6495ed", "#da70d6", "#ff69b4", "#ba55d3", "#cd5c5c", "#ffa500", "#40e0d0", "#1e90ff", "#ff6347", "#7b68ee", "#00fa9a", "#ffd700", "#6699FF", "#ff6666", "#3cb371", "#b8860b", "#30e0e0"]
                    };
                    dataSourceBar.setOption(option);
                    dataSourceBar.on('click', function (params) {
                        let time = ''
                        if(v.day == '三天'){
                            time = 3
                        }else if(v.day == '五天'){
                            time = 5
                        }else if(v.day == '七天'){
                            time = 7
                        }
                        v.$store.commit('echChanged',{'cph':params.name,'time':time})
                        log('-**---------------------**-',params)
                    });
                    window.addEventListener('resize', function() {
                        dataSourceBar.resize();
                    });
                });
			}
		}
	};
</script>
