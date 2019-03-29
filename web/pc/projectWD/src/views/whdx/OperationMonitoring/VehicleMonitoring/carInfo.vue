<style>
    .buttons .ivu-tooltip{
        padding:8px;
    }
    .label{
        display: inline-block;
        margin-top: 8px;
        margin-bottom: 8px;
    }
</style>
<template>
    <Col span="24" v-if="car != null && car != ''">
        <Card>
            <p slot="title">
                <Icon type="soup-can-outline"></Icon>
                {{car.cph}}
                <i-switch v-model="switchTyp" @on-change="switchG" v-if="car.zxzt=='00'"></i-switch>
            </p>
            <a href="#" slot="extra">
                <Icon type="md-close" @click.native="close"></Icon>
            </a>
            <Row>
                <Row style="z-index: 1000000;" class="buttons" v-if="car != null">
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="setControl('12','1-10')" icon="md-camera"></Button><br>
                        <span class="label">前摄像头拍照</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="setControl('12','2-10')" icon="ios-camera"></Button><br>
                        <span class="label">后摄像头拍照</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="setControl('12','0-10')" icon="logo-instagram"></Button><br>
                        <span class="label">前后摄像拍照</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="setControl('11','1-10')" icon="ios-videocam"></Button><br>
                        <span class="label">前摄像头视频</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="setControl('11','2-10')" icon="ios-videocam-outline"></Button><br>
                        <span class="label">后摄像头视频</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="setControl('11','0-10')" icon="ios-videocam-outline"></Button><br>
                        <span class="label">前后摄像视频</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="showFance" icon="md-qr-scanner"></Button><br>
                        <span class="label">电子围栏</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="showPathHistory" icon="md-git-pull-request"></Button><br>
                        <span class="label">历史轨迹</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="gotoPhotoList" icon="md-camera"></Button><br>
                        <span class="label">图片库</span>
                    </Col>
                    <Col span="12" style="text-align: center">
                        <Button shape="circle"  type="success" @click="gotoVideoList" icon="ios-videocam"></Button><br>
                        <span class="label">视频库</span>
                    </Col>
                </Row>
                <Row  v-if="photo.showModal">
                    <div style="width:100%;height: 100%;text-align: center;">
                        <i-circle v-if="photo.src == ''" :percent="photo.percent">
                            <span class="demo-Circle-inner" style="font-size:24px">{{photo.sec}} 秒</span>
                        </i-circle>
                        <img v-if="photo.src != ''" :src="staticPath+photo.src" style="width: 100%;height: 100%"  @click="showImg(staticPath+photo.src)">
                    </div>
                </Row>
                <!--<Modal v-model="photo.src != ''" width="1000" height="800"-->
                    <!--:closable='false'-->
                    <!--:mask-closable="false" title="预览">-->
                    <!--<img v-if="photo.src != ''" :src="staticPath+photo.src" style="width: 100%;height: 100%">-->
                    <!--<div slot='footer' style="text-align: center;">-->
                        <!--<Button type="primary" @click="closePhoto">关闭</Button>-->
                    <!--</div>-->
                <!--</Modal>-->
                <Row v-if="video.showModal">
                    <div style="width:100%;height: 100%;text-align: center;">
                        <i-circle v-if="video.src == ''" :percent="video.percent">
                            <span class="demo-Circle-inner" style="font-size:24px">{{video.sec}} 秒</span>
                        </i-circle>
                        <video v-if="video.src != ''" :src="staticPath+video.src" style="width: 100%;height: 100%" controls  @click="window.open(staticPath+video.src)"></video>
                    </div>
                    <div style="text-align: center;">
                        <Button type="primary" @click="closeVideo">关闭</Button>
                    </div>
                </Row>
                <!--<Modal v-model="video.src != ''" width="1000" height="800"-->
                       <!--:closable='false'-->
                       <!--:mask-closable="false" title="预览">-->
                    <!--<div slot='footer' style="text-align: center;">-->
                        <!--<Button type="primary" @click="closePhoto">关闭</Button>-->
                    <!--</div>-->
                <!--</Modal>-->
            </Row>
        </Card>
    </Col>
</template>

<script>

    export default {
        name: "carInfo",
        components: {
        },
        data(){
            return {
                switchTyp:'false',
                staticPath :this.apis.VIDEO_PATH,
                showConfirmButton:false,
                componentName:'',
                SpinShow:false,
                car:'',
                taskId:'',
                video:{
                    showModal:false,
                    src:'',
                    sec:120,
                    percent:100,
                    totalSec : 120,
                    success:false,
                },
                photo:{
                    showModal:false,
                    src:'',
                    sec:120,
                    percent:100,
                    totalSec : 120,
                    success:false,
                },
            }

        },
        computed: {
            GetSendhbsp() {
                return this.$store.state.app.sendsp
            },
            GetSendZp() {
                return this.$store.state.app.sendzp
            },
        },
        watch: {
            'car.zdbh':function (n,o) {
                this.switchTyp = false
            }
        //     GetSendhbsp: function(newQuestion, oldQuestion) {
        //         this.onVideoResult(newQuestion);
        //     },
        //     GetSendZp: function(newQuestion, oldQuestion) {
        //         this.onPhotoResult(newQuestion);
        //     },
        },
        created(){
        },
        mounted(){
        },
        methods:{
            switchG(status){
                var v= status;
                // console.log("aaa",v);
                this.$emit('switchGJ',v);
            },
            gotoPhotoList(){
              this.$router.push({name:'CloudPhoto',params:{zdbh:this.car.zdbh}});
            },
            gotoVideoList(){
              this.$router.push({name:'CloudVideo',params:{zdbh:this.car.zdbh}});
            },
            showImg(src){
                window.open(src);
            },
            closePhoto(){
                this.photo.showModal = false
                this.photo.success = false
                this.resetPhotoCount();
            },
            closeVideo(){
                this.video.showModal = false
                this.video.success = false
                this.resetVideoCount();
            },
            queryPhoto(){
                this.querySpk(this.photo.taskId,this.onPhotoResult);
            },
            queryVideo(){
                this.querySpk(this.video.taskId,this.onVideoResult);
            },
            querySpk(taskId,callback){
              this.$http.get(this.apis.CLOUD.QUERY,{params:{bj:taskId}}).then((res)=>{
                  if (res.code == 200){
                      if (res.page.list.length > 0){
                          callback(res.page.list[0]);
                      }
                  }else{
                      this.$Message.error(res.message);
                  }
              })
            },
            countVideo(){
                this.video.sec--;
                this.video.percent = this.video.sec*100 / this.video.totalSec;
                clearTimeout();
                if (this.video.success)return;
                if (!this.video.showModal || this.video.sec <= 0){
                    this.resetVideoCount();
                    return;
                }
                if (this.$route.path != '/OperationMonitoring/VehicleMonitoring'){
                    return;
                }
                if (this.video.sec <= 0){
                    this.$Message.error("等待超时！");
                    this.resetVideoCount();
                    return;
                }
                if (this.video.sec % 10 == 0){
                    this.queryVideo();
                }
                let v = this;
                setTimeout(()=>{
                    if (this.video.success)return;
                    if (!v.video.showModal){
                        v.resetVideoCount();
                        return;
                    }
                    if (v.video.sec <= 0){
                        v.$Message.error("等待超时！");
                        v.resetVideoCount();
                        return;
                    }
                    v.countVideo();
                },1000)
            },
            countPhoto(){
                this.photo.sec--;
                this.photo.percent = this.photo.sec*100 / this.photo.totalSec;
                clearTimeout();
                if (this.photo.success){
                    return;
                }
                if (!this.photo.showModal || this.photo.sec <= 0){
                    this.resetPhotoCount();
                    return;
                }
                if (this.$route.path != '/OperationMonitoring/VehicleMonitoring'){
                    return;
                }
                if (this.photo.sec <= 0){
                    this.$Message.error("等待超时！");
                    this.resetPhotoCount();
                    return;
                }
                if (this.photo.sec % 5 == 0){
                    this.queryPhoto();
                }
                let v = this;
                setTimeout(()=>{
                    if (this.photo.success)return;
                    if (!v.photo.showModal){
                        v.resetPhotoCount();
                        return;
                    }
                    if (v.photo.sec <= 0){
                        v.$Message.error("等待超时！");
                        v.resetPhotoCount();
                        return;
                    }
                    v.countPhoto();
                },1000)
            },
            resetPhotoCount(){
                this.photo.showModal = false;
                this.photo.sec = this.photo.totalSec;
                this.photo.src = '';
                this.photo.percent = 100;
                this.photo.taskId = '';
                this.photo.success = false
            },
            resetVideoCount(){
                this.video.showModal = false;
                this.video.sec = this.video.totalSec;
                this.video.src = '';
                this.video.percent = 100;
                this.video.taskId = '';
                this.video.success = false
            },
            stopPhotoCount(){
                this.photo.sec = this.photo.totalSec;
                this.photo.percent = 100;
                this.photo.taskId = '';
            },
            stopVideoCount(){
                this.video.sec = this.video.totalSec;
                this.video.percent = 100;
                this.video.taskId = '';
            },
            onPhotoResult(m){
                if (m.bj != this.photo.taskId){
                    return;
                }
                this.$Message.success("拍摄成功!")
                this.photo.src = m.url;
                this.photo.showModal = true;
                this.photo.success = true
                this.stopPhotoCount();
            },
            onVideoResult(m){
                if (m.bj != this.video.taskId){
                    return;
                }
                this.wait = false;
                this.$Message.success("拍摄成功!")
                this.video.src = m.url;
                this.video.showModal = true;
                this.video.success = true
                this.stopVideoCount();
            },
            init(item){
                this.car = item;
                // this.$refs.carInfo.init(item);
                this.resetPhotoCount();
                this.resetVideoCount();
            },
            hide(){
                this.car = null;
            },
            close(){
                this.car = null;
                this.$emit('md-close');
            },
            save(){
                this.SpinShow = true;
                setTimeout(()=>{
                    this.$Message.success("操作成功!")
                    this.SpinShow = false;
                    this.init();
                },1000)
            },
            /**deviceId:设备id  cmdType:11拍视频  12拍照片 13合并视屏 (三选一)
             *
             * 	 * cmdType 为11和12的时候使用
             * 参数格式为分隔式字符串  如:0-10 前一个0 标识要抓拍的摄像头  后一个10标识当前时间点前后十秒
             * 摄像头参数如下:0,前后都抓拍, 1表示仅前摄像头, 2表示仅仅后摄像头。当cmdType为12的时候，此参数也是一样，只是抓拍前后多少秒参数无效【客户端自动判断，后台传递参数即可】
             * cmdType 为13的时候参数是0-0 或者1-0  ，特别注意，为13的时候，startTime和endTime必须有值
             * 摄像头参数如下:0 合并前摄像头  1 合并后摄像头  2 合并内置摄像头【内置摄像头这个暂时无法使用】
             */
            setControl(type,param){
                var v = this
                let params = {
                    deviceId:this.car.zdbh,
                    cmdType:type,
                    cmdParams:param
                }
                this.closePhoto();
                this.closeVideo();
                this.$http.post(this.apis.CLJK.SEND_CONTROLL,params).then((res) =>{
                    if (res.code === 200){
                        if (res.message.indexOf('成功') >= 0){
                            this.$Message.success("发送成功!")
                        }else{
                            this.$Message.error(res.message)
                        }
                        if (type == '12'){
                            this.photo.showModal = true;
                            this.photo.taskId = res.result;
                            this.countPhoto();
                        }else if (type == '11'){
                            this.video.showModal = true;
                            this.video.taskId = res.result;
                            this.countVideo();
                        }
                    }else{
                        this.$Message.error(res ? res.message:"网络异常")
                    }
                })
            },
            showPathHistory(){
                this.$router.push({name: 'historyTarck_new',params:{zdbh:this.car.zdbh}});
            },
            showFance(){
                this.$parent.showFance(this.car.clid)
                // this.close();
            }
        }
    }
</script>

<style scoped>
    .padding-top16{
        padding-top: 16px;
    }
    .height200{
        height: 200px;
    }
    .height300{
        height: 300px;
    }
</style>
