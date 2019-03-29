<style lang="less">
	@import '../../../../../styles/common.less';
	.ddfpSty{
		position: relative;
		margin-bottom: 6px;
		margin-right: 3px;
		padding: 3px 8px;
		border-left: solid #fb00ff 2px;
		border-right: solid #fb00ff 2px;
		border-radius: 15px;
	}
	.ElistDemo{
		width: 400px;
		height: 76px;
		z-index: 999;
		background-color: #c8eaef;
		box-shadow: 2px 5px 5px #888888;
		position: absolute;
		right: 15px;
		top: 2px;
		text-align: center;
	.tit{
		width: 52px;
		font-size: 16px;
		padding:10px 8px 0 8px;
		border-right: solid 2px #b0bbc8;
	}
	.hg{
		line-height: 35px;
		padding: 0 10px;
	}
	.zwxz{
		cursor: pointer;
	.num{
		color: #ff8300;
		font-size: 16px;
		font-weight: 600;
	}
	}
	.zwxz:hover{
		color: #00b5d3;
		font-size: 16px;
		font-weight: 600;
	.num{
		color: #ff8300;
	}
	}
	}
</style>
<style scoped>
	.demo-badge{
		width: 42px;
		height: 42px;
		background: #eee;
		border-radius: 6px;
		display: inline-block;
	}
</style>
<!--订单分派-->
<template>
	<div class="box boxbackborder" style="position: relative">
		<div class="ElistDemo box-row-nh">
			<div class="tit">
				今日数据
			</div>
			<div style="border-right: solid 2px #b0bbc8;">
				<div class="box">
					<div class="body hg">
						已完成
					</div>
					<div class="body hg">
						<span style="color: #00a864;font-size: 16px;font-weight: 600;">
						{{jrpd.WCORDER}}
						</span>
					</div>
				</div>
			</div>
			<div class="body-1">
				<div class="box">
					<div class="body hg" style="border-bottom: solid 2px #b0bbc8;cursor: pointer"  @click="getDrvList()">
						待派定单
						<span style="color: #ff8300;font-size: 16px;font-weight: 600;">
							{{jrpd.num}}
						</span>
					</div>
					<div class="body hg">
						<div class="box-row-nh" v-if="jrpd.list.length>0">
							<div class="body-1 zwxz" v-for="(item,index) in jrpd.list" @click="getDrvList(item.ZWS)">
								{{parseInt(item.ZWS)}}座
								<span class="num">
									{{item.COU}}
								</span>
							</div>
						</div>
						<div v-else>
							……
						</div>
					</div>
				</div>
			</div>
		</div>
		<Row class="margin-top-10" style='background-color: #fff;position: relative;'>
				<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
			<div style="height: 45px;line-height: 45px;">
				<div class="margin-top-10 box-row">
					<div class="titmess">
						<span>订单分派</span>
					</div>
					<div class="body-r-1 inputSty" style="text-align: left">
						<Menu mode="horizontal" :theme="'light'"
						      @on-select="menuClick"
						      active-name="1">
							<Menu-item name="1" style="font-size: 22px">
								<Icon type="ios-paper"></Icon>
								内部车
							</Menu-item>
							<Menu-item name="2"  style="font-size: 22px">
								<Icon type="ios-people"></Icon>
								外借车
							</Menu-item>
						</Menu>
					</div>
					<div class="butevent">
					</div>
				</div>
			</div>
		</Row>
		<div class="body" style="border-top:3px #c8c8c8 solid;padding-top: 5px">
			<div class="box-row-list">
				<Card style="width:440px;margin:8px" v-for="(item,index) in drvlist">
					<div slot="title" style="height: 40px;line-height: 25px">
						<div class="box-row" style="height: 24px">
							<div style="font-weight: 700">
								<Icon type="ios-film-outline"></Icon>
								{{item.xm}}
							</div>
							<div class="body-O" style="padding-left:8px " v-if="zjcx==2030">
									<span>
										总（{{item.endOrderCount+item.startOrderCount}}）
									</span>
								<span>
										完（{{item.endOrderCount}}）
									</span>
								<span>
										未（{{item.startOrderCount}}）
									</span>
							</div>
						</div>
						<div class="box-row">
							<div style="margin-right: 16px">
								<Icon type="ios-car"></Icon>
								{{item.cph}}
							</div>
							<div class="body-O">
								<Icon type="android-contacts"></Icon>
								{{parseInt(item.zkl)}}座
							</div>
						</div>
					</div>
					<span slot="extra">
						<i-switch  v-if="zjcx==2030" size="large" v-model="item.zt=='00'" @on-change="switchCh(item)">
							<span slot="open">在班</span>
							<span slot="close">休息</span>
						</i-switch>
						<Tooltip content="订单分配" placement="left">
							<Button type="primary"
									:disabled="item.zt=='01'"
									size="small"
									shape="circle"
									icon="md-add"
									@click="showList(item)"></Button>
						</Tooltip>
					</span>
					<div style="height: 160px;overflow: auto">
						<div  v-if="item.clDdList.length==0">
							<span>
								暂无分配订单
							</span>
						</div>
						<div v-else class="ddfpSty" v-for="(p,i) in item.clDdList" >
							<div>
								<Row>
									<Col span="10">
									<Icon type="md-person"
										  color="#7b7b7b"></Icon>
									<!--信息工程学院—陈小伟-->
									{{p.jgmc}}-{{p.ck}}
									</Col>
									<Col span="10">
									<Icon type="ios-clock"
										  color="#0897ff"></Icon>
									{{p.yysj}}
									</Col>
								</Row>
							</div>
							<div>
								<Row>
									<Col span="10">
									<Icon type="record" color="#19a853"></Icon>
									{{p.hcdz}}
									</Col>
									<Col span="10">
									<Icon type="record" color="#ff8f00"></Icon>
									{{p.mdd}}
									</Col>
								</Row>
							</div>
							<div style="position: absolute;right: 5px;top:10px;">
								<Tooltip content="撤回" placement="left">
									<Button type="warning"
											size="small"
											shape="circle"
											icon="reply"
											@click="dele(p.id)"></Button>
								</Tooltip>
							</div>
						</div>

					</div>
				</Card>
			</div>
		</div>
		<component :is="compName" :mess="mess" :type="zjcx"></component>
	</div>
</template>

<script>
    import pageList from './comp/list'
    export default{
        name:'driver',
        components: {
            pageList
        },
        data(){
            return{
                swi:true,
                mess:{},
                compName:'',
                drvlist:[],
                jrpd:{},
                zjcx:'2030'
            }
        },
        created(){
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '车辆管理',
            },{
                title: '订单管理',
            },{
                title: '订单分配',
            }]);
            this.getDrvList()
            this.pdtj()
        },
        mounted(){
        },
        methods:{
            menuClick(val){//内部车与外部车
                // alert(val)
		if(val==1){
                    this.zjcx = '2030'
		}else if(val==2){
                    this.zjcx = '40'
		}
		this.getDrvList()
	    },
            switchCh(item){
                let zt = ''
                if(item.zt=='00'){
                    zt = '10'
                }else {
                    zt = '00'
                }
                this.$http.post(this.apis.ORDER.DRZT,{'sfzhm':item.sfzhm,'zt':zt}).then((res) =>{
                    log('驾驶员数据',res)
                    if(res.code==200){
                        this.$Message.success(res.message);
                    }else{
                        this.$Message.error(res.message);
                    }
                    this.getDrvList()
                })
            },
            pdtj(){//派单统计
                var v = this
                this.$http.post(this.apis.ORDER.PDTJ,{'cllx':2030}).then((res) =>{
                    // debugger
                    if(res.code == 200){
                        let num = 0
                        if(res.result.list.length == 0){
                            res.result.num = num
                        }else {
                            for(var i of res.result.list){
                                num = num+i.COU
                            }
                            res.result.num = num
                        }
                        v.jrpd = res.result

                        log('派单****',res.result)
                    }
                }).catch(()=>{

                })
            },
            getDrvList(zkl){//司机列表
                var v = this
                if(!zkl){
                    zkl = ''
                }
                this.$http.post(this.apis.ORDER.SJLB,{'zjcx':v.zjcx}).then((res) =>{
                    if(res.code == 200){
                        v.drvlist = res.result
                    }
                    log(res)
                })
            },
            dele(id){//取消分派
                var v = this
                this.$http.post(this.apis.ORDER.QXPD,{'id':id}).then((res) =>{
                    if(res.code===200){
                        v.$Message.success(res.message);
                        v.getDrvList()
						v.pdtj()

                    }else{
                        v.$Message.error(res.message);
                    }
                }).catch((error) =>{
                    v.$Message.error('出错了！！！');
                })
            },
            showList(item){
                this.compName = 'pageList'
                this.mess = item
            }
        }
    }
</script>

<style>
</style>
