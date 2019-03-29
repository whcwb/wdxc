<style lang="less">
      @import '../../../../styles/common.less';
</style>
<!--角色管理-->
<template>
      <div class="boxbackborder">
            <Card>
                  <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
				<span class="tabPageTit">
    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
    			</span>
                        <div style="height: 45px;line-height: 45px;">
                              <div class="margin-top-10 box-row">
                                    <div class="titmess">
                                          <span>报销记账</span>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <Input v-model="param.gnmcLike" placeholder="请输入功能名称" style="width: 200px"
                                                 @on-change="getPageData()"></Input>
                                    </div>
                                    <div class="butevent">
                                          <Button type="primary" @click="v.util.getPageData(v)">
                                                <Icon type="md-search"></Icon>
                                          </Button>
                                          <Button type="primary" @click="v.util.add(v)">
                                                <Icon type="md-add"></Icon>
                                          </Button>
                                    </div>
                              </div>
                        </div>
                  </Row>
                  <Row style="position: relative;">
                        <Table ref="table"  :height="tabHeight" :row-class-name="rowClassName" :columns="tableTitle"
                               :data="pageData"></Table>
                  </Row>
                  <Row class="margin-top-10 pageSty">
                        <Page :total=param.total :current=param.pageNum :page-size=param.pageSize
                              :page-size-opts=[8,10,20,30,40,50]
                              @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}' show-total show-elevator
                              show-sizer placement='top'
                              @on-change='pageChange'></Page>
                  </Row>
            </Card>
            <component :is="componentName"></component>
      </div>
</template>

<script>
    import mixins from '@/mixins'
    import formData from './comp/formData.vue'

    export default {
        name: 'char',
        mixins: [mixins],
        components: {
            formData
        },
        data() {
            return {
                v: this,
                SpinShow: true,
                apiRoot: this.apis.BXJZ,
                tabHeight: 220,
                componentName: '',
                choosedItem: null,
                tableTitle: [
                    {title: "序号", width: 80, align: 'center', type: 'index'},
                    {title: '报销人', align: 'center', key: 'bxr'},
                    {title: '报销事项', align: 'center', key: 'bxsx'},
                    {title: '报销时间', align: 'center', key: 'bxsj'},
                    {title: '报销金额', align: 'center', key: 'bxje'},
                    {title: '发票数量', align: 'center', key: 'fpsl'}
                ],
                pageData: [],
                param: {
                    gnmcLike: '',
                    total: 0,
                    pageNum: 1,
                    pageSize: 8,
                },
            }
        },
        created() {
            this.$store.commit('setCurrentPath', [{title: '首页',}, {title: '系统管理',}, {title: '功能管理',}])
            this.util.initTable(this)
        },
        methods: {
            pageChange(event) {
                this.util.pageChange(this, event);
            },
        }
    }
</script>
