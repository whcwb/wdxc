<style lang="less">
      @import '../../../../styles/common.less';
</style>
<template>
      <div>
            <Modal v-model="showModal"
                   :closable='false' :mask-closable="mesF"
                   title="新建终端设备">
                  <div>
                        <Form
                                :model="param"
                                :rules="ruleInline"
                                ref="addmess"
                                :label-width="100">
                              <Row>
                                    <FormItem label='客户选着:'>
                                          <Select value="" placeholder="客户选着">
                                                <Option value="0001" key="0001">客户一</Option>
                                                <Option value="0002" key="0002">客户二</Option>
                                          </Select>
                                    </FormItem>
                                    <FormItem label='客户选着:'>
                                          <Input value="GPS/定位器" readonly/>
                                    </FormItem>
                                    <FormItem label='服务时间:'>
                                          <RadioGroup value="0001">
                                                <Radio label="0001">1年</Radio>
                                                <Radio label="0002">2年</Radio>
                                                <Radio label="0003">3年</Radio>
                                                <Radio label="0005">5年</Radio>
                                          </RadioGroup>
                                    </FormItem>
                                    <FormItem label='设备编码:'>
                                          <Input value="" type="textarea" :autosize="{minRows: 6,maxRows: 6}"
                                                 placeholder="请填写设备编号,编号之间请用逗号  ‘ , ’  分割" />
                                    </FormItem>
                                    <FormItem label='备注信息:'>
                                          <Input value="" type="textarea" :autosize="{minRows: 3,maxRows: 3}" placeholder="请填写备注信息" />
                                    </FormItem>
                              </Row>
                        </Form>
                  </div>
                  <!--<div v-show="!dataRead" slot='footer'>-->
                        <!--<Button type="default" @click="close" style="color: #949494">取消</Button>-->
                        <!--<Button type="primary" @click="save('addmess')">确定</Button>-->
                  <!--</div>-->
                  <!--<div v-show="dataRead" slot='footer'>-->
                        <!--<Button type="default" @click="close">关闭</Button>-->
                        <!--<Button type="success" @click="seet('addmess')">设置</Button>-->
                  <!--</div>-->
            </Modal>
      </div>
</template>

<script>
    import treelist from '@/data/list.js'

    export default {
        name: '',
        data() {
            return {
                dataRead: false,
                showModal: true,
                mesF: false,
                param: {
                    zdbh: '',//终端编号
                    mc: '',//名称
                    cs: '',//厂商
                    zt: '',//终端状态
                    xh: '',
                    cmd: '',//接口地址
                    zdLx:'30'
                },
                ruleInline: {
                    zdbh: [
                        {required: true, message: '请输入终端编号', trigger: 'blur'}
                    ],
                    mc: [
                        {required: true, message: '请输入终端名称', trigger: 'blur'}
                    ],
                    xh: [
                        {required: true, message: '请输如设备型号', trigger: 'blur'}
                    ]
                },
                ztDictionary: [],
                ztlmdmDictionary: 'ZDCLK0031'//设备状态
            }
        },
        created() {
            this.getLXDic()
        },
        mounted() {
        },
        methods: {
            getLXDic() {
                this.ztDictionary = this.dictUtil.getByCode(this, this.ztlmdmDictionary);
            },
            save(name) {
                var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        let url = this.apis.ZDGL.ADD;
                        delete this.param.cl;
                        delete this.param.cph;
                        this.$http.post(url, this.param).then((res) => {
                            if (res.code == 200) {
                                v.$Message.success(res.message);
                                v.close()
                                v.$parent.getPageData()
                            } else {
                                v.$Message.error(res.message);
                            }
                        }).catch((error) => {
                            v.$Message.error('出错了！！！');
                        })
                    } else {
                        v.$Message.error('请认真填写用户信息!');
                    }
                })
            },
            bud() {
                var v = this
                swal({
                    title: "终端设备添加成功,是否绑定接口地址？",
                    text: "",
                    icon: "success",
                    buttons: ['取消', '确认'],
                }).then((willDelete) => {
                    if (willDelete) {
                        v.dataRead = true
                    } else {
                        v.close()
                    }
                });
            },
            seet(name) {
                var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        v.$http.post(this.apis.SBZDDZ.ADD, {
                            'deviceId': this.param.zdbh,
                            'cmdType': 91,
                            'cmd': this.param.cmd
                        }).then((res) => {
                            v.$Message.success(res.message);
                            v.$parent.getPageData()
                            v.close()
                        }).catch((error) => {
                            v.$Message.error('出错了！！！');
                        })
                    } else {
                        v.$Message.error('请认真填写用户信息!');
                    }
                })
            },
            close() {
                this.$parent.componentName = '';
                this.$parent.choosedRow = {}
            }

        }
    }
</script>

<style>

</style>
