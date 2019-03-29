<style lang="less">
    @import '../../../../styles/common.less';
    @import 'draggable-list.less';
</style>

<template>
    <div>
        <Row>
            <Col span="16">
                <Card>
                    <Row>
                        <Col span="12">
                            <Card dis-hover>
                                <p slot="title">
                                    <Icon type="ios-list-outline"></Icon>
                                    本周欲完成任务清单(拖拽到右侧删除)
                                </p>
                                <div style="height: 360px;">
                                    <ul id="doList" class="" style="min-height:100px ;"></ul>
                                </div>
                            </Card>
                        </Col>
                        <Col span="12" class="padding-left-10">
                            <Card dis-hover>
                                <p slot="title">
                                    <Icon type="ios-list"></Icon>
                                		所剩任务清单(拖拽到左侧添加)
                                </p>
                                <div style="height: 360px;">
                                    <ul id="todoList" class="">
                                        <li v-for="(item, index) in todoArray" :key="index" class="notwrap todolist-item" :data-index="index">
                                            {{ item.content }}
                                            <!--<Button type="error" size="small" shape="circle" icon="close" @click="delelist('indexs')">1</Button>-->
                                        </li>
                                    </ul>
                                </div>
                            </Card>
                        </Col>
                    </Row>
                </Card>
            </Col>
        </Row>
    </div>
</template>

<script>
import Sortable from 'sortablejs';
export default {
    name: 'draggable-list',
    data () {
        return {
            todoArray: [
                {
                    content: '完成iview-admin基本开发'
                },
                {
                    content: '对iview-admin进行性能优化'
                },
                {
                    content: '对iview-admin的细节进行优化'
                },
                {
                    content: '完成iview-admin开发'
                },
                {
                    content: '解决发现的bug'
                },
                {
                    content: '添加更多组件'
                },
                {
                    content: '封装更多图表'
                },
                {
                    content: '增加更多人性化功能'
                }
            ],
            doArray: [],
            dotoArray:[],
        };
    },
    watch:{
    	doArray:function(){
    	},
    	todoArray:function(){
    	}
    },
    methods:{
    	delelist(indexs){
			this.$Message.success('移出成功');
		},
    },
    mounted () {
//      document.body.ondrop = function (event) {
//          event.preventDefault();
//          event.stopPropagation();
//      };
        let vm = this;
        let todoList = document.getElementById('todoList');
        Sortable.create(todoList, {
            group: {
                name: 'list',
                pull: 'clone'
            },
            animation: 120,
            ghostClass: 'placeholder-style',
            fallbackClass: 'iview-admin-cloned-item',
            onRemove (event) {
            	log('数据移出',event)
//              vm.doArray.splice(event.newIndex, 0, vm.todoArray[event.item.getAttribute('data-index')]);
//          	vm.doArray.splice(event.newIndex, 0, vm.todoArray[]
            }
        });
        let doList = document.getElementById('doList');
        Sortable.create(doList, {
            group: {
                name: 'list',
                pull: true
            },
            sort: 120,
            filter: '.iview-admin-draggable-delete',
            animation: 120,
            fallbackClass: 'iview-admin-cloned-item',
            
            setData: function (/** DataTransfer */dataTransfer, /** HTMLElement*/dragEl) {
				log('1')
            },
		
			// Element is chosen
			onChoose: function (/**Event*/evt) {
				log('2',evt)
			},
		
			// Element dragging started
			onStart: function (/**Event*/evt) {
				log('3')
			},
		
//			onEnd: function (/**Event*/evt) {
//				var itemEl = evt.item;  // dragged HTMLElement
//				evt.to;    // target list
//				evt.from;  // previous list
//				evt.oldIndex;  // element's old index within old parent
//				evt.newIndex;  // element's new index within new parent
//			},
		
			// Element is dropped into the list from another list
			onAdd: function (/**Event*/evt) {
				// same properties as onEnd
				log('4',evt)
				
			},
		
			// Changed sorting within list
			onUpdate: function (/**Event*/evt) {
				// same properties as onEnd
				log('5')
			},
		
			// Called by any change to the list (add / update / remove)
			onSort: function (/**Event*/evt) {
				// same properties as onEnd
//				evt.bubbles = false
//				var ad = evt.item.getAttribute('data-index')
//				var ad = 
				evt.item.innerHTML = evt.item.innerText+
				'<button type="button" onclick=onRemove class="ivu-btn ivu-btn-error ivu-btn-circle ivu-btn-small" style=""><!----> <i class="ivu-icon ivu-icon-close"></i> <span>1</span></button>'
//				'<button type="button" class="ivu-btn ivu-btn-primary ivu-btn-circle ivu-btn-icon-only"><!----> <i class="ivu-icon ivu-icon-ios-search"></i> <!----></button>'+
//				'<button type="button" class="ivu-btn ivu-btn-error ivu-btn-circle ivu-btn-icon-only"><!----> <i class="ivu-icon ivu-icon-close"></i> <!----></button>'
//				log('6',ad)
				log('6',evt)
			},
		
			onFilter: function (/**Event*/evt) {
				var itemEl = evt.item;  // HTMLElement receiving the `mousedown|tapstart` event.
			},
            onRemove (event) {
            }
        });
    }
};
</script>
