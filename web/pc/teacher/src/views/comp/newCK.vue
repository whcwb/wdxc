<!--
换乘车人
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
      background-color: #fff;
      height: 0.65rem;
      line-height: 0.65rem;
      border-bottom: solid 1px #C8C8C8;
      color:#8f8f8f;
      .name{
        margin: 0 0.4rem 0 0.2rem;
        min-width: 1.5rem;
        i{
          color: #00bdff;
        }
      }
      .phone{
        i{
          color: #8c00ff;
        }
      }
    }
  }
</style>
<template>
  <div class="newck box">
    <div class="box-row" style="height: 0.68rem;margin: 0.2rem 0">
      <div @click="close"
           style="color: #838383;line-height: 0.68rem;padding-left: 0.2rem;">
        <i class="iconfont icon-left"></i>
      </div>
      <div class="body-O" style="text-align: center;line-height: 0.7rem">
          换乘人信息
      </div>
      <div style="color: #838383;line-height: 0.68rem;padding-right: 0.2rem;width: 30px">
        <!--<i class="iconfont icon-queren"></i>-->
      </div>
    </div>
    <div style="border-top: solid 2px #c1c1c1;padding: 0 0.2rem">
      <div class="box-row">
        <div style="line-height: 1rem;margin: 0 0.05rem;color: #00bdff;">
          <i class="iconfont icon-wo" style="font-size: 0.4rem"></i>
        </div>
        <div class="body-O" style="padding-right: 0.5rem">
            <md-input-item
              ref="name"
              placeholder="乘客姓名"
              v-model="Nname.ck"
              clearable
            ></md-input-item>
        </div>
        <div style="line-height: 1rem;margin: 0 0.05rem;color: #8c00ff">
          <i class="iconfont icon-dianhua" style="font-size: 0.4rem"></i>
        </div>
        <div class="body-O">
          <md-input-item
            ref="id"
            placeholder="联系电话"
            v-model="Nname.cklxdh"
            type="phone"
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
          <i class="iconfont icon-wo"></i>
          {{item.ck}}
        </div>
        <div class="phone">
          <i class="iconfont icon-dianhua"></i>
          {{item.cklxdh}}
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
              ck:'',
              cklxdh:''
            },
            userList:[
              // {
              //   ck:'',
              //   cklxdh:''
              // }
            ]
          }
        },
        created(){
          if(this.local.get('hisUserList')){
            this.userList = this.local.get('hisUserList')
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
          getlocal(){

          },
          setLocal(mes){
            this.local.set('hisUserList',mes)
          },
          mesForeach(mess){
            var v = this
            let al = false
            if(v.userList==0){
              al=true
            }
            v.userList.forEach((item,index)=>{
              if(item.cklxdh!=mess.cklxdh&&item.ck!=mess.ck){
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
            if(v.Nname.ck==''&&v.Nname.cklxdh!=''){
              v.tos('换乘人姓名不能为空')
            }else if(v.Nname.ck!=''&&v.Nname.cklxdh==''){
              v.tos('换乘人电话不能为空')
            }else {
              v.$emit('getNck',v.Nname)
              v.mesForeach(v.Nname)
              v.close()
            }

          },
          getUsermes(it){
            this.Nname = it
            this.$emit('getNck',it)
            this.close()
          }
        }

    }
</script>

<style scoped>

</style>
