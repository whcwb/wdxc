<template>
	<view class="box_row cardLine">
		<view class="letfIco">
			<slot name="icon"></slot>
		</view>
		<view class="cardMes box_row_100">
			<slot></slot>
		</view>
		<view class="right">
			<view class="box_row">
				<view class="box_row_100 fontSty">
					{{rigthTit}}
				</view>
				<view class="box_row_100">
					<uni-icon type="forward" size=40 color="#b1b1b1" @click="rightClick"></uni-icon>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import uniIcon from "@/components/uni-icon.vue"
	export default {
		props: {
			rigthTit: String, //右侧文字
		},
		components: {
			uniIcon
		},
		data() {
			return {

			};
		},
		methods: {
			rightClick() {
				this.$emit('RClick')
			}
		}
	}
</script>

<style lang="less">
	.cardLine {
		height: 80upx;
		padding: 15upx 24upx;
		background: #fff;
		.letfIco {
			text-align: center;
			width: 80upx;
		}

		.cardMes {
			padding-left: 15upx;
			line-height: 80upx;
			font-size: 32upx;
			color: #7b7676;
			font-weight: 600;
		}

		.right {
			.fontSty {
				font-size: 26upx;
				font-weight: 600;
				line-height: 80upx;
			}
		}
	}
</style>
