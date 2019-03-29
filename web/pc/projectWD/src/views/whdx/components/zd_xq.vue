<template>
      <div>
            <Modal
                    v-model="showModal"
                    :closable="false"
                    :mask-closable="false"
                    width="300"
                    class-name="vertical-center-modal"
                    :title="'服务续期__'+mess.zdbh">
                  <div style="font-weight: 700;text-align: center">
                        <RadioGroup v-model="param.fwnx">
                              <Radio label="1">1年</Radio>
                              <Radio label="2">2年</Radio>
                              <Radio label="3">3年</Radio>
                              <Radio label="5">5年</Radio>
                        </RadioGroup>
                  </div>
                  <div slot="footer">
                        <Button type="default" @click="close">关闭</Button>
                        <Button type="info" @click="xq">续期</Button>
                  </div>
            </Modal>
      </div>
</template>

<script>
    export default {
        name: "zd_xq",
        props: {
            mess: {
                type: Object,
                default: {
                    zdbh: ''
                }
            }
        },
        data() {
            return {
                showModal: true,
                param: {
                    zdbh: this.mess.zdbh,
                    fwnx: ''
                }

            }
        },
        created() {
        },
        methods: {
            close(){
                this.$parent.componentName = ''
                this.choosedRow = ''
            },
            xq() {//终端续期
                if (this.param.fwnx == '') {
                    this.swal({
                        title: '请选择续期年限',
                        type: 'warning'
                    })
                    return
                }
                this.$http.post('/api/zdgl/delay', this.param).then(res => {
                    if (res.code == 200) {
                        this.$parent.getPageData()
                        this.close()
                    }
                    this.swal({
                        title: res.message,
                        type: res.code == 200 ? 'success' : 'error'
                    })
                }).catch(err => {})
            }
        }
    }
</script>

<style scoped>

</style>