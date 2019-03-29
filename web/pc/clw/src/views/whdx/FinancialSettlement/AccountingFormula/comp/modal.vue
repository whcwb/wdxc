<template>
	<div>
		<Modal
		    v-model="showModal"
		    :closable='false'
		    width='800'
		    :mask-closable="false"
		    title="核算公式">
		    <div>
		    	<Row :gutter= '15'>
		    		<Col span="6">
		    			<Select filterable clearable  v-model="addlist.carmodal" placeholder="请选择车型...">
					        <Option value="5人坐">5人坐</Option>
					        <Option value="7人坐">7人坐</Option>
					        <Option value="45人坐">45人坐</Option>
					        <Option value="48人坐">48人坐</Option>
					    </Select>
		    		</Col>
		    		<!--类型-->
		    		<Col span="6">
		    			<Select filterable clearable  v-model="addlist.cartype" placeholder="请选择核算类型...">
					        <Option value="里程">里程</Option>
					        <Option value="加班">加班</Option>
					        <Option value="节假日">节假日</Option>
					    </Select>
		    		</Col>
		    		<!--内容-->
		    		<Col span="6">
		    			<Input v-model="addlist.spotmess" placeholder="请填写核算基数..." :disabled="addlist.cartype=='节假日'">
		    			</Input>
		    		</Col>
		    		<!--金额-->
		    		<Col span="6">
		    			<Input v-model="addlist.spotmoney" placeholder="请填写核算单价...">
		    			</Input>
		    		</Col>
		    	</Row>
		    </div>
		    <div slot='footer'>
		    	<Button type="default" @click="colse"style="color: #949494">取消</Button>
	        	<Button type="primary" @click="colse">确定</Button>
		    </div>
		</Modal>
	</div>
</template>

<script>
	export default{
		name:'',
		data(){
			return{
				showModal:true,
				addlist:[
					{
						carmodal:'',
						cartype:'',
						spotmess:'',
						spotmoney:''
					}
				]
			}
		},
		methods:{
			colse(){
				var v = this
				v.$parent.compName = ''
		    }
		}
	}
</script>

<style>
</style>
