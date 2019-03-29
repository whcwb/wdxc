<style lang="less">
	@import "../../../styles/common.less";
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
	  		<x-header
	  			:left-options="{showBack: false}">
	  			<span slot="left" @click="back">
	  				<i class="iconfont icon-left"></i>
	  			</span>
	  			<span class="tit">{{XBline.name}}</span>
	  			<span slot="right" @click="mapmess">
	  				<i class="iconfont icon-ditu"></i>
	  			</span>
	  			</x-header>
	  	</div>
	  	<div class="carmess">
	  		<div class="direction">
	  			<span>{{XBline.list[0].zdName}}</span>
	  			<i class="iconfont icon-xiangyoujiaohuan"></i>
	  			<span>{{XBline.list[XBline.list.length-1].zdName}}</span>
	  		</div>
	  		<div class="timeMoney">
	  			<span>运营时间：{{XBline.yxkssj}}-{{XBline.yxjssj}}</span>
	  			<!--<span>票价：2.0元</span>-->
	  		</div>
	  	</div>
	  	<div class="cartime box-row">
	  		<div class="body-O body-left">
	  			<div>
	  				<i class="iconfont icon-xinhao"></i>
	  				<span class="text">{{carZD[0] | carMess}}</span>
	  				站后
	  			</div>
	  			<div>
	  				第一辆车抵达
	  			</div>
	  		</div>
	  		<div class="body-O">
	  			<div>
	  				<i class="iconfont icon-xinhao"></i>
	  				<span class="text">{{carZD[1] | carMess}}</span>
	  				站后
	  			</div>
	  			<div>
	  				第二辆车抵达
	  			</div>
	  		</div>
	  	</div>
	  	<div class="carlines body">
	  		<div class="box-row-z">
	  			<div v-for="(item,index) in XBline.list" @click="getZD(item,index)">
	    			<carline
	    				:zd="item.entryCount!=0"
	    				:linecar='item.exportCount!=0'
              :lineColor="item.lineColor"
              :barColor="item.bar"
	    				:siteName="item.zdName"
	    				:lineShow="!(index==XBline.list.length-1)"></carline>
	    		</div>
	  			<!--<div>
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
	  			</div>-->
	  		</div>
	  	</div>
	  	<!--<div class="footer">

	  	</div>-->
	</div>
</template>

<script>
	import {XHeader } from 'vux'
	import carline from './comp/carline'

	import configApi from '@/axios/config.js'
	export default {
		name:'',
		components: {
		    XHeader,carline
		},
    filters: {
      carMess: function (value) {
        if (value==null||value==undefined||value=='') return '*'
        return value
      }
    },
		data(){
			return{
				XBline:{
					list:[{}]
				},
        carZD:['','']
			}
		},
		created(){
//			console.log(this.$route.query.lineID)
      if(this.$store.state.app.lineID !=0){
			  this.lineMess(this.$store.state.app.lineID)
      }else {
        this.$router.push({
          name:'center'
        })
      }
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
			lineMess(id){
				var v = this
				this.$http.post(configApi.LINEMESS.QUERTY,{'xlid':id}).then((res)=>{
		  			if(res.code ==200){
		  				console.log('线路详情 ',res)
              res.result.list.forEach((item,index)=>{
                item.lineColor = '#ffa700'
                item.bar = '#ffa700'
              })
		  				v.XBline = res.result
//		  				v.$Message.success('This is a success tip');
		  			}else{
		  				v.$Message.success('线路数据获取失败');
		  			}
		  		}).catch((error) =>{
	        		console.log('出错了',error)
	        	})
			},
      getZD(item,index){
			  this.XBline.list.forEach((it,dex)=>{
			    if(dex>=index){
            it.lineColor = '#c9c9c9'
          }else {
            it.lineColor = '#ffa700'
          }

          if(dex>index){
            it.bar = '#c9c9c9'
          }else {
            it.bar = '#ffa700'
          }

        })
        this.$http.post(configApi.GETCAR.QUERTY,{'xlId':this.$store.state.app.lineID,'zdId':item.zdId}).then((res)=>{
          if(res.code == 200&&res.result){
            this.carZD = res.result
          }
        })
      }
		}
	}
</script>

<style>
</style>
