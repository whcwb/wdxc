<template>
  <div class="carListSty box_col">
    <i class="iconfont icon-triangle-top"></i>
    <div class="itemBox box_col_auto">
      <div class="carListItem" v-for="(item,index) in mess" :key="index" v-if="item.cph!=''" @click="itemClick(item)">
        {{item.cph}}
      </div>
    </div>
    <div class="carListItemAdd" @click="itemClick('add')">
      <i class="iconfont icon-add3"></i>
      新增车辆
    </div>
  </div>
</template>

<script>
  export default {
    name: "carLIstModal",
    props:{
      mess:{
        type:Array,
        default:()=>{
          return []
        }
      }
    },
    methods:{
      itemClick(item){
        if(item === 'add'){
          this.$emit('itemClick','add')
        }else {
          this.$emit('itemClick',item)
        }
      }
    }
  }
</script>

<style lang="less">
  .carListSty {
    background-color: #333;
    padding: 4px 14px;
    border-radius: 6px;
    font-size: 12px;
    position: relative;
    max-height: 140px;
    .icon-triangle-top {
      position: absolute;
      left: 10px;
      top: -16px;
      color: #333;
      z-index: 999;
    }
    .itemBox {
    }
    .carListItem {
      min-width: 70px;
      text-align: center;
      height: 30px;
      line-height: 30px;
      color: #fff;
      border-bottom: solid 2px #ffffff;
    }
    .carListItemAdd {
      position: relative;
      height: 30px;
      line-height: 30px;
      padding-left: 20px;
      color: #fff;
      i {
        position: absolute;
        left: -3px;
        color: #fff;
      }
    }
  }
</style>
