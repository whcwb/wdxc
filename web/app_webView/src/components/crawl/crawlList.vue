<template>
  <div class="box_col" style="background-color: #ededed">
    <div class="titbox mapAppTitSty">
      <div class="tit box_row">
        <div class="box_row_100 TITleft" @click="goback">
          <i class="iconfont icon-jiantouarrowhead7"></i>
        </div>
        <div class="box_row_100 TITMess">
          电子围栏
        </div>
        <div class="box_row_100 TITRight" @click="addCrawl">
          新增围栏
        </div>
      </div>
    </div>
    <div class="box_col_auto EnclosureList">
      <div v-if="crawlLisr.length==0" style="height: 100%;position: relative">
        <div style="text-align: center;position: absolute;top: 22%;width: 100%">
          <img src="static/img/wcl.png" style="width: 80px" alt="">
          <div style="color: #999;font-size: 14px">
            暂无围栏信息,请点击右上角新增围栏信息
          </div>
        </div>
      </div>

      <swipe-cell v-else class="swipeCell" v-for="(item,index) in crawlLisr" :key="index">
        <div class="box_col content" slot="content" @click="goCrawl(item)">
          <div style="height:32px;line-height:32px;font-size: 14px;color: #000">
            <div v-if="item.cphs != ''">{{item.wlmc}} ( {{item.cphs}} )</div>
            <div v-else>(未添加车辆)</div>
          </div>
          <div style="color:#b3b3b3;font-size: 15px">
            {{item.dlxxzb}}方圆{{item.wlfw}}米
          </div>
        </div>
        <button @click="remove(item,'swipeCell'+index)" style="background:#ed4014">删除</button>
      </swipe-cell>
    </div>
  </div>
</template>

<script>
  import { MessageBox } from 'mint-ui';
  export default {
    name: "crawlList",
    data(){
      return{
        crawlLisr:[],
        prams:{
          pageSize:50,
          pageNum:1
        },
        swipeCellID:''
      }
    },
    created(){
      this.getCrawlList()
    },
    mounted(){
      this.$nextTick(()=>{
        var v = this
        document.addEventListener( "plusready", function(){
          try {
            plus.key.addEventListener('backbutton',function(){
              v.goback()
            },false);
          }catch (e) {}
        }, false );
      })
    },
    methods:{
      getCrawlList(){
      // ,this.prams
        this.$http.post('/app/dzwl/pager',this.prams).then(res=>{
          if(res.code == 200 && res.page.list.length>0){
            res.page.list.forEach((it,index)=>{
                it.cphs = ''
              if(it.cls!==""){
                it.cls.forEach((val,key)=>{
                  console.log(val.cph);
                  if(key == it.cls.length-1){
                    it.cphs = it.cphs + val.cph
                  }else {
                    it.cphs = it.cphs + val.cph +'/'
                  }
                })
              }
              if(index == res.page.list.length-1){
                this.crawlLisr =res.page.list
              }
            })
          }else {
            this.crawlLisr=[]
          }
          console.log('====',res);
        }).catch(err=>{})
      },
      goback(){
        uni.switchTab({
          url: '/pages/user/user'
        });
      },
      addCrawl(){
        // this.$router.push({name: 'crawl'})
        this.$router.push({name: 'addCrawl'})

      },
      goCrawl(it){
        // this.$router.push({
        //   name: 'ShowCrawl',
        //   params:{
        //     Range:it.wlfw,//半径
        //     lng:it.ksjd,
        //     lat:it.kswd,
        //     wlmc:it.wlmc,//围栏名称
        //     address:it.dlxxzb
        //   }
        // })
        uni.navigateTo({
          url: '/pages/user/comp/showCrawl?Range='+it.wlfw+'&lng='+it.ksjd+'&lat='+it.kswd+'&wlmc='+it.wlmc+'&address='+it.dlxxzb
        });
      },
      remove(it,idIndex){
        var v= this

        MessageBox.confirm('是否删除围栏信息？').then(action => {
          if(action){
            this.$http.post('/app/dzwl/remove/'+it.id).then(res=>{
              if(res.code == 200){
                v.Toast({
                  message: '围栏删除成功！',
                  duration: 2000
                });
                this.crawlLisr = []

                v.getCrawlList()
              }
            }).catch(err=>{})
          }
        });
        return
      }
    }
  }
</script>

<style lang="less">
  .swipeCell{
    background-color: #fff;
    .cell-content{
      padding: 2px 0 2px 12px!important;
      .content{
        border-right: solid #ed4014 2px;
        height: 65px;
      }
    }
  }
.EnclosureList{
  .EncItem{
    height: 65px;
    background-color: #fff;
    border-bottom: 1px #ededed solid;
  }
}
</style>
