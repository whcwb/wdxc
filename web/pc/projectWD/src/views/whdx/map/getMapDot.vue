<!--
地图选取点位 获取坐标
-->
<style type="text/css">
	#allmap{
		height: 100%;
		width: 100%;
	}
	.claer{
		position: absolute;
	    width: 64px;
	    height: 47px;
	    top: 5px;
	    right: 57px;
	    z-index: 100;
	    background-color: rgb(255, 255, 255);
	    text-align: center;
	    line-height: 45px;
	    border: solid 1px #696666;
	    border-radius: 7px;
	}
</style>
<!--地图选点-->
<template>
	<div style="height: 100%;background-color: #00FFFF;position: relative;">
		<div id="allmap"></div>
	</div>
</template>

<script>
	export default {
		name:'getmapdot',
		data(){
			return {
				map:'',
				mapcenter:{
					lng: 114.357527,
	    			lat: 30.550822
				},
				zoom:16,
			}
		},
		created(){
			
		},
		mounted(){
			this.Buildmap()
		},
		methods:{
			Buildmap(){
				var v = this
				// 百度地图API功能
				this.map = new BMap.Map("allmap");    // 创建Map实例
				this.map.centerAndZoom(new BMap.Point(this.mapcenter.lng, this.mapcenter.lat),this.zoom);  // 初始化地图,设置中心点坐标和地图级别
				//添加地图类型控件
				this.map.addControl(new BMap.MapTypeControl({
						mapTypes:[
				            BMAP_NORMAL_MAP
				        ]
					})
				);
			    this.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
			    this.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件
			    this.map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
			    this.map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件
			    
			    this.map.addEventListener("click",function(e){
//					log(e.point.lng + "," + e.point.lat);//打印map点数据
					v.map.clearOverlays();//清楚数据点
                    v.addPoint(e.point.lng,e.point.lat);
					v.$emit('getDot',e)
				});
			},
			addPoint(lng,lat){
                this.map.clearOverlays();//清楚数据点
                let newMapDot = new BMap.Point(lng, lat);//点数据
                // var myIcon = new BMap.Icon("http://lbsyun.baidu.com/jsdemo/img/fox.gif", new BMap.Size(300,150), {anchor: new BMap.Size(130,110),});
                var marker = new BMap.Marker(newMapDot); // 创建点
                this.map.addOverlay(marker);
			}
		}
}
</script>