<template>
    <Card>
        <p slot="title" style="font-size: 18px">
            <Icon type="ios-car" size="26"/>
            {{data.cph}}
        </p>
        <a href="#" slot="extra" @click.prevent="changeLimit">
            <Tag color="cyan">{{getCx(data.cx)+data.zkl+'座'}}</Tag>
        </a>
        <Row>
            <Col span="24">
                <Row v-if="data.sjxm">
                    <Col span="2" style="padding-right: 45px;margin-top: -5px;">
                        <Icon type="ios-person" size="28" color="#2d81f0"/>
                    </Col>
                    <Col span="10">
                        <span>{{data.sjxm}}</span>
                    </Col>
                    <Col span="6" offset="4">
                        <Poptip
                                confirm
                                title="确认解除绑定?"
                                @on-ok="unbindDriver"
                                @on-cancel="cancel">
                            <Button type="text" icon="ios-trash" style="color:#ff9900;font-size:24px;margin-top: -16px;" ghost></Button>
                        </Poptip>
                    </Col>
                </Row>
                <Row v-else-if="!data.sjxm">
                    <Col span="2" style="padding-right: 45px;margin-top: -5px;">
                        <Icon type="ios-person" size="28" color="#2d8cf0"/>
                    </Col>
                    <Col span="12">
                        <span  v-if="!bindDriverFlag">暂未绑定</span>
                        <Select v-else-if="bindDriverFlag" v-model="driverId" style="width:100%" filterable clearable>
                            <Option v-for="(item,index) in driverList" :key="item.sfzhm" :value="item.sfzhm">{{item.xm}}</Option>
                        </Select>
                    </Col>
                    <Col span="4" offset="4">
                        <Tooltip content="绑定司机" v-if="!bindDriverFlag">
                            <Button type="text" icon="md-code-working" style="color:#2db7f5;font-size:24px;margin-top: -16px;" ghost @click="chooseDriver"></Button>
                        </Tooltip>
                        <div v-else-if="bindDriverFlag">
                            <Tooltip content="绑定司机">
                                <Button type="success" shape='circle' size="small" icon="md-checkmark" @click="bindDriver"></Button>
                            </Tooltip>
                            <Tooltip content="取消绑定">
                                <Button type="error" shape='circle' size="small" icon="md-close" @click="cancelChooseDriver"></Button>
                            </Tooltip>
                        </div>
                    </Col>
                </Row>
            </Col>
        </Row>
        <Row style="margin-top: 10px;">
            <Col span="24">
                <Row v-if="data.zdbh">
                    <Col span="2" style="padding-right: 45px;margin-top: -5px;">
                        <Icon type="ios-videocam" size="28" color="#2d8cf0"/>
                    </Col>
                    <Col span="10">
                        <span>{{data.zdbh}}</span>
                    </Col>
                    <Col span="6" offset="4">
                        <Poptip
                                confirm
                                title="确认解除绑定?"
                                @on-ok="unbindDevice"
                                @on-cancel="cancel">
                            <Button type="text" icon="ios-trash" style="color:#ff9900;font-size:24px;margin-top: -16px;" ghost></Button>
                        </Poptip>
                    </Col>
                </Row>
                <Row v-else-if="!data.zdbh">
                    <Col span="2" style="padding-right: 45px;margin-top: -5px;">
                        <Icon type="ios-videocam" size="28" color="#2d8cf0"/>
                    </Col>
                    <Col span="12">
                        <span  v-if="!bindDeviceFlag">暂未绑定</span>
                        <Select v-else-if="bindDeviceFlag" v-model="deviceId" style="width:100%" filterable clearable sea>
                            <Option v-for="(item,index) in deviceList" :key="item.zdbh" :value="item.zdbh">{{item.zdbh}}</Option>
                        </Select>
                    </Col>
                    <Col span="4" offset="4">
                        <Tooltip content="绑定终端" v-if="!bindDeviceFlag">
                            <Button type="text" icon="md-code-working" style="color:#2db7f5;font-size:24px;margin-top: -16px;" ghost @click="chooseDevice"></Button>
                        </Tooltip>
                        <div v-else-if="bindDeviceFlag">
                            <Tooltip content="绑定终端">
                                <Button type="success" shape='circle' size="small" icon="md-checkmark" @click="bindDevice"></Button>
                            </Tooltip>
                            <Tooltip content="取消绑定">
                                <Button type="error" shape='circle' size="small" icon="md-close" @click="cancelChooseDevice"></Button>
                            </Tooltip>
                        </div>
                    </Col>
                </Row>
            </Col>
        </Row>
        <Row type="flex" justify="end" style="padding-top: 20px">
            <Col span="22" style="text-align: center">
                <ButtonGroup size="large">
                    <Tooltip content="编辑">
                        <Button  icon="md-create" @click="emit('editCar')"></Button>
                    </Tooltip>
                    <Tooltip content="车辆档案">
                        <Button  icon="ios-clipboard-outline" @click="emit('showDoc')"></Button>
                    </Tooltip>
                    <Tooltip content="历史轨迹">
                        <Button  icon="ios-map-outline" @click="emit('trace')"></Button>
                    </Tooltip>
                    <Tooltip content="电子围栏">
                        <Button icon="ios-globe-outline" @click="emit('showFance')"></Button>
                    </Tooltip>
                    <Tooltip content="删除">
                        <Button icon="ios-trash" @click="emit('delCar')"></Button>
                    </Tooltip>
                </ButtonGroup>
            </Col>
        </Row>
    </Card>
</template>

<script>
    export default {
        name: "carItem",
        data(){
          return{
              driverList:[],
              deviceList:[],
              driverId:'',
              deviceId:'',
              cxDict:[],
              cxDictCode:'ZDCLK0019',
              bindDriverFlag:false,
              bindDeviceFlag:false,
          }
        },
        props:{
          data:{
              type:Object,
              default:function(){
                  return {}
              }
          }
        },
        created(){
            this.bindDriverFlag = !!this.data.sjxm
            this.bindDeviceFlag = !!this.data.zdbh
        },
        mounted(){
        },
        methods:{
            emit(method){
                this.$emit(method,this.data);
            },
            cancelChooseDriver(){
                this.bindDriverFlag = false
            },
            cancelChooseDevice(){
                this.bindDeviceFlag = false
            },
            chooseDriver(){
                this.getdriverList();
                this.bindDriverFlag = true
            },
            chooseDevice(){
                this.getDevices();
                this.bindDeviceFlag = true
            },
            bindDriver(){
                this.postAndReload(this.apis.CLGL.bindDriver,{carId:this.data.clId,driverId:this.driverId})
            },
            bindDevice(){
                this.postAndReload(this.apis.CLGL.bindDevice,{carId:this.data.clId,devcieId:this.deviceId})
            },
            unbindDriver(){
                this.driverId = '';
                this.postAndReload(this.apis.CLGL.unbindDriver,{carId:this.data.clId})
            },
            unbindDevice(){
                this.devcieId = '';
                this.postAndReload(this.apis.CLGL.unbindDevice,{carId:this.data.clId})
            },
            postAndReload(url,param){
                let v = this;
                v.$http.post(url,param).then((res) =>{
                    if(res.code === 200){
                        this.$emit('reload');
                        this.$Message.success(res.message);
                    }else{
                        this.$Message.error(res.message);
                    }
                })
            },
            getdriverList(){
                let v = this;
                v.driverList = [];
                v.$http.get(this.apis.JSY.QUERY,{params:{pageSize:1000}}).then((res) =>{
                    if(res.code===200){
                        v.driverList = res.page.list;
                    }
                })
            },
            getDevices(){
                let v = this;
                v.deviceList = []
                v.$http.post(this.apis.ZDGL.SXQUERY).then((res) =>{
                    if(res.code===200){
                        v.deviceList = res.result;
                    }
                })
            },
            ok(item){

            },
            cancel(item){

            },
            getCx(code){
                let val = this.dictUtil.getValByCode(this,this.cxDictCode,code)
                return val;
            },
        }
    }
</script>

<style scoped>

</style>