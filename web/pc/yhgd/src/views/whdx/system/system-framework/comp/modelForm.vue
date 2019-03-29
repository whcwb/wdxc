<style lang="less">
      @import '../../../../../styles/common.less';
</style>
<template>
      <div>
            <Modal v-model="showModal" width='900' :closable='mesF'
                   :mask-closable="mesF"
                   :title="operate+'机构'">
                  <div style="height:600px;overflow: scroll;">
                        <Form :rules="ruleInline" ref="formItem" :model="formItem">
                              <Row>
                                    <Col span="10">
                                          <FormItem label='机构名称' prop="jgmc">
                                                <Input type="text" v-model="formItem.jgmc"
                                                       placeholder="请填写机构名称"></Input>
                                          </FormItem>
                                          <FormItem label='机构负责人姓名' prop="glyxm">
                                                <Input type="text" v-model="formItem.glyxm"
                                                       placeholder="请填写机构负责人姓名"></Input>
                                          </FormItem>
                                          <FormItem label='负责人联系电话' prop="lxdh">
                                                <Input type="text" v-model="formItem.lxdh"
                                                       placeholder="请填写机构负责人联系电话"></Input>
                                          </FormItem>
                                          <Row>
                                                <Col span="8">
                                                      <FormItem label='接口授权状态'>
                                                            <i-switch v-model="formItem.jkSq"
                                                                      true-value="00" false-value="10"
                                                                      size="large">
                                                                  <span slot="open">授权</span>
                                                                  <span slot="close">停用</span>
                                                            </i-switch>
                                                      </FormItem>
                                                </Col>
                                                <Col span="16" v-if="formItem.jkSq == '00'">
                                                      <FormItem label='接口ip地址' prop="jkIp">
                                                            <Input type="text" v-model="formItem.jkIp"
                                                                   placeholder="接口ip地址"></Input>
                                                      </FormItem>
                                                </Col>
                                          </Row>
                                          <FormItem label='地址所在'>
                                                <Input type="textarea" :autosize="{minRows: 3,maxRows: 3}"
                                                       v-model="formItem.dz"
                                                       placeholder="请填写机构地址所在"></Input>
                                          </FormItem>
                                          <FormItem label='机构说明' prop="jgsm">
                                                <Input type="text" v-model="formItem.jgsm"
                                                       placeholder="请填写机构说明"></Input>
                                          </FormItem>
                                          <FormItem label='终端数量'>
                                                <InputNumber style="width: 100%;" v-model="formItem.zdsl" :step="5"
                                                             :min="0" value='100'></InputNumber>
                                          </FormItem>
                                    </Col>
                                    <Col span="10" offset="4" style="height: 580px;overflow: scroll">
                                          <FormItem label='权限选择:'>
                                                <br>
                                                <menu-choose v-if="showTree" :data="orgTree"
                                                             :choosedData="hasPermissionCodes"
                                                             @treeChange="treeChange"></menu-choose>
                                                <!--<Tree :data="orgTree" show-checkbox multiple></Tree>-->
                                          </FormItem>
                                    </Col>
                              </Row>
                        </Form>
                  </div>
                  <div slot='footer'>
                        <Button type="default" @click="colse" style="color: #949494">取消</Button>
                        <Button type="primary" @click="save('formItem')">确定</Button>
                  </div>
            </Modal>
      </div>
</template>

<script>

    import menuChoose from '../../../components/menuChoose'

    export default {
        name: '',
        components: {menuChoose},
        data() {
            return {
                options: {
                    disabledDate(date) {
                        return date && date.valueOf() < Date.now() - 86400000;
                    }
                },
                showModal: true,
                choosedData: [],
                operate: '新建',
                mesF: false,
                edit: false,
                showTree: false,
                formItem: {
                    fjgdm: '',
                    gly: '',
                    jkSq:'10'
                },
                ruleInline: {
                    jgmc: [
                        {required: true, message: '请输入机构名称', trigger: 'blur'}
                    ],
                    glyxm: [
                        {required: true, message: '请输入负责人姓名', trigger: 'blur'}
                    ],
                    lxdh: [
                        {required: true, message: '请输入负责人联系电话', trigger: 'blur'}
                    ],
                    jkIp: [
                        {required: true, message: '请输入接口ip地址', trigger: 'blur'}
                    ]
                },
                userList: [],
                orgTree: [],
                choosedIds: [],
                mode: 'add',
                jdgm: '',
                parentCode: '',
                sonCode: '',
                newOrgCode: '',
                hasPermissionCodes: [],
            }
        },
        watch:{
            'formItem.jkSq':function (n,o) {
                console.log(n);
            }
        },
        created() {
        },
        mounted() {
            this.mode = this.$parent.mode;
            if (this.mode == 'md-create') {
                this.operate = '编辑'
                this.formItem = this.$parent.currentNode;
                this.parentCode = this.$parent.currentNode.fjgdm;
                this.sonCode = this.$parent.currentNode.jgdm;
                this.getHasPermissionCodes();
                this.getOrgPermissionTree();
            } else {
                this.operate = '新增'
                this.formItem.fjgdm = this.$parent.parentNode.jgdm;
                this.parentCode = this.$parent.parentNode.jgdm;

                this.getOrgPermissionTree();
            }
            this.getUserList();
        },
        methods: {
            treeChange(e) {
                this.choosedIds = e;
            },
            getOrgPermissionTree() {
                this.orgTree = [];
                this.$http.get(this.apis.FUNCTION.getPermissionTreeWithChecked + "?hideSystem=true&parentCode=" + this.parentCode + '&sonCode=' + this.sonCode).then((res) => {
                    if (res.code === 200) {
                        this.orgTree = res.result;
                        this.showTree = true;
                    }
                })
            },
            getHasPermissionCodes() {
                this.hasPermissionCodes = [];
                this.$http.get(this.apis.FUNCTION.GET_ORG_FUNCTIONS + "?jgdm=" + this.formItem.jgdm).then((res) => {
                    if (res.code === 200) {
                        if (res.result) {
                            for (let r of res.result) {
                                this.hasPermissionCodes.push(r.gndm);
                            }
                            this.choosedIds = this.hasPermissionCodes;
                        }
                        this.showTree = this.orgTree.length > 0;
                    }
                })
            },
            getUserPermissionTree() {
                this.orgTree = [];
                this.$http.get(this.apis.FUNCTION.GET_USER_PERMISSION_TREE).then((res) => {
                    if (res.code === 200) {
                        this.orgTree = res.result;
                    }
                })
            },
            setOrgPermission() {
                // this.choosedIds = [];
                // this.getChoosedIds(this.orgTree);
                let ids = '';
                for (let r of this.choosedIds) {
                    ids += r + ',';
                }
                let orgCode = this.formItem.jgdm;
                if (this.mode == 'add') {
                    orgCode = this.newOrgCode;
                }
                this.$http.post(this.apis.FUNCTION.SET_ORG_FUNCTIONS, {'jgdm': orgCode, 'gndms': ids}).then((res) => {
                    if (res.code === 200) {
                        this.$Message.success(res.message);
                    } else {
                        this.$Message.error(res.message);
                    }
                })
            },
            getUserList() {
                let userInfoJson = sessionStorage.getItem("userInfo");
                let userInfo = JSON.parse(userInfoJson);
                let jgdm = userInfo.jgdm;
                log('jgdm', jgdm);
                this.$http.get(this.apis.USER.QUERY, {params: {jgdmStartWith: jgdm}}).then((res) => {
                    if (res.code === 200 && res.page.list) {
                        this.userList = res.page.list;
                    }
                })
            },
            save(name) {
                var v = this
                v.SpinShow = true
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        let url = this.apis.FRAMEWORK.ADD;
                        if (this.mode == 'md-create') {
                            url = this.apis.FRAMEWORK.CHANGE;
                        }
                        delete this.formItem.children;
                        this.$http.post(url, this.formItem).then((res) => {
                            if (res.code === 200) {
                                this.newOrgCode = res.result;
                                this.setOrgPermission();
                            } else {
                                v.$Message.error(res.message);
                            }
                            v.$parent.componentName = ''
                            v.$parent.getTree()
                        })
                    } else {
                        v.SpinShow = false
                        v.$Message.warning('请认真填写相关信息!');
                    }
                })
            },
            colse() {
                var v = this
                v.$parent.componentName = ''
            }
        }
    }
</script>

<style>

</style>
