<style lang="less">
	.carline{
		position: relative;
		width: 100px;
		.barT{
			width: 18px;
			height: 18px;
			border: solid 4px #fba05e;
			border-radius: 20px;
			background-color: #fffa8a;
			position: absolute;
			left: 0;
			top: 0;
		}
		.bar{
			width: 18px;
			height: 18px;
			border-radius: 20px;
			background-color: #ffc573;
			position: absolute;
			left: 0;
			top: 0;
		}
		.line{
			width: 80px;
			height: 2px;
			background-color: #ffe7a1;
			position: absolute;
			left: 18px;
			top: 9px;
		}
		.name{
			font-size: 14px;
			width: 20px;
			text-align: center;
			position: absolute;
			left: 0;
			top: 18px;
		}
		.nameT{
			font-size: 14px;
			width: 20px;
			color:#ff9500;
			text-align: center;
			position: absolute;
			left: 0;
			top: 18px;
		}
		.car{
			text-align: center;
			position: absolute;
			top: -25px;
			font-size: 22px;
			color: #ff7800;
		}
		.carT{
			text-align: center;
			position: absolute;
			left: -2px;
			top: -25px;
			font-size: 22px;
			color: #ff7800;
		}
	}
</style>
<template>
	<div class="carline">
		<div class="bar" v-if="!zd"></div>
		<div class="barT" v-else></div>
		
		<div class="line" v-show="lineShow"></div>
		
		<div class="name" v-if="!zd">{{siteName}}</div>
		<div class="nameT" v-else>{{siteName}}</div>
		
		<i class="carT iconfont icon-weibiaoti507" v-show="zd"></i>
		<i class="car iconfont icon-weibiaoti507" v-show="linecar"></i>
	</div>
</template>

<script>
	export default{
		name:'',
		data(){
			return {
			}
		},
		props:{
			zd:{//站点上的车
				type:Boolean,
				default:false
			},
			linecar:{//线路上的车
				type:Boolean,
				default:false
			},
			lineShow:{
				type:Boolean,
				default:true
			},
			siteName:{
				type:String,
				default:"站点名称"
			}
		}
	}
</script>