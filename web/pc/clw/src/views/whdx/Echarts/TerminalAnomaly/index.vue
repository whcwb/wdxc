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
							<span>终端异常</span>
						</div>
						<div class="body-r-1 inputSty">
							<Input v-model="param.cphLike" placeholder="请输入车牌号" style="width: 200px"></Input>
						</div>
						<div class="butevent">
							<Button type="primary" @click="getData()">
								<Icon type="md-search"></Icon>
							</Button>
							<Button type="primary" size="large" @click="v.util.exportData(v,'终端异常统计')">
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
                        title: '终端编号',
                        align: 'center',
                        key: 'zdbh'
                    },
                    {
                        title: '车牌号',
                        align: 'center',
                        key: 'cph'
                    },
                    {
                        title: '最后离线日期',
                        align: 'center',
                        key: 'time'
                    },
                    {
                        title: '离线时长',
                        align: 'center',
                        key: 'time',
						render:(h,p)=>{
                            let s = this.dateFormat(p.row.lxsc);
                            return h('div',s);
						}
                    },
                ],
                pageData: [],
                param: {
                    cphLike: '',
                    total: 0,
                    pageNum: 1,
                    pageSize: 8,
                },
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{title: '首页',}, {title: '数据报表',}, {title: '安全驾驶',}])
            this.tabHeight = this.getWindowHeight() - 295
            this.getData()
        },
        methods: {
            dateFormat(longTypeDate){
                if(!longTypeDate)return;
                let hour = parseInt(longTypeDate / (1000*60*60));
                let min = parseInt((longTypeDate - hour*1000*60*60) / (1000 * 60));
                let sec = parseInt((longTypeDate - hour*1000*60*60 - min*1000*60) / (1000));
                let s = '';
                if (hour > 0){
                    s += hour + '小时';
                }
                if (min > 0){
                    s += min + '分';
                }
                if (sec > 0){
                    s += sec + '秒';
                }
                return s;
            },
            getData(){
                this.pageData = [];
                this.$http.get(this.apis.CLJK.QUERY,{params:this.param}).then((res) =>{
                    if (res.code == 200){
                        for (let r of res.result){
                            if (r.zxzt == '20'){
                                this.pageData.push(r);
							}
						}
                    }
                })
            },
        }
    }
</script>
