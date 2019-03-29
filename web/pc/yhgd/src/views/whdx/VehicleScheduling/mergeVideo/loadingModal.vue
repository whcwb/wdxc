<template>
    <div>
        <Modal v-model="showModal" width="400" height="200"
               :closable='false'
               :mask-closable="false">
            <div style="height: 200px;text-align: center;padding-top: 10%">
                <i-circle :percent="percent">
                    <span class="demo-Circle-inner" style="font-size:24px">{{sec}} 秒</span>
                </i-circle>
            </div>
            <div slot='footer' style="text-align: center;">
                <Button type="primary" @click="close">取消等待</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    export default {
        name: "loading",
        data(){
            return{
                showModal:true,
                SpinShow1:true,
                sec:120,
                percent:100,
                totalSec : 120
            }
        },
        mounted(){
          this.count();
        },
        methods:{
            close(){
                var v = this
                v.$parent.componentName = ''
            },
            count(){
                this.sec--;
                this.percent = this.sec*100 / this.totalSec;
                clearTimeout();
                if (!this.$route.path == '/VehicleScheduling/mergeVideo'){
                    return;
                }
                if (this.sec <= 0){
                    this.$Message.error("等待超时！");
                    this.close();
                    return;
                }
                let v = this;
                setTimeout(()=>{
                    v.count();
                },1000)

            }
        }
    }
</script>

<style scoped>

</style>