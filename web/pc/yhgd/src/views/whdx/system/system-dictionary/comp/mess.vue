<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF' 
			:mask-closable="mesF" title="编辑字典">
			<div style="overflow: auto;height: 300px;">
				<Form>
					<FormItem label='字典名称'>
						<Input type="text" v-model="addmess.RoleName" placeholder="请填写字典名称...">
						</Input>
					</FormItem>
					<FormItem label='权重：'>
						<Input type="text" v-model="addmess.Remarks" placeholder="请填写字典权重...">
						</Input>
					</FormItem>
				</Form>
			</div>
			</Form>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">取消</Button>
				<Button type="primary" @click="colse">确定</Button>
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
					RoleName: '字典一',
					RoleType: '',
					Remarks: '权重一',
				}
			}
		},
		created(){
		},
		methods: {
			colse() {
				//				var v = this
				//				v.$parent.compName = ''
				//				log(v.$parent)
				this.$emit('colsemodal')
			}
		}
	}
</script>

<style>

</style>