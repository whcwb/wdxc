let ajaxUrl = "http://47.98.39.45:9099";

module.exports = {
    SWIPER:{
        QUERTY:'/put/xl/getadsense'//轮播图片
    },
    LINE:{
        QUERTY:'/put/xl/getAll'//线路信息
    },
    LINEMESS:{
        QUERTY:'/put/xl/getzdcl'//车辆到站信息
    },
    LOGIN:{//登录
        QUERTY:'/put/jzg/getJzg'
    },
    PROPOSAL:'/put/yj/save',//意见反馈 yjLx 00意见 10反馈 20投诉
    PROPList:'/put/yj/pager',//意见列表
    DDSAVE:{//订单创建
        SAVE:'/put/dd/save'
    },
    ZWS:{//座位数字典获取ZDCLK0041
        QUERTY:'/put/dd/getzdxm'
    },
    DDLIST:{//订单列表
        // QUERTY:'/put/dd/getorderworkerslis'
        QUERTY:'/put/dd/pager'
    },
    REMOVE:{
        DELE:'/put/yj/removeIds'
    },
    PJDD:{//订单评分{id:'',grade:''}
        QUERTY:'/put/dd/evaluate'
    },
    GPS:{
        DOT:'/put/dd/getStartPointAndEndPoint'
    },
    USERINFO:'/put/jzg/getInfo',
    HISTORYGJ: '/put/dd/getddgps'
}