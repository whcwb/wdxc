<!--角色分配-->
<template>
  <div class="root">
    <Modal v-model='RootShow' height="400" :closable='false' :mask-closable="false" title="角色分配">
      <div v-if="SpinShow" style="width:100%;height:100%;position: fixed;top: 0;left:0;z-index: 1111;">
        <Spin fix>
          <Icon type="load-c" size=55 class="demo-spin-icon-load"></Icon>
          <div style="font-size: 30px;">数据加载中请稍后</div>
        </Spin>
      </div>
      <div style="overflow: auto;height: 600px;width: 500px">
        <select-role v-if="showRoleTable" :hasIds="hasIds" @arrIds="arrIds"></select-role>
      </div>
      <div slot='footer'>
        <Button type="primary" @click="save">确定</Button>
        <Button type="default" @click="close" style="color: #949494">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>

    import selectRole from '../../system-role/selectTable'

  export default {
    name: '',
    components: {selectRole},
    data() {
      return {
        SpinShow: false,
        RootShow: true,
        indeterminate: false,
        checkAll: false,
        checkAllGroup: [],
        userRoles: [],
        checkGroup: [],
        hasIds: [],
        showRoleTable: false,
        roleList: [],
      }
    },
    props: {
      usermes: {
        type: Object,
        default: {}
      }
    },
    created() {
      // this.checkGroup = usermes
      console.log('数据传输', this.usermes)
    },
    mounted() {
      this.getUserRoles();
    },
    methods: {
      hasRole(roleId) {
        for (let r of this.userRoles) {
          if (r.jsId == roleId) return true;
        }
        return false;
      },
      getUserRoles() {
        this.$http.get(this.apis.ROLE.GET_USER_ROLES + '?userId=' + this.usermes.yhid,{timers:new Date().getTime()}).then((res) => {
          if (res.code === 200) {
            if (res.result) {
              this.userRoles = res.result;
              this.hasIds = [];
              for (let r of this.userRoles) {
                this.hasIds.push(r.jsId);
                this.checkGroup.push(r.jsId);
              }
            } else {
              this.userRoles = [];
            }
            this.showRoleTable = true;
          }
        })
      },
      close() {
        var v = this
        v.$parent.compName = ''
      },
      save() {
        let v = this;
        v.SpinShow = true
        this.$http.post(this.apis.ROLE.MODIFY_USER_ROLES, {
          userId: this.usermes.yhid,
          roleIds: this.checkGroup
        }).then((res) => {
          if (res.code === 200) {
            this.$Message.success('操作成功');
            this.$emit('listF', res)
            v.SpinShow = false
          }
        })
      },
      arrIds(jsIds) {
        let ids = [];
        for (let r of jsIds) {
          ids.push(r);
        }
        this.checkGroup = ids;
      }
    }
  }
</script>

<style>

</style>