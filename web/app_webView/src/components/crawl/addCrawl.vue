<template>
  <div class="box_col" style="background-color: #efeff4">
    <div class="titbox mapAppTitSty">
      <div class="tit box_row">
        <div class="TITleft" style="width: 55px" @click="goback">
          <i class="iconfont icon-jiantouarrowhead7"></i>
        </div>
        <div class="box_row_100 TITMess" style="font-weight: 500">
          添加围栏车辆
        </div>
        <div class="TITRight" style="width: 55px">

        </div>
      </div>
    </div>
    <div class="box_col_auto">
      <!--:bottom-method="loadBottom"-->
      <mt-loadmore :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <mt-checklist
          v-model="carList"
          :options="RangeList">
        </mt-checklist>
      </mt-loadmore>
    </div>
    <div style="padding: 8px 12px;background-color: #f5f5f5;border-top: solid 2px #d9d9d9">
      <mt-button type="primary" style="width: 100%" @click="goCrawl">完 成</mt-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "addCrawl",
    data() {
      return {
        allLoaded: false,//是否开启上拉加载
        carList: [],
        RangeList: []
      }
    },
    watch:{
      carList:(n,o)=>{
        console.log(n);
      }
    },
    created() {
      this.getCarList()
    },
    methods: {
      getCarList() {//获取车辆列表
        var v = this
        this.$http.post('/app/device/InitClGps').then(res => {
          if (res.code == 200 && res.result) {
            res.result.forEach((it, index) => {
              it.value = it.clid==''?'-----':it.clid
              it.label = it.cph==''?'-----':it.cph
              if (index == res.result.length - 1) {
                v.RangeList = res.result
              }
            })
          }
        }).catch(err => {
        })
      },
      loadTop() {//下拉
        this.$refs.loadmore.onTopLoaded();
        this.getCarList()
        console.log('loadTop');
      },
      loadBottom() {
        this.allLoaded = true;
        this.$refs.loadmore.onBottomLoaded();
        this.allLoaded = false;
      },
      goback() {
        this.$router.back()
      },
      goCrawl(){
        if(this.carList.length == 0){
          this.Toast('请选择车辆');
          return
        }
        let param = {}
        param.carIds = this.carList.join(',')
        // param.wlid = this.$route.params.wlid
        // this.$router.push({name: 'crawl',params:{'carIds':param.carIds}})
        uni.navigateTo({
          url: '/pages/user/comp/addCrawl?carIds='+param.carIds
        });
      }
    }
  }
</script>

<style scoped>

</style>
