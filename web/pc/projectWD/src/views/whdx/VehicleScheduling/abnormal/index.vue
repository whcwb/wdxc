<style lang="less">
    @import '../../../../styles/common.less';

</style>
<!--异常记录-->
<template>
    <div class="box boxbackborder">
		<!--<Card>-->
			<Row class="margin-top-10" style='background-color: #fff;position: relative;'>
    			<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
				<div style="height: 45px;line-height: 45px;">
					<div class="margin-top-10 box-row">
						<div class="titmess">
							<span>{{$t("EVENT_RECORD")}}</span>
						</div>
						<div class="body-r-1 inputSty">
							<Select v-model="param.cx"
								@on-change = 'findMessList'
								clearable
									:placeholder='$t("CAR_TYPE")'
								filterable style="width: 160px;">
				                <Option v-for="(item,index) in carType"
				                	:value="item.key"
				                	style="text-align: left;"
				                	:key="index">{{item.val}}</Option>
				            </Select>
							<Input v-model="param.cphLike" :placeholder='$t("CAR_NUMBER")' style="width: 160px;"></Input>
							<Input v-model="param.zdbhLike" :placeholder='$t("TERMINAL_NUM")' style="width: 160px;"></Input>
				            <Select v-model="param.sjlx"
				            	@on-change = 'findMessList'
				            	clearable
									:placeholder='$t("EVENT_TYPE")'
				            	filterable style="width: 160px;">
				                <Option v-for="(item,index) in thingType"
				                	:value="item.key"
				                	style="text-align: left;"
				                	:key="index">{{item.val}}</Option>
				            </Select>
							<DatePicker v-model="cjsjInRange"
								@on-change = 'findMessList'
								format="yyyy-MM-dd" type="daterange"
								placement="bottom-end" :placeholder='$t("TIME_RESEARCH")'
								@on-keyup.enter="findMessList()" style="width: 180px"></DatePicker>
						</div>
						<div class="butevent">
							<Button type="primary" @click="findMessList()">
								<Icon type="md-search"></Icon>
								<!--查询-->
							</Button>
						</div>
					</div>
				</div>
			</Row>
			<Row>
				<Table ref="table"
						:height="tabHeight"
						:row-class-name="rowClassName"
						:columns="tableTiT"
						:data="tableData"></Table>
			</Row>
			<Row class="margin-top-10 pageSty">
				<Page :total=pageTotal
					  :current=param.pageNum
					  :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}'
					  show-total
					  show-elevator show-sizer placement='top'
					  @on-change='pageChange'></Page>
			</Row>
		<!--</Card>-->
    </div>
</template>

<script>
	import mixins from '@/mixins'
	import i18nTabTit from '@/mixins/i18nTabTit'


	export default {
    	name:'char',
    	mixins:[mixins,i18nTabTit],
        data () {
            return {
            	SpinShow:true,
				tabHeight: 220,
            	//分页
            	pageTotal:1,
            	page:{
            		pageNum:1,
            		pageSize:13
            	},
                tableTiT: [
                	{
	                	title:"序号",
						tit:"ORDER",
	                	width:80,
	                	align:'center',
	                	type:'index'
	                },
                    {
                        title: '车牌号',
						tit:"CAR_NUM_TAB",
                        key: 'cph',
                        align: 'center',
                    },
                    {
                        title: '终端编号',
						tit:"TERMINAL_NUMBER",
                        key: 'zdbh',
                        align: 'center',
                    },
                    {
                        title: '事件类型',
						tit:"EVENT_TYPE_TAB",
                        key: 'sjlx',
                        align: 'center',
                        render: (h, p) => {
							let val = this.dictUtil.getValByCode(this,this.dicSJcode,p.row.sjlx)
							return h('div',val)
						}
                    },
                    {
                        title: '驾驶员',
						tit:"DRIVER",
                        key: 'sjxm',
                        align: 'center',
                    },
                    {
                        title: '车型',
						tit:"CAR_TYPE_TAB",
                        key: 'cx',
                        align: 'center',
                        render: (h, p) => {
							let val = this.dictUtil.getValByCode(this,this.dicCarCode,p.row.cx)
							return h('div',val)
						}
                    },
                    {
                        title: '发生时间',
						tit:"OCCURRENCE_TIME",
                        key: 'cjsj',
                        align: 'center',
                    }
                ],
                tableData: [
                ],
                //车型
                carType:[],
                //车牌号码
                carnumber:[],
                //事件类型
                thingType:[],
                //收索时间
                cjsjInRange:[],
                param:{
                	cx:'',
                	cph:'',
                	sjlx:'',
                	cjsjInRange:[],
					pageNum:1,
					pageSize:13
                },
               	dicCarCode:'ZDCLK0019',
               	dicCarList:[],
               	dicSJcode:'ZDCLK0038',
               	dicSJlist:[]
            }
        },
        watch: {
			cjsjInRange:function(newQuestion, oldQuestion){
				if (newQuestion.length > 0 && newQuestion[0] != ''){
					this.param.cjsjInRange = this.getdateParaD(newQuestion[0]) + ',' + this.getdateParaD(newQuestion[1])
				}else{
					this.param.cjsjInRange  = ''
				}
				log('newQuestion',newQuestion)
			},
		},
        created(){
    	    var dae = new Date()
			var date1 = new Date();
    	    date1.setDate(date1.getDate() + 1);
            this.cjsjInRange = [dae,date1]

        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '车辆调度',
            },{
                title: '异常行驶记录',
            }]),
			this.tabHeight = this.getWindowHeight() - 295
            this.getmess()
            this.getLXDic()
        },
        methods: {
        	getLXDic(){
        		var v = this
                this.carType = this.dictUtil.getByCode(this,this.dicCarCode);
            	this.thingType = this.dictUtil.getByCode(this,this.dicSJcode);
            	this.$http.get(this.apis.CLGL.QUERY).then((res) =>{
            		if(res.code == 200){
            			v.carnumber = res.page.list
            		}
				})
            	log('车型',this.carType)
            	log('事件',this.thingType)
			},
        	getmess(){
        	    this.findMessList();
			},
            pageChange(event){
        		var v = this
        		v.param.pageNum = event
        		v.findMessList()
//      		log(v.page)
        	},
        	findMessList(){
        		var v = this
        		v.SpinShow = true;
                // if (v.param.sjlx == ''){
                 //    v.param.sjlxIn = '10,20,30,40,50,60';
				// }else{
                 //    delete v.param.sjlxIn
				// }
        		this.$http.get(this.apis.CLSBYXJL.QUERY,{params:v.param}).then((res) =>{
					log('数据',res)
					v.tableData = res.page.list
                    v.pageTotal = res.page.total;
					v.SpinShow = false;
				})
        	},

        }
    }
</script>
