<style type="text/css">
	@import "../../../styles/common.less";
</style>
<template>
	<div style="height: 100%;width: 100%;position: relative;">
		<div class="box-row" style="width:100%;height:100%;">
			<div class="body-F" style="height: 100%;" v-if="result != null">
				<leftbar leid='leftbars' :result="result"></leftbar>
			</div>
			<div style="width:100px;height:100%;" :id="Eid"></div>
		</div>
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
import leftbar from './comp/leftbar.vue'
export default {
    name: 'dataSourcePie',
    components: {
    	leftbar
	},
    data () {
        return {
            SpinShow:true,
			loading:this.$store.state.app.loading,
			result:null,
			form:{},
			pieData:[]
        };
    },
    props: {
		Eid:{
			type:String,
			default:'ePie'
		}
	},
	created(){
		setTimeout(() => {
            this.SpinShow = false;
        }, 1000);
	},
    mounted () {
        this.getData();
    },
	methods:{
        initChart(){
            var v = this
            this.$nextTick(() => {
                var dataSourcePie = echarts.init(document.getElementById(v.Eid));
                const option = {
					title: {
								// text: '今日派单统计'
						},
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} 单'
                    },
                    legend: {
                        orient: 'horizontal',
                        x: "center",
                        data: ['待派单','已派单']
                    },
                    series: [
                        {
                            name: '今日订单',
                            type: 'pie',
                            radius: '66%',
                            // center: ['50%', '40%'],
                            label: {
                                normal: {
                                    position: 'inner'
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data:this.pieData,
                            itemStyle: {
                                normal: {
                                    label: {
                                        formatter: "{b}: {c}",
                                        show: true,
                                        position: "inside"
                                    }
                                },
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ],
                    color: ["#87cefa", "#da70d6", "#32cd32", "#6495ed", "#ff69b4", "#ba55d3", "#cd5c5c", "#ffa500", "#40e0d0", "#1e90ff", "#ff6347", "#7b68ee", "#00fa9a", "#ffd700", "#6699FF", "#ff6666", "#3cb371", "#b8860b", "#30e0e0"]
                };
                dataSourcePie.setOption(option);
                window.addEventListener('resize', function () {
                    dataSourcePie.resize();
                });
            });
		},
        getData(){
            this.$http.post(this.apis.CHART_DATA.PD,this.form).then((res)=>{
                if (res.code == 200){
                    this.result = res.result;
					for (let r in res.result.tjMap){
					    this.pieData.push({name:r,value:res.result.tjMap[r]});
                    }
                    this.initChart();
                }
            })
        }
	}
};
</script>