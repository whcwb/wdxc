<template>
      <div :id="mapID" style="height: 400px;width:100%;background-color: #f5a623">
      </div>
</template>

<script>
    export default {
        name: "hisList",
        props:{
            gpsList:Array,
            index:Number
        },
        data(){
            return{
                mapID:'allmap',
                map: '',
                mapcenter: {
                    // lng: 114.368383,
                    // lat: 30.551134
                    lng: 113.888383,
                    lat: 30.681134
                },
                zoom: 12,
            }
        },
        created(){
            // console.log(this.gpsList);
            // console.log(this.index);
            this.mapID = this.mapID +this.index
        },
        mounted(){
            var v = this
            setTimeout(function () {
                v.showMap();
            },50)
        },
        methods:{
            showMap() {

                var v = this
                // 百度地图API功能
                this.map = new BMap.Map(v.mapID);    // 创建Map实例
                // if (this.gpsList.length > 0) {
                //     let lng = (this.gpsList[0].longitude+this.gpsList[this.gpsList.length-1].longitude)/2
                //     let lat = (this.gpsList[0].latitude+this.gpsList[this.gpsList.length-1].latitude)/2
                let lng = this.gpsList[0].longitude
                let lat = this.gpsList[0].latitude
                    this.map.centerAndZoom(new BMap.Point(v.mapcenter.lng, v.mapcenter.lat), this.zoom);  // 初始化地图,设置中心点坐标和地图级别
                // this.map.centerAndZoom(new BMap.Point(lng,lat), this.zoom)
                // } else {
                //     this.map.centerAndZoom(new BMap.Point(114.367161, 30.543013), this.zoom);  // 初始化地图,设置中心点坐标和地图级别
                //
                // }
                //添加地图类型控件
                this.map.addControl(new BMap.MapTypeControl({
                        mapTypes: [BMAP_NORMAL_MAP]
                    })
                );
                this.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
                this.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件
                this.map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
                this.map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件

                this.addMarker(this.gpsList[0].longitude,
                    this.gpsList[0].latitude,
                    this.apis.STATIC_PATH+'icon/map_line_begin.png')
                this.addMarker(this.gpsList[this.gpsList.length-1].longitude,
                    this.gpsList[this.gpsList.length-1].latitude,
                    this.apis.STATIC_PATH+'icon/map_line_end.png')
                this.showLine(this.gpsList)
                // setTimeout(function () {
                //     v.map.setViewport(this.gpsList);
                // },100)
            },
            showLine(line) {
                var v = this
                // if (!line.points) return;
                let ps = [];
                for (let r of line) {
                    ps.push(new BMap.Point(r.longitude, r.latitude));
                }
                var polyline = new BMap.Polyline(ps,
                    {strokeColor: line.color, strokeWeight: 6, strokeOpacity: 0.9}
                );
                this.map.addOverlay(polyline);
            },
            addMarker(lng,lat,icon) {
                var pt = new BMap.Point(lng, lat);
                var myIcon = new BMap.Icon(icon, new BMap.Size(70,70),{imageSize: new BMap.Size(37,62),anchor: new BMap.Size(19,62)});
                var marker = new BMap.Marker(pt,{icon:myIcon}); // 创建标注
                this.map.addOverlay(marker); // 将标注添加到地图中
            },
        }
    }
</script>

<style scoped>

</style>