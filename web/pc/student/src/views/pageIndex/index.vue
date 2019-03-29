<style lang="less">
	@import "../../styles/common.less";
	@import "./pageIndex";
</style>
<template>
	<div class="box">
		<div class="header">
			<div class="box-row">
				<!--<div class="titLeft" @click="MyCenter()">-->
					<!--<Icon type="person" color="#949494" size='26'></Icon>-->
				<!--</div>-->
				<div class="titCenter body-O" style="text-align: center;">
            <h2 style="color: #929292;">
              班车线路查询
            </h2>
					 <!--<Input v-model="titFind" size="small" placeholder="请输入站点名称" ></Input>-->
				</div>
				<!--<div class="titRight" @click="feedback()">-->
					<!--<Icon type="ios-compose" color="#949494" size='26'></Icon>-->
				<!--</div>-->
			</div>
		</div>
		<div>
			<swiper
				:list="imglist"
				v-model="imglistV"
				:auto="true"
				:loop="true"
				:interval="2000"
				@on-index-change="demo01_onIndexChange"></swiper>
		</div>
		<div class="body bodylist" style="background-color: #fff;">
			<div class="box">
				<div class="">
					<tab>
				      <tab-item :selected="barNum==0" @on-item-click="onItemClick">校园巴士</tab-item>
				      <tab-item :selected="barNum==1" @on-item-click="onItemClick">通勤巴士</tab-item>
				    </tab>
				</div>
				<div class="body">
					<div class="list">
						<div class="listTiT box-row"
							v-for="(item,index) in lineList"
							@click="lineMess(item.id)">
							<div class="body-O">
								<i class="iconfont icon-003lubiao"></i>
								{{item.xlmc}}
							</div>
							<div>
								<i class="iconfont icon-right"></i>
							</div>
						</div>
					</div>
					<div class="list" v-show="false">
						<div class="listTiT box-row">
							<div class="body-O">
								<i class="iconfont icon-003lubiao"></i>
								校巴一号站点
							</div>
							<div>
								<i class="iconfont icon-right"></i>
							</div>
						</div>
						<ul>
							<li v-for="item in [,,,,,,]">
								<div class="carNum">
									202
								</div>
								<div class="box-row line lineTop" @click="lineMess">
									<div class="body-O">
										<i class="iconfont icon-jiantouarrow591"></i>
										开往校图书馆
									</div>
									<div>
										<i class="iconfont icon-xinhao"></i>2站
									</div>
								</div>
								<div class="box-row line" @click="lineMess">
									<div class="body-O">
										<i class="iconfont icon-jiantouarrow591"></i>
										第一食堂
									</div>
									<div>
										<i class="iconfont icon-xinhao"></i>4站
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
    <!--<component-->
      <!--:is="componentName"-->
      <!--:mess="choosedRow"></component>-->
	</div>
</template>

<script>
	import { Swiper, SwiperItem, Cell, XButton, Tab, TabItem } from 'vux'
	import configApi from '@/axios/config.js'
	export default{
		name:'',
		components: {
		    Swiper,
		    SwiperItem,
		    Cell,
		    XButton, Tab, TabItem
		 },
		data(){
			return{
				school:true,
				titFind:'',
				imglistV:1,
        barNum:this.$store.state.app.barNum,
				imglist: [],
				lineList:[]
			}
		},
		created(){
		  if(this.barNum == 0){
			  this.getLineMess(30)
      }else if(this.barNum == 1){
        this.getLineMess(20)
      }
			this.getSwiperMess()
		},
		methods:{
		  	MyCenter(){//个人中心
		  		this.$router.push({
		    		name:'myCenter'
		    	})
		  	},
		  	feedback(){//反馈信息
		  		this.$router.push({
		    		name:'feedback'
		    	})
		  	},
		  	getSwiperMess(){
		  		var v = this
		  		this.$http.post(configApi.SWIPER.QUERTY).then((res)=>{
		  			if(res.code ==200){
		  				console.log('图片数据',res)
//		  				v.$Message.success('This is a success tip');
		  				v.imglist = res.result
		  			}else{
		  				v.$Message.success('图片获取失败');
		  			}
		  		}).catch((error) =>{
	        		console.log('出错了',error)
	        	})
		  	},
		  	getLineMess(lx){
		  		var v = this
		  		this.$http.post(configApi.LINE.QUERTY,{'lx':lx}).then((res)=>{
		  			if(res.code ==200){
		  				console.log('线路数据',res)
//		  				v.$Message.success('This is a success tip');
		  				v.lineList = res.result
		  			}else{
		  				v.$Message.success('线路数据获取失败');
		  			}
		  		}).catch((error) =>{
	        		console.log('出错了',error)
	        	})
		  	},
		  	demo01_onIndexChange (index) {
//				console.log(index)
		    },
		    lineMess(id){
          this.$store.commit('ChlineID',id)
		    	this.$router.push({
            name:'lineMess'
          })
		    },
		    onItemClick(index){
		    	if(index==0){
            this.getLineMess(30)
		    	}else if(index==1){
            this.getLineMess(20)
		    	}
          this.$store.commit('barCh',index)
		    }
		}
	}
</script>

<style>
</style>
