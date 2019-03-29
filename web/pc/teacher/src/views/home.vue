<style lang="less">
  @import '../styles/box.less';
  @import "./hone.less";
  .md-popup .md-popup-box{
    background:rgba(255,255,255,0);
  }
  #pickerTime{
    .bottom{
      .top{
        height:2rem!important;
      }
      .bottom{
        height: 1.3rem!important;
      }
    }
  }
</style>
<template>
  <div class="box bodylist">
      <div class="header">
          <div class="box-row">
              <div class="titLeft" @click="MyCenter()">
                  <i class="iconfont icon-wo"></i>
              </div>
              <div class="titCenter body-O" style="text-align: center;">
                  约车
              </div>
              <div class="titRight">
                  <!--<i class="iconfont icon-ybbfeedback"></i>-->
              </div>
          </div>
      </div>
      <div class="body" style="padding:0 0.5rem;background-color: #f4f5f7">
        <div class="formlist">
          <i class="iconfont icon-dian2"
             style="color: #46ac00"></i>
          <md-input-item
            style="border-bottom: solid #ff9e00 2px"
            v-model="form.hcdz"
            title=""
            placeholder="你在哪"
            align="left"
          ></md-input-item>
        </div>
        <div class="formlist" @click="compName='mddLIdt'">
          <i class="iconfont icon-dian2"
             style="color: #ffa817"></i>
          <md-input-item
            v-model="form.mdd"
            title=""
            placeholder="你要去哪"
            align="left"
          ></md-input-item>
        </div>
        <div class="carMes">
          <div class="time box-row">
              <div class="body-O" @click="isDatePickerShow = true">
                  <i class="iconfont icon-shijian"></i>
                  {{datePickerValue}}
              </div>
              <div class="body-O" @click="compName='newCk'">
                  <i class="iconfont icon-wo"></i>
                  {{lxfs}}
              </div>
          </div>
          <div class="box-row carZws">
            <div class="body-O carimg" v-for="(item,index) in carZws" @click="zws(item.val,index)">
              <img src="/static/car.jpg" alt="">
              <div :style="{color:item.color,fontWeight:'600'}">
                {{item.text}}
              </div>
            </div>
          </div>
        </div>

        <div v-if="form.hcdz!=''&&form.mdd!=''&&form.yysj!=''&&form.zws!=0" class="button" @click="addList">
          <button style="background: linear-gradient(to right, #f8b145 , #ff7f5c)">
            提    交
          </button>
        </div>
        <div v-else class="button" @click="tsi('请将信息填写完整')">
              <button style="background-color: #d8d8d8">
                提    交
              </button>
        </div>
      </div>
      <md-date-picker
        id="pickerTime"
        ref="datePicker"
        v-model="isDatePickerShow"
        type="custom"
        title="选择出发时间"
        :custom-types="['MM','dd', 'hh','mm']"
        :default-date="currentDate"
        @confirm="onDatePickerConfirm"
      ></md-date-picker>
      <transition name="bounce">
        <div class="trans" v-if="show">
          <div class="centerMess box">
              <div class="header">
                <div class="box-row">
                  <div @click="show=false"
                      style="color: #838383;line-height: 0.68rem"
                  >
                    <i class="iconfont icon-left"></i>
                  </div>
                  <div class="titCenter body-O" style="text-align:center;line-height: 0.68rem">
                    <i class="iconfont icon-wo"
                        style="font-size: 0.4rem;color: #949494;"
                    ></i>
                    <span style="font-size: 16px">
                      {{User}}
                    </span>
                  </div>
                </div>
              </div>
              <div class="body">
                <myCenter></myCenter>
              </div>
          </div>
          <div class="close" @click="MyCenter">

          </div>
        </div>
      </transition>
      <component
        :is="compName"
        @getMdd="getMdd"
        @getNck='getNck'></component>
  </div>
</template>
<script>
  import {DatePicker,FieldItem,InputItem,Landscape,Button,Toast} from 'mand-mobile'
  import  myCenter from './myCenter'
  import newCk from './comp/newCK'
  import mddLIdt from './comp/mddLIdt'
  export default {
    name: 'swiper-demo',
    components: {
      myCenter,
      [Button.name]: Button,
      [Landscape.name]: Landscape,
      [InputItem.name]: InputItem,
      [FieldItem.name]: FieldItem,
      [DatePicker.name]: DatePicker,
      [Button.name]: Button,
      newCk,mddLIdt
    },
    data() {
      return {
        compName:'',
        // save:false,
        form:{
          hcdz:'',//候车地址
          mdd:'',//目的地
          cllx:'10',//10小车20大车
          yysj:'',
          cklxdh:'',
          ck:'',
          zws:0
        },
        carZws:[
          {
            text:'5人座',
            val:5,
            color:'#000',
          },{
            text:'7人座',
            val:7,
            color:'#000',
          },{
            text:'11人座',
            val:11,
            color:'#000',
          }
        ],
        lxfsF:'',
        lxfs:'换乘车人',
        // showPic:false,
        currentDate: new Date(),
        isDatePickerShow:false,
        datePickerValue:'乘车时间',
        User:this.cok.get('result')? JSON.parse(this.cok.get('result')).name : '123',
        show:false,
        findInput:'',
        lineList:[],
        barNum:this.$store.state.app.barNum,
      }
    },
    computed:{
    },
    watch:{
      lxfsF:function(n,o) {
        if(n){
          this.lxfs = n
          this.form.cklxdh = n
        }else {
          this.lxfs = '换乘车人'
          this.form.lxfs = ''
        }
      }
    },
    created(){
      if(this.cok.get('result')){

      }else {
        this.$router.push({
          name:'login'
        })
      }
    },
    mounted() {
    },
    methods: {
      getMdd(it){
        this.form.mdd = it.mdd
      },
      getMdd(it){
        this.form.mdd = it.mdd
      },
      getNck(it){
        this.form.ck = it.ck
        this.lxfsF = it.ck
        this.form.cklxdh = it.cklxdh
      },
      zws(val,index){
          this.form.zws = val
          this.carZws.forEach((it,vl)=>{
              if(vl == index){
                it.color = '#1faa41'
              }else {
                it.color = '#000'
              }
          })
      },
      tsi(mes){//表单验证提示
        Toast({
          content: mes,
          icon: 'circle-alert'
        })
        // Toast.info(mes)
        // setTimeout(function () {
        //   Toast.hide()
        // },1800)
      },
      addList(){//订单创建
        var v =this
        console.log('***********',this.form)
        v.$http.post(this.apis.DDSAVE.SAVE, this.form).then((res) =>{
          if (res.code==200){
            Toast({
              content: '订单创建成功',
              icon: 'circle-alert'
            })
            setTimeout(function () {
              v.$router.push({
                name:'list'
              })
            },1000*0.8)
          }
        }).catch((error)=>{

        })
      },
      MyCenter(){//个人中心
        this.show = !this.show
      },
      feedback(){//信息反馈
        this.$router.push({
          name: 'feedBack'
        });
      },
      onDatePickerConfirm(c) {
        console.log('-************',c)
        var NowDate = new Date
        // debugger
        let a = '-'
        this.form.yysj = NowDate.getFullYear()+a
        this.datePickerValue = ''
        // this.form.yysj = ''
        c.forEach((item, index) => {
          if((item.type == "Month"&&item.value.toString().length==1)){
            item.value = '0'+item.value
          }else
            if((item.type == "Date")) {
            a = ' '
          }else if(item.type == "Minute"){
            a = ':00'
          }else if(item.type == "Hour"){
            a=':'
          }else {
            a='-'
          }
          this.form.yysj = this.form.yysj+item.value+a
          this.datePickerValue =this.datePickerValue + item.text
        })
      }
    },
  }

</script>
