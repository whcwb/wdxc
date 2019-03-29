<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF'
			:mask-closable="mesF" title="事故编辑">
			<div style="overflow: auto;height: 440px;">
				<Form
						ref="formItem"
						:model="formItem"
						:rules="ruleInline"
						:label-width="100"
						:styles="{top: '20px'}">
					<Row>
						<Col span="12">
							<FormItem prop="cph" label='车牌号'>
								<Input type="text" v-model="formItem.cph" placeholder="请填写车牌号..."></Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="sj" label='司机'>
								<Select filterable clearable  v-model="formItem.sj">
									<Option v-for="(item) in drivers" :value="item.sfzhm" :key="item.sfzhm">{{item.xm}}</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='事故地点'>
								<Input type="text" v-model="formItem.sgdd" placeholder="请填写车牌号..."></Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="hdsj" label='事故时间'>
								<DatePicker v-model="formItem.sgsj"
											format="yyyy-MM-dd" type="datetime"
											placement="bottom-end"
											placeholder="请输事故时间"
											style="width: 100%"></DatePicker>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="sgms" label='事故描述'>
								<Input type="text" v-model="formItem.sgms" placeholder="请填写事故描述..."></Input>
							</FormItem>
						</Col>
						<Col span="24">
							<FormItem>
								<div>
									添加图片
								</div>
								<addlistfileImg
									@addImg="addImg"
									@removeFile = "removeFile"
									:urlList = "mess.filePaths"
								></addlistfileImg>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">取消</Button>
				<Button type="primary" @click="save">确定</Button>
			</div>
		</Modal>
	</div>
</template>

<script>
	import mixins from '@/mixins'

	import addlistfileImg from './addlistfileImg.vue'
	import addlistfileVideo from './addlistfileVideo.vue'

	export default {
		name: '',
		mixins: [mixins],
		components: {
        	addlistfileImg,
        	addlistfileVideo
        },
		data() {
			return {
			    edie:false,
				showModal: true,
				mesF: false,
				formItem: {
					kssj:'',
					jssj:''
				},
                drivers:[],//驾驶员
                ruleInline:{},
                cjsjInRange:[]
			}
		},
		props:{
			mess:{
				type:Object,
				default:{}
			}
		},
		watch: {
		},
		created(){
            this.getDrivers();
			this.formItem = this.mess
            log(this.formItem);
        },
		methods: {
            getDriverName(){
                for(let r of this.drivers){
                    if (r.sfzhm ===  this.formItem.sj){
                        this.addmess.sjxm = r.xm;
                    }
                }
            },
            getDrivers(){
                let v = this;
                v.$http.get(this.apis.JSY.NOT_BIND_LIST).then((res) =>{
                    if(res.code===200){
                        if(res.result==undefined||res.result==null){
                            res.result = []
                        }
                        this.drivers = res.result;
                    }
                })
            },
            addImg(path){
                this.formItem.filePaths += path+",";
			},
			removeFile(url){
                log('图片',url)
				log('原图片',this.formItem.filePaths)
				
                let aindex = this.formItem.filePaths.indexOf(url)//开始的位置
                let urlLength = url.length//传递的长度
                let filePath = this.formItem.filePaths//原数据拼接
                let filelength = filePath.length//原数据长度
                
                this.formItem.filePaths = filePath.slice(0,aindex)+filePath.slice(aindex+urlLength,filelength)
                
				log('修改数据',this.formItem.filePaths)
			},
            save(){
                var v = this
                let url = this.apis.SG.CHANGE;
                this.$http.post(url,this.formItem).then((res) =>{
                    if(res.code===200){
                        v.$Message.success(res.message);
                    }else{
                        v.$Message.error(res.message);
                    }
                    v.$parent.compName = ''
                    v.$parent.getmess()
                })
            },
			colse() {
				var v = this
				v.$parent.compName = ''
			}
		}
	}
</script>
