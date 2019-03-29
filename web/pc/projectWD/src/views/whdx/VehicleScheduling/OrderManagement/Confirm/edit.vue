<style lang="less">
	@import '../../../../../styles/common.less';
</style>
<template>
	<div>
		<Modal v-model="showModal" width='1200' :closable='mesF' :mask-closable="mesF" title="编辑订单">
			<div v-if="SpinShow" style="width:100%;height:100%;position: fixed;top: 0;left:0;z-index: 1111;">
				<Spin fix>
					<Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
					<div style="font-size: 30px;">数据加载中请稍后</div>
				</Spin>
			</div>
			<div style="overflow: auto;height: 400px;">
				<Form :model="param" :label-width="80">
					<Row :gutter="16">
						<Col span="8">
							<FormItem label="用车单位">
								<Select disabled filterable clearable  v-model="param.jgdm" size="large" placeholder="请选择用车单位" filterable>
									<Option v-for="item in jgdmList" :value="item.jgdm">{{item.jgmc}}</Option>
								</Select>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="用车人">
								<Input v-model="param.ck" size="large" placeholder="请填写用车人姓名"></Input>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="客户电话">
								<Input v-model="param.cklxdh" size="large" placeholder="请填写用车人电话"></Input>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="出车时间">
								<DatePicker v-model="param.yysj" size="large"  format="yyyy-MM-dd HH:mm:ss" type="datetime" placement="bottom-end" placeholder="请填写用车时间" ></DatePicker>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="候车地点">
								<Input v-model="param.hcdz" size="large" placeholder="请填写候车地点..."></Input>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="目的地">
								<Input v-model="param.mdd" size="large" placeholder="请填写目的地点"></Input>
							</FormItem>
						</Col>
						<Col span="8">
							<Row>
								<Col span="12">
									<FormItem label="费用来源">
										<Select filterable clearable  v-model="param.fkfs" size="large" placeholder="请选择费用来源" filterable>
											<Option v-for="item in fromMoneyList" :value="item.key">{{item.val}}</Option>
										</Select>
									</FormItem>
								</Col>
								<Col span="12">
									<FormItem label="单据类型">
										<Select v-model="param.wf" filterable clearable  size="large" placeholder="请选择单据类型" filterable>
											<Option value="00">单程</Option>
											<Option value="10">往返</Option>
										</Select>
									</FormItem>
								</Col>
							</Row>
							<Row v-if="param.fkfs=='20'">
								<Col span="12">
									<FormItem label="课题">
										<Input
												v-if="param.ktcode=='添加课题'"
												v-model="param.newtask"
												size="large"
												placeholder="添加课题"></Input>
										<Select filterable clearable
												v-else
												v-model="param.ktcode" size="large" placeholder="请选择用车单位" filterable>
											<Option v-for="item in ctasklList" :value="item.value"></Option>
										</Select>
									</FormItem>
								</Col>
								<Col span="12">
									<FormItem label="车型">
										<Cascader @on-change="changeCLLX" :data="CasData" v-model="cllx" ></Cascader>
										<!--<Select filterable clearable  v-model="param.zws" size="large" placeholder="请选择用车单位" filterable>-->
											<!--<Option v-for="item in zwsList" :value="item.value"></Option>-->
										<!--</Select>-->
									</FormItem>
								</Col>
							</Row>
							<Row v-else	>
								<FormItem label="车型">
									<Cascader @on-change="changeCLLX" :data="CasData" v-model="cllx" ></Cascader>
									<!--<Select filterable clearable  v-model="param.zws" size="large" placeholder="请选择用车单位" filterable>-->
										<!--<Option v-for="item in zwsList" :value="item.value"></Option>-->
									<!--</Select>-->
								</FormItem>
							</Row>
						</Col>
						<Col span="16">
							<FormItem label="事由">
								<Input v-model="param.sy" type="textarea" :rows="6" placeholder="请填写用车事由"></Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="8">
							<FormItem label="行车里程">
								<Input v-model="param.lc" type="text" :rows="6" placeholder="请填写行车里程"></Input>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="里程单价">
								<Input v-model="param.dj" type="text" :rows="6" placeholder="请填写里程单价"></Input>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="里程费用">
								<Input v-model="param.jcf" type="text" :rows="6" placeholder="请填写里程费用"></Input>
							</FormItem>
						</Col>
					</Row>
					<Row>
						<Col span="8">
							<FormItem label="结束时间">
								<DatePicker v-model="param.sjqrsj" format="yyyy-MM-dd HH:mm:ss" type="datetime" placement="top-end" placeholder="请填写结束时间" ></DatePicker>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="过路费">
								<Input v-model="param.glf" type="text" :rows="6" placeholder="请填写过路费"></Input>
							</FormItem>
						</Col>
						<Col span="8">
							<FormItem label="总费用">
								<Input v-model="param.zj" type="text" :rows="6" placeholder="请填写总费用"></Input>
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
                CasData:[{
                    value: '20',
                    label: '大车',
                    children: [{
                        value: '20',
                        label: '20',
                    },{
                        value: '32',
                        label: '32',
                    },{
                        value: '45',
                        label: '45',
                    },{
                        value: '48',
                        label: '48',
                    }]
                }, {
                    value: '10',
                    label: '小车',
                    disabled: false,
                    children: [{
                        value: '5',
                        label: '5',
                    },{
                        value: '7',
                        label: '7',
                    },{
                        value: '11',
                        label: '11',
                    }]
                }],
                jgdmList:[],
                ruleInline:{

                },
                addmess:{

                },
				cllx:[],
                fromMoneyList:[
                ],
                ctasklList:[
                    {
                        value:'添加课题',
                    },
                    {
                        value:'H012菌课题',
                    },
                    {
                        value:'甲骨文课题',
                    }
                ],
                zwsList:[
                    {
                        value:'4'
                    },
                    {
                        value:'7'
                    },
                    {
                        value:'45'
                    },{
                        value:'48'
                    }
                ]

            }
        },
        created(){
            if (this.$parent.choosedRow){
                this.param = this.$parent.choosedRow;
                this.operate = '编辑';
                this.cllx = [this.param.cllx,''+this.param.zws];
            }
        },
        mounted(){
            this.getOrgList();
            this.fromMoneyList = this.dictUtil.getByCode(this,'ZDCLK0043')
        },
        methods: {
            changeCLLX(v,s){
                this.param.cllx=v[0]
                this.param.zws=v[1]
            },
            getOrgList(){
                this.$http.get(this.apis.FRAMEWORK.getSubOrgList).then((res) =>{
                    this.jgdmList = res.result
                })
            },
            save(){
                let v = this
                this.SpinShow = true
                let url = this.apis.ORDER.CHANGE;
                this.$http.post(url,this.param).then((res) =>{
                    this.SpinShow = false
                    if(res.code===200){
                        var v = this
                        v.$parent.findMessList()
                        this.$Message.success(res.message);
                        v.$parent.componentName = ''
                    }else{
                        this.$Message.error(res.message);
                    }
                // }).catch((error) =>{
                //     v.$Message.error('出错了！！！');
                //     v.SpinShow = false
                })
            },
            close(){
                let v = this;
                v.$parent.componentName = ''
                v.$parent.findMessList()
            }

        }
    }
</script>

<style>

</style>
