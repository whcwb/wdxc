<style lang="less">
	@import '../../../../styles/common.less';
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
							<span>出车统计</span>
						</div>
						<div class="body-r-1 inputSty">
							<DatePicker v-model="param.kssj" :options="dateOpts" type="date" placeholder="请输入开始时间" ></DatePicker>
							<DatePicker v-model="param.jssj" :options="dateOpts" type="date"  placeholder="请输入结束时间"  ></DatePicker>
							<Input v-model="param.sjxm" placeholder="请输入司机姓名" style="width: 200px"></Input>
						</div>
						<div class="butevent">
							<Button type="primary" @click="getData()">
								<Icon type="md-search"></Icon>
							</Button>
							<Button type="primary" size="large" @click="v.util.exportData(v,'出车统计')">
								<Icon type="ios-download-outline"></Icon>导出
							</Button>
						</div>
					</div>
				</div>
			</Row>
			<Row style="position: relative;">
				<Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="tableTiT" :data="pageData"></Table>
			</Row>
		</Card>
	</div>
</template>

<script>
    import mixins from '@/mixins'

    export default {
        name: 'char',
        mixins: [mixins],
        components: {
        },
        data() {
            return {
                v:this,
                SpinShow: true,
                tabHeight: 220,
                componentName: '',
                choosedItem: null,
                dateOpts: {
                    shortcuts: [
                        {
                            text: '今天',
                            value () {
                                return new Date();
                            }
                        },
                        {
                            text: '三天前',
                            value () {
                                const date = new Date();
                                date.setTime(date.getTime() - 3600 * 1000 * 24 * 3);
                                return date;
                            }
                        },
                        {
                            text: '一周前',
                            value () {
                                const date = new Date();
                                date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                                return date;
                            }
                        }
                    ]
                },
                //数据传输
                chmess: {},
                tableTiT: [
                    {
						title: "序号",
						width: 80,
						align: 'center',
						type: 'index'
					},
                    {
                        title: '司机',
                        align: 'center',
                        key: 'sjname'
                    },
                    {
                        title: '出车次数',
                        align: 'center',
                        key: 'ddzsCount'
                    },
                ],
                pageData: [],
                param: {
                    sjxm: '',
                    total: 0,
                    pageNum: 1,
                    pageSize: 8,
                },
            }
        },
        created() {
            this.param.kssj  = this.getTodayDate();
            this.param.jssj  = this.getTodayDate();
            this.$store.commit('setCurrentPath', [{title: '首页',}, {title: '数据报表',}, {title: '出车统计',}])
            this.tabHeight = this.getWindowHeight() - 295
            this.getData()
        },
        methods: {
            getTodayDate(){
                let now = new Date();
                return now.format("yyyy-MM-dd");
            },
            getData(){
                let startTime = this.param.kssj;
                let endTime = this.param.jssj;
                if (typeof startTime === 'object'){
                    this.param.kssj = startTime.format('yyyy-MM-dd');
                }
                if (typeof endTime === 'object'){
                    this.param.jssj = endTime.format('yyyy-MM-dd');
                }
                this.pageData = []
                this.$http.post(this.apis.ORDER.cctj,this.param).then((res) =>{
                    if (res.code == 200 && res.result){
                        this.pageData = res.result;
                    }
                })
            },
        }
    }
</script>
