package com.ldz.geo.util;

import com.ldz.geo.bean.GeoModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * auther chenwei
 * create at 2018/8/4
 */
@Component
public class GeoUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 添加位置
     * @param geoModel
     */
    public void add(GeoModel geoModel){
        if (StringUtils.isEmpty(geoModel.getKey())
                || StringUtils.isEmpty(geoModel.getName())
                || geoModel.getLat() == null
                || geoModel.getLng() == null
                ){
            return;
        }
        redisTemplate.opsForGeo().geoAdd(geoModel.getKey(),
                new Point(geoModel.getLng(),geoModel.getLat()),
                geoModel.getName());
        if (geoModel.getExpire() != null && geoModel.getTimeUnit() != null){
            redisTemplate.expire(geoModel.getKey(),geoModel.getExpire(),geoModel.getTimeUnit());
        }
    }

    /**
     * 获取某个地理位置的坐标
     * @param key
     * @param names
     * @return
     */
    public List<Point> get(String key,String... names){
        List<Point> points = redisTemplate.boundGeoOps(key).geoPos(names);
        return points;
    }

    /**
     * 获取两个地理位置的距离
     * @param key
     * @param name1
     * @param name2
     * @return
     */
    public double dist(String key,String name1,String name2){
        Distance distance = redisTemplate.boundGeoOps(key).geoDist(name1,name2);
        return distance.getValue();// 使用米作为统一单位
    }


    /**
     * 根据给定地理位置坐标获取指定范围内的地理位置集合
     * @param key
     * @param lng
     * @param lat
     * @param radius
     * @param withDist
     * @param sort
     * @param count
     * @return
     */
    public GeoResults<GeoLocation<String>> getRadius(String key,double lng,double lat,float radius,boolean withDist,String sort,long count){
        Circle circle = new Circle(new Point(lng,lat),new Distance(radius));
        GeoRadiusCommandArgs  args = GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates();
        if (withDist) args.includeDistance();
        if ("ASC".equals(sort)){
            args.sortAscending();
        }else if("DESC".equals(sort)){
            args.sortDescending();
        }
        if (count != 0){
            args.limit(count);
        }

        GeoResults<GeoLocation<String>> result = redisTemplate.boundGeoOps(key).geoRadius(circle,args);
        return result;
    }

    /**
     * 根据给定地理位置获取指定范围内的地理位置集合
     * @param key
     * @param name
     * @param radius
     * @param withDist
     * @param sort
     * @param count
     * @return
     */
    public GeoResults<GeoLocation<String>> getRadiusByMember(String key,String name,float radius,boolean withDist,String sort,long count){
//        Circle circle = new Circle(new Point(lng,lat),new Distance(radius));
        Distance distance = new Distance(radius);
        GeoRadiusCommandArgs  args = GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates();
        if (withDist) args.includeDistance();
        if ("ASC".equals(sort)){
            args.sortAscending();
        }else if("DESC".equals(sort)){
            args.sortDescending();
        }
        if (count != 0){
            args.limit(count);
        }

        GeoResults<GeoLocation<String>> result = redisTemplate.boundGeoOps(key).geoRadiusByMember(key+" "+name,distance,args);
        return result;
    }

    /**
     * 批量添加位置
     * @param key
     * @param geoModels
     * @param expire
     * @param timeUnit
     */
    public void add(String key, List<GeoModel> geoModels, int expire, TimeUnit timeUnit){
        if (CollectionUtils.isEmpty(geoModels))return;
        List<GeoLocation<String>> locations = new ArrayList<>();
        for (GeoModel geoModel : geoModels) {
            Point point = new Point(geoModel.getLng(),geoModel.getLat());
            GeoLocation location = new GeoLocation(geoModel.getName(),point);
            locations.add(location);
        }
        redisTemplate.opsForGeo().geoAdd(key,locations);

        if (timeUnit != null){
            redisTemplate.expire(key,expire,timeUnit);
        }
    }
}
