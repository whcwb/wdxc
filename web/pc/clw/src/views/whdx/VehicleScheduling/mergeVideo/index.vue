<style lang="less">
      @import '../../../../styles/common.less';
</style>
<template>
      <div>
            <component :is="componentName"></component>
            <Card style="height: 800px;width: 1200px;">
                  <Row>
                        <Form
                                ref="formItem"
                                :model="formItem"
                                :rules="ruleInline"
                                :label-width="100"
                                :styles="{top: '20px'}">
                              <Row>
                                    <Col span="8">
                                          <FormItem label='终端编号'>
                                                <Select v-model="formItem.zdbh" filterable clearable>
                                                      <Option v-for="(item,index) in carList"
                                                              :value="item.zdbh" :key="item.zdbh">{{item.zdbh}}
                                                      </Option>
                                                </Select>
                                          </FormItem>
                                    </Col>
                                    <Col span="8">
                                          <FormItem label='开始时间'>
                                                <DatePicker v-model="formItem.startTime" type="datetime"
                                                            placeholder="请输时间" style="width: 220px"></DatePicker>
                                          </FormItem>
                                    </Col>
                                    <Col span="8">
                                          <FormItem label='提取时长' prop="duration">
                                                <Input v-model="formItem.duration" :number="true" placeholder="请输时长"
                                                       style="width: 220px"><span slot="append">秒</span></Input>
                                                <span style="color: red;margin-left: 40px;">*一次最多提取30秒</span>
                                          </FormItem>
                                    </Col>
                              </Row>
                              <Row>
                                    <Col span="24" style="text-align: center">
                                          <Button type="primary" @click="confirm('0-0')">合并前摄像头</Button>
                                          <Button type="primary" @click="confirm('1-0')">合并后摄像头</Button>
                                    </Col>
                              </Row>
                        </Form>
                  </Row>
                  <Row>
                        <div v-if="SpinShow" style="width:100%;height:100%;z-index: 1111;">
                              <Spin fix>
                                    <Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
                                    <div style="font-size: 30px;">文件上传中请稍后...</div>
                              </Spin>
                        </div>
                        <video v-if="videoUrl != null"
                               style="width: 100%;height: 600px;margin-top: 20px"
                               :src="videoPath+videoUrl"
                               autoplay="autoplay"
                               controls="controls"></video>
                  </Row>
            </Card>
      </div>
</template>

<script>

    Date.prototype.format = function (format) {
        var o = {
            "M+": this.getMonth() + 1, //month
            "d+": this.getDate(),    //day
            "h+": this.getHours(),   //hour
            "m+": this.getMinutes(), //minute
            "s+": this.getSeconds(), //second
            "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
            "S": this.getMilliseconds() //millisecond
        }
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length == 1 ? o[k] :
                    ("00" + o[k]).substr(("" + o[k]).length));
        return format;
    }

    import mixins from '@/mixins'
    import loading from './loadingModal'

    export default {
        name: '',
        mixins: [mixins],
        components: {
            loading
        },
        data() {
            return {
                componentName: '',
                SpinShow: false,
                videoPath: this.apis.VIDEO_PATH,
                cjsjInRange: [],
                ruleInline: {
                    startTime: [
                        {required: true, message: '请输入开始时间', trigger: 'blur'}
                    ],
                },
                formItem: {
                    startTime: '',
                    endTime: '',
                    zdbh: '',
                    duration: 10
                },
                carList: [],
                count: 0,
                bj: '',
                videoUrl: null,
                wait: false,
            }
        },

        computed: {
            GetSendhbsp() {
                return this.$store.state.app.sendhbsp
            }
        },
        watch: {
            // 以websocket推送方式获取视频是否上传成功
            GetSendhbsp: function (newQuestion, oldQuestion) {
                this.onResult(newQuestion);
            },
        },
        created() {
            this.getCarList();
        },
        methods: {
            onResult(r) {
                log(r);
                let m = JSON.parse(r);
                this.wait = false;
                this.componentName = '';
                this.SpinShow = false;
                this.$Message.success("拍摄成功!")
                this.videoUrl = m.url;
            },
            getCarList() {
                var v = this
                this.$http.get(this.apis.CLGL.GET_ORG_CAR_LIST).then((res) => {
                    this.carList = res.result
                    v.SpinShow = false;
                })
            },
            stopWait() {
                this.wait = false;
            },
            confirm(param) {
                var v = this
                this.$refs['formItem'].validate((valid) => {
                    if (valid) {
                        if (this.formItem.duration > 30) {
                            this.$Message.error('时长不能大于30秒')
                            return;
                        }
                        this.mergeVideo(param);
                    } else {
                        v.$Message.error('请将信息填写完整!');
                    }
                })
            },
            cancel() {
                this.wait = false;
                clearTimeout();
                this.SpinShow = false;
            },
            mergeVideo(param) {
                this.wait = true;
                this.videoUrl = null;
                let endTime = new Date(this.formItem.startTime.getTime());
                endTime.setSeconds(endTime.getSeconds() + this.formItem.duration);
                let params = {
                    deviceId: this.formItem.zdbh,
                    cmdType: 13,
                    cmdParams: param,
                    startTime: this.formItem.startTime.format('yyyy-MM-dd hh:mm:ss'),
                    endTime: endTime.format('yyyy-MM-dd hh:mm:ss'),
                }
                // this.SpinShow = true;
                this.$http.post(this.apis.CLJK.SEND_CONTROLL, params).then((res) => {
                    if (res.code === 200) {
                        this.$Message.success("发送成功!")
                        this.bj = res.result;
                        this.componentName = 'loading';
                        // this.check();
                    } else {
                        this.SpinShow = false;
                        this.$Message.error(res.message)
                    }
                })
            },
            /**
             *以轮询方式检查视频是否上传成功
             */
            check() {
                this.wait = false;
                let params = {
                    bj: this.bj,
                }
                this.$http.post(this.apis.CLOUD.QUERY, params).then((res) => {
                    if (res.code === 200) {
                        if (res.page.total > 0) {
                            this.SpinShow = false;
                            this.$Message.success("拍摄成功!")
                            this.videoUrl = res.page.list[0].url;
                        } else {
                            clearTimeout();
                            if (this.wait) {
                                setTimeout(() => {
                                    this.check()
                                }, 5000)
                            }
                            // let path = this.$route.path;
                            // log(path);
                            // if (path === ''){
                            //    setTimeout(()=>{
                            //        this.check()
                            //    },5000)
                            // }
                        }
                    } else {
                        this.$Message.error(res.message);
                    }
                })
            }
        }
    }
</script>

<style>

</style>
