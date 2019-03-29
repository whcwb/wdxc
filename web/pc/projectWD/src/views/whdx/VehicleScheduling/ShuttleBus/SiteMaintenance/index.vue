<!--班车站点维护-->
<style lang="less">
      @import '../../../../../styles/common.less';
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
                                          <span>班车站点维护</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <DatePicker v-model="cjsjInRange" format="yyyy-MM-dd" type="daterange"
                                                      placement="bottom-end" placeholder="请输时间"
                                                      @on-keyup.enter="findMessList()"
                                                      style="width: 220px"></DatePicker>
                                          <Input v-model="param.mcLike" placeholder="请输入站点名称" style="width: 200px"
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
                                :data="data9"></Table>
                        <div v-if="SpinShow"
                             style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 100;">
                              <Spin fix>
                                    <Icon type="load-c" :size=loading.size class="demo-spin-icon-load"></Icon>
                                    <div style="font-size: 30px;">{{loading.text}}</div>
                              </Spin>
                        </div>
                  </Row>
                  <Row class="margin-top-10" style="text-align: right;">
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
            <component :is="componentName"></component>
      </div>
</template>
<script>
    import mixins from '@/mixins'

    import formModal from './comp/formModal.vue'

    export default {
        components: {
            formModal
        },
        mixins: [mixins],
        data() {
            return {
                SpinShow: true,
                loading: this.$store.state.app.loading,
                tabHeight: 220,
                componentName: '',
                //弹层
                pageTotal: 1,
                choosedRow: null,
                columns10: [
                    {
                        title: '序号',
                        type: 'index',
                        align: 'center',
                        width: 60,
                    },
                    {
                        title: '站点名称',
                        align: 'center',
                        key: 'mc'
                    },
                    {
                        title: '状态',
                        align: 'center',
                        key: 'zt',
                        render: (h, params) => {
                            return h('div', params.row.zt == '00' ? "正常" : "停用");
                        }
                    },
                    {
                        title: '创建时间',
                        width: 180,
                        align: 'center',
                        key: 'cjsj'
                    },
                    {
                        title: '备注',
                        align: 'center',
                        key: 'bz'
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
                                            this.choosedRow = params.row;
                                            this.componentName = 'formModal';
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
                    },
                ],
                data9: [
                    {
                        siteName: '珞珈山',
                        type: '正常',
                        Founder: '王峰',
                        time: '2017-09-08 10:11:12',
                        mess: '备注信息'
                    }
                ],
                cjsjInRange: [],
                param: {
                    cjsjInRange: '',
                    mcLike: '',
                    pageNum: 1,
                    pageSize: 8,
                    lx: '20'
                }
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
                title: '站点维护',
            }])
            this.tabHeight = this.getWindowHeight() - 290
            setTimeout(() => {
                this.SpinShow = false;
            }, 6000);
        },
        mounted() {
            this.getmess()
        },
        methods: {
            getmess() {
                if (this.cjsjInRange.length != 0 && this.cjsjInRange[0] != '' && this.cjsjInRange[1] != '') {
                    this.param.cjsjInRange = this.getdateParaD(this.cjsjInRange[0]) + "," + this.getdateParaD(this.cjsjInRange[1]);
                } else {
                    this.param.cjsjInRange = '';
                }
                var v = this
                this.$http.get(this.apis.ZD.QUERY, {params: v.param}).then((res) => {
                    log('超速数据', res)
                    v.data9 = res.page.list
                    v.pageTotal = res.page.total
                    v.SpinShow = false;
                })
            },
            findMessList() {
                this.getmess();
            },
            AddDataList() {
                this.choosedRow = null;
                this.componentName = 'formModal'
            },
            //删除数据
            listDele(r) {
                this.util.del(this, this.apis.ZD.DELE, [r.id], () => {
                    this.getmess();
                });
//              this.$http.post(this.apis.ZD.DELE,{'ids':[r.id]}).then((res) =>{
//                  if(res.code===200){
//                      this.$Message.success('操作成功');
//                  }else{
//                      this.$Message.error('操作成功');
//                  }
//                  this.getmess()
//              })
            },
            pageChange(event) {
                this.param.pageNum = event;
                this.getmess();
            }
        }
    }
</script>
