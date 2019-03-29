<style lang="less">
      @import '../../../../../styles/common.less';

      .headTit {
            text-align: center;
      }

      /*5 7 12 20 45 48*/
</style>
<template>
      <div class="box">
            <Card>
                  <div class="tit headTit padding">
                        <h1>
                              派车单
                        </h1>
                  </div>
                  <div class="body padding-top-10" style="border-top: solid #dddee1 2px;">
                        <Form :model="formItem" :label-width="50">
                              <Row>
                                    <Col span="8">
                                          <FormItem label="">
                                                <h3>
                                                      用车单位
                                                </h3>
                                                <Cascader @on-change="change" change-on-select :data="orgTree"
                                                          placeholder="请选择用车单位" filterable clearable></Cascader>
                                          </FormItem>
                                    </Col>
                                    <Col span="8">
                                          <FormItem label="">
                                                <h3>
                                                      用车人
                                                </h3>
                                                <Input v-model="formItem.ck" size="large"
                                                       placeholder="请填写用车人姓名"></Input>
                                          </FormItem>
                                    </Col>
                                    <Col span="8">
                                          <FormItem label="">
                                                <h3>
                                                      客户电话
                                                </h3>
                                                <Input v-model="formItem.cklxdh" size="large"
                                                       placeholder="请填写用车人电话"></Input>
                                          </FormItem>
                                    </Col>
                                    <Col span="8">
                                          <FormItem label="">
                                                <h3>
                                                      候车地点
                                                </h3>
                                                <Input v-model="formItem.hcdz" size="large"
                                                       placeholder="请填写候车地点..."></Input>
                                          </FormItem>
                                    </Col>
                                    <Col span="8">
                                          <FormItem label="">
                                                <h3>
                                                      目的地
                                                </h3>
                                                <Input v-model="formItem.mdd" size="large"
                                                       placeholder="请填写目的地点"></Input>
                                          </FormItem>
                                    </Col>
                                    <Col span="4">
                                          <FormItem label="">
                                                <h3>
                                                      出车时间
                                                </h3>
                                                <DatePicker v-model="formItem.yysj"
                                                            size="large" placement="left"
                                                            format="yyyy-MM-dd HH:mm:ss"
                                                            type="datetime"
                                                            :options="options"
                                                            placeholder="请填写用车时间"></DatePicker>
                                          </FormItem>
                                    </Col>
                                    <Col span="4">
                                          <FormItem label="">
                                                <h3>
                                                      单据类型
                                                </h3>
                                                <Select v-model="formItem.wf" filterable clearable size="large"
                                                        placeholder="请选择单据类型" filterable>
                                                      <Option value="00">单程</Option>
                                                      <Option value="10">往返</Option>
                                                </Select>
                                          </FormItem>
                                    </Col>
                                    <Col span="8">
                                          <Row>
                                                <Col span="12">
                                                      <FormItem label="">
                                                            <h3>
                                                                  费用来源
                                                            </h3>
                                                            <Select filterable clearable
                                                                    v-model="formItem.fkfs"
                                                                    @on-change="fyly"
                                                                    size="large" placeholder="请选择费用来源" filterable>
                                                                  <Option v-for="item in fromMoneyList"
                                                                          :value="item.key">{{item.val}}
                                                                  </Option>
                                                            </Select>
                                                      </FormItem>
                                                </Col>
                                                <Col span="12">
                                                      <FormItem label="">
                                                            <h3>
                                                                  课题
                                                            </h3>
                                                            <div class="box-row">
                                                                  <div class="body-O">
                                                                        <Select filterable clearable
                                                                                :disabled="formItem.fkfs!='20'"
                                                                                v-model="formItem.ktcode" size="large"
                                                                                placeholder="请选择课题" filterable>
                                                                              <Option v-for="item in ctasklList"
                                                                                      :value="item.key">{{item.val}}
                                                                              </Option>
                                                                        </Select>
                                                                  </div>
                                                                  <div title="新建课题">
                                                                        <Button type="info"
                                                                                icon="md-add"
                                                                                :disabled="formItem.fkfs!='20'"
                                                                                @click="newKT"></Button>
                                                                  </div>
                                                            </div>
                                                      </FormItem>
                                                </Col>
                                          </Row>
                                          <Row>
                                                <Col span="12">
                                                      <FormItem label="">
                                                            <h3>
                                                                  车型
                                                            </h3>
                                                            <Cascader @on-change="changeCLLX"
                                                                      :data="CasData"></Cascader>
                                                      </FormItem>
                                                </Col>
                                                <Col span="12">
                                                      <FormItem label="">
                                                            <h3>
                                                                  行程费用
                                                            </h3>
                                                            <Input v-model="formItem.zj" placeholder="请输入行程费用"></Input>
                                                      </FormItem>
                                                </Col>
                                          </Row>
                                    </Col>
                                    <Col span="16">
                                          <FormItem label="">
                                                <h3>
                                                      事由
                                                </h3>
                                                <Input v-model="formItem.sy" type="textarea" :rows="6"
                                                       placeholder="请填写用车事由"></Input>
                                          </FormItem>
                                    </Col>
                                    <Col span="24" style="padding: 8px 0 8px 50px;">
                                          <Button type="primary" style="width: 100%;" @click="AddNewlist">提交</Button>
                                    </Col>
                              </Row>
                        </Form>
                  </div>
            </Card>
            <component :is="compName" :dicListMess='dicListMess'></component>
      </div>
</template>

<script>
    import swal from 'sweetalert2'

    import addmessList from './comp/addmessList.vue'

    export default {
        name: 'NewCarList',
        components: {
            addmessList
        },
        data() {
            return {
                compName: '',
                dicListMess: 'ZDCLK0045',
                formItem: {
                    jgmc: '',//单位名称
                    jgdm: '',//单位Code
                    ck: '',//用车人
                    cklxdh: '',//用车电话
                    hcdz: '',//候车地点
                    mdd: '',//目的点
                    yysj: '',//发车时间
                    fkfs: '',//费用来源
                    ktcode: '',//课题
                    cllx: '',//车型
                    zws: '',//座位数
                    zj: '',//总价
                    sy: '',//备注
                },
                options:{
                    disabledDate (date) {
                        return date && date.valueOf() < Date.now() - 86400000;
                    }
                },
                // 机构代码(用车单位ID)	JGDM
                // 机构名称(用车单位名称)	jgmc
                // 乘客姓名  ck
                // 乘客联系电话	cklxdh
                //     候车地址  hcdz;
                // 目的地  mdd;
                // 预约时间   yysj
                // 付款方式(费用来源)	fkfs
                //车型cllx
                // 座位数		zws
                // 总价  zj

                // 事由	sy
                jgdmList: [],
                fromMoneyList: [//费用来源
                ],
                ctasklList: [//课题
                ],
                CasData: [{
                    value: '20',
                    label: '大车',
                    children: [{
                        value: '20',
                        label: '20',
                    }, {
                        value: '32',
                        label: '32',
                    }, {
                        value: '45',
                        label: '45',
                    }, {
                        value: '48',
                        label: '48',
                    }]
                }, {
                    value: '10',
                    label: '小车',
                    disabled: false,
                    children: [{
                        value: '5',
                        label: '5',
                    }, {
                        value: '7',
                        label: '7',
                    }, {
                        value: '11',
                        label: '11',
                    }]
                }],
                treeValue: [],
                orgTree: [],
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '车辆管理',
            }, {
                title: '订单管理',
            }, {
                title: '创建订单',
            }]);
            this.getKT()
        },
        mounted() {
            this.getOrgTree();
        },
        methods: {
            newKT() {
                this.compName = 'addmessList'
            },
            getKT() {
                this.ctasklList = this.dictUtil.getByCode(this, this.dicListMess)
                this.fromMoneyList = this.dictUtil.getByCode(this, 'ZDCLK0043')
            },
            fyly() {
                if (this.formItem.fkfs != '课题费用') {
                    this.formItem.task = ''
                }
            },
            change(vaule, selectedData) {
                this.formItem.jgmc = selectedData[selectedData.length - 1].label
                this.formItem.jgdm = selectedData[selectedData.length - 1].value
                this.treeValue = vaule;
            },
            changeCLLX(v, s) {
                this.formItem.cllx = v[0]
                this.formItem.zws = v[1]
            },
            getOrgTree() {
                this.$http.get(this.apis.FRAMEWORK.GET_TREE_Node).then((res) => {
                    this.orgTree = res.result
                })
            },
            //表单数据提交
            AddNewlist() {
                log(this.formItem)
                // if (this.treeValue.length === 0){
                //    this.$Message.error('请选择机构');
                //    return;
                // }
                // var v = this
                // swal({
                //   title: "是否提交数据?",
                //   text: "",
                //   icon: "success",
                //   buttons:['取消','确认'],
                // })
                // .then((willDelete) => {
                //   if (willDelete) {
                this.create();
                //   }
                // });
            },
            create() {
                // this.formItem.jgdm = this.treeValue[this.treeValue.length - 1];
                this.$http.post(this.apis.ORDER.ADD, this.formItem).then((res) => {
                    if (res.code === 200) {
                        this.$Message.success("创建成功");
                        this.formItem = {
                            jgmc: '',//单位名称
                            jgdm: '',//单位Code
                            ck: '',//用车人
                            cklxdh: '',//用车电话
                            hcdz: '',//候车地点
                            mdd: '',//目的点
                            yysj: '',//发车时间
                            fkfs: '',//费用来源
                            ktcode: '',//课题
                            cllx: '',//车型
                            zws: '',//座位数
                            zj: '',//总价
                            sy: '',//备注
                        }

                    } else {
                        this.$Message.error(res.message);
                    }
                })
            },
        }
    }
</script>

<style>
</style>
