<style>
    .buttons .ivu-tooltip {
        padding: 8px;
    }

    .label {
        display: inline-block;
        margin-top: 8px;
        margin-bottom: 8px;
    }

    /*.ZDmessAll{*/
    .RowSty {
        font-size: 16px;
        font-weight: 600;
        padding: 4px 0;
    }

    /*}*/
</style>
<template>
    <div>
        <Col span="24" v-if="car != null && car != ''">
            <Card>
                <p slot="title">
                    <Icon type="soup-can-outline"></Icon>
                    <Icon type="ios-analytics-outline"/>
                    <i class="iconfont icon-weibiaoti507"></i>
                    {{car.cph}}
                </p>
                <a href="#" slot="extra">
                    <Icon type="md-close" @click.native="close"></Icon>
                </a>
                <div class="ZDmessAll">
                    <!--<div>-->
                        <!--<div style="text-align:center;font-size: 16px;color: #fa541c;-->
            		<!--font-weight: 600;padding: 6px 0">-->
                            <!--<i class="iconfont icon-sudu1"></i>-->
                            <!--累计里程：-->
                            <!--<Tag color="cyan">{{ZdMess.lcs}}</Tag>-->
                            <!--公里-->
                        <!--</div>-->
                    <!--</div>-->
                    <Row type="flex" justify="center" class-name='RowSty'>
                        <Col span="12" style="text-align: center">
                            <Button shape="circle"  type="success" @click="showPathHistory" icon="md-git-pull-request"></Button><br>
                            <span class="label">历史轨迹</span>
                        </Col>
                    </Row>
                </div>
            </Card>
        </Col>
        <component :is="compName"></component>
    </div>

</template>

<script>
    // import zl from './comp/ZL'

    export default {
        name: "carInfo",
        components: {},
        data() {
            return {
                compName: '',
                staticPath: this.apis.VIDEO_PATH,
                showConfirmButton: false,
                componentName: '',
                SpinShow: false,
                car: '',
                taskId: '',
                video: {
                    showModal: false,
                    src: '',
                    sec: 120,
                    percent: 100,
                    totalSec: 120
                },
                photo: {
                    showModal: false,
                    src: '',
                    sec: 120,
                    percent: 100,
                    totalSec: 120
                },
                ZdMess:{}
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
            GetSendhbsp: function (newQuestion, oldQuestion) {
                this.onVideoResult(newQuestion);
            },
            GetSendZp: function (newQuestion, oldQuestion) {
                this.onPhotoResult(newQuestion);
            },
            'car.zdbh':function (n,o) {
                if(n){
                    console.log('终端编号',n);
                    this.getZdMess(n)
                }
            }
        },
        created() {
        },
        mounted() {
        },
        methods: {
            getZdMess(zdbh){
                this.$http.post('/api/zdgl/getDwqInfo',{ zdbh:zdbh}).then(res=>{
                    if(res.code == 200 && res.result){
                        this.ZdMess = res.result
                    }
                    console.log(res);
                }).catch(err=>{})
            },
            init(item) {
                this.car = item;
                this.resetPhotoCount();
                this.resetVideoCount();
            },
            resetPhotoCount() {
                this.photo.showModal = false;
                this.photo.sec = this.photo.totalSec;
                this.photo.src = '';
                this.photo.percent = 100;
                this.taskId = '';
            },
            resetVideoCount() {
                this.video.showModal = false;
                this.video.sec = this.video.totalSec;
                this.video.src = '';
                this.video.percent = 100;
                this.taskId = '';
            },
            close() {
                this.car = null
                this.$emit('close')
            },
            showPathHistory(){
                this.$router.push({name: 'historyTarck_new',params:{zdbh:this.car.zdbh}});
            },
            /*
            gotoPhotoList() {
                this.$router.push({name: 'CloudPhoto'});
            },
            gotoVideoList() {
                this.$router.push({name: 'CloudVideo'});
            },
            showImg(src) {
                window.open(src);
            },
            closePhoto() {
                this.photo.showModal = false
                this.resetPhotoCount();
            },
            closeVideo() {
                this.video.showModal = false
                this.resetVideoCount();
            },
            countVideo() {
                this.video.sec--;
                this.video.percent = this.video.sec * 100 / this.video.totalSec;
                clearTimeout();
                if (!this.video.showModal || this.video.sec <= 0) {
                    this.resetVideoCount();
                    return;
                }
                if (this.$route.path != '/OperationMonitoring/VehicleMonitoring') {
                    return;
                }
                if (this.video.sec <= 0) {
                    this.$Message.error("等待超时！");
                    this.resetVideoCount();
                    return;
                }
                let v = this;
                setTimeout(() => {
                    if (!v.video.showModal) {
                        v.resetVideoCount();
                        return;
                    }
                    if (v.video.sec <= 0) {
                        v.$Message.error("等待超时！");
                        v.resetVideoCount();
                        return;
                    }
                    v.countVideo();
                }, 1000)
            },
            countPhoto() {
                this.photo.sec--;
                this.photo.percent = this.photo.sec * 100 / this.photo.totalSec;
                clearTimeout();
                if (!this.photo.showModal || this.photo.sec <= 0) {
                    this.resetPhotoCount();
                    return;
                }
                if (this.$route.path != '/OperationMonitoring/VehicleMonitoring') {
                    return;
                }
                if (this.photo.sec <= 0) {
                    this.$Message.error("等待超时！");
                    this.resetPhotoCount();
                    return;
                }
                let v = this;
                setTimeout(() => {
                    if (!v.photo.showModal) {
                        v.resetPhotoCount();
                        return;
                    }
                    if (v.photo.sec <= 0) {
                        v.$Message.error("等待超时！");
                        v.resetPhotoCount();
                        return;
                    }
                    v.countPhoto();
                }, 1000)
            },


            stopPhotoCount() {
                this.photo.sec = this.photo.totalSec;
                this.photo.percent = 100;
                this.taskId = '';
            },
            stopVideoCount() {
                this.video.sec = this.video.totalSec;
                this.video.percent = 100;
                this.taskId = '';
            },
            onPhotoResult(r) {
                let m = JSON.parse(r);
                if (m.bj != this.taskId) {
                    return;
                }
                this.$Message.success("拍摄成功!")
                this.photo.src = m.url;
                this.photo.showModal = true;
                this.stopPhotoCount();
            },
            onVideoResult(r) {
                let m = JSON.parse(r);
                if (m.bj != this.taskId) {
                    return;
                }
                this.wait = false;
                this.$Message.success("拍摄成功!")
                this.video.src = m.url;
                this.video.showModal = true;
                this.stopVideoCount();
            },
            hide() {
                this.car = null;
            },
            close() {
                this.car = null
                this.$emit('md-close')
            },
            save() {
                this.SpinShow = true;
                setTimeout(() => {
                    this.$Message.success("操作成功!")
                    this.SpinShow = false;
                    this.init();
                }, 1000)
            },
            // *deviceId:设备id  cmdType:11拍视频  12拍照片 13合并视屏 (三选一)
            //  *
            //  *         * cmdType 为11和12的时候使用
            //  * 参数格式为分隔式字符串  如:0-10 前一个0 标识要抓拍的摄像头  后一个10标识当前时间点前后十秒
            //  * 摄像头参数如下:0,前后都抓拍, 1表示仅前摄像头, 2表示仅仅后摄像头。当cmdType为12的时候，此参数也是一样，只是抓拍前后多少秒参数无效【客户端自动判断，后台传递参数即可】
            //  * cmdType 为13的时候参数是0-0 或者1-0  ，特别注意，为13的时候，startTime和endTime必须有值
            //  * 摄像头参数如下:0 合并前摄像头  1 合并后摄像头  2 合并内置摄像头【内置摄像头这个暂时无法使用】
            setControl(type, param) {
                var v = this
                let params = {
                    deviceId: this.car.zdbh,
                    cmdType: type,
                    cmdParams: param
                }
                this.SpinShow = true;
                this.closePhoto();
                this.closeVideo();
                this.$http.post(this.apis.CLJK.SEND_CONTROLL, params).then((res) => {
                    this.SpinShow = false;
                    if (res.code === 200) {
                        this.$Message.success("发送成功!")
                        this.taskId = res.result;
                        if (type == '12') {
                            this.photo.showModal = true;
                            this.countPhoto();
                        } else if (type == '11') {
                            this.video.showModal = true;
                            this.countVideo();
                        }
                    } else {
                        this.$Message.error(res ? res.message : "网络异常")
                    }
                })
            },
            showPathHistory() {
                this.$router.push({name: 'historyTarck_new', params: {zdbh: this.car.zdbh}});
            },
            showFance() {
                this.$parent.showFance(this.car.clid)
            }
            */
        }
    }
</script>

<style scoped>
    .padding-top16 {
        padding-top: 16px;
    }

    .height200 {
        height: 200px;
    }

    .height300 {
        height: 300px;
    }
</style>
