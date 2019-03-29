<template>
	<div class="box">
		<div class="tit" style="text-align: center;">
			{{CarSpeed.cph}}			
		</div>
		<div class="body">
			<div style="width:100%;height:92%;position: relative;" :id="Eid"></div>
		</div>
	</div>
</template>

<script>
import echarts from 'echarts';

export default {
    name: 'datagauge',
    data () {
        return {
            speed:''
        };
    },
    props: {
		Eid:{
			type:String,
			default:'ePie'
		},
		CarSpeed:{
			type:Object,
			default:{}
		}
	},
	watch: {
		CarSpeed: function(newQuestion, oldQuestion) {
			this.echart()
		}
	},
    mounted () {
    	var v = this
		this.echart()
   	},
   	methods:{
   		echart(){
   			var v = this
   			this.$nextTick(() => {
	            var datagauge = echarts.init(document.getElementById(v.Eid));
	            const option = {
	            	title: {
		                show: true,
		                textStyle: {
		                    fontSize: 22,
		                    fontStyle: "normal"
		                },
		                height: 0,
		                offsetCenter: [0, 0],
		            },
	            	tooltip : {
				        formatter: "{a}:<br/>{c}{b}"
				    },
				    toolbox: {
				        show: false,
				        feature: {
				            mark: {
				                show: true
				            },
				            restore: {
				                show: true
				            },
				            saveAsImage: {
				                show: true
				            }
				        }
				    },
				    series: [
				        {
				            name: "时速",
				            type: "gauge",
				            detail: {
				                formatter: "{value}",//时速
				                show: true,
				                height: 51,
	                			offsetCenter: [0, 51],
				                textStyle: {
				                    fontSize: 20
				                }
				            },
				            data: [
				                {
				                    value: v.CarSpeed.speed,
				                    name: "km/h"
				                }
				            ],
				            min: 0,
				            max: 150,
				            splitNumber: 10,
				            axisLine: {
				                lineStyle: {
				                    color: [[0.2, "#228b22"], [0.8, "#48b"], [1, "#ff4500"]]
				                }
				            },
				            radius: "97%"
				        }
				    ]
	//          	color: ["#19be6b","#ffad33","#ed3f14","#ff7f50", "#87cefa", "#da70d6", "#32cd32", "#6495ed", "#ff69b4", "#ba55d3", "#cd5c5c", "#ffa500", "#40e0d0", "#1e90ff", "#ff6347", "#7b68ee", "#00fa9a", "#ffd700", "#6699FF", "#ff6666", "#3cb371", "#b8860b", "#30e0e0"]
	            };
	            datagauge.setOption(option);
	//          setInterval(function () {
	//			    option.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	//			    datagauge.setOption(option, true);
	//			},2000);
	//          window.addEventListener('resize', function () {
	//              datagauge.resize();
	//          });
	        });  	
   		}
        
   	}
};
</script>