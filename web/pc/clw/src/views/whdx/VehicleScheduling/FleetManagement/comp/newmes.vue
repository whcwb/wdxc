<template>
    <div>
        <Modal
                v-model="showModal"
                width='800'
                :closable='false'
                :mask-closable="false"
                :title="operate+'车队'">
            <Form
                    ref="addmess"
                    :model="addmess"
                    :rules="ruleInline"
                    :label-width="100"
                    :styles="{top: '20px'}">
                <Row>
                    <Row>
                        <Col span="8">
                            <FormItem prop="cdmc" label='车队名称：'>
                                <Input v-model="addmess.cdmc" placeholder="请设置车队名称">
                                </Input>
                            </FormItem>
                        </Col>
                        <Col span="8">
                            <FormItem prop="dzxm" label='队长姓名：'>
                                <Select filterable clearable remote
                                        :remote-method="remoteMethod" v-model="userSelectMss" @on-change='userSelect()'>
                                    <Option v-for="(item,index) in userList" :value="item.yhid">{{item.xm}}</Option>
                                </Select>
                            </FormItem>
                        </Col>
                        <Col span="8">
                            <FormItem prop="zt" label='状态：'>
                                <Select filterable clearable v-model="addmess.zt">
                                    <Option v-for="item in ty" :value="item.key">{{item.val}}</Option>
                                </Select>
                            </FormItem>
                        </Col>
                    </Row>
                </Row>
                <Row>
                    <Col span="24">
                        <Card dis-hover>
                            <p slot="title">
                                <Icon type="ios-car" size="large"/>
                                车辆
                            </p>
                            <div>
                                <Tag color="success" v-for="item in selectedCar" :key="item.cph" :name="item.cph">{{item.cph}}</Tag>
                                <Poptip placement="right" width="400" @on-popper-show="show">
                                    <Button icon="ios-add" type="dashed" size="small">添加车辆</Button>
                                    <div class="api" slot="content">
                                        <choose-car v-if="showCarPop" @carChange="carChange" :selectedData="selectedCar"></choose-car>
                                    </div>
                                </Poptip>
                            </div>
                        </Card>
                    </Col>
                </Row>
                <Row style="padding-top: 20px">
                    <Col span="24">
                        <Card dis-hover>
                            <p slot="title">
                                <Icon type="ios-people" size="large"/>
                                司机
                            </p>
                            <div>
                                <Tag color="success" v-for="item in selectedDriver" :key="item.sfzhm" :name="item.xm">{{ item.xm}}</Tag>
                                <Poptip placement="right" width="400" @on-popper-show="show">
                                    <Button icon="ios-add" type="dashed" size="small">添加司机</Button>
                                    <div class="api" slot="content">
                                        <choose-dvr v-if="showDriverPop" @driverChange="driverChange" :selectedData="selectedDriver"></choose-dvr>
                                    </div>
                                </Poptip>
                            </div>
                        </Card>
                    </Col>
                </Row>
            </Form>
            <div slot='footer'>
                <Button type="default" @click="colse" style="color: #949494">取消</Button>
                <Button type="primary" @click="AddDataListOk('addmess')">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import chooseCar from './choosedCar'
    import chooseDvr from './choosedDriver'

    export default {
        components: {chooseCar,chooseDvr},
        name: '',
        data() {
            return {
                showModal: true,
                operate: '新增',
                userSelectMss: '',
                //新增数据
                addmess: {
                    cdmc: '',//车队名称
                    dzxm: '',//队长姓名
                    sjhm: '',//手机号码
                    zt: '00',//状态
                    dzbh: '',//队长编号
                    clIds:'',
                    driverIds:'',
                },
                ruleInline: {
                    cdmc: [
                        {required: true, message: '请输填写车队名称', trigger: 'blur'}
                    ],
                    dzxm: [
                        {required: true, message: '请设队长姓名', trigger: 'blur'}
                    ],
                    sjhm: [
                        {required: true, message: '请输入手机号码', trigger: 'blur'}
                    ],
                    zt: [
                        {required: true, message: '请输入证件号码', trigger: 'blur'}
                    ]
                },
                userList: [],
                selectedCar:[],
                selectedDriver:[],
                showCarPop:false,
                showDriverPop:false,
            }
        },
        props: {
            messType: {
                type: Boolean,
                default: true
            },
            mess: {
                type: Object,
                default: {}
            },
            ty: {
                type: Array,
                default: []
            }
        },
        created() {
            log('字典状态', this.ty)
            this.fullcal()
            if (!this.messType) {
                this.addmess = this.mess
                this.operate = '编辑'
                this.userSelectMss = this.mess.dzbh
                this.getSelectedCar(this.addmess.cdbh);
                this.getSelectedDriver(this.addmess.cdbh);
            }else{
                this.showDriverPop = true;
                this.showCarPop = true;
            }
        },
        methods: {
            getSelectedCar(cdbh){
              this.$http.get(this.apis.CLGL.QUERY,{params:{cdbh:cdbh,pageSize:1000}}).then((res)=>{
                    if (res.code === 200 && res.page.list){
                        this.selectedCar = res.page.list;
                        this.showCarPop = true;
                    }
                })
            },
            getSelectedDriver(cdbh){
              this.$http.get(this.apis.JSY.QUERY,{params:{cdbh:cdbh,pageSize:1000}}).then((res)=>{
                    if (res.code === 200 && res.page.list){
                        this.selectedDriver = res.page.list;
                        this.showDriverPop = true;
                    }
                })
            },
            carChange(e){
                console.log('carChange',e);
                this.selectedCar = e;
            },
            driverChange(e){
              this.selectedDriver = e;
            },
            show(e){
                console.log(e);
            },
            cphChange(e) {
                console.log(e);
            },
            remoteMethod(k) {
                console.log('remoteMethod',k);
                var v = this
                let param = {
                    pageSize: 1000
                }
                console.log(k.length);
                if (k.length < 15){
                    param.xmLike = k
                }else{
                    param.id = k
                }
                this.$http.get(this.apis.USER.QUERY, {params: param}).then((res) => {
                    this.userList = res.page.list
                })
            },
            fullcal() {
                log('信息', this.mess)
            },
            colse() {
                var v = this
                v.$parent.compName = ''
            },
            userSelect() {
                console.log('userSelect');
                var v = this
                var listIndex = 0
                this.userList.forEach(function (item, index) {
                    if (item.yhid == v.userSelectMss) {
                        listIndex = index
                        v.addmess.dzxm = v.userList[listIndex].xm
                        v.addmess.sjhm = v.userList[listIndex].sjh
                        v.addmess.dzbh = v.userList[listIndex].yhid
                        return
                    }
                })
            },
            getUSER() {
                var v = this
                this.$http.get(this.apis.USER.QUERY).then((res) => {
                    log('用户列表', res)
                    this.userList = res.page.list
                })
            },
            //确认添加新用户进行前台表单数据验证
            AddDataListOk(name) {
                var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        let c = '';
                        for (let r of this.selectedCar){
                            c += r.clId+",";
                        }
                        this.addmess.clIds = c;
                        let d = '';
                        for (let r of this.selectedDriver){
                            d += r.sfzhm +",";
                        }
                        this.addmess.driverIds = d;
//                    	新增
                        if (v.messType) {
                            v.$http.post(this.apis.CD.ADD, v.addmess).then((res) => {
                                if (res.code === 200) {
                                    v.$Message.success('操作成功');
                                } else {
                                    v.$Message.warning(res.message);
                                }
                            }).then((res) => {
                                v.$parent.getmess()
                                v.$parent.compName = ''
                            }).catch((error) => {
                                v.$Message.error('出错了！！！');
                            })
                            //修改
                        } else {
                            v.$http.post(this.apis.CD.CHANGE, v.addmess).then((res) => {
                                if (res.code === 200) {
                                    v.$Message.success('操作成功');
                                } else {
                                    v.$Message.warning(res.message);
                                }
                            }).then((res) => {
                                v.$parent.getmess()
                                v.$parent.compName = ''
                            }).catch((error) => {
                                v.$Message.error('出错了！！！');
                            })
                        }
                    } else {
                        v.$Message.error('请认真填写用户信息!');
                    }
                })
            }
        }
    }
</script>

<style>
</style>
