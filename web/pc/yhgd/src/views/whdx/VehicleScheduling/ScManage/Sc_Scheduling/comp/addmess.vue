<style lang="less">
      .carListsty {
            padding: 3px 0;
            margin: 8px;
            border: solid #657180 1px;
            /*height: 30px;*/
            position: relative;
      }

      .carListstyB {
            padding: 3px 0 0 0;
            margin: 8px;
            border: solid #657180 1px;
            /*height: 30px;*/
            position: relative;
            .clickSty {
                  /*background-color: #2db7f5;*/
                  color: #fff;
                  cursor: pointer;
            }
            .borRight {
                  border-right: solid #a9a7a7 2px;
            }
      }
</style>
<template>
      <div>
            <Modal
                    v-model="showModal"
                    height="400"
                    :closable='false'
                    :mask-closable="false"
                    :fullscreen="true"
                    :footer-hide="true"
                    class-name="vertical-center-modal"
                    width='1500px'>
                  <div slot="header" class="box-row" style="align-items: center">
                        <div style="font-weight: 600;font-size: 18px">
                              {{mess.xlmc+'_线路排班'}}
                        </div>
                        <div style="margin-left: 22px">
                              <RadioGroup v-model="vertical">
                                    <Radio label="0" size="large">
                                          <span style="font-weight: 600;font-size: 18px">今日排班</span>
                                    </Radio>
                                    <Radio label="1">
                                          <span style="font-weight: 600;font-size: 18px">批量排班</span>
                                    </Radio>
                              </RadioGroup>
                        </div>
                        <div>
                              <DatePicker :readonly="vertical!='1'" :value="DatePicker"
                                          format="yyyy-MM-dd" type="daterange"
                                          placement="bottom-end" placeholder="Select date"
                                          :options="optionsDate"
                                          :split-panels="true"
                                          @on-change="DatePick"
                                          style="width: 200px"></DatePicker>
                        </div>
                        <div style="padding-left:8px" class="pbGroup">
                              <RadioGroup v-model="plpb" type="button" size="large" @on-change="changeDay">
                                    <Radio label="3">3天</Radio>
                                    <Radio label="7">7天</Radio>
                                    <Radio label="15">15天</Radio>
                                    <Radio label="30">30天</Radio>
                              </RadioGroup>
                        </div>
                        <div class="body-O" style="text-align: right">
                              <Button type="primary" @click="close">关闭</Button>
                        </div>
                  </div>
                  <div :style="{height:pagerHeight - 100 +'px'}">
                        <div style="height: 50%;border: solid 1px #000;overflow: auto">
                              <Row>
                                    <Col span="8" style="padding: 6px 8px" v-for="(val,key) of mess.clMapList">
                                          <Card style="width: 100%;">
                                                <div slot="title">
                                                      <p>{{key}}</p>
                                                </div>
                                                <div :style="{height:(pagerHeight - 260)/2-30 +'px',overflow:'auto'}">
                                                      <div class="box-row-list">
                                                            <Card style="margin:6px 0;width: 50%" v-for='(item,index) in val'>
                                                                  <div slot="title" style="">
                                                                        <Row>
                                                                              <Col span="12">
                                                                                    <p>
                                                                                          <Icon type="md-person" size="16"
                                                                                                color="#3bb84b"></Icon>
                                                                                          :{{item.sjxm}}
                                                                                    </p>
                                                                                    <p>
                                                                                          <Icon type="ios-car" size="16" color="#ff8300"></Icon>
                                                                                          :{{item.cph}}
                                                                                    </p>
                                                                              </Col>
                                                                              <Col span="12">
                                                                                    <Row type="flex" justify="end" :gutter="12">
                                                                                          <Col>
                                                                                                <Button  v-if="item.readTime" size="small" type="success" @click="saveTime(item.pbId,index)">完成</Button>
                                                                                                <Button  v-else type="info" size="small"  @click="item.readTime = true">时间维护</Button>
                                                                                          </Col>
                                                                                          <Col>
                                                                                                <Button type="error" size="small"  @click="deleteById(item.clId,index,item.pbbx)">删除</Button>
                                                                                          </Col>
                                                                                    </Row>
                                                                              </Col>
                                                                        </Row>
                                                                  </div>
                                                                  <div>
                                                                        <div style="padding: 0 3px; cursor: pointer;">
                                                                              <div v-if="item.readTime">
                                                                                    <!--{{item.pbbx | pbbx}}:-->
                                                                                    <TimePicker v-model="TimeVal" :transfer="true"  format="HH:mm" confirm type="timerange" placement="bottom-end" placeholder="请选择时间"></TimePicker>
                                                                                    <Button type="default" size="small"  @click="item.readTime = false">取消</Button>
                                                                              </div>
                                                                              <div v-else>
                                                                                    <!--{{item.pbbx | pbbx}}:-->
                                                                                    {{item.startTime}}~{{item.endTime}}
                                                                              </div>
                                                                        </div>
                                                                  </div>
                                                            </Card>
                                                      </div>
                                                </div>
                                          </Card>
                                    </Col>
                              </Row>

                              <!--<div class="box-row-list" v-if="false">-->
                                    <!--<Card style="width:350px;margin: 6px" v-for='(item,index) in mess.clList'>-->
                                          <!--<div slot="title" style="">-->
                                                <!--<Row>-->
                                                      <!--<Col span="12">-->
                                                            <!--<p>-->
                                                                  <!--<Icon type="md-person" size="16"-->
                                                                        <!--color="#3bb84b"></Icon>-->
                                                                  <!--:{{item.sjxm}}-->
                                                            <!--</p>-->
                                                            <!--<p>-->
                                                                  <!--<Icon type="ios-car" size="16" color="#ff8300"></Icon>-->
                                                                  <!--:{{item.cph}}-->
                                                            <!--</p>-->
                                                      <!--</Col>-->
                                                      <!--<Col span="12">-->
                                                            <!--<Row type="flex" justify="end" :gutter="12">-->
                                                                  <!--<Col>-->
                                                                        <!--<Button  v-if="item.readTime" size="small" type="success" @click="saveTime(item.pbId,index)">完成</Button>-->
                                                                        <!--<Button  v-else type="info" size="small"  @click="item.readTime = true">时间维护</Button>-->
                                                                  <!--</Col>-->
                                                                  <!--<Col>-->
                                                                        <!--<Button type="error" size="small"  @click="deleteById(item.clId,index,item.pbbx)">删除</Button>-->
                                                                  <!--</Col>-->
                                                            <!--</Row>-->
                                                      <!--</Col>-->
                                                <!--</Row>-->
                                          <!--</div>-->
                                          <!--<div>-->
                                                <!--<div style="padding: 0 3px; cursor: pointer;">-->
                                                      <!--<div v-if="item.readTime">-->
                                                            <!--{{item.pbbx | pbbx}}:-->
                                                            <!--<TimePicker v-model="TimeVal" :transfer="true" confirm type="timerange" placement="bottom-end" placeholder="Select time"></TimePicker>-->
                                                            <!--<Button type="default" size="small"  @click="item.readTime = false">取消</Button>-->
                                                      <!--</div>-->
                                                      <!--<div v-else>-->
                                                            <!--{{item.pbbx | pbbx}}:{{item.startTime}}~{{item.endTime}}-->
                                                      <!--</div>-->
                                                <!--</div>-->
                                          <!--</div>-->
                                    <!--</Card>-->
                              <!--</div>-->
                        </div>
                        <div style="height: 50%;border: solid 1px #000;overflow: auto">
                              <Row>
                                    <Col v-show="chrlist" span="4" v-for='(item,index) in chrlist'>
                                          <div class="carListstyB">
                                                <div style="padding: 0 3px">
                                                      <Icon type="md-person" size="16" color="#3bb84b"></Icon>
                                                      ：
                                                      {{item.sjxm}}
                                                </div>
                                                <div style="padding: 0 3px">
                                                      <Icon type="ios-car" size="16" color="#ff8300"></Icon>
                                                      ：
                                                      {{item.cph}}
                                                </div>
                                                <Row style="margin-top: 3px">
                                                      <Col span='8' align="center" class-name="clickSty borRight">
                                                            <Button type="info" size="small" long
                                                                    :disabled="item.classesList.indexOf('1')!=-1 || item.classesList.indexOf('7')!=-1"
                                                                    @click="AddList(item.clId,item.cph,item.sjxm,'1')"
                                                            >早班
                                                            </Button>
                                                      </Col>
                                                      <Col span='8' align="center" class-name="clickSty borRight">
                                                            <Button type="info" size="small" long
                                                                    :disabled="item.classesList.indexOf('2')!=-1 || item.classesList.indexOf('7')!=-1"
                                                                    @click="AddList(item.clId,item.cph,item.sjxm,'2')"
                                                            >中班
                                                            </Button>
                                                      </Col>
                                                      <Col span='8' align="center" class-name="clickSty borRight">
                                                            <Button type="info" size="small" long
                                                                    :disabled="item.classesList.indexOf('7')!=-1"
                                                                    @click="AddList(item.clId,item.cph,item.sjxm,'7')"
                                                            >全天
                                                            </Button>
                                                      </Col>
                                                </Row>
                                          </div>
                                    </Col>
                              </Row>
                        </div>
                  </div>
                  <div slot='footer'>
                        <!--<Button type="primary" @click="finish">完成</Button>-->
                        <Button type="primary" @click="close">关闭</Button>
                  </div>
            </Modal>
      </div>
</template>

<script>
    import mixins from '@/mixins'
    import moment from 'moment'
    export default {
        name: '',
        mixins: [mixins],
        data() {
            return {
                pagerHeight:0,
                plpb:'',
                optionsDate: {
                    disabledDate(date) {
                        return date && date.valueOf() < Date.now() - 86400000;
                    }
                },
                vertical: '0',
                showModal: true,
                chrlist: [],
                DatePicker: [this.pbTime, this.pbTime],
                TimeVal:[]
            }
        },
        filters: {
            pbbx: (val) => {
                if (val == '1') {
                    return '早班'
                } else if (val == '2') {
                    return '中班'
                } else if (val == '4') {
                    return '晚班'
                } else if (val == '7') {
                    return '全天'
                }
            }
        },
        watch: {
            // plpb:function(n,o){
            //     if(n!==''){
            //         this.vertical = '1'
            //     }
            // },
            vertical: function (val) {
                this.DatePicker = [this.pbTime, this.pbTime]
                if (val === '0'){
                  this.plpb = ''
                }else{
                    this.plpb = '3'
                    this.changeDay();
                }
            }
        },
        props: {
            mess: {
                type: Object,
                default: {
                      clList:[],
                      clMapList:{}
                }
            },
            pbTime: ''
        },
        created() {
            if (!this.mess.clMapList)this.mess.clMapList = {};
            this.pagerHeight = this.getWindowHeight()
            if (this.mess.clList == (null || "")) {
                this.mess.clList = []
            }
            log('信息传递', this.mess)
            this.getCarList()
        },
        methods: {
            changeDay(){
                let ld = moment().add(parseInt(this.plpb),'d');
                let startDate = this.DatePicker[0];
                this.DatePicker = null;
                this.DatePicker= [startDate,ld.format('YYYY[-]MM[-]DD')];
                this.vertical = '1';
            },
            pbtsClick(val){
                console.log(val);
                console.log(this.pbts);
            },
            saveTime(id,index){//排班id
                if(this.TimeVal.length !=2){
                    this.swal({
                        title:'请选择时间',
                        type:'error'
                    })
                    return
                }
                var v = this
                this.$http.post('/api/pb/updatepbtime',{id:id,startTime:this.TimeVal[0],endTime:this.TimeVal[1]}).then(res=>{
                    if(res.code == 200){
                        v.mess.clList[index].startTime=this.TimeVal[0]
                        v.mess.clList[index].endTime=this.TimeVal[1]
                        v.mess.clList[index].readTime = false
                        for (let k in v.mess.clMapList){
                            for (let r of v.mess.clMapList[k]){
                                r.readTime = false
                            }
                        }
                        v.getCarList()
                    }else{
                        this.swal({
                            title:res.message,
                            type:'error'
                        })
                    }
                }).catch(eer=>{

                })

            },
            DatePick(val) {
                console.log(val);
                var v = this
                this.DatePicker = [val[0], val[1]]
                this.$http.post(this.apis.XLPBXX.YZ, {
                    "xlId": v.mess.id,
                    "kssj": val[0],
                    "jssj": val[1],
                    'cx': '30'
                }).then((res) => {
                    log('数据数据数据车量', res)
                }).catch((error) => {
                      console.log(error);
                })
            },
            getCarList() {//获取车辆列表
                var v = this
                this.$http.post(this.apis.XLPBXX.CARLIST, {
                    'xlId': v.mess.id,
                    'date': v.pbTime,
                    'cx': '30'
                }).then((res) => {
                    if (res.code == 200) {
                        if (res.result) {
                            res.result.forEach((item, index) => {
                                item.classesList = item.classesList.join(',')
                            })

                            v.chrlist = res.result
                            // console.log(res);
                        } else {
                            v.chrlist = [{'cph': '无车辆数据', 'clId': '000000', 'sjxm': '****'}]
                        }
                    } else {
                        log('bug')
                    }
                    log('车辆数据', res)
                }).catch((err) => {
                    log('bug')
                })
            },
            AddList(carID, cph, sjxm, num) {
                var v = this
                let classNum = '早班'
                if(num == 1){
                    classNum = '早班'
                }else if(num == 2){
                    classNum = '中班'
                }else if(num == 7){
                    classNum = '全天'
                }
                this.$http.post(this.apis.XLPBXX.PLADD, {
                    "clId": carID,
                    "xlId": v.mess.id,
                    "kssj": this.DatePicker[0],
                    "jssj": this.DatePicker[1],
                    'cx': '30',
                    'classes': num,
                    'pbts':this.plpb
                }).then((res) => {
                    log('排版新增', res)
                    if (res.code == 200) {
                        console.log(res)
                        v.$Message.success(res.message);
                        // v.mess.clList.push({
                        //     'cph': cph,
                        //     'clId': carID,
                        //     'sjxm': sjxm,
                        //     'pbbx': num,
                        //     'startTime': res.result.startTime,
                        //     'endTime': res.result.endTime
                        // })
                          v.mess.clMapList[classNum] = []
                        v.mess.clMapList[classNum].push({
                                'cph': cph,
                                'clId': carID,
                                'sjxm': sjxm,
                                'pbbx': num,
                                    readTime:false,
                                'startTime': res.result.startTime,
                                'endTime': res.result.endTime
                        })
                        v.getCarList()
                    } else {
                        v.$Message.error(res.message);
                    }
                }).catch((error) => {
                      console.log(error);
                })
            },
            deleteById(carID, index, num) {
                var v = this
                let classNum = '早班'
                if(num == 1){
                    classNum = '早班'
                }else if(num == 2){
                    classNum = '中班'
                }else if(num == 7){
                    classNum = '全天'
                }
                this.$http.post(this.apis.XLPBXX.PLDELE, {
                    "clId": carID,
                    "xlId": v.mess.id,
                    "kssj": this.DatePicker[0],
                    "jssj": this.DatePicker[1],
                    'cx': '30',
                    'classes': num
                }).then((res) => {
                    if (res.code == 200) {
                        v.$Message.success(res.message);
                        v.$parent.getmess();
                        v.getCarList();
                        // v.mess.clList.splice(index, 1)
                        v.mess.clMapList[classNum].splice(index, 1)
                    } else {
                        v.$Message.error(res.message);
                    }
                }).catch((error) => {
                      console.log(error);
                })
            },
            finish() {
                var v = this
                v.$parent.getmess()
                v.$parent.compName = ''
            },
            close() {
                var v = this
                v.$parent.getmess()
                v.$parent.compName = ''
            }
        }
    }
</script>
<!--控制 排序-->
<style lang="less">
      .pbGroup{
            .ivu-radio-wrapper-checked{
                  background-color: #2b85e4;
                  color: #fff;
            }
      }
</style>
