<style lang="less">
  #login{
    .loginTit{
      height: 1rem;
      background-image: url("../assets/img/loginTlTBack.png");
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }
    .loginForm{
      height: 6rem;
      background-image: url("../assets/img/loginFormBack.png");
      background-repeat: no-repeat;
      background-size: 100% 100%;
      position: relative;
      .loginFormBox{
        position: absolute;
        top: 50%;
        right: 19%;
        transform: translate(0,-50%);
        width: 350px;
      }
    }
    .downApp{
      height: 6.2rem;
      background-image: url("../assets/img/downAppBack.png");
      background-repeat: no-repeat;
      background-size: 100% 100%;
      position: relative;
      img{
        width: 2.6rem;
        top: 30% ;
        position: absolute;
      }
      .iosImg{
        left: 34%;
      }
      .androidImg{
        left: 54%;
      }
    }
    .bottomIcon{
      height: 0.9rem;
      background-image: url("../assets/img/loginBotton.png");
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }
    .copyRight {
      height: 74px;
      background-image: url("../assets/img/copyRight.png");
    }
  }
</style>
<template>
  <div id="login">
    <div class="loginTit"></div>
    <div class="loginForm">
      <div class="loginFormBox">
        <Card style="width:100%">
          <p slot="title" style="text-align: center">
            账户登录
          </p>
          <div>
            <Form ref="formInline" :model="formInline" :rules="ruleInline">
              <FormItem prop="user">
                <Input type="text" v-model="formInline.user" placeholder="Username">
                <Icon type="ios-person-outline" slot="prepend"></Icon>
                </Input>
              </FormItem>
              <FormItem prop="password">
                <Input type="password" v-model="formInline.password" placeholder="Password">
                <Icon type="ios-lock-outline" slot="prepend"></Icon>
                </Input>
              </FormItem>
            </Form>
            <div>
              <Button type="primary" @click="handleSubmit('formInline')" long>登录</Button>
            </div>
          </div>
        </Card>
      </div>
    </div>
    <div class="downApp">
      <img class="iosImg" src="../assets/img/ios.png" alt="">
      <img class="androidImg" src="../assets/img/android.png" alt="">
    </div>
    <div class="bottomIcon">

    </div>
    <div class="copyRight bgImg"></div>
  </div>
</template>

<script>
  export default {
    name: "login",
    data () {
      return {
        formInline: {
          user: '',
          password: ''
        },
        ruleInline: {
          user: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入登录密码', trigger: 'blur' }
          ]
        }
      }
    },
    created(){
      function getRan(n) {
        var rnd="";
        for(var i=0;i<n;i++)
          rnd+=Math.floor(Math.random()*10);
        return rnd;
      }
      let zLength = Math.random().toFixed(2)*100
      console.log(zLength);
      let zList = []

      for(var i=0;i<zLength;i++){
        zList[i] = getRan(99).split('')
        console.log(zList);
        if(i==zLength-1){
          f1(f(zList))
        }
      }

      [
      ]
      function f(list) {
        let num = []
        for(var a=0;a<list.length;a++){
            num[a] = 0
          for(var it=0;it<list[a];it++){
            console.log('=====');

            console.log(parseInt(list[a][it]));
            if(parseInt(list[a][it])>4){
              num[a]++
            }
          }
        }

        return num
      }
      function f1(ls) {
        console.log(ls);
        let a = 0;
        let b =ls.length

        ls.forEach((it,index)=>{
          console.log(it);
          if(it>2){
            a++
          }
          if(index==ls.length-1){
            console.log('==============',a / b);
          }
        })
        return a/b
      }


    },



    methods: {
      handleSubmit(name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            // this.$Message.success('Success!');
            this.$http.post('/app/user/login',{userName: this.formInline.user,password: this.formInline.password}).then(res=>{
              if(res.code == 200){
                this.$Message.success('登录成功');
                this.$router.push({
                  name:'home'
                })
              }else {
                this.$Message.error(res.message)
              }
            }).catch(err=>{this.$Message.error('Fail!');})
          } else {

          }
        })
      }
    }
  }
</script>
