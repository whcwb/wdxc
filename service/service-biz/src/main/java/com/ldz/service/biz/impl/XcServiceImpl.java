package com.ldz.service.biz.impl;


import com.ldz.dao.biz.mapper.ClXcMapper;
import com.ldz.dao.biz.model.ClXc;
import com.ldz.service.biz.interfaces.XcService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.exception.RuntimeCheck;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.text.ParseException;
import java.util.*;

@Service
public class XcServiceImpl extends BaseServiceImpl<ClXc,String> implements XcService {
    @Autowired
    private ClXcMapper entityMapper;

    Logger error = LoggerFactory.getLogger("error_info");
    @Override
    protected Mapper<ClXc> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClXc.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClXc clXc) {
        clXc.setId(genId());
        int i = save(clXc);
        return i==1 ? ApiResponse.success():ApiResponse.fail();
    }

    @Override
    public ApiResponse<List<Map<String, Object>>> history(String zdbh, String startTime, String endTime) {
        RuntimeCheck.ifBlank(zdbh,"请选择车辆");
        SimpleCondition condition = new SimpleCondition(ClXc.class);
        condition.eq(ClXc.InnerColumn.clZdbh,zdbh);
        condition.lte(ClXc.InnerColumn.xcJssj,endTime);
        condition.gte(ClXc.InnerColumn.xcKssj,startTime);
        condition.setOrderByClause( " XC_KSSJ ASC,XC_JSSJ ASC");
        List<ClXc> xcList = findByCondition(condition);
        List<Map<String,Object>> list = new ArrayList<>(xcList.size());
        if (xcList.size() == 0){
            return ApiResponse.success(list);
        }
        for (ClXc xc : xcList) {
            if (StringUtils.isEmpty(xc.getXcStartEnd())){
                continue;
            }
            String[] startAndEndPoint = xc.getXcStartEnd().split(",");
            String startPoint = startAndEndPoint[0].replace("-",",");
            String endPoint = startAndEndPoint[1].replace("-",",");
            String distance = "0";
            if(startAndEndPoint.length >= 3){
                distance = startAndEndPoint[2];
            }
            Map<String,Object> map = new HashMap<>();
            map.put("jsjps",endPoint);
            map.put("ksjps",startPoint);
            map.put("kssj",xc.getXcKssj());
            map.put("jssj",xc.getXcJssj());
            map.put("distance",distance);
            map.put("startAddress",filterAddress(xc.getStartAddress()));
            map.put("endAddress",filterAddress(xc.getEndAddress()));
            map.put("zgss",xc.getXcZgss());
            map.put("pjss", xc.getXcPjss());
            map.put("jjs", xc.getXcJjscs());
            map.put("jsc", xc.getXcJsccs());
            map.put("jzw", xc.getXcJzwcs());
            long sc = 0 ;
            try {
                Date startDate = DateUtils.getDate(xc.getXcKssj(),"yyyy-MM-dd HH:mm:ss");
                Date endDate = DateUtils.getDate(xc.getXcJssj(),"yyyy-MM-dd HH:mm:ss");
                sc = endDate.getTime() - startDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            map.put("sc",sc);
            list.add(map);
        }
        return ApiResponse.success(list);
    }





    @Override
    public ApiResponse<String> batchParseAddress() {
        String minDate = getRequestParamterAsString("minDate");
        String maxDate = getRequestParamterAsString("maxDate");
        SimpleCondition condition = new SimpleCondition(ClXc.class);
        if (StringUtils.isBlank(minDate)) minDate = DateUtils.getToday();
        if (StringUtils.isBlank(maxDate)) maxDate = DateUtils.getToday();
        minDate += " 00:00:00";
        maxDate += " 23:59:59";
        condition.gte(ClXc.InnerColumn.xcKssj,minDate);
        condition.lte(ClXc.InnerColumn.xcKssj,maxDate);
        condition.and().andIsNull(ClXc.InnerColumn.startAddress.name());
        List<ClXc> xcs = entityMapper.selectByExample(condition);
        if (xcs.size() == 0) {
            return ApiResponse.success("没有要执行的数据");
        }
        for (ClXc xc : xcs) {
            try{
                String[] arrs = xc.getXcStartEnd().split(",");
                String[] startPoint = arrs[0].split("-");
                String startAddress = getAddress(startPoint[1],startPoint[0],"", "0");
                String[] endPoint = arrs[1].split("-");
                String endAddress = getAddress(endPoint[1],endPoint[0],"", "0");
                xc.setStartAddress(startAddress);
                xc.setEndAddress(endAddress);
                entityMapper.updateByPrimaryKeySelective(xc);
            }catch(Exception ex){

            }
        }
        return ApiResponse.success("执行成功");
    }

    @Override
    public String getAddress(String lat, String lng, String type, String area){
        String url = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location="+lat+","+lng+"&output=json&ak=evDHwrRoILvlkrvaZEFiGp30" + (!StringUtils.isBlank(type)?"&coordtype=" +type:"");
        String res = HttpUtil.get(url);
        int index = res.indexOf("formatted_address");
        if (index < 0) return "";
        String address = res.substring(index + 20);
        address = address.substring(0,address.indexOf("\""));
        if(!StringUtils.equals(area, "1")){
            return filterAddress(address);
        }
        return address;
    }

    private static String filterAddress(String s){
        if (StringUtils.isBlank(s)) return null;
        String[] key = {"区","县","市","省"};
        for (String s1 : key) {
            int index = s.lastIndexOf(s1);
            if (index >= 0 && index != s.length() -1){
                s = s.substring(index+1);
            }
        }
        return s;
    }

    public static void main(String[] args) {
        String s = "浙江省杭州市滨江区";
        String r = filterAddress(s);
        System.out.println(r);
    }

}
