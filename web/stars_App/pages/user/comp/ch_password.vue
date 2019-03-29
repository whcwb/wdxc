<template>
	<view class="pagebody">
		<view class="box_col" style="background-color: #f2f2f2;">
			<pagr-tit backUrl="/pages/user/user" TiT="密码修改">
			</pagr-tit>
			<view class="box_col_100">
				<view class="input-group checkpwdSty">
					<view class="input-row border" style="background-color: #fff;">
						<text class="title">原始密码：</text>
						<input type="text" v-model="user.oldPwd" placeholder="请输入原始密码">
					</view>
					<view class="input-row border" style="background-color: #fff;">
						<text class="title">新的密码：</text>
						<input type="text" v-model="user.newPwd" placeholder="请输入新密码">
					</view>
					<view class="input-row border" style="background-color: #fff;">
						<text class="title">确认密码：</text>
						<input type="text" v-model="user.newPwd1" placeholder="请输确认密码">
					</view>
				</view>				
				<view class="" style="padding: 20upx 30upx;">
					<button type="warn" @click="checkPwd">确认修改</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import pagrTit from '../../../comp_zh/pageTit.vue'
	export default {
		components: {
			pagrTit
		},
		data() {
			return {
				user:{
					userName:'',
					oldPwd:'',
					newPwd:'',
					newPwd1:''
				}
			};
		},
		created(){
			console.log('你的名字是永久的记忆',uni.getStorageSync('userName'))
			if(uni.getStorageSync('userName')){
				this.user.userName = uni.getStorageSync('userName')
			}else{
				uni.showToast({
					title: '用户信息丢失,请重新登录!',
					duration: 2000
				});
				setTimeout(()=>{
					uni.navigateTo({
						url: '/pages/login/login'
					});
				},1500)
			}
		},
		methods:{
			checkPwd(){
				this.$http.post('/app/user/editPwd',this.user).then(res=>{
					console.log('密码修改',res)
					if(res.code == 200){
						uni.showToast({
							title: '密码修改成功,请重新登录!',
							duration: 2000
						});
						setTimeout(()=>{
							uni.navigateTo({
								url: '/pages/login/login'
							});
						},1500)
					}else{
						uni.showToast({
							title: res.message,
							icon:"none",
							duration: 2000
						});
					}
				}).catch(err=>{})
			}
		}
	}
</script>

<style lang="less">
.checkpwdSty{
	padding: 0 20upx;
	.border{
		border-bottom: solid 2upx #f2f2f2;
	}
}
</style>
