<template>
	<view class="pagebody indexPager">
		<view id="mapBox" class="box_col">
			<web-view  ref="viewMap" :src="'/hybrid/html/index.html?userID='+user_ID+'&R_Type=home&timer='+timer"></web-view>
			<!-- <web-view v-if="view" ref="viewMap" :src="'http://192.168.123.27:8081/#/?userID='+user_ID+'&R_Type=home&timer='+timer"></web-view> -->
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex'
	export default {
		components: {},
		data() {
			return {
				timer:Date.parse(new Date()),
				GpsCode: "",
				GpsCodeLng:'',
				GpsCodeLat:'',
				GpsCodeLnga:'',
				GpsCodeLata:'',
				ZDlist: [],
				user_ID:'',
				webSrc:'',
				view:false
			}
		},
		// computed: 'mapState(['userID'])',
		onShow(){
			this.view = true;
		},
		onHide() {
			this.view = false;
		},
		onLoad(){
			const userID = uni.getStorageSync('userID');
			if (!userID) {
				uni.showModal({
					title: '信息获取失败',
					content: '您未登录，需要登录后才能继续',
					/**
					 * 如果需要强制登录，不显示取消按钮
					 */
					showCancel: false,
					success: (res) => {
						if (res.confirm) {
							/*** 如果需要强制登录，使用reLaunch方式*/
							uni.navigateTo({
								url: '../login/login'
							});
						}
					}
				});
			}else{
				this.user_ID = userID;
			}
		},
		created(){
			
		},
		mounted() {
			this.$nextTick(function(){
			})
		},
		methods: {
			handleMessage(evt) {
				console.log('接收到的消息：' + evt);
			},
			showHis() {
				try {
					uni.setStorageSync('zdbh', '101601190166');
					uni.navigateTo({
						url: '/pages/main/comp/hisLine?zdbh=101601190166'
					});
					console.log('终端编号存储');
				} catch (e) {
					// error
				}
			},
			zdEvent(item, type) {
				if (type === 'photo') {
					this.$http.post('/app/device/takePhoto', {
						deviceId: item.zdbh,
						cmd: '1-10'
					}).then(res => {
						if (res.code == 200) {
							uni.showToast({
								title: '指令下发成功',
								duration: 2000
							});
						} else {
							uni.showToast({
								title: res.message,
								duration: 2000
							});
						}
					}).catch(err => {})
				} else if (type === 'video') {
					this.$http.post('/app/device/takeVideo', {
						deviceId: item.zdbh,
						cmd: '1-10'
					}).then(res => {
						if (res.code == 200) {
							uni.showToast({
								title: '指令下发成功',
								duration: 2000
							});
						} else {
							uni.showToast({
								title: res.message,
								duration: 2000
							});
						}
					}).catch(err => {})
				}
			},
			goAddCar() {
				uni.navigateTo({
					url: '/pages/main/comp/addCar'
				});
			},
			swiperChange(res) {
				this.swiperCurrent = res.detail.current
			}
		}
	}
</script>

<style lang="less">
	.mainTiT {
		position: absolute;
		z-index: 99999999999 ! !important;
		width: 100%;
	}

	.indexPager {
		position: relative;

		.carCard {
			height: 300upx;
			color: #000;
			border-radius: 22upx 22upx 0 0;
			padding: 30upx;
			background-color: #FFF;
			border-bottom: solid #f4f4f4 1upx;
			z-index: 9999;

			.card {
				height: 100%;
				width: 100%;
				border-radius: 24upx;

				.cartyp {
					text-align: center;

					.textPosi {
						transform: translateY(-25upx);
						color: #c3c3c3;
					}
				}
			}
		}

		.mainTit {
			// background-color: #007AFF;
			background-color: #fff;
			height: 100upx;

			.findInput {
				background-color: #f3f3f3;
				// height: 70upx;
				border-radius: 35upx;
				margin: auto 16upx;
				padding: 10upx 16px;

				.input {
					margin: auto 0;
					padding-left: 15upx;
				}
			}

			.rightBut {
				width: 100upx;
				text-align: center;
				line-height: 100upx;
			}

			.letfBut {
				width: 100upx;
				text-align: center;
				line-height: 100upx;
			}
		}

		.title {
			font-size: 15upx;
			line-height: 20upx;
			color: #333333;
			padding: 15upx;
		}

		.city {
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: center;
			width: 100%;
			margin-left: 8px;
		}

		.main-input-view {
			width: 92%;
			display: flex;
			background-color: #e7e7e7;
			height: 30upx;
			border-radius: 15upx;
			padding: 0 4%;
			flex-wrap: nowrap;
			margin: 7px 0;
			line-height: 30upx;
		}

		.main-input-view .uni-icon {
			line-height: 30upx !important;
		}

		.main-input-view .input {
			height: 40upx;
			line-height: 40upx;
			width: 94%;
			padding: 0 3%;
		}
	}
</style>
