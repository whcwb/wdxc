<style lang="less">
	.mappage{
		height: 100%;
		background-color: #00FFFF;
		position: relative;
		.backSty{
			position: absolute;
			left: 10px;
			top: 10px;
			z-index: 100;
			i{
				font-size: 25px;
			}
		}
		#allmap{
			height: 100%;
			width: 100%;
		}
	}
</style>
<template>
	<div class="mappage">
		<div class="backSty" @click="back">
			<i class="iconfont icon-back"></i>
		</div>
		<div id="allmap"></div>
	</div>
</template>

<script>
	export default {
		name:'',
		data(){
			return{
				map:'',
				mapcenter:{
					lng: 114.372443,
	    			lat: 30.544572
				},
				zoom:16,
				mess:[
					{
						lng: 114.3724431,
	    				lat: 30.544572
					},
					{
						lng: 114.3725432,
	    				lat: 30.564572
					}
				]
			}
		},
		created(){
			
		},
		mounted(){
			var v = this
			// 百度地图API功能
			this.map = new BMap.Map("allmap");    // 创建Map实例
			this.map.centerAndZoom(new BMap.Point(this.mapcenter.lng, this.mapcenter.lat),this.zoom);  // 初始化地图,设置中心点坐标和地图级别
			//添加地图类型控件
			this.map.addControl(new BMap.MapTypeControl({
				mapTypes:[
		            BMAP_NORMAL_MAP
//		            BMAP_HYBRID_MAP
		        ]}));	  

			this.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
		    this.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件
		    this.map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
		    this.map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件
		  	
		  	this.areaF();
			
			this.messPoint(this.mess);
			
			this.polyline()
		},
		methods:{
			back(){
				this.$router.push({
					name:'lineMess'
				})
			}
		}
	}
</script>

<style>
</style>