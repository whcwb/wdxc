<template>
	<view class="checkCardSty box_row">
		<view :class="index==value ? checkHover:checkItem" v-for="(item,index) in checkList" :key="index" @click="check(item,index)">
			{{item.val}}
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			checkList:[]
		},
		data() {
			return {
				value:0,
				checkHover:'box_col_100 checkItem checkHover',
				checkItem:'box_col_100 checkItem'
			};
		},
		methods:{
			check(it,index){
				this.value = index
				this.$emit('checkItem',{it,index})
			}
		}
	}
</script>

<style lang="less">
	.checkCardSty {
		height: 88upx;
		background-color: #fff;
		color: #000;
		font-weight: 600;
		font-size: 34upx;
		.checkItem{
			text-align: center;
			line-height: 88upx;
		}
		.checkHover{
			color: #0faeff;
			border-bottom:solid #0faeff 2upx ;
		}
	}
</style>
