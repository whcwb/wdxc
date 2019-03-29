<style lang="less">
      @import '../../../../../styles/common.less';

      .carListstyIN {
            padding: 3px;
            margin: 8px;
            border: solid #657180 1px;
            /*height: 30px;*/
            position: relative;
      }
</style>
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
                                    <span>校巴排班</span>
                              </div>
                              <div class="body-r-1 inputSty">
                                    <DatePicker v-model="todaytime"
                                                format="yyyy-MM-dd"
                                                type="date"
                                                :options="optionsDate"
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
                                    <div :style="{height:tabHeight+'px'}">
                                          <Card style="width:100%" v-for="(val,key) of item.clMapList">
                                                <div slot="title">
                                                      <p>{{key}}</p>
                                                </div>
                                                <div :style="{height:tabHeight/3-83+'px',overflow:'auto'}">
                                                      <div class="box-row-list">
                                                            <div class="carListstyIN" v-for="it in val"
                                                                 style="height: 70px;padding:3px 6px">
                                                                  <div>
                                                                        <Icon type="md-person" size="16"
                                                                              color="#3bb84b"></Icon>
                                                                        ：
                                                                        {{it.sjxm}}
                                                                  </div>
                                                                  <div>
                                                                        <Icon type="ios-car" size="16"
                                                                              color="#ff8300"></Icon>
                                                                        ：
                                                                        {{it.cph}}
                                                                  </div>
                                                                  <div style="padding:3px 0;cursor: pointer;">
                                                                        <!--{{it.pbbx | pbbx}}:-->
                                                                        {{it.startTime}}~{{it.endTime}}
                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                          </Card>
                                          <!--<div style="height: 100%;overflow: auto">-->
                                          <!--<div class="box-row-list">-->
                                          <!--<div class="carListstyIN" v-for="it in item.clList"-->
                                          <!--style="height: 70px;padding:3px 6px">-->
                                          <!--<div>-->
                                          <!--<Icon type="md-person" size="16"-->
                                          <!--color="#3bb84b"></Icon>-->
                                          <!--：-->
                                          <!--{{it.sjxm}}-->
                                          <!--</div>-->
                                          <!--<div>-->
                                          <!--<Icon type="ios-car" size="16" color="#ff8300"></Icon>-->
                                          <!--：-->
                                          <!--{{it.cph}}-->
                                          <!--</div>-->
                                          <!--<div style="padding:3px 0;cursor: pointer;">-->
                                          <!--{{it.pbbx | pbbx}}:{{it.startTime}}~{{it.endTime}}-->
                                          <!--</div>-->
                                          <!--</div>-->
                                          <!--</div>-->
                                          <!--</div>-->
                                    </div>
                              </Card>
                        </Col>
                  </Row>
            </div>
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
            }])
            this.giveTime = this.todaytime = this.getdateParaD(this.getdate())
            this.tabHeight = this.getWindowHeight() - 340
            this.getmess()
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
                    "clcx": "30",
                    "date2": v.giveTime,
                    'lulx': '30'
                }).then((res) => {
                    log('排班数据2', res)
                    res.result.forEach((it, index) => {
                        for(var item in it.clMapList){
                              it.clMapList[item].forEach((its, val) => {
                                    its.readTime = false
                              })
                        }
                    })


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
