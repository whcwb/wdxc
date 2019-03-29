<template>
    <div>
        <Modal
                v-model="showModal"
                width='80%'
                :closable='false'
                :mask-closable="false"
                title="电子围栏">
            <div :style="bodyH">
                <bkshow :carNumber="carID"></bkshow>
            </div>
            <div slot='footer'>
                <Button type="default" @click="colse">关闭</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import mixins from '@/mixins'
    import bkshow from '../../../map/mapBK_show'
    export default {
        name:'',
        mixins:[mixins],
        components:{
            bkshow
        },
        data(){
            return {
                showModal:true,
                bodyH:{
                    height:0
                },
                carID:''
            }
        },
        props:{
            mess:{
                type:Object,
                default:{}
            }
        },
        created(){
            this.carID = this.mess.clId
            log(this.mess)
            this.bodyH.height = (this.getWindowHeight() - 330)+'px'
        },
        methods:{
            colse(){
                var v = this
                v.$parent.compName = '';
                v.$parent.componentName = '';
            }
        }
    }
</script>

<style>
</style>
