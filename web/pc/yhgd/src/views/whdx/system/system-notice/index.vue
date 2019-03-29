<style lang="less">
    @import '../../../../styles/common.less';

</style>
<!--查询统计-->
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
							<span>设备终端</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.mcLike" placeholder="请输入终端名称" style="width: 200px" @on-keyup.enter="getPageData()"></Input>
							<Input v-model="param.zdbhLike" placeholder="请输入终端编号" style="width: 200px" @on-keyup.enter="getPageData()"></Input>
							<Input v-model="param.cphLike" placeholder="请输入车牌号" style="width: 200px" @on-keyup.enter="getPageData()"></Input>
							<Input v-model="param.versionLike" placeholder="请输入版本号" style="width: 200px" @on-keyup.enter="getPageData()"></Input>
							<Select v-model="param.sjlx"
									@on-change = 'getPageData'
									clearable
									placeholder="请选择在线状态"
									filterable style="width: 160px;">
								<Option v-for="(item,index) in Dictionary"
										:value="item.key"
										style="text-align: left;"
										:key="index">{{item.val}}</Option>
							</Select>
						</div>
						<div class="butevent">
							<Tooltip content="查询" placement="top">
								<Button type="primary" @click="getPageData()">
									<Icon type="md-search"></Icon>
								</Button>
							</Tooltip>
							<Tooltip content="新增" placement="top">
								<Button type="primary" @click="AddMess()">
									<Icon type="md-add"></Icon>
								</Button>
							</Tooltip>
							<Tooltip content="批量导入" placement="top">
								<Button type="success" @click="componentName='plmess'">
									<Icon type="arrow-return-left"></Icon>
								</Button>
							</Tooltip>
							<Tooltip content="升级设备" placement="top">
								<Button type="warning" @click="batchUpdate()">
									<Icon type="arrow-up-a"></Icon>
								</Button>
							</Tooltip>
						</div>
					</div>
				</div>
			</Row>
			<Row>
				<Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="columns" :data="tableData"></Table>
				<div v-if="SpinShow" style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 100;">
					<Spin fix>
						<Icon type="load-c" :size=loading.size class="demo-spin-icon-load"></Icon>
						<div style="font-size: 30px;">{{loading.text}}</div>
					</Spin>
				</div>
			</Row>
			<Row class="margin-top-10 pageSty">
				<Page :total=pageTotal :current=param.pageNum :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{console.log(e);param.pageSize=e;pageChange(1)}' show-total show-elevator show-sizer placement='top' @on-change='pageChange'></Page>
			</Row>
		</Card>
		<component
			:is="componentName"
			:mess="choosedRow"></component>
	</div>
</template>

<script>
	import mixins from '@/mixins'
	import formData from './formData'
	import change from './change'
	import jkdz from './jkdz'
	import update from './update'
	import setting from './setting'
	import plmess from './plMess'
	export default {
    	name:'char',
    	mixins:[mixins],
		components:{
            formData,change,jkdz,setting,update,plmess
		},
        data () {
            return {
            	PickerTime:2017,
				updateMode:'single',
                SpinShow:false,
            	//分页
                loading:this.$store.state.app.loading,
            	pageTotal:1,
                cjsjInRange:'',
                tabHeight: 220,
                componentName:'',
                choosedRow:{},
            	page:{
            		pageNum:1,
            		pageSize:8
            	},
            	//弹层
            	showModal:false,
                columns: [
                	{
	                	title:"序号",
	                	width:80,
	                	align:'center',
	                	type:'index'
	                },
                    {
                        title: '名称',
                        align:'center',
                        key: 'mc'
                    },
                    {
                        title: '终端编号',
                        align:'center',
                        key: 'zdbh'
                    },
                    {
                        title: '绑定车辆',
                        align:'center',
                        key: 'cph',
						render:(h,p)=>{
                            let s = p.row.cph ? p.row.cph : '-';
                            return h('div',s);
						}
                    },
                    {
                        title: '超速设定',
                        align:'center',
                        key: 'cssd'
                    },
                    {
                        title: '碰撞灵敏度',
                        align:'center',
                        key: 'pzlmd',
                        render:(h,p)=>{
                            let val = this.dictUtil.getValByCode(this,'pzlmd',p.row.pzlmd)
                            return h('div',val)
                        }
                    },
                    {
                        title: '视频上传模式',
                        align:'center',
                        key: 'spscms',
                        render:(h,p)=>{
                            let val = this.dictUtil.getValByCode(this,'scspms',p.row.spscms)
                            return h('div',val)
                        }
                    },
                    {
                        title: '接口地址',
                        align:'center',
                        key: 'cmd',
                    },
                    {
                        title: '版本号',
                        align:'center',
                        key: 'version',
                    },
                    {
                        title: '在线状态',
                        align:'center',
                        key: 'zxzt',
                        render:(h,p)=>{
	                     	let val = this.dictUtil.getValByCode(this,this.lmdmDictionary,p.row.zxzt)
	    					return h('div',{
	    						style:{
	    							color:p.row.zxzt=="00" ? '#279a3b':'#ed3f14'
	    						}
	    					},val)
                        }
                    },
                    {
                        title: '终端状态',
                        align:'center',
                        key: 'zt',
                        render:(h,p)=>{
	                     	let val = this.dictUtil.getValByCode(this,this.ztlmdmDictionary,p.row.zt)
	    					return h('div',{
	    						style:{
	    							color:p.row.zt=="00" ? '#279a3b':'#ed3f14'
	    						}
	    					},val)
                        }
                    },
                    {
                        title:'操作',
                        align:'center',
                        type: 'action',
                        width: 200,
                        render: (h, params) => {
                            return h('div', [
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '设备升级',
                                        },
                                    },
                                    [
                                        h('Button', {
                                            props: {
                                                type: 'warning',
                                                icon: 'arrow-up-a',
                                                shape: 'circle',
                                                size: 'small'
                                            },
                                            style: {
                                                marginRight: '5px'
                                            },
                                            on: {
                                                click: () => {
                                                    this.updateMode = 'single'
                                                    this.choosedRow = params.row
                                                    this.componentName = 'update'
                                                }
                                            }
                                        }),
                                    ]
                                ),
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '信息编辑',
                                        },
                                    },
                                    [
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
                                                    this.choosedRow = params.row
                                                    this.componentName = 'change'
                                                }
                                            }
                                        }),
                                    ]
                                ),
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '设置接口地址',
                                        },
                                    },
                                    [
                                        h('Button', {
                                            props: {
                                                type: 'primary',
                                                icon: 'ios-pulse-strong',
                                                shape: 'circle',
                                                size: 'small'
                                            },
                                            style: {
                                                marginRight: '5px'
                                            },
                                            on: {
                                                click: () => {
                                                    this.choosedRow = params.row
                                                    this.componentName = 'jkdz'
                                                }
                                            }
                                        }),
                                    ]
                                ),
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '终端设置',
                                        },
                                    },
                                    [
                                        h('Button', {
                                            props: {
                                                type: 'primary',
                                                icon: 'gear-b',
                                                shape: 'circle',
                                                size: 'small'
                                            },
                                            style: {
                                                marginRight: '5px'
                                            },
                                            on: {
                                                click: () => {
                                                    this.choosedRow = params.row
                                                    this.componentName = 'setting'
                                                }
                                            }
                                        }),
                                    ]
                                ),
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '终端删除',
                                        },
                                    },
                                    [
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
                                    ]
                                ),
                            ]);
                        }
                    },
                ],
                tableData: [

                ],
                //form表单
                formTop: {
                },
                //select
                cityList: [
                ],
                //收索
                param:{
                    mcLike:'',
                	pageNum:1,
            		pageSize:8
                },
                Dictionary:[],
				lmdmDictionary:'ZDCLK0032',//在线状态
				ztDictionary:[],
				ztlmdmDictionary:'ZDCLK0031'//设备状态
            }
        },
        created(){
        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '系统管理',
            },{
                title: '设备终端',
            }]),
            this.tabHeight = this.getWindowHeight() - 300
			this.getPageData()
			this.getLXDic()
        },
        methods: {
            batchUpdate(){
                this.choosedRow = null;
                this.updateMode = 'batch'
                this.componentName = 'update';
			},
        	getLXDic(){
                this.Dictionary = this.dictUtil.getByCode(this,this.lmdmDictionary);
                this.ztDictionary = this.dictUtil.getByCode(this,this.ztlmdmDictionary);
        	},
    	    getPageData(){
                debugger
                console.error(this.param);
                this.$http.get(this.apis.ZDGL.QUERY,{params:this.param}).then((res) =>{
                    this.SpinShow = false;
                    if(res.code===200){
                        this.tableData = res.page.list;
                        this.pageTotal = res.page.total;
                        log('数据结构',this.tableData)
                    }
                })
			},
			pageChange(e){
    	        this.param.pageNum = e;
    	        this.getPageData();
			},
			//新增数据
			AddMess(){
				this.componentName = 'formData'
				this.choosedRow = null;
			},
            //删除数据
            listDele(r){
            	var v = this
		swal({
		  title: "是删除数据?",
		  text: "",
		  icon: "warning",
		  buttons:['取消','确认'],
		})
		.then((willDelete) => {
		  if (willDelete) {
			v.$http.post(this.apis.ZDGL.DELE,{'ids':[r.zdbh]}).then((res) =>{
				if(res.code===200){
					this.$Message.success('操作成功');
				}
				v.getPageData()
			})
		  } else {
			this.$Message.error('操作失败');
		  }
		})
            },
            pageChange(event){
                this.param.pageNum = event;
                this.getPageData();
            }
        }
    }
</script>
