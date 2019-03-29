import axios from 'axios';
import env from '../../build/env';
import semver from 'semver';
import packjson from '../../package.json';
import dictUtil from './dictUtil'

import Cookies from 'js-cookie'
import swal from 'sweetalert2'
let util = {
};

util.showResMessage = (v,res,successMsg,errorMsg)=>{
    if (!successMsg)successMsg = res.message;
    if (!errorMsg)errorMsg = res.message;
    if (res.code === 200){
        v.$Message.success(successMsg);
    }else{
        v.$Message.error(errMsg);
    }
}
util.fillTableColumns = (v)=>{
    if (!v.tableColumns)return;
    for(let r of v.tableColumns){
        if (!r.align){
            r.align = 'center'
        }
        if (r.title === '序号')continue;
        if (!r.render){
            r.render = (h,p)=>{
                let val = p.row[r.key];
                let s  = val ? val : '-';
                if (r.dict  && val){
                    s = dictUtil.getValByCode(v,r.dict,p.row[r.key]);
                }
                if (r.unit && val)s += r.unit;
                return h('div',s);
            }
        }
        r.ellipsis = true;

    }
}
util.exportData = (v,filename)=>{
    v.$refs.table.exportCsv({
        filename: filename
    });
}
util.buildDeleteButton = (v,h,id)=>{
    return util.buildButton(v,h,'error','md-close','删除',()=>{
        util.delete(v,[id])
    })
}
util.buildeditButton = (v,h,p)=>{
    return util.buildButton(v,h,'warning','md-create','编辑',()=>{
        v.choosedItem = p.row;
        v.componentName = 'formData'
    })
}
util.buildButton = (v,h,type,icon,tip,onClick)=>{
    return h('Tooltip',
        {props: {placement: 'top',content: tip,}},
        [
            h('Button', {
                props: {type: type,icon: icon,shape: 'circle',size: 'small'},
                style: {margin: '0 8px 0 0'},
                on: {click: onClick}
            }),
        ]
    )
}
/**
 * 初始化字典
 */
util.initDict = (v)=>{
    if (!v.dicts)return;
    if (v.dicts){
        for (let k in v.dicts){
            let r = v.dicts[k];
            let items = dictUtil.getByCode(v,r.code);
            r.items = items;
        }
    }
}
function getdateParaD(val){//时间转换
    if(val==null||val==""){
        return ''
    }
    var newDate = new Date();
    newDate.setTime(val)
    console.log(typeof newDate);
    let Year = val.getFullYear()
    let Month = val.getMonth()+1
    let Day = val.getDate()
    if(Month<10){
        Month = '0'+Month
    }
    if(Day<10){
        Day = '0'+Day
    }
    let time = Year+'-'+Month+'-'+Day
    return time
}
util.getdateStrD = ()=>{
    var NowDate = new Date
    let Year = NowDate.getFullYear()
    let Month = NowDate.getMonth()+1
    let Day = NowDate.getDate()
    let time = Year+'-'+Month+'-'+Day
    return time
},
    util.rd = (h,p,k)=>{
        let s = p.row[k] ? p.row[k] : '-';
        return h('div',s);
    }
util.dateRangeChange = (s)=>{
    if (s[0] == '')return '';
    return s[0].format("yyyy-MM-dd")+','+s[1].format("yyyy-MM-dd");
}
util.initTableHeight = (v)=>{
    v.tableHeight = window.innerHeight - 240
}
util.initModalTable = (v)=>{
    // util.initTableHeight(v);
    util.fillTableColumns(v)
    util.getPageData(v)
}
util.initForeignKeys = (v)=>{
    if (!v.foreignList)return;
    for (let k in v.foreignList){
        let r = v.foreignList[k];
        if (r.url.indexOf("/pager") > 0)r.url+="?pageSize=10000"
        v.$http.get(r.url).then((res) =>{
            if(res.code===200){
                let list = [];
                r.items = [];
                if (res.page){
                    list = res.page.list;
                }else if (res.result){
                    list = res.result;
                }
                for (let i of list){
                    r.items.push({key:i[r.key],val:i[r.val]})
                }
            }else{
                v.$Message.error(res.message);
            }
        }).catch((error) =>{
            log(error)
        })
    }
}
/**
 * 初始化列表页面
 * 自动调整table高度，页面加载完成后获取列表数据
 */
util.initTable = (v)=>{
    util.initPageSize(v);
    util.initTableHeight(v);
    util.fillTableColumns(v)
    util.getPageData(v)
}
util.initSimpleTable = (v)=>{
    v.tableHeight = window.innerHeight - 780
    util.initPageSize(v);
    util.fillTableColumns(v)
    util.getPageData(v)
}
util.initPageSize = (v)=>{
    if (!v.param || !v.param.pageSize)return;
    let pageSize = Cookies.get("pageSize");
    if (!pageSize){
        Cookies.set("pageSize",8);
        pageSize = 8;
    }
    pageSize = parseInt(pageSize);
    v.param.pageSize = pageSize;
}
/**
 * 初始化表单（包括新增和修改）页面
 * 如果是编辑页面，则对父组件传过来的数据进行深复制，方式数据联动
 * 设置modal标题
 * 设置是否readonly（如果是编辑页面，有些字段不可修改）
 * 根据formInputs设置的字段 自动添加字段验证规则
 */
util.initFormModal = (v)=>{
    v.apiRoot = v.$parent.apiRoot;
    if (v.$parent.choosedItem){
        // 深复制，避免数据联动
        v.formItem = JSON.parse(JSON.stringify(v.$parent.choosedItem));
        v.operate = '编辑'
        v.readonly = true
    }
    util.initFormRule(v);
    util.initForeignKeys(v);
    util.initDict(v);
}
/**
 * 根据formInputs设置的字段 自动添加字段验证规则
 */
util.initFormRule = (v)=>{
    if (!v.formInputs)return;
    for (let r of v.formInputs){
        if (r.required){
            let rule = [{required: true, message: '请填写'+r.label, trigger: 'blur' }]
            v.ruleInline[r.prop] = rule;
        }
    }
}
/**
 * 点击新增按钮事件
 * 弹出新增modal
 */
util.add = (v)=>{
    v.componentName = 'formData'
    v.choosedItem = null;
}
/**
 * 点击保存按钮事件（包括新增保存和编辑保存）
 * 根据状态自动判断调用新增接口还是修改接口
 * 如果表单验证通过，则调用保存或新增接口，如果验证不通过则提示消息
 * 如果执行保存操作之前需要处理数据，则先处理数据（beforeSave方法）
 * 保存或修改成功之后，提示成功，并且调用父组件刷新table数据方法，并关闭modal窗口，如果保存或修改失败，则提示错误信息
 */
util.save = function(v){
    // 根据状态自动判断调用新增接口还是修改接口
    let url = v.saveUrl ? v.saveUrl : (v.$parent.choosedItem ? v.apiRoot['CHANGE'] : v.apiRoot['ADD']);
    let rules = v.$refs.form.rules;

    function sendSave(){
        if (typeof v.beforeSave === 'function'){
            // 执行save方法之前的操作
            v.beforeSave();
        }
        v.$http.post(url,v.formItem).then((res) =>{
            if(res.code===200){
                v.$Message.success(res.message);
                util.getPageData(v.$parent)
                v.$parent.componentName = ''
            }else{
                v.$Message.error(res.message);
            }
        }).catch((error) =>{
            log(error)
        })
    }

    if (Object.keys(rules).length == 0){
        sendSave();
    }else{
        v.$refs.form.validate((valid) => {
            console.log("验证内容："+valid)
            if (valid) {
                sendSave();
            } else {
                v.$Message.error('请将信息填写完整!');
            }
        })
    }
}


/**
 * 删除
 * 删除弹出确认弹窗
 * 删除成功之后如果有回调函数则执行回调函数，如果没有则调用父组件刷新table数据方法
 * @param v 组件this
 * @param ids 需要删除的主键列表
 * @param callback 删除成功之后的回调函数（可选）
 */
util.delete = function(v,ids,callback){//数据删除方法封装
    swal({
        text: "是否删除数据?",
        type: "warning",
        showCancelButton: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消'
    }).then((isConfirm) => {
        if (isConfirm.value) {
            let url = v.apiRoot['DELE'];
            v.$http.post(url,{'ids':ids}).then((res) =>{
                if(res.code===200){
                    v.$Message.success(res.message);
                    if (callback && typeof callback === 'function'){
                        callback();
                    }else{
                        util.getPageData(v)
                    }
                }else{
                    v.$Message.error(res.message);
                }
            })
        }
    });

}
/**
 * 旧的删除方法，将逐渐弃用
 * @param v
 * @param url
 * @param ids
 * @param callback
 */
util.del = function(v,url,ids,callback){//数据删除方法封装
    swal({
        text: "是否删除数据?",
        type: "warning",
        showCancelButton: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消'
    }).then((isConfirm) => {
        if (isConfirm.value) {
            v.$http.post(url,{'ids':ids}).then((res) =>{
                if(res.code===200){
                    v.$Message.success(res.message);
                    if (callback && typeof callback === 'function'){
                        callback();
                    }else if(typeof v.getPageData === 'function'){
                        v.getPageData()
                    }
                }else{
                    v.$Message.error(res.message);
                }
            })
        }
    });
}
/**
 * 关闭弹窗
 */
util.closeDialog = function(v){
    v.showModal = false;
    setTimeout((t) => {
        v.$parent.$data.componentName = "";
    }, 200)
}
/**
 * table页面获取数据方法
 * @param v
 */
util.getPageData = function(v) {
    let url = v.pagerUrl ? v.pagerUrl : v.apiRoot['QUERY'];
    v.$http.post(url, v.param).then((response) => {
            let code = response.code;
            let msg = response.message;
            v.SpinShow = false
            if (code === 200) {
                let page = response.page;
                v.pageData = page.list;
                v.param.total = page.total;
            }
        }, (error) => {
        }
    ).then((next) => {
    });
}
/**
 * table分页切换事件
 */
util.pageChange = function(v,e) {
    v.param.pageNum = e
    util.getPageData(v)
}
util.pageSizeChange = function(v,n) {
    Cookies.set("pageSize",n);
    v.param.pageSize = n;
    util.getPageData(v);
}
/**
 * get方法并执行回调函数
 */
util.getData = function(v, url, onSuccess) {
    v.$http.get(url).then((response) => {
            let code = response.code;
            let msg = response.message;
            if (code == 200) {
                if (typeof (onSuccess) === 'function') {
                    onSuccess(response.result);
                }
            } else {
                v.$Message.error(msg);
            }
        },
        (error) => {
            v.$Message.error('网络异常');
        }).then((next) => {
    });
    ;
}
util.inOf = function (arr, targetArr) {
    let res = true;
    arr.forEach(item => {
        if (targetArr.indexOf(item) < 0) {
            res = false;
        }
    });
    return res;
};

util.oneOf = function (ele, targetArr) {
    if (targetArr.indexOf(ele) >= 0) {
        return true;
    } else {
        return false;
    }
};

util.showThisRoute = function (itAccess, currentAccess) {
    if (typeof itAccess === 'object' && Array.isArray(itAccess)) {
        return util.oneOf(currentAccess, itAccess);
    } else {
        return itAccess === currentAccess;
    }
};

util.getRouterObjByName = function (routers, name) {
    if (!name || !routers || !routers.length) {
        return null;
    }
    // debugger;
    let routerObj = null;
    for (let item of routers) {
        if (item.name === name) {
            return item;
        }
        routerObj = util.getRouterObjByName(item.children, name);
        if (routerObj) {
            return routerObj;
        }
    }
    return null;
};

util.handleTitle = function (vm, item) {
    if (typeof item.title === 'object') {
        return vm.$t(item.title.i18n);
    } else {
        return item.title;
    }
};

//util.setCurrentPath = function (vm, name) {
//	debugger
//  let title = '';
//  let isOtherRouter = false;
//  vm.$store.state.app.routers.forEach(item => {
//      if (item.children.length === 1) {
//          if (item.children[0].name === name) {
//              title = util.handleTitle(vm, item);
//              if (item.name === 'otherRouter') {
//                  isOtherRouter = true;
//              }
//          }
//      } else {
//          item.children.forEach(child => {
//              if (child.name === name) {
//                  title = util.handleTitle(vm, child);
//                  if (item.name === 'otherRouter') {
//                      isOtherRouter = true;
//                  }
//              }
//          });
//      }
//  });
//  let currentPathArr = [];
//  if (name === 'home_index') {
//      currentPathArr = [
//          {
//              title: util.handleTitle(vm, util.getRouterObjByName(vm.$store.state.app.routers, 'home_index')),
//              path: '',
//              name: 'home_index'
//          }
//      ];
//  } else if ((name.indexOf('_index') >= 0 || isOtherRouter) && name !== 'home_index') {
//      currentPathArr = [
//          {
//              title: util.handleTitle(vm, util.getRouterObjByName(vm.$store.state.app.routers, 'home_index')),
//              path: '/home',
//              name: 'home_index'
//          },
//          {
//              title: title,
//              path: '',
//              name: name
//          }
//      ];
//  } else {
//      let currentPathObj = vm.$store.state.app.routers.filter(item => {
//          if (item.children.length <= 1) {
//              return item.children[0].name === name;
//          } else {
//              let i = 0;
//              let childArr = item.children;
//              let len = childArr.length;
//              while (i < len) {
//                  if (childArr[i].name === name) {
//                      return true;
//                  }
//                  i++;
//              }
//              return false;
//          }
//      })[0];
//      if (currentPathObj.children.length <= 1 && currentPathObj.name === 'home') {
//          currentPathArr = [
//              {
//                  title: '首页',
//                  path: '',
//                  name: 'home_index'
//              }
//          ];
//      } else if (currentPathObj.children.length <= 1 && currentPathObj.name !== 'home') {
//          currentPathArr = [
//              {
//                  title: '首页',
//                  path: '/home',
//                  name: 'home_index'
//              },
//              {
//                  title: currentPathObj.title,
//                  path: '',
//                  name: name
//              }
//          ];
//      } else {
//          let childObj = currentPathObj.children.filter((child) => {
//              return child.name === name;
//          })[0];
//          currentPathArr = [
//              {
//                  title: '首页',
//                  path: '/home',
//                  name: 'home_index'
//              },
//              {
//                  title: currentPathObj.title,
//                  path: '',
//                  name: currentPathObj.name
//              },
//              {
//                  title: childObj.title,
//                  path: currentPathObj.path + '/' + childObj.path,
//                  name: name
//              }
//          ];
//      }
//  }
//  vm.$store.commit('setCurrentPath', currentPathArr);
//
//  return currentPathArr;
//};

//util.openNewPage = function(vm, name, argu, query){
//	debugger
//	log('*******')
//}

util.openNewPage= function (vm, name, argu, query) {
    let pageOpenedList = vm.$store.state.app.pageOpenedList;
    let openedPageLen = pageOpenedList.length;
    let i = 0;
    let tagHasOpened = false;
    while (i < openedPageLen) {
        if (name === pageOpenedList[i].name) { // 页面已经打开
            vm.$store.commit('pageOpenedList', {
                index: i,
                argu: argu,
                query: query
            });
            tagHasOpened = true;
            break;
        }
        i++;
    }
    if (!tagHasOpened) {
        let tag = vm.$store.state.app.tagsList.filter((item) => {
            if (item.children) {
                return name === item.children[0].name;
            } else {
                return name === item.name;
            }
        });
        tag = tag[0];
        if (tag) {
            tag = tag.children ? tag.children[0] : tag;
            if (argu) {
                tag.argu = argu;
            }
            if (query) {
                tag.query = query;
            }
            vm.$store.commit('increateTag', tag);
        }
    }
    vm.$store.commit('setCurrentPageName', name);
};

util.toDefaultPage = function (routers, name, route, next) {
    let len = routers.length;
    let i = 0;
    let notHandle = true;
    while (i < len) {
        if (routers[i].name === name && routers[i].children && routers[i].redirect === undefined) {
            route.replace({
                name: routers[i].children[0].name
            });
            notHandle = false;
            next();
            break;
        }
        i++;
    }
    if (notHandle) {
        next();
    }
};

util.fullscreenEvent = function (vm) {
    vm.$store.commit('initCachepage');
    // 权限菜单过滤相关
    vm.$store.commit('updateMenulist');
    // 全屏相关
};

util.checkUpdate = function (vm) {
    axios.get('https://api.github.com/repos/iview/iview-admin/releases/latest').then(res => {
        let version = res.data.tag_name;
        vm.$Notice.config({
            duration: 0
        });
        if (semver.lt(packjson.version, version)) {
            vm.$Notice.info({
                title: 'iview-admin更新啦',
                desc: '<p>iView-admin更新到了' + version + '了，去看看有哪些变化吧</p><a style="font-size:13px;" href="https://github.com/iview/iview-admin/releases" target="_blank">前往github查看</a>'
            });
        }
    });
};

export default util;
