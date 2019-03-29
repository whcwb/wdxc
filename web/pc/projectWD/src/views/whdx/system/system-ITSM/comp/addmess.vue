<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF'
			:mask-closable="mesF" title="新建服务">
			<div style="overflow: auto;">
				<Form
						ref="addmess"
						:model="addmess"
						:rules="ruleInline"
						:label-width="100"
						:styles="{top: '20px'}">
					<Row>
						<Col span="12">
							<FormItem prop="fwmc" label='服务名称'>
								<Input type="text" v-model="addmess.fwmc" placeholder="请填写功能名称...">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="fwdm" label='服务代码'>
								<Input type="text" v-model="addmess.fwdm" placeholder="请填写功能名称...">
								</Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem label='状态'>
								<Select filterable clearable  v-model="addmess.zt" placeholder="请填选择状态...">
									<Option v-for = '(item,index) in Dictionary' :value="item.key">{{item.val}}</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem  prop="apiQz" label='API 前缀'>
								<Input type="text" v-model="addmess.apiQz" placeholder="请填写API 前缀...">
								</Input>
							</FormItem>
						</Col>
					</Row>

					<Row>
						<Col span="24">
							<FormItem label='图标'>
								<Input type="text" v-model="addmess.tb" placeholder="请填写地址...">
								</Input>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			</Form>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">取消</Button>
				<Button type="primary" @click="addmessData('addmess')">确定</Button>
			</div>
		</Modal>
	</div>
</template>

<script>


	export default {
		name: '',
		data() {
			return {
				showModal: true,
				mesF: false,
				addmess: {
					zt:'00'
				},
				ruleInline: {
                  fwmc: [
                      { required: true, message: '请输入服务名称', trigger: 'blur' }
                  ],
                  fwdm: [
                      { required: true, message: '请输入服务代码', trigger: 'blur' }
                  ],
                  apiQz:[
                  	  { required: true, message: '请输入API前缀', trigger: 'blur' }
                  ]
              	},
			}
		},
		props:{
			Dictionary:{
				type:Array,
				default:[]
			}
		},
		created(){
		},
		methods: {
			addmessData(name){
				var v = this
				this.$refs[name].validate((valid) => {
                    if (valid) {
						this.$http.post(this.apis.ITMS.ADD,this.addmess).then((res) =>{
							log('功能数据',res)
							if(res.code===200){
								v.$Message.success('创建成功');
							}else{
								v.$Message.error('创建失败');
							}
							v.$parent.getmess()
							v.$parent.compName = ''
						}).catch((error) =>{
							v.$Message.error('出错了！！！');
						})
					} else {
                        v.$Message.error('请将信息填写完整!');
                    }
				})
			},
			colse(){
				var v = this
				v.$parent.compName = ''
			}
		}
	}
</script>

<style>

</style>
