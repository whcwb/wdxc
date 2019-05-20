export default {
    /**
     *菜单
     **/
    system: "系统管理",
    "system-user": "用户管理",
    "system-role": "角色管理",
    "system-framework":"厂商管理",
    "system-dictionary":"字典管理",
    "system-ITSM":"服务管理",
    "system-function":"功能管理",
    "system-daily":"日志管理",
    "system-suggestions":"意见反馈",
    "system-advertising":"活动管理",

    "VehicleScheduling":"车辆管理",
    /**
     * 车辆管理
     */

    "vehicle-management":"车辆管理",
    "TemporaryCarManagement":"临时车管理",
    "UnitManagement":"单位管理",
    "VehicleManagement":"车辆管理",
    "DriverManagement":"司机管理",
    "FleetManagement":"车队管理",
    "ElectronicFence":"电子围栏",
    "OverspeedLimit":"超速设定",
    "OrderManagement":"订单管理",
    "Establish":"创建订单",
    "ToExamine":"订单审核",
    "Assignment":"小车派单",
    "Assignment_max":"大车派单",
    "Confirm":"司机确认",
    "dz_Confirm":"队长确认",
    "Consult":"订单查询",
    //
    "ShuttleBus":"班车管理",
    "LineMaintenance":"线路维护",
    "Scheduling":"车辆排班",
    "SiteMaintenance":"站点维护",
    "ScManage":"校巴管理",
    "Sc_SiteMaintenance":"站点维护",
    "Sc_LineMaintenance":"线路维护",
    "Sc_Scheduling":"校巴排班",
    //
    "mergeVideo":"合并视频",
    "CloudPhoto":" 云图片库",
    "CloudVideo":"云视频库",
    "abnormal":"事件记录",
    "AccidentManagement":"事故管理",
    //
    "FinancialSettlement":"财务结算",
    "ReceivablesManagement":"收款管理",
    "PaymentManagement":"付款管理",
    "AccountingFormula":"核算公式",
    "Reimbursement":"报销记账",
    //
    "terminal":"终端管理",
    "terminal-car":"后视镜管理",
    "terminal_GPS":"定位器管理",
    "terminal_OBD":"OBD管理",
    //
    "Echarts":"数据报表",
    "SafeDriving":"安全驾驶",
    "OrderStatistics":"订单统计",
    "BusStatistics":"班车统计",
    "TrafficStatistics":"出车统计",
    "TerminalAnomaly":"终端异常",
    "OverspeedStatistics":"超速统计",
    "CollectionStatistics":"收款统计",
    "PaymentStatistics":"付款统计",
    "ReimbursementStatistics":"报销统计",
    //
    "OperationMonitoring":"终端监控",
    "VehicleMonitoring":"后视镜监控",
    "OBD_Monitor":"OBD监控",
    "GPS_Monitor":"定位器监控",
    "allcode":"聚合监控",
    "BusMonitor":"校巴监控",

    /**
     * 公共
     */
    ORDER: "序号",
    NAME: "姓名",
    TELPHONE_NUM: "电话号码",
    OPERATION: "操作",
    TYPE: "类型",
    STATUS: "状态",
    COMMONT: "备注",
    CAR_MANAGE: '车辆管理',
    PASSWORD:"密码",
    MALE:"男",
    FEMALE:"女",
    CANCEL: "取消",
    DETERMINE: "确定",
    CLOSE: "关闭",
    SET: "设置",
    ERROR_ALERM:"出错了！！!",
    CREATE:"新建",
    /**
     *系统设置
     * *    import i18nTabTit from '@/mixins/i18nTabTit'
     * tit:"",
     * {{$t("USER_MANAGEMENT")}}
     * :placeholder='$t("USER_NAME")'
     *   用户管理
     **/
    USER_MANAGEMENT: '用户管理',
    USER_NAME: "请输入用户姓名",
    PHONE_NUMBER: "请输入电话号码",
    ID: "账号",
    ID_NUMBER: "证件号码",
    DUTY: "职务",
    ADD:"新增",
    USER:"用户",
    //
    ORGANIZATION:"组织机构",
    USER_NAME_TAB:"用户名",
    NAME_TAB:"请输入姓名",
    DUTY_TAB:"请输入职务",
    ID_NUMBER_TAB:"请输入证件号码",
    PASSWORD_TAB:"请设置用户密码",
    USER_ACCOUNT:"请设置用户帐号",
    SELECT_ORGANIZATION:"请选择组织机构",
    INFORMATION:"信息",
    ADD_USER_SUCCESS:"用户注册成功",
    MODIFY_USER_SUCCESS:"用户修改成功",
    WRITE_INFOR:"请认真填写用户信息",
//
    ROLE_ASSIGN:"角色分配",
    DATA_LOADING:"数据加载中请稍后",
    DATA_TRANS:"数据传输",
    OPERATION_SUCCESS:"操作成功",



    /**
     * 角色管理
     */



    ROLE_MANAGEMENT: "角色管理",
    ROLE: "请输入角色",
    ROLE_NAME: "角色名称",
    //add
    ROLE_ADD:"角色",

    ROLE_NAME_TAB:"请填写角色名称",
    ROLE_CODE:"角色代码",
    ROLE_CODE_TAB:"请填写角色代码",
    ROLE_TYPE_TAB:"请选择角色类型",
    COMMENT:"请填写备注信息",
    PERMISSION:"权限选择",
    CREATE_ROLE:"新建角色",
    ADD_ROLE_SUCCESS:"角色注册成功",
    MODIFY_ROLE_SUCCESS:"角色修改成功",

    /**
     * 厂商管理
     */
    MANUFACTURER_MANAGEMENT: "厂商管理",
    /**
     * 字典管理
     */
    DIC_MANAGEMENT: "字典管理",
    DIC_INFORMATION: "请输入字典信息",
    /**
     * 服务管理
     */
    SERVISE_MANAGEMENT: "服务管理",
    SERVICE_NAME: "请输入服务名称",
    SERVICE_NAME_TAB: "服务名称",
    SERVICE_CODE: "服务代码",
    API_PREFIX: "API前缀",
    ICON_TAB: "图标",
    //
    CREATE_SERVICE:"新建服务",
    ADDRESS_TAB:"请填写地址",
    API_PREFIX_TAB:"请填写API前缀",
    STATUS_TAB:"请填选择状态",
    FUNCTION_NAME_ENTER:"请填写功能名称",
    CREATE_SUCCESS:"创建成功",
    CREATE_FAIL:"创建失败",
    FUNCTION_DATA:"功能数据",
    SERVICE_CODE_TAB:"请输入服务代码",
    EDIT_SERVICE:"编辑服务",

    /**
     * 功能管理
     */
    FUNCTION_MANAGEMENT: "功能管理",
    FUNCTION_NAME: "请输入功能名称",
    FUNCTION_NAME_TAB: "功能名称",
    FUNCTION_CODE: "功能代码",
    SURVICE_CODE_TAB: "服务代码",
    RANK: "排序",
    URL: "URL",
    PARENT_NODE: "父节点",
    //
    FUNCTION_:"功能",
    ORDER_ENTER:"请填写排序",
    PLEASE_ENTER:"请填写",
    LIVE_LINK:"跳转地址",
    API_SUFFIX:"API 后缀",
    /**
     *日志管理
     */
    DAILY_RECORD_MANAGEMENT: "日志管理",
    TIME_RESEARCH: "请输入时间",//public
    ACTION_TIME: "操作时间",
    TIME_CONSUMING: "耗时",
    OPERATOR: "操作人",
    PARAMETER: "参数",
    METHOD: "方法",
    ACTION_RESULT: "操作结果",


    /**
     * 意见反馈
     */
    SUGGESTION_FEEDBACK: "意见反馈",
    SERCH_FEEDBACK: "请输入反馈人姓名",
    FEEDBACK_NAME: "反馈人姓名",
    FEEDBACK_CONTENT: "反馈内容",
    FEEDBACK_STYLE: "反馈类型",
    FEEDBACK_TIME: "反馈时间",
    DEAL_RESULT: "处理结果",
    CONNECTION_METHOD: "联系方式",
    /**
     * 活动管理
     */
    ACTIVITY_MANAGEMENT: "活动管理",
    ACTIVITY_NAME: "请输入活动名称",
    ACTIVITY_TITLE: "活动标题",
    CONTENT: "内容/URL",
    ACTIVITY_STYLE: "活动类型",
    POSITION: "位置",
    ACCESSORY: "附件",
    //
    CREATE_ACTIVITY:"新建活动",

    /**
     *车辆调度
     *    import i18nTabTit from '@/mixins/i18nTabTit'
     * tit:"",
     * {{$t("USER_MANAGEMENT")}}
     * :placeholder='$t("USER_NAME")'
     *   车辆管理
     **/
    /**
     * public
     */
    OCCURRENCE_TIME: "发生时间",
    DRIVER: "司机",
    CAR_NUM_TAB: "车牌号",
    CREATE_TIME: "创建时间",
    CAR_TYPE_TAB: "车辆类型",
    //
    CAR_MANAGEMENT: "车辆管理",
    CAR_NUMBER: "请输入车牌号",//PUBLIC
    TERMINAL_NUM: "请输入终端编号",
    DRIVER_NAME: "请输入司机姓名",//public
    /**
     * 临时车管理
     * 单位管理
     */
    ORGANIZATION_MANAGEMENT: "单位管理",
    ORGANIZATION_NAME: "请输入单位名称",
    ORGANIZATION_ID: "单位编号",
    ORGANIZATION_NAME_TAB: "单位名称",
    SUPERVISOR: "负责人",
    REGISTER_NUM: "登记次数",
    CREATOR: "创建人",

    /**
     * 临时车管理
     * 临时车管理
     */
    TEMPORARY_CAR_MANAGEMENT: "临时车管理",
    CAR_FUNCTION_NAME: "请输入功能名称",
    SEAT_NUM: "座位数",
    TEMPORARY_ORGANIZATION: "临时单位",

    /**
     * 驾驶员管理
     */
    DRIVER_MANAGEMENT: "驾驶员管理",
    DRIVER_LICENSE_TYPE: "驾照类型",
    FIRST_ISSUE_DATE: "初次申领时间",
    FILE_NUM: "证件编号",
    DRIVER_LICENSE_VALID: "驾照有效期",
    HEALTH_STATUS: "健康状态",
    AGE: "年龄",
    BLOOD_TYPE: "血型",

    /**
     * 车队管理
     */
    MOTORCADE_MANAGEMENT: "车队管理",
    MOTORCADE_NAME: "请输入车队名称",
    CAPTAIN_NAME: "队长姓名",
    MOTORCADE_NAME_TAB: "车队名称",
    MOTORCADE_NUM: "车队编号",

    /**
     * 电子围栏
     */
    ELECTRONIC_FENCE: "电子围栏",
    FENCE_NAME: "围栏名称",

    /**
     * 超速限定
     */
    OVER_SPEED_LIMIT: "超速限定",
    SPEED_UP: "超速上限",
    /**
     * 合并视频
     */
    TERMINAL_NUMBER: "终端编号",//public
    // TIME_RESERCH

    START_TIME: "开始时间",
    EXTRACTION_TIME: "提取时长",
    /**
     * 云图片库
     */
    CLOUD_PHOTO: "云图片库",
    // TIME_RESEARCH
    // CAR_NUMBER
    //TERMINAL_NUM
    FRONT_CAMERA: "前摄像头",
    REAR_CAMERA: "后摄像头",
    /**
     * 云视频库
     */
    CLOUD_VIDEO: "云视频库",
    // TIME_RESEARCH
    // CAR_NUMBER
    //TERMINAL_NUM
    //FRONT_CAMERA
    //REAR_CAMERA
    /**
     * 事件记录
     */
    EVENT_RECORD: "事件记录",
    CAR_TYPE: "请选择车辆类型",
    // CAR_NUMBER
    //TERMINAL_NUM
    EVENT_TYPE: "请选择事件类型",
    //ORDER
    //CAR_NUM_TAB
    //TERMINAL_NUMBER
    EVENT_TYPE_TAB: "事件类型",
    //DRIVER

    //OCCURRENCE_TIME
    /**
     * 事故管理
     */
    ACCIDENT_MANAGEMENT: "事故管理",
    // CAR_NUM_TAB
    //DRIVER:"司机",
    //TELPHONE_NUM
    ACCIDENT_DESCRIBE: "事件描述",
    //ACCESSORY
    //OPERATION
    //OCCURRENCE_TIME

    /**
     * 终端管理
     *
     */
//TERMINAL_NUM:"请输入终端编号",
    //CAR_NUMBER:"请输入车牌号",
    ON_LINE_STATE: "请选择在线状态",
    BOUND_VEHICLE: "绑定车辆",//
    SERVICE_YEAR: "服务年限",
    ONE_YEAR: "一年",
    TWO_YEAR: "二年",
    THREE_YEAR: "三年",
    FIVE_YEAR: "五年",
    ON_LINE_STATE_TAB: "在线状态",
    ACTIVATED_STATE: "激活状态",
    REGISTRATION_DATE: "注册时间",
    ACTIVATED_DATE: "激活时间",
    VERSION_NUM_TAB: "版本号",
    EXPIRATION_DATE: "到期时间",
    MANUFACTURER: "制造商",
    EQUIPMENT_UPGRADE: "设备升级",
    INTERFACE_ADDRESS: "设置接口地址",
    SERVICE_RENEWAL: "服务续期",
    TERMINAL_DEL: "终端删除",
    INQUIRE: "查询",
    /**
     * from data
     */
    EQUIPMENT_NAME: "设备名称",
    TERMINAL_NAME: "请输入终端名称",//
    EQUIPMENT_STATE: "设备状态",
    NEW_TERMINAL_EQUIPMENT: "新建终端设备",
    MODEL: "型号",
    EQUIPMENT_MODEL: "请输入设备型号",//
    INTERFACE_ADDRESS_TAB: "接口地址",
    TERMINAL_INTERFACE_ADDRESS: "设备终端接口地址",


    /**
     * 后视镜管理
     */
    REARVIEW_MIRROR: "后视镜管理",
    //TERMINAL_NUM
    VERSION_NUM: "请输入版本号",
    //接口地址设置
    SET_INTERFACE_ADDRESS: "请输入接口地址",
    //升级
    ON_LINE_UPGRADE: "在线升级",
    CURRENT_VERSION: "当前版本",


    /**
     * 定位器管理
     */
    GPS_MANAGEMENT: "定位器管理",
    /**
     * OBD管理
     */
    OBD_MANAGEMENT: "OBD管理",
    /**
     * 财务结算
     *
     */
    PRINT:"打印",
    EDIT:"编辑",
    AMOUNT_COLLECT:"收款金额",//public

    RECEIVABLE:"应收单据",
    RECEIVED:"已收单据",
    FORMULA:"单笔费用结算公式：里程 * 单价 + 过路费 + 过桥费 + 等时费 = 合计总价",
     ENTER_START_TIME:"请输入开始时间",
    ENTER_STOP_TIME:"请输入结束时间",
    USE_UNIT:"请选择用车单位",

    TODAY:"今天",
    THREE_DAY_AGO:"三天前",
    WEEK_AGO:"一周前",
    //付款管理
    PAYABLE:"应付单据",
    PAID:"已付单据",
    SEARCH_DRIVER:"请输入司机姓名搜索",
    PAY_FORMULA:"单笔费用结算公式：里程 * 单价 + 过路费 + 过桥费 + 等时费 = 合计总价",

    USERS:"用车人员",
    WAITING_PLACE:"候车地点",
    DESTINATION:"目的地",
    TIME_DEPARTURE:"出车时间",
    MILEAGE:"里程(公里)",
    TOTAL_FARE:"车费合计",
    CONTENT_TAB:"事由",
    ORDER_TAB:"请选择订单",
    PAID_CONFIRM:"确认已付款?",
    //核算公式
    ACCOUNT_FORMULA:"核算公式",
    //CAR_FUNCTION_NAME
    CONTENT_:"内容",
    AMOUNT:"金额",
    MILEAGE_TAB:"里程",
    OVERTIME:"加班",
    HOLIDAY:"节假日",
    //
    REIMBURSES:"报销记账",
    EWIMBUR_PERSON:"报销人",
    MATTER:"报销事项",
    TIME:"报销时间",
    REIM_AMOUNT:"报销金额",
    NUM_INVOICE:"发票数量",












    /**
     * map
     * '$t("USER_NAME")'
     * {{$t("USER_MANAGEMENT")}}
     */
    TERMINAL_MANAGE_MAP:"Terminal Manage",
    FIND_V:"查设备、找车辆、找司机",
    NONE_BOUND:"暂无绑定",
    OBD_INF:"OBD信息",
    NONE_DATA:"暂无数据",
    UPDATE_DATE:"更新日期",
    ENGINE:"engine revolution",
    ENGINE_REVOLUTION:"发动机转速",
    SPEED:"车速",
    RESIDUAL_OIL:"剩余油量",
    OIL:"oil consumption",
    OIL_CONSUMPTION:"耗油量",
    ERROR_REPORT:"故障报告",
    OFFLINE:"离线",
    FLAMEOUT:"熄火",
    IGNITE:"点火",
    ALL:"全部",
    //拍照
    FRONT_CAMERA_:"前摄像头拍照",
    REAR_CAMERA_:"后摄像头拍照",
    ALL_CAMERA:"前后摄像拍照",
    FRONT_VIDEO:"前摄像头视频",
    REAR_VIDEO:"后摄像头视频",
    ALL_VIDEO:"前后摄像视频",
    HISTORICAL_ROUTE:"历史轨迹",
    HIS:"historical route",
    PHOTO_DATABASE:"图片库",
    VIDEO_DATABASE:"视频库",








}
