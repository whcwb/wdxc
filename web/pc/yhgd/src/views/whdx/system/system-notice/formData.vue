<style lang="less">
	@import '../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900'
			:closable='false' :mask-closable="mesF"
			title="新建终端设备">
			<div style="overflow: auto;height: 300px;">
				<Form
					:model="param"
					:rules="ruleInline"
					ref="addmess"
					:label-width="100"
					:styles="{top: '20px'}">
					<Row>
						<Col span="12">
							<FormItem prop="zdbh" label='终端编号'>
								<Input :readonly="dataRead" type="text" v-model="param.zdbh" placeholder="请填写终端编号...">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="mc"  label='设备名称:'>
								<Input :readonly="dataRead" type="text" v-model="param.mc" placeholder="请填终端名称...">
								</Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem label='设备状态:' >
								<Select filterable :readonly="dataRead" filterable clearable  v-model="param.zt">
									<Option v-for="item in ztDictionary" :value="item.key">{{item.val}}</Option>
								</Select>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem label='型号:'>
								<Input :readonly="dataRead" type="text" v-model="param.xh" placeholder="请输入设备型号..."></Input>
							</FormItem>
						</Col>
						<Col span="12" v-show="dataRead">
							<FormItem label='接口地址:'>
								<Input type="text" v-model="param.cmd" placeholder="设备终端接口地址..."></Input>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			</Form>
			<div v-show="!dataRead" slot='footer'>
				<Button type="default" @click="close"style="color: #949494">取消</Button>
				<Button type="primary" @click="save('addmess')">确定</Button>
			</div>
			<div v-show="dataRead" slot='footer'>
				<Button type="default" @click="close">关闭</Button>
				<Button type="success" @click="seet('addmess')">设置</Button>
			</div>
		</Modal>
	</div>
</template>

<script>
	import treelist from '@/data/list.js'

	export default {
		name: '',
		data() {
			return {
				dataRead:false,
				showModal: true,
                mesF:false,
				param: {
                    zdbh:'',//终端编号
					mc: '',//名称
                    cs: '',//厂商
                    zt:'',//终端状态
                    xh:'',
                    cmd:''//接口地址
				},
				ruleInline: {
                  zdbh: [
                      { required: true, message: '请输入终端编号', trigger: 'blur' }
                  ],
                  mc: [
                      { required: true, message: '请输入终端名称', trigger: 'blur' }
                  ],
                  xh: [
                      { required: true, message: '请输如设备型号', trigger: 'blur' }
                  ]
              	},
				ztDictionary:[],
				ztlmdmDictionary:'ZDCLK0031'//设备状态
			}
		},
		created(){
			this.getLXDic()
		},
        mounted(){
        },
		methods: {
			getLXDic(){
                this.ztDictionary = this.dictUtil.getByCode(this,this.ztlmdmDictionary);
			},
		    save(name){
		    	var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                    	let url = this.apis.ZDGL.ADD;
						delete this.param.cl;
						delete this.param.cph;
		                this.$http.post(url,this.param).then((res) =>{
		                	if(res.code==200){
		                		v.$Message.success(res.message);
								v.close()
						        v.$parent.getPageData()
		                	}else{
		                		v.$Message.error(res.message);
		                	}
		                }).catch((error) =>{
							v.$Message.error('出错了！！！');
						})
	    		    } else {
                    	v.$Message.error('请认真填写用户信息!');
                    }
                })
			},
			bud(){
				var v = this
				swal({
			        title: "终端设备添加成功,是否绑定接口地址？",
			        text: "",
			        icon: "success",
			        buttons:['取消','确认'],
			    }).then((willDelete) => {
			            if (willDelete) {
			            	v.dataRead = true
			            }else{
			            	v.close()
			            }
			        });
			},
			seet(name){
		    	var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                    	v.$http.post(this.apis.SBZDDZ.ADD,{'deviceId':this.param.zdbh,'cmdType':91,'cmd':this.param.cmd}).then((res) =>{
                    		v.$Message.success(res.message);
		                    v.$parent.getPageData()
		                    v.close()
		                }).catch((error) =>{
							v.$Message.error('出错了！！！');
						})
	    		    } else {
                    	v.$Message.error('请认真填写用户信息!');
                    }
                })
			},
			close(){
		        this.$parent.componentName = '';
		        this.$parent.choosedRow = {}
			}

		}
	}
</script>

<style>

</style>
