<style lang="less">
      @import '../../../../styles/common.less';

</style>
<!--角色管理-->
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
                                          <span>{{$t("ROLE_MANAGEMENT")}}</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <!--<DatePicker v-model="cjsjInRange" format="yyyy-MM-dd" type="daterange" placement="bottom-end" placeholder="请输时间" @on-keyup.enter="findMessList()" style="width: 220px"></DatePicker>-->
                                          <Input v-model="param.jsmcLike"
                                                 :placeholder='$t("ROLE")'
                                                 style="width: 200px"
                                                 @on-keyup.enter="findMessList()"
                                                 @on-change="findMessList"></Input>
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
                  <Row style="position: relative;">
                        <Table ref="table"
                               :row-class-name="rowClassName"
                               :height="tabHeight"
                               :columns="tableTiT"
                               :data="tableData">

                        </Table>
                  </Row>
                  <Row class="margin-top-10 pageSty">
                        <Page :total=pageTotal
                              :current=param.pageNum
                              :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]
                              @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}'
                              show-total
                              show-elevator show-sizer placement='top'
                              @on-change='pageChange'>
                        </Page>
                  </Row>
            </Card>
            <component
                    :is="compName"
                    :messdata="messdata"
                    :usermesType="userMesType"
                    :Dictionary="Dictionary"
                    @listF='listF'>
            </component>
      </div>
</template>

<script>
    import mixins from '@/mixins'
    import i18nTabTit from '@/mixins/i18nTabTit'


    import addrole from './comp/addmess.vue'
    import modifyRolePermission from './comp/modifyRolePermission.vue'

    export default {
        name: 'char',
        mixins: [mixins, i18nTabTit],
        components: {
            addrole,
            modifyRolePermission
        },
        data() {
            return {
                messdata: '',
                userMesType: 'add',
                tabHeight: 220,
                compName: '',
                PickerTime: 2017,
                //分页
                //弹层
                showModal: false,
                tableTiT: [
                    {
                        title: "序号",
                        tit: "ORDER",
                        width: 80,
                        align: 'center',
                        type: 'index'
                    },
                    {
                        title: '角色名称',
                        tit: "ROLE_NAME",
                        align: 'center',
                        key: 'jsmc'
                    },
                    {
                        title: '类型',
                        tit: "TYPE",
                        align: 'center',
                        key: 'jslx',
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, this.lmdmDictionary, p.row.jslx)
                            return h('div', val)
                        }
                    },
                    {
                        title: '备注',
                        tit: "COMMONT",
                        align: 'center',
                        key: 'bz'
                    },
                    // {
                    //     title: '创建时间',
                    //     align:'center',
                    //     key: 'cjsj'
                    // },
                    {
                        title: '操作',
                        tit: "OPERATION",
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
                                        cursor: "pointer",
                                        margin: '0 8px 0 0'
                                    },
                                    on: {
                                        click: () => {
                                            this.userMesType = 'CHANGE'
                                            this.messdata = params.row
                                            this.compName = 'addrole'
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
                                            this.listDele(params.row.jsId)
                                            this.findMessList()
                                        }
                                    }
                                })
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
//              cjsjInRange:[],
                pageTotal:0,
                param: {
//                  cjsjInRange:'',
                    jsmcLike: '',
                    pageNum: 1,
                    pageSize: 8
                },
                Dictionary: [],
                lmdmDictionary: 'ZDCLK0004'
            }
        },
        watch: {
//			cjsjInRange:function(newQuestion, oldQuestion){
//				this.param.cjsjInRange = this.getdateParaD(newQuestion[0]) + ',' + this.getdateParaD(newQuestion[1])
//			},
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '系统管理',
            }, {
                title: '角色管理',
            }]),
                this.tabHeight = this.getWindowHeight() - 290
            this.findMessList()
            this.getLXDic()//字典数据
        },
        mounted() {
        },
        methods: {
            getLXDic() {
                this.Dictionary = this.dictUtil.getByCode(this, this.lmdmDictionary);
            },
            RootShowF(val) {
                this.messdata = val.row
                this.compName = 'mess'
            },
            //收索事件
            findMessList() {
                var v = this
                this.$http.get(this.apis.ROLE.QUERY, {params: v.param}).then((res) => {
                    //log(res)
                    v.tableData = res.page.list
                    v.pageTotal = res.page.total
                })
            },
            //数据删除
            listDele(id) {
                this.util.del(this, this.apis.ROLE.DELE, [id], () => {
                    this.findMessList();
                });
            },
            //添加数据
            AddDataList() {
                var v = this
                v.compName = 'addrole'
                this.userMesType = 'ADD'
                this.messdata = null
            },
            listF(res) {
                this.findMessList()
                this.compName = ''
            },
            pageChange(event) {
                var v = this
                v.page.pageNum = event
                this.findMessList()
            }
        }
    }
</script>
