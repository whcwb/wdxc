<template>
  <div>
    <Row>
      <Col v-for="i in formInputs" v-if="i.title !=='序号' && i.title !== '操作'" :span="i.span ? i.span : 12"  class-name="colpad">
        <!--<FormItem :prop='i.key' :label='i.title'>-->
        <FormItem :label='i.title'>
          <Select v-if="i.dict" filterable clearable v-model="formItem[i.key]" :placeholder="'请选择'+i.title+'...'">
            <Option v-for='(item,index) in v.dictUtil.getByCode(v,i.dict)' :value="item.key">{{item.val}}</Option>
          </Select>
          <Input v-else-if="!i.type || i.type ==='text'" type="text" v-model="formItem[i.key]" clearable
                 :placeholder="'请填写'+i.title+'...'" :readonly="readonly && i.readonly"
                 :disabled="readonly && i.disabled">
          <span v-if="i.prepend" slot="prepend">{{i.prepend}}</span>
          <span v-if="i.append" slot="append">{{i.append}}</span>
          </Input>
          <InputNumber v-else-if="i.type ==='number'" v-model="formItem[i.key]"
                       :placeholder="'请填写'+i.title+'...'" :readonly="readonly && i.readonly"
                       :disabled="readonly && i.disabled"></InputNumber>
          <Input v-else-if="i.type ==='textarea'" type="textarea" v-model="formItem[i.key]" clearable
                 :placeholder="'请填写'+i.title+'...'" :readonly="readonly && i.readonly"
                 :disabled="readonly && i.disabled">
          <span v-if="i.prepend" slot="prepend">{{i.prepend}}</span>
          <span v-if="i.append" slot="append">{{i.append}}</span></Input>
          <DatePicker v-else-if="i.type === 'date'" :value="formItem[i.key]" type="date" placeholder="请选择日期"
                      :option="i.dateOptions != null ? i.dateOptions : {}"
                      style="width:100%" @on-change="(date)=>{formItem[i.key] = date}"
                      :disabled="readonly && i.disabled"></DatePicker>
          <DatePicker v-else-if="i.type === 'datetime'" :value="formItem[i.key]" type="datetime" placeholder="请选择日期"
                      style="width:100%" @on-change="(date)=>{formItem[i.key] = date}"
                      :disabled="readonly && i.disabled"></DatePicker>
          <Select v-else-if="i.type === 'foreignKey'" :disabled="i.disabled" filterable clearable
                  v-model="formItem[i.key]" :placeholder="'请选择'+i.title+'...'">
            <Option v-for='(item,index) in foreignList[i.key].items' :value="item.key">{{item.val}}</Option>
          </Select>
          <Input v-else-if="i.type === 'password'" type="password" v-model="formItem[i.key]" clearable
                 :placeholder="'请填写'+i.title+'...'" :readonly="readonly && i.readonly"
                 :disabled="readonly && i.disabled"></Input>

        </FormItem>
      </Col>
      <Col v-if="formInputGroups.length > 0" :span="24" class-name="colpad">
        <Row v-for="row in formInputGroups">
          <Col v-for="i in row" :span="i.span ? i.span : 12">
            <!--<FormItem :prop='i.key' :label='i.title'>-->
            <FormItem :label='i.title'>
              <Input v-if="!i.type || i.type ==='text'" type="text" v-model="formItem[i.key]" clearable
                     :placeholder="'请填写'+i.title+'...'" :readonly="readonly && i.readonly"
                     :disabled="readonly && i.disabled">
              <span v-if="i.prepend" slot="prepend">{{i.prepend}}</span>
              <span v-if="i.append" slot="append">{{i.append}}</span></Input>
              <DatePicker v-else-if="i.type == 'date'" :value="formItem[i.key]" type="date" placeholder="请选择日期"
                          style="width:100%" @on-change="(date)=>{formItem[i.key] = date}"></DatePicker>
              <DatePicker v-else-if="i.type == 'datetime'" :value="formItem[i.key]" type="datetime" placeholder="请选择日期"
                          style="width:100%" @on-change="(date)=>{formItem[i.key] = date}"></DatePicker>
              <Select v-else-if="i.type === 'dict'" filterable clearable v-model="formItem[i.key]"
                      :placeholder="'请选择'+i.title+'...'">
                <Option v-for='(item,index) in v.dictUtil.getByCode(v,i.dict)' :value="item.key">{{item.val}}</Option>
              </Select>
              <Select v-else-if="i.type === 'foreignKey'" :disabled="i.disabled" filterable clearable
                      v-model="formItem[i.key]" :placeholder="'请选择'+i.title+'...'">
                <Option v-for='(item,index) in foreignList[i.key].items' :value="item.key">{{item.val}}</Option>
              </Select>
             </FormItem>
          </Col>
        </Row>
      </Col>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "formItem",
    props: {
      parent: {
        type: Object,
        default: function () {
          return {};
        }
      },
      items: {
        type: Array,
        default: function () {
          return [];
        }
      },
    },
    data() {
      return {
        v: this,
        readonly: false,
        foreignList: [],
        formInputs: [],
        formInputGroups: [],
        formItem: {},
        width: '200px',
      }
    },
    watch: {
      'parent.formItem': {
        deep: true,
        handler(newVal, oldVal) {
          this.formItem = newVal;
        }
      }
    },
    created() {
      this.init();
    },
    methods: {
      init(){
        if (this.parent.foreignList) {
          this.foreignList = this.parent.foreignList;
        }
        if (this.items.length === 0 && this.parent.formInputs) {
          this.formInputs = this.parent.formInputs;
        } else if (this.items.length !== 0) {
          this.formInputs = this.items;
        }
        if (this.parent.formInputGroups) {
          this.formInputGroups = this.parent.formInputGroups;
        }
        if (this.parent.formItem) {
          this.formItem = this.parent.formItem;
        }
        if (this.parent.editMode) {
          this.readonly = this.parent.editMode;
        }
      }
    }
  }
</script>

<style scoped>

</style>
