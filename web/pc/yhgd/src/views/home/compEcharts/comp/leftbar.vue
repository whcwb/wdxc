<template>
		<div style="width:100%;height:100%;" :id="leid"></div>
</template>

<script>
	import echarts from 'echarts';

	export default {
		name: 'dataSourceBar',
		data() {
			return {
				//
			};
		},
		props: {
			leid: {
				type: String,
				default: 'eBar'
			},
			result:{
			    type:Object,
				default:{}
,			}
		},
		created() {
            var v = this
			this.$nextTick(() => {
				var dataSourceBar = echarts.init(document.getElementById(v.leid));

				const option = {
					title: {
				        text: "今日派单统计",
				        textStyle: {
				            // color: "rgb(255, 255, 255)"
				        }
				    },
					color: ['#3398DB'],
					tooltip: {
						trigger: 'axis',
						formatter: '{a} <br/>{b} : {c}单',
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
						data: this.result.sjxm,
						axisTick: {
							alignWithLabel: true
						}
					}],
					yAxis: [{
						type: 'value'
					}],
					series: [{
						name: '派单数',
						type: 'bar',
						label: {
							normal: {
								show: true,
								position: 'top'
							}
						},
						barWidth: '60%',
						data: this.result.count
					}]
				};
				dataSourceBar.setOption(option);
				window.addEventListener('resize', function() {
					dataSourceBar.resize();
				});
			});
		}
	};
</script>