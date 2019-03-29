<template>
	<view class="pagebody userInfo" style="background-color: #f2f2f2;">
		<view class="box_col">
			<pagr-tit backUrl="/pages/user/user" TiT="个人信息">
				<view class="" slot="titright" style="position: relative;">
					<div @click="updateUserInfo" style="line-height: 90upx;padding-left: 10upx;">
						<span style="font-size: 40upx;color: #fff;">保存</span>
					</div>
				</view>
			</pagr-tit>
			<view class="box_col_100">
				<view class="box_row infoTX backSty">
					<view class="box_row_100 infoTitTxt" >
						头像
					</view>
					<view >
						<!-- <image :src="userMess.himg" style="height: 160upx;width: 160upx;border-radius: 80upx;"  mode="scaleToFill"></image> -->
						<image @click="getImg" style="height: 160upx;width: 160upx;border-radius: 80upx;"  :src="userMess.himg"  mode="scaleToFill"></image>
					</view>
				</view>

				<!-- <view class="box_row InfoList backSty">
					<view class="infoTit">
						昵称
					</view>
				</view> -->
				
				<view class=" box_row InfoList backSty">
					<view class=" infoTit" style="width: 50%;">
						姓名
					</view>
					<view  style="width: 50%;" >
						<input v-model="yh.nickName"  style="text-align: right;  margin-top: 25upx;font-size: 40upx;color: #f6b03c;"></input>
					</view>
				</view>
				
				<!-- <view class="box_row InfoList backSty">
					<view class="infoTit">
						性别
					</view>
				</view> -->
				
				<!-- <view class="box_row InfoList backSty">
					<view class="infoTit">
						出生日期
					</view>
				</view> -->
				
				<!-- <view class="box_row InfoList backSty">
					<view class="infoTit">
						地址
					</view>
				</view> -->
				
				<!-- <view class="box_row InfoList backSty">
					<view class="infoTit">
						e-mail
					</view>
				</view> -->
				
				<!-- <view class="box_row InfoList backSty">
					<view class="infoTit">
						学历
					</view>
				</view> -->
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
				fileUpload: this.apis.UPLOAD,
				userMess:{
					himg: this.apis.ToTUrl + 'h_img/tot.png'
				},
				yh:{
					hImg: '',
					nickName: ''
				},
				imgMess:''
			};
		},
		onShow() {
			 this.getUserInfo();
		},
		created() {
			
		},
		methods:{
			updateUserInfo(u){
				this.$http.post("/app/user/editUserInfo",this.yh).then((res) => {
					if(res.code == 200 ) {
						this.userMess.himg =this.apis.ToTUrl +  res.result.userInfo.himg;
						this.yh.nickName = res.result.userInfo.xm;
						this.yh.hImg = res.result.userInfo.himg;
						/* uni.switchTab({
							url: '/pages/user/user'
						}); */
						uni.showToast({
							title: '修改成功',
							icon: 'none',
							duration: 2000
						});
					}else{
						uni.showToast({
							title: res.message,
							icon: 'none',
							duration: 2000
						});
					}
				})
			},
			getImg(){
				var v = this
				uni.chooseImage({
					count: 1, //默认9
					sizeType: [ 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: function (res) {
						const tempFilePaths = res.tempFilePaths;
						uni.uploadFile({
							url: v.fileUpload, //仅为示例，非真实的接口地址
							filePath: tempFilePaths[0],
							name: 'file',
							fileType: 'image',
							formData:{
								targetPath :  'h_img'
							},
							success: (p) => {
								let urlCallBack = JSON.parse(p.data)
								if(urlCallBack.code == 200 ){
									v.userMess.himg = urlCallBack.message;
									v.yh.hImg = urlCallBack.message;
									v.updateUserInfo();
								}else{
									uni.showToast({
										title: urlCallBack.message,
										icon: 'none',
										duration: 2000
									});
								}
								
							}
						});
						
					
						
						// const tempFilePaths = chooseImageRes.tempFilePaths;
						// console.log('res' , res);
						
					},
					fail: function (res) {
						uni.showToast({
							title: '头像上传失败',
							icon: 'none',
							duration: 2000
						});
					}
					
					
				});
			},
			getUserInfo(){
				this.$http.post("/app/user/getUserInfo").then((res) => {
					if(res.code == 200){
						// console.log('userInfo', res.result);
						this.userMess.himg =this.apis.ToTUrl +   res.result.userInfo.himg;
						console.log('himg',this.userMess.himg);
						this.yh.nickName = res.result.userInfo.xm;
						this.yh.hImg = res.result.userInfo.himg;
					}else{
						uni.showToast({
							title: res.message,
							icon: 'none',
							duration: 2000
						});
					}
				})
			}
		}
	}
</script>

<style lang="less">
.userInfo{
	.backSty{
		background-color: #ffff;
		border-bottom: solid #ededed 4upx;
	}
	.infoTX{
		padding:22upx 0;
		.infoTitTxt{
			font-size: 40upx;
			line-height: 160upx;
			padding-left: 26upx;
		}
		.infoTitImg{
			margin-right: 26upx;
			background-color: #007AFF;
			width: 160upx;
			height: 160upx;
			// background-image: url('../../../static/img/gir.jpg');
			background-size: 100% 100%;
			background-position: center;
			background-repeat: no-repeat;
		}
	}
	.InfoList{
		padding: 0 26upx;
		.infoTit{
			font-size: 40upx;
			line-height: 110upx;
		}
	}
}
</style>
