<style lang="less">
      @import '../../../../../styles/common.less';
</style>
<!--单位管理-->
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
                                          <span>{{$t("ORGANIZATION_MANAGEMENT")}}</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <Input v-model="param.dwmcLike" :placeholder='$t("ORGANIZATION_NAME")'
                                                 style="width: 200px"></Input>
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
                        <Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="tableTitle"
                               :data="pageData"></Table>
                  </Row>
                  <Row class="margin-top-10 pageSty">
                        <Page :total=param.total :current=param.pageNum :page-size=param.pageSize
                              :page-size-opts=[8,10,20,30,40,50]
                              @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator
                              show-sizer placement='top'
                              @on-change='pageChange'></Page>
                  </Row>
            </Card>
            <component :is="componentName"></component>
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
                v: this,
                SpinShow: true,
                apiRoot: this.apis.TEMP_UNIT,
                tabHeight: 220,
                componentName: '',
                choosedItem: null,
                tableTitle: [
                    {
                        title: '序号',
                        type: 'index',
                        align: 'center',
                        width: 60
                    },
                    {
                        title: '单位编号',
                        align: 'center',
                        key: 'dwbh'
                    },
                    {
                        title: '单位名称',
                        align: 'center',
                        key: 'dwmc'
                    },
                    {
                        title: '负责人',
                        align: 'center',
                        key: 'lxr'
                    },
                    {
                        title: '联系电话',
                        align: 'center',
                        key: 'lxdh'
                    },
                    {
                        title: '状态',
                        align: 'center',
                        key: 'zt',
                        render: (h, p) => {
                            return h('div', p.row.zt == '00' ? '正常' : '停用');
                        }
                    },
                    {
                        title: '登记次数',
                        align: 'center',
                        key: 'djcs'
                    },
                    {
                        title: '创建人',
                        align: 'center',
                        key: 'cjr'
                    },
                    {
                        title: '创建时间',
                        width: 100,
                        align: 'center',
                        key: 'cjsj'
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
                                            this.choosedItem = params.row
                                            this.componentName = 'formData'
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
                                            this.util.delete(this, [params.row.id])
                                        }
                                    }
                                })
                            ]);
                        }
                    }
                ],
                pageData: [],
                param: {
                    gnmcLike: '',
                    total: 0,
                    pageNum: 1,
                    pageSize: 8,
                },
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{title: '首页'}, {title: '车辆管理'}, {title: '临时车管理'}, {title: '单位管理'}])
            this.tabHeight = this.getWindowHeight() - 300
            this.getPageData()
        },
        methods: {
            getPageData() {
                this.util.getPageData(this);
            },
            add() {
                this.componentName = 'formData'
                this.choosedItem = null;
            },
            pageChange(event) {
                this.util.pageChange(this, event);
            },
        }
    }
</script>
