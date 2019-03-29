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
      <div class="boxbackborder">
            <component :is="componentName"></component>
            <Card>
                  <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
				<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
                        <div style="height: 45px;line-height: 45px;">
                              <div class="margin-top-10 box-row">
                                    <div class="titmess">
                                          <span>组织机构管理</span>
                                    </div>
                              </div>
                        </div>
                  </Row>
                  <div class="box-row framework" :style="tabHeight">
                        <div class='frame-tree'>
                              <div class="box">
                                    <div class="tit" style="margin: 6px;">
                                          <Button type="primary" style="width: 100%;font-size: 14px;"
                                                  @click="rootClick(RootTree)">
                                                <b>
                                                      组织机构管理
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
                                                <!--<Button style="float: right;margin-right: 8px;" type="primary" shape="circle" icon="md-menu" @click="edit(treeMess)"></Button>-->
                                                <Button style="float: right;margin-right: 8px;" type="primary"
                                                        shape="circle" icon="md-add" @click="add()"></Button>
                                          </div>
                                          <div class="box-row-list" v-if="treeMess.children">
                                                <Card class="bodyC" v-for="(item,index) in treeMess.children">
                                                      <p slot="title">
                                                            <Icon type="ios-film-outline"></Icon>
                                                            {{item.title}}
                                                      </p>
                                                      <div slot="extra">
                                                            <!--<Button type="primary" shape="circle" icon="android-add" size="small" @click="add(item)"></Button>-->
                                                            <Button type="primary" shape="circle" icon="md-menu"
                                                                    size="small" @click="edit(item)"></Button>
                                                            <Button type="error" shape="circle" icon="md-close"
                                                                    size="small" @click="del(item)"></Button>
                                                      </div>
                                                      <div>
                                                            <div>
                                                                  负责人 ：{{item.glyxm}}
                                                            </div>
                                                            <div>
                                                                  机构编码 ：{{item.jgbm}}
                                                            </div>
                                                            <div>
                                                                  机构说明 ：{{item.jgsm}}
                                                            </div>
                                                            <div>
                                                                  机构创建时间 ：{{item.cjsj}}
                                                            </div>
                                                            <div>
                                                                  终端数量 ：{{item.zdsl}}
                                                            </div>
                                                            <div style="padding: 6px 0">
                                                                  终端数量加 ：
                                                                  <Button size="small" icon="md-add"
                                                                          type="success"></Button>
                                                            </div>
                                                            <div style="padding: 6px 0">
                                                                  终端数量减 ：
                                                                  <Button size="small" icon="md-remove"
                                                                          type="info"></Button>
                                                            </div>
                                                            <!--<div>-->
                                                            <!--有效期开始时间 ：{{item.kssj}}-->
                                                            <!--</div>-->
                                                            <!--<div>-->
                                                            <!--有效期结束时间 ：{{item.jssj}}-->
                                                            <!--</div>-->
                                                      </div>
                                                </Card>
                                          </div>
                                    </div>
                              </div>
                        </div>
                  </div>
            </Card>
      </div>
</template>
<script>
    import mixins from '@/mixins'

    import treeList from './comp/treelist.vue'
    import modelForm from './comp/modelForm.vue'
    import FormItems from "../../components/formItems";

    export default {
        name: '',
        components: {
            FormItems,
            treeList, modelForm
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
                    title: '武汉大学',
                    children: [
                        [
                            {
                                title: '校巴系统',
                                children: [
                                    {
                                        title: 'parent 1-2',
                                        expand: true,
                                        children: [
                                            {
                                                title: 'leaf 1-2-1'
                                            },
                                            {
                                                title: 'leaf 1-2-1'
                                            }
                                        ]
                                    }
                                ]
                            }
                        ],
                        [
                            {
                                title: '公务系统',
                                children: [
                                    {
                                        title: 'parent 1-2',
                                        expand: true,
                                        children: [
                                            {
                                                title: 'leaf 1-2-1'
                                            },
                                            {
                                                title: 'leaf 1-2-1'
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    ]
                },
                treeMess: {}
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '系统管理',
            }, {
                title: '组织机构',
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
                    if (res.code === 200) {
                        v.RootTree.children = [res.result];
                        v.treeClick(v.RootTree.children[0])
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
                } else {
                    this.choosedItem = null;
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
                    children: []
                }
                for (var i = 0; i < v.RootTree.children.length; i++) {
                    v.treeMess.children.push(v.RootTree.children[i][0])
                }
            }
        }
    }
</script>
