<style lang="less">
    @import "../../../../styles/common.less";

    .VehicleMonitoringTiT {
        width: 225px;
        background-color: #fff;
        border-radius: 5px 5px 0 0;
        padding-left: 5px;
        .boxTiT {
            height: 80px;
            .cartypemess {
                text-align: center;
                /*line-height: 20px;*/
                .cartypebox {
                    margin-top: 12px;
                }
            }
        }
        .boxTiTnow {
            background-color: #0000CC;
            color: #fff;
            height: 55px;
        }
        .carlistmess {
            cursor: pointer;
            padding: 8px 5px 4px 5px;
            border-bottom: solid 2px #B3B3B3;
        }
    }

    .sugges {
        .ivu-card-body {
            padding: 0 !important;
        }
        .carlines {
            background-color: #fff;
            padding-top: 20px;
            margin: 0 15px;
            height: 100%;
            overflow: auto;
        }
    }

    .SSJKsty {
        overflow: auto;
    }

    .ycsjjlTable {
        /*height: 266px;*/
        width: 525px;
        position: absolute;
        right: 20px;
        top: 92px;
    }
</style>
<template>
    <div style="height: 100%;width: 100%;">
        <div class="box-row">
            <div class="body-r-5">
                <Card>
                    <p slot="title">
                        <Icon type="ios-film-outline"></Icon>
                        地图实时监控
                    </p>
                    <div :style="mapheight">
                        <my-map></my-map>
                    </div>
                    <!--<Card class="ycsjjlTable">-->
                        <!--<p slot="title">-->
                            <!--<Icon type="ios-film-outline"></Icon>-->
                            <!--最近5分钟异常行驶记录-->
                        <!--</p>-->
                        <!--<Button type="primary" size="small" :icon="changeBtnIcon" slot="extra" style="margin:5px" @click.native="changeBtn"></Button>-->
                        <!--<div class="a" style=" " v-show="tabShowFlag">-->
                            <!--<abnor style="height: 256px"></abnor>-->
                        <!--</div>-->
                    <!--</Card>-->
                </Card>
            </div>
        </div>
    </div>
</template>

<script>

    import myMap from '../../map/schoolmap.vue'
    import gauge from './comp/gauge.vue'
    import abnor from './comp/abnormal.vue'
    import carline from './comp/travelist.vue'


    import mixins from '@/mixins'

    export default {
        name: 'VehicleMonitoring',
        components: {
            myMap, gauge, abnor, carline
        },
        mixins: [mixins],
        data() {
            return {
                mapheight:{ height:'' },
                changeBtnIcon:'chevron-down',
                tabShowFlag:false
            };
        },
        created() {
            this.$store.commit('setCurrentPath', [{
                title: '首页',
            }, {
                title: '运营监控',
            }, {
                title: '校巴监控',
            }])
        },
        mounted() {
            this.autoHeight()
        },
        methods: {
            changeBtn(){
                if (this.changeBtnIcon == "chevron-down"){
                    this.changeBtnIcon = "chevron-up";
                    this.tabShowFlag = true;
                }else{
                    this.changeBtnIcon = "chevron-down";
                    this.tabShowFlag = false;
                }
            },
            autoHeight() {
                var windowHeight = window.innerHeight
                // this.carheight.height = (windowHeight / 2 - 120) + 'px'
                // this.SSjk.height = (windowHeight / 2 - 120) + 'px'
//  		this.mapheight.height = (windowHeight/2 - 120)+'px'
                this.mapheight.height = (windowHeight - 210) + 'px'
            },
        }
    };
</script>
