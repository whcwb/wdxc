<style lang="less">
	.mappage{
		height: 100%;
		background-color: #00FFFF;
		position: relative;
		.backSty{
			position: absolute;
			left: 10px;
			top: 10px;
			z-index: 100;
			i{
				font-size: 25px;
			}
		}
		#allmap{
			height: 100%;
			width: 100%;
		}
	}
</style>
<template>
	<div class="mappage">
		<div class="backSty" @click="back">
			<i class="iconfont icon-back"></i>
		</div>
		<div id="allmap"></div>
	</div>
</template>

<script>
  import configApi from '@/axios/config.js'
	export default {
		name:'',
		data(){
			return{
				map:'',
				mapcenter:{
					lng: 114.372443,
	    			lat: 30.544572
				},
				zoom:16,
				mess:[
					{
						lng: 114.3724431,
	    				lat: 30.544572
					},
					{
						lng: 114.3725432,
	    				lat: 30.564572
					}
				],
        stationList:[]
			}
		},
		created(){

		},
		mounted(){
			this.addmap()
      if(this.$store.state.app.lineID!=0){
        this.getStations(this.$store.state.app.lineID)
      }else {
        this.$router.push({
          name:'center'
        })
      }
		},
		methods:{
		  addmap(){
          var v = this
          // 百度地图API功能
          this.map = new BMap.Map("allmap");    // 创建Map实例
          this.map.centerAndZoom(new BMap.Point(this.mapcenter.lng, this.mapcenter.lat),this.zoom);  // 初始化地图,设置中心点坐标和地图级别
          //添加地图类型控件
          this.map.addControl(new BMap.MapTypeControl({
            mapTypes:[
              BMAP_NORMAL_MAP
            ]}));

          this.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
      },
      mapline(){
          var pois = [];
          for(let r of this.stationList){
            pois.push(new BMap.Point(r.jd,r.wd));
          }
          this.map.setViewport(pois)
          var sy = new BMap.Symbol(BMap_Symbol_SHAPE_BACKWARD_OPEN_ARROW, {
            scale: 0.6,//图标缩放大小
            strokeColor:'#f00',//设置矢量图标的线填充颜色
            strokeWeight: '2',//设置线宽
          });
          var icons = new BMap.IconSequence(sy, '10', '40');
          var polyline = new BMap.Polyline(pois, {
            enableEditing: false,//是否启用线编辑，默认为false
            enableClicking: true,//是否响应点击事件，默认为true
            icons:[icons],
            strokeWeight:'8',//折线的宽度，以像素为单位
            strokeOpacity: 0.8,//折线的透明度，取值范围0 - 1
            strokeColor:"#18a45b" //折线颜色
          });
          this.map.addOverlay(polyline);          //增加折线
      },
      getStations(id){
        this.$http.post(configApi.MAPMDATA.QUERTY,{xlId:id}).then((res) =>{
          if(res.code===200 && res.result){
            this.stationList = res.result;
            this.mapline();
          }
        })
      },
			back(){
				this.$router.push({
					name:'lineMess'
				})
			}
		}
	}
</script>

<style>
</style>
