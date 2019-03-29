<template>
  <div class="box_col trajectorySty" style="background-color: #efeff4">
    <div class="mapAppTitSty">
      <div class="tit box_row">
        <!--<div class="box_row_100 TITleft">-->
        <!--<i class="iconfont icon-jiantouarrowhead7"></i>-->
        <!--</div>-->
        <div class="box_row_100 TITMess">
          <!--消息-->
          <DatePicker id="DatePicker" :value="DatePickerVal" placement="top" :clearable="false" :editable="false"
                      format="yyyy-MM-dd" type="date" @on-change="PickerClick"
                      style="text-align: center"
          ></DatePicker>
        </div>
        <!--<div class="box_row_100 TITRight">-->
        <!--<i class="iconfont icon-shanchu"></i>-->
        <!--</div>-->
      </div>
    </div>
    <div style="padding:6px 12px;font-size: 14px;font-weight: 600;background-color: #fff;border-bottom: solid 1px #efeff4">
      {{cph}}行驶轨迹
    </div>
    <div class="box_col_auto">
      <!--:bottom-method="loadBottom"-->
      <mt-loadmore :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <div v-if="hisList.length == 0" style="height: 100%;position: relative">
          <div style="text-align: center;margin-top: 20%" @click="getHislist">
            <img src="static/img/wsj.png" alt="">
            <div style="font-size: 30px;font-weight: 600;color: #b3b3b3">
              暂无轨迹信息
            </div>
          </div>
        </div>
        <div class="hisList" v-for="(it,index) in hisList" :key="index" @click="goLineMess(it)">
          <div class="hisListTime box_row">
            <div class="box_col_100">
              {{it.kssj.substring(11,19)}}~{{it.jssj.substring(11,19)}}
            </div>
            <div>
              <i class="iconfont icon-jiantou"></i>
            </div>
          </div>
          <div class="box_row">
            <!--width / 2 * 倍数减一-->
            <div style="width: 130px;height:70px;position: relative">65*3
              <img style="position: absolute;left: -195px;top: -105px;transform: scale(0.25,0.25);"
                   :src=" 'http://api.map.baidu.com/staticimage?width=520&height=280&center='+it.centerlng+','+it.centerlat+'&zoom=11&markers='+it.ksjps+'|'+it.jsjps+'&markerStyles=-1,http://47.98.39.45:9092/icon/qd.png|-1,http://47.98.39.45:9092/icon/zd.png' "
              alt="">
            </div>
            <div class="box_row_100">
              <div class="box_col">
                <div class="box_col_100 codeMess">
                  <div class="box_row">
                    <img src="./s.png" alt="">
                    <div class="textHidden" style="width: 100px">{{it.startAddress}}</div>
                  </div>
                </div>
                <div class="box_col_100 codeMess">
                  <div class="box_row">
                    <img src="./z.png" alt="">
                    <div class="textHidden" style="width: 100px">{{it.endAddress}}</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="LC">
              {{it.distance}} <span>公里</span>
            </div>
          </div>
        </div>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        allLoaded: false,//是否开启上拉加载
        DatePickerVal: '',
        carMess: {},
        hisList: [],
        cph:'',
        params:{
          startTime:'',
          endTime:'',
          zdbh:''
        }
      }
    },
    computed:{
      locaCarMess:function(){
        return this.$store.state.carMess
      }
    },
    watch: {
      locaCarMess:function(n,o){
        console.log('车辆数据监听',n);
        this.carMess = n
        if(this.carMess.zdbh){
          this.params.zdbh = this.carMess.zdbh
          this.getHislist()
        }
      },
      DatePickerVal: function (n, o) {
        console.log(n);
      }
    },
    created() {
      if(localStorage.getItem('pickTime')){
        this.DatePickerVal = localStorage.getItem('pickTime')
        this.params.startTime = localStorage.getItem('pickTime')
        this.params.endTime = localStorage.getItem('pickTime')
      }else {
        this.DatePickerVal = new Date()
      }
    },
    mounted(){
      this.carMess = JSON.parse(localStorage.getItem('carMess'))

      this.params.zdbh = this.carMess.zdbh
      this.cph = this.carMess.cph
      this.getHislist()
      this.$nextTick(()=>{
        var v = this
        document.addEventListener( "plusready", function(){
          try {
            plus.key.addEventListener('backbutton',function(){
            },false);
          }catch (e) {}
        }, false );
      })
    },
    methods: {
      loadTop() {//下拉
        this.$refs.loadmore.onTopLoaded();
        this.getHislist()
        console.log('loadTop');
      },
      GetSeverMess(key,callBack){
        this.$http.post('app/user/getData',{key:key}).then(res=>{
          if(res.code == 200 && res.result){
            callBack && callBack(JSON.parse(res.result))
          }else {
            callBack && callBack({})
          }
        }).catch(err=>{})
      },
      SetSeverMess(key,val){
        this.$http.post('app/user/putData',{key:key,val:val}).then(res=>{

        }).catch(err=>{})

      },
      PickerClick(str, date) {
        localStorage.setItem('pickTime',str)
        this.params.startTime = str
        this.params.endTime = str
        this.getHislist()
        console.log(str);
        console.log(date);
      },
      getHislist() {
        var instanceSTAR = this.Toast( '加载中……');
        this.$http.get('/app/device/history', {params: this.params}).then(res => {
          if (res.code == 200 && res.result) {
            res.result.forEach((it, index) => {
              it.kslng = it.ksjps.split(',')[0]
              it.kslat = it.ksjps.split(',')[1]

              it.jslan = it.jsjps.split(',')[0]
              it.jslat = it.jsjps.split(',')[1]

              it.centerlng = (parseFloat(it.ksjps.split(',')[0])+parseFloat(it.jsjps.split(',')[0]))/2
              it.centerlat = (parseFloat(it.ksjps.split(',')[1])+parseFloat(it.jsjps.split(',')[1]))/2

              let a = parseInt(it.distance)/1000

              it.distance = a.toFixed(1)

            })
            this.hisList = res.result
            instanceSTAR.close();
            console.log('历史轨迹', res);
          }else {
            this.hisList = []
            // this.Toast(res.message)
          }
        }).catch(err => {
        })
      },
      goLineMess(item) {
        var v = this
        let params ={
          startTime: item.kssj,
          endTime: item.jssj,
          zdbh:v.carMess.zdbh
        }
        localStorage.setItem('params',JSON.stringify(params))
        // this.$router.push(
        //   {
        //     name: 'hisLine'
        //   }
        // )
        uni.navigateTo({
          url: '/pages/main/comp/hisLine?startTime='+item.kssj+'&endTime='+item.jssj+'&zdbh='+v.carMess.zdbh
        });
      }
    }
  }
</script>

<style lang="less">
  #DatePicker {
    input {
      text-align: center;
      font-size: 20px;
      font-weight: 600;
      background-color: #fff0;
      border: none;
      outline: none;
      color: #ffff;
      box-shadow: none;
    }
    .ivu-date-picker-header-label{
      font-size: 18px;
      color: #2b85e4;
    }
    span.ivu-date-picker-cells-cell {
      color: #828282 !important;
    }
  }

  .trajectorySty {
    .hisList {
      background-color: #fff;
      padding: 8px;
      margin-bottom: 10px;
      .hisListTime {
        padding: 3px 0 6px 0;
        font-size: 16px;
        color: #999999;
        border-bottom: solid 2px #efeff4;
        margin-bottom: 12px;
      }
      .LC {
        font-size: 22px;
        font-weight: 600;
        line-height: 70px;
        span {
          font-size: 16px;
        }
      }
      .codeMess {
        padding-left: 35px;
        position: relative;
        line-height: 25px;
        font-size: 16px;
        font-weight: 600;
        img {
          position: absolute;
          top: 50%;
          left: 0;
          transform: translate(0, -65%);
          width: 40px;
        }
        i {
          position: absolute;
          top: 50%;
          left: 0;
          transform: translate(0, -50%);
        }
      }
    }
  }
</style>
