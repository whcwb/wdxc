<!--校巴线路维护-->
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
                                          <span>校巴线路维护</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <!--<DatePicker v-model="cjsjInRange" format="yyyy-MM-dd" type="daterange" placement="bottom-end" placeholder="请输时间" @on-keyup.enter="findMessList()" style="width: 220px"></DatePicker>-->
                                          <Input v-model="param.xlmcLike" placeholder="请输入线路名称" style="width: 200px"
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
                        <Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="columns10"
                               :data="data9"></Table>
                  </Row>
                  <Row class="margin-top-10 pageSty">
                        <Page :total=pageTotal :current=param.pageNum :page-size=param.pageSize
                              :page-size-opts=[8,10,20,30,40,50]
                              @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator
                              show-sizer placement='top' @on-change='pageChange'></Page>
                  </Row>
            </Card>
            <component :is="compName"></component>
      </div>
</template>
<script>
    import mixins from '@/mixins'

    import expandRow from './table-expand.vue';
    import compModal from './comp/modal.vue'

    export default {
        components: {
            expandRow,
            compModal
        },
        mixins: [mixins],
        data() {
            return {
                tabHeight: 220,
                compName: '',
                addmessType: true,
                currentRow: [],
                pageTotal: 1,
                page: {
                    pageNum: 1,
                    pageSize: 8
                },
                columns10: [{
                    title: '#',
                    type: 'expand',
                    width: 50,
                    render: (h, params) => {
                        return h(expandRow, {
                            props: {
                                row: params.row
                            }
                        })
                    }
                },
                    {
                        title: '序号',
                        type: 'index',
                        align: 'center',
                        width: 60,
                    },
                    {
                        title: '线路名称',
                        align: 'center',
                        key: 'xlmc'
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        key: 'yxkssj',
                        render: (h, p) => {
                            return h('div', p.row.yxkssj + "点");
                        }
                    },
                    {
                        title: '结束时间',
                        align: 'center',
                        key: 'yxjssj',
                        render: (h, p) => {
                            return h('div', p.row.yxjssj + "点");
                        }
                    },
                    {
                        title: '方向',
                        align: 'center',
                        key: 'yxfx',
                        render: (h, p) => {
                            switch (p.row.yxfs) {
                                case '10':
                                    return h('div', '上行');
                                case '20':
                                    return h('div', '下行');
                            }
                        }
                    },
                    {
                        title: '状态',
                        align: 'center',
                        key: 'zt',
                        render: (h, p) => {
                            switch (p.row.zt) {
                                case '00':
                                    return h('div', '正常');
                                case '10':
                                    return h('div', '停用');
                            }
                        }
                    },
                    // {
                    // 	title: '运行方式',
                    // 	align: 'center',
                    // 	key: 'yxfx',
                    // 	render:(h,p)=>{
                    // 	    switch(p.row.yxfs){
                    // 			case '10':
                    // 			    return h('div','上行');
                    // 			case '20':
                    // 			    return h('div','下行');
                    // 		}
                    // 	}
                    // },
                    // {
                    // 	title: '创建人',
                    // 	align: 'center',
                    // 	key: 'cjr'
                    // },
                    {
                        title: '创建时间',
                        width: 180,
                        align: 'center',
                        key: 'cjsj'
                    },
                    {
                        title: '备注',
                        width: 100,
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
                                            this.addmessType = false
                                            this.currentRow = params.row;
                                            this.compName = 'compModal'
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
                                            //this.remove(params.index)
                                            this.listDele(params.row.id);
                                        }
                                    }
                                })
                            ]);
                        }
                    },
                ],
                data9: [{
                    siteName: '珞珈山',
                    direction: '上行',
                    type: '正常',
                    model: '大吧',
                    Founder: '王峰',
                    time: '2017-09-08 10:11:12',
                    mess: '备注信息'
                }],
                //收索
                cjsjInRange: [],
                param: {
                    cjsjInRange: '',
                    xlmcLike: '',
                    pageNum: 1,
                    pageSize: 8,
                    lx: '30'
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
                title: '线路维护',
            }])
            this.tabHeight = this.getWindowHeight() - 290
        },
        mounted() {
            this.getmess();
        },
        methods: {
            getmess() {
                if (this.cjsjInRange.length != 0 && this.cjsjInRange[0] != '' && this.cjsjInRange[1] != '') {
                    this.param.cjsjInRange = this.getdateParaD(this.cjsjInRange[0]) + "," + this.getdateParaD(this.cjsjInRange[1]);
                } else {
                    this.param.cjsjInRange = '';
                }
                this.$http.get(this.apis.XL.QUERY, {params: this.param}).then((res) => {
                    if (res.code === 200) {
                        this.data9 = res.page.list;
                        this.pageTotal = res.page.total;
                        log(this.data9);
                    }
                })
            },
            //收索事件
            findMessList() {
                this.getmess();
            },
            AddDataList() {
                this.addmessType = true
                this.currentRow = null
                this.compName = 'compModal'
            },
            listDele(id) {
                this.util.del(this, this.apis.XL.DELE, [id], () => {
                    this.getmess();
                });
            },
            pageChange(event) {
                var v = this
                this.param.pageNum = event;
                this.getmess();
            }
        }
    }
</script>
