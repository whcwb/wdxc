<template>
      <div>
            <Modal v-model="ms"
                   :closable='false'
                   :mask-closable=false
                   width="80%">
                  <h2 slot="header">异常事件（5分钟之内）</h2>
                  <div>
                        <abnor></abnor>
                  </div>
                  <div slot="footer">
                        <Button type="primary" @click="close">关闭</Button>
                  </div>
            </Modal>
      </div>
</template>

<script>
      import abnor from './abnormal'
    export default {
        name: "mainAbnormess",
        components:{
            abnor
        },
        data(){
            return{
                ms:true
            }
        },
        methods:{
            close(){
                this.$parent.compName = ''
            }
        }
    }
</script>

<style scoped>

</style>
