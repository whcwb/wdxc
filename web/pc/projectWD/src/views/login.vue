<style lang="less">
      @import '../styles/common.less';
      @import './login.less';

      .login {
            .imgLeft {
                  position: relative;
                  .loginImg {
                        width: 90%;
                        height: 260px;
                        position: absolute;
                        /*top:50px;*/
                        border-radius: 50px;
                  }
            }
            .from {
                  .loginTiT {
                        text-align: center;

                  }
                  .fromList {
                        padding-top: 20px;
                  }
            }

      }
</style>

<template>
      <div class="login" style="overflow-y: auto" @keydown.enter="handleSubmit">
            <div class="loginTit" style="background-color: #495060">
                  <div class="text" style="left: 38%;cursor: pointer" @click="changeTo">
                        <img src="static/icon/xq.png"
                        style="vertical-align: middle;padding-right: 5px;padding-bottom: 3px">
                        <span style="padding-top: 3px">平台简介</span>
                  </div>
            </div>
            <div class="loginForm">
                  <div class="login-con">
                        <Card :bordered="false" style="width: 100%;height:310px;">
                              <div class="form-con">
                                    <div class="body-O from">
                                          <div class="loginTiT">
                                                <h1>
                                                      {{$t("CAR_MANAGE")}}
                                                </h1>
                                          </div>
                                          <Form ref="loginForm" :model="form" :rules="rules">
                                                <div class="fromList">
                                                      <FormItem prop="username">
                                                            <Input v-model="form.username" placeholder="请输入用户名">
                                                            <span slot="prepend">
                  <Icon :size="16" type="md-person"></Icon>
                  </span>
                                                            </Input>
                                                      </FormItem>
                                                </div>
                                                <div class="fromList">
                                                      <FormItem prop="password">
                                                            <Input type="password" v-model="form.password"
                                                                   placeholder="请输入密码">
                                                            <span slot="prepend">
                  <Icon :size="14" type="md-key"></Icon>
                  </span>
                                                            </Input>
                                                      </FormItem>
                                                </div>
                                                <FormItem>
                                                      <Button @click="handleSubmit" type="primary" long>登录</Button>
                                                </FormItem>
                                          </Form>
                                    </div>
                              </div>
                        </Card>
                  </div>

            </div>
            <div class="downApp">
                  <img class="iosImg" src="/static/img/ios.png" alt="">
                  <img class="androidImg" src="/static/img/android.png" alt="">
            </div>
            <div class="bottomIcon">

            </div>
            <div class="copyRight bgImg"></div>

      </div>
</template>

<script>
    import Cookies from 'js-cookie';
    import menuList from '../data/list'
    import {appRouter} from '../router/router';

    export default {
        data() {
            return {
                SpinShow: false,
                form: {
                    username: '',
                    password: ''
                },
                menus: [],
                rules: {
                    username: [
                        {required: true, message: '账号不能为空', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '密码不能为空', trigger: 'blur'}
                    ]
                }
            };
        },
        created() {
            menuList.menuTree = [];
        },
        methods: {
            changeTo() {
                window.location.href = "http://www.168car.net/home";
            },
            handleSubmit() {
                var v = this
                this.$refs.loginForm.validate((valid) => {
                        if (valid) {
                            v.SpinShow = true
                            v.$http.post(this.apis.LOGIN.QUERY, this.form).then((res) => {
                                if (res.code === 200) {
                                    Cookies.set('usermess', this.form.username);
                                    Cookies.set('accessToken', res.result.accessToken);
                                    sessionStorage.setItem("userInfo", JSON.stringify(res.result.userInfo));
                                    v.initDict(res.result.dictList);
                                    v.getMenuTree(res.result.menuTree);
                                    v.SpinShow = false
                                }
                                else if (res.code === 500) {
                                    this.$Message.error(res.message);
                                    this.form.username = '';
                                    this.form.password = '';
                                } else {
                                    this.$Message.error("用户登陆失败，请重试！");
                                    this.form.username = '';
                                    this.form.password = '';
                                }
                                v.SpinShow = false
                            }).catch((error) => {
                                v.SpinShow = false
                                log('error', error)
                            })
                        }
                    }
                ),
                    setTimeout(() => {
                            v.SpinShow = false
                        },
                        500
                    )
            },
            getMenuTree(menuTree) {
                this.session.setItem('menuList', menuTree)
//                  menuList.menuTree = res.result;
                this.addToMenuList(menuTree);
                this.$router.push('home')
            },
            addToMenuList(list) {
                for (let r of list) {
                    menuList.menuList.push(r.name);
                    if (r.children) {
                        this.addToMenuList(r.children);
                    }
                }
            },
            getMenuList() {
                this.$http.get(this.apis.USERROOT.GET_MENU_LIST).then((res) => {
                    if (res.code === 200) {
                        menuList.menuList = res.result;
                        this.getMenuTree();
                    }
                }).catch((error) => {
                        log(error)
                    }
                )
            },
            addToList(list) {
                for (let r of list) {
                    this.menus.push(r);
                    if (r.children) {
                        for (let c of r.children) {
                            c.pid = r.name;
                        }
                        this.addToList(r.children);
                    }
                }
            },
            buildRouter(menuTree) {
                for (let r of menuTree) {
                    this.addRouterComponent(r);
                    this.router.push(r);
                }
            },
            addRouterComponent(node) {
                console.log('addRouterComponent');
                console.log(node);
                if (node.pid) {
                    console.log('f');
                    node.component = this.buildComponent();
                    console.log('children:' + node.children);
                    if (node.children && node.children.length != 0) {
                        for (let r of node.children) {
                            this.addRouterComponent(r);
                        }
                    }
                } else {
                    console.log('t');
                    node.component = Main;
                }
            },
            buildComponent(node) {
                return () =>
                    import('@/views/whdx/' + node.pid + "/" + node.name);
            },
            initDict(dictList) {
                let dictMap = new Map();
                for (let r of dictList) {
                    let a = [];
                    if (!r.zdxmList) continue
                    for (let e of r.zdxmList) {
                        a.push({key: e.zddm, val: e.zdmc});
                    }
                    dictMap.set(r.lmdm, a)
                }
                this.session.setItem('dictMap', dictMap)
            },
            initMenu() {
                this.addToList(appRouter, this.menus);
                for (let r of this.menus) {
                    delete r.children;
                    delete r.component;
                }

                let params = {menus: JSON.stringify(this.menus)}
                this.$http.post(this.apis.USERROOT.INIT_MENU, params).then((res) => {
                    if (res.code === 200) {
                        log(res);
                    }
                }).catch((error) => {
                        log(error)
                    }
                )
            }
        }
    };
</script>

<style>

</style>
