<style lang="less">
	@import '../../../../../styles/common.less';
	.SC {
		.full-calendar-header {
			font-size: 22px;
		}
		.day-number {
			font-size: 18px;
		}
		.dates-bg {
			.today {
				background: #09b938!important;
			}
		}
		.comp-full-calendar {
			max-width: 100%!important;
			margin: 0!important;
		}
	}
	#fullcalendar{
		.full-calendar-body .dates .dates-events .events-week .events-day{
			min-height: 74px!important;
		}
		.full-calendar-body .dates .week-row .day-cell{
			min-height: 74px!important;
		}
		.comp-full-calendar{
			padding: 15px!important;
		}
	}
</style>
<template>
	<div class="SC box">
		<div v-if="SpinShowChlid" style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 100;">
			<Spin fix style="background:rgba(255,255,255,0.5)!important">
	            <Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
	            <div style="font-size: 30px;">排版提交中....</div>
	        </Spin>
		</div>
		<div v-if="SpinShow" style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 110;">
			<Spin fix style="background-color:#fff">
	            <Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
	            <div style="font-size: 30px;">数据加载中请稍后</div>
	        </Spin>
		</div>
		<div class="body" v-show='dateMess' style="background-color: #fff;">
			<component
				:is="modalName"
				ref="pbxx"
				:todaytime='todaytime'
				@okdrag='okdrag'></component>
		</div>
		<div class="body" v-show='!dateMess' style="height: 100%;">
			<div class="box-row">
				<div class="body-r-1" style="height:100%;border-right: solid 1px #22CDDE;">
					<div id="fullcalendar" style="height: 100%;">
						<fullcalendar :events='events' lang='zh' title='日历标题' @dayClick="dayClick" @eventClick='eventClick'>
						</fullcalendar>
					</div>
						<!-- <div slot="fc-header-left" style="background-color: #f00;">
									123
							</div>
							<div slot="fc-header-right" style="background-color: #f00;">
									456
							</div>
							<div slot="fc-body-card" style="background-color: #f00;">
							 		798
							</div> -->
				</div>
				<div style="width: 360px;padding: 15px;height: 100%;background-color: #fff;border-left: solid 1px #22CDDE;">
					<div class="box" style="">
						<div class="tit">
							<div style="text-align: center;font-size: 18px;padding: 5px;">
								<b>
									{{todaytime}}
								</b>
								<span style="font-size: 14px;">
									排班信息
								</span>
							</div>
							<div style="text-align: right;padding-bottom: 5px;">
								<Button type="primary" size="small" @click="changeClick">编辑</Button>
							</div>
						</div>
						<div class="body">
							<Table ref="table"
								border
								:height="tabHeight"
								:columns="tableTiT"
								:data="tableData"></Table>
							<div>
								<Button type="warning">复制当日排班信息</Button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import fullcalendar from 'vue-fullcalendar'
	import drag from './comp/drag.vue'
	import drlist from '../../../components/draggable-list/draggable-list.vue'
	import mixins from '@/mixins'


	export default {
		name: '',
		mixins:[mixins],
		components: {
			fullcalendar,
			drag,drlist
		},
		data() {
			return {
				tabHeight:'',
				todaytime:'',
				dateMess: false,
				modalName: '',
				SpinShow:false,
				SpinShowChlid:false,
				events: [{
						title: '早班',
						start: '2018-01-01',
						cssClass: 'family',
						YOUR_DATA: {
							name: 'hello'
						}
					},
					{
						title: 'event1',
						start: '2018-01-01',
						cssClass: 'family',
						YOUR_DATA: {}
					},
					{
						title: 'event2',
						start: '2018-01-06',
						end: '2018-01-08',
						cssClass: ['family', 'career'],
						YOUR_DATA: {}
					}
				],
				tableTiT: [
                	{
                        type: 'index',
                        width: 40,
                        align: 'center'
                    },
                    {
                        title: '班次',
                        key: 'date'
                    },
                    {
                        title: '车辆',
                        width: 100,
                        key: 'cph',
                        align: 'center'
                    },
                    {
                        title: '线路',
                        key: 'clXl',
                        align: 'center',
                        render: (h, params) => {
                        	return params.row.clXl.xlmc
                        }
                    },
                    {
                        title: '状态',
                        key: 'type',
                        align: 'center',
                        render: (h, params) => {
							return h('div', [
								h('span',{
									style:{
										fontWeight:900,
										color:params.row.type=='正常'?'#228B22':'#FF4500',
									}
								},params.row.type)
							]);
						}
                    }
                ],
                tableData: [
                ]
			}
		},
		created() {
			this.$store.commit('setCurrentPath', [{
				title: '首页',
			}, {
				title: '车辆管理',
			}, {
				title: '班车管理',
			}, {
				title: '班车排班',
			}]),
			this.todaytime = this.getdateParaD(this.getdate())
			this.getmess()
		},
		mounted(){
			this.getalert()
		},
		methods: {
			getmess(){
				var v = this
				log('排班数据2')
				this.$http.post(this.apis.PB.QUERY,{"clcx":"30","lulx":"10","date2":v.todaytime}).then((res) =>{
					log('排班数据',res)
					v.tableData = res.result
				})
			},
			okdrag(){
//				alert('132')
				this.dateMess=false
			},
			getalert(){
        		var windowHeight = window.innerHeight
        		this.tabHeight = windowHeight - 280
        		log('浏览器高',this.tabHeight)
        	},
        	changeClick(){
        		this.dateMess = true
        		this.modalName = 'drag'
//				this.modalName = drlist
        	},
			dayClick(event) {
				this.todaytime = this.getdateParaD(event)
				this.getmess()
//				log('天事件', this.getdateParaD(event))
//				log('天事件', event.toLocaleString())
//				this.dateMess = true
			},
			eventClick(event) {
				log('备注事件', event)
			},
			//dome组件刷新
			domeC(){
//				debugger
				this.$refs.pbxx.SpinShow = false
				this.$router.push({
					name:'Sc_Scheduling'
				})
				this.modalName = 'drag'
			}
		}
	}
</script>

<style>

</style>
