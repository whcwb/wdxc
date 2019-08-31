<template>
    <div>
        <Row type="flex" justify="center" align="middle">
            <Col span="24">
                <search-bar :parent="v" :show-create-button="false" :show-search-button="false"></search-bar>
                <Button type="primary" @click="util.getPageData(v)">
                    查询
                </Button>
            </Col>
        </Row>
        <Row type="flex" justify="center" align="middle">
            <Col span="24">
                <table-area :parent="v"></table-area>
            </Col>
        </Row>
    </div>
</template>

<script>
    export default {
        name: "gpsTable",

        data(){
            return{
                v:this,
                pagerUrl:"/pub/gpsLsPager",
                tableColumns:[
                    {title: '#', type:'index',width:40,fixed:'left'},
                    {title: '时间', key:'cjsj',searchType:'daterange',minWidth:100},
                    {title: '经度', key:'bdjd',minWidth:100},
                    {title: '纬度', key:'bdwd',minWidth:100},
                    {title: '方向角', key:'fxj',minWidth:100},
                    {title: '运行速度', key:'yxsd',minWidth:100},
                    {title: '卫星个数', key:'starNum',minWidth:100},
                    {title:'GSM',key:'gsm',minWidth:100},
                    {title: '终端编号', key:'zdbh',searchKey:'zdbhLike',fixed:"right",minWidth:100},

                ],
                pageData: [],
                param:{
                    total:0,
                    pageSize:20,
                    pageNum:1
                },
                dateRange: {
                    cjsj: ''
                },
                tableLoading:false
            }
        },
        created(){
            let today = new Date();
            today = today.format("yyyy-MM-dd")
            this.dateRange.cjsj = [today + ' 00:00:00', today + ' 23:59:59']
            this.param.cjsjInRange = today + ' 00:00:00' + ',' + today + ' 23:59:59'

            this.util.initPageSize(this);
            this.util.initTableHeight(this);
            this.util.fillTableColumns(this)
        },
        methods:{
            afterPager(list){
                for(let r of list){
                    r.cjsj = r.cjsj.substring(6)
                }
                this.tableLoading = false
            },
        }
    }
</script>

<style scoped>

</style>
