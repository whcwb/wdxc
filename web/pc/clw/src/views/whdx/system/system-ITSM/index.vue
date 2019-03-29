<style lang="less">
	@import '../../../../styles/common.less';
</style>
<!--服务管理-->
<template>
	<div class="boxbackborder box">
		<Card>
			<Row class="margin-top-10" style='background-color: #fff;position: relative;'>
				<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
				<div style="height: 45px;line-height: 45px;">
					<div class="margin-top-10 box-row">
						<div class="titmess">
							<span>服务管理</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.fwmcLike" placeholder="请输服务名称" style="width: 200px" @on-keyup.enter="formList()"></Input>
						</div>
						<div class="butevent">
							<Button type="primary" @click="formList()">
								<Icon type="md-search"></Icon>
								<!--查询-->
							</Button>
							<!--<Button type="primary" @click="AddDataList()">-->
								<!--<Icon type="md-add"></Icon>-->
							<!--</Button>-->
						</div>
					</div>
				</div>
			</Row>
			<Row style="position: relative;">
				<Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="tableTiT" :data="tableData"></Table>
			</Row>
			<Row class="margin-top-10 pageSty">
				<Page :total=pageTotal :current=param.pageNum :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator show-sizer placement='top' @on-change='pageChange'></Page>
			</Row>
		</Card>
		<component
			:is="compName"
			:chmess="chmess"
			:Dictionary="Dictionary"></component>
	</div>
</template>

<script>
	import mixins from '@/mixins'


	import addmess from './comp/addmess.vue'
	import mess from './comp/mess.vue'
	export default {
		name: 'char',
		mixins: [mixins],
		components: {
			addmess,
			mess
		},
		data() {
			return {
				SpinShow:true,
				tabHeight: 220,
				compName: '',
				usermes:{},
				userMesType:true,
				//分页
				pageTotal: 1,
				//弹层
				showModal: false,
				chmess:{},
				tableTiT: [{
						title: "序号",
						width: 80,
						align: 'center',
						type: 'index'
					},
					{
						title: '服务名称',
						width: 120,
						align: 'center',
						key: 'fwmc'
					},
					{
						title: '服务代码',
						align: 'center',
						key: 'fwdm'
					},
					{
						title: '状态',
						align: 'center',
						key: 'zt',
                        render:(h,p)=>{
	                     	let val = this.dictUtil.getValByCode(this,this.lmdmDictionary,p.row.zt)
	    					return h('div', [
								h('span',{
									style:{
										fontWeight:900,
										color:p.row.zt=='00'?'#279a3b':'#ed3f14',
									}
								},val)
							]);
                        }
					},
					{
						title: 'API前缀',
						align: 'center',
						key: 'apiQz'
					},
					{
						title: '图标',
						align: 'center',
						key: 'tb',
						render: (h, params) => {
							return h('div', [
								h('Icon', {
									props: {
										type: params.row.tb,
										size: '22'
									},
									on: {
										click: () => {
											//log('数据调试', params)
										}
									}
								})
							]);
						}
					},
					// {
					// 	title: '创建时间',
					// 	width: '100',
					// 	align: 'center',
					// 	key: 'cjsj'
					// },
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
										icon: 'md-create',
										shape: 'circle',
										size: 'small'
									},
									style: {
										cursor: "pointer",
										margin: '0 8px 0 0'
									},
									on: {
										click: () => {
											this.compName = 'mess'
											this.chmess = params.row
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
									style: {
										cursor: "pointer",
										margin: '0 8px 0 0'
									},
									on: {
										click: () => {
											this.listDele(params.row.fwId)
										}
									}
								})
							]);
						}
					}
				],
				tableData: [],
				//收索
//				cjsjInRange: [],
				param: {
//					cjsjInRange:[],
					fwmcLike:'',
					pageNum: 1,
					pageSize:8
				},
				Dictionary:[],
				lmdmDictionary:'ZDCLK0006'
			}
		},
//		watch: {
//			cjsjInRange:function(newQuestion, oldQuestion){
//				this.param.cjsjInRange = this.getdateParaD(newQuestion[0]) + ',' + this.getdateParaD(newQuestion[1])
//			},
//		},
		created() {
			this.$store.commit('setCurrentPath', [{
				title: '首页',
			}, {
				title: '系统管理',
			}, {
				title: '服务管理',
			}]),
			this.tabHeight = this.getWindowHeight() - 290
            this.getmess()
            this.getLXDic()
		},
		methods: {
			getLXDic(){
                this.Dictionary = this.dictUtil.getByCode(this,this.lmdmDictionary);
                log('字典',this.Dictionary)
            },
			getmess(){
				var v = this
				this.$http.get(this.apis.ITMS.QUERY).then((res) =>{
					v.tableData = res.page.list
					v.SpinShow = false;
					v.pageTotal=res.page.total
				})
			},
			//删除数据
			listDele(id){
				this.util.del(this,this.apis.ITMS.DELE,[id],()=>{
                    this.getmess();
				});
			},
			AddDataList() {
				var v = this
				v.compName = 'addmess'
			},
			pageChange(event) {
				var v = this
				v.page.pageNum = event
			},
			formList() {
				var v = this
				v.SpinShow = true;
				this.$http.get(this.apis.ITMS.QUERY,{params:v.param}).then((res) =>{
					//log(res)
					v.tableData = res.page.list
					v.SpinShow = false;
				})
			},
		}
	}
</script>
