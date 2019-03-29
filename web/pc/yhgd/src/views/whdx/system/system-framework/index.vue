<style lang="less">
      @import '../../../../styles/common.less';

      .framework {
            background: #fff;
            border: solid 1px #ededed;
            .frame-tree {
                  width: 170px;
                  border-right: solid 2px #d8d8d8;
            }
            .frame-mess {
                  padding-top: 8px;
                  position: relative;
                  .addTree {
                        position: absolute;
                        top: 50%;
                        left: 50%;
                        transform: translate(-50%, -50%);
                        border: solid 3px #989898;
                        border-radius: 25px;
                        width: 200px;
                        height: 200px;
                        text-align: center;
                        i {
                              font-size: 190px;
                        }
                  }
            }
      }
</style>
<template>
      <div class="boxbackborder box">
            <component :is="componentName"></component>
            <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
				<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
                  <div style="height: 45px;line-height: 45px;">
                        <div class="margin-top-10 box-row">
                              <div class="titmess">
                                    <span>厂商管理</span>
                              </div>
                        </div>
                  </div>
            </Row>
            <div class="box-row framework">
                  <div class='frame-tree'>
                        <div class="box">
                              <div class="tit" style="margin: 6px;">
                                    <Button type="primary" style="width: 100%;font-size: 14px;"
                                            @click="rootClick(RootTree)">
                                          <b>
                                                平台管理
                                          </b>
                                    </Button>
                              </div>
                              <div class="body" style="margin: 6px;"
                                   :style="RootTree.children.length==0 ? TreeListStyleC : TreeListStyleF">
                                    <Tree v-for="(item,index) in RootTree.children" :data="item"
                                          @on-select-change="treeClick"
                                          @on-toggle-expand="treeToggleClick"></Tree>
                              </div>
                        </div>
                  </div>
                  <div class="body-F frame-mess">

                        <div style="padding: 6px;">
                              <div class="box">
                                    <div class="tit"
                                         style="font-size: 16px;border-bottom:solid 2px #989898;height: 35px;">
                                          <b>
                                                {{treeMess.title}}
                                          </b>
                                          <Button v-if="treeMess.jgdm!='100'"
                                                  style="float: right;margin-right: 8px;" type="error"
                                                  shape="circle" icon="md-close" @click="del(treeMess)"></Button>
                                          <Button style="float: right;margin-right: 8px;" type="primary"
                                                  shape="circle" icon="md-add" @click="add()"></Button>
                                          <Button style="float: right;margin-right: 8px;" type="primary"
                                                  shape="circle" icon="md-menu"
                                                  @click="edit(treeMess)"></Button>
                                    </div>
                                    <div style="height: 120px;color: #999999">
                                          <Row type="flex" :getter="12" style="margin-top: 6px;font-size: 18px;font-weight: 600">
                                                <Col span="6">{{treeMess.title}}</Col>
                                                <Col span="6"><span>终端数量:</span> {{treeMess.zdsl}}</Col>
                                                <Col span="6"><span>接口授权码</span>
                                                      <i-switch v-model="treeMess.jkSq" :disabled="true"
                                                                true-value="00" false-value="10"
                                                                size="large">
                                                            <span slot="open">授权</span>
                                                            <span slot="close">停用</span>
                                                      </i-switch>
                                                </Col>
                                                <Col span="6"> <span>备注说明:</span> {{treeMess.jgsm}} </Col>
                                          </Row>
                                          <Row type="flex" :getter="12" style="margin-top: 6px;font-size: 18px;font-weight: 600">
                                                <Col span="6">
                                                      <span>负责人:</span>{{treeMess.glyxm}}
                                                </Col>
                                                <Col span="6">
                                                      <span>联系电话:</span>{{treeMess.lxdh}}
                                                </Col>
                                                <Col span="12">
                                                      <span>地址所在:</span>{{treeMess.dz}}
                                                </Col>
                                          </Row>
                                          <Row style="margin-top: 6px">
                                                <Button type="info" @click="componentName='addZd'">设备绑定</Button>
                                          </Row>
                                    </div>
                                    <div class="body">
                                          <!--<div v-if="treeMess.jgdm">-->
                                                <zd-tab ref="zdtab" :tabHeight="this.getWindowHeight() - 330-120"></zd-tab>
                                          <!--</div>-->
                                    </div>
                              </div>
                        </div>
                  </div>
            </div>
      </div>
</template>
<script>
    import mixins from '@/mixins'

    import treeList from './comp/treelist.vue'
    import modelForm from './comp/modelForm.vue'
    import FormItems from "../../components/formItems";

    import zdTab from './comp/zdTab'
    import addZd from './comp/formData'
    export default {
        name: '',
        components: {
            FormItems,
            treeList, modelForm,zdTab,addZd
        },
        mixins: [mixins],
        data() {
            return {
                jgmc: '',
                tabHeight: {
                    height: ''
                },
                TreeListStyleC: "text-align: center",
                TreeListStyleF: "text-align: left",
                componentName: '',
                choosedItem: null,
                mode: 'add',
                parentNode: '',
                currentNode: '',
                RootTree: {
                    title: '平台',
                    jgdm:'100',
                    children: [
                        [
                        ],
                    ]
                },
                treeMess: {
                	jgdm:''
                }
            }
        },
        watch:{
            'treeMess.jgdm':function (n,o) {
                console.log(n);
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '系统管理',
            }, {
                title: '厂商管理',
            }]),
                this.tabHeight.height = (this.getWindowHeight() - 240) + 'px'
        },
        mounted() {
            this.getTree();
        },
        methods: {
            getTree() {
                var v = this
                this.$http.get(this.apis.FRAMEWORK.getCurrentUserOrgTree).then((res) => {
                    if (res.code === 200 && res.result) {
                        v.RootTree.children = [res.result];
                        v.treeClick(v.RootTree.children[0])
                    }else{
                        v.RootTree.children = [];
                        this.rootClick()
                    }
                })
            },
            add() {
                this.parentNode = this.choosedItem;
                if (this.parentNode == null) {
                    this.$Message.error('请选择福机构');
                    return;
                }
                this.mode = 'add';
                this.componentName = 'modelForm';
            },
            edit(item) {
                this.mode = 'md-create';
                this.currentNode = item;
                this.componentName = 'modelForm';
            },
            del(item) {
                let ids = [];
                ids.push(item.jgdm);
                this.util.del(this, this.apis.FRAMEWORK.DELE, ids, () => {
                    this.getTree();
                });
            },
            treeClick(event) {
                if (event.length > 0) {
                    this.choosedItem = event[0];
                    this.treeMess = event[0]
//                  this.$refs['zdtab'].getTabList()
                }
            },
            treeToggleClick(event) {
                if (event.expand) {
                    this.treeClick([event])
                }
            },
            rootClick() {
                var v = this
                v.treeMess = {
                    title: v.RootTree.title,
                    jgdm: v.RootTree.jgdm,
                    // children: []
                }
                v.choosedItem = {
                    title: v.RootTree.title,
                    jgdm: v.RootTree.jgdm,
                }
//              this.$refs['zdtab'].getTabList()
                // for (var i = 0; i < v.RootTree.children.length; i++) {
                //     v.treeMess.children.push(v.RootTree.children[i][0])
                // }
            }
        }
    }
</script>
