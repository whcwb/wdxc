<script>
	import {
		mapState
	} from 'vuex'
	export default {
		computed: mapState(['hasLogin,userID']),
		onLaunch: function() {
			console.log('App Launch');
			console.log(this.hasLogin)
			const value = uni.getStorageSync('userID');
			console.log('世界那么大,这里只是个起点',value)
			if (false) {
				uni.showModal({
					title: '未登录',
					content: '您未登录，需要登录后才能继续',
					/**
					* 如果需要强制登录，不显示取消按钮
					*/
					showCancel: false,
					success: (res) => {
						if (res.confirm) {
							/**
							* 如果需要强制登录，使用reLaunch方式
							*/
							if (this.forcedLogin) {
								uni.reLaunch({
									url: '../login/login'
								});
							} else {
								uni.navigateTo({
									url: '../login/login'
								});
							}
						}
					}
				});
			}
		},
		onShow: function() {
			console.log('App Show');
		},
		onHide: function() {
			console.log('App Hide');
		}
	}
</script>
<style lang="less">
	/*每个页面公共css */
	@import "./common/uni.css";
	@font-face {
	  font-family: 'iconfont';
	  src:url('https://at.alicdn.com/t/font_959231_nw17w16gokb.ttf') format('truetype');
	}
	.iconfont{
		font-family: 'iconfont';
	}
	/* uni-app默认全局使用flex布局。因为flex布局有利于跨更多平台，尤其是采用原生渲染的平台。如不了解flex布局，请参考http://www.w3.org/TR/css3-flexbox/。如不使用flex布局，请删除或注释掉本行。*/
	/* @import '//at.alicdn.com/t/font_605313_d5l8r7zubjj.css'; */
	page {
		min-height: 100%;
		display: flex;
		background-color: #f1f1f1;
	}
	.ZWTIT{
		// background-color: #007AFF;
		background-color: #333;
		height: 45upx;
	}
	.pagebody{
		flex: 1;
		overflow: auto;
	}
	.box_col {
		/* //纵向 */
		height: 100%;
		display: flex;
		flex-direction: column;
		.box_col_100 {
			flex: 1;
		}
		
		.box_col_auto {
			flex: 1;
			overflow: auto;
		}
	}
	
	
	.box_row {
		/* //横向 */
		//height: 100%;
		display: flex;
		flex-direction: row;
	
	}
	
	.box_row .box_row_100 {
		flex: 1;
		.box_row_1auto {
			flex: 1;
			overflow: auto;
		}
	}
	
	.box_row_z {
		/* //横向布局禁止换行 */
		display: flex;
		flex-direction: row;
		flex-wrap: nowrap;
		height: 100%;
	}
	
	.box_row_list {
		/* //横向布局自动换行 */
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
	
	}
	.content {
		display: flex;
		flex: 1;
		flex-direction: column;
		background-color: #efeff4;
		padding: 20upx;
	}
	.input-group {
	        background-color: #ffffff;
	        margin-top: 40upx;
	        position: relative;
	    }
	
	    .input-group::before {
	        position: absolute;
	        right: 0;
	        top: 0;
	        left: 0;
	        height: 1upx;
	        content: '';
	        -webkit-transform: scaleY(.5);
	        transform: scaleY(.5);
	        background-color: #c8c7cc;
	    }
	
	    .input-group::after {
	        position: absolute;
	        right: 0;
	        bottom: 0;
	        left: 0;
	        height: 1upx;
	        content: '';
	        -webkit-transform: scaleY(.5);
	        transform: scaleY(.5);
	        background-color: #c8c7cc;
	    }
	
	    .input-row {
	        display: flex;
	        flex-direction: row;
	        position: relative;
	    }
	
	    .input-row .title {
	        width: 16%;
	        height: 50upx;
	        min-height: 50upx;
	        padding: 15upx 0;
	        padding-left: 30upx;
	        line-height: 50upx;
	    }
	
	    .input-row input {
	        width: 80%;
	        height: 50upx;
	        min-height: 50upx;
	        padding: 15upx 0;
	        padding-right: 30upx;
	        line-height: 50upx;
	    }
	
	    .input-row.border::after {
	        position: absolute;
	        right: 0;
	        bottom: 0;
	        left: 15upx;
	        height: 1upx;
	        content: '';
	        -webkit-transform: scaleY(.5);
	        transform: scaleY(.5);
	        background-color: #c8c7cc;
	    }
	
	    .btn-row {
	        margin-top: 50upx;
	        padding: 20upx;
	    }
	
	    button.primary {
	        background-color: #0faeff;
	    }
</style>
