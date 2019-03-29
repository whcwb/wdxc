<style lang="less">
      @import '../../../../../styles/common.less';
</style>
<!--班车排班-->
<template>
      <div class="box boxbackborder">
            <!--<Card>-->
            <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
				<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
                  <div style="height: 45px;line-height: 45px;">
                        <div class="margin-top-10 box-row">
                              <div class="titmess">
                                    <span>班车排班</span>
                              </div>
                              <div class="body-r-1 inputSty">
                                    <DatePicker v-model="todaytime"
                                                format="yyyy-MM-dd"
                                                :options="optionsDate"
                                                type="date"
                                                placement="bottom-end"
                                                placeholder="请输时间"
                                                style="width: 220px"></DatePicker>
                              </div>
                              <!--<div class="butevent">
                              </div>-->
                        </div>
                  </div>
            </Row>
            <div class="body" style="border: solid 1px #dedede;padding: 0 8px;background-color: #eef3fa">
                  <Row :gutter="16">
                        <Col class="margin-top-10" span="8"
                             :lg="8" :md="8" :sm="12" :xs="24"
                             v-for="item in tableData">
                              <Card style="width:100%">
                                    <div slot="title">
                                          <p>
                                                <Icon type="ios-film-outline"></Icon>
                                                {{item.xlmc}}
                                          </p>
                                          <Icon type="ios-clock"></Icon>
                                          {{item.yxkssj}}~{{item.yxjssj}}
                                    </div>
                                    <Button slot="extra" type="success"
                                            size="small"
                                            @click="BJ(item)"
                                            shape="circle" icon="md-create"></Button>
                                    <div style="min-height: 260px" class="box">
                                          <div class="body">
                                                <div class="box-row-list">
                                                      <div class="carListsty" v-for="it in item.clList"
                                                           style="height: 45px">
                                                            <div>
                                                                  <Icon type="md-person" size="16" color="#3bb84b"></Icon>
                                                                  ：
                                                                  {{it.sjxm}}
                                                            </div>
                                                            <div>
                                                                  <Icon type="ios-car" size="16"
                                                                        color="#ff8300"></Icon>
                                                                  ：
                                                                  {{it.cph}}
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>
                                    </div>
                              </Card>
                        </Col>
                  </Row>
            </div>
            <!--<Row style="position: relative;">-->
            <!--<Table ref="table" -->
            <!--size='large'-->
            <!--:height="tabHeight"-->
            <!--:row-class-name="rowClassName"-->
            <!--:columns="tableTiT"-->
            <!--:data="tableData"></Table>-->
            <!--</Row>-->
            <!--<Row class="margin-top-10 pageSty">
                    <Page :total=pageTotal :current=param.pageNum :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator show-sizer placement='top' @on-change='pageChange'></Page>
            </Row>-->
            <!--</Card>-->
            <component
                    :is="compName"
                    :mess="mess"
                    :pbTime="giveTime"></component>
      </div>
</template>

<script>
    import mixins from '@/mixins'


    import addmess from './comp/addmess'

    export default {
        name: '',
        mixins: [mixins],
        components: {
            addmess
        },
        data() {
            return {
            	optionsDate: {
                    disabledDate(date) {
                        return date && date.valueOf() < Date.now() - 86400000;
                    }
                },
                compName: '',
                mess: {},
                tabHeight: 220,
                SpinShow: true,
                todaytime: '',
                giveTime: '',
                //分页
                //---数据总数
                pageTotal: 2,
                page: {
                    //---当前页码
                    pageNum: 1,
                    //---每页显示条数
                    pageSize: 8
                },
                tableTiT: [
                    {
                        type: 'index',
                        width: 40,
                        align: 'center'
                    },
                    {
                        title: '线路名称',
                        key: 'xlmc'
                    },
                    {
                        title: '运行时间',
                        width: 150,
                        align: 'center',
                        render: (h, p) => {
                            return h('div', p.row.yxkssj + ' ~ ' + p.row.yxjssj)
                        }
                    },
                    {
                        title: '车辆信息',
                        key: 'clList',
                        align: 'center',
                        render: (h, params) => {
                            let cl = params.row.clList
                            if (cl === null) {
                                return
                            }
                            let span = []
                            for (var i = 0; i < cl.length; i++) {
                                span.push(
                                    h('span', {
                                        style: {
                                            marginRight: '5px',
                                        }
                                    }, cl[i].cph + ' ; ')
                                )
                            }
                            return h('div', span)
                        }
                    }, {
                        title: '操作',
                        key: 'action',
                        width: 100,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'success',
                                        icon: 'md-create',
                                        shape: 'circle',
                                        size: 'small'
                                    },
                                    style: {
                                        cursor: "pointer"
                                    },
                                    on: {
                                        click: () => {
                                            if (params.row.clList != null && params.row.clList.length > 0) {
                                                params.row.clList.forEach(function (item, index) {
                                                    item.ico = false
                                                })
                                            }
                                            this.mess = params.row
                                            this.compName = 'addmess'
                                        }
                                    }
                                })
                            ]);
                        }
                    }
                ],
                tableData: []
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '车辆管理',
            }, {
                title: '校巴管理',
            }, {
                title: '校巴排班',
            }]),
                this.giveTime = this.todaytime = this.getdateParaD(this.getdate())
            this.tabHeight = this.getWindowHeight() - 220
        },
        watch: {
            todaytime: function (n, o) {
                this.giveTime = this.getdateParaD(n)
                this.getmess()
            }
        },
        mounted() {
        },
        methods: {
            BJ(item) {
                if (item.clList != null && item.clList.length > 0) {
                    item.clList.forEach(function (item, index) {
                        item.ico = false
                    })
                }
                this.mess = item
                this.compName = 'addmess'
            },
            getmess() {
                var v = this
                log('排班数据2')
                //线路数据
                this.$http.post(this.apis.XLPBXX.QUERY, {
                    "clcx": "20",
                    "date2": v.giveTime,
                    'lulx': '20'
                }).then((res) => {
                    log('排班数据2', res)
                    v.tableData = res.result
                }).then((res) => {
                    v.SpinShow = false;
                }).catch((err) => {
                    log('bug')
                })
            },
            changeClick() {
                this.compName = 'addmess'
            },
            //分页点击事件按
            pageChange(event) {
                var v = this
                v.param.pageNum = event
                v.getmess()
            }
        }
    }
</script>

<style>

</style>
