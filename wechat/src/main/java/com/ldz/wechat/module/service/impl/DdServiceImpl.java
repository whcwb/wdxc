package com.ldz.wechat.module.service.impl;


import com.github.pagehelper.PageInfo;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.MathUtil;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.gps.DistanceUtil;
import com.ldz.util.gps.LatLonUtil;
import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.base.LimitedCondition;
import com.ldz.wechat.module.mapper.*;
import com.ldz.wechat.module.model.*;
import com.ldz.wechat.module.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DdServiceImpl extends BaseServiceImpl<ClDd,String> implements DdService {

    @Autowired
    private ClDdMapper entityMapper;
    @Autowired
    private ClJsyService jsyService;
    @Autowired
    private DdrzService ddrzService;
    @Autowired
    private ClClMapper clMapper;
    @Autowired
    private JgService jgService;
    @Autowired
    private ClGpsLsMapper gpsLsMapper;
    @Autowired
    private ClyyMapper clyyMapper;
    @Autowired
    private ClYyService clYyService;

    @Autowired
    private YhService yhService;
    @Autowired
    private ClDdrzMapper ddrzMapper;//历史订单明细表
    @Autowired
    private SysJzgxxService jzgxxService;
    @Override
    protected Mapper<ClDd> getBaseMapper() {
        return entityMapper;
    }

    @Override
    public boolean fillCondition(LimitedCondition condition){
        String userId = getCurrentUser(true);
        condition.eq(ClDd.InnerColumn.ckCjl,userId);
        condition.setOrderByClause("cjsj desc");
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


    public ApiResponse<String> saveEntity(ClDd entity, String userId){
        SysJzgxx clJsy= jzgxxService.findById(userId);
        RuntimeCheck.ifNull(clJsy, "未找到记录");
        RuntimeCheck.ifBlank(entity.getHcdz(),"候车地址不能为空");
        RuntimeCheck.ifBlank(entity.getMdd(),"目的地不能为空");
        RuntimeCheck.ifBlank(entity.getCllx(),"车辆车型不能为空");
        RuntimeCheck.ifNull(entity.getZws(),"乘客人数不能为空");
        RuntimeCheck.ifNull(entity.getYysj(),"乘客预车时间不能为空");


		RuntimeCheck.ifFalse(entity.getOriginLat()!=null, "起始纬度不能为空");
		RuntimeCheck.ifFalse(entity.getOriginLng()!=null, "起始经度不能为空");
		RuntimeCheck.ifFalse(entity.getDestinationLat()!=null, "结束点纬度不能为空");
		RuntimeCheck.ifFalse(entity.getDestinationLng()!=null, "结束点经度不能为空");

        String orderId=genId();
        entity.setId(orderId);
//        entity.setCjr(userId);
        entity.setCkCjl(userId);//乘客创建人
        entity.setCk(clJsy.getXm());
        entity.setJgdm(clJsy.getJgdm());
        entity.setJgmc(clJsy.getJdmc());
        if (StringUtils.isEmpty(entity.getCklxdh())){
            if(clJsy.getSjhm()!=null){
                entity.setCklxdh(clJsy.getSjhm());
            }
        }
        RuntimeCheck.ifBlank(entity.getCklxdh(),"该职工未预留手机号码，所以暂不能约车");
        entity.setCjsj(new Date());
        entity.setFkzt("00"); // 未付款
        entity.setDdzt("10");//10-订单创建；11-订单确认；12-订单驳回；13-已派单；20-司机确认(行程结束)；30-队长确认
        int i=entityMapper.insertSelective(entity);
        RuntimeCheck.ifTrue(i==0,"订单入库失败");

        ddrzService.log(entity);
        return ApiResponse.success();
    }

  public   ApiResponse<String> updateOrder(ClDd entity,String userId){

//        1、查找该ID是否存在
      ClDd clDd=findById(entity.getId());
      RuntimeCheck.ifNull(clDd,"未找到订单记录");
      String ddzt=clDd.getDdzt();
//        订单状态  10-订单创建；11-订单确认(待派单)；12-订单驳回；13-已派单；20-司机确认(行程结束)；30-队长确认; 40-财务已收

      if(StringUtils.equals(ddzt,"13")){
          ClJsy clJsy= jsyService.findById(userId);
          RuntimeCheck.ifFalse(StringUtils.equals(clDd.getSj(),clJsy.getSfzhm()),"您不能对非本人的订单进行操作");
      }else{
          RuntimeCheck.ifTrue(true,"订单状态异常，不能进行订单编辑操作");
      }

      ClDd newClDd=new ClDd();
      newClDd.setId(clDd.getId());
      newClDd.setSj(userId);//修改人
      newClDd.setXgsj(new Date());//修改时间
      newClDd.setGlf(entity.getGlf());//过路费
      newClDd.setGqf(entity.getGqf());//过桥费
      newClDd.setSy(entity.getSy());//事由
      newClDd.setSc(entity.getSc());//时长
      newClDd.setDj(entity.getDj());//单价
      newClDd.setLc(entity.getLc());//里程
      newClDd.setScf(entity.getScf());//时长费
      newClDd.setLcf(entity.getLcf());//里程费
      newClDd.setFkzt("00"); // 未付款
      newClDd.setDdzt("20");//订单状态
      newClDd.setSjqrsj(new Date());
      double amount = MathUtil.mul(entity.getLc(),entity.getDj());
      amount = MathUtil.add(amount,entity.getGqf());
      amount = MathUtil.add(amount,entity.getGlf());
      newClDd.setZj(amount);

      int i=update(newClDd);
      if(i==0){
          RuntimeCheck.ifFalse(false,"修改订单失败");
          return ApiResponse.fail("操作数据库失败");
      }else{
          ClDdrz clDdrz=new ClDdrz();
          clDdrz.setId(genId());//主键ID
          clDdrz.setDdId(clDd.getId());//订单ID
          clDdrz.setCjsj(new Date());//创建时间
          clDdrz.setCjr(userId);//创建人
          clDdrz.setJgdm(clDd.getJgdm());//机构代码
          clDdrz.setJgmc(clDd.getJgmc());//机构名称
          clDdrz.setDdzt(newClDd.getDdzt());//订单状态
          i=ddrzMapper.insertSelective(clDdrz);

          return ApiResponse.success();
      }
  }

    /**
     * 教职工 订单查询
     * @return
     */
    public ApiResponse<List<ClDd>> getOrderWorkersList(String userId){
        ApiResponse<List<ClDd>> result = new ApiResponse<List<ClDd>>();
        SimpleCondition condition = new SimpleCondition(ClDd.class);
        condition.eq(ClDd.InnerColumn.ckCjl.name(),userId);
        condition.setOrderByClause(ClDd.InnerColumn.yysj.desc());
        List<ClDd> orders = findByCondition(condition);
        List<String> carIds = orders.stream().map(ClDd::getClId).collect(Collectors.toList());
        condition = new SimpleCondition(ClCl.class);
        condition.in(ClCl.InnerColumn.clId,carIds);
        List<ClCl> cars = clMapper.selectByExample(condition);
        Map<String,ClCl> carMap = cars.stream().collect(Collectors.toMap(ClCl::getClId,p->p));
        for (ClDd order : orders) {
            String carId = order.getClId();
            if (StringUtils.isEmpty(carId))continue;
            ClCl car = carMap.get(carId);
            if (car == null)continue;
            order.setScs(car.getScs());
            order.setXh(car.getXh());
        }
        result.setResult(orders);
        return result;
    }
    /**
     * 列表 订单查询
     * @param type  2、待确认  3、历史单据
     * @return
     */
    public ApiResponse<List<ClDd>> getOrderDriverList(String userId, String type){
        ApiResponse<List<ClDd>> result = new ApiResponse<List<ClDd>>();
        SimpleCondition condition = new SimpleCondition(ClDd.class);
        condition.eq(ClDd.InnerColumn.sj.name(),userId);

        if(StringUtils.equals(type,"2")) {//待确认
            // 10-订单创建；11-订单确认；12-订单驳回；13-已派单；20-司机完成行程(行程结束)；30-队长确认
            condition.eq(ClDd.InnerColumn.ddzt.name(),"13");
           condition.setOrderByClause(ClDd.InnerColumn.yysj.asc());
        }else if(StringUtils.equals(type,"3")) {//历史单据
            // 10-订单创建；11-订单确认；12-订单驳回；13-已派单；20-司机完成行程(行程结束)；30-队长确认
            List<String> list= new ArrayList<String>();
            list.add("13");
            condition.notIn(ClDd.InnerColumn.ddzt.name(),list);
           condition.setOrderByClause(ClDd.InnerColumn.yysj.desc());
        }else{
            RuntimeCheck.ifTrue(true, "未找到记录");
        }


        List<ClDd> orgs = findByCondition(condition);

        result.setResult(orgs);
        return result;
    }

    @Override
    public ApiResponse<String> evaluate(String orderId, String grade) {
        RuntimeCheck.ifBlank(orderId,"请选择订单");
        RuntimeCheck.ifBlank(grade,"请输入评分");
        ClDd order = findById(orderId);
        RuntimeCheck.ifNull(order,"未找到订单");
        ClDd newOrder = new ClDd();
        newOrder.setId(orderId);
        newOrder.setPjdj(new Short(grade));
        entityMapper.updateByPrimaryKeySelective(newOrder);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<Map<String, Object>> getStartPointAndEndPoint(String orderId) {
        RuntimeCheck.ifBlank(orderId,"请选择订单");
        ClDd order = entityMapper.selectByPrimaryKey(orderId);
        RuntimeCheck.ifNull(order,"订单不存在");
        String zdbh = order.getZdbm();
        RuntimeCheck.ifBlank(zdbh,"未指派车辆");
        SimpleCondition condition = new SimpleCondition(ClGpsLs.class);
        condition.eq(ClGpsLs.InnerColumn.zdbh,zdbh);
        condition.gte(ClGpsLs.InnerColumn.cjsj,order.getYysj());
        condition.setOrderByClause("cjsj asc");
        Map<String,Object> map = new HashMap<>();
        List<ClGpsLs> gpsLs1 = gpsLsMapper.selectByExampleAndRowBounds(condition,new RowBounds(0,1));
        if (gpsLs1.size() != 0){
            map.put("ksjd",gpsLs1.get(0).getBdjd());
            map.put("kswd",gpsLs1.get(0).getBdwd());
        }

        condition = new SimpleCondition(ClGpsLs.class);
        condition.eq(ClGpsLs.InnerColumn.zdbh,zdbh);
        condition.setOrderByClause("cjsj asc");
        condition.gte(ClGpsLs.InnerColumn.cjsj,order.getSjqrsj());
        List<ClGpsLs> gpsLs2 = gpsLsMapper.selectByExampleAndRowBounds(condition,new RowBounds(0,1));
        if (gpsLs2.size() != 0){
            map.put("jsjd",gpsLs2.get(0).getBdjd());
            map.put("jswd",gpsLs2.get(0).getBdwd());
        }

        if (map.size() >= 4){
            double d = DistanceUtil.getLongDistance(gpsLs1.get(0).getBdjd().doubleValue(),gpsLs1.get(0).getBdwd().doubleValue(),gpsLs2.get(0).getBdjd().doubleValue(),gpsLs2.get(0).getBdwd().doubleValue());
            map.put("distance",d);
            map.put("centerJd",(gpsLs1.get(0).getBdjd().add(gpsLs2.get(0).getBdjd())).divide(new BigDecimal(2),10,BigDecimal.ROUND_HALF_UP));
            map.put("centerWd",(gpsLs1.get(0).getBdwd().add(gpsLs2.get(0).getBdwd())).divide(new BigDecimal(2),10,BigDecimal.ROUND_HALF_UP));
        }
        return ApiResponse.success(map);
    }
    /**
     * 获取当前订单的订单  车辆GPS列表
     * @param orderId
     * @return
     */
    @Override
    public ApiResponse<List<Clyy>> getOrderGpsList(String orderId){
        RuntimeCheck.ifBlank(orderId,"请选择订单");
        ClDd clDd = entityMapper.selectByPrimaryKey(orderId);
        RuntimeCheck.ifNull(clDd,"订单不存在");

        RuntimeCheck.ifNull(clDd.getZdbm(),"暂无行程记录");

        RuntimeCheck.ifNull(clDd.getYysj(),"该行程还未开始");
        RuntimeCheck.ifNull(clDd.getSjqrsj(),"该行程还未结束");


        SimpleCondition condition = new SimpleCondition(Clyy.class);

        condition.and().andEqualTo(Clyy.InnerColumn.zdbh.name() , clDd.getZdbm());
        if(clDd.getYysj()!=null){
            String strinDate=DateUtils.getDateStr(clDd.getYysj(),"yyyy-MM-dd");
            condition.and().andGreaterThanOrEqualTo(Clyy.InnerColumn.loc_time.name() , strinDate);
        }
        if(clDd.getSjqrsj()!=null){
            String StrinDate= DateUtils.getDateStr(clDd.getSjqrsj(),"yyyy-MM-dd");
            condition.and().andLessThanOrEqualTo(Clyy.InnerColumn.loc_time.name() ,  StrinDate);
        }
        condition.setOrderByClause(" id ASC");

        List<Clyy> gpsList = clYyService.findByCondition(condition);

        RuntimeCheck.ifFalse(gpsList!=null&&gpsList.size()>0,"暂无行程记录");

        ApiResponse<List<Clyy>> ret=new ApiResponse<>();
        ret.setResult(gpsList);
        return ret;
    }
}
