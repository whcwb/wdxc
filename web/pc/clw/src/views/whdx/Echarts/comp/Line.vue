<template>
	<div style="width:100%;height:100%;" :id="Eid"></div>
</template>

<script>
	import echarts from 'echarts';

	export default {
		name: 'dataSourceLine',
		data() {
			return {
				//
			};
		},
		props: {
			Eid:{
				type:String,
				default:'eEline'
			}
		},
		mounted() {
			var v = this
			this.$nextTick(() => {
				var dataSourceLine= echarts.init(document.getElementById(v.Eid));
				const option = {
				    title: {
				        text: '折线图堆叠'
				    },
				    tooltip: {
				        trigger: 'axis'
				    },
				    legend: {
				        data:['邮件营销','联盟广告','视频广告']
				    },
				    grid: {
				        left: '3%',
				        right: '4%',
				        bottom: '3%',
				        containLabel: true
				    },
//				    toolbox: {
//				        feature: {
//				            saveAsImage: {}
//				        }
//				    },
				    xAxis: {
				        // type: 'category',
				        boundaryGap: true,
				        data: ['周一','周二','周三','周四','周五','周六','周日']
				    },
				    yAxis: {
				        type: 'value'
				    },
				    series: [
				        {
				            name:'邮件营销',
				            type:'line',
				            stack: '总量',
				            data:[120, 132, 101, 134, 90, 230, 210]
				        },
				        {
				            name:'联盟广告',
				            type:'line',
				            stack: '总量',
				            data:[220, 182, 191, 234, 290, 330, 310]
				        },
				        {
				            name:'视频广告',
				            type:'line',
				            stack: '总量',
				            data:[150, 232, 201, 154, 190, 330, 410]
				        }
				    ],
			    	color: ["#ff7f50", "#87cefa", "#da70d6", "#32cd32", "#6495ed", "#ff69b4", "#ba55d3", "#cd5c5c", "#ffa500", "#40e0d0", "#1e90ff", "#ff6347", "#7b68ee", "#00fa9a", "#ffd700", "#6699FF", "#ff6666", "#3cb371", "#b8860b", "#30e0e0"]
            };
				dataSourceLine.setOption(option);
	            window.addEventListener('resize', function () {
	                dataSourceLine.resize();
	            });
			});
		}
	};
</script>