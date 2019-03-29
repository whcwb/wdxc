<template>
    <div>
        <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
				<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
            <div style="height: 45px;line-height: 45px;">
                <div class="margin-top-10 box-row">
                    <div class="body-r-1 inputSty">
                        <Input v-model="param.zdbhLike"
                               placeholder="请输入终端编号" style="width: 200px"></Input>
                    </div>
                    <div class="butevent">
                        <Button type="primary" @click="getTabList">
                            <Icon type="md-search"></Icon>
                        </Button>
                    </div>
                </div>
            </div>
        </Row>
        <Table stripe size="small" :height="tabHeight" :columns="tabTit" :data="tabData"></Table>
        <div style="text-align: right;padding: 6px 0">
            <Page :total=total
                  :current=param.pageNum
                  :page-size=param.pageSize
                  :page-size-opts=[8,10,20,30,40,50]
                  show-total
                  show-elevator
                  show-sizer
                  placement='top'
                  @on-page-size-change='pageSizeChange'
                  @on-change='pageChange'>
            </Page>
        </div>
        <component
                :is="componentName"
                :mess="choosedRow"></component>
    </div>
</template>

<script>
    import zdxq from '../../../components/zd_xq'

    import jkdz from './jkdz'

    export default {
        name: "zdTab",
        components: {
            zdxq, jkdz
        },
        props: {
            tabHeight: 0
        },
        data() {
            return {
                componentName: '',
                choosedRow: {},
                tabTit: [
                    {
                        type: 'index2', align: 'center', width: 60, fixed: 'left',
                        render: (h, p) => {
                            return h('span', p.index + (this.param.pageNum - 1) * this.param.pageSize + 1);
                        }
                    },
                    {
                        title: '终端编号',
                        align: 'center',
                        minWidth: 180,
                        key: 'zdbh'
                    },
                    {
                        title: '服务年限',
                        align: 'center',
                        minWidth: 100,
                        key: 'fwnx',
                        filterMultiple: false,
                        filters: [
                            {
                                label: '1年',
                                value: 1
                            },
                            {
                                label: '2年',
                                value: 2
                            },
                            {
                                label: '3年',
                                value: 3
                            },
                            {
                                label: '5年',
                                value: 5
                            },
                        ],
                        filterRemote: (val, k, row) => {
                            this.param[k] = val[0]
                            this.param.pageNum = 1;
                            this.getTabList()
                            return true
                        },
                        render: (h, p) => {
                            if (p.row.fwnx) {
                                if (p.row.fwnx == '#') {
                                    return h('div', '永久');
                                }
                                return h('div', p.row.fwnx + '年')
                            } else {
                                return h('div', '-')
                            }
                        }
                    },
                    {
                        title: '终端类型',
                        align: 'center',
                        minWidth: 100,
                        key: 'zdLx',
                        filterMultiple: false,
                        filters: [
                            {
                                label: '后视镜',
                                value: '10'
                            },
                            {
                                label: 'OBD',
                                value: '20'
                            },
                            {
                                label: '定位器',
                                value: '30'
                            },
                        ],
                        filterRemote: (val, k, row) => {
                            this.param[k] = val[0]
                            this.param.pageNum = 1;
                            this.getTabList()
                            return true
                        },
                        render: (h, p) => {
                            let a = this.dictUtil.getValByCode(this, 'ZDCLK1017', p.row.zdLx)
                            return h('div', a)
                        }
                    },
                    {
                        title: '在线状态',
                        align: 'center',
                        key: 'zxzt',
                        minWidth: 100,
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, 'ZDCLK0032', p.row.zxzt)
                            return h('div', {
                                style: {
                                    color: p.row.zxzt == 20 ? '#727272' : (p.row.zxzt == 10 ? '#ed4014' : '#279a3b')
                                }
                            }, val)
                        }
                    },
                    {
                        title: '激活状态',
                        align: 'center',
                        key: 'jgzt',
                        minWidth: 100,
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, 'ZDCLK1018', p.row.jhzt)
                            return h('div', {
                                style: {
                                    color: p.row.jhzt == "10" ? '#727272' : p.row.jhzt == '20' ? '#279a3b' : 'red'
                                }
                            }, val)
                        }
                    },
                    {
                        title: '注册时间',
                        align: 'center',
                        key: 'cjsj',
                        minWidth: 100,
                        render: (h, p) => {
                            return h('div', p.row.cjsj.substring(0, 10))
                        }
                    },
                    {
                        title: '激活时间',
                        align: 'center',
                        key: 'zdKssj',
                        minWidth: 120,
                        render: (h, p) => {
                            if (p.row.zdKssj == '') {
                                return h('div', '-')
                            } else {
                                return h('div', p.row.zdKssj.substring(0, 10))
                            }
                        }
                    },
                    {
                        title: '到期时间',
                        align: 'center',
                        key: 'zdJssj',
                        minWidth: 120,
                        render: (h, p) => {
                            if (p.row.zdJssj == '') {
                                return h('div', '-')
                            } else if (p.row.zdJssj == '#') {
                                return h('div', '永久')
                            } else {
                                return h('div', p.row.zdJssj.substring(0, 10))
                            }
                        }
                    },
                    {
                        title: '制造商',
                        align: 'center',
                        key: 'zzsmc',
                        minWidth: 120,
                        render: (h, p) => {
                            if (p.row.zzsmc == '') {
                                return h('div', '-----')
                            } else {
                                return h('div', p.row.zdJssj)
                            }
                        }
                    },
                    {
                        title: '操作',
                        fixed: 'right',
                        width: 120,
                        align: 'center',
                        render: (h, p) => {
                            let buttons = [];
                            buttons.push(h('Tooltip', {
                                props: {content: '服务续期', placement: 'left', transfer: true}
                            }, [
                                h('Button', {
                                    props: {
                                        shape: "circle",
                                        type: 'info',
                                        size: 'small',
                                        disabled: p.row.fwnx == '#' ? true : false
                                    },
                                    on: {
                                        click: () => {
                                            this.choosedRow = p.row
                                            this.componentName = 'zdxq'
                                        }
                                    }
                                }, '续    ')
                            ]))
                            if (p.row.zdLx == '10'){
                                buttons.push(
                                    h('Tooltip',
                                        {
                                            props: {
                                                placement: 'top',
                                                content: '设置接口地址',
                                            },
                                        },
                                        [
                                            h('Button', {
                                                props: {
                                                    type: 'primary',
                                                    icon: 'ios-pulse',
                                                    shape: 'circle',
                                                    size: 'small'
                                                },
                                                style: {
                                                    marginRight: '5px'
                                                },
                                                on: {
                                                    click: () => {
                                                        this.choosedRow = p.row
                                                        this.componentName = 'jkdz'
                                                    }
                                                }
                                            }),
                                        ]
                                    ),
                                )
                            }
                            return h('div',buttons);
                        }
                    }
                ],
                tabData: [],
                total: 10,
                param: {
                    fwnx: '',//服务年限
                    zdLx: '',//终端类型
                    jg: '',
                    //分页数据
                    pageNum: 1,//当前页码
                    pageSize: 10//每页显示数
                },
            }
        },
        computed: {
            jgdm: function () {
                return this.$parent.treeMess.jgdm
            }
        },
        watch: {
            'jgdm': function (n, o) {
                if (n != '') {
                    this.param.jg = n
                    this.getTabList()
                } else if (n == '' && o != '') {
                    this.param.jg = o
                    this.getTabList()
                }
            }
        },
        created() {
            if (this.jgdm != '') {
                this.param.jg = this.jgdm
                this.getTabList()
            }
        },
        methods: {
            lxFliter(val, p, row) {
                this.param.fwnx = val
                this.getTabList()
                // return row.fwnx==val
                console.log(val);
                console.log(p);
                console.log(row);
            },
            pageSizeChange(size) {
                this.param.pageSize = size
                this.getTabList()
            },
            pageChange(num) {
                this.param.pageNum = num
                this.getTabList()
            },
            getTabList() {
                this.$http.post('/api/zdgl/getAllZd', this.param).then(res => {
                    if (res.code == 200) {
                        this.tabData = res.page.list
                        this.total = res.page.total
                    }
                }).catch(err => {

                })
            }
        }
    }
</script>

<style scoped>

</style>