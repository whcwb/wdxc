<style lang="less">
  @import '../../styles/box.less';
  @import "./Ycar.less";
  .md-picker-column-hook{
    text-align: center;
  }
</style>
<!--约车-->
<template>
    <div class="box">
      <head-tit tit="约车"></head-tit>
      <div class="body">
        <md-field>
          <md-input-item
            ref="input0"
            title="约车人"
            :readonly="true"
            v-model="form.ycr"
            :maxlength="5"
          ></md-input-item>
          <md-input-item
            ref="input0"
            title="乘车人"
            placeholder="请输入乘车人姓名"
            v-model="form.ck"
            :maxlength="5"
          ></md-input-item>
          <md-input-item
            ref="input0"
            type="phone"
            title="联系方式"
            v-model="form.cklxdh"
            placeholder="请输入您的联系方式"
            :maxlength="5"
          ></md-input-item>
          <md-input-item
            ref="input0"
            title="出发地"
            v-model="form.hcdz"
            placeholder="您的候车地点"
            :maxlength="5"
          ></md-input-item>
          <md-input-item
            ref="input0"
            title="目的地"
            v-model="form.mdd"
            placeholder="您的目的地"
            :maxlength="5"
          ></md-input-item>
          <md-input-item
            ref="input0"
            type="phone"
            title="乘车人数"
            v-model="Azws"
            placeholder="请输入您的乘车人数"
            :maxlength="5"
          ></md-input-item>
          <md-field-item
            name="name"
            title="出发时间"
            arrow="arrow-right"
            align="right"
            :value="datePickerValue"
            @click.native="isDatePickerShow = true">
          </md-field-item>
        </md-field>
        <md-date-picker
          ref="datePicker"
          v-model="isDatePickerShow"
          type="custom"
          title="选择出发时间"
          :text-render="textRender"
          :custom-types="['yyyy', 'MM','dd', 'hh','mm']"
          :default-date="currentDate"
          @change="onDatePickerChange"
          @confirm="onDatePickerConfirm"
        ></md-date-picker>
      </div>
      <div class="button" @click="yz(Azws)">
        <button>
          提    交
        </button>
      </div>
      <md-dialog
        title="窗口标题"
        :closable="true"
        v-model="basicDialog.open"
        :btns="basicDialog.btns"
      >
        人生的刺，就在这里，留恋着不肯快走的，偏是你所不留恋的东西。
      </md-dialog>
    </div>
</template>

<script>
  import headTit from "@/views/comp/headTit"
  import {InputItem, Field,DatePicker, FieldItem,Dialog} from 'mand-mobile'
    export default {
        name: "",
        components: {
          headTit,
          [InputItem.name]: InputItem,
          [DatePicker.name]: DatePicker,
          [Field.name]: Field,
          [FieldItem.name]: FieldItem,
          [Dialog.name]: Dialog,
        },
        data(){
          return{
            currentDate: new Date(),
            isDatePickerShow: false,
            datePickerValue: '',
            Azws:"",
            form:{
              ycr:'',
              ck:'',//乘车人
              cklxdh:'',//联系电话
              hcdz:'',//候车地址
              mdd:'',//目的地
              cllx:'10',//10小车20大车
              zws:'',//座位数
              yysj:''// 约车时间按
            },
            basicDialog: {
              open: false,
              btns: [
                {
                  text: '确认操作',
                  handler: this.onBasicConfirm,
                },
              ],
            }
          }
        },
        watch:{
          datePickerValue:function (n,o) {
            this.form.yysj = n
          }
        },
        created(){
          if (this.cok.get('result')) {
              this.ycr = JSON.parse(this.cok.get('result')).name
          }else{
            this.$router.push({
              name:'login'
            })
          }
        },
        mounted() {
        },
        methods: {
          yz(val){
            var v = this.form
            if((v.ck||v.cklxdh||v.gethcdz||v.mdd||v.zws||v.yysj)==''){
              alert('请将信息填写完成整')
            }else{
              this.zwsList(val)
            }
          },
          zwsList(val){
              let num = parseInt(val)
              if(num<=5){
                  this.form.zws = 5
              }else if(5<num&&num<=7){
                  this.form.zws = 7
              }else if(7<num&&num<=11){
                  this.form.zws = 11
              }else if(11<num){
                  this.form.zws = 11
                  this.zwsList(num-11)
              }
            this.addList()
          },
          addList(){//订单创建
            var v =this
            console.log('***********',this.form)
            v.$http.post(this.apis.DDSAVE.SAVE, this.form).then((res) =>{

            }).catch((error)=>{

            })
          },
          textRender() {
            const args = Array.prototype.slice.call(arguments)
            const typeFormat = args[0] // 类型
            if (typeFormat === 'yyyy') {
              return args[1].toString().substring(2,4) + '年'
            }
          },
          onDatePickerChange(columnIndex, itemIndex, value) {
          },
          onDatePickerConfirm(c) {
            console.log('-************',c)
            this.datePickerValue = ''
            let a = '-'
            c.forEach((item, index) => {
                if((item.type == "Date")) {
                  a = ' '
                }else if(item.type == "Minute"){
                  a = ':00'
                }else if(item.type == "Hour"){
                  a=':'
                }else {
                  a='-'
                }
              this.datePickerValue = this.datePickerValue+item.value+a
            })
          }
        }
    }
</script>

<style scoped>

</style>
