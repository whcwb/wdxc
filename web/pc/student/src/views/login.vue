<style lang="less">
    @import './login.less';
</style>

<template>
    <div class="login" @keydown.enter="handleSubmit">
    	<!--<img src="../../static/3.png"/>-->
        <div class="yc">
          <h1>欢迎约车</h1>
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
        <div class="login-con">
            <Card :bordered="false">
                <!--<p slot="title">-->
                    <!--<Icon type="log-in"></Icon>-->
                    <!--欢迎登录-->
                <!--</p>-->
                <div class="form-con">
                    <Form ref="loginForm" :model="form" :rules="rules">
                        <FormItem prop="username">
                            <Input v-model="form.username" placeholder="请输入用户名">
                                <span slot="prepend">
                                    <Icon :size="16" type="person"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                        <FormItem prop="password">
                            <Input type="password" v-model="form.password" placeholder="请输入密码">
                                <span slot="prepend">
                                    <Icon :size="14" type="locked"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                        <FormItem>
                            <Button @click="handleSubmit" type="primary" long>登录</Button>
                        </FormItem>
                    </Form>
                </div>
            </Card>
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
                username: 'admini',
                password: '123456'
            },
            rules: {
                username: [
                    { required: true, message: '账号不能为空', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '密码不能为空', trigger: 'blur' }
                ]
            }
        };
    },
    created(){
    },
    methods: {
        handleSubmit () {
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                	this.$http.post(configApi.LOGIN.QUERY, this.form).then((res) =>{
                		console.log('登陆结果',res)
                		if(res.code===200){
                			Cookies.set('usermess', this.form.username);
                			Cookies.set('result', res.result);
//						    this.$router.push({
//						    	name: 'home_index'
//						    });
							this.$router.push('home')
                		}else{
                			this.$router.push({
						    	name: 'error-500'
						    });
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
