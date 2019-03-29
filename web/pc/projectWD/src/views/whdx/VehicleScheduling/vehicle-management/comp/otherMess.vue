<style scoped>
    .demo-upload-list{
        display: inline-block;
        width: 60px;
        height: 60px;
        text-align: center;
        line-height: 60px;
        border: 1px solid transparent;
        border-radius: 4px;
        overflow: hidden;
        background: #fff;
        position: relative;
        box-shadow: 0 1px 1px rgba(0,0,0,.2);
        margin-right: 4px;
    }
    .demo-upload-list-box{
        width: 100%;
        height: 100%;
    }
    .demo-upload-list img{
        width: 100%;
        height: 100%;
        margin: auto;
    }
    .demo-upload-list-cover{
        display: none;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background: rgba(0,0,0,.6);
    }
    .demo-upload-list:hover .demo-upload-list-cover{
        display: block;
    }
    .demo-upload-list-cover i{
        color: #fff;
        font-size: 20px;
        cursor: pointer;
        margin: 0 2px;
    }
</style>
<template>
    <div>
        <Modal
                v-model="showModal"
                width='800'
                :closable='false'
                :mask-closable="false"
                title="车辆档案信息">
            <Form
                v-model="otherMess"
                :label-width="120"
                :styles="{top: '20px'}">
                <div>
                    <Row>
                        <Col span="12">
                            <FormItem label='车牌号：'>
                                <Input readonly type="text" v-model="otherMess.cph" placeholder="请设置车牌号"></Input>
                            </FormItem>
                        </Col>
                        <Col span="12">
                            <FormItem label='生产商：'>
                                <Input type="text" v-model="otherMess.scs" placeholder="请填写生产商名称"></Input>
                            </FormItem>
                        </Col>
                        <Col span="12">
                            <FormItem label='车辆型号：'>
                                <Input type="text" v-model="otherMess.xh" placeholder="请填写车辆型号"></Input>
                            </FormItem>
                        </Col>
                        <Col span="12">
                            <FormItem label='初次登记日期：'>
                                <DatePicker type="datetime" v-model="otherMess.ccdjrq"
                                            style="width: 100%"
                                            placeholder="请选着初次登记日期"></DatePicker>
                            </FormItem>
                        </Col>
                        <Col span="12">
                            <FormItem label='保险公司名称：'>
                                <Input type="text" v-model="otherMess.bxgsmc" placeholder="请填写保险公司名称"></Input>
                            </FormItem>
                        </Col>
                        <Col span="12">
                            <FormItem label='保险时间：'>
                                <DatePicker type="datetime" v-model="otherMess.bxsj"
                                            style="width: 100%"
                                            placeholder="请选着保险时间"></DatePicker>
                            </FormItem>
                        </Col>
                        <Col span="12">
                            <FormItem label="车辆图片：">
                                <div>
                                    添加图片
                                </div>
                                <addlistfileImg @addImg="addImg"
                                                @removeFile = "removeFile"
                                                :urlList = "otherMess.tp"></addlistfileImg>
                            </FormItem>
                        </Col>
                    </Row>
                </div>
            </Form>
            <div slot='footer'>
                <Button type="default" @click="colse"style="color: #949494">取消</Button>
                <Button type="primary" @click="AddDataListOk()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>

    import addlistfileImg from './addlistfileImg.vue'

    export default {
        name:'',
        components: {
            addlistfileImg,
        },
        data(){
            return {
                showModal:true,
                //车辆配置信息
                otherMess:{
                    cph:'',
                    tp:''
                },
            }
        },
        props:{
            mess:{
                type:Object,
                default:{}
            }
        },
        created(){
            log('数据传输',this.mess)
            this.otherMess = this.mess

        },
        mounted () {
        },
        methods:{
            addImg(path){
                this.otherMess.tp += path+",";
            },
            removeFile(url){
                log('图片',url)
                log('原图片',this.otherMess.tp)

                let aindex = this.otherMess.tp.indexOf(url)//开始的位置
                let urlLength = url.length//传递的长度
                let filePath = this.otherMess.tp//原数据拼接
                let filelength = filePath.length//原数据长度

                this.otherMess.tp = filePath.slice(0,aindex)+filePath.slice(aindex+urlLength,filelength)

                log('修改数据',this.otherMess.tp)
            },
            colse(){
                var v = this
                v.$parent.compName = ''
            },
            //确认添加新用户进行前台表单数据验证
            AddDataListOk(){
                var v = this
//                    	新增
                delete v.otherMess.clDzwlCl;
                delete v.otherMess.clDzwl;
                v.$http.post(this.apis.CLGL.CHANGE,v.otherMess).then((res) =>{
                    if(res.code===200){
                        v.$Message.success('车辆修改成功');
                    }else{
                        v.$Message.error('车辆修改失败');
                    }
                    v.$parent.getmess()
                    v.$parent.compName = ''
                })
            },
        }
    }
    //15271928827
</script>

<style>
</style>
