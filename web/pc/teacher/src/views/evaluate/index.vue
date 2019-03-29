<style lang="less">
  @import "../../styles/box.less";
  @import "./eval.less";
</style>
<template>
  <!--<div class="box evaluate" :style="{backgroundImage:'url(http://api.map.baidu.com/staticimage/v2?ak=evDHwrRoILvlkrvaZEFiGp30&width=375&height=667&center=武汉&markers='+mess.mdd+'|'+mess.hcdz+'&zoom=10&markerStyles=-1,http://47.98.39.45:9092/icon/map_line_begin.png|-1,http://47.98.39.45:9092/icon/map_line_end.png)'}">-->

  <div class="box evaluate">
      <div id="mapDOt" class="body">
      </div>
      <div class="carMess">
          <div class="box-row tit">
              <div class="titIcon">
                <div style="background-color: #375a7e;
                            text-align: center;
                            line-height: 50px;
                            font-size:22px;
                            font-weight: 700px;
                            height: 50px;
                            width: 50px;
                            border-radius: 25px;
                            color: #fff;">
                  {{mess.sjxm | fist}}
                </div>
                <!--<img src="./img/drive.png" style="width: 1rem">-->
              </div>
              <div class="drivMess body-O">
                  <div class="drvName">
                    {{mess.sjxm}}
                  </div>
                  <div class="drivCar">
                    {{mess.cph}}-{{mess.zws}}座位
                  </div>
                  <div class="carZws">
                    {{mess.yysj}}
                  </div>
              </div>
              <div class="drivPhone">
                <a :href="'tel:'+mess.sjdh">
                  <i class="iconfont icon-dianhua1"></i>
                </a>
              </div>
          </div>
          <div class="boxCenter">
            <div class="box-row">
              <div class="body-r-1">
                <div class="lm">
                  里程
                </div>
                <div class="mess">
                  {{mess.lc | messCh}}
                  公里
                </div>
              </div>
              <div class="body-r-1">
                <div class="lm">
                  金额
                </div>
                <div class="mess">
                  {{mess.zj | messCh}}
                  元
                </div>
              </div>
            </div>
          </div>
          <div class="boxStar">
            <five-star @starNum="starNum" :num="pf" :bj="bj"></five-star>
          </div>
          <div class="button" style=" overflow: hidden">
            <button style="float: left;width: 45%" @click="close()">
              关闭
            </button>
            <button v-if="pfzt" style="float: right;width: 45%" @click="save()">
              提    交
            </button>
            <button v-else style="float: right;width: 45%;background-color: #d9d9d9">
              提    交
            </button>
          </div>
      </div>
    </div>
</template>

<script>
  import {Toast} from 'mand-mobile'

  import fiveStar from '@/views/comp/fiveStar'
    export default {
        name: "",
        components:{
          fiveStar
        },
      filters:{
        messCh(val){
          if(val=='')return '0'
          return val
        },
        fist(val){
          return val.substr(0,1)
        }
      },
        data(){
          return{
            map:'',
            mess:this.$store.state.app.pjMess,
            pf:0,
            pfzt:false,
            bj:true
          }
        },
        created(){
          if(this.cok.get('result')) {
          }else{
            this.$router.push({
              name:'login'
            })
          };
          console.log(this.mess)
          if(this.mess.pjdj==""){
            this.bj = true
          }else if(this.mess.pjdj==undefined){
            this.$router.back()
          }else {
            this.bj = false
            this.pf = this.mess.pjdj
          }
          console.log(this.mess)
        },
        mounted(){
          this.map = new BMap.Map("mapDOt"); // 创建Map实例
          this.gpsMess()
        },
        methods:{
          gpsMess(){
            var v =this
            this.$http.post(this.apis.GPS.DOT,{'orderId':this.mess.id}).then((res)=>{
              if(res.code == 200){
                v.addmap(res)
              }
            })
          },
          addmap(dot){
            var v = this
            var point = new BMap.Point(114.29788, 30.833161);
            if(dot){
              if(dot.result){
                point = new BMap.Point(dot.result.centerJd, dot.result.centerWd);
              }
            }
            v.map.centerAndZoom(point, 10)
            //添加地图类型控件
            v.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
            v.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件


            if(dot){
              if(dot.result){
                let zoom = [
                  new BMap.Point(dot.result.ksjd,dot.result.kswd),
                  new BMap.Point(dot.result.jsjd,dot.result.jswd)
                ]
                v.disDot(zoom)
              }
            }
          },
          //撒点
          disDot(list){
            this.map.clearOverlays()
            // 随机向地图添加25个标注
            for (var i = 0; i < list.length; i ++) {
              var point = new BMap.Point(list[i].lng, list[i].lat);
              this.addMarker(point,i);
            }
            this.map.setViewport(list)
          },
          // 编写自定义函数,创建标注
          addMarker(point,index){
            var v = this
            let imgUrl = 'http://lbsyun.baidu.com/jsdemo/img/fox.gif'
            if(index==0){
              imgUrl = 'http://47.98.39.45:9092/icon/map_line_begin.png'
            }else if(index==1){
              imgUrl = 'http://47.98.39.45:9092/icon/map_line_end.png'
            }
            var myIcon = new BMap.Icon(imgUrl, new BMap.Size(300,157));
            var marker = new BMap.Marker(point,{icon:myIcon});
            v.map.addOverlay(marker);
          },
          starNum(val){
            this.pf = val
            if(val!=0){
              this.pfzt = true
            }
          },
          close(){
            var v = this
            this.$router.back()
          },
          save(){
            var v = this
            v.$http.post(this.apis.PJDD.QUERTY,{'orderId':v.mess.id,'grade':v.pf}).then((res) =>{
              if (res.code==200) {
                v.tsi('评价成功')
              }
            }).catch((error)=>{

            })

          },
          tsi(mes){//表单验证提示
            var v=this
            Toast.info(mes)
            setTimeout(function () {
              Toast.hide()
              v.close()
            },1800)
          },

      }
    }
</script>

<style scoped>

</style>
