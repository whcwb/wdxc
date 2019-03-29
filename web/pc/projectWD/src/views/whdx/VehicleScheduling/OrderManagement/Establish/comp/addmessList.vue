<style lang="less">
	@import '../../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF'
			:mask-closable="mesF" title="新建课题">
			<div style="overflow: auto;">
				<Form
					:model='addmess'
					:label-width="130"
					:rules="ruleInline"
					label-position="right">
					<FormItem prop='zddm' label='新建课题代码：'>
						<Input type="text" v-model="addmess.zddm" placeholder="请填写课题代码...">
						</Input>
					</FormItem>
							<FormItem prop='zdmc' label='新建课题名称：'>
								<Input type="text" v-model="addmess.zdmc" placeholder="课题名称...">
								</Input>
							</FormItem>
				</Form>
			</div>
			</Form>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">取消</Button>
				<Button type="primary" @click="addDicList">确定</Button>
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
					zdmc: '',
					zddm: '',
					zdlmdm:''
				},
				ruleInline: {
                  zdmc: [
                      { required: true, message: '请输入类目名称', trigger: 'blur' }
                  ],
                  zddm: [
                      { required: true, message: '请输入类目编码', trigger: 'blur' }
                  ]
              	},
			}
		},
		props:{
			dicListMess:{
				type:String,
				default:''
			}
		},
		created(){
			this.addmess.zdlmdm = this.dicListMess
		},
		methods: {
			colse() {
				var v = this
				v.$parent.compName = ''
			},
			addDicList(){
				var v = this
				log('字典数据',v.addmess)
				this.$http.post(this.apis.DICTIONARY_LIST.ADD,v.addmess).then((res) =>{
					log('字典添加',res)
					if(res.code===200){
					    v.dictUtil.reload(v,()=>{v.$parent.getKT()})
						v.$Message.success('操作成功');
						v.$parent.compName = ''
					}else{
						v.$Message.error(res.message);
					}
				})
			}
		}
	}
</script>

<style>

</style>
