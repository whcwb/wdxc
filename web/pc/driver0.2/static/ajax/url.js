let ajaxUrl = "http://47.98.39.45:9099";

export default {
    url:ajaxUrl,
    SWIPER:{
		QUERTY:'/put/xl/getadsense'//轮播图片
	},
	LINE:{
		QUERTY:'/put/xl/getAll'//线路信息
	},
	LINEMESS:{
		QUERTY:'/put/xl/getzdcl'//车辆到站信息
	},
    LOGIN:{
        QUERTY:'/put/jsy/getjsy',//登录
    },
    MESLIST:{//订单列表
        QUERTY:'/put/dd/getsjlist'
    },
    LISTOK:{//行程确认
        CHANGE:'/put/dd/update'
    },
    GPS:{
        DOT:'/put/dd/getStartPointAndEndPoint'
    },
    INFO:'/put/jsy/getInfo',//用户信息
    PROPOSAL:'/put/yj/save',//意见反馈 yjLx 00意见 10反馈 20投诉
    PROPList:'/put/yj/pager',//意见列表
    HISTORYGJ: '/put/dd/getddgps'//历史轨迹
}