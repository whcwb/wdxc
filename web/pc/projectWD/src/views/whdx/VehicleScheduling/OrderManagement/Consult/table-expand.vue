<style scoped>
      .expand-row {
            margin-bottom: 16px;
            padding-left: 40px;
      }

      .topmar {
            margin-top: 10px;
      }
</style>
<template>
      <div style="min-height: 200px">
            <component :is="componentName"></component>
            <div slot="header">
                  <Menu mode="horizontal" theme="dark" active-name="1" @on-select="MenuClick">
                        <MenuItem name="1">
                              <Icon type="ios-people"></Icon>
                              基本信息
                        </MenuItem>
                        <MenuItem name="2">
                              <Icon type="ios-paper"></Icon>
                              流程记录
                        </MenuItem>
                        <MenuItem name="3">
                              <Icon type="ios-paper"></Icon>
                              车辆轨迹
                        </MenuItem>
                        <MenuItem name="4">
                              <Icon type="ios-paper"></Icon>
                              原始单据
                        </MenuItem>
                  </Menu>
            </div>
            <div v-show="horizontal=='1'" style="overflow: hidden;" class="topmar">
                  <Row class="expand-row">
                        <Col span="6">
                              <h3>候车地点：</h3>
                              <div>
                                    {{row.hcdz}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>目的地：</h3>
                              <div>
                                    {{row.hcdz}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>单据类型：</h3>
                              <div>
                                    {{row.wf}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>预约时间：</h3>
                              <div>
                                    {{row.yysj}}
                              </div>
                        </Col>
                  </Row>
                  <Row class="expand-row">
                        <Col span="6">
                              <h3>行车里程：</h3>
                              <div>
                                    {{row.lc}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>里程单价：</h3>
                              <div>
                                    {{row.dj}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>里程费用：</h3>
                              <div>
                                    {{row.lcf}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>等时：</h3>
                              <div>
                                    {{row.Etc}}
                              </div>
                        </Col>
                  </Row>
                  <Row class="expand-row">
                        <Col span="6">
                              <h3>过路费：</h3>
                              <div>
                                    {{row.glf}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>过桥费：</h3>
                              <div>
                                    {{row.BridgeFee}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>费用合计：</h3>
                              <div>
                                    {{row.zj}}
                              </div>
                        </Col>
                        <Col span="6">
                              <h3>备注：</h3>
                              <div style="word-wrap:break-word">
                                    {{row.sy}}
                              </div>
                        </Col>
                  </Row>
            </div>
            <div v-show="horizontal=='2'" style="overflow: hidden;" class="topmar">
                  <Steps :current="settleStatus">
                        <Step v-for="(item,i) in detail.oracleLog" :title="getStepTitle(item.ddzt)"
                              :content="item.cjsj"></Step>
                  </Steps>
            </div>
            <div v-show="horizontal=='3'" style="overflow: hidden;text-align: center;" class="topmar">
                  <div v-if="detail.sjqrsj == ''">
                        <h2>司机未确认行程结束</h2>
                  </div>
                  <div v-else>
                        <h2 v-if="simpleRoute == null">暂无轨迹信息</h2>
                        <div v-else>
                              <!--<img @click="showMap()"-->
                                   <!--:src="'http://api.map.baidu.com/staticimage/v2?ak=evDHwrRoILvlkrvaZEFiGp30&center='+simpleRoute.ksjd+','+simpleRoute.kswd+'&width=600&height=400&zoom=12&markers='+simpleRoute.jsjd+','+simpleRoute.jswd+'|'+simpleRoute.ksjd+','+simpleRoute.kswd+'&markerStyles=-1,http://47.98.39.45:9092/icon/map_line_end.png|-1,http://47.98.39.45:9092/icon/map_line_begin.png'"-->
                                   <!--class="imgItem">-->
                              <!--<br><span style="color: red">*点击图片可查看详情</span>-->
                              <show-map :index="row._index" :gpsList="NewGps"></show-map>
                        </div>

                  </div>
            </div>
            <div v-if="horizontal=='4' && detail.initialOracle.length > 0" v-for="item in detail.initialOracle"
                 style="overflow: hidden;" class="topmar">
                  <Col span="6" class="margin-top-5">
                        <h5>用车单位：</h5>{{item.jgmc}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>用车人：</h5>{{item.ck}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>电话：</h5>{{item.cklxdh}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>出车时间：</h5>{{item.yysj}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>候车地点：</h5>{{item.hcdz}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>目的地：</h5>{{item.mdd}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>行车里程：</h5>{{item.lc}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>等时：</h5>{{item.ck}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>过桥费：</h5>{{item.glf}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>过路费：</h5>{{item.glf}}
                  </Col>
                  <Col span="6" class="margin-top-5">
                        <h5>合计金额：</h5>{{item.zj}}
                  </Col>
                  <Col>
                        <div style="border-bottom: 1px solid gainsboro"></div>
                  </Col>
            </div>
      </div>
</template>
<script>
    // import historyMap from './historyTarckMap'
    import showMap from './hisList'

    export default {
        name: '',
        components: {
            showMap
        },
        data() {
            return {
                componentName: '',
                horizontal: '1',
                settleStatus: 1,
                detail: {},
                simpleRoute: null,
                NewGps:[],
            }
        },
        props: {
            row: Object
        },
        mounted() {
            this.getOrderDetails();
            console.log(this.row);
        },
        methods: {
            getStepTitle(zt) {
                switch (zt) {
                    case '10':
                        return '订单创建';
                    case '11':
                        return '审核通过';
                    case '12':
                        return '审核驳回';
                    case '13':
                        return '已派单';
                    case '20':
                        return '司机已确认';
                    case '21':
                        return '司机完成行程';
                    case '30':
                        return '队长确认';
                }
            },
            showMap() {
                this.componentName = 'historyMap';
            },
            MenuClick(event) {
                var v = this
                v.horizontal = event
            },
            getOrderDetails() {
                this.$http.get(this.apis.ORDER.orderDetails + '?id=' + this.row.id).then((res) => {
                    if (res.code == 200) {
                        this.detail = res.result;
                        this.settleStatus = this.detail.oracleLog.length;
                        this.NewGps = this.detail.gpslist
                        let gpsLog = this.detail.gpsLog;
                        if (gpsLog.length != 0) {
                            this.simpleRoute = {
                                ksjd: gpsLog[0].bdjd,
                                kswd: gpsLog[0].bdwd,
                                jsjd: gpsLog[gpsLog.length - 1].bdjd,
                                jswd: gpsLog[gpsLog.length - 1].bdwd,
                            };
                        }

                    }
                })
            }
        }
    };
</script>
