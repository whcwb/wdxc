<style lang="less">
	@import "../../../../styles/common.less";
</style>
<template>
	<div>
		<Modal v-model="editPasswordModal"
		       :closable='false'
		       width="80%"
		       :mask-closable=false>
			<div :style="{height: boxHeight}">
				<div style="width:100%;height:100%;position: relative;">
					<div style="width:100%;height:100%;" :id="Eid"></div>
					<div v-if="SpinShow" style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 100;">
						<Spin fix>
							<Icon type="load-c" :size=loading.size class="demo-spin-icon-load"></Icon>
							<div style="font-size: 30px;">{{loading.text}}</div>
						</Spin>
					</div>
				</div>
			</div>
			<div slot='footer'>
				<Button type="default" @click="close">关闭</Button>
			</div>
		</Modal>
	</div>
</template>

<script>
	import echarts from 'echarts';

	export default {
		name: 'dataSourceBar',
		data() {
			return {
			    	boxHeight:'200px',
                           	editPasswordModal:true,
				SpinShow:true,
				loading:this.$store.state.app.loading,
				result:{},
			    	ech:{}
			};
		},
		props: {
			Eid: {
				type: String,
				default: 'eBar00'
			}
		},
		created(){
                    	this.ech = this.$store.state.app.ech
		    	this.boxHeight = (window.innerHeight-300)+'px'
			setTimeout(() => {
                this.SpinShow = false;
            }, 1000);
		},
		mounted() {
		    this.getData();
		},
		methods:{
                    close(){
                      this.$parent.compName = ''
		    },
            getData(){
                this.$http.post(this.apis.CHART_DATA.csxxtj,{'cph':this.ech.cph,'day':this.ech.time}).then((res)=>{
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
                            text: "超速统计("+this.ech.cph+')',
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
                            data: this.result.list,
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
                            data: this.result.count
                        }],
                        color: ["#6495ed", "#da70d6", "#ff69b4", "#ba55d3", "#cd5c5c", "#ffa500", "#40e0d0", "#1e90ff", "#ff6347", "#7b68ee", "#00fa9a", "#ffd700", "#6699FF", "#ff6666", "#3cb371", "#b8860b", "#30e0e0"]
                    };
                    dataSourceBar.setOption(option);
                });
			}
		}
	};
</script>