<style lang="less">
    @import '../../../../styles/common.less';
</style>
<!--功能管理-->
<template>
    <div class="boxbackborder">
        <!--<Card>-->
            <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
				<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
                <div style="height: 45px;line-height: 45px;">
                    <div class="margin-top-10 box-row">
                        <div class="titmess">
                            <span>功能管理</span>
                        </div>
                        <div class="body-r-1 inputSty">
                            <Input v-model="param.gnmcLike"
                                   @on-enter="v.util.getPageData(v)"
                                   placeholder="请输入功能名称" style="width: 200px"></Input>
                        </div>
                        <div class="butevent">
                            <Button type="primary" @click="v.util.getPageData(v)">
                                <Icon type="md-search"></Icon>
                            </Button>
                            <Button type="primary" @click="v.util.add(v)">
                                <Icon type="md-add"></Icon>
                            </Button>
                        </div>
                    </div>
                </div>
            </Row>
            <Row style="position: relative;">
                <Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="tableTiT" :data="pageData"></Table>
            </Row>
            <Row class="margin-top-10 pageSty">
                <Page :total=param.total :current=param.pageNum :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]  @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator show-sizer placement='top'
                      @on-change='pageChange'></Page>
            </Row>
        <!--</Card>-->
        <component
                :is="componentName"
                :Dictionary="Dictionary"></component>
    </div>
</template>

<script>
    import mixins from '@/mixins'
    import formData from './comp/formData.vue'

    export default {
        name: 'char',
        mixins: [mixins],
        components: {
            formData
        },
        data() {
            return {
                v:this,
                SpinShow: true,
                apiRoot:this.apis.FUNCTION,
                tabHeight: 220,
                componentName: '',
                choosedItem: null,
                //数据传输
                chmess: {},
                tableTiT: [
                    {title: "序号", width: 60, align: 'center', type: 'index', fixed: 'left'},
                    {title: '功能名称', align: 'center', width: 120, key: 'gnmc', fixed: 'left'},
                    {title: '功能代码', align: 'center', width: 140, key: 'gndm'},
                    {title: '服务代码', align: 'center', width: 120, key: 'fwdm'},
                    {
                        title: '状态', align: 'center', width: 120, key: 'zt',
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, this.lmdmDictionary, p.row.zt)
                            return h('div', val)
                        }

                    },
                    {title: '排序', align: 'center', width: 120, key: 'px'},
                    {title: '备注', align: 'center', width: 140, key: 'bz'},
                    {title: 'URL', align: 'center', width: 140, key: 'url'},
                    {title: '父节点', align: 'center', width: 140, key: 'fjd'},
                    {title: '跳转地址', align: 'center', width: 140, key: 'tzdz'},
                    {
                        title: '图标',
                        align: 'center',
                        width: 60,
                        key: 'tb',
                        render: (h, params) => {
                            return h('div', [
                                h('Icon', {
                                    props: {
                                        type: params.row.tb,
                                        size: '22'
                                    },
                                    on: {
                                        click: () => {
                                            //log('数据调试', params)
                                        }
                                    }
                                })
                            ]);
                        }
                    },
                    {
                        title: 'API前缀',
                        align: 'center',
                        width: 140,
                        key: 'apiQz'
                    },
                    {
                        title: 'API后缀',
                        align: 'center',
                        width: 140,
                        key: 'apiHz'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 100,
                        align: 'center',
                        fixed: 'right',
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
                                        cursor: "pointer",
                                        margin: '0 8px 0 0'
                                    },
                                    on: {
                                        click: () => {
                                            this.componentName = 'formData'
                                            this.choosedItem = params.row;
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
                                    style: {
                                        cursor: "pointer",
                                        margin: '0 8px 0 0'
                                    },
                                    on: {
                                        click: () => {
                                            this.util.delete(this,[params.row.gndm])
                                        }
                                    }
                                })
                            ]);
                        }
                    }
                ],
                pageData: [],
                param: {
                    orderBy:'gnmc asc',
                    gnmcLike: '',
                    total: 0,
                    pageNum: 1,
                    pageSize: 8,
                },
                Dictionary: [],
                lmdmDictionary: 'ZDCLK0007'
            }
        },
        created() {
            this.tabHeight = this.getWindowHeight() - 260
            this.$store.commit('setCurrentPath', [{title: '首页',}, {title: '系统管理',}, {title: '功能管理',}])
            this.util.initTable(this)
            this.getLXDic()//字典数据
        },
        methods: {
            getLXDic() {
                this.Dictionary = this.dictUtil.getByCode(this, this.lmdmDictionary);
            },
            pageChange(event) {
                this.util.pageChange(this, event);
            },
        }
    }
</script>
