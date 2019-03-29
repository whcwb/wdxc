<style lang="less">
    @import '../../../../../styles/common.less';
    .headTit {
        text-align: center;
    }
    .listStyle{
        /*background-color: #FFFFFF;*/
        cursor: pointer;
    }
</style>
<!--订单分派-->
<template>
    <!--<div class="box">-->
    <!--<div class="tit headTit padding">
        <h1>
            任务分派
        </h1>
    </div>-->
    <!--<div class="body padding-top-10" style="border-top: solid #dddee1 2px;">-->
    <Row style="height: 100%;">
        <Col span='8' class="padding-left-right" style="height: 100%;overflow: auto;">
        <div class="box">
            <div v-if="titFindMesslist" class="tit" style="height: 55px;line-height: 50px;text-align: center;background-color: #fff;margin: 0 12px;border-radius: 8px">
                <h2 style="position: relative;">
                    待派单据（6单）
                    <Button type="primary" shape="circle"
                            icon="ios-search"
                            @click="titFindMesslist=!titFindMesslist"
                            style="position: absolute;top:10px;right: 8px;"></Button>
                </h2>
            </div>
            <div v-else class="tit" style="height: 55px">
                <Input v-model="param" size="large" @on-change='findlistMess(param)'>
                <Button slot="prepend" type="warning" @click="titFindMesslist=!titFindMesslist">6单</Button>
                <Button slot="append" type="primary" icon="ios-search"></Button>
                </Input>
            </div>
            <div id='pageList' class="body">
                <div class='padding padding-left-10 listStyle' v-for="item in pagList">
                    <Card>
                        <div slot="title">
                            <b>
                                {{item.unit}}---{{item.peopleName}}
                            </b>
                        </div>
                        <span slot="extra">
							        	<Button type="success" size="small">修改</Button>
							        	<Button type="error" size="small"style="color: #949494">取消</Button>
							        </span>
                        <div style="height: 60px;overflow: auto;overflow-x: hidden;">
                            <Row style="border-bottom: solid 1px #dddee1;padding-bottom: 4px;">
                                <Col span="24" class="margin-top-5">
                                {{item.time}}
                                </Col>
                                <Col span="24" class="margin-top-5">
                                {{item.start}}
                                <Icon type="arrow-right-c"></Icon>
                                {{item.end}}
                                </Col>
                            </Row>
                        </div>
                    </Card>

                </div>
            </div>
        </div>
        </Col>
        <Col span='16' class="padding-left-right" style="height: 100%;overflow: auto;">
        <div class="box">
            <div class="tit" style="height: 60px;text-align: center;background-color: #fff;">
                <Row>
                    <Col span="12">
                    <Menu mode="horizontal" theme="light" :active-name="MenuNum" @on-select="MenuClick">
                        <MenuItem name="1">
                            <Icon type="ios-people"></Icon>
                            运输中心
                        </MenuItem>
                        <MenuItem name="2">
                            <Icon type="ios-paper"></Icon>
                            外借公司
                        </MenuItem>
                    </Menu>
                    </Col>
                    <Col span="12">
                    <div style="margin-top: 14px;">
                        <Input v-model="findcar" placeholder="请输入车辆相关信息...">
                        <Button slot="append" icon="ios-search"></Button>
                        </Input>
                    </div>
                    </Col>
                </Row>
            </div>
            <div class="body">
                <Row v-show="MenuNum=='1'">
                    <Col span='12' class='padding-top-10' v-for="(item,index) in list">
                    <Card>
                        <div slot="title">
                            <b>
                                <Icon type="ios-film-outline"></Icon>
                                {{item.name}}
                                <Icon type="stats-bars"></Icon>
                                {{item.carList.length}}
                            </b>
                        </div>
                        <span slot="extra">
								        	<Button v-if="item.type=='01'" type="success" size="small">在班</Button>
								        	<Button v-else type="error" size="small">休息</Button>
								        </span>
                        <div v-if="item.type=='02'" style="height: 120px;line-height: 100px;text-align: center;">
                            <h2>师傅休息中</h2>
                        </div>
                        <div :id="index+'pageitemList'" v-else style="height: 120px;overflow: auto;overflow-x: hidden;">
                            <Row v-for="items in item.carList" style="border-bottom: solid 3px #dddee1;padding-bottom: 4px;">
                                <Col span="22" class="margin-top-5">
                                <Row>
                                    {{items.unit}}--
                                    {{items.peopleName}}--
                                </Row>
                                <Row>
                                    {{items.starland}}--
                                    {{items.endland}}
                                </Row>
                                <Row>
                                    {{items.starTime}}
                                </Row>
                                </Col>
                                <Col span="2" style="text-align: right;" class="padding-top-10">
                                <Button
                                        type="primary" shape="circle"
                                        size="small" icon="reply-all"
                                        :disabled="items.listType=='01'"></Button>
                                </Col>
                            </Row>
                        </div>
                    </Card>
                    </Col>
                </Row>
                <Row v-show="MenuNum=='2'">
                    <Col span='12' class='padding-top-10' v-for="(item,index) in listT">
                    <Card>
                        <div slot="title">
                            <b>
                                <Icon type="ios-film-outline"></Icon>
                                {{item.num}}
                                <Icon type="stats-bars"></Icon>
                                {{item.carList.length}}
                            </b>
                        </div>
                        <div style="height: 120px;overflow: auto;overflow-x: hidden;">
                            <Row v-for="items in item.carList" style="border-bottom: solid 3px #dddee1;padding-bottom: 4px;">
                                <Col span="22" class="margin-top-5">
                                <Row>
                                    {{items.unit}}--
                                    {{items.peopleName}}--
                                    {{items.starTime}}
                                </Row>
                                <Row>
                                    {{items.starland}}--
                                    {{items.endland}}
                                </Row>
                                </Col>
                                <Col span="2" style="text-align: right;" class="padding-top-10">
                                <Button
                                        type="primary" shape="circle"
                                        size="small" icon="reply-all"
                                        :disabled="items.listType=='01'"></Button>
                                </Col>
                            </Row>
                        </div>
                    </Card>
                    </Col>
                </Row>
            </div>
        </div>
        </Col>
    </Row>
    <!--</div>
</div>-->
</template>

<script>
    import Sortable from 'sortablejs';
    export default{
        name:'driver',
        data(){
            return{
                findcar:'',
                titFindMesslist:true,
                MenuNum:'1',
                pagList:[
                    {
                        unit:'信息学院',
                        peopleName:'王厦',
                        time:'2018-03-22 09:00:00',
                        start:'武汉大学正门',
                        end:'汉口火车站'
                    },{
                        unit:'文学院',
                        peopleName:'李大明',
                        time:'2018-03-22 09:00:00',
                        start:'武汉大学正门',
                        end:'汉口火车站'
                    },{
                        unit:'生物学院',
                        peopleName:'周晓',
                        time:'2018-03-22 09:00:00',
                        start:'武汉大学正门',
                        end:'汉口火车站'
                    },{
                        unit:'生物学院',
                        peopleName:'周晓',
                        time:'2018-03-22 09:00:00',
                        start:'武汉大学正门',
                        end:'汉口火车站'
                    }
                ],
                param:'',
                list:[
                    {
                        name:'张师傅',
                        type:'01',
                        carList:[
                            {
                                unit:'生物学院',
                                peopleName:'周晓',
                                starTime:'2017-01-01 09:11:00',
                                starland:'武汉大学',
                                endland:'汉口火车站',
                                listType:'01'
                            },{
                                unit:'信息学院',
                                peopleName:'王大',
                                starTime:'2017-01-01 12:11:00',
                                starland:'汉口火车站',
                                endland:'武汉大学',
                                listType:'02'
                            },{
                                unit:'信息学院',
                                peopleName:'李小',
                                starTime:'2017-01-01 12:11:00',
                                starland:'汉口火车站',
                                endland:'武汉大学',
                                listType:'02'
                            }
                        ]
                    },{
                        name:'王师傅',
                        type:'02',
                        carList:[
                        ]
                    },{
                        name:'李师傅',
                        type:'01',
                        carList:[
                            {
                                unit:'信息学院',
                                peopleName:'王厦',
                                starTime:'2017-01-01 09:11:00',
                                starland:'武汉大学',
                                endland:'汉口火车站',
                                listType:'02'
                            }
                        ]
                    },{
                        name:'周师傅',
                        type:'01',
                        carList:[
                            {
                                unit:'恒川集团',
                                peopleName:'王呵呵',
                                starTime:'2017-01-01 12:11:00',
                                starland:'恒川集团正门',
                                endland:'东湖',
                                listType:'02'
                            }
                        ]
                    }
                ],
                listT:[
                    {
                        num:'恒船公司',
                        carList:[
                            {
                                unit:'信息学院',
                                peopleName:'王厦',
                                starTime:'2017-01-01 09:11:00',
                                starland:'武汉大学',
                                endland:'汉口火车站',
                                listType:'01'
                            }
                        ]
                    },{
                        num:'大山公司',
                        carList:[
                            {
                                unit:'信息学院',
                                peopleName:'王厦',
                                starTime:'2017-01-01 09:11:00',
                                starland:'武汉大学',
                                endland:'汉口火车站',
                                listType:'02'
                            }
                        ]
                    },
                ]
            }
        },
        created(){
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '车辆管理',
            },{
                title: '订单管理',
            },{
                title: '订单分配',
            }])
        },
        mounted(){
            let vm = this;
            let pageList = document.getElementById('pageList');
            Sortable.create(pageList, {
                group: {
                    name: 'list',
                    pull: true
                },
                animation: 120,
//	            ghostClass: 'placeholder-style',
//	            fallbackClass: 'iview-admin-cloned-item',
                onRemove (event) {
                    log(event.item.getAttribute('data-index'))
                    log(event)
                    log(event.to.id)
                    //              vm.doArray.splice(event.newIndex, 0, vm.todoArray[event.item.getAttribute('data-index')]);
                    //          	vm.doArray.splice(event.newIndex, 0, vm.todoArray[]
                }
            });
            setTimeout(function(){
                vm.fordata()
            },200)
        },
        methods:{
            findlistMess(mes){
                log(mes)
            },
            MenuClick(event){
                this.MenuNum = event
            },
            fordata(){
                let vm = this;
                for(var i=0;i<vm.list.length;i++){
                    if(vm.list[i].type!='02'){
                        Sortable.create((document.getElementById(i+'pageitemList')), {
                            group: {
                                name: 'list',
                                pull: true
                            },
                            animation: 120,
                            //			            ghostClass: 'placeholder-style',
                            //			            fallbackClass: 'iview-admin-cloned-item',
                            onRemove (event) {
                                log(event.item.getAttribute('data-index'))
                                log(event)
                                //              vm.doArray.splice(event.newIndex, 0, vm.todoArray[event.item.getAttribute('data-index')]);
                                //          	vm.doArray.splice(event.newIndex, 0, vm.todoArray[]
                            }
                        });
                    }
                }
            }
        }
    }
</script>

<style>
</style>
