<style lang="less">
      @import '../../../../styles/common.less';

      .CloudVideo {
            background-color: #fff;
            .videoSty {
                  border-bottom: solid 1px #dedede;
                  position: relative;
                  .videoBF {
                        cursor: pointer;
                        position: absolute;
                        top: 0;
                        left: 0;
                        right: 0;
                        bottom: 0;
                        background: rgba(0, 0, 0, 0.5);
                        z-index: 100;
                  }
                  .icon {
                        position: absolute;
                        top: 50%;
                        left: 50%;
                        transform: translate(-50%, -50%);
                        z-index: 101;
                  }
                  .VideoTit {
                        position: absolute;
                        top: 0px;
                        left: 0;
                        z-index: 102;
                        color: #fff;
                        background: rgba(0, 0, 0, 0.5);
                        padding: 3px 5px;
                        width: 100%;
                  }
                  .wzSty{
                        /*position: absolute;*/
                        /*top: 26px;*/
                        /*left: 0;*/
                        /*z-index: 102;*/
                        transform: translateY(-5px);
                        color: #fff;
                        background: rgba(0, 0, 0, 0.5);
                        padding: 3px 5px;
                        padding-top: 0;
                        width: 100%;
                  }
            }
            .inputTit {
                  margin-bottom: 5px;
            }
      }

      .videoInfo {
            text-align: center;
            padding: 16px;
            margin-bottom: 16px;
      }
      .wzStyM{
            /*position: absolute;*/
            /*bottom:110px;*/
            /*left: 20px;*/
            /*right: 20px;*/
            /*z-index: 102;*/
            color: #fff;
            background: rgba(0, 0, 0, 0.5);
            padding: 3px 5px;
            /*width: 100%;*/
      }
</style>
<template>
      <div class="CloudVideo boxbackborder box">
            <div class="tit">
                  <Row class="margin-top-10" style='background-color: #fff;position: relative;'>
					<span class="tabPageTit">
	    				<Icon type="ios-paper" size='30' color='#fff'></Icon>
	    			</span>
                        <div style="height: 45px;line-height: 45px;">
                              <div class="margin-top-10 box-row">
                                    <div class="titmess">
                                          <span>云图片库</span>
                                    </div>
                                    <!--<div class="body-r-1 inputSty">-->
                                          <!--<Select v-model="param.wjmLike" @on-change="findMessList()">-->
                                                <!--<Option value="F" >前摄像头</Option>-->
                                                <!--<Option value="B" >后摄像头</Option>-->
                                          <!--</Select>-->
                                          <!--<RadioGroup v-model="param.wjmLike" type="button"-->
                                                      <!--@on-change="findMessList()">-->
                                                <!--<Radio label="F">前摄像头</Radio>-->
                                                <!--<Radio label="B">后摄像头</Radio>-->
                                          <!--</RadioGroup>-->
                                    <!--</div>-->
                                    <div class="body-r-1 inputSty">
                                          <DatePicker v-model="cjsjInRange"
                                                      format="yyyy-MM-dd"
                                                      type="daterange"
                                                      placement="bottom-end"
                                                      placeholder="请输时间"
                                                      @on-keyup.enter="findMessList()"
                                                      ></DatePicker>
                                    </div>

                                    <div class="body-r-1 inputSty">
                                          <Input type="text" v-model="param.cphLike" placeholder="请输入车牌号"></Input>
                                    </div>
                                    <div class="body-r-1 inputSty">
                                          <Input type="text" v-model="param.zdbhLike" placeholder="请输入终端编号"></Input>
                                    </div>
                                    <div class="butevent">
                                          <Button type="primary" @click="findMessList()">
                                                <Icon type="md-search"></Icon>
                                                <!--查询-->
                                          </Button>
                                    </div>
                              </div>
                        </div>
                  </Row>
            </div>
            <div>
                  <Tabs type="card" v-model="param.wjmLike" @on-click="findMessList()">
                        <TabPane label="前摄像头" name="F"></TabPane>
                        <TabPane label="后摄像头" name="B"></TabPane>
                  </Tabs>
            </div>
            <div v-show="videoList.length == 0" class="body" style="border: 1px solid #dddee1;position: relative">
                  <h1 style="color: #bdbdbd;position: absolute;top:40%;left: 50%;transform: translate(-50%,-50%)">
                        {{param.cphLike}}暂无图片
                  </h1>
            </div>
            <div v-if="videoList.length != 0" class="body" style="border: 1px solid #dddee1">
                  <div class="box-row-list">
                        <div class="bodyC videoSty"
                             title="点击查看大图"
                             style="min-height: 140px;"
                             v-for="(item,index) in videoList">
                              <img
                                      :src="videoPath+item.url"
                                      @click="showMax(videoPath+item.url,index,item.wz)"
                                      style="width: 100%;">
                              <div class="VideoTit">
                                    {{item.cph}}
                                    <div style="float: right;cursor: pointer;">
						    	<span v-show="item.video"
                                                              @click="videoF(item.video,item,index)">
						    		关闭
						    	</span>
                                          <span style="color: #ff9900;" @click="videoColse(item,index)">
						    		移除
						    	</span>
                                    </div>
                              </div>
                              <div class="wzSty">
                                    [{{item.cjsj}}]
                              </div>
                              <div class="wzSty">
                                    {{item.wz ? item.wz : '……'}}
                              </div>
                        </div>
                  </div>
            </div>
            <div class="margin-top-10 pageSty" style="height: 60px;">
                  <Page
                          :total=pageTotal
                          :current=param.pageNum
                          :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]
                          @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}'
                          show-total
                          show-elevator show-sizer placement='top'
                          @on-change='pageChange'></Page>
            </div>
            <Modal
                    width="60%"
                    v-model="maxPhoto">
                  <div slot="close" style="padding: 10px" @click="maxPhoto = false">
                        <Icon type="close-round" color="#000"></Icon>
                  </div>
                  <div class="wzStyM">
                        {{ maxWz? maxWz : '……'}}
                  </div>
                  <img :src="maxUrl"
                       width="100%"
                       alt="">
                  <div slot="footer">
                        <div class="box-row">
                              <div class="body-O" style="width: 100%;overflow-y: auto;text-align: center">
                                    <img v-for="(item,index) in videoList"
                                         :src="videoPath+item.url"
                                         width="7.6%"
                                         @click="showMax(videoPath+item.url,index,item.wz)"
                                         style="margin: 2px;">

                              </div>
                              <div class="margin-top-10 pageSty">
                                    <Page
                                            :total=pageTotal
                                            :current=param.pageNum
                                            :page-size=param.pageSize :page-size-opts=[8,10,20,30,40,50]
                                            @on-page-size-change='(e)=>{param.pageSize=e;pageChange()}'
                                            simple
                                            @on-change='pageChange'></Page>
                              </div>

                        </div>
                  </div>
            </Modal>
      </div>
</template>

<script>

      import swal from 'sweetalert2'
    import mixins from '@/mixins'

    export default {
        name: '',
        mixins: [mixins],
        components: {},
        data() {
            return {
                maxPhoto: false,
                maxUrl: '',
                maxWz:'',
                maxIndex: '',
                vadeoShow: true,
                videoPath: this.apis.VIDEO_PATH,
                activeName: 0,
                cjsjInRange: [],
                carList: [],
                pageTotal: 1,
                page: {
                    pageNum: 1,
                    pageSize: 8
                },
                videoList: [],
                param: {
                    cjsjInRange: '',
                    cphLike: '',
                    wjmLike:'F',
                    pageNum: 1,
                    pageSize: 8
                }
            }
        },
        created() {
            if (this.$route.params && this.$route.params.zdbh){
                this.param.zdbhLike = this.$route.params.zdbh
            }
            this.getCarList();
        },
        methods: {
            showMax(url, index,wz) {
                this.maxPhoto = true
                this.maxUrl = url
                this.maxIndex = index
                this.maxWz = wz
            },
            videoS(type, item, index) {
                this.videoList[index].video = true
            },
            videoF(type, item, index) {
                this.videoList[index].video = false
            },
            videoColse(item, index) {
                var v = this
                  swal({
                        text: "是否删除数据?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonText: '确认',
                        cancelButtonText: '取消'
                  }).then((willDelete) => {
                        if (willDelete.value) {
                        this.$http.post(this.apis.CLOUD.DELE + '/' + item.id).then((res) => {
                            if (res.code == 200) {
                                v.$Message.success(res.message);
                            } else {
                                v.$Message.error(res.message);
                            }
                            v.getmess()
                        }).catch((error) => {
                            v.$Message.error('出错了！！！');
                        })
                    }
                });
            },
            getmess(typ) {
                if (this.cjsjInRange.length != 0 && this.cjsjInRange[0] != '' && this.cjsjInRange[1] != '') {
                    this.param.cjsjInRange = this.getdateParaD(this.cjsjInRange[0]) + "," + this.getdateParaD(this.cjsjInRange[1]);
                } else {
                    this.param.cjsjInRange = '';
                }
                var v = this
                v.param.wjmEndwith = '.jpg';
                this.$http.get(this.apis.CLOUD.QUERY, {params: v.param}).then((res) => {
                    v.pageTotal = res.page.total
                    for (let r of res.page.list) {
                        if (r.url) {
                            r.video = false
                            r.imgdz = r.url.replace('video', 'cache');
                            r.imgdz = r.imgdz.replace('mp4', 'jpg')
                        }
                    }
                    this.videoList = res.page.list
                    // videoPath+item.url,index,item.wz
                    this.maxUrl = this.videoPath + res.page.list[0].url
                    this.maxWz = res.page.list[0].wz
                    v.SpinShow = false;
                })
            },
            getCarList() {
                var v = this
                this.$http.get(this.apis.CLGL.GET_ORG_CAR_LIST).then((res) => {
                    this.carList = res.result
                    this.getmess();
                })
            },
            findMessList() {
                this.getmess();
            },
            pageChange(event) {
                var v = this
                v.param.pageNum = event
                this.getmess()
            },
        }
    }
</script>

<style>

</style>
