<style lang="less">
      @import '../../../../styles/common.less';

</style>
<!--查询统计-->
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
                                          <span>{{$t("OBD_MANAGEMENT")}}</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <!--<Input v-model="param.mcLike" placeholder="请输入终端名称" style="width: 200px"-->
                                                 <!--@on-keyup.enter="getPageData()"></Input>-->
                                          <Input v-model="param.zdbhLike" :placeholder='$t("TERMINAL_NUM")' style="width: 200px"
                                                 @on-keyup.enter="getPageData()"></Input>
                                          <Input v-model="param.cphLike" :placeholder='$t("CAR_NUMBER")' style="width: 200px"
                                                 @on-keyup.enter="getPageData()"></Input>
                                          <!--<Input v-model="param.versionLike" placeholder="请输入版本号" style="width: 200px"-->
                                                 <!--@on-keyup.enter="getPageData()"></Input>-->
                                          <Select v-model="param.zxzt"
                                                  @on-change='getPageData'
                                                  clearable
                                                  :placeholder='$t("ON_LINE_STATE")'
                                                  filterable style="width: 160px;">
                                                <Option v-for="(item,index) in Dictionary"
                                                        :value="item.key"
                                                        style="text-align: left;"
                                                        :key="index">{{item.val}}
                                                </Option>
                                          </Select>
                                    </div>
                                    <div class="butevent">
                                          <Tooltip :content='$t("INQUIRE")' placement="top">
                                                <Button type="primary" @click="getPageData()">
                                                      <Icon type="md-search"></Icon>
                                                </Button>
                                          </Tooltip>
                                          <!--<Tooltip content="新增" placement="top">-->
                                                <!--<Button type="primary" @click="AddMess()">-->
                                                      <!--<Icon type="md-add"></Icon>-->
                                                <!--</Button>-->
                                          <!--</Tooltip>-->
                                          <!--<Tooltip content="批量导入" placement="top">-->
                                                <!--<Button type="success" @click="componentName='plmess'">-->
                                                      <!--<Icon type="md-cloud-upload" size="18"/>-->
                                                <!--</Button>-->
                                          <!--</Tooltip>-->
                                    </div>
                              </div>
                        </div>
                  </Row>
                  <Row>
                        <Table ref="table" :height="tabHeight" :row-class-name="rowClassName" :columns="tableTiT"
                               :data="tableData"></Table>
                        <div v-if="SpinShow"
                             style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 100;">
                              <Spin fix>
                                    <Icon type="load-c" :size=loading.size class="demo-spin-icon-load"></Icon>
                                    <div style="font-size: 30px;">{{loading.text}}</div>
                              </Spin>
                        </div>
                  </Row>
                  <Row class="margin-top-10 pageSty">
                        <Page :total=pageTotal :current=param.pageNum :page-size=param.pageSize
                              :page-size-opts=[8,10,15,20,30,40,50]
                              @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator
                              show-sizer placement='top' @on-change='pageChange'></Page>
                  </Row>
            </Card>
            <component
                    :is="componentName"
                    :mess="choosedRow"></component>
      </div>
</template>

<script>
    import mixins from '@/mixins'
    import formData from './formData'
    import change from './change'
    import jkdz from './jkdz'
    import update from './update'
    import setting from './setting'
    import plmess from './plMess'
    import i18nTabTit from '@/mixins/i18nTabTit'

    import zdxq from '../../components/zd_xq'
    export default {
        name: 'char',
        mixins: [mixins,i18nTabTit],
        components: {
            zdxq,
            formData, change, jkdz, setting, update, plmess
        },
        data() {
            return {
                PickerTime: 2017,
                updateMode: 'single',
                SpinShow: false,
                //分页
                loading: this.$store.state.app.loading,
                pageTotal: 1,
                cjsjInRange: '',
                tabHeight: 220,
                componentName: '',
                choosedRow: {},
                //弹层
                showModal: false,
                  tableTiT: [
                    {   tit:"ORDER",
                        type: 'index2', align: 'center', minWidth: 80, fixed: 'left',
                        render: (h, params) => {
                            return h('span', params.index + (this.param.pageNum - 1) * this.param.pageSize + 1);
                        }
                    },
                    {
                        title: '终端编号',
                          tit:"TERMINAL_NUMBER",
                        align: 'center',
                        key: 'zdbh',
                        minWidth: 180
                    },
                    {
                        title: '绑定车辆',
                          tit:"BOUND_VEHICLE",
                        align: 'center',
                        key: 'cph',
                        minWidth: 100,
                        render: (h, p) => {
                            let s = p.row.cph ? p.row.cph : '-';
                            return h('div', s);
                        }
                    },
                    {
                        title: '服务年限',
                          tit:"SERVICE_YEAR",
                        align: 'center',
                        minWidth: 100,
                        key: 'fwnx',
                        filterMultiple: false,
                        filters: [
                              {
                                    label: '1年',
                                    tit:"ONE_YEAR",
                                    value: 1
                              },
                              {
                                    label: '2年',
                                    tit:"TWO_YEAR",
                                    value: 2
                              },
                              {
                                    label: '3年',
                                    tit:"THREE_YEAR",
                                    value: 3
                              },
                              {
                                    label: '5年',
                                    tit:"FIVE_YEAR",
                                    value: 5
                              },
                        ],
                        filterRemote:(val,k, row)=> {
                            this.param[k]=val[0]
                            this.param.pageNum = 1;
                            this.getPageData()
                            return true
                        },
                        render:(h,p)=>{
                            if(p.row.fwnx){
                                if(p.row.fwnx=='#'){
                                    return h('div','永久');
                                }
                                return h('div',p.row.fwnx+'年')
                            }else {
                                return h('div','-')
                            }
                        }
                    },
                    {
                        title: '在线状态',
                          tit:"ON_LINE_STATE_TAB",
                        align: 'center',
                        key: 'zxzt',
                        minWidth: 100,
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, this.lmdmDictionary, p.row.zxzt)
                            return h('div', {
                                style: {
                                    color: p.row.zxzt == 20 ? '#727272' : (p.row.zxzt == 10 ? '#ed4014' : '#279a3b')
                                }
                            }, val)
                        }
                    },
                    {
                        title: '激活状态',
                          tit:"ACTIVATED_STATE",
                        align: 'center',
                        key: 'jgzt',
                        minWidth: 100,
                        render: (h, p) => {
                            let val = this.dictUtil.getValByCode(this, 'ZDCLK1018', p.row.jhzt)
                            return h('div', {
                                style: {
                                    color: p.row.jhzt == "10" ? '#727272' : p.row.jhzt == '20'?'#279a3b':'red'
                                }
                            }, val)
                        }
                    },
                    {
                        title: '注册时间',
                          tit:"REGISTRATION_DATE",
                        align: 'center',
                        key: 'cjsj',
                        minWidth: 100,
                        render: (h, p) => {
                            return h('div', p.row.cjsj.substring(0, 10))
                        }
                    },
                    {
                        title: '激活时间',
                          tit:"ACTIVATED_DATE",
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
                          tit:"EXPIRATION_DATE",
                        align: 'center',
                        key: 'zdJssj',
                        minWidth: 120,
                        render: (h, p) => {
                            if (p.row.zdJssj == '') {
                                return h('div', '-')
                            } else if(p.row.zdJssj=='#'){
                                return h('div', '永久')
                            } else {
                                return h('div', p.row.zdJssj.substring(0, 10))
                            }
                        }
                    },
                    {
                        title: '制造商',
                          tit:"MANUFACTURER",
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
                          tit:"OPERATION",
                        align: 'center',
                        type: 'action',
                        width: 120,
                        fixed: 'right',
                        render: (h, params) => {
                            return h('div', [
                                h('Tooltip',{
                                    props:{content:this.$t("SERVICE_RENEWAL"),placement:'left',transfer:true},
                                    style:{marginRight:'8px'}
                                },[
                                    h('Button',{
                                        props:{shape:"circle",type:'info',size:'small',disabled:params.row.fwnx=='#'?true:false},
                                        on:{
                                            click:()=>{
                                                this.choosedRow = params.row
                                                this.componentName = 'zdxq'
                                            }
                                        }
                                    },'续    ')
                                ]),
                                h('Tooltip',
                                    {
                                        props: {
                                            transfer:true,
                                            placement: 'top',
                                            content: this.$t("TERMINAL_DEL"),
                                        },
                                    },
                                    [
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
                                    ]
                                ),
                            ]);
                        }
                    },
                ],
                tableData: [],
                //form表单
                formTop: {},
                //select
                cityList: [],
                //收索
                param: {
                    orderBy: 'zdbh asc',
                    mcLike: '',
                    pageNum: 1,
                    pageSize: 15,
                    zdLx:'20',//终端类型  字典：ZDCLK1017
                },
                Dictionary: [],
                lmdmDictionary: 'ZDCLK0032',//在线状态
                ztDictionary: [],
                ztlmdmDictionary: 'ZDCLK0031'//设备状态
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '系统管理',
            }, {
                title: 'OBD管理',
            }]),
                this.tabHeight = this.getWindowHeight() - 300
            this.getPageData()
            this.getLXDic()
        },
        methods: {
            batchUpdate() {
                this.choosedRow = null;
                this.updateMode = 'batch'
                this.componentName = 'update';
            },
            getLXDic() {
                this.Dictionary = this.dictUtil.getByCode(this, this.lmdmDictionary);
                this.ztDictionary = this.dictUtil.getByCode(this, this.ztlmdmDictionary);
            },
            getPageData() {
                this.pageTotal = 0;
                this.tableData = [];
                this.$http.get(this.apis.ZDGL.QUERY, {params: this.param}).then((res) => {
                    this.SpinShow = false;
                    if (res.code === 200) {
                        this.tableData = res.page.list;
                        this.pageTotal = res.page.total;
                        log('数据结构', this.tableData)
                    }
                })
            },
            pageChange(e) {
                this.param.pageNum = e;
                this.getPageData();
            },
            //新增数据
            AddMess() {
                this.componentName = 'formData'
                this.choosedRow = null;
            },
            //删除数据
            listDele(r) {
                this.util.del(this, this.apis.ZDGL.DELE, [r.zdbh])
            },
        }
    }
</script>
