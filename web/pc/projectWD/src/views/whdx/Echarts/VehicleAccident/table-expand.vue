<style lang="less">
	.expand-row {
		margin-bottom: 16px;
		padding-left: 40px;
	}
	.demo-carousel{
		width: 100%!important;
		height: 200px;
	}
	.ModalSty{
		position: relative;
		.goleft{
			position: absolute;
			top: 50%;
			left: 0;
			transform: translateY(-50%);
			z-index: 1000;
			background: rgba(0, 0, 0, 0.6);
    		border: solid #948787 1px;
		}
		.goright{
			position: absolute;
			top: 50%;
			right: 0;
			transform: translateY(-50%);
			z-index: 1000;
			background: rgba(0, 0, 0, 0.6);
    		border: solid #948787 1px;
		}
	}
</style>
<template>
	<div>
		<div>
			附件：
		</div>
		<div v-if="row.wjlx=='00'">
			<div class="demo-upload-list" 
				v-for="(item,index) in uploadList"
				v-if="(index+1)!=uploadList.length">
				<div class="demo-upload-list-box">
					<img :src="staticPath+item.url">
					<div class="demo-upload-list-cover">
						<Icon type="ios-eye-outline" @click.native="handleView(index)"></Icon>
					</div>
				</div>
			</div>
			<Modal class="videoStyle" title="View Image" v-model="visible">
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
		<div v-else-if="row.wjlx=='01'">
			<div class="demo-upload-list" 
				v-for="(item,index) in uploadList" 
				v-if="(index+1)!=uploadList.length">
				<div>
					<video :src="staticPath+imgUrl" controls="controls" height="100%"></video>
					<div class="demo-upload-list-cover">
						<Icon type="arrow-right-b" @click.native="handleView(index)"></Icon>
					</div>
				</div>
			</div>
			<Modal title="View Image" v-model="visible">
				<div class="ModalSty">
					<Button class="goleft" type="primary" shape="circle" @click="leftGo()">
			           <Icon type="arrow-left-b" size='24'></Icon>
			        </Button>
					<Button class="goright" type="primary" shape="circle" @click="rightGo()">
			           <Icon type="arrow-right-b" size='24'></Icon>
			        </Button>
					<!--<img :src="staticPath+imgUrl" style="width: 100%">-->
					<video :src="staticPath+imgUrl" controls="controls"  style="width: 100%"></video>
				</div>
			</Modal>
		</div>
	</div>
</template>
<script>

	export default {
		name: '',
		data() {
			return {
				visible: false,//modal层
				staticPath:this.apis.STATIC_PATH,
				uploadList: [],
				Ix:0,
				imgUrl:'',
				paths:[]
			}
		},
		props: {
			row: {
				type: Object,
				default: {}
			}
		},
		created(){
			log('tupian',this.row.filePaths)
            if (this.row.filePaths){
                let paths = [];
                this.uploadList = [];
                paths = this.row.filePaths.split(',');
                for(let r of paths){
                    let name = r.substring(r.lastIndexOf("/"));
                    this.uploadList.push({name:name,url:r});
				}
			}
        },
		methods: {
			handleView(index) {
				this.Ix = index
				this.imgUrl = this.uploadList[index].url
				this.visible = true;
			},
			leftGo(){
				if(this.Ix==0){
					this.Ix = this.uploadList.length-2
					this.imgUrl = this.uploadList[this.Ix].url
					return 
				}
				this.Ix --
				this.imgUrl = this.uploadList[this.Ix].url
			},
			rightGo(){
				if(this.Ix==this.uploadList.length-2){
					this.Ix = 0
					this.imgUrl = this.uploadList[this.Ix].url
					return 
				}
				this.Ix ++
				this.imgUrl = this.uploadList[this.Ix].url
			}
		}
	};
</script>