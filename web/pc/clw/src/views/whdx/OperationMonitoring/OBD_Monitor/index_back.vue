<style lang="less">
    @import "../../../../styles/common.less";
    .VehicleMonitoringTiT{
    	width: 225px;
    	background-color: #fff;
    	border-radius: 5px 5px 0 0 ;
    	padding-left: 5px;
    	.boxTiT{
    		.cartypemess{
    			text-align: center;
    			.cartypebox{
    				margin-top:12px;
    				margin-bottom:12px;
					margin-left: 16px;
					float: left;
    			}
    		}
    	}
    	.carlistmess{
			height: 150px;
    		cursor: pointer;
    		padding: 6px 16px 18px 16px;
    		border-bottom: solid 1px #919191;
    	}
    }
	.btn_obd{
		padding: 2px;
		border: 1px solid #5cadff;
		border-radius: 4px;
	}
	.top_btn{
		width: 76px;
		height: 76px;
		color:white;
		border-radius: 10px;
		font-size: 18px;
	}
	.search_input{
		background-color: #cccccc;
		border-radius: 10px;
		border:none;
		padding: 8px;
		font-size: 15pt;
		margin-left: 17px;
		appearance: none;
		-web-kit-appearance:none;
		-moz-appearance: none;
		outline:none;
		text-decoration:none;
	}
	._18pt{
		font-size: 18pt;
	}
	._16pt{
		font-size: 16pt;
	}
	._14pt{
		font-size: 14pt;
	}
	::-webkit-input-placeholder { /* WebKit browsers */
		color: #999;
	}
	:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
		color: #999;
	}
	::-moz-placeholder { /* Mozilla Firefox 19+ */
		color: #999;
	}
	:-ms-input-placeholder { /* Internet Explorer 10+ */
		color: #999;
	}
	.obdFaultStatus{
		float: right;
	}
	.obdFaultHandled{
		color: dodgerblue;
	}
	.obdFaultNotHandle{
		color: red;
	}
</style>
<template>
    <div class="box-row">
		<component :is="componentName"></component>
    	<div class="body-F" style="height:100%;">
    		<my-map ref="map"></my-map>
    	</div>
    	<div class="VehicleMonitoringTiT" style="width: 300px">
    		<div class="box">
    			<div class="boxTiT">
    				<div  class="cartypemess">
						<div class="cartypebox">
							<Button style="background-color: #6ebaff;" class="top_btn" @click="changeStatus(0)">
								<div>{{carArray[0].length}}</div>
								<div>启动</div>
							</Button>
						</div>
						<div class="cartypebox">
							<Button style="background-color: #ff9b87;" class="top_btn" @click="changeStatus(1)">
								<div>{{carArray[1].length}}</div>
								<div>熄火</div>
							</Button>
						</div>
						<div class="cartypebox">
							<Button style="background-color: #8190ff;" class="top_btn" @click="changeStatus(2)">
								<div>{{carArray[2].length}}</div>
								<div>离线</div>
							</Button>
						</div>
    				</div>
    			</div>
    			<div class="body">
					<div style="margin-top: 18px">
						<input type="text" class="search_input" v-model="keyword" placeholder="请填写车牌号码..." @input="filter"></input>
					</div>

					<div class="carlistmess" v-for="(item,index) in rightCarList" @click="rowClick(item)">
						<div>
							<span class="_16pt">{{item.zdbh}}</span>
							<Poptip v-if="item.obdId != ''" title="OBD信息"  placement="left" width="300"  style="float: right">
								<Button size="small" @click="getObdInfo(item)" style="font-weight: 700;color: black">OBD</Button>
								<div slot="content">
									<h2 v-if="gpsObdMessage == null" >暂无数据</h2>
									<Row v-if="gpsObdMessage != null">
										<Col span="8">更新日期</Col>
										<Col span="16"><span>{{formatDate(gpsObdMessage.creatorDate)}} {{formatTime(gpsObdMessage.creatortime)}}</span></Col>
									</Row>
									<Row v-if="gpsObdMessage != null">
										<Col span="8">发动机转速</Col>
										<Col span="16"><span>{{gpsObdMessage.engineSpeed}} r/min</span></Col>
									</Row>
									<Row v-if="gpsObdMessage != null">
										<Col span="8">车速</Col>
										<Col span="16"><span>{{gpsObdMessage.obdSpeed}} KM/h</span></Col>
									</Row>
									<Row v-if="gpsObdMessage != null">
										<Col span="8">剩余油量</Col>
										<Col span="16"><span>{{gpsObdMessage.syyl}} L</span></Col>
									</Row>
									<Row v-if="gpsObdMessage != null">
										<Col span="8">耗油量</Col>
										<Col span="16"><span>{{gpsObdMessage.hyl}} L</span></Col>
									</Row>
									<Row v-if="obdFaultCode && obdFaultCode.length != 0">
										<Col style="border-bottom: 1px solid #cccccc"></Col>
										<Col span="8">故障报告</Col>
										<Col span="16">
											<div v-for="item in obdFaultCode" style="border-bottom: 1px solid #cccccc">
												<span>{{item.faultCode}}</span>
												<!--<span :class="{obdFaultStatus:item.faultType != null,obdFaultHandled:item.faultType == '10',obdFaultNotHandle:item.faultType != '10'}">{{item.faultType == '10' ? '已解决' : '未解决'}}</span>-->
												<br>
												<span>{{item.creationTime}}</span>
											</div>
										</Col>
									</Row>
								</div>
							</Poptip>
						</div>
						<div>
							<span class="_16pt">司机：</span>
							<span class="_16pt">{{item.sjxm ? item.sjxm : '未绑定司机'}}</span>
						</div>
						<div style="margin-top: 18px">
							<span  class="_14pt" style="color: #919191">{{item.text}}</span><br>
							<span  class="_14pt" style="color: #919191">{{formateLongDate(item.time)}}</span>
						</div>
					</div>
    			</div>
    		</div>
    	</div>
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
import myMap from '../../map/carJK.vue';
import Stomp from '@stomp/stompjs';
import SockJS from 'sockjs-client';
export default {
    name: 'VehicleMonitoring',
    components: {
    	myMap
    },
    data () {
        return {
            SpinShow:false,
            componentName:'',
            rightCarList:[],
            mapCarList:[],
            carArray:[[],[],[]],
			status:0,
            choosedCar:null,
			keyword:'',
            obd:{},
            gpsObdMessage:null,
            obdFaultCode:[],
			initTime:'',
			allCarList:[
			],

            // socket : new SockJS(this.$http.url+"/gps"),
            socket : new SockJS("http://127.0.0.1/gps"),
        };
    },
    // computed: {
    //     GetsocketAllCar() {
    //         return this.$store.state.app.socketAllCar
    //     }
    // },
    // watch: {
    //     GetsocketAllCar: function(newQuestion, oldQuestion) {
    //         this.onGpsInfo(newQuestion);
    //     },
    // },
    created(){
        this.$store.commit('setCurrentPath', [{
            title: '首页',
        },{
            title: '运营监控',
        },{
            title: '车辆监控',
        }])
		// this.init();
        this.initGps()
    },
    methods: {
        sco(){
            //数据推送
            var v = this
            /**
             * 建立成功的回调函数
             */
            v.socket.onopen = function() {
            };
            /**
             * 服务器有消息返回的回调函数
             */
            v.socket.onmessage = function(e) {
                // log('message', e.data);
            };

            /**
             * websocket链接关闭的回调函数
             */
            v.socket.onclose = function() {
                // log('关闭');
            };
            /**
             * /topic/sendzp  订阅拍视频   /topic/sendhbsp订阅跑照片
             */

            var stompClient = Stomp.over(v.socket);
            stompClient.connect({}, function(frame) {
                for (let r of v.allCarList){
                    stompClient.subscribe('/topic/sendgps-'+r.zdbh,  function(data) { //订阅消息
                        v.onGpsInfo(data.body)
                    });
				}
            });
        },
        formateLongDate(long){
            log(long);
            if (typeof long == 'string'){
                return long;
			}
          	let d = new Date(long);
            log(d);
            return d.format("yyyy-MM-dd hh:mm:ss");
		},
        formatDate(date){
            log(date);
            if (!date)return '';
            return date.substring(0,4)+'-'+date.substring(4,6)+"-"+date.substring(6,8)+"-";
        },
        formatTime(time){
            log(time);
            if (!time)return '';
            return time.substring(0,2)+':'+time.substring(2,4)+":"+time.substring(4,6);
        },
        getObdInfo(item){
            this.SpinShow = true;
            var v = this
            this.gpsObdMessage = null;
            this.obdFaultCode = [];
            this.$http.post(this.apis.CLJK.getObdTimely,{obdId:item.obdId}).then((res) =>{
                if (res.code === 200){
                    if (res.result.gpsObdMessage){
                        this.gpsObdMessage = res.result.gpsObdMessage;
					}
                    if (res.result.obdFaultCode){
                        this.obdFaultCode = res.result.obdFaultCode;
					}
                }
                this.SpinShow = false;
            })
        },
        onGpsInfo(m){
            let has = false;
            let exist = null;
            let newCar = {};
            for(let r of this.allCarList){
			    if (r.zdbh === m.zdbh){
                    exist = r;
                    break;
                }
			}
            if (exist){
                let n = this.updateItem(exist,m);
                newCar = n;
                let index  = this.allCarList.indexOf(exist);
                this.allCarList.splice(index,1,n);
			}else{
                this.handleItem(m);
                newCar = m;
                this.allCarList.push(m);
			}
            this.classify();
            this.rightCarList = this.carArray[this.status];
            if (this.choosedCar){
                if (this.choosedCar.zdbh == newCar.zdbh){
                    this.mapCarList = [newCar];
                    this.$refs.map.update();
                    return;
				}else{
                    this.mapCarList = [this.choosedCar];
				}
            }else{
                this.mapCarList = this.carArray[this.status];
                this.$refs.map.update();
            }
		},
        init(){
            this.classify();
            this.mapCarList = this.carArray[0];
            this.rightCarList = this.carArray[0];
		},
        filter(){
            this.classify();
            this.rightCarList = this.carArray[this.status];
            this.mapCarList = this.carArray[this.status];
            this.$refs.map.init();
		},
		initGps(){
            log('initGps');
            var v = this
            this.$http.get(this.apis.CLJK.QUERY).then((res) =>{
                if (res.code === 200){
                    this.initTime = new Date().getTime();
                    this.allCarList = res.result;
                    for(let r of this.allCarList){
                        this.handleItem(r);
                    }
                    this.sco();
				}
                this.init();
            })
		},
        showPath(){
            this.$refs.map.showPath();
        },
        changeStatus(status){
            this.choosedCar = null;
            this.status = status;
            this.rightCarList = this.carArray[status];
            this.mapCarList = this.carArray[status];
            this.$refs.map.init();
		},
		classify(){
            this.carArray[0] = [];
            this.carArray[1] = [];
            this.carArray[2] = [];
			for(let r of this.allCarList){
			    let status = r.status;
                if (this.keyword === '' || r.cph.indexOf(this.keyword) > 0){
                    this.carArray[status].push(r);
				}
			}
		},
		updateItem(o,n){
            let r = {};
            r.cx = n.cx;
            r.lng = n.bdjd;
            r.lat = n.bdwd;
            r.clid = n.clid;
            r.zdbh = n.zdbh;
            r.bdjd = n.bdjd;
            r.bdwd = n.bdwd;
            r.zxzt = n.zxzt;
            r.cph = o.cph;
            r.sjxm = o.sjxm;
            r.obdId = o.obdId;
            switch(n.zxzt){
                case '10':
                    r.status = 1;
                    r.text = '熄火时间';
                    r.speed = 0;
                    r.time = o.time;
                    break;
                case '20':
                    r.status = 2;
                    r.text = '离线时间';
                    r.speed = 0;
                    r.time = o.time;
                    break;
                case '00':
                default:
                    r.time = n.time;
                    r.speed = n.speed;
                    r.status = 0;
                    r.text = '更新时间';
            }
            log(r);
            return r;
		},
		handleItem(item){
            item.lng = item.bdjd;
            item.lat = item.bdwd;
            if (item.zxzt){
                switch(item.zxzt){
                    case '10':
                        item.status = 1;
                        item.text = '熄火时间';
                        item.speed = 0;
                        break;
                    case '20':
                        item.status = 2;
                        item.text = '离线时间';
                        item.speed = 0;
                        break;
                    case '00':
                    default:
                        item.status = 0;
                        item.text = '更新时间';
                }
			}else{
                switch(item.eventType){
                    case '60':
                        item.status = 1;
                        item.text = '熄火时间';
                        item.speed = 0;
                        break;
                    case '80':
                        item.status = 2;
                        item.text = '离线时间';
                        item.speed = 0;
                        break;
                    case '50':
                    default:
                        item.status = 0;
                        item.text = '更新时间';
                }
			}
		},
		rowClick(item){
            this.choosedCar = item;
            this.mapCarList = [this.choosedCar];
            this.$refs.map.init();
		}
    }
};
</script>

