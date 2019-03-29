<template>
      <div class="modal">
        <div class="modaltit" style="position: relative">
          <div style="position: absolute;left: 5px;top: 1px;" @click="handleReset()">
            <Icon type="chevron-left" color="#9e9e9e" size="22"></Icon>
          </div>
            单据确认
        </div>
        <div class="Ymess">
          <p class="mesPadd">
              <Icon type="ios-film-outline"></Icon>
              {{mess.jgmc}}-{{mess.ck}}
          </p>
          <p class="mesPadd">
            <Icon type="ios-clock"></Icon>
            {{mess.yysj}}
          </p>
          <p class="mesPadd">
            <Icon type="ios-telephone"></Icon>
            <a>{{mess.cklxdh}}</a>
          </p>
          <p>
            <Icon type="ios-location" color="#15b740"></Icon>
            {{mess.hcdz}}
          </p>
          <p><Icon type="arrow-down-c"></Icon></p>
          <p>
            <Icon type="ios-location" color="#ff9b00"></Icon>
            {{mess.mdd}}
          </p>
        </div>
        <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="0">
            <Row :gutter="16">
                <Col span="12">
                    <FormItem label="里程">
                        <Input type="text" v-model="formValidate.lc" placeholder="里程/公里数"></Input>
                    </FormItem>
                </Col>
                <Col span="12">
                    <FormItem label="单价"
                    >
                        <Input type="text" v-model="formValidate.dj"
                               placeholder="行程单价"></Input>
                    </FormItem>
                </Col>
            </Row>
            <Row :gutter="16">
                <Col span="12">
                  <FormItem label="过桥费">
                    <Input :number="true" v-model="formValidate.gqf"
                           placeholder="过桥费"></Input>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="路停费">
                    <Input :number="true" v-model="formValidate.glf"
                           placeholder="路停费"></Input>
                  </FormItem>
                </Col>
            </Row>
        </Form>
        <loading :show="loadingShow" :text="loadingText"></loading>
        <div class="but box-row" style="height: 40px">
          <Button type="primary" @click="handleSubmit('formValidate')" long>提交</Button>
        </div>
      </div>
</template>
<style lang="less" scoped>
  .modal{
    background: #fff;
    padding: 18px;
    position: fixed;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    .modaltit{
      height: 40px;
      line-height: 40px;
      text-align: center;
      background: rgba(0,0,0,0.1);
      font-size: 18px;
      color: #919191;
      font-weight: 600;
    }
    .Ymess{
      padding: 5px;
      p{
        font-size: 18px;
      }
      .mesPadd{
        padding: 6px 0;
      }
    }
    .but{
      text-align: center;
    }
  }
</style>
<script>
    import { Loading } from 'vux'
    export default {
        name: "modal",
        components: {
          Loading
        },
        data(){
          return {
            loadingShow:false,
            loadingText:'数据提交中',
            mess:this.$store.state.app.lineData,
            formValidate: {
              id:this.$store.state.app.lineData.id,
              lc: '',
              dj: '',
              gqf: '',
              glf: ''
            },
            ruleValidate: {
              lc: [
                { required: true, message: '请填写公里数', trigger: 'blur' }
              ],
              dj: [
                { required: true, message: '请输入行程单价', trigger: 'blur' }
              ]
            }
          }
        },
      props:{
        // mess:{
        //   type:Object,
        //   default:{}
        // }
      },
      created(){
          console.log(this.mess.ck)
          if(this.mess.ck==undefined){
            this.$router.push({
              name:'center'
            })
          }
      },
        methods: {
          handleSubmit (name) {
            var v = this
            this.loadingShow = true
            // this.$refs[name].validate((valid) => {
            //   if (valid) {
                this.$http.post(this.apis.LISTOK.CHANGE,this.formValidate).then((res)=>{
                    if(res.code==200){
                      v.loadingText = '数据提交成功'
                      v.$router.push({
                        name:'center'
                      })
                    }else {
                      v.loadingText = '数据提交失败  ,  请重新提交！'
                      setTimeout(function () {
                        v.loadingShow = false
                      },1000*1.2)
                    }
                  console.log(res)
                }).catch((error) =>{
                  console.log('出错了',error)
                })
            //   } else {
            //   }
            // })
          },
          handleReset () {
            this.$router.back()
          }
        }
    }
</script>

<style scoped>

</style>
