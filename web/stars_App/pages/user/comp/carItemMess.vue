<template>
	<view class="pagebody" style="background-color: #efeff4;">
		<view class="box_col">
			<pagr-tit TiT="我的车辆">
			</pagr-tit>
			<view class="boxTit">
				车量信息
			</view>
			<view class="box_row carNum">
				<view class="tit">
					车牌号码:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.cph" confirm-type="search" disabled="disabled" @confirm="confirm" class="input" type="text" placeholder="输入车牌号码" />
				</view>
			</view>
			<view class="box_row carNum">
				<view class="tit">
					车架号码:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.cjh" confirm-type="search" disabled="disabled" @confirm="confirm" class="input" type="text" placeholder="输入车架号码" />
				</view>
			</view>
			<view class="box_row carNum">
				<view class="tit">
					发动机号:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.fdjh" confirm-type="search" @confirm="confirm" disabled="disabled" class="input" type="text" placeholder="输入发动机号" />
				</view>
			</view>
			<view class="box_row carNum">
				<view class="tit">
					注册日期:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.zcrq" confirm-type="search" @confirm="confirm"  disabled="disabled" class="input" type="text" placeholder="输入注册日期" />
				</view>
			</view>
			<view class="box_row carNum">
				<view class="tit">
					品牌型号:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.xh" confirm-type="search"  disabled="disabled" @confirm="confirm" class="input" type="text" placeholder="输入品牌型号" />
				</view>
			</view>


			<view class="boxTit">
				终端绑定
			</view>
			<view class="box_row carNum">
				<view class="tit">
					终端编号:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.zdbh" :disabled="disabledZdbh"  disabled="disabled" confirm-type="search" @confirm="confirm" class="input" type="text"
					 placeholder="请输入终端编号" />
				</view>
			</view>
			<view class="box_row carNum">
				<view class="tit">
					设备状态:
				</view>
				<view class="box_row_100 addInput">
					{{getState(carInfo.zt)}}
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
                carInfo:{
                    cph:'',
                    cjh:'',
                    zdbh:'',
                    zcrq:'',
                    fdjh:'',
                    xh:'',
                    ccdjrq:'',
                }
			};
		},
		mounted(){
		  this.carInfo = JSON.parse(uni.getStorageSync("carInfo"));
		  this.carInfo.zcrq = this.carInfo.ccdjrq.substring(0,10);
		},
		methods: {
		    getState(s){
		        switch (s) {
					case '00':return '在线';
					case '10':return '熄火';
					default:return '离线';
                }
			}
		}
	}
</script>

<style lang="less">
	.boxTit {
		color: #00BFFF;
		font-size: 28upx;
		padding: 12upx 12upx;
	}
	.carNum {
		background-color: #fff;
		height: 100upx;
		border-bottom: solid 4upx #f3f3f3;
		.tit {
			line-height: 100upx;
			font-size: 34upx;
			width: 160upx;
			text-align: right;
			padding-right: 20upx;
		}

		.addInput {
			margin: auto;
			text-align: right;
			padding-right: 36upx;
		}
	}

	.saveBut {
		button {
			font-size: 18upx;
		}
	}
</style>
