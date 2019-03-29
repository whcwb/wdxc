<template>
	<view class="box_col mediaCard">
			<view v-if="type=='photo'" class="box_col_100 imgSty" style="text-align: center;">
				<image class="imgSty" style="borderRadius:20px" mode="widthFix" :src="src"></image>
			</view>
			<view v-else class="box_col_100 imgSty">
				<video style="width: 100%;height: 100%;borderRadius:20px" :src="src"  controls="controls"></video>
			</view>
			<view class="text">
				<!-- {{text}} -->
			</view>
	</view>
</template>

<script>
	export default {
		props:{
			type:{
				type:String,
				default:'photo'
			},
			src:{
				type:String,
				default:''
			},
			text:{
				type:String,
				default:'文字'
			}
		},
		data() {
			return {

			};
		}
	}
</script>

<style lang="less">
	.mediaCard {
		height: 400upx;
		padding: 30upx;
		padding-bottom: 0;
		.imgSty{
			border-radius: 30rpx;
			background-repeat: no-repeat!important;
			background-position: center!important;
			background-size: 100%!important;
		}
		.text{
			height: 60upx;
			line-height: 60upx;
			font-weight: 600;
			text-align: center;
		}
	}
</style>
