<template>
      <div id="main" class="app-main">
            <router-view></router-view>
            <div v-if="SpinShow" style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 9999;">
                  <!--<Spin fix size="large"></Spin>-->
                  <Spin fix>
                        <Icon type="ios-loading" size=40 class="demo-spin-icon-load"></Icon>
                        <div style="font-size: 30px">Loading</div>
                  </Spin>
                  <!--<Spin fix>-->
                  <!--<Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>-->
                  <!--<div style="font-size: 30px;">数据加载中...</div>-->
                  <!--</Spin>-->
            </div>
      </div>
</template>

<script>
    export default {
        data() {
            return {
                SpinShow: false,
                theme: this.$store.state.app.themeColor
            };
        },
        computed: {
            loading() {
                return this.$store.state.app.loadingType;
            }
        },
        watch: {
            loading: function (newQuestion, oldQuestion) {
                this.SpinShow = newQuestion
                if (newQuestion == true) {
                    setTimeout(() => {
                        this.$store.commit('CloadingType', false)
                    }, 500)
                }
            }
        },
        mounted() {

        },
        beforeDestroy() {

        },
        methods: {}
    };
</script>

<style>

      /*@import "//at.alicdn.com/t/font_683602_e7rww0681ms.css";*/
      @import '//at.alicdn.com/t/font_683602_l7p6btqaoy.css';
      @import "//at.alicdn.com/t/font_723126_nmaw2zldou8.css";
      html, body {
            width: 100%;
            height: 100%;
            background: #f0f0f0;
            overflow: hidden;
      }

      .app-main {
            width: 100%;
            height: 100%;
      }

      /* 设置滚动条的样式 */
      ::-webkit-scrollbar {
            width: 8px;
            height: 6px;
      }

      /* 滚动槽 */
      ::-webkit-scrollbar-track {
            -webkit-box-shadow: inset006pxrgba(0, 0, 0, 0.3);
            border-radius: 8px;
      }

      /* 滚动条滑块 */
      ::-webkit-scrollbar-thumb {
            border-radius: 4px;
            background: rgba(0, 0, 0, 0.1);
            -webkit-box-shadow: inset006pxrgba(0, 0, 0, 0.5);
      }

      /*::-webkit-scrollbar-thumb:window-inactive {*/
      /*background:rgba(255,0,0,0.4);*/
      /*}*/
</style>
