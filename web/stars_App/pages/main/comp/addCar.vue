<template>
	<view class="pagebody" style="background-color: #efeff4;">
		<pagr-tit TiT="添加车辆">
		</pagr-tit>
		<view class="box_col">
				<view class="boxTit">
					终端绑定
				</view>
				<view class="box_row carNum">
					<view class="tit">
						终端编号:
					</view>
					<view class="box_row_100 addInput">
						<input v-model="carInfo.zdbh" confirm-type="search" @confirm="confirm" @blur="validZdbh" class="input" type="text"
							   placeholder="请输入终端编号" />
					</view>
					<view class="" style="width: 100upx;text-align: center;line-height: 100upx;" @click="scan">
						<text class="iconfont" style="font-size: 48upx;color: #999999;">&#xe615;</text>
					</view>
				</view>

			<view class="boxTit">
				车量信息
			</view>
			<view class="box_row carNum">
				<view class="tit">
					车牌号码:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.cph" confirm-type="search" @confirm="confirm" class="input" type="text" placeholder="输入车牌号码" />
				</view>
			</view>
			<view class="box_row carNum">
				<view class="tit">
					车架号码:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.cjh" confirm-type="search" @confirm="confirm" class="input" type="text" placeholder="输入车架号码" />
				</view>
			</view>
			<view class="box_row carNum">
				<view class="tit">
					发动机号:
				</view>
				<view class="box_row_100 addInput">
					<input v-model="carInfo.fdjh" confirm-type="search" @confirm="confirm" class="input" type="text" placeholder="输入发动机号" />
				</view>
			</view>
			<!--hello-->
			<view class="box_row carNum">
				<view class="tit">
					注册日期:
				</view>
				<view class="box_row_100 addInput">
					<picker class="picker-item"  mode="date" start="2018-01-01" end="2019-01-01" @change="dateChange">
						<view>注册日期：{{carInfo.ccdjrq}}</view>
					</picker>
				</view>
			</view>
			<view class="box_row carNum">
				<view class="tit">
					品牌型号:
				</view>
				<view class="box_row_100 addInput">
					<!-- :disabled="disabled" -->
					<input v-model="carInfo.xh"  confirm-type="search" @confirm="confirm" class="input" type="text" placeholder="输入品牌型号" />
				</view>
			</view>
			<view class="" style="padding: 20upx 30upx;">
				<button style="background-color: #00BFFF;color: #fff;" @click="confirm">确定</button>
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
                date:'',
                carInfo:{
                    cph:'',
                    cjh:'',
                    zdbh:'',
                    fdjh:'',
                    xh:'',
                    ccdjrq:'',
                },
                disabled:false,
			};
		},
		methods: {
            dateChange(e){
                console.log(e);
                this.carInfo.ccdjrq = e.detail.value;
            },
			scan() {
				var v = this
				console.log('123')
				uni.scanCode({
					onlyFromCamera: true,
					success: function(res) {
						console.log('条码类型：' + res.scanType);
						console.log('条码内容：' + res.result);
						v.carInfo.zdbh = res.result
						v.validZdbh();
					}
				});
			},
			validZdbh(){
                this.$http.post("/app/car/findByDeviceId",{deviceId:this.carInfo.zdbh}).then((res) => {
                    if(res.code == 200 && res.result){
                        this.carInfo = res.result;
                        this.disabled = true;
                    }else{
                        let m = '';
                        if (res.code == 4041){
                            m = '终端不存在';
						}else if (res.code == 4042){
                            m = '该终端未绑定车辆';
						}
                        uni.showToast({
                            icon:'none',
                            title: m,
                            duration: 2000
                        });
                    }
                })
			},
			confirm() {
                delete this.carInfo.clDzwl;
                delete this.carInfo.clDzwlCl;
                this.$http.post("/app/car/addCar",this.carInfo).then((res) => {
                    if(res.code == 200){
                        uni.showToast({
                            title: res.message,
                            duration: 2000
                        });
                        uni.navigateTo({
                            url: '/pages/user/comp/carList'
                        })
                    }else{
                        uni.showToast({
                            icon:'none',
                            title: res.message,
                            duration: 2000
                        });
                    }
                })
			}
		}
	}
</script>

<style lang="less">
	.boxTit {
		color: #00BFFF;
		font-size: 28upx;
		padding: 12upx 12upx;
	}
	.carNum {
		background-color: #fff;
		height: 100upx;
		border-bottom: solid 4upx #f3f3f3;
		.tit {
			line-height: 100upx;
			font-size: 34upx;
			width: 160upx;
			text-align: right;
			padding-right: 20upx;
		}

		.addInput {
			margin: auto;
			text-align: right;
			padding-right: 36upx;
		}
	}

	.saveBut {
		button {
			font-size: 18upx;
		}
	}
</style>
