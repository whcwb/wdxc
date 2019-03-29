<template>
	<view class="pagebody box_col" style="background-color: #efeff4;">
		<view class="userTit box_row marB_MX">
			<view class="userIcon">
				<view class="" style="width: 160upx;margin: auto;border-radius: 4px;">
					<image :src="userMess.himg" style="height: 160upx;width: 160upx;border-radius: 80upx;"  mode="scaleToFill"></image>
				</view>
			</view>
			<view class="box_row_100 userMess">
				<view class="userName">
					{{userMess.xm}}
				</view>
				<!-- <view class="userType">
					签名
				</view> -->
			</view>
			<view class=""  @click="RClick('/pages/user/comp/info')"
			style="line-height: 200upx;padding-right: 10px;">
				<uni-icon type="forward" size=40 color="#b1b1b1"></uni-icon>
			</view>
		</view>
		<view class="box_col_auto">
			<view class="marB_MX"  @click="RClick('/pages/user/comp/messList')">
				<card-line>
					<view slot="icon" style="padding-top: 10upx;"> <uni-icon type="email" size=32 color="#1c8edd"></uni-icon></view>
					消息通知
				</card-line>
			</view>

			<!-- <view class="marB_MIN">
				<card-line>
					<view slot="icon">
						<text class="iconfont" style="font-size: 50upx;color: #31b245;">&#xe9bb;</text>
					</view>
					我的驾照
				</card-line>
			</view> -->
			<view class="marB_MIN" @click="RClick('/pages/user/comp/carList')">
				<card-line>
					<view slot="icon">
						<text class="iconfont" style="font-size: 50upx;color: #ff0e0e;">&#xe62e;</text>
					</view>
					我的车辆
				</card-line>
			</view>
			<view class="marB_MX" @click="RClick('/pages/user/comp/crawl')">
				<card-line>
					<view slot="icon">
						<view class="" style="position: relative;">
							<text class="iconfont" style="font-size: 60upx;color: #f6b03c;position: absolute;top: -11upx;left: 7px;">&#xe802;</text>
						</view>
					</view>
					电子围栏
				</card-line>
			</view>
			
			<!-- <view class="marB_MIN" @click="RClick('')">
				<card-line>
					<view slot="icon">
						<text class="iconfont" style="font-size: 50upx;color: #ff0e0e;">&#xe62e;</text>
					</view>
					常见问题
				</card-line>
			</view>
			<view class="marB_MIN" @click="RClick('')">
				<card-line>
					<view slot="icon">
						<text class="iconfont" style="font-size: 50upx;color: #ff0e0e;">&#xe62e;</text>
					</view>
					关于车宝
				</card-line>
			</view> -->
			<view class="marB_MX" @click="RClick('/pages/user/comp/ch_password')">
				<card-line>
					<view slot="icon">
						<view class="" style="position: relative;">
							<text class="iconfont" style="font-size: 60upx;color: #8a8a8a;position: absolute;top: -11upx;left: 7px;">&#xe602;</text>
						</view>
					</view>
					修改密码
				</card-line>
			</view>
			
			<view class="" style="padding: 20upx 30upx;">
				<button type="warn" @click="out">退出登录</button>
			</view>

		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex'
	import cardLine from '../../comp_zh/card-line.vue'
	import uniTag from "@/components/uni-tag.vue"
	import uniIcon from "@/components/uni-icon.vue"
	export default {
		components: {
			uniTag,
			cardLine,
			uniIcon
		},
		onPullDownRefresh(){
			// console.log('refresh');
			this.getuserInfo();
			setTimeout(function () {
				uni.stopPullDownRefresh();
			}, 1500)
		},
		onShow(){
			 this.getuserInfo();
		},
		data(){
			return{
				userMess:{
					himg: this.apis.ToTUrl + 'h_img/tot.png',
					xm: ''
				},
				imgs: ''
			}
		},
		/* created(){
			this.getuserInfo()
		}, */
		computed: {
			...mapState(['hasLogin', 'forcedLogin'])
		},
		methods: {
			...mapMutations(['logout']),
			getuserInfo(){
				this.$http.post("/app/user/getUserInfo").then((res) => {
					if(res.code == 200){
						// console.log('userInfo', res.result);
						this.userMess = res.result.userInfo;
						this.userMess.himg = this.apis.ToTUrl +  this.userMess.himg;
					}
				})
			},
			RClick(val) {
				uni.navigateTo({
					url: val
				});
			},
			
			bindLogin() {
				uni.navigateTo({
					url: '../login/login',
				});
			},
			bindLogout() {
				this.logout();
				/**
				 * 如果需要强制登录跳转回登录页面
				 */
				if (this.forcedLogin) {
					uni.reLaunch({
						url: '../login/login',
					});
				}
			},
			out() {
				uni.clearStorage();
				uni.redirectTo({
					url: '/pages/login/login'
				});
			}
		}
	}
</script>

<!-- <style>
	
</style> -->
<style lang="less">
	.marB_MX {
		margin-bottom: 40upx;
	}

	.marB_MIN {
		margin-bottom: 2upx;
	}

	.userTit {
		height: 200upx;
		// border-top: solid 1upx #f2f2f2;
		margin-top: 16upx;
		background-color: #fff;
		color: #f2f2f2;

		.userIcon {
			width: 26%;
			padding: 20upx;
			text-align: center;

			// background-color: #008000;
			image {
				width: 80%;
			}
		}

		.userMess {

			// padding: 0 22upx;
			.userName {
				height: 50%;
				line-height: 150upx;
				font-size: 30upx;
				font-weight: 700;
				color: #000000;
			}

			.userType {
				color: #999999;
			}
		}
	}
</style>
