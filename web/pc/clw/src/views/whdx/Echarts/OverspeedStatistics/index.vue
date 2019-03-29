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
							<span>超速统计</span>
						</div>
						<div class="body-r-1 inputSty">
							<!--<DatePicker v-model="param.kssj" :options="dateOpts" type="datetime" placeholder="请输入开始时间" ></DatePicker>-->
							<!--<DatePicker v-model="param.jssj" :options="dateOpts" type="datetime"  placeholder="请输入结束时间"  ></DatePicker>-->
							<Input v-model="param.sjxmLike" placeholder="请输入司机姓名" style="width: 200px"></Input>
						</div>
						<div class="butevent">
							<Button type="primary" @click="getData()">
								<Icon type="md-search"></Icon>
							</Button>
							<Button type="primary" size="large" @click="v.util.exportData(v,'超速统计')">
								<Icon type="ios-download-outline"></Icon>导出
							</Button>
						</div>
					</div>
				</div>
			</Row>
			<Row style="position: relative;">
				<Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="tableColumns" :data="pageData"></Table>
			</Row>
		</Card>
		<component :is="compName"></component>
	</div>
</template>

<script>
    import mixins from '@/mixins'
    import csMessbar from '../../../home/compEcharts/comp/csMessbar'

    Date.prototype.format = function(format)
    {
        var o = {
            "M+" : this.getMonth()+1, //month
            "d+" : this.getDate(),    //day
            "h+" : this.getHours(),   //hour
            "m+" : this.getMinutes(), //minute
            "s+" : this.getSeconds(), //second
            "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
            "S" : this.getMilliseconds() //millisecond
        }
        if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
            (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)if(new RegExp("("+ k +")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length==1 ? o[k] :
                    ("00"+ o[k]).substr((""+ o[k]).length));
        return format;
    }
    export default {
        name: 'char',
        mixins: [mixins],
        components: {
            csMessbar
        },
        data() {
            return {
                compName:'',
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
                v:this,
                SpinShow: true,
                tabHeight: 220,
                tableColumns: [
                    {title: "序号",  align: 'center', type: 'index'},
                    {title: '驾驶人', align: 'center',  key: 'sjxm'},
                    {title: '车牌号', align: 'center', key: 'cph'},
                    {title: '超速次数', align: 'center',  key: 'overspeedCount',
					render:(h,p)=>{
                        return h('div',{
                            style:{color:'blue',cursor:'pointer'},
							on:{
                                click:()=>{
                                    this.$store.commit('echChanged',{'cph':p.row.cph,'time':10000})
								}
							}},
							p.row.overspeedCount)
					}},
                ],
                pageData: [],
                param: {
                    type:'cstj',
                    sjxmLike: '',
                    total: 0,
                },
            }
        },
        computed:{
            echData(){
                return this.$store.state.app.ech
            }
        },
        watch:{
            echData:function (n,o) {
                this.compName = 'csMessbar'
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{title: '首页',}, {title: '数据报表',}, {title: '超速统计',}])
            this.tabHeight = this.getWindowHeight() - 295
            this.getData()
        },
        methods: {
            getData(){
                let startTime = this.param.kssj;
                let endTime = this.param.jssj;
                if (typeof startTime === 'object'){
                    this.param.kssj = startTime.format('yyyy-MM-dd hh:mm:ss');
                }
                if (typeof endTime === 'object'){
                    this.param.jssj = endTime.format('yyyy-MM-dd hh:mm:ss');
                }
                this.$http.get(this.apis.AQJS.QUERY,{params:this.param}).then((res) =>{
                    if (res.code == 200){
                        this.pageData = res.result;
                    }
                })
            },
        }
    }
</script>
