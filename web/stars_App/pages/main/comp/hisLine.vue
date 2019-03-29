<template>
	<view class="pagebody indexPager">
		<view class="box_col">
			<!-- #ifdef APP-PLUS -->
			<!-- <view class="ZWTIT"></view> -->
			<!-- #endif -->
			<pagr-tit backUrl="/pages/trajectory/index" TiT="历史轨迹"></pagr-tit>
			
			<view id="mapBox" class="box_col_auto" style="background-color:#000;">
				<map id="mainMap" class="map" style="height:100%;width:100%"
				 :latitude="latitude" :longitude="longitude" :scale="scale"
				 :include-points="includePoints"
				 :markers="covers" :polyline="polyline"></map>
			</view>
			<view class="playBut">
				<view class="box_row" style="text-align: center;background-color: #fff;padding: 12upx 0;">
					<view class="box_row_100">
						<image src="../../../static/img/kuiatui.png" mode="widthFix" style="width: 34%;" @click="kuiatui"></image>
					</view>
					<view class="box_row_100">
						<image v-if="playShow" src="../../../static/img/S_play.png" mode="widthFix" style="width: 34%;" @click="playVideo"></image>
						<image v-if="!playShow" src="../../../static/img/E_play.png" mode="widthFix" style="width: 34%;" @click="stopVideo"></image>
					</view>
					<view class="box_row_100">
						<image src="../../../static/img/kuaiin.png" mode="widthFix" style="width: 34%;" @click="kuaijin"></image>
					</view>
				</view>
			</view>
			<!-- <view class="box_col_100" style="background-color: #00BFFF;">
				<map id="mainMap" style="width: 100%; height: 100%;" :markers="covers" :latitude="latitude"
				 :longitude="longitude" :scale="scale" >
				</map>
			</view> -->
		</view>
	</view>
</template>

<script>
	import pagrTit from '../../../comp_zh/pageTit.vue'
	export default {
		components: {
			pagrTit
		},
		data() {
			return {
				mainMap:null,
				mapShow:false,
				longitude: 114.29223536356,
				latitude: 30.631752510089,
				scale: 11,
				covers: [
					{
						longitude: 0,
						latitude: 0,
						iconPath: ''
					},
					{
						longitude: 0,
						latitude: 0,
						iconPath: ''
					},
					{
						id:2,
						rotate:0,
						longitude: 0,
						latitude: 0,
						iconPath: '../../../static/img/GPSCar.png'
					}
				],
				lineCodeList:[],
				polyline: [
					{
						points: [], //数据点
						// color: '#0e87da',
						color: '#19be6b',
						width: 14
					}
				],
				includePoints:[],
				params: {
					startTime: '',
					endTime: '',
					zdbh: ''
				},
				valit:1,
				playTimes:8,
				platTime:200,
				playShow:true,
				setIntervalName:null
			}
		},
		// forcedLogin vuex  判断是否登录
		onShow(){
		},
		onLoad(p) {
			this.params.startTime = p.startTime
			this.params.endTime = p.endTime
			this.params.zdbh = p.zdbh
			
			this.mainMap = uni.createMapContext("mainMap");
		},
		created() {
		},
		mounted(){
			var v = this
			this.$nextTick(function(){
				uni.getLocation({
					type: 'wgs84',
					success: function (res) {
						console.log('当前位置的经度：' + res.longitude);
						console.log('当前位置的纬度：' + res.latitude);
						v.longitude= res.longitude;
						v.latitude= res.latitude;
						v.getLineCode()
					}
				});
			})
		},		
		methods: {
			getLineCode() { //获取轨迹数据
// 				if (this.params == null) {
// 					return
// 				}
				var v = this
				uni.showLoading({
					title: '轨迹加载中……'
				});
				this.$http.post('/app/device/yyguiji', this.params).then(res => {
					console.log('轨迹数据'+parseInt(res.message))
					uni.hideLoading();
					let mes = res.message
					try{
						if(parseInt(mes)!==NaN){
							this.scale = mes
						}
					}catch(e){
						//TODO handle the exception
					}
					if (res.code == 200) {
						
						let listCode = []
						res.result.forEach((it,index)=>{
							listCode.push(v.bd09togcj02(it.longitude,it.latitude))
							if(index == 0){
								v.longitude = listCode[0].longitude
								v.latitude = listCode[0].latitude
								// 起始点								
								v.covers[0].longitude = listCode[0].longitude
								v.covers[0].latitude = listCode[0].latitude
								v.covers[0].iconPath = '../../../static/img/star.png'
								
							}
							if(index==res.result.length-1){
								v.polyline[0].points = listCode
								v.lineCodeList = listCode
								console.log('数据', listCode)
								v.mainMap.includePoints({points:listCode})
								
								v.includePoints = listCode//可视化地图级别
								// 终点
								v.covers[1].longitude = listCode[listCode.length-1].longitude
								v.covers[1].latitude = listCode[listCode.length-1].latitude
								v.covers[1].iconPath = '../../../static/img/end.png'
								
								console.log(v.polyline[0].points)
							}
						})
					}
				}).catch(err => {})
			},
			bd09togcj02(bd_lon, bd_lat) {
				var x_pi = 3.14159265358979324 * 3000.0 / 180.0;
				var x = bd_lon - 0.0065;
				var y = bd_lat - 0.006;
				var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
				var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
				var gg_lng = z * Math.cos(theta);
				var gg_lat = z * Math.sin(theta);
				return {
					longitude:gg_lng,
					latitude:gg_lat
				}
			},
			gcj02tobd09(lng, lat, callback) {
				var x_PI = 3.14159265358979324 * 3000.0 / 180.0;
				var PI = 3.1415926535897932384626;
				var a = 6378245.0;
				var ee = 0.00669342162296594323;


				var z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
				var theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
				var bd_lng = z * Math.cos(theta) + 0.0065;
				var bd_lat = z * Math.sin(theta) + 0.006;
				return [bd_lng, bd_lat]
				// callback && callback([bd_lng, bd_lat])
			},
			
			playVideo(){
				var v = this
				v.playShow = false
				v.covers[2].longitude = v.lineCodeList[0].longitude
				v.covers[2].latitude = v.lineCodeList[0].latitude
				v.covers[2].iconPath = '../../../static/img/GPSCar.png'
				
				v.animation()
			},
			animation(){
				var v = this
				clearInterval(this.setIntervalName)
				this.setIntervalName = null
				
				this.setIntervalName = setInterval(()=>{
					console.log(v.valit)
					v.mainMap.translateMarker({
						markerId:2,
						autoRotate:true,
						rotate:90,
						destination:{
							longitude:v.lineCodeList[v.valit].longitude,
							latitude:v.lineCodeList[v.valit].latitude
						},
					})
					// v.covers[2].longitude = v.lineCodeList[v.valit].longitude
					// v.covers[2].latitude = v.lineCodeList[v.valit].latitude
					// v.covers[2].iconPath = '../../../static/img/GPSCar.png'
					
					if(v.valit == v.lineCodeList.length-1){
						v.valit=0
						clearInterval(this.setIntervalName)
						this.setIntervalName = null
					}else{
						v.valit++
					}
				},this.platTime*this.playTimes)
			},
			stopVideo(){
				clearInterval(this.setIntervalName)
				this.setIntervalName = null
				this.playShow = true
			},
			kuiatui(){
				this.playTimes++
				this.animation()
			},
			kuaijin(){
				console.log(this.playTimes)
				if(this.playTimes==1){
				 return
				}
				this.playTimes--
				this.animation()
			},
			getUrlParams(name) {
				var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
				let href = window.location.href;
				var r = href.substr(href.indexOf('?') + 1).match(reg);
				if (r != null) return unescape(r[2]);
				return null;
// 				var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
// 				var r = window.location.search.substr(1).match(reg);
// 				if (r != null) return unescape(r[2]);
// 				return null;
			  }
		}
	}
</script>

<style lang="less">
	.indexPager {
		.playBut{
			height: 120upx;
		}
	}
</style>
