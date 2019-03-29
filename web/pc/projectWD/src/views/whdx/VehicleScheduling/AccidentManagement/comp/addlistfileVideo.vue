<template>
	<div>
	    <div class="demo-upload-list" v-for="item in uploadList">
	        <template v-if="item.status === 'finished'">
				<video :src="item.url" controls="false"></video>
	            <div class="demo-upload-list-cover">
	                <!--<Icon type="ios-eye-outline" @click.native="handleView(item.name)"></Icon>-->
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
	        :max-size="1024000"
			:format="['ogv','ogg','mp4']"
	        :on-format-error="handleFormatError"
	        :on-exceeded-size="handleMaxSize"
	        :before-upload="handleBeforeUpload"
	        multiple
	        type="drag"
	        :action="uploadUrl+'?targetPath=movie'"
	        style="display: inline-block;width:58px;">
	        <div style="width: 58px;height:58px;line-height: 58px;">
	            <Icon type="videocamera" size="20"></Icon>
	        </div>
	    </Upload>
	    <!--<Modal title="View Image" v-model="visible">
	        <img :src="'https://o5wwk8baw.qnssl.com/' + imgName + '/large'" v-if="visible" style="width: 100%">
	    </Modal>-->
		
	</div>
</template>
<script>

    export default {
        data () {
            return {
                defaultList: [
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
            },
            handleSuccess (res, file,fileList) {
                this.$emit('addImg',res.message);
                log(res);
                file.url = this.apis.STATIC_PATH + res.message;
            },
            handleFormatError (file) {
                this.$Notice.warning({
                    title: '文件格式错误',
                    desc: '视频仅支持 ogv、ogg、mp4'
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
        	log('fileList',this.$refs.upload.fileList)
            this.uploadList = this.$refs.upload.fileList;
        }
    }
</script>