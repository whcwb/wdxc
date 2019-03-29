<style lang="less">
	@import '../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900'
			:closable='false' :mask-closable="mesF"
			title="在线升级">
			<div style="overflow: auto;">
				<Form
					:model="param"
					:rules="ruleInline"
					ref="addmess"
					:label-width="100"
					:styles="{top: '20px'}">
					<Row>
						<Col v-if="param.zdbh != ''" span="12">
							<FormItem label='终端编号'>
								<Input :readonly="dataRead" type="text" v-model="param.zdbh" placeholder="请填写终端编号..."></Input>
							</FormItem>
						</Col>
						<Col span="24" v-show="dataRead">
							<FormItem label='接口地址:'>
								<Input type="text" v-model="param.cmd" placeholder="设备终端接口地址..."></Input>
							</FormItem>
						</Col>
						<Col span="24">
							<FormItem label='当前版本:'>
								<Input type="text" v-model="versionInfo" placeholder="..." readonly></Input>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			<div slot='footer'>
				<Button type="default" @click="close">关闭</Button>
				<Button type="success" @click="seet('addmess')">确定</Button>
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
				dataRead:true,
				showModal: true,
                mesF:false,
				versionInfo:'',
				updateMode:'single',
				param: {
                    zdbh:'',//终端编号
				},
				ruleInline: {
              	},
			}
		},
		props:{
			mess:{}
		},
		created(){
		    if (this.$parent.choosedRow){
                this.param.zdbh = this.$parent.choosedRow.zdbh;
                this.getVersionInfo();
			}
		    this.updateMode = this.$parent.updateMode;
		},
        mounted(){
        },
		methods: {
			seet(name){
		    	var v = this
				let url = ''
				let p = {};
		    	if (this.updateMode === 'batch'){
		    	    let userInfoJson = sessionStorage.getItem("userInfo");
                    let userInfo = JSON.parse(userInfoJson);
                    let jgdm = userInfo.jgdm;
		    	    p = this.$parent.param;
		    	    p.jgdm = jgdm;
		    	    url = this.apis.SBZDDZ.batchUpdate;
				}else{
                    url = this.apis.SBZDDZ.ADD
                    p = {'deviceId':this.param.zdbh,'cmdType':90,'cmd':this.param.cmd};
				}
            	v.$http.post(url,p).then((res) =>{
            	    if(res.code == 200){
                        v.$Message.success(res.message);
                        v.$parent.getPageData()
                        v.close()
					}else{
                        v.$Message.error(res.message);
					}
                }).catch((error) =>{
				})
			},
			close(){
		        this.$parent.componentName = '';
			},
			getVersionInfo(deviceId){
			    let v = this;
                v.$http.get(this.apis.SBZDDZ.getVersionInfo,{params:{deviceId:this.param.zdbh}}).then((res) =>{
                    this.versionInfo = res.message;
                }).catch((error) =>{
                })
			}

		}
	}
</script>

<style>

</style>
