<style lang="less">
    @import "../../styles/common.less";
    #abnormal{
    }
</style>
<template>
    <div id="abnormal" style="height: 100%;">
        <Table ref="table"
                border
                :height="tabHeight"
                :columns="columns1"
                :data="tabmess"></Table>
    </div>
</template>
<script>
    import mixins from '@/mixins'

    export default {
        name:'',
        mixins: [mixins],
        data () {
            return {
                tabHeight:'220',
                columns1: [
                    {
                        title: '序号',
                        type: 'index',
                        width: 80,
                        align: 'center'
                    },
                    {
                        title: '异常事件',
                        key: 'sjlx',
                        align:'center',
                        render: (h, params) => {
                            let val = this.dictUtil.getValByCode(this,this.sjBM,params.row.sjlx)
                            return h('div',val)
                        }
                    },
                    {
                        title: '车辆编号',
                        key: 'cph',
                        align: 'center'
                    },
                    {
                        title: '驾驶员',
                        key: 'sjxm',
                        align: 'center'
                    },
                    {
                        title: '事发时间',
                        key: 'cjsj',
                        align: 'center'
                    }
                ],
                tabmess:[],
                sjBM:'ZDCLK0038',
                sjDIC:[],
                param:{
                    minutes:5,
                }
            }
        },
//      props:{
//          tabmess:{
//              type:Array,
//              default:[]
//          }
//      },
        created(){
        	this.getmess()
        },
        mounted(){
            this.getalert()
        },
        methods:{
                getmess(){
                        var v = this
                        this.$http.get(this.apis.CLSBYXJL.QUERY,{params:v.param}).then((res) =>{
                                console.log('异常数据',res)
                                v.tabmess = res.page.list
                        })
                },
                getalert(){
                    var windowHeight = window.innerHeight
                    this.tabHeight = windowHeight/2 - 130
    //				this.tabHeight = windowHeight - 200
                    log('浏览器高',this.tabHeight)
                }
        }
    }
</script>
