<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<style type="text/css">

</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='false'
			:mask-closable="false" :title="operate+'核算公式'">
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
							<FormItem label='车型'>
							<Select filterable clearable  v-model="formItem.cx" placeholder="请选择车型...">
								<Option v-for="cx in cxDict" :value="cx.key">{{cx.val}}</Option>
							</Select>
							</FormItem>
						</Col>
						<!--类型-->
						<Col span="12">

							<FormItem label='类型'>
							<Select filterable clearable  v-model="formItem.lx" placeholder="请选择类型...">
								<Option value="00">里程</Option>
								<Option value="10">加班</Option>
								<Option value="20">节假日</Option>
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
		name: 'formData',
		data() {
			return {
                apiRoot:this.apis.HSGS,
                operate:'新建',
				showModal: true,
				readonly: false,
				v:this,
                formItem: {
                    cartype:'',
                },
				formInputs:[
					{label:'内容',prop:'nr',required:true},
					{label:'金额',prop:'je',required:true},
				],
                ruleInline:{},
                cxDict:[],//车量型号
                cxDictCode:'ZDCLK0019',
			}
		},
		created(){
            this.util.initFormModal(this);
            this.getDict();
		},
		methods: {
            beforeSave(){
			},
            getDict(){
                this.cxDict = this.dictUtil.getByCode(this,this.cxDictCode);
            },
		}
	}
</script>

<style>

</style>
