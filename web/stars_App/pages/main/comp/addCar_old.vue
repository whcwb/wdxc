<template>
	<view class="pagebody" style="background-color: #f2f2f2;">
		<view class="box_col">
			<pagr-tit TiT="终端绑定">
				<view class="" slot="titright" @click="scan">
					<text @click="scan" class="iconfont" style="font-size: 60upx;color: #fff;">&#xe615;</text>
				</view>
			</pagr-tit>

			<view class="box_row carNum">
				<view class="tit">
					终端编号:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="zdNum" :disabled="disabledZdbh" confirm-type="search" @confirm="confirm" class="input" type="text"
					 placeholder="请输入终端编号" />
				</view>
			</view>
			<view v-if="showCph" class="box_row carNum">
				<view class="tit">
					车牌号:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="cph" confirm-type="search" @confirm="confirm" class="input" type="text" placeholder="输入车牌号" />
				</view>
			</view>
			<view class="" style="padding: 20upx 30upx;">
				<button style="background-color: #00BFFF;color: #fff;" @click="confirm">确定</button>
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
				selected: '1',
				zdNum: '',
				cph: '',
				showCph: false,
				disabledZdbh: false,
				addCarApi: false,
			};
		},
		methods: {
			scan() {
				var v = this
				console.log('123')
				uni.scanCode({
					onlyFromCamera: true,
					success: function(res) {
						console.log('条码类型：' + res.scanType);
						console.log('条码内容：' + res.result);
						v.zdNum = res.result
					}
				});
			},
			confirm() {
				if (this.addCarApi) {
					this.addCar();
				} else {
					this.addZD()
				}
			},
			addCar() {
				console.log('终端绑定')
				let v = this;
				this.$http.post(this.apis.ADD_CAR, {
					zdbh: this.zdNum,
					cph: this.cph
				}).then(res => {
					if (res.code == 200) {
						uni.showToast({
							title: '终端绑定成功',
							icon: 'success',
							duration: 2000
						});
						v.addCarApi = false;
						v.zdNum = ''
						v.cph = ''
					} else {
						uni.showToast({
							title: res.message,
							icon: 'none',
							duration: 2000
						});
					}
				}).catch(err => {})
			},
			addZD() {
				let v = this;
				this.$http.post(this.apis.ADDZD, {
					deviceId: this.zdNum
				}).then(res => {
					if (res.code == 200 && res.result) {
						if (res.result === 'notBind') {
							uni.showToast({
								icon:'none',
								title:'该终端未绑定车辆，请绑定车辆？',
								duration: 2000
							});
							// uni.showModal({
								// title: '该终端未绑定车辆，是否新增车辆？'
// 								success: function(res) {
// 									console.log('中毒4')
// 									if (res.confirm) {
// 										v.showCph = true;
// 										v.disabledZdbh = true;
// 										v.addCarApi = true;
// 									} else if (res.cancel) {}
// 								}
							// });
							v.showCph = true;
							v.disabledZdbh = true;
							v.addCarApi = true;
						} else {
							uni.showToast({
								title: '终端绑定成功',
								icon: 'success',
								duration: 2000
							});
							v.zdNum = ''
						}
					} else {
						uni.showToast({
							title: res.message,
							icon: 'none',
							duration: 2000
						});
					}
				}).catch(err => {})
			}
		}
	}
</script>

<style lang="less">
	.carNum {
		background-color: #fff;
		height: 80upx;
		margin: 30upx 0;

		.tit {
			line-height: 80upx;
			font-size: 30upx;
			width: 160upx;
			text-align: right;
			padding-right: 20upx;
		}

		.addInput {
			margin: auto;
		}
	}

	.saveBut {
		button {
			font-size: 18upx;
		}
	}
</style>
