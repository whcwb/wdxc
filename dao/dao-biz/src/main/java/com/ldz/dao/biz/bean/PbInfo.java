package com.ldz.dao.biz.bean;

import java.util.Date;

import com.ldz.dao.biz.model.ClCl;
import com.ldz.dao.biz.model.ClXl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 用于处理车辆/校巴 日历展示模型
 */

@Getter
@Setter
@ToString
public class PbInfo {
    /**
     * ID
     */
    private String id;

    /**
     * 车牌号
     */
    private String cph;

    /**
     * 线路id
     */
    private String xlId;

    /**
     * 排班时间
     */
    private Date pbsj;

    /**
     * 司机
     */
    private String sj;

    /**
     * 司机姓名
     */
    private String sjxm;

    /**
     * 机构代码
     */
    private String jgdm;

    /**
     * 机构名称
     */
    private String jgmc;

    /**
     * 车辆id
     */
    private String clId;

    /**
     * 创建时间
     */
    private Date cjsj;

    /**
     * 创建人
     */
    private String cjr;

    /**
     * 修改时间
     */
    private Date xgj;

    /**
     * 修改人
     */
    private String xgr;
    /**
     * 车辆
     */
	private ClCl clcl;
	  /**
     * 线路
     */
	private ClXl clXl;
}
