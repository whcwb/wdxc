<!--
	收款管理
-->
<style lang="less">
      @import '../../../../styles/common.less';
</style>
<template>
      <div class="box">
            <Row class="tit" style="height: 120px;">
                  <Col span="6">
                        <Menu mode="horizontal" theme="light" active-name="1" @on-select="MenuClick">
                              <MenuItem name="1">
                                    <Icon type="ios-paper"></Icon>
                                    {{$t("RECEIVABLE")}}
                              </MenuItem>
                              <MenuItem name="2">
                                    <Icon type="android-checkbox-outline"></Icon>
                                    {{$t("RECEIVED")}}
                              </MenuItem>
                        </Menu>
                  </Col>
                  <Col span="15">
                        <div style="height: 60px;line-height: 60px;background-color: #fff;border-bottom: 1px solid #dddee1;padding: 0 15px;">
                              {{$t("FORMULA")}}
                        </div>
                  </Col>
                  <Col span="3">
                        <div style="height: 60px;line-height: 60px;background-color: #fff;border-bottom: 1px solid #dddee1;padding: 0 15px;">
                              <div v-show="param.ddzt === '30'">
                                    {{$t("RECEIVABLE")}}: {{list.length}}
                              </div>
                              <div v-show="param.ddzt === '40'">
                                    {{$t("RECEIVED")}}: {{list.length}}
                              </div>
                        </div>
                  </Col>
                  <Col span="24">
                        <div style="height: 60px;line-height: 60px;background-color: #fff;border-bottom: 1px solid #dddee1;padding: 0 15px;">
                              <Cascader
                                      style="width:300px;float: left;margin-top: 16px;margin-left: 4px;padding-right: 10px;"
                                      @on-change="change" change-on-select :data="orgTree" :placeholder='$t("USE_UNIT")'
                                      filterable clearable></Cascader>
                              <DatePicker v-model="param.startTime" :options="dateOpts" type="datetime"
                                          :placeholder='$t("ENTER_START_TIME")'></DatePicker>
                              <DatePicker v-model="param.endTime" :options="dateOpts" type="datetime"
                                          :placeholder='$t("ENTER_STOP_TIME")'></DatePicker>
                              <Button type="primary" @click="getData()">
                                    <Icon type="md-search"></Icon>
                              </Button>
                        </div>
                  </Col>
            </Row>
            <Row :gutter="16" class="margin-top-10 body clientList" v-for="(item,index) in list">
                  <Col span="24" :lg="24" :md="24" :sm="24" :xs="24" class="margin-top-10">
                        <Card style="width:100%" :id="'group_'+item.orgCode">
                              <div slot="title">
                                    <Icon type="md-person"></Icon>
                                    {{item.orgName}}
                              </div>
                              <span slot="extra">
			        	<span>
			        		{{$t("AMOUNT_COLLECT")}}：{{item.amount}}
			        		<Button type="success" size="small"
                                                        @click="print(item,index)">{{$t("PRINT")}}</Button>
			        		<Button v-if="param.ddzt === '30'" type="primary" size="small"
                                                        @click="confirm(index)">{{$t("DETERMINE")}}</Button>
			        	</span>
			        </span>
                              <!--信息-->
                              <div>
                                    <Table ref="table"
                                           border
                                           :columns="param.ddzt === '30' ? columns3 : columns4"
                                           height="220"
                                           @on-selection-change="(e)=>{tableSelectionChange(e,index)}"
                                           :data="item.orderList"></Table>
                              </div>
                        </Card>
                  </Col>
            </Row>
            <component :is="componentName"></component>
      </div>
</template>
<script>
    import swal from 'sweetalert2'
    import edit from './edit'
    import print from './print'

    export default {
        name: 'client',
        components: {
            edit, print
        },
        data() {
            return {
                dateOpts: {
                    shortcuts: [
                        {
                            text:this.$t("TODAY"),
                            value() {
                                return new Date();
                            }
                        },
                        {
                            text: this.$t("THREE_DAY_AGO"),
                            value() {
                                const date = new Date();
                                date.setTime(date.getTime() - 3600 * 1000 * 24 * 3);
                                return date;
                            }
                        },
                        {
                            text: this.$t("WEEK_AGO"),
                            value() {
                                const date = new Date();
                                date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                                return date;
                            }
                        }
                    ]
                },
                v: this,
                componentName: '',
                choosedItem: null,
                columns3: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '用车人员',
                          tit:"USERS",
                        key: 'ck'
                    },
                    {
                        title: '候车地点',
                          tit:"USERS",
                        key: 'hcdz'
                    },
                    {
                        title: '目的地',
                          tit:"DESTINATION",
                        key: 'mdd'
                    }, {
                        title: '司机',
                            tit:"DRIVER",
                        key: 'sjxm'
                    }, {
                        title: '车型',
                            tit:"CAR_TYPE_TAB",
                        key: 'zws'
                    }, {
                        title: '出车时间',
                            tit:"TIME_DEPARTURE",
                        key: 'yysj'
                    }, {
                        title: '里程(公里)',
                            tit:"MILEAGE",
                        key: 'lc'
                    }, {
                        title: '车费合计',
                            tit:"TOTAL_FARE",
                        key: 'zj'
                    }, {
                        title: '事由',
                            tit:"CONTENT_TAB",
                        key: 'sy'
                    },
                    {
                        title: '操作',
                          tit:"OPERATION",
                        key: 'action',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.choosedItem = params.row;
                                            this.componentName = 'edit';
                                        }
                                    }
                                }, this.$t("EDIT"))
                            ]);
                        }
                    }
                ],
                columns4: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '用车人员',
                        key: 'ck'
                    },
                    {
                        title: '候车地点',
                        key: 'hcdz'
                    },
                    {
                        title: '目的地',
                        key: 'mdd'
                    }, {
                        title: '司机',
                        key: 'sjxm'
                    }, {
                        title: '车型',
                        key: 'zws'
                    }, {
                        title: '出车时间',
                        key: 'yysj'
                    }, {
                        title: '里程(公里)',
                        key: 'lc'
                    }, {
                        title: '车费合计',
                        key: 'zj'
                    }, {
                        title: '事由',
                        key: 'sy'
                    },
                ],
                munName: '1',
                param: {
                    ddzt: '30',
                    ck: '',
                    jgmc: '',
                    startTime: '',
                    endTime: ''
                },
                list: [],
                selectedData: [],
                treeValue: [],
                orgTree: [],
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '财务结算',
            }, {
                title: '收款管理',
            }])
            this.getData();
            this.getOrgTree();
        },
        mounted() {

        },
        methods: {
            getOrgTree() {
                this.$http.get(this.apis.FRAMEWORK.GET_TREE_Node).then((res) => {
                    this.orgTree = res.result
                })
            },
            change(vaule, selectedData) {
                this.param.jgdm = selectedData[selectedData.length - 1].value
                this.treeValue = vaule;
            },
            tableSelectionChange(e, i) {
                this.selectedData[i] = e;
            },
            getData() {
                this.list = [];
                let startTime = this.param.startTime;
                let endTime = this.param.endTime;
                if (typeof startTime === 'object') {
                    this.param.startTime = startTime.format('yyyy-MM-dd hh:mm:ss');
                }
                if (typeof endTime === 'object') {
                    this.param.endTime = endTime.format('yyyy-MM-dd hh:mm:ss');
                }
                this.$http.get(this.apis.ORDER.collectingList, {params: this.param}).then((res) => {
                    if (res.code === 200 && res.result) {
                        this.list = res.result;
                        for (let r of this.list) {
                            this.selectedData.push([]);
                        }
                    }
                })
            },
            confirm(index) {
                if (this.selectedData[index].length === 0) {
                    this.$Message.error("请选择订单");
                    return;
                }
                swal({
                    title: "确认已付款?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                }).then((confirm) => {
                    if (confirm.value) {
                        let ids = '';
                        for (let r of this.selectedData[index]) {
                            ids += r.id + ',';
                        }
                        let v = this;
                        let url = this.apis.ORDER.collectingConfirm;
                        v.$http.post(url, {'ids': ids}).then((res) => {
                            if (res.code === 200) {
                                v.$Message.success(res.message);
                                this.getData();
                            } else {
                                v.$Message.error(res.message);
                            }
                        })
                    }
                });
            },
            //选项卡的切换
            MenuClick(event) {
                this.param.ddzt = (event === '1' ? '30' : '40');
                this.getData();
            },
            //卡片事件
            changeLimit(mes) {
                alert(mes)
            },
            print(item, index) {
                if (this.selectedData[index].length === 0) {
                    this.$Message.error("请选择订单");
                    return;
                }
                item.choosedOrderList = this.selectedData[index];
                this.choosedItem = item;
                this.componentName = 'print';
            },
            show() {

            }
        }
    }
</script>

<style>
</style>
