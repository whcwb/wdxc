<template>
	<view class="pagebody" style="background: #74809c;">
		<view class="box_col">
			<media-card  v-for="(item,index) in list" :key="index" :type="'photo'" :src="item.url"></media-card>
		</view>
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
				imgPath:'http://www.168car.net:9091/test/',
				list:[]
			};
		},
		created(){
			this.getData();
		},
		methods:{
				getData(){
					console.log(this.apis)
						this.$http.get('/app/device/photoList').then(res=>{
							if(res.code == 200){
								this.list = res.page.list;
								for (let r of this.list){
									r.url = this.apis.videoPath + r.url.replace(/\\/g,"/");
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

<style lang="less">
</style>
