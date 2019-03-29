<template>
      <div id="lineModel" style="overflow: auto">
            <div class="box-row-z">
                  <div class="itemSty" v-for="(item,index) in mess"  v-show="(item.name.indexOf('辅助点')==-1 && item.name!='') || item.name!=''" >
                        <Tooltip :transfer="true" class="changeBut"  placement="top" content="站点信息修改" v-show="item.xg">
                              <Button size="small" type="success" shape="circle" icon="md-create"
                                      @click="xg(item,index)"></Button>
                        </Tooltip>
                        <Tooltip :transfer="true" class="changeBut"  placement="top" content="站点移除" v-show="!item.xg">
                              <Button size="small" type="error" shape="circle" icon="md-close"
                                      @click="codeChangeRem(item,index)"></Button>
                        </Tooltip>
                        <i v-show="index!=mess.length-1" class="iconfont icon-one-line-arrow"></i>

                        <div class="name">
                              <div v-for="it in item.name.split('')">
                                    {{it}}
                              </div>

                        </div>
                        <div class="slelctSty" v-show="!item.xg">
                              <Select ref="clearSelectItem" filterable clearable v-model="stationId" placeholder="请选择" size="small">
                                    <Option v-for="(it,val) in stationList"
                                            :disabled='it.disabled'
                                            :value="val+1">{{it.mc}}
                                    </Option>
                              </Select>
                        </div>
                        <div class="butSTY" v-show="!item.xg">
                              <Tooltip class="changeBut"  placement="bottom" content="站点替换" v-show="!item.xg">
                                    <Button type="primary" shape="circle" icon="md-swap" size="small"
                                            @click="codeChange(index)"></Button>
                              </Tooltip>
                              <Tooltip class="changeBut"  placement="bottom" content="完成" v-show="!item.xg" style="float: right;">
                                    <Button type="primary" shape="circle" icon="md-checkmark" size="small"
                                            @click='item.xg = !item.xg'></Button>
                              </Tooltip>
                        </div>
                  </div>
            </div>
      </div>
      
</template>

<script>
      import swal from 'sweetalert2'
    export default {
        name: "line",
        data(){
          return{
              stationId:''
          }
        },
        props:{
            mess:{
                type:Array,
                default:[]
            },
            stationList:{
                type:Array,
                default:[]
            }
        },
        computed:{
        },
        watch:{
            mess:(n,o)=>{
                console.log(n);
            }
        },
        created(){
        },
        methods:{
            xg(item,index){
                this.mess.forEach((it,ix)=>{
                    if(ix == index){
                        it.xg = false
                    }else {
                        it.xg = true
                    }
                })
                // this.mess[index]

            },
            codeChangeRem(item,index){
                var v = this
                swal({
                    text: "是否删除此站点?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonText: '确认',
                    cancelButtonText: '取消'
                }).then((isConfirm) => {
                    if (isConfirm.value) {

                        var abj ={
                            item:item,
                            index:index
                        }
                        v.$emit('codeChangeRem',abj)
                    }else {
                        this.$Message.error('操作取消');
                    }
                    item.xg = !item.xg
                });
            },
            codeChange(num){
                // console.log(this.stationList[this.stationId-1]);
                // if(num.id){
                      var abj = {
                          num:num,
                          item:this.stationList[this.stationId-1],
                          index:this.stationId
                      }
                      this.$emit('codeChange',abj)
                // console.log(this.$refs.clearSelectItem);
                this.$refs.clearSelectItem[num].clearSingleSelect()
                      // console.log('res',this.$refs['thzd'+num].clearSingleSelect());
                // }

            }
        }
    }
</script>

<style lang="less">
      #lineModel{
            width: 100%;
            height: 100%;
            padding: 0 8px;
            .itemSty{
                  position: relative;
                  width: 110px;
                  min-width: 110px;
                  height: 100%;
                  padding: 10px 0 30px 0;
                  .icon-9{
                        font-size: 24px;
                        color: #57a3f3;
                  }
                  .icon-one-line-arrow{
                        font-size: 50px;
                        font-weight: 900;
                        color: #57a3f3;
                        position: absolute;
                        left: 48px;
                        top: -15px;
                  }
                  .name{
                        font-size: 16px;
                        margin-bottom: 10px;
                        transform: translateX(4px);
                  }
                  .deleBut{
                        position: absolute;
                        buttom: 4px;
                  }
                  .slelctSty{
                        position: absolute;
                        left: 30px;
                        top: 10px;
                        width: 75px;
                        z-index: 100;
                  }
                  .butSTY{
                        position: absolute;
                        left: 30px;
                        top: 50px;
                        width: 75px;
                  }
            }
      }
</style>