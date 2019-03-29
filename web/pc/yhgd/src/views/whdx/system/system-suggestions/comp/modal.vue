<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal
	        v-model="showModal"
			:closable='false'        
	        :mask-closable='false'
	        title="反馈处理">
		        <div>
		        	<Row>
		        		<Col span="6">
		        			<span>
		        				<h6>反馈人</h6>
	        					{{pams.lxrxm}}
		        			</span>
		        		</Col>
		        		<Col span="6">
		        			<span>
		        				<h6>反馈内容</h6>
		        				{{pams.nr}}
		        			</span>
		        		</Col>
		        		<Col span="6">
		        			<span>
		        				<h6>反馈类型</h6>
		        				{{pams.yjlx}}
		        			</span>
		        		</Col>
		        		<Col span="6">
		        			<span>
		        				<h6>反馈时间</h6>
		        				{{pams.cjsj}}
		        			</span>
		        		</Col>
		        	</Row>
		        	<Row class='margin-top-5'>
		        		<Col span="24">
		        			<Input v-model="mess.cljg" type="textarea" placeholder="处理信息"></Input>
		        		</Col>
		        	</Row>
		        </div>
		        <div slot='footer'>
		        	<Button type="default" @click="colse"style="color: #949494">取消</Button>
		        	<Button type="primary" @click="feedback">解决</Button>
		        </div>
		 </Modal>
	</div>
</template>

<script>

	export default{
		name:'',
		props:{
			pams:{
				type:Object,
				default:{
				}
			}
		},
		data(){
			return {
				showModal:true,
				mess:{
					cljg:'',
					yjId:this.pams.yjId,
					zt:'10'
				}
			}
		},
		methods:{
			feedback(){
				var v = this
				this.$http.post(this.apis.SUGGES.CHANGE,v.mess).then((res) =>{
					log('shuju',res)
					if(res.code===200){
						v.$Message.success('反馈解决');
					}else{
						v.$Message.error('解决失败');
					}
					v.$parent.compName = ''
					v.$parent.getmess()
				})
			},
			colse(){
				var v = this
				v.$parent.compName = ''
				log(v.$parent)
		    }
		}
	}
</script>

<style>
</style>