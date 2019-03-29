<template>
	<div>
		<Modal
		    v-model="showModal"
			height="600"
			width="900"
		    :closable='false'
		    :mask-closable="false"
		    :title="operate+'用户'">
    		<Form
    			ref="addmess"
    			:model="addmess"
    			:rules="ruleInline"
    			:label-width="100"
    			:styles="{top: '20px'}">
	    		<div style="overflow: auto;height: 400px;width:800px">
					<Row>
						<Col span="12">
							<FormItem prop="jgdm" label='组织机构：'>
								<Cascader @on-change="change" v-model="orgPath" change-on-select :data="orgTree"  placeholder="请选择组织机构"  filterable clearable  ></Cascader>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="zh" label='用户名：'>
								<Input :readonly="!usermesType" type="text" v-model="addmess.zh" placeholder="请设置用户帐号">
								</Input>
							</FormItem>
						</Col>
						<Col span="12" v-if="showPsd">
							<FormItem prop="mm" label='密码：'>
								<Input type="password" v-model="addmess.mm" placeholder="请设置用户密码">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="xm" label='姓名：'>
								<Input type="text" v-model="addmess.xm" placeholder="请输入姓名">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label="性别： ">
								<Select filterable clearable  v-model="addmess.xb">
									<Option value="1">男</Option>
									<Option value="0">女</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='类型：'>
								<Select filterable clearable  v-model="addmess.lx">
									<Option v-for="item in yhlxDict" :value="item.key">{{item.val}}</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='职务：'>
								<Input type="text" v-model="addmess.zw" placeholder="请输入职务">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="sjh" label='手机号码：'>
								<Input type="text" v-model="addmess.sjh" placeholder="请输入手机号码">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="zjhm" label='证件号码：'>
								<Input type="text" v-model="addmess.zjhm" placeholder="请输入证件号码">
								</Input>
							</FormItem>
						</Col>
					</Row>
	    		</div>
    		</Form>
		    <div slot='footer'>
		    	<Button type="default" @click="colse" style="color: #949494">取消</Button>
	        	<Button type="primary" @click="AddDataListOk('addmess')">确定</Button>
		    </div>
		</Modal>
	</div>
</template>

<script>


	export default {
		name:'',
		data(){
			return {
				showModal:true,
				operate:"新增",
				//新增数据
            	addmess: {
                    zh: '',
                    xm:'',
                    mm:'',
                    lx:'',
                    xb:'0',
                    zw:'',
                    sjh:'',
                    zjhm:'',
                    jgdm:''
                },
                showPsd:false,
                ruleInline: {
                  zh: [
                      { required: true, message: '请输入用户名', trigger: 'blur' }
                  ],
                  xm: [
                      { required: true, message: '请输入姓名', trigger: 'blur' }
                  ],
                  sjh:[
                      { required: true,message: '请输入手机号码', trigger: 'blur' }
                  ],
              	},
                yhlxDict:[],
                yhlxDictCode:'ZDCLK0003',
				orgList:[],
                treeValue:[],
                orgTree:[],
                orgPath:[],
			}
		},
		props:{
			usermesType:{
				type:Boolean,
				default:true
			},
			usermes:{
				type:Object,
				default:{}
			}
		},
		created(){
			this.fullcal()
            if(this.usermesType){
                this.showPsd = true;
            }else{
                this.addmess = this.usermes
                this.operate = '编辑'
                this.getOrgPath(this.addmess.jgdm);
			}
			this.yhlxDict = this.$parent.yhlxDict
            this.getOrgTree();
        },
		methods:{
            change(vaule,selectedData){
                this.addmess.jgdm=selectedData[selectedData.length-1].value
                this.treeValue = vaule;
            },
			getOrgPath(orgCode){
                this.orgPath = [];
                this.$http.get(this.apis.FRAMEWORK.getOrgPath+'?orgCode='+orgCode).then((res)=>{
                    if (res.code == 200){
                        for(let r of res.result){
                            this.orgPath.push(r.jgdm);
                        }
					}
				})
			},
            getOrgTree(){
                this.$http.get(this.apis.FRAMEWORK.getCurrentOrgTree).then((res) =>{
                    this.orgTree = res.result
                })
            },
		    getOrgList(){
		        let v = this;
                v.$http.get(this.apis.FRAMEWORK.QUERY,{params:{pageSize:10000}}).then((res) =>{
                    if(res.code===200){
                        this.orgList = res.page.list;
                    }else{
                        v.$Message.error(res.message);
                    }
                }).catch((error) =>{
                    v.$Message.error('出错了！！！');
                })
			},
			fullcal(){
				log('信息',this.usermes)
			},
			colse(){
				var v = this
				v.$parent.compName = ''
		   	},
		   //确认添加新用户进行前台表单数据验证
            AddDataListOk(name){
            	var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
//                    	新增
                    	if(v.usermesType){
                    		v.$http.post(this.apis.USER.ADD,v.addmess).then((res) =>{
								if(res.code===200){
			                    	v.$Message.success('用户注册成功');
									v.$emit('listF',res)
								}else{
                                    v.$Message.error(res.message);
								}
							}).catch((error) =>{
								v.$Message.error('出错了！！！');
							})
                    	}else{
                    	    delete v.addmess.mm;
                    		v.$http.post(this.apis.USER.CHANGE,v.addmess).then((res) =>{
								if(res.code===200){
									v.$Message.success('用户修改成功');
									v.$emit('listF',res)
								}else{
                                    v.$Message.error(res.message);
								}
							}).catch((error) =>{
								v.$Message.error('出错了！！！');
							})
                    	}
                    } else {
                        v.$Message.error('请认真填写用户信息!');
                    }
                })
            },
		}
	}
//15271928827
</script>

<style>
</style>
