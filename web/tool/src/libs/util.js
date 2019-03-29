import axios from 'axios';
import env from '../config/env';
import dictUtil from "./dictUtil";
import swal from 'sweetalert2'

let util = {};
util.title = function (title) {
    title = title ? title + ' - Home' : 'iView project';
    window.document.title = title;
};

const ajaxUrl = 'http://119.23.242.234:8080';
// const ajaxUrl = 'http://127.0.0.1:8080';

util.ajax = axios.create({
    baseURL: ajaxUrl,
    timeout: 30000
});

util.showResMessage = (v, res, successMsg, errorMsg) => {
    if (!successMsg) successMsg = res.message;
    if (!errorMsg) errorMsg = res.message;
    if (res.code === 200) {
        v.$Message.success(successMsg);
    } else {
        v.$Message.error(errMsg);
    }
}
util.fillTableColumns = (v) => {
    if (!v.tableColumns) return;
    for (let r of v.tableColumns) {
        if (!r.align) {
            r.align = 'center'
        }
        if (r.title === '序号') continue;
        if (!r.render) {
            r.render = (h, p) => {
                let val = p.row[r.key];
                let s = val ? val : '-';
                if (r.dict && val) {
                    s = dictUtil.getValByCode(v, r.dict, p.row[r.key]);
                }
                if (r.unit && val) s += r.unit;
                return h('div', s);
            }
        }
        r.ellipsis = true;

    }
}
util.exportData = (v, filename) => {
    v.$refs.table.exportCsv({
        filename: filename
    });
}
util.buildDeleteButton = (v, h, id) => {
    return util.buildButton(v, h, 'error', 'md-close', '删除', () => {
        util.delete(v, [id])
    })
}
util.buildeditButton = (v, h, p) => {
    return util.buildButton(v, h, 'warning', 'md-create', '编辑', () => {
        v.choosedItem = p.row;
        v.componentName = 'formData'
    })
}
util.buildButton = (v, h, type, icon, tip, onClick) => {
    return h('Tooltip',
        {props: {placement: 'top', content: tip,}},
        [
            h('Button', {
                props: {type: type, icon: icon, shape: 'circle', size: 'small'},
                style: {margin: '0 8px 0 0'},
                on: {click: onClick}
            }),
        ]
    )
}
/**
 * 初始化字典
 */
util.initDict = (v) => {
    if (!v.dicts) return;
    if (v.dicts) {
        for (let k in v.dicts) {
            let r = v.dicts[k];
            let items = dictUtil.getByCode(v, r.code);
            r.items = items;
        }
    }
}

function getdateParaD(val) {//时间转换
    if (val == null || val == "") {
        return ''
    }
    var newDate = new Date();
    newDate.setTime(val)
    console.log(typeof newDate);
    let Year = val.getFullYear()
    let Month = val.getMonth() + 1
    let Day = val.getDate()
    if (Month < 10) {
        Month = '0' + Month
    }
    if (Day < 10) {
        Day = '0' + Day
    }
    let time = Year + '-' + Month + '-' + Day
    return time
}

util.getdateStrD = () => {
    var NowDate = new Date
    let Year = NowDate.getFullYear()
    let Month = NowDate.getMonth() + 1
    let Day = NowDate.getDate()
    let time = Year + '-' + Month + '-' + Day
    return time
},
    util.rd = (h, p, k) => {
        let s = p.row[k] ? p.row[k] : '-';
        return h('div', s);
    }
util.dateRangeChange = (s) => {
    if (s[0] == '') return '';
    return s[0].format("yyyy-MM-dd") + ' 00:00:00,' + s[1].format("yyyy-MM-dd") + ' 23:59:59';
}
util.initTableHeight = (v) => {
    v.tableHeight = window.innerHeight - 240
}
util.initModalTable = (v) => {
    // util.initTableHeight(v);
    util.fillTableColumns(v)
    util.getPageData(v)
}
util.initForeignKeys = (v) => {
    if (!v.foreignList) return;
    for (let k in v.foreignList) {
        let r = v.foreignList[k];
        if (r.url.indexOf("/pager") > 0) r.url += "?pageSize=10000"
        v.http.get(r.url).then((res) => {
            if (res.code === 200) {
                let list = [];
                r.items = [];
                if (res.page) {
                    list = res.page.list;
                } else if (res.result) {
                    list = res.result;
                }
                for (let i of list) {
                    r.items.push({key: i[r.key], val: i[r.val]})
                }
            } else {
                v.$Message.error(res.message);
            }
        }).catch((error) => {
            log(error)
        })
    }
}
/**
 * 初始化列表页面
 * 自动调整table高度，页面加载完成后获取列表数据
 */
util.initTable = (v) => {
    util.initPageSize(v);
    util.initTableHeight(v);
    util.fillTableColumns(v)
    util.getPageData(v)
}
util.initSimpleTable = (v) => {
    v.tableHeight = window.innerHeight - 780
    util.initPageSize(v);
    util.fillTableColumns(v)
    util.getPageData(v)
}
util.initPageSize = (v) => {
    if (!v.param || !v.param.pageSize) return;
}
/**
 * 初始化表单（包括新增和修改）页面
 * 如果是编辑页面，则对父组件传过来的数据进行深复制，方式数据联动
 * 设置modal标题
 * 设置是否readonly（如果是编辑页面，有些字段不可修改）
 * 根据formInputs设置的字段 自动添加字段验证规则
 */
util.initFormModal = (v) => {
    v.apiRoot = v.$parent.apiRoot;
    if (v.$parent.choosedItem) {
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
util.initFormRule = (v) => {
    if (!v.formInputs) return;
    for (let r of v.formInputs) {
        if (r.required) {
            let rule = [{required: true, message: '请填写' + r.label, trigger: 'blur'}]
            v.ruleInline[r.prop] = rule;
        }
    }
}
/**
 * 点击新增按钮事件
 * 弹出新增modal
 */
util.add = (v) => {
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
util.save = function (v) {
    // 根据状态自动判断调用新增接口还是修改接口
    let url = v.saveUrl ? v.saveUrl : (v.$parent.choosedItem ? v.apiRoot['CHANGE'] : v.apiRoot['ADD']);
    let rules = v.$refs.form.rules;

    function sendSave() {
        if (typeof v.beforeSave === 'function') {
            // 执行save方法之前的操作
            v.beforeSave();
        }
        v.http.post(url, v.formItem).then((res) => {
            if (res.code === 200) {
                v.$Message.success(res.message);
                util.getPageData(v.$parent)
                v.$parent.componentName = ''
            } else {
                v.$Message.error(res.message);
            }
        }).catch((error) => {
            log(error)
        })
    }

    if (Object.keys(rules).length == 0) {
        sendSave();
    } else {
        v.$refs.form.validate((valid) => {
            console.log("验证内容：" + valid)
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
util.delete = function (v, ids, callback) {//数据删除方法封装
    swal({
        text: "是否删除数据?",
        type: "warning",
        showCancelButton: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消'
    }).then((isConfirm) => {
        if (isConfirm.value) {
            let url = v.apiRoot['DELE'];
            v.http.post(url, {'ids': ids}).then((res) => {
                if (res.code === 200) {
                    v.$Message.success(res.message);
                    if (callback && typeof callback === 'function') {
                        callback();
                    } else {
                        util.getPageData(v)
                    }
                } else {
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
util.del = function (v, url, ids, callback) {//数据删除方法封装
    swal({
        text: "是否删除数据?",
        type: "warning",
        showCancelButton: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消'
    }).then((isConfirm) => {
        if (isConfirm.value) {
            v.http.post(url, {'ids': ids}).then((res) => {
                if (res.code === 200) {
                    v.$Message.success(res.message);
                    if (callback && typeof callback === 'function') {
                        callback();
                    } else if (typeof v.getPageData === 'function') {
                        v.getPageData()
                    }
                } else {
                    v.$Message.error(res.message);
                }
            })
        }
    });
}
/**
 * 关闭弹窗
 */
util.closeDialog = function (v) {
    v.showModal = false;
    setTimeout((t) => {
        v.$parent.$data.componentName = "";
    }, 200)
}
/**
 * table页面获取数据方法
 * @param v
 */
util.getPageData = function (v) {
    v.tableLoading = true;
    let url = v.pagerUrl ? v.pagerUrl : v.apiRoot['QUERY'];
    v.http.get(url, {params:v.param}).then((response) => {
            response = response.data;
            let code = response.code;
            let msg = response.message;
            v.SpinShow = false
            if (code === 200) {
                let page = response.page;
                if (typeof v.afterPager === 'function') {
                    v.afterPager(page.list)
                }
                v.pageData = page.list;
                v.param.total = page.total;
            }
            v.tableLoading = false;
        }, (error) => {
        }
    ).then((next) => {
    });
}
/**
 * table分页切换事件
 */
util.pageChange = function (v, e) {
    v.param.pageNum = e
    util.getPageData(v)
}
util.pageSizeChange = function (v, n) {
    v.param.pageSize = n;
    util.getPageData(v);
}
/**
 * get方法并执行回调函数
 */
util.getData = function (v, url, onSuccess) {
    v.http.get(url).then((response) => {
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
export default util;
