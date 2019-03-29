<style lang="less">
      @import "../../../../styles/common.less";
</style>
<style type="text/css" scoped="">
      .carlist {
            padding: 3px 4px;
            background-color: #fff;
      }

      .carlistBor {
            border: solid 1px #bbb3b3;
      }

      #input div {
            float: left
      }

      #input button {
            float: left
      }
</style>
<template>
      <div class="box boxbackborder" style="padding: 5px 15px;">
            <component
                    :is="componentName"
                    :mess="mess"></component>
            <div class="tit" v-show="!RootShow">
                  <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
    			<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
                        <div style="height: 45px;line-height: 45px;">
                              <div class="margin-top-10 box-row">
                                    <div class="titmess">
                                          <span class="titmess">电子围栏</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <!--<DatePicker v-model="cjsjInRange" format="yyyy-MM-dd" type="daterange" placement="bottom-end" placeholder="请输时间" @on-keyup.enter="findMessList()" style="width: 220px"></DatePicker>-->
                                          <Input v-model="param.cphLike" placeholder="请输入车牌号" style="width: 200px"
                                                 @on-keyup.enter="findMessList()"></Input>
                                    </div>
                                    <div class="butevent">
                                          <Button type="success" @click="findList">
                                                <Icon type="md-search"></Icon>
                                                <!--查询-->
                                          </Button>
                                          <Button type="primary" @click="RootShow = !RootShow">
                                                <Icon type="md-add"></Icon>
                                          </Button>
                                    </div>
                              </div>
                        </div>
                  </Row>
            </div>
            <div class="body" v-show="!RootShow">
                  <Table ref="table"
                          :height="tabHeight"
                          :row-class-name="rowClassName"
                          :columns="columns10"
                          :data="data9"></Table>
                  <div v-if="SpinShow" style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 100;">
                        <Spin fix>
                              <Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
                              <div style="font-size: 30px;">数据加载中请稍后</div>
                        </Spin>
                  </div>
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
            </div>
            <div class="body" v-show="RootShow" style="height: 80%;">
                  <div class="box">
                        <div style="overflow: hidden;">
                              <h1 style="float: left;">新增电子围栏</h1>
                              <div id="input" style="float: right;padding: 5px;">
                                    <Form
                                            ref="param"
                                            :model="param"
                                            :rules="ruleInline"
                                            :label-width="100"
                                            :styles="{top: '20px'}">
                                          <FormItem label='围栏名称' prop="wlmc">
                                                <Input type="text"
                                                       v-model="param.wlmc"
                                                       size="large"
                                                       placeholder='请输入电子围栏名称'
                                                       style="width: 200px"></Input>
                                          </FormItem>
                                          <Button type="success"
                                                  size="large"
                                                  style="margin: 0 8px;"
                                                  @click="finish">完成
                                          </Button>
                                          <Button type="primary" size="large" @click="AddRali" style="color: #949494">
                                                取消
                                          </Button>
                                    </Form>
                              </div>
                        </div>
                        <div class="body">
                              <div class="box-row" style="height: 100%;">
                                    <div class="carlist carlistBor" style="width: 180px;height: 100%;">
                                          <div class="box">
                                                <div class="tit">
                                                      <Input value="" placeholder="请输入车辆信息..." size="small"
                                                             style="width: 100%"></Input>
                                                </div>
                                                <div class="body">
                                                      <Tree :data="data1" ref="tree"
                                                            show-checkbox
                                                            @on-check-change='checkClick'
                                                            @on-select-change='treeClick'></Tree>
                                                </div>
                                          </div>
                                    </div>
                                    <div class="body-F carlistBor" style="position: relative;height: 100%;">
                                          <my-map ref='maps' :mapDot="mapDot" @choosePoint="choosePoint"></my-map>
                                    </div>
                              </div>
                        </div>
                  </div>
            </div>
      </div>
</template>

<script>
    import myMap from '../../map/mapBK.vue'

    import mixins from '@/mixins'
    import formData from './comp/formData'
    import bkShow from '../vehicle-management/comp/BKshow'

    export default {
        name: '',
        mixins: [mixins],
        components: {
            myMap, formData, bkShow
        },
        data() {
            return {
                pageTotal: 1,
                page: {
                    pageNum: 1,
                    pageSize: 8
                },
                SpinShow: false,
                tabHeight: 220,
                mess: {},
                mapDot: [],
                points: [],
                RootShow: false,
                choosedRow: null,
                componentName: '',
                //收索
                cjsjInRange: [],
                param: {
                    cjsjInRange: '',
                    cphLike: '',
                    pageNum: 1,
                    pageSize: 8,
                    dlxxzb: '',
                    clIds: ''
                },
                ruleInline: {
                    // wlmc: [
                    //     { required: true, message: '请输入围栏名称', trigger: 'blur' }
                    // ],
                },
                fanceId: '',
                columns10: [
                    {
                        title: '序号',
                        type: 'index',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '车牌号',
                        align: 'center',
                        key: 'cph'
                    },
                    {
                        title: '围栏名称',
                        align: 'center',
                        key: 'wlmc'
                    },
                    {
                        title: '创建时间',
                        width: 180,
                        align: 'center',
                        key: 'cjsj'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 150,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {//电子围栏展示
                                    props: {
                                        type: 'primary',
                                        icon: 'ios-globe-outline',
                                        shape: 'circle',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.componentName = 'bkShow'
                                            this.mess = params.row
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
                                            this.listDele(params.row.id)
                                        }
                                    }
                                }),
                            ]);
                        }
                    }
                ],
                data9: [
                    {
                        FleetNumber: '鄂A12345',
                        FleetName: '公务车',
                        captainName: '公务车电子围栏0001',
                        Founder: '王铭',
                        time: '20170203 08:20:22'
                    }
                ],
                data1: [{
                    "children": [{
                        "mapCenMap": {},
                        "title": "111111"
                    }, {
                        "mapCenMap": {},
                        "title": "1212"
                    }, {
                        "mapCenMap": {},
                        "title": "121212"
                    }],
                    "expand": true,
                    "title": "小车"
                }, {
                    "children": [{
                        "mapCen": {
                            "lat": "31.3366798136",
                            "lng": "114.2353441913"
                        },
                        "title": "鄂A66771"
                    }, {
                        "mapCenMap": {
                            "lat": "31.2173317951",
                            "lng": "114.1443044286"
                        },
                        "title": "鄂A66773"
                    }, {
                        "mapCenMap": {},
                        "title": "鄂TEST"
                    }],
                    "expand": true,
                    "title": "校巴"
                }],
                carIds: ''
            };
        },
        computed: {},
        watch: {
            cjsjInRange: function (newQuestion, oldQuestion) {
                this.param.cjsjInRange = this.getdateParaD(newQuestion[0]) + ',' + this.getdateParaD(newQuestion[1])
            },
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '车辆管理',
            }, {
                title: '电子围栏',
            }])
            this.tabHeight = this.getWindowHeight() - 212
            this.getCarTree()
            this.findMessList();
        },
        methods: {
            //删除数据
            listDele(id) {
                this.util.del(this, this.apis.DZWL_CL.DELE, [id], () => {
                    this.findMessList();
                });
            },
            finish() {
                this.saveDzwl();
            },
            getCarTree() {
                this.$http.get(this.apis.CARTREE.QUERY).then((res) => {
                    this.data1 = res.result
                }).catch((error) => {
                    log('error', error)
                })
            },
            choosePoint(points) {
                this.param.dlxxzb = '';
                for (let r of points) {
                    this.param.dlxxzb += r.lng + "," + r.lat + ";";
                }
                log(this.param.dlxxzb);
            },
            //电子围栏
            AddRali() {
                this.RootShow = !this.RootShow
            },
            //树多选框
            checkClick(event) {
                var v = this
                v.mapDot = []
                if (event[0]) {
                    v.getLeaf([event[0]])
                } else if (event == undefined) {
                    v.getLeaf([])
                }
            },
            getLeaf(list) {
                let v = this;
                if (list.length > 0) {
                    for (let r of list) {
                        if (r.children) {
                            v.getLeaf(r.children)
                        } else {
                            v.mapDot.push(r);
                        }
                    }
                } else {
                    v.mapDot.push([]);
                }
            },
            getChoosedIds() {
                this.carIds = '';
                let nodes = this.$refs['tree'].getCheckedNodes();
                for (let r of nodes) {
                    this.carIds += r.clid + ",";
                }
            },
            save() {
                var v = this
                this.$refs['param'].validate((valid) => {
                    if (valid) {
                        this.getChoosedIds();
                        this.$http.post(this.apis.DZWL.setCarsDzwl, {
                            wlid: this.fanceId,
                            carIds: this.carIds
                        }).then((res) => {
                            if (res.code === 200) {
                                this.RootShow = !this.RootShow
                                this.findMessList();
                            } else {
                                this.$Message.error(res.message);
                            }
                            v.SpinShow = false;
                        })
                    } else {
                        v.$Message.error('请认真填写信息!');
                    }
                })
            },
            saveDzwl() {
                var v = this
                if (!v.param.wlmc || v.param.wlmc == '') {
                    v.$Message.error('请输入围栏名称');
                    return;
                }
                this.$http.post(this.apis.DZWL.ADD, v.param).then((res) => {
                    if (res.code === 200) {
                        this.fanceId = res.message;
                        this.save();
                    }
                    v.SpinShow = false;
                })
            },
            //树多点击事件
            treeClick(mess) {
                if (mess[0]) {
                    this.mapDot = mess
                }
            },
            changeTime(val) {
            },
            findList() {
                this.findMessList();
            },
            findMessList() {
                var v = this
                delete this.param.dlxxzb;
                delete this.param.wlmc;
                this.$http.get(this.apis.DZWL_CL.QUERY, {params: v.param}).then((res) => {
                    v.data9 = res.page.list
                    v.pageTotal = res.page.total
                    v.SpinShow = false;
                })
            },
            pageChange(event) {
                var v = this
                v.param.pageNum = event
                v.findMessList()
            },
        }
    };
</script>

<style>
</style>
