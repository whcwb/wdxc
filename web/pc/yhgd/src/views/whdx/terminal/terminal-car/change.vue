<style lang="less">
	@import '../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900'
			:closable='false' :mask-closable="mesF"
			title="终端设备数据修改">
			<div style="overflow: auto">
				<Form
					:model="param"
					:rules="ruleInline"
					ref="addmess"
					:label-width="100"
					:styles="{top: '20px'}">
					<Row>
						<Col span="12">
							<FormItem prop="zdbh" label='终端编号'>
								<Input readonly="readonly" type="text" v-model="param.zdbh" placeholder="请填写终端编号...">
								</Input>
							</FormItem>
						</Col>
						<!--<Col span="12">-->
							<!--<FormItem prop="mc"  label='设备名称:'>-->
								<!--<Input type="text" v-model="param.mc" placeholder="请填终端名称...">-->
								<!--</Input>-->
							<!--</FormItem>-->
						<!--</Col>-->
					<!--</Row>-->
					<!--<Row>-->
						<Col span="12">
							<FormItem label='设备状态:' >
								<Select filterable clearable  v-model="param.zt">
									<Option v-for="item in ztDictionary" :value="item.key">{{item.val}}</Option>
								</Select>
							</FormItem>
						</Col>
						<!--<Col span="12">-->
							<!--<FormItem label='厂商：'>-->
								<!--<Input type="text" v-model="param.cs" placeholder="请填写厂商信息...">-->
								<!--</Input>-->
							<!--</FormItem>-->
						<!--</Col>-->
					<!--</Row>-->
					<!--<Row>-->
						<!--<Col span="12">-->
							<!--<FormItem label='型号:'>-->
								<!--<Input type="text" v-model="param.xh" placeholder="请输入设备型号..."></Input>-->
							<!--</FormItem>-->
						<!--</Col>-->
						<!--<Col span="12">
							<FormItem label='接口地址:'>
								<Input type="text" v-model="param.cmd" placeholder="设备终端接口地址..."></Input>
							</FormItem>
						</Col>-->
					</Row>
				</Form>
			</div>
			</Form>
			<div slot='footer'>
				<Button type="default" @click="close"style="color: #949494">取消</Button>
				<Button type="primary" @click="save('addmess')">确定</Button>
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
                    xh:'',//型号
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
		props:{
			mess:{}
		},
		created(){
			this.param= {
                    'zdbh':this.mess.zdbh,//终端编号
					'mc': this.mess.mc,//名称
                    'cs': this.mess.cs,//厂商
                    'zt':this.mess.zt,//终端状态
                    'xh':this.mess.xh,//型号
//                  'cmd':this.mess.cmd//接口地址
				},
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
                    	let url = this.apis.ZDGL.CHANGE;
		                this.$http.post(url,this.param).then((res) =>{
		                	if(res.code==200){
		                		v.$Message.success(res.message);
		                		v.$parent.componentName = '';
						        v.$parent.getPageData()
		                	}else{
		                		v.$Message.error(res.message);
		                		v.close()
		                	}
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
			}

		}
	}
</script>

<style>

</style>
