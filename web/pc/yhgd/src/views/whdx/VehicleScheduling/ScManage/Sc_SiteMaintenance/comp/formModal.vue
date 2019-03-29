<template>
	<div>
		<Modal
		    v-model="showModal"
		    :closable='false'
		    width='800'
		    :mask-closable="false"
		    title="新增站点">
			<Form
					ref="addmess"
					:model="formItem"
					:rules="ruleInline"
					:label-width="100"
					:styles="{top: '20px'}">
		    <div>
				<Row>
					<Col span="8">
						<FormItem prop="mc" label='站点名称：'>
							<Input v-model="formItem.mc"
								   placeholder="请输入站点名称...">
							</Input>
						</FormItem>
					</Col>
					<Col span="8">
						<FormItem label='站点范围：'>
							<Select filterable clearable  v-model="formItem.fw"
									placeholder="请选择站点范围...">
								<Option v-for="item in fwList" :value="item.value">{{item.label}}</Option>
							</Select>
						</FormItem>
					</Col>
					<Col span="8">
						<FormItem prop="zkl" label='纬度：'>
							<Input v-model="formItem.wd"
								   readonly="readonly"
								   placeholder="用鼠标选取站点坐标">
							</Input>
						</FormItem>
					</Col>
					<Col span="8">
						<FormItem prop="sjxm" label='经度：'>
							<Input v-model="formItem.jd"
								   readonly="readonly"
								   placeholder="用鼠标选取站点坐标"></Input>
						</FormItem>
					</Col>
					<Col span="8">
						<FormItem prop="zt" label='站点状态：'>
							<Select filterable clearable  v-model="formItem.zt"
									placeholder="请选择站点状态...">
								<Option value="00">正常</Option>
								<Option value="10">停用</Option>
							</Select>
						</FormItem>
					</Col>
					<Col span="8">
						<FormItem prop="cdbh" label='备注信息：'>
							<Input v-model="formItem.bz" placeholder="备注信息...">
							</Input>
						</FormItem>
					</Col>
				</Row>
		    	<div style="height: 400px;">
		    		<get-map-dot ref='maps'
		    			:center="mapCenter"
		    			@getDot="getDot"></get-map-dot>
		    	</div>
		    </div>
			</Form>
		    <div slot='footer'>
		    	<Button type="default" @click="close"style="color: #949494">取消</Button>
	        	<Button type="primary" @click="save">确定</Button>
		    </div>
		</Modal>
	</div>
</template>

<script>
	import getMapDot from '../../../../map/getMapDot.vue'


	export default{
		name:'',
		components: {
	    	getMapDot,
	    },
		data(){
			return{
				showModal:true,
				formItem:{
                    mc:'',
                    fw:50,
				    jd:'',
                    zt:'',
                    bz:'',
					wd:'',
                    lx:'30'
				},
				mapCenter:{
	        		lng: 114.372443,
	        		lat: 30.544572
	        	},
                fwList:[
                    {label:10,value:10},
                    {label:20,value:20},
                    {label:30,value:30},
                    {label:40,value:40},
                    {label:50,value:50},
                ],
                ruleInline: {
                    mc: [
                    	{ required: true, message: '请输入站点名称', trigger: 'blur' }
                	],

            },
			}
		},
		created(){
		},
		mounted(){
            if (this.$parent.choosedRow !== null){
                this.formItem = this.$parent.choosedRow;
                this.addDot(this.formItem.jd,this.formItem.wd);
            }
        },
		methods:{
			close(){
				var v = this
				v.$parent.componentName = ''
                v.$parent.getmess();
		    },
			save(){
                var v = this
                this.$refs['addmess'].validate((valid) => {
                    if (valid) {
                        v.$http.post(this.apis.ZD.ADD,v.formItem).then((res) =>{
                            if(res.code===200){
                                v.$Message.success(res.message);
                            }else{
                                v.$Message.warning(res.message);
                            }
                        }).then((res)=>{
                            v.close();
                        }).catch((e)=>{
                            log(e);
                            v.$Message.error("失败了！");
                        })
                } else {
                        v.$Message.error('请认真填写信息!');
                    }
                    })
            },
		    getDot(e){
                this.formItem.jd = e.point.lng
        		this.formItem.wd = e.point.lat
            },
		    addDot(lng,lat){
			    this.$refs['maps'].addPoint(lng,lat);
            }
		}
	}
</script>

<style>
</style>
