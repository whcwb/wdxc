<style lang="less">
    @import '../../../../styles/common.less';

</style>
<style>
</style>
<!--车辆管理-->
<template>
    <div class="boxbackborder">
		<Card>
			<Row class="margin-top-10" style='background-color: #fff;position: relative;'>
    			<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
				<div style="height: 45px;line-height: 45px;">
					<div class="margin-top-10 box-row">
						<div class="titmess">
							<span>{{$t("CAR_MANAGEMENT")}}</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.cphLike" :placeholder='$t("CAR_NUMBER")' style="width: 200px" @on-keyup.enter="findMessList()"></Input>
							<Input v-model="param.zdbhLike" :placeholder='$t("TERMINAL_NUM")' style="width: 200px" @on-keyup.enter="findMessList()"></Input>
							<Input v-model="param.sjxmLike" :placeholder='$t("DRIVER_NAME")' style="width: 200px" @on-keyup.enter="findMessList()"></Input>
						</div>
						<div class="butevent">
							<Button type="primary" @click="findMessList()">
								<Icon type="md-search"></Icon>
								<!--查询-->
							</Button>
							<Button type="primary" @click="AddDataList()">
								<Icon type="md-add"></Icon>
							</Button>
						</div>
					</div>
				</div>
			</Row>
			<Row :gutter="20" v-for="(row,rowIndex) in tableData" :data="row"  :key="rowIndex">
				<Col span="6" v-for="(col,index) in row" :key="index" style="margin-top: 16px;">
					<car-item  :data="col"
							  @reload="getPageData"
							  @editCar="editCar"
							  @showDoc="showDoc"
							  @trace="trace"
							  @showFance="showFance"
							  @delCar="delCar"
					></car-item>
				</Col>
			</Row>
			<Row class="margin-top-10 pageSty">
				<Page :total=pageTotal
					  :current=param.pageNum
					  :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}'
					  show-total
					  show-elevator show-sizer placement='top'
					  @on-change='pageChange'></Page>
			</Row>
		</Card>
    	<component
			:is="compName"
			:mess="mess"
			:derMess="derMes"
			:messType="messType"></component>
    </div>
</template>

<script>
	import mixins from '@/mixins'
  	import expandRow from './table-expand.vue'
  	import newmes from './comp/newmes.vue'
	import allmes from './comp/otherMess.vue'
	import bkShow from './comp/BKshow.vue'
	import carItem from './comp/carItem'
	export default {
	  name:'char',
      components: {
        expandRow,newmes,allmes,bkShow,carItem
      },
    	mixins:[mixins],
        data () {
            return {
            	mess:{},
            	derMes:{
            		sjId:'',
            		sjxm:''
            	},
				cell:{
                    bindDrvFlag:false
				},
            	messType:true,
            	compName:'',
                clztDict:[],
                clztDictCode:'ZDCLK0016',
                cxDict:[],
                cxDictCode:'ZDCLK0019',
            	SpinShow:true,
				tabHeight: 220,
            	PickerTime:2017,
            	//分页
            	pageTotal:1,
            	page:{
            		pageNum:1,
            		pageSize:8
            	},
                tableData: [],
                //收索
				param: {
                    cphLike: '',
					pageNum: 1,
					pageSize:8
				},
                drivers:[],
                deviceList:[],
            }
        },
        created(){
	      log('123','title')
        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '车辆调度',
            },{
                title: '车辆管理',
            }]),
			this.tabHeight = this.getWindowHeight() - 290
            this.SpinShow = false;
            this.getPageData()
			this.getCxDict();
			this.getClztDict();
        },
        methods: {
            editCar(car){
                this.messType = false
                this.mess = car
                //由于数据传递丢失 司机ID 司机 姓名 单独传递
                this.derMes.sjId = car.sjId
                this.derMes.sjxm = car.sjxm
                this.compName = newmes
            },
            showDoc(car){
                this.mess = car
                this.compName = allmes
            },
            trace(car){
                this.$router.push(
                    {
                        name: 'historyTarck_new',
                        params:{zdbh:car.zdbh}
                    }
                );
            },
			showFance(car){
                this.compName = bkShow
                this.mess = car
			},
			delCar(car){
                this.listDele(car)
			},
            getDeviceList(){//获取终端编号
                let v = this;
                v.$http.post(this.apis.ZDGL.SXQUERY).then((res) =>{
                    if(res.code===200){
                        if(res.result==undefined){
                            res.result = []
                        }
                        log('终端数据',res)
                        this.deviceList = res.result;
                        this.deviceList.push({'zdbh':v.mess.zdbh})
                    }
                })
            },
            getDrivers(){
                let v = this;
                v.$http.get(this.apis.JSY.QUERY,{params:{pageSize:1000}}).then((res) =>{
                    if(res.code===200){
                        v.drivers = res.page.list;
                    }
                })
            },
	      ok(item){

		  },
	      cancel(item){

		  },
	      getCx(code){
              let val = this.dictUtil.getValByCode(this,this.cxDictCode,code)
			  return val;
		  },
            getClztDict(){
                this.clztDict = this.dictUtil.getByCode(this,this.clztDictCode);
            },
            getCxDict(){
                this.cxDict = this.dictUtil.getByCode(this,this.cxDictCode);
            },
        	getPageData(){
				var v = this
                v.tableData = [];
				this.$http.get(this.apis.CLGL.QUERY,{params:v.param}).then((res) =>{
				    if (res.code == 200 && res.page.list){
				        let data = res.page.list;
                        for (let r of data){
                            r.bindDrvFlag = !!r.sjxm;
                        }
				        let colCount = 4;
				        let rowCount = data.length / colCount;
				        if (data.length % colCount != 0){
				            rowCount += 1;
						}
						let array = [];
						for (let i = 0;i<rowCount;i++){
						    let start = i*colCount;
						    let end = start + colCount;
						    array.push(data.slice(start,end));
						}
                        v.tableData = array
                        v.pageTotal = res.page.total
					}

					v.SpinShow = false;
				})
			},
			unbindDevice(carId){
                swal({
                    title: "确定解绑该终端？",
                    text: "",
                    icon: "warning",
                    buttons:['取消','确认'],
                }).then((willDelete) => {
                    if (willDelete) {
                        this.$http.post(this.apis.CLGL.unbindDevice,{carId:carId}).then((res)=>{
                            if (res.code === 200){
                                this.getPageData();
                                this.$Message.success("解绑成功");
                            }else{
                                this.$Message.error(res.message);
                            }
                        })
                    }
                });
			},
			unbindDriver(carId){
                swal({
                    title: "确定解绑驾驶员？",
                    text: "",
                    icon: "warning",
                    buttons:['取消','确认'],
                }).then((willDelete) => {
                    if (willDelete) {
                        this.$http.post(this.apis.CLGL.unbindDriver,{carId:carId}).then((res)=>{
                            if (res.code === 200){
                                this.getPageData();
                                this.$Message.success("解绑成功");
                            }else{
                                this.$Message.error(res.message);
                            }
                        })
                    }
                });
			},
        	AddDataList() {
				var v = this
				v.mess = {}
            	v.messType = true
				v.compName = 'newmes'
			},
        	findMessList(){
                this.getPageData()
        	},
        	listDele(id){
                this.util.del(this,this.apis.CLGL.DELE,[id.clId],()=>{
                    this.getPageData()
				})
			},
            pageChange(event){
                this.param.pageNum = event
                this.getPageData()
        	},
        }
    }
</script>
