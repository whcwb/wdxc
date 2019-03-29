<style lang="less">
	@import "../styles/common.less";
    @import "./main.less";
</style>
<template>
  <div class="box main">
  	<!--<div class="header">-->
		<!--<div class="box-row">
			<div class="titLeft" @click="MyCenter()">
				<Icon type="person" color="#949494" size='26'></Icon>
			</div>
			<div class="titCenter body-O" style="text-align: center;">
				 <Input v-model="titFind" size="small" placeholder="请输入站点名称" ></Input>
			</div>
			<div class="titRight">
				<Icon type="ios-compose" color="#949494" size='26'></Icon>
			</div>
			
		</div>-->
  	<!--</div>-->
  	<div class="body center">
  		<router-view></router-view>
  	</div>
  	<!--<div class="footer">
  		<div class="box-row">
  			<div class="body-O">
  				<a>站点</a>
  			</div>
  			<div class="body-O">
  				<a>反馈</a>
  			</div>
  			<div class="body-O">
  				<a>我的</a>
  			</div>
  		</div>
  	</div>-->
  </div>
</template>

<script>
	export default {
	  name: 'HelloWorld',
	  components: {
	  },
	  data () {
	    return {
	    	titFind:''
	    }
	  },
	  methods:{
	  	MyCenter(){
	  		alert('left')
	  	},
	  	back(){
	  		alert('back')
	  	}
	  }
	}
</script>
