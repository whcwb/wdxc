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
							<span>智能站牌</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.mcLike" placeholder="请输入站牌名称" style="width: 200px" @on-keyup.enter="getPageData()"></Input>
						</div>
						<div class="butevent">
							<Button type="primary" @click="getPageData()">
								<Icon type="md-search"></Icon>
								<!--查询-->
							</Button>
							<Button type="primary" @click="choosedRow = null;componentName = 'formData'">
								<Icon type="md-add"></Icon>
							</Button>
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
				<Page :total=pageTotal :current=param.pageNum :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator show-sizer placement='top' @on-change='pageChange'></Page>
			</Row>
		</Card>
		<component :is="componentName"></component>
	</div>
</template>

<script>
	import mixins from '@/mixins'

	import formData from './formData'
	export default {
    	name:'char',
    	mixins:[mixins],
		components:{
            formData
		},
        data () {
            return {
            	PickerTime:2017,
                SpinShow:false,
            	//分页
                loading:this.$store.state.app.loading,
            	pageTotal:1,
                cjsjInRange:'',
                tabHeight: 220,
				choosedRow:null,
                componentName:'',
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
                        title: '厂商',
                        align:'center',
                        key: 'cs'
                    },
                    {
                        title: '地址',
                        align:'center',
                        key: 'dz'
                    },
                    {
                        title: '在线状态',
                        align:'center',
                        key: 'zxzt',
						render:(h,p)=>{
                            let s = p.row.zxzt == '00' ? '在线' :'离线';
                            return h('div',s);
						}
                    },
                    // {
                    //     title: '创建时间',
                    //     align:'center',
                    //     key: 'cjsj'
                    // },
                    // {
                    //     title: '创建人',
                    //     align:'center',
                    //     key: 'cjr'
                    // },
                    {
                        title:'操作',
                        align:'center',
                        type: 'action',
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
                                            this.choosedRow = params.row;
                                            this.componentName = 'formData';
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
                datetime:[],
                param:{
                    mcLike:'',
                	pageNum:1,
            		pageSize:8
                }
            }
        },
        created(){
        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '系统管理',
            },{
                title: '智能站牌',
            }]),
            this.tabHeight = this.getWindowHeight() - 300,
			this.getPageData();
        },
        methods: {
    	    getPageData(){
                if (this.cjsjInRange.length != 0 && this.cjsjInRange[0] != '' && this.cjsjInRange[1] != ''){
                    this.param.cjsjInRange = this.getdateParaD(this.cjsjInRange[0])+","+this.getdateParaD(this.cjsjInRange[1]);
                }else{
                    this.param.cjsjInRange = '';
                }
                this.$http.get(this.apis.ZNZP.QUERY,{params:this.param}).then((res) =>{
                    this.SpinShow = false;
                    if(res.code===200){
                        this.tableData = res.page.list;
                        this.pageTotal = res.page.total;
                    }
                })
			},
			pageChange(e){
    	        this.param.pageNum = e;
    	        this.getPageData();
			},
            //删除数据
            listDele(r){
                this.util.del(this,this.apis.ZNZP.DELE,[r.zdbh])
            },
            pageChange(event){
                this.param.pageNum = event;
                this.getPageData();
            }
        }
    }
</script>
