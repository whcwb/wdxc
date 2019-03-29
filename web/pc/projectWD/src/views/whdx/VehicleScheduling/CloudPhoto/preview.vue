<template>
    <div>
        <Modal title="图片详情" v-model="visible">
            <div class="ModalSty">
                <Button class="goleft" type="primary" shape="circle" @click="leftGo()">
                    <Icon type="arrow-left-b" size='24'></Icon>
                </Button>
                <Button class="goright" type="primary" shape="circle" @click="rightGo()">
                    <Icon type="arrow-right-b" size='24'></Icon>
                </Button>
                <img :src="staticPath+imgUrl" style="width: 100%">
            </div>
        </Modal>
    </div>
</template>

<script>
    export default {
        name: "preview",
        date(){
            return{
                visible:true,
                imgUrl:'',
                Ix:0,
            }
        },
        method:{
            handleView(index) {
                this.Ix = index
                this.imgUrl = this.fileList[index].url
                this.visible = true;
            },
            leftGo(){
                if(this.Ix==0){
                    this.Ix = this.fileList.length-2
                    this.imgUrl = this.fileList[this.Ix].url
                    return
                }
                this.Ix --
                this.imgUrl = this.fileList[this.Ix].url
            },
            rightGo(){
                if(this.Ix==this.fileList.length-2){
                    this.Ix = 0
                    this.imgUrl = this.fileList[this.Ix].url
                    return
                }
                this.Ix ++
                this.imgUrl = this.fileList[this.Ix].url
            }
        }
    }
</script>

<style scoped>

</style>