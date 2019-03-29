<template>
	<view class="content">
		<view class="" style="text-align: center;margin-top: 100upx;">
			<image src="../../static/img/logo.png" mode="aspectFit" style="width: 200upx;height: 200upx;"></image>
		</view>
		<view class="input-group inputSty">
			<view class="input-row ">
				<!-- <text class="title">手机号码：</text> -->
				<view class="title" style="position: relative;">
					<image src="../../static/img/zh.png" mode="aspectFit"
					 style="width: 55upx;height: 55upx;"></image>
				</view>
				<input type="text" v-model="user.userName" placeholder="请输入手机号码">
			</view>
		</view>
		<view class="input-group inputSty">
			<view class="input-row" style="position: relative;">
				<!-- <text class="title">登录密码：</text> -->
				<view class="title" style="position: relative;">
					<image src="../../static/img/mm.png" mode="aspectFit"
					 style="width: 55upx;height: 55upx;"></image>
				</view>
				<input type="text" password="true" v-model="user.password" placeholder="请设置密码">
			</view>
		</view>
		
		<view class="input-group inputSty">
			<view class="input-row border" style="position: relative;">
				<!-- <text class="title">验证码：</text> -->
				<view class="title" style="position: relative;">
					<image src="../../static/img/yzm.png" mode="aspectFit"
					 style="width: 55upx;height: 55upx;"></image>
				</view>
				<input type="text" v-model="user.password1" placeholder="请输入验证码">
				<!-- <button size="mini" type="warn" @click="">获取短信验证码</button> -->
				<view v-if="times == 60" class="regTag" @click="getCode">获取验证码</view>
				<view v-else-if="times != 60" class="regTag regTagOK">已发送({{times}}s)</view>
			</view>
		</view>
		
		
		<view class="btn-row loginBut">
			<button type="primary" size="small" class="primary" @tap="register">注册</button>
		</view>
	</view>
</template>

<script>
	import service from '../../service.js';
	import uniTag from "@/components/uni-tag.vue"
	import {
		mapMutations
	} from 'vuex'
	export default {
		components: {
			uniTag
		},
		data() {
			return {
				times: 60,
				timeInterval: null,
				user: {
					userName: '',
					password: '',
					password1: ''
				},
				account: '',
				password: '',
			}
		},
		methods: {
			...mapMutations(['setUser']),
			getCode() {
				this.timeInterval = setInterval(() => {
					this.times--
				}, 1000)
				setTimeout(() => {
					this.user.password1 = this.user.password
				}, 10 * 1000)
				setTimeout(() => {
					clearInterval(this.timeInterval)
					this.times = 60
				}, 60500)
			},
			showCity() {
				uni.showToast({
					title: '选择城市'
				})
			},
			scan() {
				uni.showToast({
					title: '扫码'
				})
			},
			getMessCode() {

			},

			register() {
				if (this.user.userName == '' || this.user.password == '' || this.user.password1 == '') {
					uni.showToast({
						title: '请将信息填写完整！！！',
						icon: 'none',
						duration: 2000
					});
					return
				}
				if (this.user.password != this.user.password1) {
					uni.showToast({
						title: '两次填写的密码不一致！！！',
						icon: 'none',
						duration: 2000
					});
					return
				}

				this.$http.post(this.apis.REGIST, this.user).then(res => {
					if (res.code == 200) {
						uni.showModal({
							// title: '用户注册成功,请重新登录。',
							content: '用户注册成功,请重新登录。',
							confirmText: '',
							cancelText: '登录',
							success: function(res) {
								if (res.confirm) {
									console.log('用户点击确定');
								} else if (res.cancel) {
									uni.redirectTo({
										url: '/pages/login/login'
									});
									console.log('用户点击取消');
								}
							}
						});
						this.setUser(this.user.userName)
					} else {
						uni.showToast({
							title: res.message,
							icon: 'none',
							duration: 2000
						});
					}
				}).catch(err => {})
				/**
				 * 客户端对账号信息进行一些必要的校验。
				 * 实际开发中，根据业务需要进行处理，这里仅做示例。
				 */
			}
		}
	}
</script>

<style lang="less">
	.inputSty{
		margin: 2upx 60upx 20upx;
		border: #dbdbdf 1px solid;
	}
	.inputIcon{
		position: absolute;
		z-index: 100;
	}
	.loginBut{
		margin: 20upx 40upx;
	}
	.regTag {
		position: absolute;
		right: 0upx;
		top: 0upx;
		bottom: 0upx;
		width: 200upx;
		background-color: #0faeff;
		color: #fff;
		text-align: center;
		line-height: 70upx;
		padding: 5upx 3upx;
		// border-radius: 50upx;
	}

	.regTagOK {
		background-color: #47cb89 !important;
	}

	.butGetMessode {
		position: absolute;
		right: 10upx;
		top: 14upx;
	}

	.uniNavBar {
		.title {
			font-size: 15upx;
			line-height: 20upx;
			color: #333333;
			padding: 15upx;
		}

		.city {
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: center;
			width: 100%;
			margin-left: 8upx;
		}

		.input-view {
			width: 92%;
			display: flex;
			background-color: #e7e7e7;
			height: 30upx;
			border-radius: 15upx;
			padding: 0 4%;
			flex-wrap: nowrap;
			margin: 7upx 0;
			line-height: 30upx;
		}

		.input-view .uni-icon {
			line-height: 30upx !important;
		}

		.input-view .input {
			height: 30upx;
			line-height: 30upx;
			width: 94%;
			padding: 0 3%;
		}
	}
</style>
