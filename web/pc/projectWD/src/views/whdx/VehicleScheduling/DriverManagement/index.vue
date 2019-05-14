<style lang="less">
    @import '../../../../styles/common.less';
</style>
<!--驾驶员管理-->
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
							<span>{{$t("DRIVER_MANAGEMENT")}}</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.xmLike" :placeholder='$t("DRIVER_NAME")' style="width: 200px" @on-keyup.enter="findMessList()"></Input>
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
			<Row>
				<Table ref="table"
						:height="tabHeight"
						:row-class-name="rowClassName"
						:columns="tableTiT"
						:data="tableData"></Table>
			</Row>
			<Row class="margin-top-10" style="text-align: right;">
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
			:messType="messType"></component>
    </div>
</template>

<script>
	import mixins from '@/mixins'
	import i18nTabTit from '@/mixins/i18nTabTit'
	import newmes from './comp/newmes.vue'
	export default {
    	name:'char',
    	components: {
			newmes
        },
    	mixins:[mixins,i18nTabTit],
        data () {
            return {
            	mess:{},
            	messType:true,
            	compName:'',

            	SpinShow:true,
				tabHeight: 220,
            	PickerTime:2017,
            	//分页
            	pageTotal:1,
            	page:{
            		pageNum:1,
            		pageSize:8
            	},
                tableTiT: [
                  	{
  	                	title:"序号",
						tit:"ORDER",
  	                	width:40,
  	                	align:'center',
  	                	type:'index',
                      fixed: 'left'
  	                },
                    {
                        title: '姓名',
						tit:"Name",
                        width:110,
                        align:'center',
                        key: 'xm',
                        fixed: 'left'
                    },
  	                {
                        title: '性别',
						tit:"GENDER",
                        width:60,
                        align:'center',
                        key: 'xb',
                        render: (h, params) => {
                            return h('div',params.row.xb == '1' ? '男' : '女')
                        }
                    },
                    {
                        title: '年龄',
						tit:"AGE",
						width:80,
                        align:'center',
                        key: 'nl'
                    },
                    {
                        title: '身份证号',
						tit:"ID_NUMBER",
                        align:'center',
                        key: 'sfzhm'
                    },
                    {
                        title: '血型',
						tit:"BLOOD_TYPE",
                        align:'center',
                        key: 'xx'
                    },
                    {
                        title: '健康状况',
						tit:"HEALTH_STATUS",
                        align:'center',
                        key: 'jkzk'
                    },
                    {
                        title: '驾照有效期',
						tit:"DRIVER_LICENSE_VALID",
                        align:'center',
                        key: 'jzyxq'
                    },
                    {
                        title: '状态',
						tit:"STATUS",
                        width:80,
                        align:'center',
                        key: 'zt',
                        render:(h,p)=>{
                            let val = this.dictUtil.getValByCode(this,this.ztDictCode,p.row.zt);
                            return h('div',[
                                h('i-switch',{
                                    props:{
										size:'large',
										value:p.row.zt =='00' ? true:false,
									},
                                    on:{
                                        'on-change':(value)=>{
                                            let rzt = value ? '00':'10'
                                            this.$http.post(this.apis.JSY.jsytype,{'sfzhm':p.row.sfzhm,'zt':rzt}).then((res) =>{
                                                log('驾驶员数据',res)
												if(res.code==200){
                                                    this.$Message.success(res.message);
                                                }else{
                                                    this.$Message.error(res.message);
												}
												this.getmess()
                                            })
										}
									}
								},[
								    h('span',{
										slot:"open"
									},'在班'),
                                    h('span',{
										slot:"close"
                                    },'休息')
								])
							]);
                        }
                    },
                    {
                        title:'驾驶证类型',
						tit:"DRIVER_LICENSE_TYPE",
                        width:120,
                        align:'center',
                        key:'zjcx'
                    },
                    {
                        title:'初领日期',
						tit:"FIRST_ISSUE_DATE",
                        width:150,
                        align:'center',
                        key:'clrq',
                        render:(h,p)=>{
                            return h('div',p.row.clrq.substring(0,10));
                        }

                    },
                    {
                        title:'档案编号',
						tit:"FILE_NUM",
                        width:180,
                        align:'center',
                        key:'dabh'
                    },
                    // {
                    //     title: '创建时间',
                    //     width:150,
                    //     align:'center',
                    //     key: 'cjsj'
                    // },
                    {
                        title: '操作',
						tit:"OPERATION",
                        key: 'action',
                        width: 150,
                        fixed: 'right',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'success',
										icon: 'md-create',
										shape: 'circle',
										size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                        	this.messType = false
                                        	this.mess = params.row
                                            this.compName = 'newmes'
                                        }
                                    }
                                }),
                                h('Button', {
                                    props: {
                                        type: 'error',
										icon: 'md-close',
										shape: 'circle',
										size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.listDele(params.row)
                                        }
                                    }
                                })
                            ]);
                        }
                    }
                ],
                tableData: [],
                //收索
                cjsjInRange:[],
				param: {
					cjsjInRange:'',
					xmLike: '',
					pageNum: 1,
					pageSize:8
				},
                ztDict:[],
                ztDictCode:'ZDCLK0016',
            }
        },
        watch: {
			cjsjInRange:function(newQuestion, oldQuestion){
					this.param.cjsjInRange = this.getdateParaD(newQuestion[0]) + ',' + this.getdateParaD(newQuestion[1])
			},
		},
        created(){
        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '车辆管理',
            },{
                title: '驾驶员管理',
            }]),
			this.tabHeight = this.getWindowHeight() - 290
            this.SpinShow = false;
            this.getmess()
            this.getClztDict()
        },
        methods: {
            getClztDict(){
                this.ztDict = this.dictUtil.getByCode(this,this.ztDictCode);
            },
        	getmess(){
				var v = this
				this.$http.get(this.apis.JSY.QUERY,{params:v.param}).then((res) =>{
					log('驾驶员数据',res)
					v.tableData = res.page.list
					v.pageTotal = res.page.total
					v.SpinShow = false;
				})
			},
        	AddDataList() {
				var v = this
				v.mess = {},
            	v.messType = true,
				v.compName = 'newmes'
			},
        	findMessList(){
        		var v = this
        		v.getmess()
        	},
        	listDele(id){
        		this.$http.post(this.apis.JSY.DELE,{'ids':[id.sfzhm]}).then((res) =>{
					if(res.code===200){
						this.$Message.success('操作成功');
					}else{
						this.$Message.error('操作失败');
					}
					this.getmess()
				})
        	},
            pageChange(event){
        		var v = this
        		v.param.pageNum = event
				v.getmess()
        	},
        }
    }
</script>
