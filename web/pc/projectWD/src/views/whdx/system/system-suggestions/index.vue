<style lang="less">
    @import '../../../../styles/common.less';
</style>
<!--意见反馈-->
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
                            <span>{{$t("SUGGESTION_FEEDBACK")}}</span>
                        </div>
                        <div class="body-r-1 inputSty">
                            <!--<DatePicker v-model="cjsjInRange" format="yyyy-MM-dd" type="daterange" placement="bottom-end" placeholder="请输时间" @on-keyup.enter="findMessList()" style="width: 220px"></DatePicker>-->
                            <Input v-model="param.lxrxmLike" :placeholder='$t("SERCH_FEEDBACK")' style="width: 200px"
                                   @on-keyup.enter="findMessList()"></Input>
                        </div>
                        <div class="butevent">
                            <Button type="primary" @click="findData()">
                                <Icon type="md-search"></Icon>
                                <!--查询-->
                            </Button>
                        </div>
                    </div>
                </div>
            </Row>
            <Row>
                <Table ref="table" :height="tabHeight" :row-class-name="rowClassName" :columns="tableTiT"
                       :data="tableData"></Table>
            </Row>
            <Row class="margin-top-10 pageSty">
                <Page :total=pageTotal :current=param.pageNum :page-size=param.pageSize
                      :page-size-opts=[8,10,20,30,40,50]
                      @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator
                      show-sizer placement='top' @on-change='pageChange'></Page>
            </Row>
        </Card>
        <component :is="compName" :pams='pams'></component>
    </div>
</template>

<script>
    import mixins from '@/mixins'
    import i18nTabTit from '@/mixins/i18nTabTit'


    import compModal from './comp/modal.vue'

    export default {
        name: 'char',
        mixins: [mixins, i18nTabTit],
        components: {
            compModal
        },
        data() {
            return {
                SpinShow: true,
                tabHeight: 220,
                pams: {},
                compName: '',
                PickerTime: 2017,
                //分页
                pageTotal: 1,
                page: {
                    pageNum: 1,
                    pageSize: 8
                },
                //弹层
                showModal: false,
                tableTiT: [{
                    title: "序号",
                    tit: "ORDER",
                    width: 60,
                    align: 'center',
                    type: 'index'
                },
                    {
                        title: '反馈人姓名',
                        tit: "FEEDBACK_NAME",
                        width: 100,
                        align: 'center',
                        key: 'lxrxm'
                    },
                    {
                        title: '反馈内容',
                        tit: "FEEDBACK_CONTENT",
                        align: 'center',
                        key: 'nr'
                    },
                    {
                        title: '状态',
                        tit: "STATUS",
                        align: 'center',
                        width: 80,
                        key: 'zt',
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, this.yjztCode, p.row.zt)
                            return h('div', [
                                h('span', {
                                    style: {
                                        fontWeight: 900,
                                        color: p.row.zt == '10' ? '#279a3b' : '#ed3f14',
                                    }
                                }, val)
                            ]);
                        }
                    },
                    {
                        title: '反馈类型',
                        tit: "FEEDBACK_STYLE",
                        width: 100,
                        align: 'center',
                        key: 'yjlx',
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, this.yjlxCode, p.row.yjlx)
                            return h('div', val)
                        }
                    },
                    {
                        title: '反馈时间',
                        tit: "FEEDBACK_TIME",
                        width: 100,
                        align: 'center',
                        key: 'cjsj'
                    },
                    {
                        title: '处理结果',
                        tit: "DEAL_RESULT",
                        align: 'center',
                        key: 'cljg'
                    },
                    {
                        title: '联系方式',
                        tit: "CONNECTION_METHOD",
                        align: 'center',
                        key: 'lxfs'
                    },
                    {
                        title: '操作',
                        tit: "OPERATION",
                        key: 'zt',
                        width: 150,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        disabled: params.row.zt == '10' ? true : false,
                                        type: 'success',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.feedback(params)
                                        }
                                    }
                                }, '解决')
                            ]);
                        }
                    }
                ],
                tableData: [],
                //form表单
                formTop: {},
                //select
                cityList: [],
                //收索
                cjsjInRange: [],
                param: {
                    cjsjInRange: '',
                    lxrxmLike: '',
                    zt: '',
                    pageNum: 1,
                    pageSize: 8
                },
                yjlxCode: 'ZDCLK0009',
                yjlxDic: [],
                yjztCode: 'ZDCLK0010',
                yjztDic: [],
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
                title: '系统管理',
            }, {
                title: '意见反馈',
            }]),
                this.tabHeight = this.getWindowHeight() - 290
            this.getmess()
            this.getLXDic()//字典数据
        },
        methods: {
            getLXDic() {
                this.yjlxDic = this.dictUtil.getByCode(this, this.yjlxCode);
                this.yjztDic = this.dictUtil.getByCode(this, this.yjztCode);
            },
            getmess() {
                var v = this
                this.$http.get(this.apis.SUGGES.QUERY, {params: this.param}).then((res) => {
                    v.tableData = res.page.list
                    v.SpinShow = false;
                })
            },
            feedback(params) {
                this.pams = params.row
                this.compName = 'compModal'
            },
            findData() {
                var v = this.getmess();
            },
            pageChange(event) {
                var v = this
                v.param.pageNum = event
                v.findData(v.page)
            },

        }
    }
</script>
