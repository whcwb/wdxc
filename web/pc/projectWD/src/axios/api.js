// let url = 'http://10.113.4.70:'//8080
let url = 'http://127.0.0.1:'//8080
// let url = 'http://192.168.123.6:'//8081宋陵云
// let url = 'http://119.23.242.234:'//8080
let port = '8080'
// let port = '8081'
export default {
    // url:url+port+'/biz',
    url:url+port,
    UPLOAD:url+port+'/biz/upload',
    STATIC_PATH:'http://119.23.242.234:'+'9092/',//地图icon地址前缀
    VIDEO_PATH:url+'9091/test/',
    TXT:'/api/clsbyxsjjl/pager',
    fileDR:'/api/zdgl/excel',
    USERROOT:{
        GET_MENU_LIST:'/api/gn/getUserFunctions',
        GET_MENU_TREE:'/api/gn/getMenuTree',
        INIT_MENU:'/api/gn/initMenu',
        MODIFY_PSD:'/api/yh/mdfPwd'
    },
    CHART_DATA:{
       SAFE_DRIVING:'/api/cl/carAccStatistics',
       WEEK:'/api/dd/ddzTj',
       PD:'/api/dd/ccTTj',
        nianshenbt:'/api/cl/nianshenbt',
        xbkb:'/api/pb/xbkb',//今日校巴开班条形图
        csxxtj:'/api/clsbyxsjjl/csxxtj',//获取时间段内超数值
        zdcx:'/api/zdgl/zdcx',//获取设备状况展示
    },
    LOGIN:{
        QUERY:'login'
    },
    USER:{
        QUERY:'/api/yh/pager',//用户管理
        ADD:'/api/yh/save',
        CHANGE:'/api/yh/update',
        GIVE:'/api/js/modifyUserRoles',
        DELE:'/api/yh/removeIds'
    },
    BXJZ:{
        QUERY:'/api/bxjz/pager',//报销记账管理
        ADD:'/api/bxjz/save',
        CHANGE:'/api/bxjz/update',
        DELE:'/api/bxjz/removeIds'
    },
    TEMP_UNIT:{
        QUERY:'/api/lsdw/pager',//临时单位管理
        ADD:'/api/lsdw/save',
        CHANGE:'/api/lsdw/update',
        DELE:'/api/lsdw/removeIds'
    },
    TEMP_CAR:{
        QUERY:'/api/lsc/pager',//临时车管理
        ADD:'/api/lsc/save',
        CHANGE:'/api/lsc/update',
        DELE:'/api/lsc/removeIds'
    },
    ORDER:{
        QUERY:'/api/dd/pager',//订单管理
        ADD:'/api/dd/save',
        CHANGE:'/api/dd/update',
        DELE:'/api/dd/removeIds',
        AUDIT:'/api/dd/orderAuditing',
        //订单分派
        PDTJ:'/api/dd/dtddtj',
        SJLB:'/api/dd/pdsjlb',//司机列表
        DFP:'/api/dd/dfp',//待分派订单
        QXPD:'/api/dd/qxpd',//取消派单
        CONFIRM:'/api/dd/ddqr',//订单确认
        DRIVER_CONFIRM:'/api/dd/driverConfirm',//队长确认
        orderDetails:'/api/dd/orderDetails',//订单详情
        FP:'/api/dd/pd',//订单分派
        TJ:'/api/dd/ddtj',//订单统计
        skgl:'/api/dd/skgl',//收款管理
        cctj:'/api/dd/cctj', // 出车统计
        fktj:'/api/dd/fktj', // 付款统计
        sktj:'/api/dd/sktj', // 收款统计
        collectingList:'/api/dd/collectingList', // 收款列表
        paymentList:'/api/dd/paymentList', // 付款列表
        cwupdate:'/api/dd/cwupdate', // 财务编辑
        collectingConfirm:'/api/dd/collectingConfirm', // 收款确认
        paymentConfirm:'/api/dd/paymentConfirm', // 付款确认
        DRZT:'/api/jsy/jsytype ',//司机状态修改
    },
    ROLE:{
        QUERY:'/api/js/pager',//角色管理
        ALL:'/api/js/getAll',//角色管理
        ADD:'/api/js/save',
        CHANGE:'/api/js/update',
        GIVE:'/api/js/modifyUserRoles',
        DELE:'/api/js/removeIds',
        MODIFY_USER_ROLES:'/api/js/modifyUserRoles',
        GET_USER_ROLES:'/api/js/getUserRoles'
    },
    FRAMEWORK:{
        QUERY:'api/jg/pager',//机构管理
        ADD:'/api/jg/save',
        CHANGE:'/api/jg/update',
        DELE:'/api/jg/removeIds',
        GET_TREE:'/api/jg/getOrgTree',
        GET_TREE_Node:'/api/jg/getTree',
        getSubOrgList:'/api/jg/getSubOrgList',
        getCurrentUserOrgTree:'/api/jg/getCurrentUserOrgTree',
        getCurrentOrgTree:'/api/jg/getCurrentOrgTree',
        getOrgPath:'/api/jg/getOrgPath'
    },
    DICTIONARY:{
        QUERY:'/api/zd/pager',// 查询字典
        ADD:'/api/zd/save',// 新增字典
        CHANGE:'/api/zd/update',// 编辑字典
        DELE:'/api/zd/removeIds' // 删除字典
    },
    DICTIONARY_LIST:{
        QUERY:'/api/zdxm/pager',//查询字典项
        ADD:'/api/zdxm/save',// 新增字典项
        CHANGE:'/api/zdxm/update',// 编辑字典项
        DELE:'/api/zdxm/removeIds',// 删除字典项
        GET_BY_CONDITION:'/api/zdxm/getCondition', // 删除字典
    },
    ITMS:{
        QUERY:'api/fw/pager',//服务管理
        ADD:'/api/fw/save',
        CHANGE:'/api/fw/update',
        DELE:'/api/fw/removeIds'
    },
    HSGS:{
        QUERY:'api/hsgs/pager',//核算公式
        ADD:'/api/hsgs/save',
        CHANGE:'/api/hsgs/update',
        DELE:'/api/hsgs/removeIds'
    },
    AQJS:{
        QUERY:'/api/clsbyxsjjl/Safedriving',

    },
    FUNCTION:{
        QUERY:'api/gn/pager',//功能管理
        ADD:'/api/gn/save',
        CHANGE:'/api/gn/update',
        DELE:'/api/gn/removeIds',
        GET_ORG_PERMISSION_TREE:'/api/gn/getOrgPermissionTree',
        GET_ALL_PERMISSION_TREE:'/api/gn/getAllPermissionTree',
        GET_ROLE_PERMISSION_TREE:'/api/gn/getRolePermissionTree',
        GET_USER_PERMISSION_TREE:'/api/gn/getUserPermissionTree',
        getPermissionTreeWithChecked:'/api/gn/getPermissionTreeWithChecked',
        SET_ROLE_FUNCTIONS:'/api/gn/setRoleFunctions',
        SET_ORG_FUNCTIONS:'/api/gn/setOrgFunctions',
        GET_ORG_FUNCTIONS:'/api/gn/getOrgFunctions',
        GET_ROLE_FUNCTIONS:'/api/gn/getRoleFunctions',
    },
    DAILY:{
        QUERY:'api/rz/pager',//日志管理
        ADD:'/api/rz/save',
        CHANGE:'/api/rz/update',
        DELE:'/api/rz/removeIds'
    },
    CLSBYXJL:{
        QUERY:'api/clsbyxsjjl/pager',//异常事件管理
        ADD:'/api/clsbyxsjjl/save',
        CHANGE:'/api/clsbyxsjjl/update',
        DELE:'/api/clsbyxsjjl/removeIds'
    },
    SUGGES:{
        QUERY:'api/yj/pager',//意见管理
        CHANGE:'/api/yj/update',
    },
    ADVERTISING:{
        QUERY:'api/hd/pager',//活动管理
        ADD:'/api/hd/save',
        CHANGE:'/api/hd/update',
        DELE:'/api/hd/removeIds'
    },
    ACCIDENT:{
        QUERY:'api/sg/pager',//事故管理
        ADD:'/api/sg/save',
        CHANGE:'/api/sg/update',
        DELE:'/api/sg/removeIds'
    },
    //******************车辆管理******************
    CLOUD:{//云视频库
        QUERY:'/api/spk/pager',
        DELE:'/api/spk/remove'
    },
    CLGL:{
        QUERY: '/api/cl/pager',
        ADD:   '/api/cl/save',
        CHANGE:'/api/cl/update',
        DELE:  '/api/cl/removeIds',
        GET_ORG_CAR_LIST:'/api/cl/getOrgCarList',
        GPS_HITSOR:'/api/xc/history',
        GPS_HITSOR_GPS:'/api/clsbyxsjjl/historygps',
        GPS_HITSOR_GPS_BD:'/api/clsbyxsjjl/yyguiji',
        nianshen:'/api/cl/nianshen',
        unbindDevice:'/api/cl/unbindDevice',
        unbindDriver:'/api/cl/unbindDriver',
        bindDriver:'/api/cl/bindDriver',
        bindDevice:'/api/cl/bindDevice',
    },
    XL:{
        QUERY: '/api/xl/pager',
        ADD:   '/api/xl/save',
        CHANGE:'/api/xl/update',
        DELE:  '/api/xl/removeIds'
    },
    LSDW:{
        QUERY: '/api/lsdw/pager',
        ADD:   '/api/lsdw/save',
        CHANGE:'/api/lsdw/update',
        DELE:  '/api/lsdw/removeIds'
    },
    LSC:{
        QUERY: '/api/lsc/pager',
        ADD:   '/api/lsc/save',
        CHANGE:'/api/lsc/update',
        DELE:  '/api/lsc/removeIds'
    },
    JSY:{
        QUERY:  '/api/jsy/pager',
        ADD:    '/api/jsy/save',
        CHANGE: '/api/jsy/update',
        DELE:   '/api/jsy/removeIds',
        NOT_BIND_LIST:   '/api/jsy/notBindList',
        jsytype:   '/api/jsy/jsytype',
    },
    CD:{
        QUERY:  '/api/cd/pager',
        ADD:    '/api/cd/save',
        CHANGE: '/api/cd/update',
        DELE:   '/api/cd/removeIds',
        GET_CONDITION:'/api/cd/getCondition',
        notBindCarList:'/api/cd/notBindCarList',
        notBindDriverList:'/api/cd/notBindDriverList',
    },
    SG:{
        QUERY:  '/api/sg/pager',
        ADD:    '/api/sg/save',
        CHANGE: '/api/sg/update',
        DELE:   '/api/sg/removeIds'
    },
    ZD:{
        QUERY:  '/api/clzd/pager',
        ADD:    '/api/clzd/save',
        CHANGE: '/api/clzd/update',
        DELE:   '/api/clzd/removeIds',
        GET_ALL:'/api/clzd/getAll',
        getByCondition:'/api/clzd/getCondition',
        GET_BY_ROUTE_ID:'/api/clzd/getByXlId',
        getNotBindList:'/api/clzd/getNotBindList',
    },
    PB:{//校巴排版
        QUERY:  '/api/pb/xbpb/',
        banchetj :  '/api/pb/banchetj',
    },
    XLPBXX:{//车辆排班信息
        QUERY: '/api/pb/xbbjpb/',
        ADD:    '/api/pb/savepb',
        PLADD:'/api/pb/pbList',
        YZ:'/api/pb/checkPbCl',
        PLDELE:'/api/pb/delPbList',
        DELE:   '/api/pb/deleteByXlAndCl',
        CARLIST:'api/pb/getcllist',//其余车辆
        deleteByXlAndCl:   '/api/pb/deleteByXlAndCl',

    },
    XBDT:{//校巴实时动态
        QUERY: '/api/clzd/getzdcl/',
    },
    CS:{//超速
        QUERY:  '/api/cssd/pager',
//		ADD:    '/api/cssd/save',
        ADD:	'api/cssd/setCssds',
        CHANGE: '/api/cssd/update',
        DELE:   '/api/cssd/removeIds'
    },
    ZNZP:{//智能站牌
        QUERY:  '/api/znzp/pager',
        ADD:    '/api/znzp/save',
        CHANGE: '/api/znzp/update',
        DELE:   '/api/znzp/removeIds',
        getXlIds:   '/api/znzp/getXlIds',
    },
    ZDGL:{//终端管理
        QUERY:  '/api/zdgl/pager',//终端数据列表
        SXQUERY:'/api/zdgl/getunboundlist',//状态 正常 未绑定的终端
        ADD:    '/api/zdgl/save',
        CHANGE: '/api/zdgl/update',
        DELE:   '/api/zdgl/removeIds',
        GET_BY_ID:'/api/zdgl/',
        getWebsocketInfo:'/api/zdgl/getWebsocketInfo'
    },
    DZWL:{//电子围栏
        QUERY:  '/api/dzwl/pager',
        ADD:    '/api/dzwl/save',
        CHANGE: '/api/dzwl/update',
        DELE:   '/api/dzwl/removeIds',
        GET_BY_CAR_ID:'/api/dzwl/getByCarId',
        setCarsDzwl:'/api/dzwl/setCarsDzwl'
    },
    DZWL_CL:{//电子围栏车辆
        QUERY:  '/api/dzwlCl/pager',
        ADD:    '/api/dzwlCl/save',
        CHANGE: '/api/dzwlCl/update',
        DELE:   '/api/dzwlCl/removeIds',
    },
    clyxjl:{//车辆运行记录
        QUERY:  '/api/clyxjl/pager',
        ADD:    '/api/clyxjl/save',
        CHANGE: '/api/clyxjl/update',
        DELE:   '/api/clyxjl/removeIds',
    },
    CLJK:{
        QUERY:'/api/cl/InitClGps',
        GET_CAR_INFO:'/api/cl/getCarInfo',
        GET_OBD_INFO:'/obd/getObdInfo',
        getObdTimely:'/obd/getObdTimely',
        SEND_CONTROLL:'/pub/intstruction/send'
    },
    CARTREE:{//车辆列表
        QUERY:'/api/cl/getcltj'
    },
    SBZDDZ:{//设备终端接口地址
        ADD:'/pub/intstruction/send',
        batchUpdate:'/pub/batchUpdate',
        getVersionInfo:'/api/zdgl/getVersionInfo'
    }
}
