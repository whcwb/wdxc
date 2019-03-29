<style lang="less">
  @import "../../styles/box.less";
</style>
<template>
  <div class="boxHeader">
      <div class="box-row">
          <div class="titLeft" @click="back()">
            <slot  name="headleft">
                <i class="iconfont icon-left"></i>
            </slot>
          </div>
          <div class="titCenter body-O" style="text-align: center;">
            {{tit}}
          </div>
          <div class="titRight" @click="titRight">
              <slot name="headright">
              </slot>
          </div>
      </div>
  </div>
</template>

<script>
    export default {
        name: "headTit",
        props:{
            tit:{
              type:String,
              default:'tit'
            }
        },
        methods:{
          back(){
            this.$router.back()
          },
          titRight(){
            this.$emit('titRigCli')
          }
        }
    }
</script>

<style scoped>

</style>
