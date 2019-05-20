<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF'
			:mask-closable="mesF" :title='$t("EDIT_SERVICE")'>
			<div style="overflow: auto;">
				<Form
						ref="addmess"
						:model="addmess"
						:rules="ruleInline"
						:label-width="100"
						:styles="{top: '20px'}">
					<Row>
						<Col span="12">
							<FormItem prop="fwmc" :label='$t("SERVICE_NAME_TAB")'>
								<Input readonly="readonly" type="text" v-model="addmess.fwmc" :placeholder='$t("FUNCTION_NAME_ENTER")'>
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="fwdm" l:label='$t("SERVICE_CODE")'>
								<Input readonly="readonly" type="text" v-model="addmess.fwdm" :placeholder='$t("FUNCTION_NAME_ENTER")'>
								</Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem :label='$t("STATUS")'>
								<Select disabled="disabled" filterable clearable  v-model="addmess.zt" :placeholder='$t("STATUS_TAB")'>
									<Option v-for = '(item,index) in Dictionary' :value="item.key">{{item.val}}</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="apiQz" :label='$t("API_PREFIX")'>
								<Input readonly="readonly" type="text" v-model="addmess.apiQz" :placeholder='$t("API_PREFIX_TAB")'>
								</Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="24">
							<FormItem :label='$t("ICON_TAB")'>
								<Input type="text" v-model="addmess.tb" :placeholder='$t("ADDRESS_TAB")'>
								</Input>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			</Form>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">{{$t("CANCEL")}}</Button>
				<Button type="primary" @click="chMessData('addmess')">{{$t("DETERMINE")}}</Button>
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
				},
				ruleInline: {
                  fwmc: [
                      { required: true, message: this.$t("SERVICE_NAME"), trigger: 'blur' }
                  ],
                  fwdm: [
                      { required: true, message: this.$t("SERVICE_CODE_TAB"), trigger: 'blur' }
                  ],
                  apiQz:[
                  	  { required: true, message: this.$t("API_PREFIX_TAB"), trigger: 'blur' }
                  ]
              	},
			}
		},
		props:{
			chmess:{
				type:Object,
				default:{}
			},
			Dictionary:{
				type:Array,
				default:[]
			}
		},
		created(){
			this.addmess = this.chmess
		},
		methods: {
			chMessData(name){
				var v = this
				this.$refs[name].validate((valid) => {
                    if (valid) {
						delete this.addmess.functions
						delete this.addmess.cjsj
						delete this.addmess.xgsj
						this.$http.post(this.apis.ITMS.CHANGE,this.addmess).then((res) =>{
							log(this.$t("FUNCTION_DATA"),res)
							if(res.code===200){
								v.$Message.success(this.$t("CREATE_SUCCESS"));
							}else{
								v.$Message.error(this.$t("CREATE_FAIL"));
							}
							v.$parent.getmess()
							v.$parent.compName = ''
						}).catch((error) =>{
							v.$Message.error(this.$t("ERROR_ALERM"));
						})
					} else {
						v.$Message.error(this.$t("WRITE_INFOR"));
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
