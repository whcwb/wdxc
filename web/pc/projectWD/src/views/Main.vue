<style lang="less">
      @import "./main.less";
      @import "../styles/common";
</style>
<template>
      <div class="main" :class="{'main-hide-text': shrink}">
            <div class="sidebar-menu-con"
                 :style="{width: shrink?'60px':'200px', overflow: shrink ? 'visible' : 'auto'}">
                  <!--:theme="menuTheme"-->
                  <shrinkable-menu
                          :shrink="shrink"
                          @on-change="handleSubmenuChange"
                          :before-push="beforePush"
                          :open-names="openedSubmenuArr"
                          :menu-list="menuList">
                        <div slot="top" class="logo-con">
                              <div v-show="!shrink"
                                   style="color: white;font-size: 9pt;background-color: rgb(45, 140, 240);border-radius: 10px;padding: 10px;text-align: center">
                                    车辆管理平台
                              </div>
                              <div v-show="shrink"
                                   style="color: white;font-size: 9pt;background-color: rgb(45, 140, 240);border-radius: 10px;padding: 10px;text-align: center">
                                    车
                              </div>
                              <!--<img v-show="!shrink"  src="../images/logo.png" key="max-logo" />-->
                              <!--<img v-show="shrink" src="../images/logo-min.png" key="min-logo" />-->
                        </div>
                  </shrinkable-menu>
            </div>
            <div class="main-header-con" :style="{paddingLeft: shrink?'60px':'200px'}">
                  <div class="main-header box-row" style="height: 65px;">
                        <div class="navicon-con">
                              <Button :style="{transform: 'rotateZ(' + (this.shrink ? '-90' : '0') + 'deg)'}"
                                      type="text" @click="toggleClick">
                                    <Icon type="ios-menu" size="32"></Icon>
                              </Button>
                        </div>
                        <!--<div class="header-middle-con" style="background-color: #00cc66;width: 350px">-->
                        <div class="" style="line-height: 40px;width: 350px">
                              <div class="main-breadcrumb">
                                    <breadcrumb-nav :currentPath="currentPath"></breadcrumb-nav>
                              </div>
                        </div>
                        <div class="body-O" style="line-height: 65px;padding: 0 20px">
                              <marquee v-if="showTip" behavior="scroll" direction="left" align="middle"
                                       scrolldelay="120"
                                       style="font-size: 18px">
                                    最近5分钟之内：
                                    <span v-if="ycMess.length==0">暂无异常数据</span>
                                    <span v-else v-for="(item,index) in ycMess" style="margin-right: 12px">
                               [{{item.cph}}]{{item.sjxm}} {{getDictVal(item.sjlx)}} {{item.cjsj.substring(11)}}
                              </span>
                              </marquee>

                        </div>

                        <div style="line-height: 65px;padding: 0 8px">
                              <Tooltip content="更多异常信息">
                                    <Button type="primary"
                                            size="small" icon="logo-buffer"
                                            @click="compName='errmess'"></Button>
                              </Tooltip>
                        </div>
                        <!--<div class="header-avator-con" style="background-color: #00cc66">-->
                              <Language></Language>
                        <div class="">
                              <div class="user-dropdown-menu-con" style="height: 100%;line-height:65px">
                                    <Row type="flex" justify="end" align="middle" class="user-dropdown-innercon">
                            <span style="margin-right: 30px;">
                            	<span style="font-size: 18px;">
                            		<b>
                            			欢迎
                            		</b>
                            	</span>
                                <span class="main-user-name">{{ userName }}</span>
                            </span>
                                          <Button type="primary" shape="circle" icon="md-key" @click="person"
                                                  style="margin-right: 8px;"></Button>
                                          <Button
                                                  size="large"
                                                  type="primary"
                                                  shape="circle"
                                                  @click="handleClickUserDropdown"
                                                  @DOMMouseScroll="ButOnmouseover('移入')"
                                                  @mousewheel="ButOnmouseover('移出')">
                                                <span>退出登陆</span>
                                                <!--<Icon type="ios-redo"></Icon>-->
                                          </Button>
                                    </Row>
                              </div>
                        </div>
                  </div>
                  <div class="tags-con">
                        <tags-page-opened :pageTagsList="pageTagsList"></tags-page-opened>
                  </div>
            </div>
            <div class="single-page-con" :style="{left: shrink?'60px':'200px'}">
                  <div class="single-page" style="height: 100%;">
                        <keep-alive :include="cachePage" style="position: relative;height: 100%;">
                              <router-view></router-view>
                        </keep-alive>
                  </div>
            </div>
            <component
                    :is="compName"></component>
      </div>
</template>
<script>
    import shrinkableMenu from './main-components/shrinkable-menu/shrinkable-menu.vue';
    import tagsPageOpened from './main-components/tags-page-opened.vue';
    import breadcrumbNav from './main-components/breadcrumb-nav.vue';
    import fullScreen from './main-components/fullscreen.vue';
    import lockScreen from './main-components/lockscreen/lockscreen.vue';
    import messageTip from './main-components/message-tip.vue';
    import themeSwitch from './main-components/theme-switch/theme-switch.vue';

    import Language from './components/language'

    import Cookies from 'js-cookie';
    import util from '@/libs/util.js';

    Date.prototype.format = function (format) {
        var o = {
            "M+": this.getMonth() + 1, //month
            "d+": this.getDate(),    //day
            "h+": this.getHours(),   //hour
            "m+": this.getMinutes(), //minute
            "s+": this.getSeconds(), //second
            "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
            "S": this.getMilliseconds() //millisecond
        }
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length == 1 ? o[k] :
                    ("00" + o[k]).substr(("" + o[k]).length));
        return format;
    }

    import pass from './passworld'
    import errmess from './components/mainAbnormess'

    export default {
        components: {
            Language,
            pass, errmess,
            shrinkableMenu,
            tagsPageOpened,
            breadcrumbNav,
            fullScreen,
            lockScreen,
            messageTip,
            themeSwitch
        },
        filters: {
            sjjb: (val) => {
                // switch (val){
                //     case '10':
                //         return '急加速'
                //     break;
                //     case '20':
                //         return '急刹车'
                //     break;
                //     case '30':
                //         return '急转弯'
                //     break;
                //     default:
                //         return '正常'
                // }
                let mes = v.dictUtil.getValByCode(v, 'ZDCLK0038', val)
                return mes

            }
        },
        data() {
            return {
                showTip: false,
                v: this,
                compName: '',
//				socket : '',
                scoketMess: [],
                scoketAllCar: [],

                shrink: false,
                userName: '',
                isFullScreen: false,
                openedSubmenuArr: this.$store.state.app.openedSubmenuArr,
                ycMess: []
            };
        },
        computed: {
            GetscoketMess() {
                return this.$store.state.app.socketMess
            },
            GetscoketAllCar() {
                return this.$store.state.app.socketAllCar
            },
            menuList() {
                return this.$store.state.app.menuList;
            },
            pageTagsList() {
                return this.$store.state.app.pageOpenedList; // 打开的页面的页面对象
            },
            currentPath() {
                return this.$store.state.app.currentPath; // 当前面包屑数组
            },
            avatorPath() {
                return localStorage.avatorImgPath;
            },
            cachePage() {
                return this.$store.state.app.cachePage;
            },
            lang() {
                return this.$store.state.app.lang;
            },
            menuTheme() {
                return []
            },
            mesCount() {
                return this.$store.state.app.messageCount;
            }
        },
        watch: {
            '$route'(to) {
                this.$store.commit('setCurrentPageName', to.name);
                this.checkTag(to.name);
                localStorage.currentPageName = to.name;
            },
            GetscoketMess: function (newQuestion, oldQuestion) {
                this.scoketMess = newQuestion
            },
            GetscoketAllCar: function (newQuestion, oldQuestion) {
                this.scoketAllCar = newQuestion
            }
        },
        mounted() {
            alert('1')
            this.init();
            this.checkSubscribe()
            // 设置初始语言
            console.log('语言', this.$i18n.locale);
            this.$store.commit('setLocal', this.$i18n.locale)
        },
        created() {
            // 显示打开的页面的列表
            this.$store.commit('setOpenedList');
            this.gdTxt()
            let v = this;
            setInterval(function () {
                v.gdTxt()
            }, 1000 * 60 * 5)
        },
        methods: {
            getDictVal(c) {
                let mes = this.dictUtil.getValByCode(this, 'ZDCLK0038', c)
                return mes
            },
            person() {
                this.compName = 'pass'
            },
            checkSubscribe() {
                this.websocketUtil.connect(() => this.subscribe());
            },
            subscribe() {
                let v = this;
                this.websocketUtil.subscribe('/topic/sendhbsp', function (data) { //订阅消息
                    v.$store.commit('addSendhbsp', data.body)
                });
                this.websocketUtil.subscribe('/topic/sendzp', function (data) { //订阅
                    v.$store.commit('addSendZp', data.body)
                });
                this.websocketUtil.subscribe('/topic/sendsp', function (data) { //订阅消息
                    v.$store.commit('addSendsp', data.body)
                });
            },
            ButOnmouseover(mes) {
            },
            init() {
                this.$store.commit('updateMenulist');
                let userInfo = JSON.parse(sessionStorage.getItem('userInfo'));
                this.userName = userInfo.xm;
                let messageCount = 3;
                this.messageCount = messageCount.toString();
                this.checkTag(this.$route.name);
                this.$store.commit('setMessageCount', 3);
            },
            toggleClick() {
                this.shrink = !this.shrink;
            },
            handleClickUserDropdown(name) {
                // 退出登录
                Cookies.set('usermess', '');
                this.$store.commit('logout', this);
                this.$store.commit('clearOpenedSubmenu');
                this.$store.commit('clearAllTags');//关闭多页面操作
                this.$router.push({
                    name: 'login'
                });
//              }
            },
            checkTag(name) {
//              let openpageHasTag = this.pageTagsList.some(item => {
//                  if (item.name === name) {
//                      return true;
//                  }
//              });
//              if (!openpageHasTag) { //  解决关闭当前标签后再点击回退按钮会退到当前页时没有标签的问题
//                  util.openNewPage(this, name, this.$route.params || {}, this.$route.query || {});
//              }
            },
            handleSubmenuChange(val) {
//                 log('路由',val)
            },
            beforePush(name) {
                return true;
            },
            fullscreenChange(isFullScreen) {
            },
            gdTxt() {
                this.ycMess = [];
                this.$http.post(this.apis.TXT, {minutes: '5', sjlxIn: '10,20,30,40,70'}).then((res) => {
                    if (res.code === 200) {
                        this.showTip = true;
                        this.ycMess = res.page.list
                    }
                }).catch((err) => {

                })
            }
        }
    };
</script>
