<style lang="less">
      @import '../../../../../styles/common.less';
</style>
<style type="text/css">

</style>
<template>
      <div>
            <Modal v-model="showModal" width='900' :closable='false'
                   :mask-closable="false" :title='operate+$t("FUNCTION_")'>
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
                                                       :placeholder='$t("PLEASE_ENTER")+i.label'></Input>
                                          </FormItem>
                                    </Col>
                                    <Col span="12">
                                          <FormItem :label='$t("STATUS")'>
                                                <Select filterable clearable v-model="formItem.zt"
                                                        :placeholder='$t("STATUS_TAB")'>
                                                      <Option v-for='(item,index) in Dictionary' :value="item.key">
                                                            {{item.val}}
                                                      </Option>
                                                </Select>
                                          </FormItem>
                                    </Col>
                                    <Col span="12">
                                          <FormItem :label='$t("ORDER")'>
                                                <!--<Input type="number" v-model="formItem.px" placeholder="请填写排序..."></Input>-->
                                                <input class="input" type="number" min="0" v-model="formItem.px"
                                                       :placeholder='$t("ORDER_ENTER")'/>
                                          </FormItem>
                                    </Col>
                              </Row>
                        </Form>
                  </div>
                  <div slot='footer'>
                        <Button type="default" @click="v.util.closeDialog(v)" style="color: #949494">{{$t("CANCEL")}}</Button>
                        <Button type="primary" @click="v.util.save(v)">{{$t("DETERMINE")}}</Button>
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
                operate: this.$t("CREATE"),
                showModal: true,
                readonly: false,
                formItem: {
                    px: 1,
                    zt: '00'
                },
                formInputs: [
                    {label: this.$t("FUNCTION_NAME_TAB"), prop: 'gnmc', required: true, readonly: false},
                    {label: this.$t("FUNCTION_CODE"), prop: 'gndm', required: true, readonly: false},
                    {label: this.$t("SURVICE_CODE_TAB"), prop: 'fwdm', required: true, readonly: false},
                    {label: this.$t("URL"), prop: 'url', required: true, readonly: false},
                    {label: this.$t("ICON_TAB"), prop: 'tb', required: true, readonly: false},
                    {label: this.$t("PARENT_NODE"), prop: 'fjd', readonly: false},
                    {label: this.$t("LIVE_LINK"), prop: 'tzdz', required: true, readonly: false},
                    {label: this.$t("API_PREFIX"), prop: 'apiQz', required: true, readonly: false},
                    {label: this.$t("API_SUFFIX"), prop: 'apiHz', readonly: false},
                    {label: this.$t("COMMONT"), prop: 'bz', readonly: false},
                ],
                ruleInline: {
                    px: [
                        {required: true, message: this.$t("ERROR_ALERM"), trigger: 'blur'},
                        {min: 0, message: this.$t("ERROR_ALERM"), trigger: 'blur'}
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
            if (this.v.operate == this.$t("EDIT")) {
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
