<template>
    <div>
        <Col v-for="i in formInputs" :span="i.span ? i.span : 12">
            <FormItem :prop='i.prop' :label='i.label'>
                <Input v-if="!i.type || i.type ==='text'" type="text" v-model="formItem[i.prop]" :placeholder="'请填写'+i.label+'...'" :readonly="readonly && i.readonly" :disabled="readonly && i.disabled"></Input>
                <DatePicker v-else-if="i.type == 'date'"  :value="formItem[i.prop]" type="date" placeholder="请选择日期" @on-change="(date)=>{formItem[i.prop] = date}" :disabled="readonly && i.disabled"></DatePicker>
                <DatePicker v-else-if="i.type == 'datetime'"  :value="formItem[i.prop]" type="datetime" placeholder="请选择日期" @on-change="(date)=>{formItem[i.prop] = date}" :disabled="readonly && i.disabled"></DatePicker>
                <Select v-else-if="i.type === 'dict'" filterable clearable  v-model="formItem[i.prop]" :placeholder="'请选择'+i.label+'...'">
                    <Option v-for = '(item,index) in v.dictUtil.getByCode(v,i.dict)' :value="item.key">{{item.val}}</Option>
                </Select>
                <Select v-else-if="i.type === 'foreignKey'" :disabled="i.disabled" filterable clearable  v-model="formItem[i.prop]" :placeholder="'请选择'+i.label+'...'">
                    <Option v-for = '(item,index) in foreignList[i.prop].items' :value="item.key">{{item.val}}</Option>
                </Select>
            </FormItem>
        </Col>
        <Col v-if="formInputGroups.length > 0" :span="24">
            <Row v-for="row in formInputGroups">
                <Col v-for="i in row" :span="i.span ? i.span : 12">
                    <FormItem :prop='i.prop' :label='i.label'>
                        <Input v-if="!i.type || i.type ==='text'" type="text" v-model="formItem[i.prop]" :placeholder="'请填写'+i.label+'...'" :readonly="readonly && i.readonly" :disabled="readonly && i.disabled"></Input>
                        <DatePicker v-else-if="i.type == 'date'"  :value="formItem[i.prop]" type="date" placeholder="请选择日期" @on-change="(date)=>{formItem[i.prop] = date}"></DatePicker>
                        <DatePicker v-else-if="i.type == 'datetime'"  :value="formItem[i.prop]" type="datetime" placeholder="请选择日期" @on-change="(date)=>{formItem[i.prop] = date}"></DatePicker>
                        <Select v-else-if="i.type === 'dict'" filterable clearable  v-model="formItem[i.prop]" :placeholder="'请选择'+i.label+'...'">
                            <Option v-for = '(item,index) in v.dictUtil.getByCode(v,i.dict)' :value="item.key">{{item.val}}</Option>
                        </Select>
                        <Select v-else-if="i.type === 'foreignKey'" :disabled="i.disabled" filterable clearable  v-model="formItem[i.prop]" :placeholder="'请选择'+i.label+'...'">
                            <Option v-for = '(item,index) in foreignList[i.prop].items' :value="item.key">{{item.val}}</Option>
                        </Select>
                    </FormItem>
                </Col>
            </Row>
        </Col>
    </div>
</template>

<script>
    export default {
        name: "formItems",
        props:{
            parent:{
              type:Object,
              default:function(){
                  return {};
              }
          }
        },
        data(){
          return{
              v:this,
              readonly:false,
              foreignList:[],
              formInputs:[],
              formInputGroups:[],
              formItem:{},
          }
        },
        watch:{
            'parent.formItem':{
                deep:true,
                handler(newVal, oldVal){
                    this.formItem = newVal;
                }
            }
        },
        created(){
            if (this.parent.foreignList){
                this.foreignList = this.parent.foreignList;
            }
            if (this.parent.formInputs){
                this.formInputs = this.parent.formInputs;
            }
            if (this.parent.formInputGroups){
                this.formInputGroups = this.parent.formInputGroups;
            }
            if (this.parent.formItem){
                this.formItem = this.parent.formItem;
            }
            if (this.parent.readonly){
                this.readonly = this.parent.readonly;
            }
        }
    }
</script>

<style scoped>

</style>
