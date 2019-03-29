<style lang="less">
	@import "../../styles/box.less";
	.linemess{
		.header{
			.vux-header{
				background-color: #fff;
			}
			.tit{
				color: #000000;
			}
		}
		.carmess{
			padding: 4px 10px;
			background-color: #fff;
			border-bottom: solid 1px #CCCCCC;
			.direction{
				font-size: 16px;
				color: #5d5252;
			}
			.timeMoney{
				font-size: 12px;
				color: #949494;
				margin: 2px 0;
			}
		}
		.cartime{
			background-color: #fff;
			height: 80px;
			padding: 15px 0;
			text-align: center;
			/*border-bottom: solid 3px #CCCCCC;*/
			.body-O{
				.icon-xinhao{
					color:#ff7800;
					font-size:16px;
				}
				.text{
					color:#ff7800;
					font-size:16px;
				}
			}
			.body-left{
				border-right: solid 2px #ccc;
			}
		}
		.carlines{
			background-color: #fff;
			padding-top: 35px;
			margin: 0 15px;
		}
		.footer{
			background-color: #000000;
			height: 45px;
		}
	}
</style>
<template>
	<div class="linemess box">
		<div class="header">
      <headTit tit="线路"></headTit>
	  		<!--<x-header-->
	  			<!--:left-options="{showBack: false}">-->
	  			<!--<span slot="left" @click="back">-->
	  				<!--<i class="iconfont icon-left"></i>-->
	  			<!--</span>-->
	  			<!--<span class="tit">{{XBline.name}}</span>-->
	  			<!--</x-header>-->
	  	</div>
	  	<div class="carmess">
	  		<div class="direction">
	  			<span>
            mane
            <!--{{XBline.list[0].zdName}}-->
          </span>
	  			<i class="iconfont icon-xiangyoujiaohuan"></i>
	  			<span>
            <!--{{XBline.list[XBline.list.length-1].zdName}}-->
            namw
          </span>
	  		</div>
	  		<div class="timeMoney">
	  			<span>运营时间：{{XBline.yxkssj}}-{{XBline.yxjssj}}</span>
	  			<!--<span>票价：2.0元</span>-->
	  		</div>
	  	</div>
	  	<div class="carlines body">
	  		<div class="box-row-z">
	  			<!--<div v-for="(item,index) in XBline.list">-->
	    			<!--<carline-->
	    				<!--:zd="item.entryCount!=0"-->
	    				<!--:linecar='item.exportCount!=0'-->
	    				<!--:siteName="item.zdName"-->
	    				<!--:lineShow="!(index==XBline.list.length-1)"></carline>-->
	    		<!--</div>-->
	  			<div>
	  				<carline :linecar="true" siteName="第一食堂第一食堂"></carline>
	  			</div>
	  			<div>
	  				<carline siteName="第二食堂第二食堂"></carline>
	  			</div>
	  			<div>
	  				<carline :zd="true" siteName="第003食堂第二食堂"></carline>
	  			</div>
	  			<div>
	  				<carline siteName="第003食堂第二食堂"></carline>
	  			</div>
	  			<div>
	  				<carline siteName="第003食堂第二食堂"></carline>
	  			</div>
	  			<div>
	  				<carline siteName="第003食堂第二食堂" :lineShow="false"></carline>
	  			</div>
	  		</div>
	  	</div>
	  	<!--<div class="footer">

	  	</div>-->
	</div>
</template>

<script>
	import carline from './comp/carline'
  import headTit from './headTit'
	export default {
		name:'',
		components: {
		    carline,headTit
		},
		data(){
			return{
				XBline:{
					list:[{}]
				}
			}
		},
		created(){
// 			this.lineMess()
		},
		mounted(){
		},
		methods:{
			back(){
				this.$router.push({
					name:'center'
				})
			},
			mapmess(){
				this.$router.push({
					name:'mapMess'
				})
			},
			lineMess(){
				var v = this
			// 	this.$http.post(configApi.LINEMESS.QUERTY,{'xlid':this.$route.query.lineID}).then((res)=>{
		  	// 		if(res.code ==200){
		  	// 			console.log('线路详情 ',res)
		  	// 			v.XBline = res.result
		  	// 		}else{
		  	// 			v.$Message.success('线路数据获取失败');
		  	// 		}
		  	// 	}).catch((error) =>{
	        	// 	console.log('出错了',error)
	        	// })
			}
		}
	}
</script>

<style>
</style>
