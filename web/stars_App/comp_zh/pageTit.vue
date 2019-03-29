<template>
	<view>
	<!-- #ifdef APP-PLUS -->
		<view class="ZWTIT"></view>
	<!-- #endif -->
	<view class="box_row pagerHeader">
		<view class="pagerLeft butWidth" @click="goback()">
			<text class="iconfont" style="font-size: 60upx;color: #fff;">&#xe6bc;</text>
		</view>
		<view class="pagerTit box_row_100">
			{{TiT}}
		</view>
		<view class="pagerRight butWidth">
			<slot name="titright">
				
			</slot>
		</view>
	</view>
	</view>
</template>

<script>
	export default {
		props:{
			backUrl:{
				type:String,
				default:''
			},
			TiT:{
				type:String,
				default:'页面标题'
			}
		},
		data() {
			return {
				
			};
		},
		methods:{
			goback(){
				if(this.backUrl==''){
					uni.navigateBack({delta:1});
				}else{//跳转到 tabBar 页面
					uni.switchTab({
						url: this.backUrl
					});
				}
			}
		}
	}
</script>

<style lang="less">
.pagerHeader{
	height: 88upx;
	// background-color: #0faeff;
	// background-color: #0faeff;
	background-color: #333333;
	.butWidth{
		width: 188upx;
	}
	.pagerLeft{
		line-height: 88upx;
		text-align: left;
		padding-left: 8px;
	}
	.pagerRight{
		line-height: 88upx!!important;
		text-align: right;
		padding-right: 8px;
	}
	.pagerTit{
		line-height: 88upx;
		text-align: center;
		color: #fff;
		font-weight: 700;
		font-size: 32upx;
	}
}
</style>
