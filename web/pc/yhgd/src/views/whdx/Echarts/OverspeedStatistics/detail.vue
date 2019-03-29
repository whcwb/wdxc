<template>
    <div>
        <Modal v-model="showModal" width='900' :closable='mesF'
               :mask-closable="mesF" title="新建事故">
            <div style="overflow: auto;height: 440px;">

            </div>
            <div slot='footer'>
                <Button type="default" @click="colse"style="color: #949494">取消</Button>
                <Button type="primary" @click="save('formItem')">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    export default {
        name: "detail"
    }
</script>

<style scoped>

</style>
