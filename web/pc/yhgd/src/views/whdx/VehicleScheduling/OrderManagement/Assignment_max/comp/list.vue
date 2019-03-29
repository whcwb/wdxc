<template>
    <div>
        <Modal
                v-model="showModal"
                width="700"
                :closable='false'
                :mask-closable="false"
                :title="'正在为 '+mess.xm+' 分配订单'">
            <div style="height: 400px">
                <Table ref="table"  border
                       height="400"
                       :columns="columns4"
                       :data="data1"
                       @on-selection-change="selectionClick"></Table>
            </div>
            <div slot='footer'>
                <Button type="default" @click="colse"style="color: #949494">取消</Button>
                <Button type="primary" @click="add()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>


    export default {
        name:'',
        data(){
            return {
                showModal:true,
                drveName:'hello',columns4: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '用车人',
                        key: 'ck',
                        align: 'center'
                    },
                    {
                        title: '用车时间',
                        key: 'yysj',
                        align: 'center'
                    },
                    {
                        title: '候车地点',
                        key: 'hcdz',
                        align: 'center'
                    },
                    {
                        title: '目的地',
                        key: 'mdd',
                        align: 'center'
                    },
                    {
                        title: '载客数',
                        key: 'zws',
                        align: 'center'
                    }
                ],
                data1: [
                ],
                seleList:[]

            }
        },
        props:{
          mess:{
              type:Object,
              default:{}
          },
           type:{
              type:String,
               default:'2030'
           }
        },
        created(){
            this.getDfp()
        },
        methods:{
            getDfp(){
                var v = this
                this.$http.post(this.apis.ORDER.DFP,{'cllx':2030,'zkl':this.mess.zkl}).then((res) =>{
                    if(res.code ==200){
                        log('******************',res)
                        v.data1 = res.result

                    }
                })
            },
            add(){
                if(this.seleList.length==0){

                }else{
                    var v = this
                    let listLth = this.seleList.length
                    for( let i = 0 ; i<listLth ;i++){
                        v.addlist(this.seleList[i].id)
                    }
                }
            },
            addlist(listID){
                let sjSx = '10'
                if(this.type==2030){
                    sjSx =10
                }else if(this.type==40){
                    sjSx =11
                }
                var v = this
                this.$http.post(this.apis.ORDER.FP,{'sjSx':sjSx,'sj':this.mess.sfzhm,'id':listID,'cph':this.mess.cph}).then((res) =>{
                    if(res.code===200){
                        v.$Message.success(res.message);
                        v.$parent.getDrvList()
                        v.$parent.pdtj()
                        v.colse()
                    }else{
                        v.$Message.error(res.message);
                    }
                }).catch((error) =>{
                    v.$Message.error('出错了！！！');
                })
            },
            colse(){
                var v = this
                v.$parent.compName = ''
            },
            selectionClick(event){
                this.seleList = event
                log('*************************',event)
            }
        }
    }
    //15271928827
</script>

<style>
</style>
