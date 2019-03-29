<style lang="less">
      @import '../../../../../styles/common.less';

      .showmapline {
            height: 260px;
            .showmap {
                  height: 100%;
            }
            .showline {
                  height: 100%;
            }
      }

      .stepsList {
            padding-top: 25px;
            .ivu-steps-head {
                  padding-left: 0 !important;
                  background-color: #ffffff00;
            }
            .ivu-steps.ivu-steps-small .ivu-steps-content {
                  padding-left: 0px;
                  transform: translate(-5px,5px);
                  width: 20px !important;
                  font-size: 18px;
            }
            .ivu-steps.ivu-steps-small .ivu-steps-head-inner > .ivu-steps-icon.ivu-icon {
                  font-size: 24px !important;
            }
      }
</style>
<template>
      <div>
            <div>
                  <Button type="success" @click="changeS=true">地图查看</Button>
                  <Button type="info" @click="changeS=false">线路查看</Button>
            </div>
            <div class="showmapline">
                  <div class="showmap" v-if="changeS">
                        <map-show-line :xlid="xlId"></map-show-line>
                  </div>
                  <div class="showline stepsList" v-else>
                        <Steps :current="stationList.length" size="small">
                              <Step v-show="item.mc.indexOf('辅助点')==-1" icon="ios-disc" :content="item.mc" v-for="(item,index) in stationList"></Step>
                        </Steps>
                        <!--<Steps :current="stationList.length">-->
                        <!--<Step title="注册" icon="disc"></Step>-->
                        <!--<Step title="上传头像"></Step>-->
                        <!--<Step title="验证邮箱"></Step>-->
                        <!--</Steps>-->

                  </div>
            </div>
      </div>
</template>
<script>
    import mapShowLine from '../../../map/mapShowLine.vue'

    export default {
        name: '',
        components: {
            mapShowLine,
        },
        data() {
            return {
                changeS: true,
                xlId: 'qweqweqwe',
                stationList: []
            }
        },
        props: {
            row: Object
        },
        created() {
            this.xlId = this.row.id;
        },
        mounted() {
            this.getStations();
        },
        methods: {
            getStations() {
                this.$http.get(this.apis.ZD.GET_BY_ROUTE_ID, {params: {xlId: this.xlId}}).then((res) => {
                    if (res.code === 200) {
                        this.stationList = res.result;
                    }
                })
            }
        }
    };
</script>
