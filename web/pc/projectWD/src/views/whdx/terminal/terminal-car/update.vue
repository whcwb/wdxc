<style lang="less">
	@import '../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900'
			   :closable='false' :mask-closable="mesF"
			   :title='$t("ON_LINE_UPGRADE")'>
			<div style="overflow: auto;">
				<Form
						:model="param"
						:rules="ruleInline"
						ref="addmess"
						:label-width="100"
						:styles="{top: '20px'}">
					<Row>
						<Col v-if="param.zdbh != ''" span="12">
							<FormItem :label='$t("TERMINAL_NUMBER")'>
								<Input :readonly="dataRead" type="text" v-model="param.zdbh" :placeholder='$t("TERMINAL_NUM")'></Input>
							</FormItem>
						</Col>
						<Col span="24" v-show="dataRead">
							<FormItem :label='$t("INTERFACE_ADDRESS_TAB")'>
								<Input type="text" v-model="param.cmd" :placeholder='$t("TERMINAL_INTERFACE_ADDRESS")'></Input>
							</FormItem>
						</Col>
						<Col span="24">
							<FormItem :label='$t("CURRENT_VERSION")'>
								<Input type="text" v-model="versionInfo" placeholder="..." readonly></Input>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			<div slot='footer'>
				<Button type="default" @click="close">{{$t("CLOSE")}}</Button>
				<Button type="success" @click="seet('addmess')">{{$t("DETERMINE")}}</Button>
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
                    p = {jgdm:jgdm}
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
