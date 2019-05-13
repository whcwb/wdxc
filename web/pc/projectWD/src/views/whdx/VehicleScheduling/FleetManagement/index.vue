<!--车队管理-->
<style lang="less">
      @import '../../../../styles/common.less';
</style>
<template>
      <div class="boxbackborder">
            <Card>
                  <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
    			<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
                        <div style="height: 45px;line-height: 45px;">
                              <div class="margin-top-10 box-row">
                                    <div class="titmess">
                                          <span>{{$t("MOTORCADE_MANAGEMENT")}}</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <!--<DatePicker v-model="cjsjInRange" format="yyyy-MM-dd" type="daterange" placement="bottom-end" placeholder="请输时间" @on-keyup.enter="findMessList()" style="width: 220px"></DatePicker>-->
                                          <Input v-model="param.cdmcLike" :placeholder='$t("MOTORCADE_NAME")' style="width: 200px"
                                                 @on-keyup.enter="findMessList()"></Input>
                                    </div>
                                    <div class="butevent">
                                          <Button type="primary" @click="findMessList()">
                                                <Icon type="md-search"></Icon>
                                                <!--查询-->
                                          </Button>
                                          <Button type="primary" @click="AddDataList()">
                                                <Icon type="md-add"></Icon>
                                          </Button>
                                    </div>
                              </div>
                        </div>
                  </Row>
                  <Row>
                        <Table ref="table"
                               :height="tabHeight"
                               :row-class-name="rowClassName"
                               :columns="columns10"
                               :data="tableData"></Table>
                        <div v-if="SpinShow"
                             style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 1111;">
                              <Spin fix>
                                    <Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
                                    <div style="font-size: 30px;">数据加载中请稍后</div>
                              </Spin>
                        </div>
                  </Row>
                  <Row class="margin-top-10 pageSty">
                        <Page
                                :total=pageTotal
                                :current=param.pageNum
                                :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]
                                @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}'
                                show-total
                                show-elevator show-sizer placement='top'
                                @on-change='pageChange'></Page>
                  </Row>
            </Card>
            <component
                    :is="compName"
                    :mess="mess"
                    :messType="messType"
                    :ty="ztDict"
            ></component>
      </div>
</template>
<script>
    import mixins from '@/mixins'
    import i18nTabTit from '@/mixins/i18nTabTit'


    import newmes from './comp/newmes.vue'

    export default {
        name: '',
        components: {
            newmes
        },
        mixins: [mixins,i18nTabTit],
        data() {
            return {
                mess: {},
                messType: true,
                compName: '',

                SpinShow: true,
                tabHeight: 220,

                pageTotal: 1,
                page: {
                    pageNum: 1,
                    pageSize: 8
                },
                  tableTiT: [
                    {
                        title: '序号',
                          tit:"ORDER",
                        type: 'index',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '车队编号',
                          tit:"MOTORCADE_NUM",
                        align: 'center',
                        key: 'cdbh'
                    },
                    {
                        title: '车队名称',
                          tit:"MOTORCADE_NAME_TAB",
                        align: 'center',
                        key: 'cdmc'
                    },
                    {
                        title: '队长姓名',
                          tit:"CAPTAIN_NAME",
                        align: 'center',
                        key: 'dzxm'
                    },
                    {
                        title: '手机号码',
                          tit:"TELPHONE_NUM",
                        align: 'center',
                        key: 'sjhm'
                    },
                    {
                        title: '状态',
                          tit:"STATUS",
                        align: 'center',
                        key: 'zt',
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, this.ztDictCode, p.row.zt)
                            return h('div', val)
                        }
                    },
                    // {
                    //     title: '创建人',
                    //     align:'center',
                    //     key: 'cjr'
                    // },
                    // {
                    //     title: '创建时间',
                    //     width:'180',
                    //     align:'center',
                    //     key: 'cjsj'
                    // },
                    {
                        title: '操作',
                          tit:"OPERATION",
                        key: 'action',
                        width: 150,
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
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.messType = false
                                            this.mess = params.row
                                            this.compName = 'newmes'
                                        }
                                    }
                                }),
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        icon: 'md-close',
                                        shape: 'circle',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.listDele(params.row)
                                        }
                                    }
                                })
                            ]);
                        }
                    }
                ],
                tableData: [],
                //收索
                cjsjInRange: [],
                param: {
                    cjsjInRange: '',
                    cdmcLike: '',
                    pageNum: 1,
                    pageSize: 8
                },
                ztDict: [],
                ztDictCode: 'ZDCLK0016',
            }
        },
        watch: {
            cjsjInRange: function (newQuestion, oldQuestion) {
                this.param.cjsjInRange = this.getdateParaD(newQuestion[0]) + ',' + this.getdateParaD(newQuestion[1])
            },
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '车辆管理',
            }, {
                title: '订单管理',
            }])
            this.tabHeight = this.getWindowHeight() - 290
            this.getmess()
            this.getClztDict()
        },
        methods: {
            getClztDict() {
                this.ztDict = this.dictUtil.getByCode(this, this.ztDictCode);
                log('字典数据', this.ztDict)
            },
            getmess() {
                var v = this
                this.$http.get(this.apis.CD.QUERY, {params: v.param}).then((res) => {
                    log('车队数据', res)
                    v.tableData = res.page.list
                    v.pageTotal = res.page.total
                    v.SpinShow = false;
                })
            },
            AddDataList() {
                var v = this
                v.mess = {},
                    v.messType = true,
                    v.compName = 'newmes'
            },
            findMessList() {
                var v = this
                v.getmess()
            },
            listDele(id) {
                var v = this
                this.util.del(this, this.apis.CD.DELE, [id.cdbh], () => {
                    v.SpinShow = true;
                    v.getmess();
                });
            },
            pageChange(event) {
                var v = this
                v.param.pageNum = event
                v.getmess()
            },
        }
    }
</script>
