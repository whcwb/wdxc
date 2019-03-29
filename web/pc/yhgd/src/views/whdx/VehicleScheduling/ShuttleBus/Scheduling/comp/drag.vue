<style lang="less">
@import './draggable-list.less';
li{
	list-style: none;
}
</style>
<template>
	<div class="box">
		<div class="tit">
			<Row class="margin-top-10">
				<Col span="24" style="text-align:center;">
				<h1>
					{{todaytime}}
					排班</h1>
				<Button style="float: right;margin-left:8px" type="success" @click="okmodel()">完成</Button>
				</Col>
			</Row>
		</div>
		<div class="body">
			<div class="box-row">
				<div style="width: 260px;margin-right:5px;height: 100%;font-size: 16px;border-right: solid 1px #008855;">
					<Row style="text-align: center;border-bottom: solid 3px #9B9B9B;">
						<Col span="12">姓名</Col>
						<Col span="12">车辆</Col>
					</Row>
					<Row style="text-align: center;font-size: 14px;">
						<ul id="todoList" class="iview-admin-draggable-list" style="min-height: 300px;">
                            <li v-for="(item,index) in carList" :key="index" :data-index="index">
		                        <div style="border: solid 1px #9B9B9B;padding: 3px 0;overflow: hidden;">
		                        	<div style="width: 50%;float: left;">{{item.sjxm}}</div>
		                        	<div style="width: 50%;float: left;">{{item.cph}}</div>
		                        	<!--<div style="width: 33.33%;float: left;">{{item.zt | changeZT}}</div>-->
								</div>
                            </li>
                        </ul>
					</Row>
				</div>
				<div class="body-F" style="background-color: #00A854;padding: 5px;">
					<Row :gutter='8'>
						<Col span="8" class="margin-bottom-10" v-for="(item,index) in listdata">
							<Card style="width:100%">
								<p slot="title">
									<Icon type="ios-film-outline"></Icon>
									{{item.xlmc}}
								</p>
								<span slot="extra">
										            <Icon type="ios-loop-strong"></Icon>
										            时间
										        </span>
								<div style="height: 140px;text-align: center;overflow: auto;" :id="index+'_card'">
									<div class="xlxq" v-for="(items,indexs) in item.clList">
										<div class="xlxq-list">
												{{items.sjxm}}
										</div>
										<div class="xlxq-list">
												{{items.cph}}
										</div>
										<div class="xlxq-list xlxq-but">
												<Button type="error" size="small" shape="circle" icon="close" @click="deleteById(item.id,items.clId)"></Button>
										</div>
									</div>
								</div>
							</Card>
						</Col>
					</Row>
				</div>
			</div>
		</div>
	</div>
</template>
<style lang="less" scoped="scoped">
	.xlxq{
		height: 34px;
		line-height: 28px;
		overflow: hidden;
		border: solid 1px #9B9B9B;
		padding: 3px 0;
		.xlxq-list{
			float: left;
			width: 33.3%;
		}
		.xlxq-but{
			text-align: right;
			padding-right: 5px;
		}
	}
</style>
<script>
	import Sortable from 'sortablejs';
	

	export default {
		name:'',
		data(){
			return{
				listdata:[],
				carList:[]
			}
		},
		props: {
			todaytime: {
				type: String,
				default: '2018-03-26'
			}
		},
		filters: {
			changeZT:(value)=>{
				switch(value){
					case '00' :
						return '正常'
					case '01' :
						return '停用'	
				}
			}
		},
		created(){
			this.getmess()
			window.wvm = this
		},
		mounted (){
			let vm = this;
	        let todoList = document.getElementById('todoList');
	        Sortable.create(todoList, {
	            group: {
	                name: 'list',
	                pull: 'clone'
	            },
	            animation: 120,
	            onRemove (event) {
                    //车辆id
	            	let clId = vm.carList[event.item.getAttribute('data-index')].clId
	            	//线路id
	            	let linelength = event.to.id
	            	let lineID = linelength.substring(0,linelength.length-5)
	            	let xlId = vm.listdata[lineID].id
	            	vm.AddList(clId,xlId)
	            }
	        });
		},
		methods:{
			
			getmess(){
				var v = this
				//车辆数据
				this.$http.get(this.apis.CLGL.QUERY).then((res) =>{
					v.carList = res.page.list
					log('车辆数据',res.page.list)
				}).catch((err) =>{
					log('bug')
				})
				//线路数据
				this.$http.post(this.apis.XLPBXX.QUERY,{"clcx":"30","date2":v.todaytime}).then((res) =>{
					log('排班数据2',res)
					v.listdata = res.result
				}).then((res) =>{
					v.fordata()
				}).catch((err) =>{
					log('bug')
				})
			},
			AddList(carID,LineID){
				var v = this
				this.$http.post(this.apis.XLPBXX.ADD,{"clId":carID,"xlId":LineID,"date2":v.todaytime}).then((res) =>{
					log('排版新增',res)
//					if(res.code==500){
//						v.$parent.domeC()
//					}
				})
			},
			deleteById(xlId,clId){
                this.$Message.success('移出成功');
                var v = this
                this.$http.post(this.apis.XLPBXX.deleteByXlAndCl,{"xlId":xlId,clId:clId}).then((res) =>{
                    this.getmess();
                })
			},
			okmodel(){
				this.$emit('okdrag')
			},
			fordata(){
				let vm = this;
				for(var i=0;i<vm.listdata.length;i++){
					Sortable.create((document.getElementById(i+'_card')), {
			            group: {
			                name: 'list',
			                pull: false,
			                put: true
			            },
			            animation: 120,
			            onSort: function (evt) {
							var newhtml = '<div style="height: 34px;line-height: 28px;overflow: hidden;border: solid 1px #9B9B9B;padding: 3px 0;">'+
										'<div style="float: left;width: 33.3%;">'+
												evt.item.textContent.split(" ")[0]+
										'</div>'+
										'<div style="float: left;width: 33.3%;">'+
												evt.item.textContent.split(" ")[1]+
										'</div>'+
										'<div style="float: left;width: 33.3%;text-align: right;padding-right: 5px;">'+
											'<button onClick="window.wvm.deleteById('+evt.item+')" type="button" class="ivu-btn ivu-btn-small ivu-btn-error ivu-btn-circle ivu-btn-icon-only">'+
												'<!----><i class="ivu-icon ivu-icon-close"></i> <!---->'+
											'</button>'+
										'</div>'+
									'</div>'
							
							evt.item.innerHTML = newhtml
							log('6',evt)
						},
			            onRemove (event) {
                            log(event.item);
                            event.item.setAttribute('style',"display:none");
			            	log(event)
			           }
			        });
				}
			}
		}
	}
</script>

<style>

</style>