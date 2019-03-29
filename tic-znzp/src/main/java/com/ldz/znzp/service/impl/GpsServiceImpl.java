package com.ldz.znzp.service.impl;

import com.ldz.util.gps.Gps;
import com.ldz.util.gps.PositionUtil;
import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.bean.GpsInfo;
import com.ldz.znzp.mapper.ClGpsMapper;
import com.ldz.znzp.model.ClGps;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class GpsServiceImpl extends BaseServiceImpl<ClGps,String> implements GpsService{
    @Autowired
    private ClGpsMapper entityMapper;

    @Override
    protected Mapper<ClGps> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClGps.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClGps entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }

    @Override
    public ClGps changeCoordinates(GpsInfo entity) {
        ClGps clGps = new ClGps();
        if (entity.getLatitude()!=null) {
            clGps.setWd(new BigDecimal(entity.getLatitude()));
        }
        if (entity.getLongitude()!=null) {
            clGps.setJd(new BigDecimal(entity.getLongitude()));
        }
        clGps.setCjsj(new Date());
        if (entity.getGpsjd() != null && entity.getGpsjd().length()<=3) {
            clGps.setDwjd(Short.valueOf(entity.getGpsjd()));
        }
        if (entity.getFxj() != null) {
            clGps.setFxj(new BigDecimal(entity.getFxj()));
        }
        clGps.setZdbh(entity.getDeviceId());
        if (entity.getSpeed() != null) {
            clGps.setYxsd(String.valueOf(entity.getSpeed()));
        }
        // 将收到的gps转换成火星坐标系(谷歌)
        Gps gps84_To_Gcj02 = PositionUtil.gps84_To_Gcj02(clGps.getWd().doubleValue(), clGps.getJd().doubleValue());
        // 将火星系坐标转换成百度坐标
        Gps gcj02_To_Bd09 = PositionUtil.gcj02_To_Bd09(gps84_To_Gcj02.getWgLat(), gps84_To_Gcj02.getWgLon());
        // 保存gps对象
        clGps.setBdjd(new BigDecimal(gcj02_To_Bd09.getWgLon()));
        clGps.setBdwd(new BigDecimal(gcj02_To_Bd09.getWgLat()));
        clGps.setGgjd(new BigDecimal(gps84_To_Gcj02.getWgLon()));
        clGps.setGgwd(new BigDecimal(gps84_To_Gcj02.getWgLat()));
        return clGps;
    }
}
