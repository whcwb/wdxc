<!--订单查阅-->
<style lang="less">
      @import '../../../../../styles/common.less';
</style>
<template>
      <div class="box boxbackborder">
            <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
    			<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
                  <div style="height: 45px;line-height: 45px;">
                        <div class="margin-top-10 box-row">
                              <div class="titmess">
                                    <span>订单查询</span>
                              </div>
                              <div class="body-r-1 inputSty">
                                    <Cascader
                                            style="width:300px;float: right;margin-top: 7px;margin-left: 4px;padding-right: 16px;"
                                            @on-change="change" change-on-select :data="orgTree"
                                            placeholder="请选择用车单位" filterable clearable></Cascader>
                                    <Input v-model="param.ckLike" type="text" placeholder="输入乘客姓名查询"
                                           style="width: 220px"></Input>
                                    <Input v-model="param.sjxmLike" type="text" placeholder="输入司机姓名查询"
                                           style="width: 220px"></Input>
                              </div>
                              <div class="butevent">
                                    <Button type="primary" @click="findMessList()">
                                          <Icon type="md-search"></Icon>
                                          <!--查询-->
                                    </Button>
                              </div>
                        </div>
                  </div>
            </Row>
            <!--<div class="body">-->
            <Row>
                  <Table ref="table"
                         :height="pageHeight"
                         :row-class-name="rowClassName"
                         :columns="columns10"
                         :data="data9"></Table>
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
            <!--</div>-->
            <Modal
                    v-model="showModal"
                    width='800'
                    :mask-closable="false"
                    title="信息详情">
                  <div slot='footer'></div>
            </Modal>
      </div>
</template>
<script>
    import mixins from '@/mixins'

    import expandRow from './table-expand.vue';

    export default {
        components: {
            expandRow
        },
        mixins: [mixins],
        data() {
            return {
                pageHeight: this.getWindowHeight() - 260,
                //收索
                datetime: '',
                param: {
                    ckLike: '',
                    pageNum: 1,
                    pageSize: 8
                },
                //弹层
                pageTotal: 1,
                page: {
                    pageNum: 1,
                    pageSize: 8
                },
                showModal: false,
                columns10: [
                    {
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
                        title: '用车单位',
                        align: 'center',
                        key: 'jgmc'
                    },
                    {
                        title: '用车人',
                        align: 'center',
                        key: 'ck'
                    },
                    {
                        title: '客户电话',
                        align:'center',
                        key: 'cklxdh'
                    },
                    {
                        title: '出车司机',
                        align:'center',
                        key: 'sjxm'
                    },
                    {
                        title: '司机电话',
                        align:'center',
                        key: 'DriverPhone'
                    },
                    {
                        title: '约车时间',
                        key: 'yysj',
                        render:(h,p)=>{
                            return h('div', p.row.yysj.substring(0, 13));
                        }
                    },
                    {
                        title: '约车地点',
                        key: 'hcdz'
                    },
                    {
                        title: '目的地',
                        key: 'mdd'
                    },
                    {
                        title: '座位数',
                        align: 'center',
                        key: 'zws',
                        render: (h, p) => {
                            let cx = ''
                            switch (p.row.cllx) {
                                case '10':
                                    cx = '小车';
                                    break;
                                case '20':
                                    cx = '大车';
                                    break;
                            }
                            return h('span', cx + '/' + p.row.zws)
                        }
                    }
                ],
                data9: [],
                treeValue:[],
                orgTree:[],
            }
        },
        created(){
        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '车辆管理',
            },{
                title: '订单管理',
            },{
                title: '订单查阅',
            }])
			this.findMessList()
            this.getOrgTree();
        },
        methods:{
            getOrgTree(){
                this.$http.get(this.apis.FRAMEWORK.GET_TREE_Node).then((res) =>{
                    this.orgTree = res.result
                })
            },
            change(vaule, selectedData) {
                this.param.jgdm = selectedData[selectedData.length - 1].value
                this.treeValue = vaule;
            },
            changeTime(val) {
            },
            pageChange(event) {
                var v = this
                v.page.pageNum = event
                this.param.pageNum = event;
                v.findMessList()
            },
            findMessList() {
                this.$http.get(this.apis.ORDER.QUERY, {params: this.param}).then((res) => {
                    if (res.code === 200 && res.page.list) {
                        this.data9 = res.page.list;
                        this.pageTotal = res.page.total;
                    }
                })
            },
        }
    }
</script>
