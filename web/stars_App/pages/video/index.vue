<template>
	<view class="pagebody" style="background: #74809c;">
		<view class="box_col">
			<view class="box_col_auto">
				<media-card  v-for="(item,index) in list" :key="index" :src="item.url" :type="'video'"></media-card>
			</view>
			
		</view>
		<!-- <navigator url="navigate/navigate?title=navigate" hover-class="navigator-hover">
                    <button type="default">跳转到新页面</button>
                </navigator>
                <navigator url="redirect/redirect?title=redirect" redirect hover-class="other-navigator-hover">
                    <button type="default">在当前页打开</button>
                </navigator> -->
	</view>
</template>

<script>
	import mediaCard from "../../comp_zh/mediaCard.vue"
	export default {
		components:{
			mediaCard
		},
		data() {
			return {
				imgPath:'http://www.168car.net:9092/test/',
				list:[]
			};
		},
		created(){
			this.getData();
		},
		methods:{
				getData(){
					console.log(this.apis)
						this.$http.get('/app/device/videoList').then(res=>{
							if(res.code == 200){
								this.list = res.page.list;
								for (let r of this.list){
									r.url = this.apis.videoPath + r.url;
								}
							}else{
								uni.showToast({
									title: res.message,
									duration: 2000
								});
							}
						}).catch(err=>{})
				}
		}
	}
</script>

<style>

</style>
