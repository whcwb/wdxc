<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF'
			:mask-closable="mesF" title="新建事故">
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
								></addlistfileImg>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">取消</Button>
				<Button type="primary" @click="save('formItem')">确定</Button>
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
					wjlx:'00',
					kssj:'',
					jssj:'',
					filePaths:'',
					wz:'10',
					sj:''
				},
                drivers:[],//驾驶员
                ruleInline:{
                	hdbt: [
              	    	{ required: true, message: '请输入事故标题', trigger: 'blur' }
                    ],
                    url: [
                        { required: true, message: '请输入URL地址', trigger: 'blur' }
                    ],
                    hdlx:[
                        { required: true,message: '请选择事故类型', trigger: 'blur' }
                    ],
                    // hdsj:[
                  	//     { required: true,message: '请选择事故时间', trigger: 'blur' }
                    // ],
                    wjlx:[
                  	    { required: true,message: '请选择附件类型', trigger: 'blur' }
                    ]
                },
                cjsjInRange:[]
			}
		},
		watch: {
		},
		created(){
            this.getDrivers();
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
			selectC(){
				log(this.formItem.filePaths)
				this.formItem.filePaths =''
				log(this.formItem.filePaths)
			},
            save(name){
                var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        if (typeof this.formItem.sgsj == 'object'){
                            this.formItem.sgsj = this.formItem.sgsj.format('yyyy-MM-dd hh:mm:ss');
						}
		                let url = this.apis.SG.ADD;
		                this.$http.post(url,this.formItem).then((res) =>{
		                    if(res.code===200){
		                        v.$Message.success(res.message);
		                    }else{
		                        v.$Message.error(res.message);
		                    }
		                    v.$parent.compName = ''
		                    v.$parent.getmess()
		                })
		            } else {
                        v.$Message.error('请认真填写信息!');
                    }
                })
            },
			colse() {
				var v = this
				v.$parent.compName = ''
			}
		}
	}
</script>

<style>

</style>
