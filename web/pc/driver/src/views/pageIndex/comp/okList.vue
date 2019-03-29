<style lang="less">
  @import "../../../styles/common.less";
  .ddlist{
    width:100%;
    font-size: 16px;
    padding: 6px 10px;
    background-color: #fff;
    .icon{
      /*background-color: #2b85e4;*/
      width: 75px;
      margin-right: 8px;
      text-align: center;
      position: relative;
      img{
        margin-top: 10px;
      }
      .iconBack{
        background-color: #375a7e;
        text-align: center;
        line-height: 50px;
        font-size:22px;
        font-weight: 700px;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%,-50%);
        height: 50px;
        width: 50px;
        border-radius: 25px;
        color: #fff;

      }
    }
    .lcmess{

    }
    .right{
      padding-right: 6px;
      line-height: 75px;
      font-size: 22px;
    }
  }
</style>
<template>
  <div class="box">
    <div class="body">
      <div v-if="mesList==undefined"style="text-align: center">
        <h1 style="color: #C8C8C8">
          暂无数据
        </h1>
      </div>
      <div v-else v-for="item in mesList" @click="lineMess(item)" style="margin: 6px 0">
        <div class="box-row ddlist">
          <div class="icon">
            <!--<img src="/static/tou.jpg" alt="">-->
            <div class="iconBack">
              {{item.ck | fist}}
            </div>
          </div>
          <div class="body-O lcmess">
            <div>
              <Icon type="ios-clock"></Icon>
              {{item.yysj}}
            </div>
            <div>
              <Icon type="ios-location" color="#15b740"></Icon>
              {{item.hcdz}}
            </div>
            <div>
              <Icon type="ios-location" color="#ff9b00"></Icon>
              {{item.mdd}}
            </div>
          </div>
          <div class="right">
            <Icon type="chevron-right"></Icon>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import Cookies from 'js-cookie';
  export default {
    name:'',
    components:{
    },
    filters:{
      fist(val){
        return val.substr(0,1)
      }
    },
    data () {
      return {
        mesList:[],
        mess:{}
      }
    },
    computed:{
      listTyp() {
        return this.$store.state.app.listType
      }
    },
    watch:{
      listTyp:function (n,o) {
          this.dqr()
      }
    },
    created(){
      if(Cookies.get('result')) {
          this.dqr()
      }else{
        this.$router.push({
          name:'login'
        })
      }
    },
    methods:{
      close(){
        this.compName = ''
        this.dqr()
      },
      dqr(){//待确认列表
        let listType = 2
        let tp = this.$store.state.app.listType
         if(tp==0){
           listType = 2
         }else if(tp==1){
           listType = 3
         }
        this.$http.post(this.apis.MESLIST.QUERTY,{'ddType':listType}).then((res)=>{
          if(res.code == 200){
            this.mesList = res.page.list
          }
        })
      },
      lineMess(mess){
        this.$router.push({
            name:'XCmess'
        })
        this.$store.commit('lineMess',mess)
      }
    }
  }
</script>
