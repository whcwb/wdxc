<!--订单查阅-->
<style lang="less">
    @import '../../../../styles/common.less';
</style>
<style>
    .demo-upload-list{
        display: inline-block;
        width: 60px;
        height: 60px;
        text-align: center;
        line-height: 60px;
        border: 1px solid transparent;
        border-radius: 4px;
        overflow: hidden;
        background: #fff;
        position: relative;
        box-shadow: 0 1px 1px rgba(0,0,0,.2);
        margin-right: 4px;
    }
    .demo-upload-list-box{
    	width: 100%;
    	height: 100%;
    }
    .demo-upload-list img{
        width: 100%;
        height: 100%;
        margin: auto;
    }
    .demo-upload-list-cover{
        display: none;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background: rgba(0,0,0,.6);
    }
    .demo-upload-list:hover .demo-upload-list-cover{
        display: block;
    }
    .demo-upload-list-cover i{
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        margin: 0 2px;
    }
</style>
<template>
	<div class="boxbackborder acdive">
		<Card>
			<Row class="margin-top-10" style='background-color: #fff;position: relative;'>
    			<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
				<div style="height: 45px;line-height: 45px;">
					<div class="margin-top-10 box-row">
						<div class="titmess">
							<span>{{$t("ACCIDENT_MANAGEMENT")}}</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.cphLike" :placeholder='$t("CAR_NUMBER")' style="width: 200px" @on-change="findMessList"></Input>
						</div>
						<div class="butevent">
							<Button type="primary" @click="findMessList()">
								<Icon type="md-search"></Icon>
								<!--查询-->
							</Button>
							<Button type="primary" @click="getDataList()">
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
						:data="data9"></Table>
			</Row>
			<Row class="margin-top-10 pageSty">
				<Page
						:total=pageTotal
						:current=param.pageNum
						:page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}'
						show-total
						show-elevator show-sizer placement='top'
						@on-change='pageChange'></Page>
			</Row>
		</Card>
    	<component :is="compName"
    		:mess = 'choosedRow'
    		@colsemodal='colsemodal'></component>
	</div>
</template>
<script>
	import mixins from '@/mixins'
	import i18nTabTit from '@/mixins/i18nTabTit'
    import expandRow from './table-expand.vue';
    import addmess from './comp/addmess.vue'
	import mess from './comp/mess.vue'
    export default {
        components: {
        	expandRow,
        	addmess,
			mess
        },
        mixins:[mixins,i18nTabTit],
        data () {
            return {
            	Carousel:3,
            	SpinShow:true,
				tabHeight: 220,
            	compName: '',
            	//收索
                datetime:[],
                choosedRow:{},
                param:{
                    cphLike:'',
                	pageNum:1,
            		pageSize:8
                },
            	//弹层
            	pageTotal:1,
            	page:{
            		pageNum:1,
            		pageSize:8
            	},
            	showModal:false,
				tableTiT: [
                    {
                        title:'序号',
						tit:"ORDER",
                        type: 'index',
                        align:'center',
                        width: 60,
                    },
                    {
                        title: '车牌号码',
						tit:"CAR_NUM_TAB",
                        align:'center',
                        key: 'cph'
                    },
                    {
                        title: '司机',
						tit:"DRIVER",
                        align:'center',
                        key: 'sj'
                    },
                    {
                        title: '联系电话',
						tit:"TELPHONE_NUM",
                        align:'center',
                        key: 'lxdh'
                    },
                    {
                        title: '发生时间',
						tit:"OCCURRENCE_TIME",
                        align:'center',
                        key: 'sgsj'
                    },
                    {
                        title: '事故描述',
						tit:"ACCIDENT_DESCRIBE",
                        align:'center',
                        key: 'sgms'
                    },
                    {
                        title:'附件',
						tit:"ACCESSORY",
                        type: 'expand',
                        width: 65,
                        render: (h, params) => {
                            return h(expandRow, {
                                props: {
                                    row: params.row
                                }
                            })
                        }
                    },
                    {
                        title:'操作',
						tit:"OPERATION",
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
                                            this.messType = false
                                            this.choosedRow = params.row
                                            this.compName = 'mess'
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
                                            this.remove(params.row.id)
                                        }
                                    }
                                })
                            ]);
                        }
                    },
                ],
                data9: []
            }
        },
        watch:{
        	defaultList:function(n,o){
        	}
        },
        created(){
        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '车辆管理',
            },{
                title: '事故管理',
            }]),
			this.tabHeight = this.getWindowHeight() - 290
			this.SpinShow = false;
     		this.getmess()
        },
        methods:{
        	getmess(){
				var v = this
				v.SpinShow = true;
				this.$http.get(this.apis.SG.QUERY,{params:v.param}).then((res) =>{
					v.data9 = res.page.list
					v.pageTotal = res.page.total
					v.SpinShow = false;
				})
			},
        	changeTime(val){
        	},
        	pageChange(event){
        		var v = this
        	},
        	findMessList(){
        		var v = this
        		v.SpinShow = true;
				this.$http.get(this.apis.SG.QUERY,{params:this.param}).then((res) => {
					 v.data9 = res.page.list
					 v.pageTotal = res.page.total
					 v.SpinShow = false;
				 })
        	},
        	remove(id){
        		this.util.del(this,this.apis.SG.DELE,[id],()=>{
                    this.getmess();
				});
        	},
        	getDataList() {
				var v = this
				this.choosedRow = null;
				v.compName = 'addmess'
				v.choosedRow = null
			},
			colsemodal() {
				this.compName = ''
			},
        }
    }
</script>
