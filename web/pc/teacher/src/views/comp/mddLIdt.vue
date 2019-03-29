<!--
目的地信息
-->
<style lang="less" scoped>
  @import "../../styles/box";
  .newck{
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #fff;
    .hislistSty{
      padding: 0.14rem 0;
      background-color: #fff;
      height: 0.65rem;
      line-height: 0.65rem;
      border-bottom: solid 1px #C8C8C8;
      color:#8f8f8f;
      .name{
        margin: 0 0.4rem 0 0.2rem;
        i{
          color: #ff9400;
        }
      }
      .phone{

      }
    }
  }
</style>
<template>
  <div class="newck box">
    <div class="box-row" style="height: 0.68rem;margin: 0.2rem 0">
      <div @click="close"
           style="color: #838383;line-height: 0.68rem;padding-left: 0.2rem;">
        <i class="iconfont icon-left" style="font-size: 20px"></i>
      </div>
      <div class="body-O" style="text-align: center;line-height: 0.7rem">
        约车出行
      </div>
      <div style="color: #838383;line-height: 0.68rem;padding-right: 0.2rem;width: 30px">
        <!--<i class="iconfont icon-queren" style="color: #36cb00;font-size: 0.5rem;"></i>-->
      </div>
    </div>
    <div style="border-top: solid 2px #c1c1c1;padding: 0 0.2rem">
      <div class="box-row">
        <div style="line-height: 1rem;margin: 0 0.05rem;color: #ff9400;">
          <i class="iconfont icon-xiazai20" style="font-size: 0.4rem"></i>
        </div>
        <div class="body-O" style="padding:0 0.8rem">
          <md-input-item
            ref="name"
            placeholder="你要去哪儿"
            v-model="Nname.mdd"
            clearable
          ></md-input-item>
        </div>
        <div style="line-height: 0.9rem" @click="okmes">
          <i class="iconfont icon-queren" style="color: #36cb00;font-size: 0.5rem;"></i>
        </div>
      </div>
    </div>
    <div class="body" style="border-top: solid 2px #c1c1c1;padding: 0.2rem 0.2rem;background-color: #eeeeee">
      <div class="box-row hislistSty"
           style="text-align: center"
          v-if="userList.length==0">
          无历史数据……
      </div>
      <div v-else class="box-row hislistSty"
           v-for="(item,index) in userList"
           @click="getUsermes(item)">
        <div class="name">
          <i class="iconfont icon-xiazai20"></i>
          {{item.mdd}}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {InputItem,Toast} from 'mand-mobile'
  export default {
    name: "newCK",
    components: {
      [InputItem.name]: InputItem,
    },
    data(){
      return{
        Nname:{
          mdd:'',
        },
        userList:[
        ]
      }
    },
    created(){
      if(this.local.get('hisMddList')){
        this.userList = this.local.get('hisMddList')
        console.log(this.userList)
      }
    },
    methods:{
      tos(txt){
        Toast({
          content: txt,
          icon: 'circle-alert',
        })
      },
      close(){
        this.$parent.compName = ''
      },
      setLocal(mes){
        this.local.set('hisMddList',mes)
      },
      mesForeach(mess){
        var v = this
        let al = false
        if(v.userList==0){
          al=true
        }
        v.userList.forEach((item,index)=>{
          if(item.mdd!=mess.mdd){
            al = true
          }
        })
        if(al){
          v.userList.push(mess)
          v.setLocal(v.userList)
        }
      },
      okmes(){
        var v = this
        if(v.mdd==''){
          v.tos('目的地不能为空！')
        }else {
          v.$emit('getMdd',v.Nname)
          v.mesForeach(v.Nname)
          v.close()
        }

      },
      getUsermes(it){
        this.mdd = it.mdd
        this.$emit('getMdd',it)
        this.close()
      }
    }

  }
</script>

<style scoped>

</style>
