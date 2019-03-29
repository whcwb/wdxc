<style lang="less">
  @import "../../styles/box.less";
  @import "./list.less";
</style>
<template>
      <div class="pagelist box" style="font-size: 16px">
          <div class="header" >
              <div class="box-row">
                <div class="titLeft" @click="back()">
                  <i class="iconfont icon-left"></i>
                </div>
                <div class="titCenter body-O" style="text-align: center;">
                      历史订单
                </div>
                <div class="titLeft" :class="anima" @click="Nfive">
                  <i class="iconfont icon-msnui-refresh-line"></i>
                </div>
              </div>
          </div>
          <div>
              <md-tab-bar
                @indexChanged="tabbarC"
                :titles="titles"
                :default-index="barNum"
              ></md-tab-bar>
          </div>
          <div class="body DDlist" style="background-color: #e8e8e8">
              <div class="DDitem" v-if="mesliat.length==0" style="text-align: center">
                  无数据……
              </div>
              <div class="DDitem"
                   v-else
                   v-for="item in mesliat">
                  <div class="tit box-row" v-if="item.sjxm!=''">
                        <div class="body-O" style="color: #3b3b3b;font-weight: 600;font-size: 14px;">
                          {{item.sjxm}}-{{item.cph}}
                        </div>
                        <div style="position: relative"
                             @click="fiveStar(item)" v-if="item.ddzt==20||item.ddzt==30">
                          已完成
                          <i class="iconfont icon-right"></i>
                          <div style="position: absolute;
                                      top: 45px;
                                      right: 5px;
                                      text-align: center;
                                      border: solid 2px #ff8a00;
                                      color: #ff8a00;
                                      width: 65px;
                                      border-radius: 9px;
                                      padding: 4px;">
                            <div>
                                合计
                            </div>
                            <div>
                              {{item.zj | zj}}元
                            </div>
                          </div>
                        </div>
                        <div style="padding-right:18px" v-else>
                          待出发
                        </div>
                  </div>
                  <div class="tit box-row" v-else>
                    <div class="body-O">
                      {{item.ddzt | ddztCH}}
                    </div>
                  </div>


                  <div style="margin: 8px 0;">
                    <i class="iconfont icon-shijian"
                        style="font-size: 14px"
                    ></i>
                    <span style="font-size: 14px;font-weight: 600">
                    {{item.yysj}}
                    </span>
                  </div>
                  <div style="margin: 6px 0">
                    <i class="iconfont icon-dian1"
                        style="color: #00a854;font-size: 14px"
                    ></i>
                    <span style="font-size: 14px">
                      {{item.hcdz}}
                    </span>
                  </div>
                  <div>
                    <i class="iconfont icon-dian1"
                       style="color: #ff8f00;font-size: 14px"
                    ></i>
                    <span style="font-size: 14px">
                      {{item.mdd}}
                    </span>
                  </div>
                  <div style="font-size: 14px" v-show="barNum==4">
                    驳回原因:{{item.bhyy}}
                  </div>
              </div>
          </div>
      </div>
</template>

<script>
    import {TabBar,Toast} from 'mand-mobile'
    export default {
        name: "",
        components:{
          [TabBar.name]: TabBar,
        },
      filters:{
        zj(val){
          if(val=='')return 0
          return val
        },
        ddztCH(val){
          switch (val){
            case '10':
              return '待审核...'
            default:
              return val
          }

        }
      },
        data(){
          return{
            anima:'',
            barNum:this.$store.state.app.barNum,
            titles: ['待出发','待派单', '待审核','历史行程','驳回'],
            mesliat:[]
          }
        },
        created(){
          if (this.cok.get('result')) {

          }else{
            this.$router.push({
              name:'login'
            })
          }
          this.getList(this.barNum)
        },
        methods:{
          Nfive(){
            this.getList(this.barNum)
            Toast({
              content: '数据更新中……',
              icon: 'spinner',
            })
          },
          tabbarC(n,o){//tab切换
            this.barNum = n
            this.$store.commit('barCh',n)
            this.getList(n)
          },
          starNum(val){
            alert(val)
          },
          dicBarnum(val){
            if(val==0)return '13'//待出发
            else if(val==1)return '11'//待指派

            else if(val==2)return '10'//待审核

            else if(val==3)return '20,30'//历史行程

            else if(val==4)return '12'//行程驳回

          },
          back(){
            this.$router.push({
              name:'Home'
            })
          },
          fiveStar(item){
            this.$router.push({
                name:'evaluate'
            })
            this.$store.commit('pjMessCh',item)
          },
          getList(num){
            let tp = this.dicBarnum(num)
            var v = this
            v.$http.post(this.apis.DDLIST.QUERTY,{'ddztIn':tp}).then((res) =>{
                if (res.code==200) {
                  this.mesliat = res.page.list
                }
            }).catch((error)=>{

            })
          },
        }
    }
</script>

<style scoped>

</style>
