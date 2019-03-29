<template>
  <div>
    <Row style="position: relative;margin-top: 10px;">
      <Table
        ref="table"
        :loading="parent.tableLoading"
        :stripe="true"
        :border="false"
        :size='sizeTyp'
        :height="TabHeight"
        :columns="parent.tableColumns"
        :data="parent.pageData"></Table>
    </Row>
    <div v-if="pager" style="text-align: right;padding: 6px 0">
      <Page :total=parent.param.total :current=parent.param.pageNum :page-size=parent.param.pageSize
            :page-size-opts=[8,10,20,30,40,50]
            @on-page-size-change='(e)=>{parent.param.pageSize=e;parent.util.getPageData(parent)}'
            show-total show-elevator show-sizer placement='top'
            @on-change='(e)=>parent.util.pageChange(parent,e)'></Page>
    </div>
  </div>
</template>

<script>
  export default {
    name: "tableArea",
    props: {
      sizeTyp:{
        type:String,
        default:'small'
      },
      parent: {
        type: Object,
        default: function () {
          return {};
        }
      },
      pager:{
        type:Boolean,
        default:true
      },
      TabHeight:{
        type:Number,
        default:650
      }
    },
    created(){
    },
    methods:{
      exportCsv(param){
        if (!param) param = {}
        this.$refs.table.exportCsv(param);
      }
    }
  }
</script>

<style scoped>

</style>
