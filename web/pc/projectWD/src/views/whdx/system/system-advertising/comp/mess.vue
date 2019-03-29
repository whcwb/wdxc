<style lang="less">
      @import '../../../../../styles/common.less';
</style>
<template>
      <div>
            <Modal v-model="showModal" width='900' :closable='mesF'
                   :mask-closable="mesF" title="活动编辑">
                  <div style="overflow: auto;height: 440px;">
                        <Form
                                ref="formItem"
                                :model="formItem"
                                :rules="ruleInline"
                                :label-width="100"
                                :styles="{top: '20px'}">
                              <Row>
                                    <Col span="12">
                                          <FormItem label='活动标题'>
                                                <Input type="text" v-model="formItem.hdbt" placeholder="请填写活动标题...">
                                                </Input>
                                          </FormItem>
                                    </Col>
                                    <Col span="12">
                                          <FormItem label='URL'>
                                                <Input type="text" v-model="formItem.url" placeholder="请填写URL...">
                                                </Input>
                                          </FormItem>
                                    </Col>
                              </Row>

                              <Row>
                                    <Col span="12">
                                          <FormItem label='活动类型'>
                                                <Select filterable clearable v-model="formItem.hdlx">
                                                      <Option value="00">微信</Option>
                                                      <Option value="01">智能站牌</Option>
                                                </Select>
                                          </FormItem>
                                    </Col>
                                    <Col span="12">
                                          <FormItem label='位置'>
                                                <Select filterable clearable v-model="formItem.wz">
                                                      <Option value="00">上</Option>
                                                      <Option value="01">中</Option>
                                                      <Option value="02">下</Option>
                                                </Select>
                                          </FormItem>
                                    </Col>
                              </Row>
                              <Row>
                                    <Col span="12">
                                          <FormItem label='活动时间'>
                                                <DatePicker v-model="cjsjInRange"
                                                            format="yyyy-MM-dd" type="daterange"
                                                            placement="bottom-end"
                                                            placeholder="请输活动时间"
                                                            style="width: 100%"></DatePicker>

                                                <!--<Input type="text" v-model="formItem.wjlx" placeholder="请填写广告类型...">
                                                </Input>-->
                                          </FormItem>
                                    </Col>
                                    <Col span="12">
                                          <FormItem label='附件类型'>
                                                <Select filterable clearable v-model="formItem.wjlx">
                                                      <Option value="00">图片</Option>
                                                      <Option value="01">视频</Option>
                                                </Select>
                                          </FormItem>
                                    </Col>
                              </Row>

                              <Row>
                                    <Col span="24">
                                          <FormItem v-if="formItem.wjlx==='00'">
                                                <div>
                                                      添加图片
                                                </div>
                                                <addlistfileImg
                                                        @addImg="addImg"
                                                        @removeFile="removeFile"
                                                        :urlList="mess.filePaths"
                                                ></addlistfileImg>
                                          </FormItem>
                                          <FormItem v-else-if="formItem.wjlx==='01'">
                                                <div>
                                                      添加视频
                                                </div>
                                                <addlistfileVideo></addlistfileVideo>
                                          </FormItem>
                                    </Col>
                              </Row>
                        </Form>
                  </div>
                  <div slot='footer'>
                        <Button type="default" @click="colse" style="color: #949494">取消</Button>
                        <Button type="primary" @click="save">确定</Button>
                  </div>
            </Modal>
      </div>
</template>

<script>
    import mixins from '@/mixins'

    import addlistfileImg from './addlistfileImg.vue'
    import addlistfileVideo from './addlistfileVideo.vue'

    export default {
        name: '',
        mixins: [mixins],
        components: {
            addlistfileImg,
            addlistfileVideo
        },
        data() {
            return {
                edie: false,
                showModal: true,
                mesF: false,
                formItem: {
                    wjlx: '00',
                    kssj: '',
                    jssj: ''
                },
                ruleInline: {},
                cjsjInRange: []
            }
        },
        props: {
            mess: {
                type: Object,
                default: {}
            }
        },
        watch: {
            cjsjInRange: function (newQuestion, oldQuestion) {
                this.formItem.kssj = this.getdateParaD(newQuestion[0])
                this.formItem.jssj = this.getdateParaD(newQuestion[1])
            },
        },
        created() {
            this.formItem = this.mess
            this.cjsjInRange = [this.mess.kssj, this.mess.jssj]
        },
        methods: {
            addImg(path) {
                this.formItem.filePaths += path + ",";
            },
            removeFile(url) {
                log('图片', url)
                log('原图片', this.formItem.filePaths)

                let aindex = this.formItem.filePaths.indexOf(url)//开始的位置
                let urlLength = url.length//传递的长度
                let filePath = this.formItem.filePaths//原数据拼接
                let filelength = filePath.length//原数据长度

                this.formItem.filePaths = filePath.slice(0, aindex) + filePath.slice(aindex + urlLength, filelength)

                log('修改数据', this.formItem.filePaths)
            },
            save() {
                var v = this
                let url = this.apis.ADVERTISING.CHANGE;
                this.$http.post(url, this.formItem).then((res) => {
                    if (res.code === 200) {
                        v.$Message.success(res.message);
                    } else {
                        v.$Message.error(res.message);
                    }
                    v.$parent.compName = ''
                    v.$parent.getmess()
                })
            },
            colse() {
                var v = this
                v.$parent.compName = ''
            }
        }
    }
</script>
