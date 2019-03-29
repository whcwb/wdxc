<template>
    <span  :id="'searchRow'">
        <span v-for="(r,index) in tableColumns" v-if="r.searchKey || r.searchType" :key="index">
            <label :style="index > 1 ? 'margin-left:12px;' : ''">{{r.title}}:</label>

            <Input v-if="!r.searchType || r.searchType == 'text'" v-model="form[r.searchKey]" :placeholder="'请输入'+r.title" :style="inputStyle"></Input>
            <DatePicker v-else-if="r.searchType == 'daterange'" v-model="dateRange" @on-change="form[r.searchKey] = parent.util.dateRangeChange(dateRange)" confirm format="yyyy-MM-dd" type="daterange" placeholder="不限" :style="inputStyle"></DatePicker>
            <Select v-else-if="r.searchType == 'dict'" filterable clearable  v-model="form[r.key]" :placeholder="'不限'" :style="inputStyle">
                <Option v-for = '(item,index) in parent.dictUtil.getByCode(parent,r.dict)'  v-if="r.excludeDict == null || r.excludeDict.indexOf(item.key) < 0"  :value="item.key" :key="item.key">{{item.val}}</Option>
            </Select>
        </span>
        <Button v-if="showSearchButton" type="primary" @click="parent.util.getPageData(parent)">
            <Icon type="md-search"></Icon>
        </Button>
        <Button v-if="showCreateButton" type="primary" @click="parent.util.add(parent)">
            <Icon type="plus-round"></Icon>
        </Button>
    </span>
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
        default:true
      },
      showCreateButton:{
        type:Boolean,
        default:false
      },
      labelWith:{
        type:Number,
        default:100
      },
      inputWith:{
        type:Number,
        default:100
      }
    },
    data(){
      return{
        labelStyle:'',
        inputStyle:'width:100px;',
        dateRange:'',
        tableColumns:[],
        form:{},
      }
    },
    created(){
      if (this.parent.tableColumns){
        this.tableColumns = this.parent.tableColumns;
      }
      if (this.parent.dateRange){
        this.dateRange = this.parent.dateRange;
      }
      if (this.parent.form){
        this.form = this.parent.form;
      }
    },
  }
</script>

<style scoped>

</style>
