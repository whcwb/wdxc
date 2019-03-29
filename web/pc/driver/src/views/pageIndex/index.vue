<style lang="less">
	@import "../../styles/common.less";
	@import "./pageIndex";
</style>
<template>
	<div class="box">
		<div class="header">
			<div class="box-row">
				<div class="titLeft" @click="backlogin()">
        <Icon type="chevron-left" color="#949494" size='26'></Icon>
      </div>
				<div class="titCenter body-O"
             style="text-align: center;font-size: 16px;font-weight: 600;color:#a8a8a8">
					 <!--<Input v-model="titFind" size="small" placeholder="请输入站点名称" ></Input>-->
				    我的单据
        </div>
				<!--<div class="titRight" @click="feedback()">-->
					<!--<Icon type="ios-compose" color="#949494" size='26'></Icon>-->
				<!--</div>-->
			</div>
		</div>
		<div style="text-align: center;height: 200px;background:url('/static/backTit.png');background-repeat: no-repeat;background-size: cover;background-position: center;">
        <div style="padding: 16px 0">
          <div style="border-radius: 25px;margin:auto;background-color: #5e6dbd;color: #fff;font-weight: 700;font-size: 22px;width: 50px;height: 50px;text-align: center;line-height: 50px">
            {{user.xm | fist}}
          </div>
          <!--<img src="/static/drive.png" alt="" width="65px">-->
        </div>
        <div style="color: #8c8585;font-size: 16px;font-weight: 600">
          {{user.xm}}
        </div>
        <div style="margin: 12px 0;color: #ff9400;font-size: 24px;font-weight: 600">
          {{pf | strT}}分
        </div>

		</div>
		<div class="body bodylist" style="background-color: #fff;">
			<div class="box" style="background-color: #f3f3f3">
				<div class="">
					<tab>
				      <tab-item :selected="listType==0" @on-item-click="onItemClick">待确认</tab-item>
              <tab-item :selected="listType==1" @on-item-click="onItemClick">历史接单</tab-item>
				    </tab>
				</div>
				<div class="body"  style="position: relative;padding: 5px 12px">
            <ok-list></ok-list>
        </div>
      </div>
		</div>
	</div>
</template>

<script>
	import { XButton, Tab, TabItem } from 'vux'
  import Cookies from 'js-cookie';
  import okList from './comp/okList'
	export default{
		name:'',
		components: {
		    XButton, Tab, TabItem,
        okList
		 },
    filters:{
      fist(val){
        if(val){
          return val.substr(0,1)
        }
        return val
      },
      strT(val){
        return val.toString().substr(0,4)
      }
    },
		data(){
			return{
			  user:{},
        pf:'',
        listType:0,
				titFind:'',
			}
		},
		created(){
          if(Cookies.get('result')) {
              this.listType = this.$store.state.app.listType
              this.pf = JSON.parse(Cookies.get('user')).grade
              this.user = JSON.parse(JSON.parse(Cookies.get('user')).userInfo)
          }else{
            this.$router.push({
              name:'login'
            })
          }
		},
		methods:{
        backlogin(){
          this.$router.push({
            name:'login'
          })
        },
		  	MyCenter(){//个人中心
		  		this.$router.push({
		    		name:'myCenter'
		    	})
		  	},
		  	feedback(){//反馈信息
		  		this.$router.push({
		    		name:'feedback'
		    	})
		  	},
		    onItemClick(index){
		  	  console.log(index)
          this.$store.commit('listTypeCh',index)
          // if(index==1){
          //     this.His()
          // }
		    },
		}
	}
</script>

<style>
</style>
