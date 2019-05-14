<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<!--角色管理-->
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
							<span>{{$t("TEMPORARY_CAR_MANAGEMENT")}}</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.gnmcLike" :placeholder='$t("CAR_FUNCTION_NAME")' style="width: 200px"></Input>
						</div>
						<div class="butevent">
							<Button type="primary" @click="v.util.getPageData(v)">
								<Icon type="md-search"></Icon>
							</Button>
							<Button type="primary" @click="v.util.add(v)">
								<Icon type="md-add"></Icon>
							</Button>
						</div>
					</div>
				</div>
			</Row>
			<Row style="position: relative;">
				<Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="tableTiT" :data="pageData"></Table>
			</Row>
			<Row class="margin-top-10 pageSty">
				<Page :total=param.total :current=param.pageNum :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator show-sizer placement='top'
					  @on-change='pageChange'></Page>
			</Row>
		</Card>
		<component :is="componentName"></component>
	</div>
</template>

<script>
    import mixins from '@/mixins'
    import formData from './comp/formData.vue'
	import i18nTabTit from '@/mixins/i18nTabTit'

    export default {
        name: 'char',
        mixins: [mixins,i18nTabTit],
        components: {
            formData
        },
        data() {
            return {
                v:this,
                SpinShow: true,
                apiRoot: this.apis.TEMP_CAR,
                tabHeight: 220,
                componentName: '',
                choosedItem: null,
				tableTiT: [
                    {
                        title:'序号',
						tit:"ORDER",
                        type: 'index',
                        align:'center',
                        width: 60,
                    },
                    {
                        title: '车牌号',
						tit:"CAR_NUM_TAB",
                        align:'center',
                        key: 'cph'
                    },
                    {
                        title: '临时单位',
						tit:"TEMPORARY_ORGANIZATION",
                        align:'center',
                        key: 'lsdwId',
						render:(h,p)=>{
							let s = this.getLsdwmcById(p.row.lsdwId);
							return h('div',s);
						}
                    },
                    {
                        title: '车辆类型',
						tit:"CAR_TYPE_TAB",
                        align:'center',
                        key: 'cllx',
                        render:(h,p)=>{
                            let val = this.dictUtil.getValByCode(this,this.cxDictCode,p.row.cllx)
                            return h('div',val)
                        }
                    },
                    {
                        title: '座位数',
						tit:"SEAT_NUM",
                        align:'center',
                        key: 'zws'
                    },
                    {
                        title: '状态',
						tit:"STATUS",
                        align:'center',
                        key: 'zt',
                        render:(h,p)=>{
                            return h('div',p.row.zt == '00' ? '正常' :'停用');
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
                                            this.choosedItem = params.row
                                            this.componentName = 'formData'
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
                                            this.util.delete(this,[params.row.id])
                                        }
                                    }
                                })
                            ]);
                        }
                    },
                ],
                pageData: [],
                param: {
                    gnmcLike: '',
                    total: 0,
                    pageNum: 1,
                    pageSize: 8,
                },
                cxDict:[],//车量型号
                cxDictCode:'ZDCLK0019',
				lswdList:[]
            }
        },
        created() {
            this.tabHeight = this.getWindowHeight() - 300
            this.$store.commit('setCurrentPath', [{title: '首页'}, {title: '车辆管理'},{title: '临时车管理'},{title: '车辆管理'}])
	    this.util.initTable(this);
            this.getDict()
            this.getLsdw()
        },
        methods: {
            getLsdwmcById(id){
                for (let r of this.lswdList){
                    if (r.id == id){
                        return r.dwmc;
					}
				}
			},
            getLsdw(){
                let v = this;
                v.$http.get(this.apis.TEMP_UNIT.QUERY,{params:{pageSize:1000}}).then((res) =>{
                    if (res.code == 200 && res.page.list){
                        this.lswdList = res.page.list;
                    }
                })
            },
            getDict(){
                this.cxDict = this.dictUtil.getByCode(this,this.cxDictCode);
            },
            pageChange(event) {
                this.util.pageChange(this, event);
            },
        }
    }
</script>
