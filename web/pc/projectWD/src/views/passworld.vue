<template>
	<div>
		<Modal v-model="editPasswordModal"
			:closable='false' 
			:mask-closable=false 
			:width="500">
            <h3 slot="header" style="color:#2D8CF0">修改密码</h3>
            <Form ref="editPasswordForm" :model="editPasswordForm" :label-width="100" label-position="right" :rules="passwordValidate">
                <FormItem label="原密码" prop="oldPwd" :error="oldPwdError">
                    <Input v-model="editPasswordForm.oldPwd" type="password" placeholder="请输入现在使用的密码" ></Input>
                </FormItem>
                <FormItem label="新密码" prop="newPwd">
                    <Input v-model="editPasswordForm.newPwd" type="password" placeholder="请输入新密码，至少6位字符" ></Input>
                </FormItem>
                <FormItem label="确认新密码" prop="secPwd">
                    <Input v-model="editPasswordForm.secPwd" type="password" placeholder="请再次输入新密码" ></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="canceleditPass"style="color: #949494">取消</Button>
                <Button type="primary" :loading="savePassLoading" @click="saveeditPass">保存</Button>
            </div>
        </Modal>
	</div>
</template>

<script>

	export default{
		name:'',
		data(){
			const validePhone = (rule, value, callback) => {
	            var re = /^1[0-9]{10}$/;
	            if (!re.test(value)) {
	                callback(new Error('请输入正确格式的手机号'));
	            } else {
	                callback();
	            }
	        };
	        const validesecPwdword = (rule, value, callback) => {
	            if (value !== this.editPasswordForm.newPwd) {
	                callback(new Error('两次输入密码不一致'));
	            } else {
	                callback();
	            }
	        };
			return{
				editPasswordModal: true, // 修改密码模态框显示
            	savePassLoading: false,
            	oldPwdError: '',
            	editPasswordForm: {
	                oldPwd: '',
	                newPwd: '',
	                secPwd: ''
	            },
            	passwordValidate: {
	                oldPwd: [
	                    { required: true, message: '请输入原密码', trigger: 'blur' }
	                ],
	                newPwd: [
	                    { required: true, message: '请输入新密码', trigger: 'blur' },
	                    { min: 6, message: '请至少输入6个字符', trigger: 'blur' },
	                    { max: 32, message: '最多输入32个字符', trigger: 'blur' }
	                ],
	                secPwd: [
	                    { required: true, message: '请再次输入新密码', trigger: 'blur' },
	                    { validator: validesecPwdword, trigger: 'blur' }
	                ]
	            },
			}
		},
		methods:{
			//修改密码
        	canceleditPass () {
	            this.$parent.compName = '';
	        },
	        saveeditPass () {
	            this.$refs['editPasswordForm'].validate((valid) => {
	                if (valid) {
	                    this.savePassLoading = true;
	                    this.$http.post(this.apis.USERROOT.MODIFY_PSD,this.editPasswordForm).then((res) => {
	                        if(res.code == 200){
	                            this.$Message.success('密码改成功,请重新登陆');
	                            this.editPasswordModal = false;
	                            this. editPasswordForm = {
					                'oldPwd': '',
					                'newPwd': '',
					                'secPwd': ''
					            }
	                            this.$parent.handleClickUserDropdown()
	                        }else{
	                        	this.$Message.error(res.message);
	                        }
	                        this.savePassLoading = false;
	                    }).catch(()=>{
	                    	this.$Message.error('出错了！！！');
	                    })
	                    // you can write ajax request here
	                }
	            });
	        },
		}
	}
</script>

<style>
</style>