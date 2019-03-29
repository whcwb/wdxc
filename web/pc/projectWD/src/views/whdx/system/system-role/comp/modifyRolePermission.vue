<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF' :mask-closable="mesF" title="新建角色">
			<div v-if="SpinShow" style="width:100%;height:100%;position: fixed;top: 0;left:0;z-index: 1111;">
				<Spin fix>
					<Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
					<div style="font-size: 30px;">数据加载中请稍后</div>
				</Spin>
			</div>
			<div style="overflow: auto;height: 300px;">
				


			</div>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">取消</Button>
				<Button type="primary" @click="AddDataListOk('addmess')">确定</Button>
			</div>
		</Modal>
	</div>
</template>

<script>


    export default {
        name: '',
        data() {
            return {
            	SpinShow:false,
                showModal: true,
                mesF: false,
                addmess: {
                    jsmc: '',
                    jslx: '',
                    sm: '',
                },
                ruleInline: {
                    jsmc: [
                        { required: true, message: '请输角色名称', trigger: 'blur' }
                    ]
                },
                data4: [
                ],
                choosedIds :[],
				hasPermissions:[],
            }
        },
        props:{
            usermesType:{
                type:Boolean,
                default:true
            },
            messdata:{
                type:Object,
                default:{}
            }
        },
        created(){
            this.addmess = this.messdata
        },
        mounted(){
            this.getRolePermission();
        },
        methods: {
            getPermissionTree(){
                this.$http.get(this.apis.FUNCTION.GET_ORG_PERMISSION_TREE).then((res) =>{
                    if(res.code===200){
                        this.data4 = res.result[0].functions;
                    }
                })
            },
			getRolePermission(){
                this.$http.get(this.apis.FUNCTION.GET_ROLE_FUNCTIONS+"?jsdm="+this.addmess.jsId).then((res) =>{
                    if(res.code===200){
                        this.hasPermissions = res.result;
                        this.getPermissionTree();
                    }
                })
			},
            setRolePermission(){
                this.getChoosedIds(this.data4);
                this.$http.post(this.apis.FUNCTION.SET_ROLE_FUNCTIONS,{'jsdm':this.addmess.jsId,'gndms':this.choosedIds}).then((res) =>{
                    if(res.code===200){
                        v.$Message.success('修改成功');
                    }
                })
            },
            getChoosedIds(list){
                for(let r of list){
                    if (r.checked)this.choosedIds.push(r.gndm);
                    if (r.children){
                        this.getChoosedIds(r.children);
                    }
                }
            },
            AddDataListOk(name){
                var v = this
                v.SpinShow = true
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        if(v.usermesType){
                            v.$http.post(this.apis.ROLE.ADD,v.addmess).then((res) =>{
                                if(res.code===200){
                                    v.$Message.success('角色注册成功');
                                    this.setRolePermission();
                                    v.$emit('listF',res)
                                    v.SpinShow = false
                                }
                            }).catch((error) =>{
								v.$Message.error('出错了！！！');
								v.SpinShow = false
							})
                        }else{
                            v.$http.post(this.apis.ROLE.CHANGE,v.addmess).then((res) =>{
                                if(res.code===200){
                                    v.$Message.success('角色修改成功');
                                    this.setRolePermission();
                                    v.$emit('listF',res)
                                    v.SpinShow = false
                                }
                            }).catch((error) =>{
								v.$Message.error('出错了！！！');
								v.SpinShow = false
							})
                        }
                    } else {
                    	v.SpinShow = false
                        v.$Message.error('请认真填写角色信息!');
                    }
                })
            },
            colse() {
                var v = this
                v.$parent.compName = ''
            }
        }
    }
</script>

<style>

</style>
