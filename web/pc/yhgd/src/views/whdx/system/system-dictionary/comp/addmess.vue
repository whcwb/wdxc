<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF' 
			:mask-closable="mesF" title="新建字典">
			<div style="overflow: auto;">
				<Form
					:model='addmess'
					:label-width="100"
					:rules="ruleInline"
					label-position="right">
					<FormItem prop='lmdm' label='字典代码：'>
						<Input type="text" v-model="addmess.lmdm" placeholder="请填写字典编码...">
						</Input>
					</FormItem>
							<FormItem prop='lmmc' label='字典名称：'>
								<Input type="text" v-model="addmess.lmmc" placeholder="请填写字典名称...">
								</Input>
							</FormItem>
				</Form>
			</div>
			</Form>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">取消</Button>
				<Button type="primary" @click="addDic">确定</Button>
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
					lmmc: '',
					lmdm: '',
				},
				ruleInline: {
                  lmmc: [
                      { required: true, message: '请输入字典名称', trigger: 'blur' }
                  ],
                  lmdm: [
                      { required: true, message: '请输入字典编码', trigger: 'blur' }
                  ]
              	},
			}
		},
		created(){
		},
		methods: {
			colse() {
				var v = this
				v.$parent.compName = ''
			},
			addDic(){
				var v = this
				log('字典数据',v.addmess)
				this.$http.post(this.apis.DICTIONARY.ADD,v.addmess).then((res) =>{
					log('字典添加',res)
					if(res.code===200){
						v.$parent.getmess()
						v.$Message.success('操作成功');
						v.$parent.compName = ''
					}else{
						v.$Message.error('操作失败!');
					}
				})
			}
		}
	}
</script>

<style>

</style>