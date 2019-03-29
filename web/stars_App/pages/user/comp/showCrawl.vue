<template>
	<view class="pagebody">
		<view class="box_col">
			<pagr-tit TiT="电子围栏"></pagr-tit>
			<view class="box_row" style="background-color: #fff;">
				<view class="box_row_100" style="padding:14upx 24upx;">
					<!-- <input type="text" v-model="wlmc" placeholder="请填写围栏名称"> -->
					{{wlmc}}
				</view>
				<view class="box_row_100" style="text-align: center;padding:14upx 24upx;">
					围栏范围
				</view>
				<view class="box_row_100" style="text-align: right;padding:14upx 24upx;">
					<!--<view class="uni-list-cell-db"> 
						<picker @change="bindPickerChange" :value="index" :range="RangeList"> -->
							<!-- <view class="uni-input"> -->
							{{Range}}米
							<!-- </view> -->
						<!-- </picker> -->
					<!-- </view> -->
				</view>
			</view>
			<view class="" style="padding:14upx 24upx;background-color: #fff;">
				位置:{{address}}
			</view>
			<view class="box_col_100" style="background-color: #00BFFF;">
				<map id="mainMap" style="width: 100%; height: 100%;" :circles="circles" :markers="covers" :latitude="latitude"
				 :longitude="longitude" :scale="scale" >
				</map>
			</view>
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
				//地图对象
				mainMap: null,
				longitude: 114.3059655840734,
				latitude: 30.591615527537712,
				//地图自定marker
				covers: [],
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
			this.Range = p.Range
			console.log(p.Range)
			this.longitude = p.lng
			console.log(p.lng)
			this.latitude = p.lat
			console.log(p.lat)
			this.wlmc = p.wlmc
			console.log(p.wlmc)
			this.address = p.address
			console.log(p.address)
			this.scale = this.zict[p.Range]
			this.setCircles()
		},
		created() {},
		mounted() {
			this.$nextTick(function() {
				// this.mainMap = uni.createMapContext("mainMap");
			})
		},
		methods: {
			setCircles(){
				this.circles = []
				this.covers = []
				this.circles = [
					{
						longitude: this.longitude,
						latitude: this.latitude,
						radius:this.Range,
						color: '#00c7ff',
						strokeWidth: 2,
						fillColor: '#00c7ff5c'
					}
				]
				this.covers = [{//markerId，指定后，可以移动指定marker的位置
						longitude: this.longitude,
						latitude: this.latitude,
						iconPath: '../../../static/img/GPSCar.png'
					}]
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
			}
		}
	}
</script>

<style>

</style>
