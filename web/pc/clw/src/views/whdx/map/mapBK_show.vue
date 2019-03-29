<!--
电子围栏
电子围栏 展示 修改
-->

<style type="text/css">
    #allmap{
        height: 100%;
        width: 100%;
    }
    .claer{
        position: absolute;
        width: 64px;
        height: 47px;
        top: 5px;
        right: 0;
        z-index: 100;
        background-color: rgb(255, 255, 255);
        text-align: center;
        line-height: 45px;
        border: solid 1px #696666;
        border-radius: 7px;
    }
</style>

<template>
    <div style="height: 100%;background-color: #00FFFF;position: relative;">
        <div id="allmap"></div>
    </div>
</template>

<script>

    export default {
        name:'',
        data(){
            return{
                map:'',
                mapcenter:{
                    lng: 114.370095,//bdjd
                    lat: 30.545038//bdwd
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
                // carID:'437316001277673472',
                wlDot:[]
            }
        },
        props:{
            carNumber:{
                type:String,
                default:''
            }
        },
        created(){
        },
        mounted(){
            var v = this
            // 百度地图API功能
            this.map = new BMap.Map("allmap"); // 创建Map实例
            this.mapCenter()
            this.bkDot(this.carNumber)

        },
        methods:{
            //电子围栏点
            bkDot(id){
                var v = this
                this.$http.get(this.apis.DZWL.GET_BY_CAR_ID + "?clId=" + id).then((res) => {
                    log('电子围栏点',res)
                    if (res.code === 200 && res.result) {
                        let ditMess  = res.result.dlxxzb.split(';');
                        let dotLength = res.result.dlxxzb.split(';').length;
                        for(var i = 0 ; i<dotLength ; i++){
                            v.wlDot.push(
                                new BMap.Point(ditMess[i].split(',')[0],ditMess[i].split(',')[1])
                            )
                        }
                        setTimeout(function () {
                            v.map.setViewport(v.wlDot);
                            v.bkshow(v.wlDot)//电子围栏显示
                        },100)
                    }
                })
            },
            //地图级别中心
            mapCenter(){
                var v = this
                var point = new BMap.Point(v.mapcenter.lng, v.mapcenter.lat);
                this.map.centerAndZoom(point, v.zoom);// 初始化地图,设置中心点坐标和地图级别
                this.map.addControl(new BMap.MapTypeControl({
                    mapTypes:[
                        BMAP_NORMAL_MAP
                    ]}));
                //添加地图类型控件
                this.map.enableScrollWheelZoom(true);     					     //开启鼠标滚轮缩放
                this.map.addControl(new BMap.ScaleControl()); 					 // 添加比例尺控件
                this.map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
                this.map.addControl(new BMap.NavigationControl()); 				// 添加平移缩放控件
            },
            //撒点
            disDot(list){
                this.clear()
                // 编写自定义函数,创建标注
                var v = this
                function addMarker(point){
                    var marker = new BMap.Marker(point);
                    v.map.addOverlay(marker);
                }
                // 随机向地图添加25个标注
                for (var i = 0; i < list.length; i ++) {
                    var point = new BMap.Point(list[i].mapCen.lng, list[i].mapCen.lat);
                    addMarker(point);
                }
            },
            bkshow(mess){
                var v = this
                    // mess=[
                    // new BMap.Point(116.387112,39.920977),
                    //     new BMap.Point(116.385243,39.913063),
                    //     new BMap.Point(116.394226,39.917988),
                    //     new BMap.Point(116.401772,39.921364),
                    //     new BMap.Point(116.41248,39.927893)
                    // ]
                var polygon = new BMap.Polygon(mess, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});  //创建多边形
                v.map.addOverlay(polygon);           //增加多边形
                // v.map.setViewport(mess);             //可视化点
            },
            //布控
            bk(){
                var v = this
                var styleOptions = {//覆盖层的样式
                    strokeColor:"red",    //边线颜色。
                    fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
                    strokeWeight: 3,       //边线的宽度，以像素为单位。
                    strokeOpacity: 0.8,    //边线透明度，取值范围0 - 1。
                    fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
                    strokeStyle: 'solid' //边线的样式，solid或dashed。
                }

                var drawingManager = new BMapLib.DrawingManager(v.map, {
                    isOpen: false, //是否开启绘制模式
                    enableDrawingTool: true, //是否显示工具栏
                    drawingToolOptions: {
                        anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
                        offset: new BMap.Size(64, 5), //偏离值
                    },
                    circleOptions: styleOptions, //圆的样式
                    polylineOptions: styleOptions, //线的样式
                    polygonOptions: styleOptions, //多边形的样式
                    rectangleOptions: styleOptions //矩形的样式
                })
                drawingManager.addEventListener("overlaycomplete", function(e) {
                    log(e);
                    v.$emit('choosePoint',e.overlay.ia)
                });
            },
            //清除层
            clear(){
                this.map.clearOverlays()
            },
            bk_OK(){

            }
        }
    }
</script>

<style>
</style>
