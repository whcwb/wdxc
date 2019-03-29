<style lang="less">
      @import '../../../../styles/common.less';
</style>
<template>
      <div>
            <Modal v-model="showModal" width='60%'
                   :closable='false' :mask-closable="mesF"
                   title="批量导入">
                  <div class="box-row" style="height: 55px;font-size: 16px">
                        <div>
                              模版下载：
                        </div>
                        <div class="body-O">
                              <a :href="staticPath+'批量新增终端模板.xls'">批量导入终端模版</a>
                        </div>
                  </div>
                  <div class="box-row" style="height: 55px;font-size: 16px">
                        <div>
                              上传文件：
                        </div>
                        <div class="body-O">
                              <Upload v-show="upShow"
                                      :action="apis.UPLOAD"
                                      :multiple="false"
                                      :show-upload-list="false"
                                      :format="['xlsx','xls']"
                                      :on-format-error="handleFormatError"
                                      :on-success="handleSuccess"
                                      :on-error="handleError">
                                    <Button type="default" icon="ios-cloud-upload-outline"
                                            size="small">上传文件</Button>
                              </Upload>
                              <div v-show="!upShow" class="box-row">
                                    <div class="body-O">
                                          <Icon type="clipboard"></Icon>
                                          {{file}}
                                    </div>
                                    <div style="padding: 0 8px">
                                          <Tooltip content="重新上传" placement="top">
                                                <Button type="primary"
                                                        shape="circle" icon="refresh"
                                                        @click="file=''"></Button>
                                          </Tooltip>
                                          <Tooltip content="批量导入" placement="top">
                                                <Button type="success"
                                                        shape="circle" icon="arrow-up-a"
                                                        @click="save"></Button>
                                          </Tooltip>
                                    </div>
                              </div>
                        </div>
                  </div>
                  <div v-for="(item,index) in errors" style="color: red">{{item}}</div>
                  <div slot='footer'>
                        <Button type="default" @click="close"style="color: #949494">取消</Button>
                        <!--<Button type="primary" @click="save">确定</Button>-->
                  </div>
            </Modal>
      </div>
</template>

<script>
    import treelist from '@/data/list.js'

    export default {
        name: '',
        data() {
            return {
                staticPath:this.apis.STATIC_PATH,
                mesF:false,
                showModal: true,
                file:'',
                upShow:true,
                errors:[]
            }
        },
        watch:{
          file:function (n,o) {
              if(n!=''){
                  this.upShow = false
              }else {
                  this.upShow = true
              }
          }
        },
        created(){
        },
        mounted(){
        },
        methods: {
            handleSuccess (res, file) {
                this.file = '/'+res.message

            },
            handleError(err,file){
                this.$Notice.error({
                    title: '文件上传失败！！！'
                });
            },
            handleFormatError (file) {
                this.$Notice.warning({
                    title: '文件格式上传错误！！！',
                    desc: '请上传模版文件。'
                });
            },
            save(){
                var v = this
                this.$http.post(this.apis.fileDR,{filePath:this.file}).then((res)=>{
                    if(res.code==200){
                        this.$Message.success(res.message);
                        this.$parent.getPageData()
                        this.close()
                    }else {
                        this.errors = res.result;
                        this.file = ''
                    }
                }).catch((err)=>{

                })
            },
            close(){
                this.$parent.componentName = '';
            }

        }
    }
</script>

<style>

</style>
