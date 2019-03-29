package com.ldz.wechat.module.service.impl;


import com.github.pagehelper.PageInfo;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.gps.LatLonUtil;
import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.base.LimitedCondition;
import com.ldz.wechat.module.mapper.ClDdMapper;
import com.ldz.wechat.module.model.ClDd;
import com.ldz.wechat.module.model.ClJsy;
import com.ldz.wechat.module.service.ClJsyService;
import com.ldz.wechat.module.service.SjDdService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.common.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SjDdServiceImpl extends BaseServiceImpl<ClDd,String> implements SjDdService {

    @Autowired
    private ClDdMapper entityMapper;
    @Autowired
    private ClJsyService jsyService;
    @Autowired

    @Override
    protected Mapper<ClDd> getBaseMapper() {
        return entityMapper;
    }

    @Override
    public boolean fillCondition(LimitedCondition condition){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userType = (String) request.getAttribute("type");
        RuntimeCheck.ifBlank(userType,"当前登录用户未空！");
        RuntimeCheck.ifFalse(StringUtils.equals(userType,"jsy"),"请用司机角色登录");
        String userId=null;
        if ("jsy".equals(userType)){
            String userInfo = (String) request.getAttribute("userInfo");
            ClJsy jsy = JsonUtil.toBean(userInfo,ClJsy.class);
            RuntimeCheck.ifNull(jsy,"未找到驾驶员信息");
            userId= jsy.getSfzhm();
        }
        RuntimeCheck.ifBlank(userId,"当前登录用户未空！");
        condition.eq(ClDd.InnerColumn.sj.name(),userId);

        String ddType = request.getParameter("ddType");//订单状态
        if(StringUtils.equals(ddType,"2")) {//待确认
            // 10-订单创建；11-订单确认；12-订单驳回；13-已派单；20-司机完成行程(行程结束)；30-队长确认
            condition.eq(ClDd.InnerColumn.ddzt.name(),"13");
            condition.setOrderByClause(ClDd.InnerColumn.yysj.asc());
        }else if(StringUtils.equals(ddType,"3")) {//历史单据
            // 10-订单创建；11-订单确认；12-订单驳回；13-已派单；20-司机完成行程(行程结束)；30-队长确认
            List<String> list= new ArrayList<String>();
            list.add("13");
            condition.notIn(ClDd.InnerColumn.ddzt.name(),list);
            condition.setOrderByClause(ClDd.InnerColumn.yysj.desc());
        }else{
            RuntimeCheck.ifTrue(true, "未找到记录");
        }

        condition.setOrderByClause(" yysj desc");
        return true;
    }

    @Override
    public void afterPager(PageInfo<ClDd> pageInfo){
        List<String> driverIds = pageInfo.getList().stream().map(ClDd::getSj).collect(Collectors.toList());
        if (driverIds.size() == 0)return;
        List<ClJsy> drivers =  jsyService.findIn(ClJsy.InnerColumn.sfzhm,driverIds);
        if (drivers.size() == 0)return;
        Map<String,ClJsy> driverMap = drivers.stream().collect(Collectors.toMap(ClJsy::getSfzhm,p->p));
        for (ClDd dd : pageInfo.getList()) {
            //计算出GPS的距离
            BigDecimal originLat=dd.getOriginLat();//起始纬度
            BigDecimal originLng=dd.getOriginLng();//起始经度
            BigDecimal destinationLat=dd.getDestinationLat();//结束点经度
            BigDecimal destinationLng=dd.getDestinationLng();//结束点纬度
            if(originLat!=null&&originLng!=null&&destinationLat!=null&&destinationLng!=null){
                double gpsDistance= LatLonUtil.getDistance(originLng.doubleValue(),originLat.doubleValue(),destinationLng.doubleValue(),destinationLat.doubleValue());
                if(gpsDistance>0){
                    dd.setGpsDistance(String.valueOf(gpsDistance));
                }else{
                    dd.setGpsDistance("0");
                }
            }else{
//                dd.setGpsDistance("0");
            }

            String driverId = dd.getSj();
            if (StringUtils.isEmpty(driverId))continue;
            ClJsy driver = driverMap.get(driverId);
            if (driver == null)continue;
            dd.setSjdh(driver.getSjh());



        }
    }

}
