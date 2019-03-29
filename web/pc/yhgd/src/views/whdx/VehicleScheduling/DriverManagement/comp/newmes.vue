<template>
	<div>
		<Modal
		    v-model="showModal"
		    width='800'
		    :closable='false'
		    :mask-closable="false"
		    :title="operate+'驾驶员'">
    		<Form
    			ref="addmess"
    			:model="addmess"
    			:rules="ruleInline"
    			:label-width="100"
    			:styles="{top: '20px'}">
	    		<div style="overflow: auto;height: 600px;">
					<Row>
						<Col span="12">
							<FormItem prop="xm" label='姓名：'>
								<Input type="text" v-model="addmess.xm" placeholder="请输入姓名"></Input>
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
					</Row>
					<Row>
						<Col span="12">
							<FormItem prop="nl" label='年龄：'>
								<Input type="text" v-model="addmess.nl" placeholder="请输入年龄"></Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='驾驶证类型：'>
								<Select filterable clearable  v-model="addmess.zjcx">
									<Option value="A1">A1</Option>
									<Option value="A2">A2</Option>
									<Option value="A3">A3</Option>
									<Option value="B1">B1</Option>
									<Option value="B2">B2</Option>
									<Option value="C1">C1</Option>
									<Option value="C2">C2</Option>
								</Select>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem prop="sfzhm" label='证件号码：'>
								<Input type="text" v-model="addmess.sfzhm" placeholder="请输入证件号码">
								</Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="dabh" label='档案编号：'>
								<Input type="text" v-model="addmess.dabh" placeholder="请输入档案编号">
								</Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem prop="clrq" label='初领日期：'>
								<DatePicker v-model="clrq" format="yyyy-MM-dd" type="date" placement="bottom-end" placeholder="请输时间"  style="width: 220px"></DatePicker>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label="状态： ">
								<Select filterable clearable  v-model="addmess.zt">
									<Option value="00">正常</Option>
									<Option value="10">休息</Option>
								</Select>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem label="血型： ">
								<Select filterable clearable  v-model="addmess.xx">
									<Option value="A">A</Option>
									<Option value="B">B</Option>
									<Option value="O">O</Option>
									<Option value="AB">AB</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem prop="jkzk" label='健康状况：'>
								<Input type="text" v-model="addmess.jkzk" placeholder="健康状况"></Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="12">
							<FormItem prop="clrq" label='驾照有效期：'>
								<DatePicker v-model="jzyxq" format="yyyy-MM-dd" type="date" placement="bottom-end" placeholder="请输时间"  style="width: 220px"></DatePicker>
							</FormItem>
						</Col>
					</Row>
	    		</div>
    		</Form>
		    <div slot='footer'>
		    	<Button type="default" @click="colse"style="color: #949494">取消</Button>
	        	<Button type="primary" @click="AddDataListOk('addmess')">确定</Button>
		    </div>
		</Modal>
	</div>
</template>

<script>

    import mixins from '@/mixins'
	export default {
		name:'',
        mixins:[mixins],
		data(){
			return {
                operate:'新增',
				showModal:true,
				//新增数据
                clrq:'',
                jzyxq:'',
                addmess: {
                	xm:'',
                	xb:'1',
                	nl:'',
                	zjcx:'11',
                	sfzhm:'',
                	cdbh:'',
                	dabh:'',
                    clrq:''
                },
                ruleInline: {
                  xm: [
                      { required: true, message: '请输入驾驶员名', trigger: 'blur' }
                  ],
                  sfzhm:[
                  	{ required: true,message: '请输入身份证号码', trigger: 'blur' }
                  ],
                  clrq:[
                  	{ required: true,message: '请输入证件号码', trigger: 'blur' }
                  ],
                    dabh:[
                  	{ required: true,message: '请输入档案编号', trigger: 'blur' }
                  ],
              	},
				fleetList:[],
			}
		},

        watch:{
            clrq:function(n,o){
            	this.addmess.clrq = this.getdateParaD(n)
            },
            jzyxq:function(n,o){
            	this.addmess.jzyxq = this.getdateParaD(n)
            }
        },
		props:{
			messType:{
				type:Boolean,
				default:true
			},
			mess:{
				type:Object,
				default:{}
			}
		},
		created(){

            if(!this.messType){
            	this.addmess = this.mess
			    this.clrq = new Date(this.addmess.clrq)
            }
		},
		methods:{
			colse(){
				var v = this
				v.$parent.compName = ''
		   	},
		   //确认添加新驾驶员进行前台表单数据验证
            AddDataListOk(name){
            	var v = this
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        delete v.addmess.clDdList;
//                    	新增
                    	if(v.messType){
                    		v.$http.post(this.apis.JSY.ADD,v.addmess).then((res) =>{
								if(res.code===200){
			                    	v.$Message.success('驾驶员注册成功');
                                    v.$parent.getmess()
                                    v.$parent.compName = ''
								}else{
									v.$Message.warning(res.message);
								}
							})
                    	}else{
                    		v.$http.post(this.apis.JSY.CHANGE,v.addmess).then((res) =>{
								if(res.code===200){
									v.$Message.success('驾驶员修改成功');
                                    v.$parent.getmess()
                                    v.$parent.compName = ''
								}else{
									v.$Message.error('驾驶员修改失败');
								}
							})
                    	}
                    } else {
                        v.$Message.error('请认真填写驾驶员信息!');
                    }
                })
            },
		}
	}
</script>

<style>
</style>
