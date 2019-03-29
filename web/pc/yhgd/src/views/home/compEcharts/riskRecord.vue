<template>
	<div style="height: 100%;width: 100%;position: relative;">
		<div class="box" style="background-color:#fff;height: 260px;border: solid 2px rgba(0,0,0,0.5);">
			<div>
				<span style="font-size: 18px;margin: 5px 0 0 8px;">
					<b>
						危险驾驶记录
					</b>
				</span>
			</div>
			<div class="body">
				<Steps :current="5" direction="vertical" style="width: 220px;margin: auto;">
					<Step title="鄂A12345-周师傅" content="1号站点-2号站点 超速行驶  75km/h" icon="model-s"></Step>
					<Step title="鄂A12345-周师傅" content="1号站点-2号站点 超速行驶  75km/h" icon="model-s"></Step>
					<Step title="鄂A12345-周师傅" content="1号站点-2号站点 超速行驶  75km/h" icon="model-s"></Step>
					<Step title="鄂A12345-周师傅" content="1号站点-2号站点 超速行驶  75km/h" icon="model-s"></Step>
					<Step title="鄂A12345-周师傅" content="1号站点-2号站点 超速行驶  75km/h" icon="model-s"></Step>
				</Steps>
			</div>
		</div>
		<div v-if="SpinShow" style="width:100%;height:100%;position: absolute;top: 0;left:0;z-index: 100;">
			<Spin fix>
	            <Icon type="load-c" :size=loading.size class="demo-spin-icon-load"></Icon>
	            <div style="font-size: 30px;">{{loading.text}}</div>
	        </Spin>
		</div>
	</div>
</template>

<script>
	export default{
		name:'',
		data(){
			return {
				SpinShow:true,
				loading:this.$store.state.app.loading
			}
		},
		created(){
			setTimeout(() => {
                this.SpinShow = false;
            }, 1000);
		}
	}
</script>

<style>

</style>