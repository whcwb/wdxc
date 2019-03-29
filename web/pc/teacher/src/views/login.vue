<style lang="less">
  @import './login.less';
  @import "../styles/box";
</style>
<template>
  <div class="login box">
      <!--<div class="backSty" @click="back">-->
        <!--<i class="iconfont icon-left"></i>-->
      <!--</div>-->
      <!--<div class="backCenter" @click="backCenter">-->
        <!--首页-->
      <!--</div>-->
      <div class="titIcon">
        <img src="../img/logo.png" alt="">
      </div>
      <div class="form body-D">
        <md-field>
          <md-input-item
            title="姓名"
            type="text"
            v-model="form.name"
            placeholder="请填写您的姓名"
            clearable
          ></md-input-item>
          <md-input-item
            title="工号"
            type="text"
            v-model="form.zjhm"
            placeholder="请输入您的工号"
            clearable
          ></md-input-item>
        </md-field>
        <div class="submit"  @click="login">
          <md-button>登录</md-button>
        </div>
      </div>
    <img style="width: 100%" src="../img/logbotImg.jpg" alt="">
  </div>
</template>

<script>
  import {InputItem, Field ,Button} from 'mand-mobile'

  export default {
    name: 'input-item-demo',
    /* DELETE */
    title: '标题隐藏输入框',
    /* DELETE */
    components: {
      [Button.name]: Button,
      [InputItem.name]: InputItem,
      [Field.name]: Field,
    },
    data(){
      return{
        form:{
          name:'宋林殊',
            zjhm:'32948289347289'
        }
      }
    },
    methods:{
      back(){
        this.$router.back()
      },
      backCenter(){
        this.$router.push({
            name:'Home'
        })
      },
      login(){
        var v = this
        v.$http.post(this.apis.LOGIN.QUERTY, this.form).then((res) =>{
            if (res.code == 200){
                this.cok.set('result',this.form)
                this.cok.set("token",res.result);
                this.$router.push({
                    name:'Home'
                })
            }
        }).catch((error)=>{

        })
      }
    }
  }

</script>
