<style lang="less">
  @import '../../styles/box.less';
  .feedbackHis{
    .hisList{
      border: solid 2px #d8d8d8;
      padding: 0.3rem 0.4rem;
      margin: 0.3rem 0;
      box-shadow: 2px 5px 5px #888888;
      position: relative;
      .close{
        i{
          color: #808080;
        }
        position: absolute;
        top: 0;
        right: 0;
      }
      .time{
        color: #808080;
      }
      .mrmes{
        .tit{
          color:#808080;
        }
        .mess{
          color:#808080;
          max-height: 65px;
          padding-left: 0.4rem
        }
      }
    }
  }
</style>
<template>
  <div class="box feedbackHis">
    <head-tit tit="反馈记录">
    </head-tit>
    <div>
      <md-tab-bar
        @indexChanged="tabbarC"
        :titles="titles"
      ></md-tab-bar>
    </div>
    <div class="body" style="padding: 8px">
        <div  v-if="listmess.length==0" style="text-align: center;line-height: 4rem">
          <div v-if="form.yjlx=='00'">
            无建议数据
          </div>
          <div v-else>
            无投诉数据
          </div>
        </div>
        <div class="hisList" v-for="item in listmess" v-else>
            <div class="close" @click="remove(item.yjId)">
              <i class="iconfont icon-guanbi"></i>
            </div>
            <div class="time">
                <i class="iconfont icon-shijian"></i>
                {{item.cjsj}}
            </div>
            <div class="fknr mrmes">
              <div class="tit">反馈内容：</div>
              <div class="mess">
                {{item.nr}}
              </div>
            </div>
            <div class="cljg mrmes">
              <div class="tit">处理结果：</div>
              <div class="mess" v-if="item.cljg!=''">
                {{item.cljg}}
              </div>
              <div class="mess" v-else>
                未处理
              </div>
            </div>
        </div>
    </div>
  </div>

</template>

<script>
    import {TabBar} from 'mand-mobile'
    import headTit from "@/views/comp/headTit"
    export default {
        name: "index",
        components: {
          headTit,
          [TabBar.name]: TabBar,
        },
      data(){
        return{
          titles: ['建议', '投诉'],
          form:{
            yjlx:'00'
          },
          listmess:[]
        }
      },
      created(){
        this.feedbackList()
      },
      methods:{
        tabbarC(n,o){//tab切换****10投诉  00改建/建议
          if(n==0){
            this.form.yjlx = '00'
          }else if(n==1){
            this.form.yjlx = '10'
          }
          this.feedbackList()
        },
        feedbackList(){
          var v = this
          v.$http.post(this.apis.FEEDBACK.LIST, {yhId:'2334232',yjlx:v.form.yjlx}).then((res) =>{
            console.log('*****',res)
            if(res.code ==200){
              v.listmess = res.page.list
            }
          }).catch((error)=>{

          })
        },
        remove(id){
          var v = this
          v.$http.post(this.apis.REMOVE.DELE, {'ids':[id]}).then((res) =>{
            console.log('*****',res)
            v.feedbackList()
          }).catch((error)=>{

          })
        }
      }
    }
</script>

<style scoped>

</style>
