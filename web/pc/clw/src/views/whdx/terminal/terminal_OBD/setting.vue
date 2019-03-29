<template>
    <div>
        <component :is="componentName"></component>
        <Modal width="1000" v-model="showModal"  title="终端设置" :closable="false">
            <div v-if="SpinShow" style="width:100%;height:100%;position: fixed;top: 0;left:0;z-index: 1111;">
                <Spin fix>
                    <Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
                    <div style="font-size: 30px;">数据加载中请稍后</div>
                </Spin>
            </div>
                <Row class="height300">
                    <Form :label-width="100">
                        <Row>
                            <Col span="20">
                                <FormItem label='碰撞灵敏度'>
                                    <Select filterable clearable  v-model="deviceControl.pzlmd">
                                        <Option value="00">低</Option>
                                        <Option value="10">中</Option>
                                        <Option value="20">高</Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col span="4">
                                <Button type="primary"  @click="selectChange20">确定</Button>
                            </Col>
                        </Row>
                        <!--<Row>-->
                            <!--<Col span="20">-->
                                <!--<FormItem label='上传模式'>-->
                                    <!--<Select filterable clearable  v-model="deviceControl.scms">-->
                                        <!--<Option value="00">实时</Option>-->
                                        <!--<Option value="10">仅WIFI</Option>-->
                                    <!--</Select>-->
                                <!--</FormItem>-->
                            <!--</Col>-->
                            <!--<Col span="4">-->
                                <!--<Button type="primary"  @click="selectChange30">确定</Button>-->
                            <!--</Col>-->
                        <!--</Row>-->
                        <Row>
                            <Col span="20">
                                <FormItem label='上传视屏模式'>
                                    <Select filterable clearable  v-model="deviceControl.spscms">
                                        <Option value="10">WIFI下上传普通视频</Option>
                                        <Option value="20">WIFI下不上传普通视频</Option>
                                        <Option value="30">WIFI/4G都上传</Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col span="4">
                                <Button type="primary"  @click="selectChange50">确定</Button>
                            </Col>
                        </Row>
                        <Row>
                            <Col span="20">
                                <FormItem label='超速设定'>
                                    <Input type="text" v-model="cssd" placeholder="请填写超速设定..."><span slot="append">KM/h</span></Input>
                                </FormItem>
                            </Col>
                            <Col span="4">
                                <Button type="primary"  @click="setCssd">确定</Button>
                            </Col>
                        </Row>
                    </Form>
                </Row>
            <div slot='footer'>
                <Button type="default" @click="close">关闭</Button>
            </div>
        </Modal>
    </div>
</template>

<script>

    export default {
        name: "deviceInfo",
        components: {
        },
        data(){
            return {
                showConfirmButton:false,
                componentName:'',
                showModal:true,
                SpinShow:false,
                device:'',
                deviceControl:{
                    lmd:1,
                    scms:1,
                    jslmd:'',
                    pzlmd:'',
                    spscms:'',
                    cssd:'',
                    gpsxtjg:'',
                },
                mergeVideoParam:{
                    startTime:'',
                    endTime:''
                },
                cssd:''
            }

        },
        created(){
            this.init();
        },
        mounted(){
        },
        methods:{
            formatDate(date){
              return date.substring(0,4)+'年'+date.substring(4,6)+"月"+date.substring(6,8)+"日";
            },
            formatTime(time){
              return time.substring(0,2)+':'+time.substring(2,4)+":"+time.substring(4,6);
            },
            tabClick(k){
                this.showConfirmButton = k === 2
            },
            setCssd(){
                var v = this
                let params = {
                    cphs:this.device.cph,
                    csz:this.cssd,
                }
                this.SpinShow = true;
                this.$http.post(this.apis.CS.ADD,params).then((res) =>{
                    this.SpinShow = false;
                    if (res.code === 200){
                        this.$Message.success("设置成功!")
                    }else{
                        this.$Message.error(res.message)
                    }
                })
            },
            getCssd(){
                var v = this
                let params = {
                    cph:this.device.cph,
                }
                this.$http.post(this.apis.CS.QUERY,params).then((res) =>{
                    if (res.code === 200 && res.page.list &&  res.page.list.length>0){
                        this.cssd = res.page.list[0].sdsx;
                    }
                })
            },
            saveCssd(){
                this.setting('01',this.deviceControl.cssd);
            },
            saveXtjg(){
                this.setting('40',this.deviceControl.gpsxtjg);
            },
            selectChange02(e){
                this.setting('02',e);
            },
            selectChange20(){
                this.setting('20',this.deviceControl.pzlmd);
            },
            selectChange30(){
                this.setting('30',this.deviceControl.scms);
            },
            selectChange50(){
                this.setting('50',this.deviceControl.spscms);
            },
            setting(type,param){
                var v = this
                let params = {
                    deviceId:this.device.zdbh,
                    cmdType:type,
                    cmd:param
                }
                this.SpinShow = true;
                this.$http.post(this.apis.CLJK.SEND_CONTROLL,params).then((res) =>{
                    this.SpinShow = false;
                    if (res.code === 200){
                        this.$Message.success("设置成功!")
                    }else{
                        this.$Message.error(res.message)
                    }
                })
            },
            init(){
                setTimeout(() => {
                    this.showModal = true;
                }, 100);
                this.device = this.$parent.choosedRow;
                this.getDeviceInfo();
                this.getCssd();
            },
            close(){
                this.showModal = false;
                setTimeout(() => {
                    this.$parent.$data.componentName = "";
                }, 200)
            },
            save(){
                this.SpinShow = true;
                setTimeout(()=>{
                    this.$Message.success("操作成功!")
                    this.SpinShow = false;
                    this.init();
                },1000)
            },
            getDeviceInfo(){
                var v = this
                this.$http.get(this.apis.ZDGL.GET_BY_ID+this.device.zdbh).then((res) =>{
                    if (res.code === 200){
                        this.deviceControl = res.result;
                    }
                })
            },
        }
    }
</script>

<style scoped>
    .padding-top16{
        padding-top: 16px;
    }
    .height200{
        height: 200px;
    }
    .height300{
        height: 300px;
    }
</style>
