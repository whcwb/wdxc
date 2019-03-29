<style scoped>
</style>
<template>
    <div class="index">
        <Row type="flex" justify="center" align="middle">
            <Col span="24">
                <Tabs value="name1">
                    <TabPane label="查询轨迹" name="name1">
                        <gps-table></gps-table>
                    </TabPane>
                    <TabPane label="查询事件" name="name3">
                        <sjjl-table></sjjl-table>
                    </TabPane>
                </Tabs>
            </Col>
        </Row>
    </div>
</template>
<script>
    import gpsTable from './table/gpsTable'
    import sjjlTable from './table/sjjlTable'
    export default {
        components: {sjjlTable,gpsTable},
        data(){
          return{
              v:this,
          }
        },
        created(){
        },
        methods:{
        }
    };
</script>
