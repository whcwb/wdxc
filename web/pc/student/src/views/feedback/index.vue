<style lang="less">
  @import "../../styles/common.less";
  .textarea{
      background: rgba(227,227,227,0.5);
      padding: 1rem;
      height: 60%;
      margin-top: 10%;
      border:dashed 0.03rem #b3b3b3;
      textarea {
          resize: none;
          width: 100%;
          height: 100%;
          padding: 0;
          outline: none;
          .text {

          }
          .text::-webkit-input-placeholder {
            color: red;
          }
          .text::-moz-placeholder {
            color: red;
          }
      }
  }
  .header{
      .vux-header{
          background-color: #FFF;
          .vux-header-left{
            color: #8d8d8d;
          }
          .vux-header-title{
            color: #8d8d8d;
            font-weight: 600;
          }
      }
  }
  .button{
    padding: 0.4rem 0.8rem;
        button{
          height: 30px;
          width: 100%;
          background-color: #ff8a00;
          border: none;
          color: #fff;
        }
  }
</style>
<template>
	<div class="box" style="height: 100%;">
		  <div class="header">
	  		<x-header
	  			:left-options="{showBack: true}">
	  			<span class="tit">反馈</span>
	  			</x-header>
	  	</div>
      <div class="body" style="padding: 1rem">
        <!--<div class="textarea">-->
                      <!--<textarea class="text"-->
                                <!--:placeholder="'请输入您的'+placeholder+'信息'"-->
                      <!--&gt;</textarea>-->
        <!--</div>-->
        <group>
          <x-input
            title="姓名  "
            :min="2"
            :max="4"
            v-model="form.lxrxm"
            :is-type="rulName"
            placeholder="请填写姓名"></x-input>
        </group>
        <group>
          <x-input
              title="手机号码"
              mask="999 9999 9999"
              v-model="form.lxfs" :max="13"
              placeholder="请填写联系方式"
              :is-type="rul"></x-input>
        </group>
        <group>
          <!--<x-input :placeholder="placeholder" ></x-input>-->
          <x-textarea
            title="反馈内容"
            v-model="form.nr"
            :max="200" name="description" placeholder="请填写反馈信息"></x-textarea>
        </group>
      </div>
      <div class="button" @click="submit">
        <button>
          提    交
        </button>
      </div>
	</div>
</template>

<script>
	import {XHeader,XInput,Group,XTextarea,AlertModule } from 'vux'

	export default{
		name:'',
		components: {
		    XHeader,Group,XInput,XTextarea,AlertModule
		},
    data(){
		  return{
        form:{
          lxfs:'',
          nr:'',
          lxrxm:'',
          yjlx:'00'
        },
        rulName: function (value) {
          return {
            valid: value.length==2|| value.length==3|| value.length==4,
            msg: '请输入你的名字'
          }
        },
        rul: function (value) {
          return {
            valid: value.length ==13,
            msg: '请输入正确的电话号码！'
          }
        },
        placeholder:''
      }
    },
    methods:{
      submit(){
        if(this.rul(this.form.lxfs).valid&&this.form.nr.length!=0&&this.rulName(this.form.lxrxm).valid){
          this.sub()
        }else{
          alert('请将信息填写完整')
        }
      },
      sub(){
          var v = this
          v.$http.post(this.apis.FEEDBACK.QUERTY, this.form).then((res) =>{
            console.log('*****',res)
            if(res.code){
              v.showPlugin
            }
          }).catch((error)=>{

          })
      },
      showPlugin () {
        var v = this
        AlertModule.show({
          title: '提交成功',
          content: '信息提交成功，请留意短信查看处理结果。',
          onShow () {
            console.log('Plugin: I\'m showing')
          },
          onHide () {
            console.log('Plugin: I\'m hiding now')
            v.$router.push({
              name:'center'
            })
          }
        })
        setTimeout(function () {
          AlertModule.hide()
        }, 3000)
      },
    }
	}
</script>

<style>
</style>
