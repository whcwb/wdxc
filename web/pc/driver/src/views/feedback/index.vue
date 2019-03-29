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
        <div class="textarea">
                      <textarea class="text"
                                :placeholder="'请输入您的'+placeholder+'信息'"
                      ></textarea>
        </div>
      </div>
      <div class="button">
        <button>
          提    交
        </button>
      </div>
	</div>
</template>

<script>
	import {XHeader } from 'vux'

	export default{
		name:'',
		components: {
		    XHeader
		},
    data(){
		  return{
        placeholder:''
      }
    }
	}
</script>

<style>
</style>
