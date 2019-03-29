<style lang="less">
	@import '../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF' :mask-closable="mesF" :title="operate+'站牌'">
			<div style="overflow: auto;height:500px;">
				<Form
						ref="addmess"
						:model="param"
						:rules="ruleInline"
						:label-width="100"
						:styles="{top: '20px'}">
					<Row>
						<Col span="12">
							<FormItem prop='zdbh' label='终端编号'>
								<Input type="text" v-model="param.zdbh" placeholder="请填写终端编号..." :disabled="edit">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop='mc' label='站牌名称'>
								<Input type="text" v-model="param.mc" placeholder="请填写站牌名称...">
								</Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem label='型号:' placeholder="请选择站牌型号...">
								<Select filterable clearable  v-model="param.xh">
									<Option value="10">型号1</Option>
									<Option value="20">型号2</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='厂商：'>
								<Input type="text" v-model="param.cs" placeholder="请填写厂商信息...">
								</Input>
							</FormItem>
						</Col>
					</Row>

					<Row>
						<Col span="12">
							<FormItem label='地址:'>
								<Input type="text" v-model="param.dz" placeholder="请填写地址..."></Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='线路:' placeholder="请选择线路...">
								<Select filterable clearable  v-model="choosedXlIds" multiple>
									<Option v-for="item in xlList" :value="item.id" :key="item.id">{{item.xlmc}}</Option>
								</Select>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
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
			    operate:'新建',
				showModal: true,
                mesF:false,
				edit:false,
				param: {
                    zdbh:'',
					mc: '',
					xh: '',
                    cs: '',
                    dz:'',
					zdId:'',
					xlIds:'',
				},
                choosedXlIds:[],
				ruleInline: {
                  zdbh: [
                      { required: true, message: '请输入终端编号', trigger: 'blur' }
                  ],
                  mc: [
                      { required: true, message: '请输站牌名称', trigger: 'blur' }
                  ],
                  sjh:[
                      { required: true,message: '请输入手机号码', trigger: 'blur' }
                  ],
                  zjhm:[
                  	{ required: true,message: '请输入证件号码', trigger: 'blur' }
                  ]
              	},
				xlList:[],
				jgdm:''
			}
		},
		created(){
            let userInfoJson = sessionStorage.getItem("userInfo");
            let userInfo = JSON.parse(userInfoJson);
            this.jgdm = userInfo.jgdm;
			if (this.$parent.choosedRow){
				this.param = this.$parent.choosedRow;
				this.operate = '编辑'
				this.edit = true;
				if (this.param.xlIds != ''){
                    this.choosedXlIds = this.param.xlIds.split(",");
				}
            }
            this.getXlList();
            this.getHasXlIds();
		},
        mounted(){
        },
		methods: {
		    getHasXlIds(){
                this.$http.get(this.apis.ZNZP.getXlIds,{params:{zpId:this.param.zdbh}}).then((res) =>{
                    if(res.code===200 && res.result){
                        var v = this
						this.choosedXlIds = res.result;
                    }
                })
			},
		    getChoosedXlIds(){
		      this.param.xlIds = '';
		      for(let r of this.choosedXlIds){
		          this.param.xlIds += r+',';
			  }
			},
		    save(){
		    	var v = this
		    	this.$refs['addmess'].validate((valid) => {
                    if (valid) {
                        this.getChoosedXlIds();
				        let url = this.apis.ZNZP.ADD;
						if (this.$parent.choosedRow){
		                    url = this.apis.ZNZP.CHANGE;
						}
		                this.$http.post(url,this.param).then((res) =>{
		                    if(res.code===200){
		                        var v = this
		                        v.$parent.componentName = ''
		                        v.$parent.getPageData()
		                        this.$Message.success(res.message);
		                    }
		                }).catch((error) =>{
							v.$Message.error('出错了！！！');
						})
		            } else {
                        v.$Message.error('请认真填写用户信息!');
                    }
                })
			},
			getXlList(){
                this.$http.get(this.apis.XL.QUERY,{params:{jgdmStartWith:this.jgdm,pageSize:1000}}).then((res) =>{
                    if(res.code===200 && res.page.list){
                        this.xlList = res.page.list;
                    }
                })
			},
			close(){
		        let v = this;
                v.$parent.componentName = ''
                v.$parent.getPageData()
			}

		}
	}
</script>

<style>

</style>
