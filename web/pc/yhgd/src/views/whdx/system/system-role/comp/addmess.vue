<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF' :mask-closable="mesF" :title="operate+'角色'">
			<div v-if="SpinShow" style="width:100%;height:100%;position: fixed;top: 0;left:0;z-index: 1111;">
				<Spin fix>
					<Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
					<div style="font-size: 30px;">数据加载中请稍后</div>
				</Spin>
			</div>
			<div  style="height:600px;overflow: scroll;">
				<Form
	    			ref="addmess"
	    			:model="addmess"
	    			:rules="ruleInline"
	    			:label-width="100"
	    			:styles="{top: '20px'}">
                    <Row>
                        <Col span="10">
                            <FormItem prop="jsmc" label='角色名称：'>
                                <Input type="text" v-model="addmess.jsmc" placeholder="请填写角色名称">
                                </Input>
                            </FormItem>
                            <FormItem prop="jsId" label='角色代码：'>
                                <Input type="text" v-model="addmess.jsId" placeholder="请填写角色代码" :disabled="edit">
                                </Input>
                            </FormItem>
                            <FormItem label='类型：' placeholder="请选择角色类型...">
                                <Select filterable clearable  v-model="addmess.jslx">
                                    <Option v-for = '(item,index) in Dictionary' :value="item.key">{{item.val}}</Option>
                                </Select>
                            </FormItem>
                            <FormItem label='备注：'>
                                <Input type="text" v-model="addmess.sm" placeholder="请填写备注信息...">
                                </Input>
                            </FormItem>
                        </Col>
                        <Col span="10" offset="4"  style="height: 580px;overflow: scroll">
                            <FormItem label='权限选择:'>
                                <br>
                                <menu-choose v-if="showTree" :data="permissionTree" :choosedData="roleFunctionCodes" @treeChange="treeChange"></menu-choose>
                                <!--<Tree :data="permissionTree" show-checkbox multiple></Tree>-->
                            </FormItem>
                        </Col>
                    </Row>
	    		</Form>
			</div>
			<div slot='footer'>
				<Button type="default" @click="colse"style="color: #949494">取消</Button>
				<Button type="primary" @click="AddDataListOk('addmess')">确定</Button>
			</div>
		</Modal>
	</div>
</template>

<script>
	import treelist from '@/data/list.js'

    import menuChoose from '../../../components/menuChoose'

	export default {
		name: '',
		components:{menuChoose},
		data() {
			return {
				SpinShow:false,
				showModal: true,
				operate:'新建',
				mesF: false,
				addmess: {
					jsmc: '',
					jslx: '',
					sm: '',
				},
				ruleInline: {
                  	jsmc: [
                  		{ required: true, message: '请输角色名称', trigger: 'blur' }
                  	],
                  	jsId: [
                  		{ required: true, message: '请输角色代码', trigger: 'blur' }
                  	]
              	},
				permissionTree: [
                ],
				choosedIds :[],
                Dictionary:[],
                lmdmDictionary:'ZDCLK0004',
				edit:false,
                orgTree: [],
                showTree:false,
                hasPermissionCodes:[],
				roleFunctionCodes:[]
			}
		},
		props:{
			usermesType:{
				type:String,
				default:'ADD'
			},
		},
		created(){
            if(this.usermesType == 'CHANGE'){
                this.addmess = this.$parent.messdata
                this.operate = '编辑';
                this.edit = true;
			}
            this.getOrgPermissionTree();
        },
		mounted(){
            this.getLXDic();
		},
		methods: {
            treeChange(e){
                this.choosedIds = e;
            },
            getLXDic(){
                this.Dictionary = this.dictUtil.getByCode(this,this.lmdmDictionary);
            },
            getOrgPermissionTree(){
                let orgCode = '';
                if(!this.usermesType){
                    orgCode = this.$parent.messdata.jgdm;
                }else{
                    let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
                    orgCode = userInfo.jgdm;
                }
                this.permissionTree = [];
                this.$http.get(this.apis.FUNCTION.getPermissionTreeWithChecked+"?hideSystem=true&parentCode="+orgCode).then((res) =>{
                    if(res.code===200){
                        this.permissionTree = res.result;
                        if (this.usermesType == 'CHANGE'){
                            this.getRoleFunctions();
						}else{
                            this.showTree = true;
						}
                    }
                })
            },
		    getRoleFunctions(){
                let url = this.apis.FUNCTION.GET_ROLE_FUNCTIONS+"?jsdm="+this.addmess.jsId;
                this.$http.get(url).then((res) =>{
                    if(res.code === 200 && res.result){
                        for (let r of res.result){
                            this.roleFunctionCodes.push(r.gndm);
						}
						this.choosedIds = this.roleFunctionCodes;
                    }
                    this.showTree = true;
                })
			},
			setPermissionChecked(list){
                for (let r of list){
                    if (this.roleFunctionCodes.indexOf(r.gndm) >= 0){
                        r.checked = true;
					}
					if (r.children && r.children.length > 0){
                        this.setPermissionChecked(r.children);
                    }
				}
			},
		    setRolePermission(){
                this.getChoosedIds(this.permissionTree);
                let ids = '';
                for (let r of this.choosedIds){
                    ids += r+',';
				}
                this.$http.post(this.apis.FUNCTION.SET_ROLE_FUNCTIONS,{'jsdm':this.addmess.jsId,'gndms':ids}).then((res) =>{
                    if(res.code===200){
                        this.$Message.success(res.message);
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
                        let url = this.apis.ROLE[v.usermesType]
                        v.$http.post(url,v.addmess).then((res) =>{
                            if(res.code===200){
                                this.setRolePermission();
                                v.$emit('listF',res)
                            }else{
                                v.$Message.error(res.message);
                            }
                            v.SpinShow = false
                        }).catch((error) =>{
                            v.$Message.error('出错了！！！');
                            v.SpinShow = false
                        })
                    } else {
                    	v.SpinShow = false
                        v.$Message.warning('请认真填写角色信息!');
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
