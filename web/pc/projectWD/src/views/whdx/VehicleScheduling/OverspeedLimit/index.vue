<style lang="less">
      @import '../../../../styles/common.less';
</style>
<style lang="less" scoped="scoped">
      .fromTiT {
            /*text-align: right;*/
      }
</style>
<!--超速限定-->
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
                                          <span>超速限定</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <Input v-model="param.cphLike" placeholder="请输入车牌号" style="width: 200px"
                                                 @on-keyup.enter="getmess()"></Input>
                                    </div>
                                    <div class="butevent">
                                          <Button type="primary" @click="getmess()">
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
                                :row-class-name="rowClassName"
                                :columns="tableTiT"
                                :height="tabHeight"
                                :data="tableData"></Table>
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
                    :mess="mess"></component>
      </div>
</template>

<script>
    import mixins from '@/mixins'


    import newmes from './comp/newmes.vue'
    import change from './comp/change.vue'

    export default {
        name: 'char',
        components: {
            newmes, change
        },
        mixins: [mixins],
        data() {
            return {
                mess: {},
                compName: '',

                SpinShow: true,
                tabHeight: 220,
                //分页
                pageTotal: 1,
                page: {
                    pageNum: 1,
                    pageSize: 8
                },
                cxDict: [],
                cxDictCode: 'ZDCLK0002',
                showModal: false,
                tableTiT: [
                    {
                        title: "序号",
                        width: 80,
                        align: 'center',
                        type: 'index'
                    },
                    {
                        title: '车牌号码',
                        align: 'center',
                        width: 120,
                        key: 'cph'
                    },
                    {
                        title: '车速上限',
                        align: 'center',
                        key: 'sdsx',
                        render: (h, p) => {
                            return h('div', p.row.sdsx + ' KM/h')
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        type: 'action',
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
                                            this.compName = 'change'
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
                param: {
                    cphLike: '',
                    pageNum: 1,
                    pageSize: 8
                }
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '车辆管理',
            }, {
                title: '超速限定',
            }])
            this.tabHeight = this.getWindowHeight() - 290
            this.SpinShow = false;
            this.getmess()
        },
        methods: {
            getmess() {
                var v = this
                this.$http.get(this.apis.CS.QUERY, {params: v.param}).then((res) => {
                    v.tableData = res.page.list
                    v.pageTotal = res.page.total
                    v.SpinShow = false;
                })
            },
            listF(res) {
                this.getmess()
                this.compName = ''
            },
            AddDataList() {
                var v = this
                v.compName = 'newmes'
                this.messType = true;
            },
            findMessList() {
                var v = this
            },
            listDele(r) {
                this.util.del(this, this.apis.CS.DELE, [r.id], () => {
                    this.getmess();
                });
            },
            pageChange(event) {
                var v = this
                v.param.pageNum = event
                this.getmess()
            }
        }
    }
</script>
