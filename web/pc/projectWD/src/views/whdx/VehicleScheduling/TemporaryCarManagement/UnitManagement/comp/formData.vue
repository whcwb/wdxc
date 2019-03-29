<style lang="less">
	@import '../../../../../../styles/common.less';
</style>
<style type="text/css">

</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='false'
			:mask-closable="false" :title="operate+'单位'">
			<div style="overflow: auto;height: 500px;">
				<Form
						ref="form"
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
							<FormItem label='状态'>
								<Select filterable clearable  v-model="formItem.zt" placeholder="请填选择状态...">
									<Option value="00">正常</Option>
									<Option value="10">停用</Option>
								</Select>
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
		name: '',
		data() {
			return {
			    v:this,
                apiRoot:this.apis.TEMP_UNIT,
                operate:'新建',
				showModal: true,
				readonly: false,
				formItem: {
					px:1,
					zt:'00'
				},
                formInputs:[
                    {label:'单位名称',prop:'dwmc',required:true},
                    {label:'单位编号',prop:'dwbh',required:true},
                    {label:'联系人',prop:'lxr',required:true},
                    {label:'联系电话',prop:'lxdh',required:true},
                ],
                ruleInline:{
				}
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
