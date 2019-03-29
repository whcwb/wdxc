<template>
      <div id="allmap" class="" style="height: 100%;background-color: #999999">

      </div>
</template>

<script>
    export default {
        name: "mapAllCode",
        data(){
            return{
                map:'',
                mapcenter: {
                    lng: 114.370095,//bdjd
                    lat: 30.545038//bdwd
                },
                zoom: 5,
            }
        },
        mounted() {
            var v = this
            // 百度地图API功能
            this.map = new BMap.Map("allmap"); // 创建Map实例
            this.buildMap()
        },
        methods:{
            //地图级别中心
            buildMap() {
                var v = this
                var point = new BMap.Point(v.mapcenter.lng, v.mapcenter.lat);
                this.map.centerAndZoom(point, v.zoom);// 初始化地图,设置中心点坐标和地图级别
                this.map.enableScrollWheelZoom();
                this.map.addControl(new BMap.MapTypeControl({
                    mapTypes: [
                        BMAP_NORMAL_MAP
                    ]
                }));
                //添加地图类型控件
                this.map.enableScrollWheelZoom(true);     			 //开启鼠标滚轮缩放
                this.map.addControl(new BMap.ScaleControl()); 			 // 添加比例尺控件
                this.map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
                this.map.addControl(new BMap.NavigationControl()); 		 // 添加平移缩放控件

                var MAX = 100
                var markers = []
                var pt = null
                for (var i= 0;i<MAX;i++){
                    pt = new BMap.Point(Math.random() * 40 + 85, Math.random() * 30 + 21);
                    markers.push(new BMap.Marker(pt));
                }

                var markerClusterer = new BMapLib.MarkerClusterer(this.map, {markers:markers})
            },
        }
    }
</script>

<style scoped>

</style>