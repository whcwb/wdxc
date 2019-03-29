<style lang="less">
    @import "../styles/common";
    @import './login.less';
  .loginBytton{
      /*position: fixed;*/
      /*top: 56%;*/
      /*width:100%;*/
      padding:0 20px;
  }
</style>

<template>
    <div class="login box" @keydown.enter="handleSubmit">
    	<!--<img src="../../static/3.png"/>-->
        <div class="yc">
            <img src="../img/logo.png" alt="">
          <!--<h1 class="huan">-->
            <!--欢-->
          <!--</h1>-->
          <!--<h1 class="ying">-->
            <!--迎-->
          <!--</h1>-->
          <!--<h1 class="yue">-->
            <!--约-->
          <!--</h1>-->
          <!--<h1 class="che">-->
            <!--车-->
          <!--</h1>-->
        </div>
        <div class="login-con body">
            <Card :bordered="false">
                <!--<p slot="title">-->
                    <!--<Icon type="log-in"></Icon>-->
                    <!--欢迎登录-->
                <!--</p>-->
                <div class="form-con">
                    <Form ref="loginForm" :model="form" :rules="rules">
                        <FormItem prop="xm">
                            <Input v-model="form.xm" placeholder="请输入用户名">
                                <span slot="prepend">
                                    <Icon :size="16" type="person"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                        <FormItem prop="sjh">
                            <Input type="text" v-model="form.sjh" placeholder="请输入手机号">
                                <span slot="prepend">
                                    <Icon :size="14" type="iphone"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                    </Form>
                </div>
            </Card>
            <div class="loginBytton">
                <Button @click="handleSubmit"
                        size="large"
                        type="primary" long>登录</Button>
            </div>
        </div>
        <div class="botimg">
          <img src="../img/logbotImg.jpg" alt="" width="100%">

        </div>
    </div>
</template>

<script>
import Cookies from 'js-cookie';
import configApi from '@/axios/config.js'
export default {
    data () {
        return {
            form: {
                xm: '',//小车7坐
                sjh: '',//320333333333333333
            },
            rules: {
                xm: [
                    { required: true, message: '姓名不能为空', trigger: 'blur' }
                ],
                sjh: [
                    { required: true, message: '手机号不能为空', trigger: 'blur' }
                ]
            }
        };
    },
    created(){
    },
    methods: {
        handleSubmit () {
          var v = this
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                	this.$http.post(this.apis.LOGIN.QUERTY, this.form).then((res) =>{
                		console.log('登陆结果',res)
                		if(res.code===200){
                		      // v.$store.state.app.user = res.result
                          Cookies.set('user', res.result);
                          Cookies.set('result', res.result.token);
                          this.$router.push({
                            name:'center'
                          })
                    }else{
                		}
                	})
                }
            });
        }
    }
};
</script>

<style>

</style>
