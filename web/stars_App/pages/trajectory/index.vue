<template>
	<view class="pagebody">
		<!-- #ifdef APP-PLUS -->
		<view class="ZWTIT"></view>
		<!-- #endif -->
		<web-view v-if="view" ref="viewMap" :src="'/hybrid/html/index.html?R_Type=trajectory&userID='+user_ID+'&timer='+timer"></web-view>
		<!-- <web-view v-if="view" ref="viewMap" :src="'http://192.168.123.27:8081/#/?R_Type=trajectory&userID='+user_ID+'&timer='+timer"></web-view> -->

	</view>
</template>

<script>
	export default {
		data() {
			return {
				timer: Date.parse(new Date()),
				view: false,
				user_ID: '',
				
			};
		},
		onShow() {
			this.view = true;
		},
		onHide() {
			this.view = false;
		},
		onLoad() {
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
			} else {
				this.user_ID = userID;
			}
		},
		created() {
			view: true
		},
		methods:{
			bindChange: function (e) {
				const val = e.detail.value
				this.year = this.years[val[0]]
				this.month = this.months[val[1]]
				this.day = this.days[val[2]]
			}
		}
	}
</script>

<style>

</style>
