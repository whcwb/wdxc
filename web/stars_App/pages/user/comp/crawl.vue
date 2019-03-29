<template>
	<view>
		<web-view v-if="view" ref="viewMap" :src="'/hybrid/html/index.html?R_Type=crawlList&userID='+user_ID+'&timer='+timer"></web-view>
		<!-- <web-view v-if="view" ref="viewMap" :src="'http://192.168.123.27:8081/#/?R_Type=crawlList&userID='+user_ID+'&timer='+timer"></web-view> -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				webViewUrl:this.apis.webViewUrl,
				timer:0,
				view:false,
				user_ID:''
			};
		},
		onShow(){
			this.view = true;
			this.timer = new Date().getTime();
		},
		onHide() {
			this.view = false;
		},
		onLoad(){
			const userID = uni.getStorageSync('userID');
			if (!userID) {
				uni.showModal({
					title: '信息获取失败',
					content: '您未登录，需要登录后才能继续',
					/**
					 * 如果需要强制登录，不显示取消按钮
					 */
					showCancel: false,
					success: (res) => {
						if (res.confirm) {
							/*** 如果需要强制登录，使用reLaunch方式*/
							uni.navigateTo({
								url: '../login/login'
							});
						}
					}
				});
			}else{
				this.user_ID = userID;
			}
		},
		created() {
			view:true
		}
	}
</script>

<style>

</style>
