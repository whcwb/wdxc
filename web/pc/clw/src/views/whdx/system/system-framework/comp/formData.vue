<template>
      <div>
            <Modal v-model="showModal"
                   :closable='false' :mask-closable="false"
                   title="添加终端设备">
                  <div>
                        <Form
                                :model="param"
                                :rules="ruleInline"
                                ref="addmess"
                                :label-width="100">
                              <Row>
                                    <FormItem label='客户选择:'>
                                          <Input v-model="kh" readonly />
                                    </FormItem>
                                    <FormItem label='制造商:'>
                                          <Input v-model="param.zzsmc" placeholder="请填写设备制造商"/>
                                    </FormItem>
                                    <FormItem prop="zdLx" label='设备类型:'>
                                          <Select v-model="param.zdLx" placeholder="设备类型">
                                                <Option v-for="(item,index) in sbList" :value="item.key" :key="index">{{item.val}}</Option>
                                          </Select>
                                    </FormItem>
                                    <FormItem prop="fwnx" label='服务时间:'>
                                          <RadioGroup v-model="param.fwnx">
                                                <Radio label="1">1年</Radio>
                                                <Radio label="2">2年</Radio>
                                                <Radio label="3">3年</Radio>
                                                <Radio label="5">5年</Radio>
                                          </RadioGroup>
                                    </FormItem>
                                    <FormItem prop="zdbhs" label='设备编码:'>
                                          <Input v-model="param.zdbhs" type="textarea" :autosize="{minRows: 6,maxRows: 6}"
                                                 placeholder="请填写设备编号,编号之间请用逗号  ‘ , ’  分割" />
                                    </FormItem>
                              </Row>
                        </Form>
                  </div>
                  <div slot='footer'>
                        <Button type="default" @click="close">关闭</Button>
                        <Button type="primary" @click="save('addmess')">确定</Button>
                  </div>
            </Modal>
      </div>
</template>

<script>

    export default {
        name: '',
        data() {
            return {
                dataRead: false,
                showModal: true,
                kh:'',
                param: {
                    zdbhs:'',//终端编号
                    zdLx:'',//终端类型
                    jgdm:this.$parent.treeMess.jgdm,
                    fwnx:'',//服务年限  #永久
                },
                ruleInline: {
                    zdLx: [
                        {required: true, message: '请输选择终端类型', trigger: 'change'}
                    ],
                    fwnx: [
                        {required: true, message: '请输服务年限', trigger: 'change'}
                    ],
                    zdbhs: [
                        {required: true, message: '请输如设备编号', trigger: 'blur'}
                    ]
                },
                ztDictionary: [],// ZDCLK0031 设备状态
                sbList:[],//ZDCLK1017 终端类型
            }
        },
        created() {
            this.kh = this.$parent.treeMess.title
            this.getLXDic()
        },
        mounted() {
        },
        methods: {
            getLXDic() {
                this.ztDictionary = this.dictUtil.getByCode(this, 'ZDCLK0031');
                this.sbList = this.dictUtil.getByCode(this,'ZDCLK1017');
                console.log(this.sbList);
            },
            save(name) {
                var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$http.post('/api/zdgl/saveList', this.param).then((res) => {
                            if (res.code == 200) {
                                v.$Message.success(res.message);
                                v.close()
                            } else {
                                v.$Message.error(res.message);
                            }
                        }).catch((error) => {
                            v.$Message.error('出错了！！！');
                        })
                    } else {
                        v.$Message.error('请认真填写!');
                    }
                })
            },
            close() {
                this.$parent.componentName = '';
                this.$parent.$refs['zdtab'].getTabList()
            }

        }
    }
</script>

<style>

</style>
