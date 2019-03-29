<style lang="less" scoped>
  .star{
    i{
      font-size: 0.4rem;
      margin: 0.08rem;
    }
  }
</style>
<template>
    <div class="star" v-if="bj">
        <i v-for="(item,index) in star"
          class="iconfont icon-xing"
           :style="{color:item.color}"
            @click="starClick(item,index)"
        ></i>
    </div>
    <div class="star" v-else>
      <i v-for="(item,index) in star"
         class="iconfont icon-xing"
         :style="{color:item.color}"></i>
    </div>
</template>

<script>
    export default {
        name: "",
        data(){
            return{
              star:[
                  {
                    color:'#a09999'
                  },
                  {
                    color:'#a09999'
                  },
                  {
                    color:'#a09999'
                  },
                  {
                    color:'#a09999'
                  },
                  {
                    color:'#a09999'
                  }
              ]
            }
        },
        props:{
            num:{
                type:Number,
                default:0
            },
            bj:{
                type:Boolean,
                default:true
            }
        },
        created(){
            this.star.forEach((val,i)=>{
              if(i<=(this.num-1)){
                val.color='#ffbe00'
              }else {
                val.color='#a09999'
              }

            })
        },
        methods:{
          starClick(item,index){
              this.star.forEach((val,i)=>{
                  if(i<=index){
                    val.color='#ffbe00'
                  }else {
                    val.color='#a09999'
                  }

              })
              this.$emit('starNum',(index+1))
          }
        }
    }
</script>

<style scoped>

</style>
