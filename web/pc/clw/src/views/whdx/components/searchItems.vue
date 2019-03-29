<template>
    <div style="display: inline-block">
        <div v-for="r in tableColumns" v-if="r.searchKey || r.searchType" style="display: inline-block;margin-bottom: 6px;">
            <label class="searchLabel">{{r.title}}:</label>
            <Input v-if="!r.searchType || r.searchType == 'text'" v-model="param[r.searchKey]" :placeholder="'请输入'+r.title" style="width: 200px"></Input>
            <DatePicker v-else-if="r.searchType == 'daterange'" v-model="dateRange" @on-change="param[r.searchKey] = parent.util.dateRangeChange(dateRange)" confirm format="yyyy-MM-dd" type="daterange" placeholder="请输时间" style="width: 200px"></DatePicker>
            <Select v-else-if="r.searchType == 'dict'" filterable clearable  v-model="param[r.key]" :placeholder="'不限'" style="width: 200px">
                <Option v-for = '(item,index) in parent.dictUtil.getByCode(parent,r.dict)'  v-if="r.excludeDict == null || r.excludeDict.indexOf(item.key) < 0"  :value="item.key" :key="item.key">{{item.val}}</Option>
            </Select>
        </div>
        <Button v-if="showSearchButton" type="primary" @click="parent.util.getPageData(parent)"><Icon type="md-search"></Icon></Button>
    </div>
</template>

<script>
    export default {
        name: "searchItems",
        props:{
            parent:{
                type:Object,
                default:function(){
                    return {};
                }
            },
            showSearchButton:{
                type:Boolean,
                default:false
            }
        },
        data(){
            return{
                dateRange:'',
                tableColumns:[],
                param:{},
            }
        },
        created(){
            if (this.parent.tableColumns){
                this.tableColumns = this.parent.tableColumns;
            }
            if (this.parent.dateRange){
                this.dateRange = this.parent.dateRange;
            }
            if (this.parent.param){
                this.param = this.parent.param;
            }
        }
    }
</script>

<style scoped>

</style>
