package com.ldz.job.service.impl;

import com.ldz.dao.biz.mapper.ClGpsLsMapper;
import com.ldz.dao.biz.mapper.ClGpsMapper;
import com.ldz.dao.biz.mapper.ClyyMapper;
import com.ldz.dao.biz.model.ClGps;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.dao.biz.model.Clyy;
import com.ldz.job.service.GpsService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.bean.TrackJiuPian;
import com.ldz.util.bean.TrackPoint;
import com.ldz.util.bean.TrackPointsForReturn;
import com.ldz.util.bean.TrackPointsForReturn.Point;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.util.yingyan.GuiJIApi;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class GpsServiceImpl extends BaseServiceImpl<ClGps, String> implements GpsService {


    Logger errorLog = LoggerFactory.getLogger("error_info");
    Logger accessLog = LoggerFactory.getLogger("access_info");

    @Autowired
    private ClGpsMapper entityMapper;
    @Autowired
    private RedisTemplateUtil redis;
    @Autowired
    private ClyyMapper clyymapper;
    @Autowired
    private ClGpsLsMapper clgpslsMapper;

    @Override
    protected Mapper<ClGps> getBaseMapper() {
        return entityMapper;
    }

    @Override
    public void insetAndUpdate(ClGps entity) {
        if (StringUtils.isBlank(entity.getZdbh())) {
            return;
        }

        boolean flag = ifExists("zdbh", entity.getZdbh());
        if (flag == true) {
            update(entity);
        } else {
            save(entity);
        }
    }

    @Override
    public void InsetRedisToDb() {

        /*redis.keys(ClGps.class.getSimpleName()+"*");*/

        Set<Object> keys = redis.keys(ClGps.class.getSimpleName() + "*");
        List<ClGpsLs> list = new ArrayList<>();
        for (Object s : keys) {
            String key = (String) s;
            try {
                if (StringUtils.contains(key, "ClGpsLs")) {

                    BoundListOperations<Object, Object> boundListOps = redis.boundListOps(key);
                    String index = (String) boundListOps.index(0);
                    if (StringUtils.isNotEmpty(index)) {
                        Long length = boundListOps.size();
                        for (int i = 0; i < length; i++) {
                            String clgpsls = (String) boundListOps.rightPop();
                            ClGpsLs gpssss = JsonUtil.toBean(clgpsls, ClGpsLs.class);

                            list.add(gpssss);
                        }
                    }
                } else {
                    String bean = (String) redis.boundValueOps(key).get();
                    if (bean != null) {
                        ClGps object = JsonUtil.toBean(bean, ClGps.class);
                        if (object.getJd() == null || object.getWd() == null || object.getJd().doubleValue() <= 0 || object.getWd().doubleValue() <= 0 ||
                                object.getBdjd() == null || object.getBdwd() == null || object.getBdjd().doubleValue() <= 0 || object.getBdwd().doubleValue() <= 0
                                || object.getWd().doubleValue() > 100) {
                            // 异常实时点位 ， 不存储 , 删除此redis key
                            redis.delete(key);
                            continue;
                        }
                        // 将该终端的实时点位插入数据库中
                        insetAndUpdate(object);
                    }
                }
            } catch (Exception e) {
                errorLog.error("GPS数据存储异常", e);
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            clgpslsMapper.insertList(list);
        }
		/*String bean = (String) redis.boundValueOps(ClGps.class.getSimpleName()).get();
		if (bean != null) {
			ClGps object = JsonUtil.toBean(bean, ClGps.class);
			// 将该终端的实时点位插入数据库中
			insetAndUpdate(object);
		}

		BoundListOperations<Object, Object> boundListOps = redis.boundListOps(ClGpsLs.class.getSimpleName() );

		String index = (String) boundListOps.index(0);
		if (StringUtils.isNotEmpty(index)) {
				Long length = boundListOps.size();
			List<ClGpsLs> list = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				String clgpsls = (String) boundListOps.rightPop();
				ClGpsLs gpssss = JsonUtil.toBean(clgpsls, ClGpsLs.class);

				list.add(gpssss);

			}

			clgpslsMapper.insertList(list);

			//将集合按照100个拆分(鹰眼批量上传点位规则)
//			List<List<ClGpsLs>> splitList = splitList(list,100);
//
//			for (List<ClGpsLs> list2 : splitList) {
//
//				if (CollectionUtils.isNotEmpty(list2)) {
//					try {
//						AddPointResponse addPoints = GuiJIApi.addPoints(changeModel(list2), GuiJIApi.addPointsURL);
//						accessLog.debug("成功上传鹰眼的点位:" + list2 + "状态为:" + addPoints);
//					} catch (Exception e) {
//						errorLog.error("上传鹰眼失败", e);
//						continue;
//					}
//
//				}
//			}





		}*/
    }

    public List<TrackPoint> changeModel(List<ClGpsLs> list) {
        List<TrackPoint> TrackPointlist = new ArrayList<>();
        for (ClGpsLs clgps : list) {
            TrackPoint tracktPoint = new TrackPoint();
            tracktPoint.set_object_key(clgps.getYxsd());
            tracktPoint.setAk(GuiJIApi.AK);
            tracktPoint.setCoord_type_input("bd09ll");
            tracktPoint.setEntity_name(clgps.getZdbh());
            tracktPoint.setLatitude(clgps.getBdwd());
            tracktPoint.setLoc_time((clgps.getCjsj().getTime()) / 1000);
            tracktPoint.setLongitude(clgps.getBdjd());
            tracktPoint.setService_id(GuiJIApi.SERVICE_ID);
            tracktPoint.setSpeed(Double.valueOf(clgps.getYxsd()));
            tracktPoint.setDirection((int) Math.ceil(clgps.getFxj().doubleValue()));
            TrackPointlist.add(tracktPoint);
        }

        return TrackPointlist;
    }


    @Override
    public void guiJiJiuPian(String zdbh, long startTime, long endTime) {

        TrackJiuPian guijis = new TrackJiuPian();
        guijis.setAk(GuiJIApi.AK);
        guijis.setService_id(GuiJIApi.SERVICE_ID);
        guijis.setEntity_name(zdbh);
        guijis.setIs_processed("1");
        // 查询 去燥 ，抽希 ， 绑路 的坐标点
        guijis.setProcess_option("need_denoise=1,need_vacuate=1,need_mapmatch=1,transport_mode=driving");
        guijis.setSupplement_mode("driving");
        guijis.setSort_type("asc");
        guijis.setCoord_type_output("bd09ll");
        guijis.setEnd_time(String.valueOf(endTime / 1000));
        guijis.setStart_time(String.valueOf(startTime / 1000));
        guijis.setPage_size("5000");
        // 查询 6小时内的轨迹坐标点
        TrackPointsForReturn points = GuiJIApi.getPoints(guijis, GuiJIApi.getPointsURL);
        List<Point> points2 = points.getPoints();
        if (CollectionUtils.isNotEmpty(points2)) {
            List<Clyy> yyList = new ArrayList<>();

            for (Point point : points2) {
                Clyy clyy = new Clyy();
                clyy.setDirection(point.getDirection() + "");
                clyy.setId(genId());
                clyy.setLatitude(BigDecimal.valueOf(point.getLatitude()));
                clyy.setLoc_time(parse(point.getLoc_time()));
                clyy.setLongitude(BigDecimal.valueOf(point.getLongitude()));
                clyy.setSpeed(BigDecimal.valueOf(point.getSpeed()));
                clyy.setZdbh(zdbh);
                yyList.add(clyy);
            }
            clyymapper.insertList(yyList);

        } else {
            return;
        }

    }

    public static List<List<ClGpsLs>> splitList(List<ClGpsLs> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }

        List<List<ClGpsLs>> result = new ArrayList<List<ClGpsLs>>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<ClGpsLs> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    public String parse(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Long aLong = time * 1000;
        Date date = new Date(aLong);
        String format = simpleDateFormat.format(date);
        return format;
    }


    public static void main(String[] args) {
        List<ClGpsLs> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ClGpsLs clGpsLs = new ClGpsLs();
            clGpsLs.setZdbh("1111");
            list.add(clGpsLs);
        }
        List<List<ClGpsLs>> splitList = splitList(list, 100);

        System.out.println(splitList.get(0).size());


    }
}
