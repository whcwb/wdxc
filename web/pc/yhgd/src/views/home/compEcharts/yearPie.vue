<template>
	<div style="width: 100%;height: 100%;position: relative;">
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
    name: 'dataSourcePie',
    data () {
        return {
            SpinShow:true,
			loading:this.$store.state.app.loading,
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
        getData(){
            this.$http.get(this.apis.CHART_DATA.nianshenbt,{params:this.form}).then((res)=>{
                if (res.code == 200){
                    for (let r in res.result){
                        this.pieData.push({name:r+'天',value:res.result[r]});
                    }
                    this.initChart();
                }
            })
        },
        initChart(){
            var v = this
            this.$nextTick(() => {
                var dataSourcePie = echarts.init(document.getElementById(v.Eid));
                const option = {
                    title: {
                        text: '年审提醒',
                        textStyle: {
                        }
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c}台'
                    },
                    legend: {
                        orient: 'horizontal',
                        left: 'right',
                        data: ['90天', '60天', '30天']
                    },
                    series: [
                        {
                            name: '年审提醒',
                            type: 'pie',
                            radius: '66%',
                            // center: ['50%', '60%'],
                            data: this.pieData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ],
                    color: ["#19be6b","#ffad33","#ed3f14","#ff7f50", "#87cefa", "#da70d6", "#32cd32", "#6495ed", "#ff69b4", "#ba55d3", "#cd5c5c", "#ffa500", "#40e0d0", "#1e90ff", "#ff6347", "#7b68ee", "#00fa9a", "#ffd700", "#6699FF", "#ff6666", "#3cb371", "#b8860b", "#30e0e0"]
                };
                dataSourcePie.setOption(option);
                window.addEventListener('resize', function () {
                    dataSourcePie.resize();
                });
            });
		}
	}
};
</script>