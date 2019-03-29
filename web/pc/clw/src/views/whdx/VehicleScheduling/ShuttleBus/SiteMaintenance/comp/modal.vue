<template>
	<div>
		<Modal
		    v-model="showModal"
		    :closable='false'
		    width='800'
		    :mask-closable="false"
		    title="新增站点">
		    <div>
		    	<Row :gutter='30' style="margin-bottom: 15px;">
		    		<Col span="8">
		    			<Input v-model="addspot.spotName" placeholder="请输入站点名称...">
		    				<Button slot="append">地图选点</Button>
		    			</Input>
		    		</Col>
		    		<Col span="6">
		    			<Select filterable clearable  v-model="addspot.spotType">
					        <Option value="正常">正常</Option>
					        <Option value="停用">停用</Option>
					    </Select>
		    		</Col>
		    		<Col span="6">
	    				<Input v-model="addspot.spotmess" placeholder="站点范围...">
		    			</Input>
		    		</COl>
		    	</Row>
		    	<div style="height: 300px;">
		    		<my-map ref='maps' :center="mapCenter"></my-map>
		    	</div>
		    </div>
		    <div slot='footer'>
		    	<Button type="default" @click="colse"style="color: #949494">取消</Button>
	        	<Button type="primary" @click="colse">确定</Button>
		    </div>
		</Modal>
	</div>
</template>

<script>
	import myMap from '../../../../map/schoolmap.vue'

	export default{
		name:'',
		components: {
	    	myMap,
	    },
		data(){
			return{
				showModal:true,
				addspot:{
					spotName:'',
					spotType:'',
					spotmess:''
				},
				mapCenter:{
	        		lng: 114.372443,
	        		lat: 30.544572
	        	}
			}
		},
		methods:{
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
