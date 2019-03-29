<style lang="less">
	@import '../../../../styles/common.less';
</style>
<!--用户管理-->
<template>
	<div class="boxbackborder">
		<component :is="componentName"></component>
		<Card style="height:600px">
			<Row class="margin-top-10" style='background-color: #fff'>
				<Col span="3">
					<h2>{{car.cph}}</h2>
				</Col>
				<Col span="3">
					<DatePicker v-model="param.startTime" :options="dateOpts" type="date" placeholder="请输入开始时间" ></DatePicker>
				</Col>
				<Col span="3">
					<DatePicker v-model="param.endTime" :options="dateOpts" type="date"  placeholder="请输入结束时间"  ></DatePicker>
				</Col>
				<Col span="3">
					<Button type="primary" @click="formList()">
						<Icon type="md-search"></Icon>
					</Button>
				</Col>
			</Row>
			<br>
			<Row :gutter="16">
				<Col span="6" v-for="item in tableData">
					<Card>
						<Row>
							<img @click="showMap(item)" :src="'http://api.map.baidu.com/staticimage/v2?ak=evDHwrRoILvlkrvaZEFiGp30&center='+item.ksjd+','+item.kswd+'&width=300&height=200&zoom=12&markers='+item.jsjd+','+item.jswd+'|'+item.ksjd+','+item.kswd+'&markerStyles=-1,http://47.98.39.45:9092/icon/map_line_end.png|-1,http://47.98.39.45:9092/icon/map_line_begin.png'" class="imgItem">
						</Row>
						<Row>
							<Col span="8"><span>开始时间</span></Col><Col span="16"><span class="span_time">{{item.kssj}}</span></Col>
							<Col span="8"><span>结束时间</span></Col><Col span="16"><span class="span_time">{{item.jssj}}</span></Col>
							<Col span="8"><span>时长</span></Col><Col span="16"><span class="span_time">{{dateFormat(item.sc)}}</span></Col>
						</Row>
					</Card>
				</Col>
			</Row>
			<Row class="margin-top-10 pageSty">
				<Page :total=pageTotal :current=param.pageNum :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator show-sizer placement='top' @on-change='pageChange'></Page>
			</Row>
		</Card>
	</div>
</template>

<script>
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
	import historyMap from '../../map/historyTarckMap'

	export default {
		name: 'char',
		components:{historyMap},
		data() {
			return {
                componentName:'',
                showModal:true,
				tabHeight: 220,
				compName: '',
				pageTotal: 2,
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
				page: {

					pageNum: 1,
					pageSize:8
				},
				tableTiT: [
				    {
						title: "序号",
						width: 80,
						align: 'center',
						type: 'index'
					},
				    {
						title: "开始时间",
						width: 200,
						align: 'center',
						key: 'kssj'
					},
				    {
						title: "结束时间",
						width: 200,
						align: 'center',
                        key: 'jssj'
					},
                    {
                        title:'操作',
                        align:'center',
                        type: 'action',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'success',
                                        icon: 'md-menu',
                                        shape: 'circle',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.showPath();
                                        }
                                    }
                                }),
                            ]);
                        }
                    }
				],
				tableData: [
				],
				//收索
				cjsjInRange:[],
				param: {
                    startTime:'',
                    endTime: '',
                    zdbh: 1,
                    ignition: 50,
                    brennschluss:'60'
				},
				choosedItem:null,
				car:{},
			}
		},
		// watch: {
		// 	cjsjInRange:function(newQuestion, oldQuestion){
		// 		this.param.cjsjInRange = this.getdateParaD(newQuestion[0]) + ',' + this.getdateParaD(newQuestion[1])
		// 	},
		// },
		created() {
			this.$store.commit('setCurrentPath', [{
				title: '首页',
			}, {
				title: '车辆历史轨迹',
			}])
			this.param.zdbh = this.$route.params.zdbh;
            //
            this.param.startTime = this.getTodayDate() + " 00:00:00";
            this.param.endTime = this.getTodayDate() + " 23:59:59";
            this.getCarInfo();
			this.formList();
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
			getTodayDate(){
				let now = new Date();
				return now.format("yyyy-MM-dd");
			},
		    showMap(item){
		        this.choosedItem = item;
		      	this.componentName = 'historyMap';
			},
		    back(){
                this.$router.back();
			},
		    showPath(){
		      this.$parent.showPath();
		      this.$parent.componentName = '';
			},
			getCarInfo(){
                var v = this
                this.$http.get(this.apis.CLGL.QUERY+'?zdbh='+this.param.zdbh).then((res) =>{
                    if (res.code === 200 && res.page.list.length > 0){
                        this.car = res.page.list[0];
                        log(res);
                    }
                })
			},
            formList(){
                var v = this
				let startTime = this.param.startTime;
				let endTime = this.param.endTime;
                if (typeof startTime === 'object'){
                    startTime = startTime.format('yyyy-MM-dd hh:mm:ss');
                }
                if (typeof endTime === 'object'){
                    endTime = endTime.format('yyyy-MM-dd hh:mm:ss');
                }
                let p = {
                    startTime:startTime,
                    endTime : endTime,
                    zdbh: this.param.zdbh,
                    ignition: this.param.ignition,
                    brennschluss:this.param.brennschluss
				}
                this.$http.post(this.apis.CLGL.GPS_HITSOR,p).then((res) =>{
                    if (res.code === 200 && res.result){
                        //var geoc = new BMap.Geocoder();
                        for (let r of res.result){
                            let ksgps = r.ksgps.split(',');
                            let jsgps = r.jsjps.split(',');
                            r.ksjd = ksgps[1],
                            r.kswd = ksgps[0],
                            r.jsjd = jsgps[1],
                            r.jswd = jsgps[0];
                            /*
                            暂时不处理地址解析，地址解析是一个异步方法，可能造成延迟数据展示不出来
                            //解析开始地址
                            var point = new BMap.Point(r.ksjd,r.kswd);
                            geoc.getLocation(point, (rs)=>{
                                var addComp = rs.addressComponents;
                                r.ksdz = addComp.street;
                            });
                            //解析结束地址
                            var jsPoint = new BMap.Point(r.jsjd,r.jswd);
                            geoc.getLocation(jsPoint, (rs)=>{
                                var addComp = rs.addressComponents;
                                r.jsdz = addComp.street;
                            });*/
                        }
                        this.tableData = res.result;
					}
                })
			},
			//分页点击事件按
			pageChange(event) {
				var v = this
				v.param.pageNum = event
			}
		}
	}
</script>
<style>
	.span_time{
		color: #5cadff;
	}
	.imgItem{
		width: 100%;
		height: 100%;
	}
</style>
