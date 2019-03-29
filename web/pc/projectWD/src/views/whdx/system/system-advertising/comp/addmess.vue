<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF'
			:mask-closable="mesF" title="新建活动">
			<div style="overflow: auto;height: 440px;">
				<Form
						ref="formItem"
						:model="formItem"
						:rules="ruleInline"
						:label-width="100"
						:styles="{top: '20px'}">
					<Row>
						<Col span="12">
							<FormItem prop="hdbt" label='活动标题'>
								<Input type="text" v-model="formItem.hdbt" placeholder="请填写活动标题...">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="url" label='URL'>
								<Input type="text" v-model="formItem.url" placeholder="请填写URL...">
								</Input>
							</FormItem>
						</Col>
					</Row>

					<Row>
						<Col span="12">
							<FormItem prop="hdlx" label='活动类型'>
								<Select filterable clearable  v-model="formItem.hdlx">
									<Option value="00">微信</Option>
									<Option value="01">智能站牌</Option>
									<Option value="02">LED</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='位置'>
								<Select filterable clearable  v-model="formItem.wz">
									<Option value="10">上</Option>
									<Option value="20">中</Option>
									<Option value="30">下</Option>
								</Select>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem prop="hdsj" label='活动时间'>
								<DatePicker v-model="formItem.hdsj"
									format="yyyy-MM-dd" type="daterange"
									placement="bottom-end"
									placeholder="请输活动时间"
									style="width: 100%"></DatePicker>

								<!--<Input type="text" v-model="formItem.wjlx" placeholder="请填写广告类型...">
								</Input>-->
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="wjlx" label='附件类型'>
								<Select filterable clearable  v-model="formItem.wjlx" @on-change="selectC()">
									<Option value="00">图片</Option>
									<Option value="01">视频</Option>
								</Select>
							</FormItem>
						</Col>
					</Row>

					<Row>
						<Col span="24">
							<FormItem v-if="formItem.wjlx==='00'">
								<div>
									添加图片
								</div>
								<addlistfileImg
									@addImg="addImg"
								></addlistfileImg>
							</FormItem>
							<FormItem v-else-if="formItem.wjlx==='01'">
								<div>
									添加视频
								</div>
								<addlistfileVideo @addImg="addImg"></addlistfileVideo>
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
					wz:'10'
				},
                ruleInline:{
                	hdbt: [
              	    	{ required: true, message: '请输入活动标题', trigger: 'blur' }
                    ],
                    url: [
                        { required: true, message: '请输入URL地址', trigger: 'blur' }
                    ],
                    hdlx:[
                        { required: true,message: '请选择活动类型', trigger: 'blur' }
                    ],
                    // hdsj:[
                  	//     { required: true,message: '请选择活动时间', trigger: 'blur' }
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
		},
		methods: {
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
                        let hdsj = this.formItem.hdsj;
                        this.formItem.kssj = this.getdateParaD(hdsj[0])
                        this.formItem.jssj = this.getdateParaD(hdsj[1])
                        delete this.formItem.hdsj;
		                let url = this.apis.ADVERTISING.ADD;
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
                        v.$Message.error('请认真填写用户信息!');
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
