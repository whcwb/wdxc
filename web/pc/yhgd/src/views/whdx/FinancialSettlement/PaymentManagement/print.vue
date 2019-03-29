<style lang="less">
	@import '../../../../styles/common.less';
</style>
<style scoped>
	table,table th, table td { border:1px solid #000; }
</style>
<template>
	<div>
		<Modal v-model="showModal" width='700' :closable='false' :mask-closable="false">
			<div style="text-align: center">
				<h2>{{item.driverName}}付款单</h2>
				<h3>({{startTime}} 至 {{endTime}})</h3>
			</div>
			<br>
			<div class="info">
				<div style="float: left">
					<span>单据编号</span><span>{{number}}</span>
				</div>
				<div style="float: right">
					<span>打印日期</span><span>{{date}}</span>
				</div>
			</div>
			<div style="clear: both"></div>
			<div style="text-align: center">
				<table style="width: 100%" cellspacing="0" cellpadding="0">
					<tr>
						<th width="40">序号</th>
						<th width="40">用车人员</th>
						<th width="40">出发地</th>
						<th width="40">目的地</th>
						<th width="40">司机</th>
						<th width="40">车型</th>
						<th width="40">出车时间</th>
						<th width="40">里程</th>
						<th width="40">事由</th>
						<th width="40">车费合计</th>
					</tr>
					<tbody>
						<tr v-for="(item,index) in item.choosedOrderList">
							<td>{{index + 1}}</td>
							<td>{{item.ck}}</td>
							<td>{{item.hcdz}}</td>
							<td>{{item.mdd}}</td>
							<td>{{item.sjxm}}</td>
							<td>{{getCx(item.cllx)+'-'+item.zws+'人'}}</td>
							<td>{{format(item.yysj)}}</td>
							<td>{{item.lc}}</td>
							<td>{{item.sy}}</td>
							<td>￥{{item.zj}}</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td>合计</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>￥{{total}}</td>
						</tr>
					</tfoot>
				</table>
			</div>
			<div style="overflow: auto;height: 400px;margin-top:60px;text-align: right">
				<span>收款人：_____________________</span>
			</div>
			<div slot='footer'>
				<Button type="default" v-if="showFooter" @click="close"style="color: #949494">取消</Button>
				<Button type="primary" v-if="showFooter" @click="print">确定</Button>
			</div>
		</Modal>
	</div>
</template>

<script>

    Date.prototype.format = function(format)
    {
        var o = {
            "M+" : this.getMonth()+1, //month
            "d+" : this.getDate(),    //day
            "h+" : this.getHours(),   //hour
            "m+" : this.getMinutes(), //minute
            "s+" : this.getSeconds(), //second
            "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
            "S" : this.getMilliseconds() //millisecond
        }
        if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
            (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)if(new RegExp("("+ k +")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length==1 ? o[k] :
                    ("00"+ o[k]).substr((""+ o[k]).length));
        return format;
    }
    export default {
        name: '',
        data() {
            return {
                total:0,
                showFooter:true,
                showModal:true,
				item:{},
				startTime:'',
				endTime:'',
				number:'',
				date:''
            }
        },
        created(){
            let now = new Date();
            this.date = now.format("yyyy-MM-dd");
            this.number = now.format('yyyyMMddhhmmss');
            this.item = this.$parent.choosedItem;
            if (this.item.choosedOrderList && this.item.choosedOrderList.length != 0){
				this.startTime = this.item.choosedOrderList[0].cjsj.substring(0,10);
				this.endTime = this.item.choosedOrderList[this.item.choosedOrderList.length - 1].cjsj.substring(0,10);
                for (let o of this.item.choosedOrderList){
                    let zj = parseFloat(o.zj);
                    this.total += isNaN(zj) ? 0 : zj;
                }
			}
        },
        mounted(){
        },
        methods: {
            format(s){
                let d = new Date(s);
                return d.format('MM-dd hh:mm');
			},
            getCx(s){
              if (s == '10')return '小车';
              return '大车';
			},
            close(){
                let v = this;
                v.$parent.componentName = ''
            },
            print(){
                this.showFooter = false;
                setTimeout(function(){
                    window.print();
                },10)
                let v = this;
                setTimeout(function(){
                    v.close();
                },150)
			}
        }
    }
</script>

<style>

</style>
