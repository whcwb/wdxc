<style lang="less">
    @import '../../../../../styles/common.less';

</style>
<!--订单审核-->
<template>
    <div class="box boxbackborder">
			<Row class="margin-top-10" style='background-color: #fff;position: relative;'>
    			<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
				<div style="height: 45px;line-height: 45px;">
					<div class="margin-top-10 box-row">
						<div class="titmess">
							<span>订单审核</span>
						</div>
						<div class="body-r-1 inputSty">
							<Cascader style="width:300px;float: right;margin-top: 7px;margin-left: 4px;padding-right: 16px;" @on-change="change" change-on-select :data="orgTree"  placeholder="请选择用车单位"  filterable clearable  ></Cascader>
							<Input v-model="param.ckLike" type="text" placeholder="输入乘客姓名查询" style="width: 220px"></Input>
							<Input v-model="param.sjxmLike" type="text" placeholder="输入司机姓名查询" style="width: 220px"></Input>						</div>
						<div class="butevent">
							<Button type="primary" @click="findMessList()">
								<Icon type="md-search"></Icon>
								<!--查询-->
							</Button>
						</div>
					</div>
				</div>
			</Row>
			<div class="body">
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
			</div>
		<Modal
		    v-model="showModal"
		    width='800'
			:closable="false"
		    :mask-closable="false"
		    title="请填写驳回原因">
			<div>
				<Form ref="formValidate" :model="formValidate"
					  :rules="ruleValidate" :label-width="80">
					<FormItem label="原因" prop="bhyy">
						<Input v-model="formValidate.bhyy" type="textarea" placeholder="请填写驳回原因"></Input>
					</FormItem>
				</Form>
			</div>
		    <div slot='footer'>
				<Button type="text" @click="showModal=false,bhyy=''"style="color: #949494">取消</Button>
				<Button type="primary" @click="handleSubmit('formValidate')">提交</Button>
			</div>
		</Modal>
    </div>
</template>

<script>

	import mixins from '@/mixins'
	import swal from 'sweetalert2'
//	import axios from '@/axios'
	export default {
    	name:'char',
    	mixins:[mixins],
        data () {
            return {
                formValidate: {
                    bhyy: ''
                },
                ruleValidate: {
                    bhyy: [
                        { required: true, message: '请认真填写驳回原因', trigger: 'blur' }
					]
                },
                //tab高度
                tabHeight: 220,
            	PickerTime:2017,
            	//分页
            	pageTotal:1,
            	page:{
            		pageNum:1,
            		pageSize:13
            	},
            	//弹层
            	showModal:false,
                tableTiT: [
                	{
	                	title:"序号",
	                	width:80,
	                	align:'center',
	                	type:'index'
	                },
	                {
                        title: '订单编号',
                        width:180,
                        align:'center',
                        key: 'id'
                    },
                    {
                        title: '用车单位',
                        align:'center',
                        key: 'jgmc'
                    },
                    {
                        title: '用车人',
                        align:'center',
                        key: 'ck'
                    },
                    {
                        title: '联系电话',
                        align:'center',
                        key: 'cklxdh'
                    },
                    {
                        title: '候车地点',
                        align:'center',
                        key: 'hcdz'
                    },
                    {
                        title: '目的地',
                        align:'center',
                        key: 'mdd'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 150,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'success',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.pass(params.row.id)
                                        }
                                    }
                                },'通过'),
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.reject(params.row.id)
                                        }
                                    }
                                },'驳回')
                            ]);
                        }
                    }
                ],
                tableData: [],
                //form表单
                formTop: {
                },
                //select
                cityList: [
                ],
                //收索
                datetime:[],
                param:{
                    ckLike:'',
					ddzt:'10',
                	pageNum:1,
            		pageSize:13
                },
				messError:'',
                idMs:'',
                treeValue:[],
                orgTree:[],
            }
        },
        // watch: {
        //     datetime:function(newQuestion, oldQuestion){
			// 	this.param.cjsjInRange = this.getdateParaD(newQuestion[0]) + ',' + this.getdateParaD(newQuestion[1])
			// },
        // },
        created(){
        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '车辆管理',
            },{
                title: '订单管理',
            },{
                title: '订单审核',
            }]),
			this.tabHeight = this.getWindowHeight() - 295
            this.findMessList();
            this.getOrgTree();
        },
        methods: {
            getOrgTree(){
                this.$http.get(this.apis.FRAMEWORK.GET_TREE_Node).then((res) =>{
                    this.orgTree = res.result
                })
            },
            change(vaule,selectedData){
                this.param.jgdm=selectedData[selectedData.length-1].value
                this.treeValue = vaule;
            },
        	changeTime(val){
//      		this.param.gte_StartTime=val[0]
//      		this.param.lte_StartTime=val[1]
//      		log(this.param)
//      		this.findMessList()
        	},
        	print(){
        		window.print()
        	},
            listEve(num,event){
        		log('event',event)
        		log('num',num)
        		var v = this
				swal({
				  title: "审核通过?",
				  text: "",
				  icon: "success",
				  buttons:['取消','确认'],
				})
				.then((willDelete) => {
				  if (willDelete) {
				  	this.$Message.success('订单审核通过');
				  } else {
				  	this.$Message.error('订单审核失败');
				  }
				});
        	},

			pass(id){
                swal({
                    title: "审核通过?",
                    text: "",
                    icon: "success",
                    buttons:['取消','确认'],
                }).then((willDelete) => {
                        if (willDelete) {
                            this.audit(id,'11');
                        }
                    });
			},
			reject(id){
                // swal({
                //     title: "审核驳回?",
                //     text: "",
                //     icon: "warning",
                //     buttons:['取消','确认'],
                // }).then((willDelete) => {
                //     if (willDelete) {
                        this.showModal = true
						this.idMs = id
                //     }
                // });
			},
            handleSubmit (name) {
        	    var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        v.audit(v.idMs,'12');
                    } else {
                        this.$Message.error('请填写驳回信息');
                    }
                })
            },
            audit(id,zt){
        	    let param = {
        	        ddzt:zt,
					id:id,
                    bhyy:this.formValidate.bhyy
				}
                this.$http.post(this.apis.ORDER.AUDIT,param).then((res) =>{
                    if (res.code === 200){
                        this.$Message.success(res.message);
                        this.findMessList();
                        this.showModal = false
                    }
                })
			},
        	GetMess(page){
        		var v = this
//      		log(page)
        	},
            pageChange(event){
        		var v = this
        		v.page.pageNum = event
                this.param.pageNum = event;
        		v.findMessList()
        	},
            findMessList(){
                this.$http.get(this.apis.ORDER.QUERY,{params:this.param}).then((res) =>{
                    if (res.code === 200 && res.page.list){
                        this.tableData = res.page.list;
                        this.pageTotal = res.page.total;
                    }
                })
            },

        }
    }
</script>
