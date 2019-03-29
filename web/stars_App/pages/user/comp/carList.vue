<template>
	<view class="pagebody" @onError="onError">
		<view class="box_col">
			<pagr-tit backUrl="/pages/user/user" TiT="车辆列表">
				<view class="box_row" slot="titright" style="position: relative;">
				<!-- #ifdef H5 -->
					<view @click="goAddCar" style="position: absolute;right: 75upx;top: -25upx;">
				<!-- #endif -->
				<!-- #ifdef APP-PLUS -->
					<view @click="goAddCar" style="position: absolute;right: 75upx;top: 20upx;">
				<!-- #endif -->
						<span class="iconfont" style="font-size: 80upx;color: #fff;">&#xe608;</span>
					</view>
				<!-- #ifdef H5 -->
					<view class="" v-if="carList.length>0" @click="remove=!remove" style="position: absolute;right: 0;top: -20upx;">
				<!-- #endif -->
				<!-- #ifdef APP-PLUS -->
					<view class="" v-if="carList.length>0" @click="remove=!remove" style="position: absolute;right: 0;top: 22upx;">
				<!-- #endif -->
						<span v-if="!remove" class="iconfont" style="font-size: 75upx;color: #fff;">&#xe624;</span>
						<span v-if="remove" class="iconfont" style="font-size: 75upx;color: #fff;">&#xe678;</span>
					</view>
				</view>
			</pagr-tit>
			<view class="box_col_100">
				<view class="" v-if="carList.length==0"  style="text-align: center;margin-top: 20%">
					<image src="../../../static/img/wsj.png" mode="aspectFit" style="width: 60%;"></image>
					<view style="font-size: 30px;font-weight: 600;color: #b3b3b3">
					  暂无车辆数据
					</view>
				 </view>
				 <view class="" v-if="carList.length>0" style="height: 100%;overflow: auto;">
					<view class="carItemBox box_row" v-for="(item,index) in carList" :key="index">
						<view class="itemIndex">
							<view class="indexSty">
								{{(index+1)}}
							</view>
						</view>
						<view @click="carMess(item)" class="box_row_100" style="height: 120upx;line-height: 120upx;font-size:35upx">
							车牌号码 : {{item.cph}}
						</view>
						<view v-if="!remove" class="" style="line-height: 120upx;">
							<span class="iconfont" style="font-size: 50upx;color: #999;">&#xe61f;</span>
						</view>
						<view v-if="remove" class="" @click="removeCar(item)"
							style="text-align: center;width: 120upx;line-height: 120upx;background-color:#ed4014 ;">
							<span class="iconfont" style="font-size: 100upx;color: #fff;">&#xe624;</span>
						</view>
					</view>
				 </view>
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
				remove:false,
				carList: [],
				params: {
					pageNum: 1,
					pageSize: 8
				}
			};
		},
		filters: {
			kong: (val) => {
				if (val) return val
				return '***'
			}
		},

		onPullDownRefresh(){
			// console.log('refresh');
			this.getCarList();
			setTimeout(function () {
				uni.stopPullDownRefresh();
			}, 1500)
		},
		created() {
		},
		mounted(){
            this.getCarList()
		},
		methods: {
			goAddCar() {
				uni.navigateTo({
					url: '/pages/main/comp/addCar'
				});
			},
			dictVal(code, val) {
				return this.dictUtil.getValByCode(this, code, val)
			},
            removeCar(item){//车辆移除
                let v = this
                this.$http.post('/app/car/unbind/'+item.zdbh).then(res => {
                    if (res.code == 200) {
                        this.getCarList()
                        uni.showToast({
                            title: res.message,
                            duration: 2000
                        });
                    }else{
                        uni.showToast({
                            icon:'none',
                            title: res.message,
                            duration: 2000
                        });
					}
                }).catch(err => {
                    console.log(err);
                })
			},
			carMess(item){
                uni.setStorageSync("carInfo", JSON.stringify(item));
				uni.navigateTo({
					 url: '/pages/user/comp/carItemMess'
				})
			},
			getCarList() {
				let v = this
				this.$http.post('/app/car/getBindCars').then(res => {
					if (res.code == 200 && res.result) {
						v.carList = res.result
					}else{
						v.carList = []
					}
                }).catch(err => {
                    console.log(err);
                })
			}
		}
	}
</script>

<style lang="less">
	.carItemBox {
		height: 120upx;
		background-color: #fff;
		border-bottom: solid #f3f3f3 6upx;

		.itemIndex {
			width: 120upx;
			.indexSty {
				text-align: center;
				line-height: 60upx;
				width: 60upx;
				height: 60upx;
				border: solid #00BFFF 4upx;
				border-radius: 34upx;
				margin: 26upx auto;
			}
		}
	}
</style>
