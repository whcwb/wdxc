<template>
      <div>
            <Modal :value="true" width="380" :mask-closable="false"
                   :closable="false" :footer-hide="true"
                   class-name="vertical-center-modal">
                  <div slot="header"
                       style="color:#f60;text-align:center;font-size: 18px;font-weight: 600;position: relative">
                        <Icon type="ios-information-circle" size="24"></Icon>
                        故障诊断
                        <div @click="close" style="position: absolute;right: -10px;top: -5px;cursor: pointer">
                              <Icon type="md-close-circle" size="24"/>
                        </div>
                  </div>
                  <div :style="{'textAlign':'center','maxHeight':getWindowHeight()-260+'px'}">
                        <div v-if="carZjMess==null">

                        </div>
                        <Row v-else v-for="(val,key) of carZjMess">
                            <Col span="24">
                                  {{val}}
                            </Col>
                        </Row>
                        <!--<Row type="flex" align="middle" :gutter="5" style="padding: 6px 0">-->
                              <!--<Col span="4">-->
                                    <!--<i style="font-size: 18px;color: #2b85e4" class="iconfont icon-xinxinicon"></i>-->
                              <!--</Col>-->
                              <!--<Col span="14" align="left" class-name="ColMess">发动机系统</Col>-->
                              <!--&lt;!&ndash;<Col span="6" align="center" class-name="ColMess">扫描结果</Col>&ndash;&gt;-->
                              <!--<Col span="6" align="center" class-name="ColMess">-->
                                    <!--<Icon class="loadingTif" size="44" type="ios-cog"/>-->
                              <!--</Col>-->
                        <!--</Row>-->
                        <!--<Row type="flex" align="middle" :gutter="5" style="padding: 6px 0">-->
                              <!--<Col span="4">-->
                                    <!--<i style="font-size: 28px;color: #2b85e4" class="iconfont icon-chachexitong"></i>-->
                              <!--</Col>-->
                              <!--<Col span="14" align="left" class-name="ColMess">自动变速箱系统</Col>-->
                              <!--&lt;!&ndash;<Col span="6" align="center" class-name="ColMess">扫描结果</Col>&ndash;&gt;-->
                              <!--<Col span="6" align="center" class-name="ColMess">-->
                                    <!--<Icon class="loadingTif" size="44" type="ios-cog"/>-->
                              <!--</Col>-->
                        <!--</Row>-->
                        <!--<Row type="flex" align="middle" :gutter="5" style="padding: 0">-->
                              <!--<Col span="4">-->
                                    <!--<i style="font-size: 32px;color: #2b85e4" class="iconfont icon-fangxiangpansuo"></i>-->
                              <!--</Col>-->
                              <!--<Col span="14" align="left" class-name="ColMess">刹车系统</Col>-->
                              <!--&lt;!&ndash;<Col span="6" align="center" class-name="ColMess">扫描结果</Col>&ndash;&gt;-->
                              <!--<Col span="6" align="center" class-name="ColMess">-->
                                    <!--<Icon class="loadingTif" size="44" type="ios-cog"/>-->
                              <!--</Col>-->
                        <!--</Row>-->
                        <!--<Row type="flex" align="middle" :gutter="5" style="padding: 0">-->
                              <!--<Col span="4">-->
                                    <!--<i style="font-size: 32px;color: #2b85e4"-->
                                       <!--class="iconfont icon-anquanqinangheanquandai"></i>-->
                              <!--</Col>-->
                              <!--<Col span="14" align="left" class-name="ColMess">安全气囊</Col>-->
                              <!--&lt;!&ndash;<Col span="6" align="center" class-name="ColMess">扫描结果</Col>&ndash;&gt;-->
                              <!--<Col span="6" align="center" class-name="ColMess">-->
                                    <!--<Icon class="loadingTif" size="44" type="ios-cog"/>-->
                              <!--</Col>-->
                        <!--</Row>-->
                        <!--<Row type="flex" align="middle" :gutter="5" style="padding: 5px 0">-->
                              <!--<Col span="4">-->
                                    <!--<i style="font-size: 28px;color: #2b85e4" class="iconfont icon-shujushuniugang"></i>-->
                              <!--</Col>-->
                              <!--<Col span="14" align="left" class-name="ColMess">数据流分析</Col>-->
                              <!--&lt;!&ndash;<Col span="6" align="center" class-name="ColMess">扫描结果</Col>&ndash;&gt;-->
                              <!--<Col span="6" align="center" class-name="ColMess">-->
                                    <!--<Icon class="loadingTif" size="44" type="ios-cog"/>-->
                              <!--</Col>-->
                        <!--</Row>-->

                  </div>
                  <!--<div class="loadTif" v-if="loadModal">-->
                  <!--<i-circle :percent="circleNum" :size="550" :stroke-color="circleNum==100?'#5cb85c':'#2d8cf0'">-->
                  <!--<span class="demo-Circle-inner"  v-if="circleNum==100"-->
                  <!--style="font-weight:600;font-size:60px;color: #5cb85c;text-align: center">-->
                  <!--<Icon type="ios-checkmark" size="300" style="color:#5cb85c"></Icon>-->
                  <!--</span>-->
                  <!--<span v-else class="demo-Circle-inner" style="font-weight:600;font-size:60px;color: #2b85e4">-->
                  <!--{{circleNum}}%-->
                  <!--</span>-->
                  <!--</i-circle>-->
                  <!--</div>-->
            </Modal>
      </div>
</template>

<script>
    import mixins from '@/mixins'
    export default {
        name: "ZL",
        props: {
            zdbh: {
                type: String,
                default: ''
            }
        },
        mixins: [mixins],
        data() {
            return {
                carZjMess:null,
                loadModal: true,
                circleNum: 90,
                inster: setInterval(() => {
                    this.circleNum++
                }, 100)
            }
        },
        // computed: {
        //     zjMess:function(){
        //         console.log(this.name);
        //         return this.$parent.zjMess
        //     }
        // },
        watch: {
            // zjMess:function (n,o) {
            //     console.log('车辆自检数据****',n);
            // }
        },
        created() {
            this.testCar((res)=>{
                  if(this.$parent.$parent.zjMess!= null){
                        this.carZjMess =JSON.parse(this.$parent.$parent.zjMess.body)
                  }else {
                      this.swal({
                          title: '车辆检测失败！！！',
                          type: 'error',
                      }).then(val => {
                          if (val.value) {
                              this.close()
                          }
                      })
                  }
            })
        },
        watch: {
            circleNum: function (n, o) {
                if (n == 100) {
                    clearInterval(this.inster)
                    setTimeout(() => {
                        this.loadModal = false
                    }, 500)
                }
            }
        },

        methods: {
            close() {
                this.$parent.compName = ''
            },
            testCar(callback) {
                this.$http.post('/api/zdgl/check', {zdbh: this.zdbh}).then(res => {
                    if (res.code == 200) {
                        console.log('指令发送成功');
                        callback && callback()
                    } else {
                        this.swal({
                            title: res.message,
                            type: 'error',
                        }).then(val => {
                            if (val.value) {
                                this.close()
                            }
                        })
                    }
                }).catch(err => {
                })
            },

        }
    }
</script>

<style lang="less">
      .ColMess {
            font-size: 16px;
            color: #999999;
            font-weight: 600;
      }

      .loadTif {
            background-color: rgba(255, 255, 255, 0);
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            div {
                  top: 50%;
                  left: 50%;
                  transform: translate(-50%, -50%);
            }
      }

      .loadingTif {
            animation: loading 1s linear infinite;
      }

      @keyframes loading {
            from {
                  transform: rotate(0deg);
            }
            50% {
                  transform: rotate(180deg);
            }
            to {
                  transform: rotate(360deg);
            }
      }
</style>