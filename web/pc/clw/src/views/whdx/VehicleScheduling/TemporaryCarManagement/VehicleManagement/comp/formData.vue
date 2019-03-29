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
							<FormItem prop='lsdwId' label='临时单位'>
								<Select v-model="formItem.lsdwId">
									<Option v-for="item in lswdList" :value="item.id" :key="item.id">{{item.dwmc}}</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='车辆类型'>
								<Select filterable clearable  v-model="formItem.cllx">
									<Option v-for="cx in cxDict" :value="cx.key">{{cx.val}}</Option>
								</Select>
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
                apiRoot:this.apis.TEMP_CAR,
                operate:'新建',
				showModal: true,
				readonly: false,
                cxDict:[],//车量型号
                cxDictCode:'ZDCLK0019',
                lswdList:[],
				formItem: {
					px:1,
					zt:'00',
                    lsdwId:'',
                    cllx:'',
				},
				formInputs:[
					{label:'车牌号',prop:'cph',required:true},
					{label:'座位数',prop:'zws',required:true},
				],
                ruleInline:{
                	gnmc: [
                    	{ required: true, message: '请将信息填写完整', trigger: 'blur' }
                    ],
                    gndm: [
                    	{ required: true, message: '请将信息填写完整', trigger: 'blur' }
                    ],
                    fwdm: [
                    	{ required: true, message: '请将信息填写完整', trigger: 'blur' }
                    ],
                    url: [
                    	{ required: true, message: '请将信息填写完整', trigger: 'blur' }
                    ],
                    px: [
                    	{ required: true, message: '请将信息填写完整', trigger: 'blur' },
                    	{ min: 0, message: '请将信息填写完整', trigger: 'blur' }
                    ],
				}
			}
		},
		created(){
		    this.util.initFormModal(this);
		    this.getDict();
            this.getLsdw();
		},
		methods: {
            getLsdw(){
                let v = this;
                v.$http.get(this.apis.TEMP_UNIT.QUERY,{params:{pageSize:1000}}).then((res) =>{
                    if (res.code == 200 && res.page.list){
                        this.lswdList = res.page.list;
                    }
                })
            },
            getDict(){
                this.cxDict = this.dictUtil.getByCode(this,this.cxDictCode);
            },
            beforeSave(){
			},
		}
	}
</script>

<style>

</style>
