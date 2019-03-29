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
							<span>车辆管理</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.cphLike" placeholder="请输入车牌号" style="width: 200px" @on-keyup.enter="findMessList()"></Input>
							<Input v-model="param.zdbhLike" placeholder="请输入终端编号" style="width: 200px" @on-keyup.enter="findMessList()"></Input>
							<Input v-model="param.sjxmLike" placeholder="请输入司机姓名" style="width: 200px" @on-keyup.enter="findMessList()"></Input>
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
						:columns="tableTiT"
						:data="tableData"
				></Table>
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

	export default {
	  name:'char',
      components: {
        expandRow,newmes,allmes,bkShow
      },
    	mixins:[mixins],
        data () {
            return {
            	mess:{},
            	derMes:{
            		sjId:'',
            		sjxm:''
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
                tableTiT: [
					 // {
                    //     title:'#',
                    //     type: 'expand',
                    //     width: 50,
                    //     render: (h, params) => {
                    //         return h(expandRow, {
                    //             props: {
                    //                 row: params.row
                    //             }
                    //         })
                    //     }
                    // },
					{
						title: "序号",
						width: 80,
						align: 'center',
						type: 'index'
					},
                    {
                        title: '车牌号',
                        width:'100',
                        align:'center',
                        key: 'cph'
                    },
                    {
                        title: '车型',
                        align:'center',
                        key: 'cx',
                        render:(h,p)=>{
                            let val = this.dictUtil.getValByCode(this,this.cxDictCode,p.row.cx)
                            return h('div',val)
                        }
                    },
                    {
                        title: '载客量',
                        align:'center',
                        width:80,
                        key: 'zkl'
                    },
                    // {
                    //     title: '创建人',
                    //     width:160,
                    //     align:'center',
                    //     key: 'cjr'
                    // },
                    // {
                    //     title: '创建时间',
                    //     width:150,
                    //     align:'center',
                    //     key: 'cjsj'
                    // },
                    {
                        title: '司机',
                        align:'center',
                        key: 'sjxm',
                        render:(h,p)=>{
                            if (!p.row.sjxm)return h('div','-');
                            return h('div', [
                                h('span',p.row.sjxm+"  "),
                                h('Tooltip',
                                    {props: {placement: 'top', content: '解绑',},style:{float:'right'}},
                                    [
                                        h('Button', {
                                            props: {type: 'success', icon: 'code-working', shape: 'circle', size: 'small'},
                                            style: {marginRight: '5px'},
                                            on: {
                                                click: () => {
                                                    this.unbindDriver(p.row.clId);
                                                }
                                            }
                                        }),
                                    ]
                                ),
                            ]);
                        }
                    },
                    {
                        title: '车辆状态',
                        width:90,
                        align:'center',
                        key: 'zt',
                        render:(h,p)=>{
                            let val = this.dictUtil.getValByCode(this,this.clztDictCode,p.row.zt)
                            return h('div',{
                                style: {
                                    color: val=='正常' ?'#279a3b':'#ed3f14'
                                }
                            },val)
                        }
                    },
                    {
                        title: 'OBD编码',
                        align:'center',
                        key: 'obdCode'
                    },
                    {
                        title: '终端编号',
                        align:'center',
                        key: 'zdbh',
						render:(h,p)=>{
                            if (!p.row.zdbh)return h('div','-');
                            return h('div', [
                                h('span',p.row.zdbh+"  "),
                                h('Tooltip',
                                    {props: {placement: 'top', content: '解绑',},style:{float:'right'}},
                                    [
                                        h('Button', {
                                            props: {type: 'success', icon: 'code-working', shape: 'circle', size: 'small'},
                                            style: {marginRight: '5px'},
                                            on: {
                                                click: () => {
                                                    this.unbindDevice(p.row.clId);
                                                }
                                            }
                                        }),
                                    ]
                                ),
                            ]);
						}
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 180,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '编辑车辆',
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
                                                    this.messType = false
                                                    this.mess = params.row
                                                    //由于数据传递丢失 司机ID 司机 姓名 单独传递
                                                    this.derMes.sjId = params.row.sjId
                                                    this.derMes.sjxm = params.row.sjxm
                                                    this.compName = newmes
                                                }
                                            }
                                        }),
                                    ]
                                ),
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '车辆档案信息',
                                        },
                                    },
									[
                                        h('Button', {
                                            props: {
                                                type: 'primary',
                                                icon: 'clipboard',
                                                shape: 'circle',
                                                size: 'small'
                                            },
                                            style: {
                                                marginRight: '5px'
                                            },
                                            on: {
                                                click: () => {
                                                    // log('数据信息获取',params.row)
                                                    this.mess = params.row
                                                    // debugger
                                                    this.compName = allmes
                                                }
                                            }
                                        })
									]
                                ),
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '历史轨迹',
                                        },
                                    },
                                    [
                                        h('Button', {//历史轨迹
                                            props: {
                                                type: 'warning',
                                                icon: 'map',
                                                shape: 'circle',
                                                size: 'small'
                                            },
                                            style: {
                                                marginRight: '5px'
                                            },
                                            on: {
                                                click: () => {
                                                    this.$router.push(
                                                        {
                                                            name: 'historyTarck_new',
                                                            params:{zdbh:params.row.zdbh}
                                                        }
                                                    );
                                                },
                                            }
                                        }),
                                    ]
                                ),
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '电子围栏',
                                        },
                                    },
                                    [
                                        h('Button', {//电子围栏展示
                                            props: {
                                                type: 'primary',
                                                icon: 'ios-globe-outline',
                                                shape: 'circle',
                                                size: 'small'
                                            },
                                            style: {
                                                marginRight: '5px'
                                            },
                                            on: {
                                                click: () => {
                                                    this.compName = bkShow
                                                    this.mess = params.row
                                                }
                                            }
                                        }),
                                    ]
                                ),
                                h('Tooltip',
                                    {
                                        props: {
                                            placement: 'top',
                                            content: '删除车辆',
                                        },
                                    },
                                    [
                                        h('Button', {// 删除
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
                    }
                ],
                tableData: [],
                //收索
				param: {
                    cphLike: '',
					pageNum: 1,
					pageSize:8
				},
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
            this.getmess()
			this.getCxDict();
			this.getClztDict();
        },
        methods: {
            getClztDict(){
                this.clztDict = this.dictUtil.getByCode(this,this.clztDictCode);
            },
            getCxDict(){
                this.cxDict = this.dictUtil.getByCode(this,this.cxDictCode);
            },
        	getmess(){
				var v = this
				this.$http.get(this.apis.CLGL.QUERY,{params:v.param}).then((res) =>{
					v.tableData = res.page.list
					v.pageTotal = res.page.total
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
                                this.getmess();
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
                                this.getmess();
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
                this.getmess()
        	},
        	listDele(id){
                this.util.del(this,this.apis.CLGL.DELE,[id.clId],()=>{
                    this.getmess()
				})
			},
            pageChange(event){
                this.param.pageNum = event
                this.getmess()
        	},
        }
    }
</script>
