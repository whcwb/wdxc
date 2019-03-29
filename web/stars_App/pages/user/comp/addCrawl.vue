<template>
	<view class="pagebody">
		<view class="box_col">
			<pagr-tit TiT="电子围栏"></pagr-tit>
			<view class="box_row" style="background-color: #fff;">
				<view class="box_row_100" style="padding:14upx 24upx;">
					<input type="text" v-model="wlmc" placeholder="请填写围栏名称">
				</view>
				<view class="box_row_100" style="text-align: center;padding:14upx 24upx;">
					围栏范围
				</view>
				<view class="box_row_100" style="text-align: right;">
					<view class="uni-list-cell-db">
						<picker @change="bindPickerChange" :value="index" :range="RangeList">
							<view class="uni-input">{{Range}}米</view>
						</picker>
					</view>
				</view>
			</view>
			<view class="" style="padding:14upx 24upx;background-color: #fff;">
				位置:{{address}}
			</view>
			<view class="box_col_100" style="background-color: #00BFFF;">
				<map id="mainMap" style="width: 100%; height: 100%;" :circles="circles" :markers="covers" :latitude="latitude"
				 :longitude="longitude" :scale="scale"  @regionchange="regionchange" @end="regend">
				 <!-- @regionchange="regionchange"   @end-->
				</map>
			</view>
			<button type="primary" style="width: 100%;" @click="setCrawlName">完 成</button>
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
				RangeList: ['500', '1000', '1500', '2000', '2500', '3000'],
				index: 0,
				zict: {
					'500': 15,
					'1000': 14,
					'1500': 13,
					'2000': 13,
					'2500': 13,
					'3000': 12
				},
				Range: 1000,
				scale: 14,
				address: null, //地图中心点的位置信息
				wlmc:'', //围栏名称
				clIds: '', //车辆
				//地图对象
				mainMap: null,
				longitude: 114.3059655840734,
				latitude: 30.591615527537712,
				//地图自定marker
				covers: [
					{//markerId，指定后，可以移动指定marker的位置
						id: 1,
						longitude: 114.3059655840734,
						latitude: 30.591615527537712,
						iconPath: '../../../static/img/GPSCar.png'
					}
				],
				//地图画圈圈
				circles: [
					{
						longitude: 114.3059655840734,
						latitude: 30.591615527537712,
						radius:1000,
						color: '#00c7ff',
						strokeWidth: 2,
						fillColor: '#00c7ff5c'
					}
				],
				getGpsCode:false,
				appRun:true
				
			};
		},
		onShow() {
		},
		onLoad(p) {
			this.clIds = p.carIds
		},
		created() {},
		mounted() {
			this.$nextTick(function() {
				this.mainMap = uni.createMapContext("mainMap");
			})
		},
		methods: {
			getGps() {
				var v = this
				uni.getLocation({
					type: 'gcj02 ',
					success: function(res) {
						v.longitude = res.longitude
						v.latitude = res.latitude
						let obj ={//markerId，指定后，可以移动指定marker的位置
							id: 1,
							longitude:res.longitude,
							latitude:res.longitude,
							iconPath: '../../../static/img/GPSCar.png'
						}
						v.covers[0] = obj
						v.circles = []
						v.covers =  []
						v.circles =[
							{
								longitude: res.longitude,
								latitude: res.latitude,
								radius: v.Range,
								color: '#00c7ff',
								strokeWidth: 2,
								fillColor: '#00c7ff5c'
							}
						]
						v.covers =  [
							{//markerId，指定后，可以移动指定marker的位置
								id: 1,
								latitude: res.latitude,
								longitude: res.longitude,
								iconPath: '../../../static/img/GPSCar.png'
							}
						]
						v.getGpsCode = true
						v.getAddress(res.longitude,res.latitude)
						console.log('当前位置的经度：' + res.longitude);
						console.log('当前位置的纬度：' + res.latitude);
					}
				});
			},
			bindPickerChange(e) {
				let val = this.RangeList[e.target.value] //取值 
				this.Range = parseInt(val) //半径赋值  整型转换
				this.scale = this.zict[val] //地图级别
				this.circles = [];
				this.circles = [{
					latitude: this.latitude,
					longitude: this.longitude,
					radius: this.Range,
					color: '#00c7ff',
					strokeWidth: 2,
					fillColor: '#00c7ff5c'
				}];
			},
			setCrawlName() {
				var v = this
				if (v.wlmc == '') {
					console.log('蒙城叹气')
					uni.showToast({
						title: '给围栏起个名字吧',
						icon:'none',
						duration: 1500
					});
				} else {
					v.goCrawlList()
				}
			},
			goCrawlList() {
				this.$http.post('/app/dzwl/save', {
					wlmc: this.wlmc,
					ksjd: this.longitude,
					kswd: this.latitude,
					wlfw: this.Range,
					dlxxzb: this.address,
					clIds: this.clIds
				}).then(res => {
					if (res.code == 200) {
						uni.showToast({
							title: '围栏添加成功',
							duration: 2000
						});
						uni.navigateBack();
					}
				}).catch(err => {})
			},
			regionchange(event) {
				console.log('ppppppppppppppppppppppppp')
				let self = this;
				//获取地图中心点
				if(self.getGpsCode){
					this.mainMap.getCenterLocation({
						success: function(res) {
							console.log('中心點',res.latitude)
							console.log(res.latitude)
							console.log(res.longitude)
							self.latitude = res.latitude,
							self.longitude = res.longitude
							self.circles = [];
							//移动指定markerId对象
							self.mainMap.translateMarker({
								markerId: 1,
								autoRotate: true,
								rotate: 270,
								destination: {
									latitude: res.latitude,
									longitude: res.longitude
								},
								autoRotate: false,
								rotate: 0
							});
							self.circles = [{
								latitude: res.latitude,
								longitude: res.longitude,
								radius: self.Range,
								color: '#00c7ff',
								strokeWidth: 2,
								fillColor: '#00c7ff5c'
							}];
							self.getAddress(res.longitude, res.latitude)
						},
						fail: function(res) {
							console.log("fail", res);
						}
					});
				}else{
					self.getGps()
				}
			},
			regend(event) {
				let self = this;
				if(!self.appRun){
					return
				}
				console.log('qqqqqqqqqqqqqqqqqqqqqqqqqq')
				//获取地图中心点
				if(self.getGpsCode){
					self.appRun = false
					this.mainMap.getCenterLocation({
						success: function(res) {
							console.log('中心點',res.latitude)
							console.log(res.latitude)
							console.log(res.longitude)
							self.latitude = res.latitude,
							self.longitude = res.longitude
							self.circles = [];
							self.covers = []
							//移动指定markerId对象
// 							self.mainMap.translateMarker({
// 								markerId: 1,
// 								autoRotate: true,
// 								rotate: 270,
// 								destination: {
// 									latitude: res.latitude,
// 									longitude: res.longitude
// 								},
// 								autoRotate: false,
// 								rotate: 0
// 							});
							self.circles = [{
								latitude: res.latitude,
								longitude: res.longitude,
								radius: self.Range,
								color: '#00c7ff',
								strokeWidth: 2,
								fillColor: '#00c7ff5c'
							}];
							self.covers =  [
								{//markerId，指定后，可以移动指定marker的位置
									id: 1,
									latitude: res.latitude,
									longitude: res.longitude,
									iconPath: '../../../static/img/GPSCar.png'
								}
							]
							self.getAddress(res.longitude, res.latitude)
						},
						fail: function(res) {
							console.log("fail", res);
						}
					});
				}else{
					self.appRun = false
					self.getGps()
				}
			},
			getAddress(lng, lat) {
				var v = this
				this.$http.post('/geo/getAddress',{lng:lng,lat:lat,type:'gcj02',area:'1'}).then(res=>{
					console.log(res.message)
					v.address = res.message
					setTimeout(()=>{
						v.appRun = true
					},50)
				}).catch(err=>{})
			}
		}
	}
</script>

<style>

</style>
