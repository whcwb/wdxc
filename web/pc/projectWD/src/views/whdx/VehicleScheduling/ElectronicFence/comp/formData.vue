<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='900' :closable='mesF' :mask-closable="mesF" :title="operate+'站牌'">
			<div v-if="SpinShow" style="width:100%;height:100%;position: fixed;top: 0;left:0;z-index: 1111;">
				<Spin fix>
					<Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
					<div style="font-size: 30px;">数据加载中请稍后</div>
				</Spin>
			</div>
			<div style="overflow: auto;height: 300px;">
				<Form
						ref="addmess"
						:model="addmess"
						:rules="ruleInline"
						:label-width="100"
						:styles="{top: '20px'}">
					<Row>
						<Col span="12">
							<FormItem label='围栏名称'>
								<Input type="text" v-model="param.wlmc" placeholder="请填写围栏名称..."></Input>
							</FormItem>
						</Col>
						<Col span="12">
							<FormItem label='状态'>
								<Select v-model="param.zt">
									<Option value="00">正常</Option>
									<Option value="10">停用</Option>
								</Select>
							</FormItem>
						</Col>
					</Row>
				</Form>
			</div>
			<div slot='footer'>
				<Button type="default" @click="close"style="color: #949494">取消</Button>
				<Button type="primary" @click="save">确定</Button>
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
				SpinShow:false,
			    operate:'新建',
				showModal: true,
                mesF:false,
				param: {
                    zdbh:'',
					mc: '',
					xh: '',
                    cs: '',
                    dz:'',
				},
                ruleInline:{

				},
                addmess:{

				}

			}
		},
		created(){
			if (this.$parent.choosedRow){
				this.param = this.$parent.choosedRow;
				this.operate = '编辑'
			}
		},
        mounted(){
        },
		methods: {
		    save(){
		    	var v = this
            	v.SpinShow = true
		        let url = this.apis.DZWL.ADD;
				if (this.$parent.choosedRow){
                    url = this.apis.DZWL.CHANGE;
				}
                this.param.wlmj = 1000;
                this.$http.post(url,this.param).then((res) =>{
                    if(res.code===200){
                        var v = this
                        v.$parent.findMessList()
                        this.$Message.success(res.message);
                        v.SpinShow = false
                        v.$parent.componentName = ''
                    }
                }).catch((error) =>{
					v.$Message.error('出错了！！！');
					v.SpinShow = false
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
