<style lang="less">
    @import "../../../../../styles/common.less";
</style>
<template>
	<div>
		<Modal
		    v-model="showModal"
		    width='800'
		    :closable='false'
		    :mask-closable="false"
		    title="超速设定">
    		<Form
    			ref="addmess"
    			:model="addmess"
    			:rules="ruleInline"
    			:label-width="100"
    			:styles="{top: '20px'}">
	    		<div style="height: 300px;">
	    			<div class="box-row">
	    				<div style="overflow: auto;padding: 5px 8px;">
	    					<div v-show="treeList.length==0" style="color: red;">
	    						*请选车量
	    					</div>
	    					<Tree :data="data1"
						  		show-checkbox
						 	 	@on-check-change='checkClick'></Tree>
	    				</div>
	    				<div class="body-F">
	    					<FormItem prop="sdsx" label='速度上限：'>
								<Input type="text" v-model="addmess.sdsx" placeholder="请设置速度上限">
								</Input>
							</FormItem>
	    				</div>
	    			</div>
	    		</div>
    		</Form>
		    <div slot='footer'>
		    	<Button type="default" @click="colse"style="color: #949494">取消</Button>
	        	<Button type="primary" @click="AddDataListOk('addmess')">确定</Button>
		    </div>
		</Modal>
	</div>
</template>

<script>

	
	export default {
		name:'',
		data(){
			return {
                operate:'新建',
				showModal:true,
				//新增数据
            	addmess: {
                    cph: '',
                    sdsx:'',
                },
                ruleInline: {
                  sdsx: [
                      { required: true, message: '请输车数上线', trigger: 'blur' }
                  ],
              	},
              	data1:[],
              	treeList:[]
			}
		},
		created(){
			this.fullcal()
            this.getCarTree()
		},
		methods:{
			//获取车辆树
			getCarTree(){
	    		this.$http.get(this.apis.CARTREE.QUERY,{params:{'zxzt':'00'}}).then((res) =>{
	    			log('数据结构数据',res)
	    			this.data1 = res.result
	        	}).catch((error) =>{
	        		log('error',error)
	        	})
	    	},
			//树多选框
	    	checkClick(event){
	    		log('2',event)
	    		var v = this
	    		v.treeList = []
	    		for( var i = 0 ; i<event.length;i++){
	    			if(event[i].children){
	    				log('树输出')
	    			}else{
	    				v.treeList.push(event[i].title)
	    				log('车牌号',v.treeList)
	    				log('车牌转',v.treeList.join(','))
	    			}
	    		}
	    	},
			fullcal(){
				log('信息',this.mess)
			},
			colse(){
				var v = this
				v.$parent.compName = ''
		   	},
		   //确认添加新用户进行前台表单数据验证
            AddDataListOk(name){
                var v = this
                this.$refs[name].validate((valid) => {
                    if (valid && v.treeList.length>0) {
//                    	新增
	                		v.$http.post(this.apis.CS.ADD,{'cphs':v.treeList.join(','), 'csz':v.addmess.sdsx}).then((res) =>{
								if(res.code===200){
									v.$parent.getmess();
			                    	v.$Message.success(res.message);
			                    	v.colse()
								}else{
	                                v.$Message.warning(res.message);
								}
							}).catch((e)=>{
	                            v.$Message.error("失败了！！！");
							})
                    } else {
                        v.$Message.error('请认真填写超速信息!');
                    }
                })
            }
		}
	}
</script>

<style>
</style>