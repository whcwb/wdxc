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
        name: "sjjlTable",
        data(){
            return{
                v:this,
                pagerUrl:"/pub/sjjlPager",
                tableColumns:[
                    {title: '#', type:'index',width:30},
                    {title: '终端编号', key:'zdbh',searchKey:'zdbhLike'},
                    {title: '时间', key:'cjsj',searchType:'daterange'},
                    {title: '事件', key:'eventName'},
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
                eventMap:{
                    "00":"正常",
                    "10":"急加速",
                    "20":"急刹车",
                    "30":"急转弯",
                    "40":"超速",
                    "50":"点火",
                    "60":"熄火",
                    "70":"不在电子围栏",
                    "31":"急左转弯",
                    "32":"急右转弯",
                    "33":"急左变道",
                    "34":"急右变道",
                }
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
                for (let r of list){
                    r.eventName = this.eventMap[r.sjlx]
                }
            },
        }
    }
</script>

<style scoped>

</style>
