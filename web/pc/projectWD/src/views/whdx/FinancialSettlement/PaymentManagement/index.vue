<!--
	收款管理
-->
<style lang="less">
    @import '../../../../styles/common.less';
</style>
<template>
	<div class="box">
		<Row class="tit" style="height: 60px;">
			<Col span="6">
				<Menu mode="horizontal" theme="light" active-name="1" @on-select="MenuClick">
			        <MenuItem name="1">
			            <Icon type="ios-paper"></Icon>
						{{$t("PAYABLE")}}
			        </MenuItem>
			        <MenuItem name="2">
			            <Icon type="android-checkbox-outline"></Icon>
						{{$t("PAID")}}
			        </MenuItem>
			    </Menu>
		    </Col>
		    <Col span="6">
		    	<div style="height: 60px;line-height: 60px;background-color: #fff;border-bottom: 1px solid #dddee1;padding: 0 15px;">
		    		<Input  v-model="param.sjxm" :placeholder='$t("SEARCH_DRIVER")' style="width: 100%;" @input="getData"></Input>
		    	</div>
		    </Col>
		    <Col span="9">
		    	<div style="height: 60px;line-height: 60px;background-color: #fff;border-bottom: 1px solid #dddee1;padding: 0 15px;">
					{{$t("PAY_FORMULA")}}
		    	</div>
		    </Col>
		    <Col span="3">
		    	<div style="height: 60px;line-height: 60px;background-color: #fff;border-bottom: 1px solid #dddee1;padding: 0 15px;">
		    		<div v-show="param.fkzt === '00'">
						{{$t("PAYABLE")}}：{{list.length}}
		    		</div>
		    		<div v-show="param.fkzt === '10'">
						{{$t("PAID")}}：{{list.length}}
		    		</div>
		    	</div>
		    </Col>
		</Row>
		<Row :gutter="16" class="margin-top-10 body clientList"  v-for="(item,index) in list" >
			<Col span="24" :lg="24" :md="24" :sm="24" :xs="24" class="margin-top-10">
				<Card style="width:100%" :id="'order_'+item.orderId">
			        <div slot="title">
			            <Icon type="md-person"></Icon>
			            	{{item.driverName}}
			        </div>
			        <span slot="extra">
			        	<span>
			        		{{$t("AMOUNT_COLLECT")}}：{{item.amount}}
			        		<Button type="success" size="small" @click="print(item,index)">{{$t("PRINT")}}</Button>
			        		<Button v-if="param.fkzt === '00'" type="primary" size="small" @click="confirm(index)">{{$t("DETERMINE")}}</Button>
			        	</span>
			        </span>
			        <!--信息-->
			        <div>
			        	<Table ref="table"
			        		border
							   :columns="param.fkzt === '00' ? columns3 : columns4"

							   height="220"
			        		:data="item.orderList"
							@on-selection-change="(e)=>{tableSelectionChange(e,index)}"
						></Table>
			        </div>
			    </Card>
			</Col>
		</Row>
		<component :is="componentName"></component>
	</div>
</template>

<script>
    function PrintPage(){
        var newstr = document.getElementById("printPage").innerHTML;
        var oldstr = document.body.innerHTML;
        document.body.innerHTML = newstr;
        window.print();
        document.body.innerHTML = oldstr;
        return false;

    }
    import edit from './edit'
    import print from './print'
    import swal from 'sweetalert2'
	import i18nTabTit from '@/mixins/i18nTabTit'
	import mixins from '@/mixins'

	export default{
		name:'client',
		mixins: [mixins,i18nTabTit],
		components:{
		  edit,print
		},
		watch:{
			"param.fkzt":function (n,o) {
				if(n == '00'){
					this.tabTiT = this.columns3
				}else {
					this.tabTiT = this.columns4

				}
			}
		},
		data(){
			return {
			    v:this,
                componentName:'',
                choosedItem:null,
				tabTiT:this.columns3,
				columns3: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '用车人员',
						tit:"USERS",
                        key: 'ck'
                    },
                    {
                        title: '候车地点',
						tit:"WAITING_PLACE",
                        key: 'hcdz'
                    },
                    {
                        title: '目的地',
						tit:"DESTINATION",
                        key: 'mdd'
                    },{
                        title: '司机',
						tit:"DRIVER",
                        key: 'sjxm'
                    },{
                        title: '车型',
						tit:"CAR_TYPE_TAB",
                        key: 'zws'
                    },{
                        title: '出车时间',
						tit:"TIME_DEPARTURE",
                        key: 'yysj'
                    },{
                        title: '里程(公里)',
						tit:"MILEAGE",
                        key: 'lc'
                    },{
                        title: '车费合计',
						tit:"TOTAL_FARE",
                        key: 'zj'
                    },{
                        title: '事由',
						tit:"CONTENT_TAB",
                        key: 'sy'
                    },
                    {
                        title: '操作',
						tit:"OPERATION",
                        key: 'action',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.choosedItem = params.row;
                                            this.componentName = 'edit';
                                        }
                                    }
                                }, this.$t("EDIT"))
                            ]);
                        }
                    }
                ],
				columns4: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
					{
						title: '用车人员',
						tit:"USERS",
						key: 'ck'
					},
					{
						title: '候车地点',
						tit:"WAITING_PLACE",
						key: 'hcdz'
					},
					{
						title: '目的地',
						tit:"DESTINATION",
						key: 'mdd'
					},{
						title: '司机',
						tit:"DRIVER",
						key: 'sjxm'
					},{
						title: '车型',
						tit:"CAR_TYPE_TAB",
						key: 'zws'
					},{
						title: '出车时间',
						tit:"TIME_DEPARTURE",
						key: 'yysj'
					},{
						title: '里程(公里)',
						tit:"MILEAGE",
						key: 'lc'
					},{
						title: '车费合计',
						tit:"TOTAL_FARE",
						key: 'zj'
					},{
						title: '事由',
						tit:"CONTENT_TAB",
						key: 'sy'
					},{
                        title: '车费合计',
						tit:"TOTAL_FARE",
                        key: 'zj'
                    },
                ],
				munName:'1',
				param:{
				    fkzt:'00',
					ck:'',
					sjxm:'',
				},
                list:[],
                selectedData:[],
			}
		},
		created(){
        	this.$store.commit('setCurrentPath', [{
                title: '首页',
            },{
                title: '财务结算',
            },{
                title: '付款管理',
            }])
			this.getData();
        },
		mounted(){

		},
		methods:{
            tableSelectionChange(e,i){
                this.selectedData[i] = e;
            },
		    getData(){
                this.list = [];
                let startTime = this.param.startTime;
                let endTime = this.param.endTime;
                if (typeof startTime === 'object'){
                    this.param.startTime = startTime.format('yyyy-MM-dd hh:mm:ss');
                }
                if (typeof endTime === 'object'){
                    this.param.endTime = endTime.format('yyyy-MM-dd hh:mm:ss');
                }
		      	this.$http.get(this.apis.ORDER.paymentList,{params:this.param}).then((res)=>{
		      	    if (res.code === 200 && res.result){
						this.list = res.result;
						for (let r of this.list){
						    this.selectedData.push([]);
						}
                    }
				})
			},
			confirm(index){
                if (this.selectedData[index].length === 0){
                    this.$Message.error(this.$t("ORDER_TAB"));
                    return;
                }
                swal({
                    title: this.$t("PAID_CONFIRM"),
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonText: this.$t("DETERMINE"),
                    cancelButtonText: this.$t("CANCEL"),
                }).then((confirm) => {
                    if (confirm.value) {
                        let ids = '';
                        for (let r of this.selectedData[index]){
                            ids += r.id +',';
                        }
                        let v = this;
                        let url = this.apis.ORDER.paymentConfirm;
                        v.$http.post(url,{'ids':ids}).then((res) =>{
                            if(res.code===200){
                                v.$Message.success(res.message);
                                this.getData();
                            }else{
                                v.$Message.error(res.message);
                            }
                        })
                    }
                });
			},
			//选项卡的切换
			MenuClick(event){
                this.param.fkzt = (event === '1' ? '00' : '10');
                this.getData();
			},
			//卡片事件
			changeLimit(mes){
				alert(mes)
			},
            print(item,index){
                if (this.selectedData[index].length === 0){
                    this.$Message.error(this.$t("ORDER_TAB"));
                    return;
                }
                item.choosedOrderList = this.selectedData[index];
                this.choosedItem = item;
                this.componentName = 'print';
            },
			show(){

			}
		}
	}
</script>

<style>
</style>
