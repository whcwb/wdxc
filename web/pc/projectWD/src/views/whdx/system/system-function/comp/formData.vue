<style lang="less">
      @import '../../../../../styles/common.less';
</style>
<style type="text/css">

</style>
<template>
      <div>
            <Modal v-model="showModal" width='900' :closable='false'
                   :mask-closable="false" :title="operate+'功能'">
                  <div style="overflow: auto;height: 500px;">
                        <Form
                                ref="form"
                                :model="formItem"
                                :rules="ruleInline"
                                :label-width="100"
                                :styles="{top: '20px'}">
                              <Row>
                                    <Col span="12" v-for="i in formInputs">
                                          <FormItem :prop='i.prop' :label='i.label'>
                                                <Input type="text" v-model="formItem[i.prop]"
                                                       :readonly="i.readonly"
                                                       :placeholder="'请填写'+i.label+'...'"></Input>
                                          </FormItem>
                                    </Col>
                                    <Col span="12">
                                          <FormItem label='状态'>
                                                <Select filterable clearable v-model="formItem.zt"
                                                        placeholder="请填选择状态...">
                                                      <Option v-for='(item,index) in Dictionary' :value="item.key">
                                                            {{item.val}}
                                                      </Option>
                                                </Select>
                                          </FormItem>
                                    </Col>
                                    <Col span="12">
                                          <FormItem label='排序'>
                                                <!--<Input type="number" v-model="formItem.px" placeholder="请填写排序..."></Input>-->
                                                <input class="input" type="number" min="0" v-model="formItem.px"
                                                       placeholder="请填写排序..."/>
                                          </FormItem>
                                    </Col>
                              </Row>
                        </Form>
                  </div>
                  <div slot='footer'>
                        <Button type="default" @click="v.util.closeDialog(v)" style="color: #949494">取消</Button>
                        <Button type="primary" @click="v.util.save(v)">确定</Button>
                  </div>
            </Modal>
      </div>
</template>

<script>
    export default {
        name: '',
        data() {
            return {
                v: this,
                apiRoot: this.apis.FUNCTION,
                operate: '新建',
                showModal: true,
                readonly: false,
                formItem: {
                    px: 1,
                    zt: '00'
                },
                formInputs: [
                    {label: '功能名称', prop: 'gnmc', required: true, readonly: false},
                    {label: '功能代码', prop: 'gndm', required: true, readonly: false},
                    {label: '服务代码', prop: 'fwdm', required: true, readonly: false},
                    {label: 'URL', prop: 'url', required: true, readonly: false},
                    {label: '图标', prop: 'tb', required: true, readonly: false},
                    {label: '父节点', prop: 'fjd', readonly: false},
                    {label: '跳转地址', prop: 'tzdz', required: true, readonly: false},
                    {label: 'API 前缀', prop: 'apiQz', required: true, readonly: false},
                    {label: 'API 后缀', prop: 'apiHz', readonly: false},
                    {label: '备注信息', prop: 'bz', readonly: false},
                ],
                ruleInline: {
                    px: [
                        {required: true, message: '请将信息填写完整', trigger: 'blur'},
                        {min: 0, message: '请将信息填写完整', trigger: 'blur'}
                    ],
                }
            }
        },
        props: {
            Dictionary: {
                type: Array,
                default: []
            }
        },
        created() {
            this.util.initFormModal(this);
            if (this.v.operate == '编辑') {
                // this.formInputs[1].readonly = true;
            }
        },
        methods: {
            beforeSave() {
                delete this.formItem.children;
            },
        }
    }
</script>

<style>

</style>
