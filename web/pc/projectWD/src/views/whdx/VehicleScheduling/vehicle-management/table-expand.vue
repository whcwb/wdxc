<style scoped>
    .expand-row{
        margin-bottom: 16px;
        padding-left: 40px;
    }
</style>
<template>
    <div>
        <Row class="expand-row">
        	<Col span="6">
            	<h3>图片</h3>
            	<div>
					{{row.tp}}
            	</div>
            </Col>
            <Col span="6">
            	<h3>生产商</h3>
            	<div>
            	</div>
            </Col>
            <Col span="6">
            	<h3>型号</h3>
            	<div>
            	</div>
            </Col>
            <Col span="6">
            	<h3>终端编号</h3>
            	<div>
            	</div>
            </Col>
        </Row>
        <Row class="expand-row">
            <Col span="6">
            	<h3>初次登记日期</h3>
            	<div>
            	</div>
            </Col>
            <Col span="6">
            	<h3>保险公司名称</h3>
            	<div>
            	</div>
            </Col>
            <Col span="6">
            	<h3>保险时间</h3>
            	<div>
            	</div>
            </Col>
        </Row>
    </div>
</template>
<script>
    export default {
        props: {
            row: Object
        }
    };
</script>
