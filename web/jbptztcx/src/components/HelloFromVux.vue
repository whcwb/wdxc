<template>
  <div style="padding: 16px;">
    <div style="text-align: center">
      <h2>潍坊市交通违法举报平台</h2>
      <h2>举报信息状态查询</h2>
    </div>
    <group>
      <x-input title="设备编号" v-model="form.sbbh"></x-input>
      <x-input title="身份证号" v-model="form.sfzh"></x-input>
    </group>
    <br>
    <x-button type="primary" text="查询" @click.native="confirm"></x-button>
    <br>
    <div>
      <h3 style="color: red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用户通过行车记录仪抓拍上传举报视频信息后，输入设备编号及身份证号，即可了解举报信息状态。</h3>
    </div>
  </div>
</template>

<script>
import { Group, Cell ,AlertModule} from 'vux'

export default {
  components: {
    Group,
    Cell
  },
  data () {
    return {
      msg: 'Hello World!',
      form:{
        sbbh:'',
        sfzh:''
      }
    }
  },
  methods:{
    confirm(){
      if (this.form.sbbh === '' && this.form.sfzh === ''){
        AlertModule.show({
          title: '提示',
          content: '请输入查询条件',
          onShow () {
          },
          onHide () {
          }
        })
        return;
      }
      this.goto();
    },
    goto(){
        window.location.href = "http://www.wfcwkj.com/api/toWeixinSsp?openid=0000000000000000000000000000&ret_url=ssp"
        +'&sbbh='+this.form.sbbh
        +'&sfzh='+this.form.sfzh
    }
  }
}
</script>

<style>
.vux-demo {
  text-align: center;
}
.logo {
  width: 100px;
  height: 100px
}
</style>
