<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<style type="text/css">

</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='false'
			:mask-closable="false" :title="operate+'报销记账'">
			<div style="overflow: auto;height: 500px;">
				<Form ref="form"
						:model="formItem"
						:rules="ruleInline"
						:label-width="100"
						:styles="{top: '20px'}">
					<Row>
						<Col span="12" v-for="i in formInputs">
							<FormItem :prop='i.prop' :label='i.label'>
								<Input type="text" v-model="formItem[i.prop]" :placeholder="'请填写'+i.label+'...'"></Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='报销时间'>
								<Input type="text" v-model="formItem.bxsj" placeholder="请填写报销时间..."></Input>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			<div slot='footer'>
				<Button type="default" @click="v.util.closeDialog(v)"style="color: #949494">取消</Button>
				<Button type="primary" @click="v.util.save(v)">确定</Button>
			</div>
		</Modal>
	</div>
</template>

<script>
	export default {
		name: 'formData',
		data() {
			return {
                apiRoot:this.apis.BXJZ,
                operate:'新建',
				showModal: true,
				readonly: false,
				v:this,
                formItem: {
                    bxsj:'',
                },
				formInputs:[
					{label:'报销人',prop:'bxr',required:true},
					{label:'报销事项',prop:'bxsx',required:true},
					{label:'报销金额',prop:'bxje',required:true},
					{label:'发票数量',prop:'fpsl',required:true},
				],
                ruleInline:{}
			}
		},
		created(){
            this.util.initFormModal(this);
		},
		methods: {
            beforeSave(){
			},
		}
	}
</script>

<style>

</style>
