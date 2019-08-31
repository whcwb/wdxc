package com.ldz.service.biz.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.mapper.ClGpsMapper;
import com.ldz.dao.biz.mapper.ClZdglMapper;
import com.ldz.dao.biz.mapper.ZdYhMapper;
import com.ldz.dao.biz.model.*;
import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.dao.obd.model.GpsObdMessageBean;
import com.ldz.service.biz.interfaces.*;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.JgService;
import com.ldz.util.bean.*;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.util.yingyan.GuiJIApi;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.common.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZdglServiceImpl extends BaseServiceImpl<ClZdgl,String> implements ZdglService {
    @Value("${apiurl}")
    private String apiurl;
    @Value("${staticPath:/}")
    private String staticPath;
    @Value("${obdCheckUrl}")
    private String obdCheckUrl;
    @Autowired
    private ClZdglMapper entityMapper;
    @Autowired
    private ClService clService;
    @Autowired
    private CssdService cssdService;
    @Autowired
    private InstructionService instructionService;
    @Autowired
    private JgService jgService;
    @Autowired
    private ClGpsMapper gpsMapper;
    @Autowired
    private ZdYhMapper zdYhMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private SbyxsjjlService sbyxsjjlService;

    private static final String DEVICEID = "DEVICEID-";
    @Override
    protected Mapper<ClZdgl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClZdgl.class;
    }

    @Override
    public ApiResponse<String> bindAppUser(String userId, String deviceId) {
        RuntimeCheck.ifBlank(deviceId,"请输入终端编号");
        ClZdgl zdgl = entityMapper.selectByPrimaryKey(deviceId);
        if(zdgl == null && StringUtils.length(deviceId) >= 12 ){
            deviceId = deviceId.substring(deviceId.length() - 12);
            zdgl = entityMapper.selectByPrimaryKey(deviceId);

        }
        RuntimeCheck.ifNull(zdgl, "终端不存在");
        SimpleCondition condition = new SimpleCondition(ClZdYh.class);
        condition.eq(ClZdYh.InnerColumn.deviceId,deviceId);
        condition.eq(ClZdYh.InnerColumn.userId,userId);
        List<ClZdYh> zdYhList = zdYhMapper.selectByExample(condition);
        if (zdYhList.size() == 0){
            ClZdYh zdYh = new ClZdYh();
            zdYh.setId(genId());
            zdYh.setDeviceId(deviceId);
            zdYh.setUserId(userId);
            zdYhMapper.insert(zdYh);
        }

        ClGps gps = gpsMapper.selectByPrimaryKey(deviceId);
        if (gps == null){
            gps = new ClGps();
            gps.setZdbh(deviceId);
            gps.setCjsj(new Date());
            gps.setBdjd(new BigDecimal(-1));
            gps.setBdwd(new BigDecimal(-1));
            gps.setGdjd(new BigDecimal(-1));
            gps.setGdwd(new BigDecimal(-1));
            gps.setGgjd(new BigDecimal(-1));
            gps.setGgwd(new BigDecimal(-1));
            gps.setFxj(new BigDecimal(-1));
            gps.setYxsd("0");
            gpsMapper.insertSelective(gps);
        }

        List<ClCl> cars = clService.findEq(ClCl.InnerColumn.zdbh,deviceId);
        ApiResponse<String> res = ApiResponse.success();
        if (cars.size() == 0){
            res.setResult("notBind");
        }else{
            res.setResult("bind");
        }
        return res;
    }

    @Override
    public ApiResponse<String> unbindAppUser(String userId, String deviceId) {
        RuntimeCheck.ifBlank(userId,"请输入用户id");
        RuntimeCheck.ifBlank(deviceId,"请输入终端编号");
        SimpleCondition condition = new SimpleCondition(ClZdYh.class);
        condition.eq(ClZdYh.InnerColumn.userId,userId);
        condition.eq(ClZdYh.InnerColumn.deviceId,deviceId);
        List<ClZdYh> zdYhs = zdYhMapper.selectByExample(condition);
        if (zdYhs.size() != 0){
            zdYhMapper.deleteByPrimaryKey(zdYhs.get(0).getId());
            return ApiResponse.success();
        }else{
            return ApiResponse.fail("未找到记录");
        }
    }

    @Override
    public ApiResponse<String> saveEntity(ClZdgl entity) {
        SysYh user = getCurrentUser();
        RuntimeCheck.ifBlank(entity.getZdLx(), "终端类型不能为空");
    	ClZdgl findById = findById(entity.getZdbh());
    	if (findById!=null) {
			return ApiResponse.fail("终端编号已存在");
		}

		RuntimeCheck.ifBlank(entity.getFwnx(),"终端的服务年限不能为空");

        SimpleCondition condition = new SimpleCondition(ClZdgl.class);
        condition.eq(ClZdgl.InnerColumn.jgdm,user.getJgdm());
        Integer count = countByCondition(condition);
        SysJg jg = jgService.findByOrgCode(user.getJgdm());
        RuntimeCheck.ifTrue( jg.getZdsl() <= count , "当前机构的终端数量已满,请联系管理员");
        //默认设置终端在线状态为离线
    	entity.setZxzt("20");

    	//默认设备碰撞灵敏度
    	entity.setPzlmd("10");
    	//默认设备急加速灵敏度
    	entity.setJslmd("2");
        entity.setZt("00");
    	//默认设备视屏上传模式
    	entity.setSpscms("20");
    	//默认设备的心跳
    	entity.setGpsxt("10");
    	// 默认设置终端为未激活状态
        entity.setJhzt("10");
    	if(StringUtils.isBlank(entity.getCmd())){
            entity.setCmd(apiurl);
        }
    	entity.setJgdm(user.getJgdm());
    	entity.setJgmc(jg.getJgmc());
        entity.setCjr(getOperateUser());
        entity.setCjsj(new Date());
        // 上传至百度鹰眼
        YyEntity yyEntity = new YyEntity();
        yyEntity.setAk(GuiJIApi.AK);
        yyEntity.setEntity_name(entity.getZdbh());
        yyEntity.setService_id(GuiJIApi.SERVICE_ID);
        YingyanResponse yingyanResponse = GuiJIApi.changeEntity(yyEntity, GuiJIApi.saveEntityuRL);
        if (StringUtils.equals(yingyanResponse.getStatus(), "0")) {
            entity.setSfyy("已上传鹰眼服务器");
        }
        save(entity);
        redisTemplateUtil.boundValueOps(DEVICEID+entity.getZdbh()).set("");
        GpsInfo gpsInfo = new GpsInfo();
        gpsInfo.setCmd(entity.getCmd());
        gpsInfo.setCmdType("91");
        gpsInfo.setDeviceId(entity.getZdbh());
        instructionService.sendinstruction(gpsInfo);

        return ApiResponse.saveSuccess();
    }

	@Override
	public void insetAndUpdate(ClZdgl entity) {
	 boolean flag= ifExists("zdbh",entity.getZdbh());
		if (flag==true) {
			update(entity);
		}else {
            redisTemplateUtil.boundValueOps(DEVICEID+entity.getZdbh()).set("");
			save(entity);
		}
	}

    @Override
    public ApiResponse<String> updateEntity(ClZdgl entity) {
//    	ClZdgl findById = findById(entity.getZdbh());
//    	if (findById!=null) {
//			return ApiResponse.fail("终端编号已存在");
//		}
//        entity.setXgr(getOperateUser());
        entity.setXgsj(new Date());
        update(entity);
        return ApiResponse.success();
    }

    public ApiResponse<List<ClZdgl>> unboundList(){
        SysYh currentUser = getCurrentUser();
        String jgdm = currentUser.getJgdm();
        String zdbh = getRequestParamterAsString("zdbh");
//        1、定义初始变量
        ApiResponse<List<ClZdgl>> result = new ApiResponse<List<ClZdgl>>();
        List<ClZdgl> list=entityMapper.getUnboundList(jgdm,zdbh);
        result.setResult(list);
        return result;
    }


    @Override
    public boolean fillPagerCondition(LimitedCondition condition){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request.getAttribute("appUserId") != null){
            String userId = (String)request.getAttribute("appUserId");
            SimpleCondition condition1 = new SimpleCondition(ClZdYh.class);
            condition1.eq(ClZdYh.InnerColumn.userId,userId);
            List<ClZdYh> zdYhs = zdYhMapper.selectByExample(condition1);
            if (zdYhs.size() == 0){
                return false;
            }
            List<String> zdbhs = zdYhs.stream().map(ClZdYh::getDeviceId).collect(Collectors.toList());
            condition.in(ClZdgl.InnerColumn.zdbh,zdbhs);
        }
        String cphLike = getRequestParamterAsString("cphLike");
        if (StringUtils.isNotEmpty(cphLike)){
            List<ClCl> carList = clService.findLike(ClCl.InnerColumn.cph,cphLike);
            if (CollectionUtils.isEmpty(carList)){
                return false;
            }
            List<String> zdbhs = carList.stream().map(ClCl::getZdbh).collect(Collectors.toList());
            condition.in(ClZdgl.InnerColumn.zdbh,zdbhs);
        }
        return true;
    }
    /**
     * 自定义分页的对象
     * @param resultPage
     */
    protected void afterPager(PageInfo<ClZdgl> resultPage){
        if(resultPage!=null){
            List<ClZdgl> list=resultPage.getList();
            if(list!=null&&list.size()>0){
                List<String> listIds = list.stream().map(ClZdgl::getZdbh).collect(Collectors.toList());
                List<ClCl> clList=clService.findIn(ClCl.InnerColumn.zdbh,listIds);
                Map<String,ClZdgl> zdbhMap = list.stream().collect(Collectors.toMap(ClZdgl::getZdbh, p->p));

                if(clList!=null&&clList.size()>0){
                    for(ClCl cl:clList){
                        ClZdgl zdbh=zdbhMap.get(cl.getZdbh());
                        if (zdbh == null){
                            continue;
                        }

                        zdbh.setCl(cl);
                        zdbh.setCph(cl.getCph());
                        // 获取速度上限
                        List<ClCssd> eq = cssdService.findEq(ClCssd.InnerColumn.cph.name(), cl.getCph());
                        if(!CollectionUtils.isEmpty(eq)){
                            zdbh.setCssd(eq.get(0).getSdsx());
                        }

                    }
                }
            }
        }
    }

	@Override
	public ApiResponse<Map<String, Integer>> getzdxc() {
        SysYh user = getCurrentUser();
        String userJgdm=user.getJgdm();
		int dianhuo =0;
		int xihuo =0;
		int lixian=0;
		List<ClZdgl> selectAll = entityMapper.selectAll();
	       RuntimeCheck.ifEmpty(selectAll, "暂无设备");
		for (ClZdgl clZdgl : selectAll) {
		    if(StringUtils.isNotEmpty(userJgdm)){
                if(StringUtils.indexOf(userJgdm,clZdgl.getJgdm())>-1){
                    //点火
                    if (StringUtils.equals(clZdgl.getZxzt(), "00")) {
                        dianhuo++;
                    }
                    //熄火
                    if (StringUtils.equals(clZdgl.getZxzt(), "10")) {
                        xihuo++;
                    }
                    //离线
                    if (StringUtils.equals(clZdgl.getZxzt(), "20")) {
                        lixian++;
                    }
                }
            }

		}
		Map<String,Integer> map = new HashMap<>();
		map.put("设备总数", selectAll.size());
		map.put("设备在线数量", dianhuo);
		map.put("设备熄火数量", xihuo);
		map.put("设备离线数量", lixian);
		ApiResponse<Map<String, Integer>>  apiResponse = new ApiResponse<>();
		apiResponse.setResult(map);

		return apiResponse;
	}

    @Override
    public ApiResponse<String> getVersionInfo(String deviceId) {
        String val = (String) redisTemplateUtil.boundValueOps("versionInfo-"+deviceId).get();
        if (StringUtils.isEmpty(val)){
            return ApiResponse.fail("暂无版本信息");
        }
        return ApiResponse.success(val);
    }

    private List<String> checlDeviceCode(Sheet sheet){
        List<String> errors = new ArrayList<>();
        int rows = sheet.getPhysicalNumberOfRows();// 获取表格的
        List<String> deviceCodes = new ArrayList<>(rows);
        for (int r = 0; r < rows; r++) {
            if(r==0){
                continue;
            }
            Row row = sheet.getRow(r); // 获取单元格中指定的行对象
            Cell cell = row.getCell(0);
            String deviceCode = cell.toString();
            if (!deviceCodes.contains(deviceCode)){
                deviceCodes.add(deviceCode);
            }else{
                errors.add("第"+r+"行，终端编号重复");
            }
        }
        return errors;
    }

    /**
     * 解析excel 批量导入终端
     * @param
     * @return
     */
    @Override
    public ApiResponse<List<String>> saveBatch(String filePath) throws IOException {
        // 获取当前用户的机构代码
        SysYh user = getCurrentUser();
        String jgdm = user.getJgdm();

        ApiResponse<List<String>> result = new ApiResponse<>();
        List<ClZdgl> zdglList = new ArrayList<>();
        List<ClCssd> cssdList = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        filePath = staticPath + filePath;
//        filePath = "/Users/chenwei/Downloads/批量新增终端模板-2.xls";
        Workbook workbook;
        try {
            if(filePath.indexOf(".xlsx")>-1){
                workbook = new XSSFWorkbook(new FileInputStream(filePath));
            } else if (filePath.indexOf(".xls")>-1){
                workbook = new HSSFWorkbook(new FileInputStream(filePath));
            }else {
                result.setMessage("请上传excel文件");
                return result;
            }
            //HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileToBeRead)); //2003 创建对Excel工作簿文件的引用
            //XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(fileToBeRead)); //2007,2010 创建对Excel工作簿文件的引用
            Sheet sheet = workbook.getSheetAt(0); // 创建对工作表的引用
            List<String> es = checlDeviceCode(sheet);
            if (es.size() != 0){
                result.setCode(100);
                result.setResult(es);
                return result;
            }
            int rows = sheet.getPhysicalNumberOfRows();// 获取表格的
            int columns = 0;
            for (int r = 0; r < rows; r++) { // 循环遍历表格的行

                if(r==0){
                    //在第一行标题行计算出列宽度,因为数据行中可能会有空值
                    columns = sheet.getRow(r).getLastCellNum();
                    continue;
                }

                Row row = sheet.getRow(r); // 获取单元格中指定的行对象
                if (row != null) {

                    // 终端管理
                    ClZdgl clZdgl = new ClZdgl();
                    // 设定默认值
                    clZdgl.setZxzt("20"); // 默认离线
                    //默认设备急加速灵敏度
                    clZdgl.setJslmd("2");
                    //默认设备的心跳
                    clZdgl.setGpsxt("10");
                    clZdgl.setCjr(getOperateUser());
                    clZdgl.setCjsj(new Date());
                    clZdgl.setJgdm(jgdm);
                    SysJg jg = jgService.findById(jgdm);
                    clZdgl.setJgmc(jg.getJgmc());
                    // 超速设定
                    ClCssd clCssd = new ClCssd();
                    clCssd.setCjr(getOperateUser());
                    clCssd.setCjsj(new Date());
                    clCssd.setId(genId());
                    //int cells = row.getPhysicalNumberOfCells();// 获取一行中的单元格数
                    //int cells = row.getLastCellNum();// 获取一行中最后单元格的编号（从1开始）
                    for (short c = 0; c < columns; c++) { // 循环遍历行中的单元格
                        Cell cell = row.getCell(c);
                        String v = cell == null ? "" : cell.toString();
                        switch(c){
                            case 0: // 终端号
                                if(StringUtils.isEmpty(v)){
                                    errors.add("第" + (r+1) + "行 , " + "第" + (c+1) + "列的设备终端号不能为空" );
                                }
                                ClZdgl zdgl = findById(v);
                                if(zdgl != null){
                                    errors.add("第" + (r+1) + "行 , " + "第" + (c+1) + "列的设备终端号已经存在" );
                                }
                                clZdgl.setZdbh(v);
                                break;
                            case 1: // 名称
                                if(StringUtils.isEmpty(v)){
                                    errors.add("第" + (r+1) + "行 , " + "第" + (c+1) + "列的设备名称不能为空" );
                                }
                                clZdgl.setMc(v);
                                break;
                            case 2: // 型号
//                                if(StringUtils.isEmpty(v)){
//                                    errors.add("第" + (r+1) + "行 , " + "第" + (c+1) + "列的设备型号不能为空" );
//                                }
                                clZdgl.setXh(v);
                                break;
                            case 3: // 碰撞灵敏度
                                if(StringUtils.isEmpty(v)){
                                    clZdgl.setPzlmd("10");
                                }else{
                                    clZdgl.setPzlmd(v);
                                }
                                break;
                            case 4: //上传视频模式
                                if(StringUtils.isEmpty(v)){
                                    clZdgl.setSpscms("20");
                                }else{
                                    clZdgl.setSpscms(v);
                                }
                                break;
                            case 5: // 超速设定
                                if(StringUtils.isEmpty(v)){
                                    clCssd.setSdsx((short)120);
                                }else{
                                    if(Integer.parseInt(v)>128 || Integer.parseInt(v)<-127){
                                        errors.add("第" + (r+1) + "行 , " + "第" + (c+1) + "列的超速限定异常" );
                                    }
                                    clCssd.setSdsx(Short.parseShort(v));
                                }
                                break;
                            case 6: // 接口地址
                                if(StringUtils.isEmpty(v) || cell == null){
                                    clZdgl.setCmd(apiurl);
                                }else{
                                    clZdgl.setCmd(v);
                                }
                                break;
                        }
                    }
                    // 遍历完每一行为一个终端对象 , 保存终端
                    clZdgl.setZt("00");
                    zdglList.add(clZdgl);
                    cssdList.add(clCssd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(CollectionUtils.isEmpty(errors)){
            saveBatch(zdglList);
            cssdService.saveBatch(cssdList);
            return result;
        }else{
            result.setCode(100);
            result.setResult(errors);
            return result;
        }
    }

    @Override
    public void saveBatch(List<ClZdgl> clZdgls) {
        // 批量上传终端前首先上传终端至鹰眼
        for (ClZdgl clZdgl : clZdgls) {
            // 上传至百度鹰眼
            YyEntity yyEntity = new YyEntity();
            yyEntity.setAk(GuiJIApi.AK);
            yyEntity.setEntity_name(clZdgl.getZdbh());
            yyEntity.setService_id(GuiJIApi.SERVICE_ID);
            YingyanResponse yingyanResponse = GuiJIApi.changeEntity(yyEntity, GuiJIApi.saveEntityuRL);
            if (StringUtils.equals(yingyanResponse.getStatus(), "0")) {
                clZdgl.setSfyy("已上传鹰眼服务器");
            }
            redisTemplateUtil.boundValueOps(DEVICEID+clZdgl.getZdbh()).set("");
        }


        entityMapper.insertList(clZdgls);
    }

    @Override
    public ApiResponse<String> saveList(String zdbhs, ClZdgl entity) {
//        SysYh user = getCurrentUser();
        RuntimeCheck.ifBlank(entity.getZdLx(), "终端类型不能为空");
        RuntimeCheck.ifBlank(entity.getFwnx(),"终端的服务年限不能为空");
        SimpleCondition condition = new SimpleCondition(ClZdgl.class);
        condition.eq(ClZdgl.InnerColumn.jgdm,entity.getJgdm());
        Integer count = countByCondition(condition);
        SysJg jg = jgService.findByOrgCode(entity.getJgdm());
        //默认设置终端在线状态为离线
        entity.setZxzt("20");
        //默认设备碰撞灵敏度
        entity.setPzlmd("10");
        //默认设备急加速灵敏度
        entity.setJslmd("2");
        //默认设备视屏上传模式
        entity.setSpscms("20");
        //默认设备的心跳
        entity.setGpsxt("10");
        // 默认设置激活状态为 未激活
        entity.setJhzt("10");
        if(StringUtils.isBlank(entity.getCmd())){
            entity.setCmd(apiurl);
        }
        entity.setJgdm(entity.getJgdm());
        entity.setJgmc(jg.getJgmc());
        entity.setCjr(getOperateUser());
        entity.setCjsj(new Date());
        String[] split = zdbhs.split(",");
        if(split !=null){
            RuntimeCheck.ifTrue(split.length + count > (jg.getZdsl()==null?0:jg.getZdsl()) ,"当前新增终端数量大于当前机构的剩余数量" );
            for (String zdbh : split) {
                ClZdgl zdgl = new ClZdgl();
                BeanUtils.copyProperties(entity,zdgl,"zdbh");
                zdgl.setZdbh(zdbh);
                ClZdgl findById = findById(zdgl.getZdbh());
                if (findById!=null) {

                    return ApiResponse.fail("终端编号:" + zdgl.getZdbh() + "已存在, 如要续费,请前往续费界面 ");

                }
                // 上传至百度鹰眼
                YyEntity yyEntity = new YyEntity();
                yyEntity.setAk(GuiJIApi.AK);
                yyEntity.setEntity_name(entity.getZdbh());
                yyEntity.setService_id(GuiJIApi.SERVICE_ID);
                YingyanResponse yingyanResponse = GuiJIApi.changeEntity(yyEntity, GuiJIApi.saveEntityuRL);
                if (StringUtils.equals(yingyanResponse.getStatus(), "0")) {
                    zdgl.setSfyy("已上传鹰眼服务器");
                }
                zdgl.setZt("00");
                save(zdgl);

                GpsInfo gpsInfo = new GpsInfo();
                gpsInfo.setCmd(zdgl.getCmd());
                gpsInfo.setCmdType("91");
                gpsInfo.setDeviceId(zdgl.getZdbh());
                instructionService.sendinstruction(gpsInfo);
            }

        }else {
            return ApiResponse.fail("请输入终端编号");
        }


        return ApiResponse.success();
    }

    @Override
    public ApiResponse<String> getAllZd(Page<ClZdgl> page) {
        ApiResponse<String> result = new ApiResponse<>();

        String jg = getRequestParamterAsString("jg");
        String lx = getRequestParamterAsString("lx");
        RuntimeCheck.ifBlank(jg, "请选择机构");
        LimitedCondition queryCondition = getQueryCondition();
        if(StringUtils.isBlank(lx) || StringUtils.equals(lx,"1")) {
            queryCondition.eq(ClZdgl.InnerColumn.jgdm,jg);
        }else if(StringUtils.equals(lx, "2")){
            queryCondition.and().andCondition( " jgdm != " + jg);
            queryCondition.startWith(ClZdgl.InnerColumn.jgdm,jg);
        }
        queryCondition.setOrderByClause( " cjsj desc" );
        PageInfo<ClZdgl> pageInfo = findPage(page, queryCondition);
        result.setPage(pageInfo);
        return result;
    }

    @Override
    public ApiResponse<String> check(String zdbh) {
        Map<String,String> params = new HashMap<>();
        params.put("deviceId",zdbh);
        String response = HttpUtil.post(obdCheckUrl,params, "UTF-8");
        ApiResponse<String> apiResponse = JsonUtil.toBean(response,ApiResponse.class);
        return apiResponse;
    }

    @Override
    public ApiResponse<String> updateDelay(String zdbh, String fwnx) throws ParseException {
        RuntimeCheck.ifBlank(zdbh, "终端编号不能为空");
        RuntimeCheck.ifBlank(fwnx, "延期年限不能为空");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ClZdgl zdgl = findById(zdbh);
        RuntimeCheck.ifTrue(zdgl.getFwnx().equals("#"), "当前终端的服务年限已经是永久, 不用延期");
        if(StringUtils.equals(zdgl.getJhzt(),"10")) {
            if (StringUtils.equals(fwnx, "#")) {
                // 如果设备未激活 , 只修改年限即可
                zdgl.setFwnx(fwnx);
            }else{
                // 设备未激活 , 增加服务年限
                zdgl.setFwnx(Integer.parseInt(zdgl.getFwnx()) + Integer.parseInt(fwnx) + "");
            }
        }else if(StringUtils.equals(zdgl.getJhzt(),"20")){
            if(StringUtils.equals(fwnx,"#")){
                zdgl.setFwnx(fwnx);
                zdgl.setZdJssj(fwnx);
            }else{
                // 已经激活 , 在当前到期时间往后加
                String zdJssj = zdgl.getZdJssj();
                Date date = DateUtils.getDate(zdJssj, "yyyy-MM-dd");
                LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                String plusYear = DateUtils.plusYear(dateTime, Integer.parseInt(fwnx));
                zdgl.setZdJssj(plusYear);
                zdgl.setFwnx(Integer.parseInt(zdgl.getFwnx()) + Integer.parseInt(fwnx) + "");
            }
            // 更改redis中的终端到期时间
            redisTemplateUtil.boundValueOps("expire_"+zdgl.getZdbh()).set(zdgl.getZdJssj());

        }else if(StringUtils.equals(zdgl.getJhzt(),"30")){
            if(StringUtils.equals(fwnx,"#")){
                zdgl.setFwnx(fwnx);
                zdgl.setZdJssj(fwnx);
            }else {
                // 已到期 , 从今天开始往后加
                String today = DateUtils.getToday("yyyy-MM-dd");

                Date date = DateUtils.getDate(today, "yyyy-MM-dd");
                LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

                String plusYear = DateUtils.plusYear(dateTime, Integer.parseInt(fwnx));
                zdgl.setZdJssj(plusYear);
                zdgl.setFwnx(Integer.parseInt(zdgl.getFwnx()) + Integer.parseInt(fwnx) + "");
            }
            zdgl.setJhzt("20");
            redisTemplateUtil.boundValueOps("expire_"+zdgl.getZdbh()).set(zdgl.getZdJssj());
        }
        update(zdgl);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<String> updateDelayList(String zdbhs, String fwnx) throws ParseException {
        RuntimeCheck.ifBlank(zdbhs,"请选择终端");
        RuntimeCheck.ifBlank(fwnx, "服务年限不能为空");

        List<String> list = Arrays.asList(zdbhs.split(","));
        for (String s : list) {
            updateDelay(s, fwnx);
        }

        return ApiResponse.success();
    }

    @Override
    public ApiResponse<GpsObdMessageBean> getObdInfo() {
        String zdbh = getRequestParamterAsString("zdbh");
        RuntimeCheck.ifBlank(zdbh,"请传入终端编号");
        String obdInfo = (String) redisTemplateUtil.boundValueOps("obdInfo_" + zdbh).get();
        GpsObdMessageBean gpsObdMessageBean = new GpsObdMessageBean();
        gpsObdMessageBean.setDeviceId("终端编号");
        gpsObdMessageBean.setDataType("数据类型");
        gpsObdMessageBean.setSchedulingCode("行程id");
        gpsObdMessageBean.setCreatorDate("创建日期");
        gpsObdMessageBean.setCreatortime("创建时间");
        gpsObdMessageBean.setLatitude("纬度");
        gpsObdMessageBean.setLongitude("经度");
        gpsObdMessageBean.setIndication("位指示 Gps是否定位");
        gpsObdMessageBean.setGpsTempo("速度  km/h");
        gpsObdMessageBean.setDirection("方向");
        gpsObdMessageBean.setGpsCount("Gps 卫星个数");
        gpsObdMessageBean.setGpsSignalIntensity("GSM 信号质量");
        gpsObdMessageBean.setMileage("里程 长度  KM");
        gpsObdMessageBean.setLoadValue("负荷计算值。 长度：1  先进行10进制转换，然后使用公式：BYTE*100/255（165*100/255）=64（取整）");
        gpsObdMessageBean.setCoolantTemperature("冷却液温度  长度：1 BYTE – 40，单位℃");
        gpsObdMessageBean.setEngineSpeed("发动机转速");
        gpsObdMessageBean.setObdSpeed("OBD 车速");
        gpsObdMessageBean.setIgnitionAngle("点火提前角");
        gpsObdMessageBean.setIntakePressure("进气歧管绝对压力 1 BYTE，单位 kpa");
        gpsObdMessageBean.setControlVoltage("控制模块电压 1 BYTE/10，单位 V");
        gpsObdMessageBean.setIntakeTemperature("进气温度 1 BYTE-40，单位℃");
        gpsObdMessageBean.setIntakeFlux("空气流量 2 ((BYTE1*256)+BYTE2)/100，单位 g/s");
        gpsObdMessageBean.setJqmxdwz("节气门相对位置 1 BYTE*100/255，单位%");
        gpsObdMessageBean.setChryxz("长期燃油修正 1 (BYTE1-128)*100/128，单位%");
        gpsObdMessageBean.setKlbxs("空燃比系数 2 ((BYTE1*256)+BYTE2)*0.0000305");
        gpsObdMessageBean.setJqmxjwz("节气门绝对位置 1 BYTE*100/255，单位%");
        gpsObdMessageBean.setRyyl("燃油压力 1 BYTE*3,单位 kpa");
        gpsObdMessageBean.setSjyh("瞬间油耗 L/H 2 ((BYTE1*256)+BYTE2)*0.1,单位 L/H");
        gpsObdMessageBean.setSyyl("剩余油量 2 if((BYTE1&0X80)==0X80)\n" +
                "     ((BYTE1-0X80)*256)+BYTE2)*0.1 单位%\n" +
                "     Else (BYTE1*256+BYTE2)*0.1 单位 L");
        gpsObdMessageBean.setJz("基站 8 4 字节运营商代码(IMSI 前 5 位),2 字节 LAC,2 字节 CELL");
        gpsObdMessageBean.setHyl("耗油量 4 OBD 累计耗油量,单位 L");
        gpsObdMessageBean.setSeq("流水号 1 从 0 -255 一直循环累加，可以用来检测是否有包丢失");

        if(StringUtils.isNotBlank(obdInfo)) {
             gpsObdMessageBean = JsonUtil.toBean(obdInfo, GpsObdMessageBean.class);
        }
        return ApiResponse.success(gpsObdMessageBean);
    }

    @Override
    public ApiResponse<GpsBean> getDwqInfo() {
        String zdbh = getRequestParamterAsString("zdbh");
        RuntimeCheck.ifBlank(zdbh,"请传入终端编号");
        String dwqInfo = (String) redisTemplateUtil.boundValueOps("dwqInfo_" + zdbh).get();
        if(StringUtils.isNotBlank(dwqInfo)){
            GpsBean gpsBean = JsonUtil.toBean(dwqInfo, GpsBean.class);
            return ApiResponse.success(gpsBean);
        }
        GpsBean gpsBean = new GpsBean();
        gpsBean.setDeviceId("终端编号");
        gpsBean.setTime("gps时间");
        gpsBean.setXxlx("//消息类型\n" +
                "\t/**\n" +
                "\t0 正常定位\n" +
                "\t1 休眠通知\n" +
                "\t2 SOS 告警\n" +
                "\t3 震动告警\n" +
                "\t4 摔倒告警\n" +
                "\t5 拆卸告警\n" +
                "\t6 拔出告警\n" +
                "\t7 急加速\n" +
                "\t8 急减速\n" +
                "\t9 急转弯\n" +
                "\t10 急变道\n" +
                "\t11 熄火\n" +
                "\t12 点火\n" +
                "\t */");
        gpsBean.setLcs("里程数");
        gpsBean.setJd("经度.精确到小数点后 6 位，负号表示西半球");
        gpsBean.setWd("纬度.精确到小数点后 6 位，负号表示南半球");
        gpsBean.setSd("速度.单位：公里/小时");
        gpsBean.setFx("方向.取值范围：0-359.9");
        gpsBean.setHb("海拔高度");
        gpsBean.setWifi("WiFi定位信息.SSID#MAC#RSSI|SSID#MAC#RSSI|…");
        gpsBean.setLbs("LBS定位信息.LAC#CI#Dbm|LAC#CI#Dbm|…。");


        return ApiResponse.success(gpsBean);
    }

    @Override
    public ApiResponse<ClZdgl> getZdInfo(String zdbh) {
        RuntimeCheck.ifBlank(zdbh, "请输入终端编号");
        List<ClZdgl> zdgls = findEq(ClZdgl.InnerColumn.zdbh, zdbh);
        RuntimeCheck.ifEmpty(zdgls, "未找到终端");
        ClZdgl clZdgl = zdgls.get(0);
        List<ClCl> cls = clService.findEq(ClCl.InnerColumn.zdbh, clZdgl.getZdbh());
        if(CollectionUtils.isNotEmpty(cls)){
            clZdgl.setCl(cls.get(0));
        }
        return ApiResponse.success(clZdgl);

    }

    @Override
    public ApiResponse<String> redisCache() {
        List<ClZdgl> deviceList = findAll();
        for (ClZdgl zdgl : deviceList) {
            redisTemplateUtil.boundValueOps(DEVICEID+zdgl.getZdbh()).set("");
        }
        return ApiResponse.success();
    }




}
