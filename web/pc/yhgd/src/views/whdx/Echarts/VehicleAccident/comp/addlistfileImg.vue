<template>
	<div>
	    <div class="demo-upload-list" v-for="item in uploadList">
	        <template v-if="item.status === 'finished'">
	            <img :src="item.url">
	            <div class="demo-upload-list-cover">
	                <!--<Icon type="ios-eye-outline" @click.native="handleView(item.name)"></Icon>-->
	                <!--文件移除-->
	                <Icon type="ios-trash-outline" @click.native="handleRemove(item)"></Icon>
	            </div>
	        </template>
	        <template v-else>
	            <Progress v-if="item.showProgress" :percent="item.percentage" hide-info></Progress>
	        </template>
	    </div>
	    <Upload
	        ref="upload"
	        :show-upload-list="false"
	        :default-file-list="defaultList"
	        :on-success="handleSuccess"
	        :format="['jpg','jpeg','png']"
	        :max-size="1000"
	        :on-format-error="handleFormatError"
	        :on-exceeded-size="handleMaxSize"
	        :before-upload="handleBeforeUpload"
	        multiple
	        type="drag"
	        :action="uploadUrl+'?targetPath=hdFile'"
	        style="display: inline-block;width:58px;">
	        <div style="width: 58px;height:58px;line-height: 58px;">
	            <Icon type="camera" size="20"></Icon>
	        </div>
	    </Upload>
	</div>
</template>
<script>

    export default {
    	name:'',
        data () {
            return {
                defaultList: [
//                  {
//                      'name': 'a42bdcc1178e62b4694c830f028db5c0',
//                      'url': 'https://o5wwk8baw.qnssl.com/a42bdcc1178e62b4694c830f028db5c0/avatar'
//                  },
//                  {
//                      'name': 'bc7521e033abdd1e92222d733590f104',
//                      'url': 'https://o5wwk8baw.qnssl.com/bc7521e033abdd1e92222d733590f104/avatar'
//                  }
                ],
                imgName: '',
                visible: false,
                uploadList: [],
				uploadUrl:this.apis.UPLOAD
            }
        },
        props:{
			urlList:{
				type:String,
				default:''
			}
		},
        created(){
        	log(this.urlList)
        	this.dataList()
        },
        methods: {
        	dataList(){
        		let ArrList = this.urlList.split(',')
	        	for(var i=0;i<ArrList.length-1;i++){
	        		this.defaultList.push({'url':this.apis.STATIC_PATH+ArrList[i]})
	        	}
        	},
            handleView (name) {
                this.imgName = name;
                this.visible = true;
            },
            handleRemove (file) {
                const fileList = this.$refs.upload.fileList;
                this.$refs.upload.fileList.splice(fileList.indexOf(file), 1);
                log('移出文件',file)
//              this.formItem.filePaths.replace(url+',','')
                this.$emit('removeFile',file.url.replace(this.apis.STATIC_PATH,'')+',')
            },
            handleSuccess (res, file,fileList) {
                this.$emit('addImg',res.message);
                file.url = this.apis.STATIC_PATH + res.message;
            },
            handleFormatError (file) {
                this.$Notice.warning({
                    title: '文件格式错误',
                    desc: '图片仅支持 jpg、jpeg、png'
                });
            },
            handleMaxSize (file) {
                this.$Notice.warning({
                    title: '文件太大了',
                    desc: '文件不能超过18M'
                });
            },
            handleBeforeUpload () {
                const check = this.uploadList.length < 5;
                if (!check) {
                    this.$Notice.warning({
                        title: 'Up to five pictures can be uploaded.'
                    });
                }
                return check;
            }
        },
        mounted () {
            this.uploadList = this.$refs.upload.fileList;
        }
    }
</script>
