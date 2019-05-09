<style lang="less">
	@import "../../styles/common.less";
	.homeE{
		.indexCarType{
			border-bottom: solid 1px #C0C0C0;
			box-shadow: 2px 5px 5px #888888;
		}
		.divpadd{
			box-shadow: 2px 5px 5px #888888;
    		border: solid 2px #ded9d9;
			.divbgcolor{
				height: 260px;
				/*background: rgba(0,0,0,0.5);*/
				background-color: #fff;
			}
		}
	}
</style>
<template>
	<div class="box" style="height: 100%;background:#fff">
		<component :is="compName"></component>
		<div class="body" style="position: relative">
			<span style="position: absolute;top: 45%;left: 50%;transform: translate(-50%,-50%);font-size: 26px;text-align: center;color: #a4a4a4">
				<h1>
					欢迎登录
				</h1>
				<h1>
					车辆管理平台
				</h1>
			</span>
		</div>
	</div>
</template>

<script>
	import inforCard from './components/inforCard.vue'
	import eLine from './compEcharts/line.vue'
	import yPie from './compEcharts/yearPie.vue'
	import safeline from './compEcharts/safeline.vue'
	import listpie from './compEcharts/listPie.vue'
	import extra from './compEcharts/extrabar.vue'
	import scbar from './compEcharts/scbar.vue'
	import risk from './compEcharts/riskRecord.vue'


	import csMessbar from './compEcharts/comp/csMessbar'
	export default {
		name: 'home',
		components: {
			inforCard,eLine,yPie,
			safeline,listpie,extra,scbar,risk,
                    	csMessbar
		},
		data() {
			return {
                userType:'',
                showChart:false,
				compName:'',
				count: {
                    total: 0,
                    online: 0,
                    stop: 0,
                    offline: 0
				},
			};
		},
		    // computed:{
			// echData(){
			//     return this.$store.state.app.ech
			// }
		    // },
		    // watch:{
			// 	echData:function (n,o) {
			// 		this.compName = 'csMessbar'
			// 	}
		    // },
		created() {
			this.$store.commit('setCurrentPath', [{
				title: '首页'
			}])
            // let userInfoJson = sessionStorage.getItem("userInfo");
            // let userInfo = JSON.parse(userInfoJson);
            // this.userType = userInfo.type;
            // if (this.userType == 'su' || this.userType == '00'){
            //     this.showChart = true;
            //     this.getDeviceCount();
            // }
		},
		methods: {
            getDeviceCount(){
                this.$http.get(this.apis.CHART_DATA.zdcx).then((res)=>{
                    if (res.code == 200 && res.result){
                        this.count.total = parseInt(res.result['设备总数'])
                        this.count.online = parseInt(res.result['设备在线数量'])
                        this.count.stop = parseInt(res.result['设备熄火数量'])
                        this.count.offline = parseInt(res.result['设备离线数量'])
                    }
                })
            }
		}
	};
</script>
