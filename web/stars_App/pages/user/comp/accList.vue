<template>
	<view class="pagebody">
		<view class="box_col">
			<pagr-tit backUrl="/pages/user/user" TiT="事故列表"></pagr-tit>
			<check-card :checkList="checkList"></check-card>
			<view class="box_col_auto">
				
			</view>
		</view>
	</view>
</template>

<script>
import pagrTit from '../../../comp_zh/pageTit.vue'
import checkCard from '../../../comp_zh/checkCard.vue'
	export default {
		components:{pagrTit,checkCard},
		data() {
			return {
				checkList:[
					{
						key: '01',
						val: '全部'
					}, {
						key: '02',
						val: '未处理'
					}, {
						key: '03',
						val: '已处理'
					}
				]
			};
		}
	}
</script>

<style>

</style>
