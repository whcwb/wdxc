<template>
	<div style="width:100%;height:100%;" :id="Eid"></div>
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
			Eid:{
				type:String,
				default:'eBar'
			}
		},
		mounted() {
			var v = this
			this.$nextTick(() => {
				var dataSourceBar= echarts.init(document.getElementById(v.Eid));
				
				var dataAxis = ['点', '击', '柱', '子', '或', '者', '两', '指', '在', '触', '屏', '上', '滑', '动', '能', '够', '自', '动', '缩', '放'];
				var data = [220, 182, 191, 234, 290, 330, 310, 123, 442, 321, 90, 149, 210, 122, 133, 334, 198, 123, 125, 220];
				var yMax = 500;
				var dataShadow = [];
				
				for (var i = 0; i < data.length; i++) {
				    dataShadow.push(yMax);
				}
				const option = {
					title: {
						text: '特性示例：渐变色 阴影 点击缩放',
						subtext: 'Feature Sample: Gradient Color, Shadow, Click Zoom'
					},
					xAxis: {
//			            axisLabel: {
//			                show: true,
//			                interval: 0,
//			                rotate: 0,
//			                margin: 0
//			            },
//			            position: "bottom",
						data: dataAxis,
						axisLabel: {
							inside: true,
							textStyle: {
								color: '#fff'
							}
						},
						axisTick: {
							show: false
						},
						axisLine: {
							show: false
						},
						z: 5
					},
					yAxis: {
						axisLine: {
							show: false
						},
						axisTick: {
							show: false
						},
						axisLabel: {
							textStyle: {
								color: '#999'
							}
						}
					},
					dataZoom: [{
						type: 'inside'
					}],
					series: [{ // For shadow
							type: 'bar',
							itemStyle: {
								normal: {
									color: 'rgba(0,0,0,0.05)'
								}
							},
							barGap: '-100%',
							barCategoryGap: '40%',
							data: dataShadow,
							animation: false
						},
						{
							type: 'bar',
							itemStyle: {
								normal: {
									color: new echarts.graphic.LinearGradient(
										0, 0, 0, 1, [{
												offset: 0,
												color: '#83bff6'
											},
											{
												offset: 0.5,
												color: '#188df0'
											},
											{
												offset: 1,
												color: '#188df0'
											}
										]
									)
								},
								emphasis: {
									color: new echarts.graphic.LinearGradient(
										0, 0, 0, 1, [{
												offset: 0,
												color: '#2378f7'
											},
											{
												offset: 0.7,
												color: '#2378f7'
											},
											{
												offset: 1,
												color: '#83bff6'
											}
										]
									)
								}
							},
							data: data
						}
					]
				};
				dataSourceBar.setOption(option);
	            window.addEventListener('resize', function () {
	                dataSourceBar.resize();
	            });
			});
		}
	};
</script>