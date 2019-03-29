package com.ldz.sys.service.impl;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.bean.Menu;
import com.ldz.sys.mapper.*;
import com.ldz.sys.model.*;
import com.ldz.sys.service.FwService;
import com.ldz.sys.service.GnService;
import com.ldz.sys.service.JgService;
import com.ldz.sys.service.JsService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.exception.RuntimeCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GnServiceImpl extends BaseServiceImpl<SysGn, String> implements GnService {

    @Autowired
    private SysYhJsMapper yhJsMapper;
    @Autowired
    private SysJsGnMapper jsGnMapper;
    @Autowired
    private JgService jgService;
    @Autowired
    private JsService jsService;
    @Autowired
    private GnService gnService;
    @Autowired
    private SysPtjgMapper ptjgMapper;
    @Autowired
    private SysJgsqlbMapper jgsqMapper;
    @Autowired
    private SysFwgnMapper gnMapper;
    @Autowired
    private SysJgsqlbMapper jgsqlbMapper;
    @Autowired
    private StringRedisTemplate redisTemplateUtil;
    @Autowired
    private FwService fwService;
    @Override
    protected Mapper<SysGn> getBaseMapper() {
        return gnMapper;
    }

    @Override
    public ApiResponse<String> saveEntity(SysGn entity) {
    	SysGn selectByPrimaryKey = gnMapper.selectByPrimaryKey(entity.getGndm());
    	if(selectByPrimaryKey!=null) {
    		return ApiResponse.fail("功能代码已存在");
    	}
    	String creator = getOperateUser();
    	Date now = new Date();
        entity.setCjr(creator);
        entity.setCjsj(now);
        gnMapper.insert(entity);

        SysJgsq jgsq = new SysJgsq();
        jgsq.setFwdm(entity.getFwdm());
        jgsq.setId(genId());
        jgsq.setJgdm("100");
        jgsq.setGndm(entity.getGndm());
        jgsq.setCjsj(now);
        jgsq.setCjr(getOperateUser());
        jgsqlbMapper.insertSelective(jgsq);
        initPermission();
        return ApiResponse.success();
    }

    @Override
    public boolean fillCondition(LimitedCondition condition){
        condition.setOrderByClause("px asc");
        return true;
    }


    @Override
    public ApiResponse<String> updateEntity(SysGn gn) {
    	gn.setXgr(getOperateUser());
    	gn.setXgsj(new Date());
    	SysGn gndm = gnMapper.selectByPrimaryKey(gn.getGndm());
    	if (StringUtils.isEmpty(gn.getFjd())){
    	    gn.setFjd(null);
        }
    	if (gndm==null) {
    		save(gn);
		}else {
			update(gn);
		}

        // 更新角色权限缓存
        // 获取具有该权限的角色
        List<String> ids = jsGnMapper.findJsIdsByGndm(gn.getGndm());
        cachePermission(ids);
        return ApiResponse.success();
    }


    /**
     * 更新缓存中用户的权限
     * @param ids
     */
    @Override
    public void cachePermission(List<String> ids) {
        SysJsGn jsGn = new SysJsGn();
        ids.forEach(s -> {
            jsGn.setJsdm(s);
            List<SysJsGn> jsGns = jsGnMapper.select(jsGn);
            if(CollectionUtils.isNotEmpty(jsGns)) {
            // 根据功能代码查询所有的api前缀
            List<String> gndms = jsGns.stream().map(SysJsGn::getGndm).collect(Collectors.toList());

                List<SysGn> gns = gnService.findIn(SysGn.InnerColumn.gndm, gndms);
                List<String> apiQzs = gns.stream().map(SysGn::getApiQz).distinct().collect(Collectors.toList());
                StringBuilder sb = new StringBuilder();
                // 拼接api 前缀
                apiQzs.stream().forEach(s1 -> {
                    sb.append(s1).append(",");
                });
                //   存储 角色功能 api
                redisTemplateUtil.boundValueOps("permission_" + s).set(sb.toString());
            }
        });
    }

    @Override
    public List<SysGn> buildFunctionTree(List<SysGn> nodeList) {
        List<SysGn> root = new ArrayList<>();
        Map<String,SysGn> nodeMap = nodeList.stream().collect(Collectors.toMap(SysGn::getGndm, p->p));

        for (SysGn node : nodeList) {
            String fatherCode = node.getFjd();
            if (StringUtils.isEmpty(fatherCode)){
                root.add(node);
                continue;
            }
            SysGn father = nodeMap.get(fatherCode);
            if (father == null)continue;
            if (father.getChildren() == null){
                List<SysGn> children = new ArrayList<>();
                children.add(node);
                father.setChildren(children);
            }else{
                father.getChildren().add(node);
            }
        }
        return root;
    }

    @Override
    public ApiResponse<String> setRoleFunctions(String jsdm, List<String> gndms) {
        RuntimeCheck.ifBlank(jsdm,"角色代码不能为空");
        List<SysJs> roles = jsService.findEq(SysJs.InnerColumn.jsId,jsdm);
        RuntimeCheck.ifTrue(roles.size() == 0,"未找到记录");

        SimpleCondition condition = new SimpleCondition(SysJsGn.class);
        condition.eq(SysJsGn.InnerColumn.jsdm,jsdm);
        jsGnMapper.deleteByExample(condition);

        if (gndms == null || gndms.size() == 0)return ApiResponse.success();
        List<SysGn> functionList = findIn(SysGn.InnerColumn.gndm,gndms);

        String createUser = getOperateUser();
        Date now = new Date();
        List<SysJsGn> jsGns = new ArrayList<>();
        for (SysGn function : functionList) {
            SysJsGn jsGn = new SysJsGn();
            jsGn.setGndm(function.getGndm());
            jsGn.setJsdm(jsdm);
            jsGn.setCjr(createUser);
            jsGn.setCjsj(now);
            jsGn.setFwdm(function.getFwdm());
            jsGn.setFgndm(function.getFjd());
            jsGn.setId(genId());
            jsGns.add(jsGn);
        }
        jsGnMapper.insertList(jsGns);
        cachePermission(Arrays.asList(jsdm));
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<List<SysGn>> getRoleFunctions(String jsdm) {
        SimpleCondition condition = new SimpleCondition(SysJsGn.class);
        condition.eq(SysJsGn.InnerColumn.jsdm,jsdm);
        List<SysJsGn> roleFunctions = jsGnMapper.selectByExample(condition);
        if (roleFunctions.size() == 0){
            List<SysGn> gnList = new ArrayList<>();
            return ApiResponse.success(gnList);
        }
        List<String> gndms = roleFunctions.stream().map(SysJsGn::getGndm).collect(Collectors.toList());
        List<SysGn> functions = gnService.findIn(SysGn.InnerColumn.gndm,gndms);
        return ApiResponse.success(functions);
    }

    private List<Menu> convertToMenus(List<SysGn> functions){
        List<Menu> menuList = new ArrayList<>();
        for (SysGn function : functions) {
            Menu menu = new Menu(function);
            menuList.add(menu);
        }
        return menuList;
    }


    @Override
    public List<Menu> getMenuTree(SysYh user) {
        List<SysGn> functions = getUserFunctions(user);
        if (functions == null || functions.size() == 0)return new ArrayList<>();
        Set<String> functionCodes = functions.stream().map(SysGn::getGndm).collect(Collectors.toSet());
        findParent(functions,functionCodes);
        functions.sort(Comparator.comparing(SysGn::getPx));
        List<Menu> menuList = convertToMenus(functions);
        return buildMenuTree(menuList);
    }

    @Override
    public ApiResponse<String> initMenu(List<Menu> menus) {
        List<SysGn> functions = new ArrayList<>();
        addToMenuList(menus,functions);
        for (SysGn function : functions) {
            SysGn exist = gnMapper.selectByPrimaryKey(function.getGndm());
            if (exist == null){
                gnService.save(function);
            }else{
                gnService.update(function);
            }
        }
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<String> setOrgFunctions(String jgdm, List<String> gndmList) {
        RuntimeCheck.ifBlank(jgdm,"请选择机构");
        if ("100".equals(jgdm))return ApiResponse.success();
        SimpleCondition condition = new SimpleCondition(SysJgsq.class);
        condition.eq(SysJgsq.InnerColumn.jgdm,jgdm);
        jgsqlbMapper.deleteByExample(condition);

        String creator = getOperateUser();
        Date now = new Date();
        for (String s : gndmList) {
            SysJgsq jgsq = new SysJgsq();
            jgsq.setCjr(creator);
            jgsq.setCjsj(now);
            jgsq.setGndm(s);
            jgsq.setJgdm(jgdm);
            jgsq.setId(genId());
            // todo 服务代码
//            jgsq.setFwdm();
            jgsqlbMapper.insertSelective(jgsq);
        }
        return ApiResponse.success();
    }

    /**
     * 加载所有角色权限
     */
    @Override
    public void initPermission() {

        // 获取所有角色的 id ， 查询对应角色的 功能
        List<SysJs> sysJsList = jsService.findAll();
        List<String> jsIds = sysJsList.stream().map(SysJs::getJsId).collect(Collectors.toList());
        cachePermission(jsIds);

    }

    private void addToMenuList(List<Menu> menuList,List<SysGn> functionList){
        List<SysGn> functions = convertToFunctionList(menuList);
        functionList.addAll(functions);
        for (Menu menu : menuList) {
            if (menu.getChildren() != null){
                addToMenuList(menu.getChildren(),functionList);
            }
        }
    }

    private List<SysGn> convertToFunctionList(List<Menu> menus){
        List<SysGn> list = new ArrayList<>();
        String creartor = "初始导入";
        Date now = new Date();
        for (Menu menu : menus) {
            SysGn function = new SysGn();
            function.setFwdm("1");
            function.setZt("00");
            function.setCjr(creartor);
            function.setCjsj(now);
            function.setFjd(menu.getPid());
            function.setGndm(menu.getName());
            function.setUrl(menu.getName());
            function.setGnmc(menu.getTitle());
            function.setTb(menu.getIcon());
            list.add(function);
        }
        return list;
    }
    private List<Menu> buildMenuTree(List<Menu> menuList){
        Map<String,Menu> menuIdMap = menuList.stream().collect(Collectors.toMap(Menu::getId,p->p));
        List<Menu> root = new ArrayList<>();
        for (Menu menu : menuList) {
            if (StringUtils.isEmpty(menu.getPid())){
                root.add(menu);
                continue;
            }
            Menu father = menuIdMap.get(menu.getPid());
            if (father == null)continue;
            if (father.getChildren() == null){
                List<Menu> children = new ArrayList<>();
                children.add(menu);
                father.setChildren(children);
            }else{
                father.getChildren().add(menu);
            }
        }
        return root;
    }


    /**
     * 查找某个服务的所有功能
     *
     * @param serviceCode 服务代码
     * @return 功能列表
     */
    @Override
    public List<SysGn> findByServiceCode(String serviceCode) {
        RuntimeCheck.ifBlank(serviceCode,"请输入服务代码");
        return findEq(SysGn.InnerColumn.fwdm,serviceCode);
    }

    /**
     * 设置机构功能
     *
     * @param orgCode      机构代码
     * @param functionCode 功能代码
     * @return 执行结果
     */
    @Override
    public ApiResponse<String> setOrgFunction(String orgCode, List<String> functionCode) {
        SysYh user = getCurrentUser();
        RuntimeCheck.ifBlank(orgCode,"请选择机构");
        SysJg org = jgService.findByOrgCode(orgCode);
        RuntimeCheck.ifNull(org,"机构不存在");

        if ("100".equals(orgCode))return ApiResponse.success();

        // 删除旧数据
        SimpleCondition condition = new SimpleCondition(SysJg.class);
        condition.eq(SysJg.InnerColumn.jgdm,orgCode);
        jgsqMapper.deleteByExample(condition);

        // 插入新数据
        List<SysGn> functions = findIn(SysGn.InnerColumn.gndm,functionCode);
        Date now = new Date();
        for (SysGn function : functions) {
            SysJgsq jgsq = new SysJgsq();
            jgsq.setCjr(user.getYhid());
            jgsq.setCjsj(now);
            jgsq.setFwdm(function.getFwdm());
            jgsq.setGndm(function.getGndm());
            jgsq.setJgdm(orgCode);
            jgsqMapper.insertSelective(jgsq);
        }
        return ApiResponse.success();
    }

    /**
     * 查询机构拥有的功能列表
     *
     * @param gndm 机构代码
     * @return 功能列表
     */
    @Override
    public List<SysGn> findByGndm(String gndm) {
        return findEq(SysGn.InnerColumn.gndm,gndm);
    }

    @Override
    public List<SysGn> getUserFunctions(SysYh user) {
        if ("su".equals(user.getLx())){
            return findAll();
        }
        List<String> functionCodes = getUserFunctionCodes(user);
        if (!"00".equals(user.getLx())){
            List<String> orgFunctionCodes = getOrgFunctionCodes(user.getJgdm());
            functionCodes.retainAll(orgFunctionCodes);
        }
        if (functionCodes.size() == 0)return new ArrayList<>();
        SimpleCondition condition = new SimpleCondition(SysGn.class);
        condition.in(SysGn.InnerColumn.gndm,functionCodes);
        condition.eq(SysGn.InnerColumn.zt,"00");
        return findByCondition(condition);
    }

    @Override
    public List<SysGn> getOrgFunctions(String orgCode) {
        List<String> functionCodes = getOrgFunctionCodes(orgCode);
        if(CollectionUtils.isNotEmpty(functionCodes)) {
            return gnService.findIn(SysGn.InnerColumn.gndm, functionCodes);
        }else{
            return new ArrayList<>();
        }
    }
    @Override
    public List<String> getOrgFunctionCodes(String orgCode) {
        String hideSystem = getRequestParamterAsString("hideSystem");
        SimpleCondition condition = new SimpleCondition(SysJgsq.class);
        condition.eq(SysJgsq.InnerColumn.jgdm,orgCode);
        List<SysJgsq> jgsqs = jgsqMapper.selectByExample(condition);
        if (jgsqs.size() == 0) {
            return new ArrayList<>();
        }
        List<String> functionCodes;
        if (StringUtils.isNotEmpty(hideSystem)){
            functionCodes = jgsqs.stream().filter(jgsq -> !StringUtils.startsWith(jgsq.getGndm(),"system")).map(SysJgsq::getGndm).collect(Collectors.toList());
        }else{
            functionCodes  = jgsqs.stream().map(SysJgsq::getGndm).collect(Collectors.toList());
        }


        return functionCodes;
    }

    private void initjgsq(){
        List<SysGn> functions = findAll();
        Date now = new Date();
        String creator = getOperateUser();
        for (SysGn function : functions) {
            SysJgsq jgsq = new SysJgsq();
            jgsq.setFwdm(function.getFwdm());
            jgsq.setGndm(function.getGndm());
            jgsq.setId(genId());
            jgsq.setJgdm("100");
            jgsq.setCjsj(now);
            jgsq.setCjr(creator);
            jgsqlbMapper.insertSelective(jgsq);
        }
    }

    @Override
    public List<String> getUserFunctionCodes(SysYh user) {
        // 获取用户角色
        SimpleCondition condition = new SimpleCondition(SysYhJs.class);
        condition.eq(SysYhJs.InnerColumn.yhId,user.getYhid());
        List<SysYhJs> yhJs = yhJsMapper.selectByExample(condition);
        if (yhJs.size() == 0)  return new ArrayList<>();

        // 获取角色功能
        List<String> jsdms = yhJs.stream().map(SysYhJs::getJsId).collect(Collectors.toList());
        return getRolesFunctionCodes(jsdms);
    }

    @Override
    public List<SysGn> getRolesFunctions(List<String> jsdms) {
        if (jsdms == null || jsdms.size() == 0)return new ArrayList<>();
        List<String> functionCodes = getRolesFunctionCodes(jsdms);
        if (functionCodes.size() == 0)return new ArrayList<>();

//        SysYh user = getCurrentUser();
//        List<String> orgFunctionCodes = getOrgFunctionCodes(user.getJgdm());
//        functionCodes.retainAll(orgFunctionCodes);
        return findIn(SysGn.InnerColumn.gndm,functionCodes);
    }

    @Override
    public List<String> getRolesFunctionCodes(List<String> jsdms) {
        if (jsdms == null || jsdms.size() == 0)return new ArrayList<>();
        SysJs role = jsService.findById(jsdms.get(0));
        if (role == null)return new ArrayList<>();
        SimpleCondition condition = new SimpleCondition(SysJsGn.class);
        condition.in(SysJsGn.InnerColumn.jsdm,jsdms);
        List<SysJsGn> roleFunctions = jsGnMapper.selectByExample(condition);
        if (roleFunctions.size() == 0)return new ArrayList<>();
        return roleFunctions.stream().map(SysJsGn::getGndm).collect(Collectors.toList());
    }

    private List<SysFw> getAllPermissionTreeWithChecked(List<SysFw> services,List<SysGn> functions){
        SysYh user = getCurrentUser();
        List<SysGn> allFunctions = getOrgFunctions(user.getJgdm());
        List<SysFw> allServices = fwService.findAll();

        List<String> functionCodes = functions.stream().map(SysGn::getGndm).collect(Collectors.toList());

        Map<String,SysFw> serviceMap = allServices.stream().collect(Collectors.toMap(SysFw::getFwdm,p->p));
        Map<String,SysGn> functionMap = allFunctions.stream().collect(Collectors.toMap(SysGn::getGndm, p->p));

//        List<String> serviceCodes = new ArrayList<>();
//        for (SysGn function : functions) {
//            String serviceCode = function.getFwdm();
//            if (StringUtils.isEmpty(serviceCode))continue;
//            if (serviceMap.containsKey(serviceCode))continue;
//            serviceCodes.add(serviceCode);
//        }
//        if (serviceCodes.size() != 0){
//            List<SysFw> addServices = fwService.findIn(SysFw.InnerColumn.fwdm,serviceCodes);
//            services.addAll(addServices);
//        }
        for (SysGn function : allFunctions) {
            if (functionCodes.contains(function.getGndm())){
                function.setChecked("true");
            }
            String fatherCode = function.getFjd();
            // 如果没有父节点，则代码这是个一级功能
            if (StringUtils.isEmpty(fatherCode)){
                SysFw father = serviceMap.get(function.getFwdm());
                if (father == null)continue;
                if (father.getFunctions() == null){
                    List<SysGn> children = new ArrayList<>();
                    children.add(function);
                    father.setFunctions(children);
                }else{
                    if (father.getFunctions().contains(function))continue;
                    father.getFunctions().add(function);
                }
            }else{
                SysGn father = functionMap.get(fatherCode);
                if (father == null)continue;
                if (father.getChildren() == null){
                    List<SysGn> children = new ArrayList<>();
                    children.add(function);
                    father.setChildren(children);
                }else{
                    if (father.getChildren().contains(function))continue;
                    father.getChildren().add(function);
                }
            }
        }
        return allServices;

    }
    private List<SysFw> getPermissionTree(List<SysFw> services,List<SysGn> functions){
        return getPermissionTree(services,functions,null);
    }
    private List<SysFw> getPermissionTree(List<SysFw> services,List<SysGn> functions,List<String> hasFunctionCodes){
        Set<String> functionCodes = functions.stream().map(SysGn::getGndm).collect(Collectors.toSet());
        findParent(functions,functionCodes);
        Map<String,SysFw> serviceMap = services.stream().collect(Collectors.toMap(SysFw::getFwdm,p->p));
        Map<String,SysGn> functionMap = functions.stream().collect(Collectors.toMap(SysGn::getGndm, p->p));

        List<String> serviceCodes = new ArrayList<>();
        for (SysGn function : functions) {
            String serviceCode = function.getFwdm();
            if (StringUtils.isEmpty(serviceCode))continue;
            if (serviceMap.containsKey(serviceCode))continue;
            serviceCodes.add(serviceCode);
        }
        if (serviceCodes.size() != 0){
            List<SysFw> addServices = fwService.findIn(SysFw.InnerColumn.fwdm,serviceCodes);
            services.addAll(addServices);
            for (SysFw service : addServices) {
                serviceMap.put(service.getFwdm(),service);
            }
        }
        for (SysGn function : functions) {
            String fatherCode = function.getFjd();
            if (hasFunctionCodes != null && hasFunctionCodes.contains(function.getGndm())){
                function.setChecked("checked");
            }
            // 如果没有父节点，则代码这是个一级功能
            if (StringUtils.isEmpty(fatherCode)){
                SysFw father = serviceMap.get(function.getFwdm());
                if (father == null)continue;
                if (father.getFunctions() == null){
                    List<SysGn> children = new ArrayList<>();
                    children.add(function);
                    father.setFunctions(children);
                }else{
                    father.getFunctions().add(function);
                }
            }else{
                SysGn father = functionMap.get(fatherCode);
                if (father == null)continue;
                if (father.getChildren() == null){
                    List<SysGn> children = new ArrayList<>();
                    children.add(function);
                    father.setChildren(children);
                }else{
                    father.getChildren().add(function);
                }
            }
        }
        return services;
    }

    private void findParent(List<SysGn> functions,Set<String> functionCodes){
        Set<String> addCodes = new HashSet<>();
        Set<String> fatherCodes = new HashSet<>();
        for (SysGn function : functions) {
            if (StringUtils.isEmpty(function.getFjd()))continue;
            if (functionCodes.contains(function.getFjd()))continue;
            fatherCodes.add(function.getFjd());
            addCodes.add(function.getFjd());
        }
        if (fatherCodes.size() != 0){
            List<SysGn> fathers = findIn(SysGn.InnerColumn.gndm,fatherCodes);
            for (SysGn father : fathers) {
                if (StringUtils.isEmpty(father.getFjd()))continue;
                if (functionCodes.contains(father.getFjd()))continue;
                addCodes.add(father.getFjd());
            }
        }
        if (addCodes.size() != 0){
            List<SysGn> addFunctions = findIn(SysGn.InnerColumn.gndm,addCodes);
            functions.addAll(addFunctions);
        }
    }

    @Override
    public List<SysFw> getAllPermissionTree() {
        // todo  机构分权
        return getPermissionTree(fwService.findAll(),gnMapper.selectAll());
    }

    @Override
    public List<SysFw> getOrgPermissionTree(String jgdm) {
        List<String> hasFunctionCodes = getOrgFunctionCodes(jgdm);
        SysJg org = jgService.findByOrgCode(jgdm);
        if (org == null)return new ArrayList<>();
        return getPermissionTree(fwService.findAll(),getOrgFunctions(org.getFjgdm()),hasFunctionCodes);
    }

    @Override
    public List<SysFw> getUserPermissionTree(SysYh user) {
        List<SysGn> functions = getUserFunctions(user);
        List<String> serviceCodes = functions.stream().map(SysGn::getFwdm).collect(Collectors.toList());
        List<SysFw> services = fwService.findIn(SysFw.InnerColumn.fwdm,serviceCodes);
        return getPermissionTree(services,functions);
    }

    @Override
    public List<SysFw> getRolePermissionTree(String jsdm) {
        List<SysGn> functions = getRolesFunctions(Collections.singletonList(jsdm));
        List<SysFw> services;
        if (functions.size() != 0){
            Set<String> serviceCodes = functions.stream().map(SysGn::getFwdm).collect(Collectors.toSet());
            services = fwService.findIn(SysFw.InnerColumn.fwdm,serviceCodes);
        }else{
            services = new ArrayList<>();
        }
        return getAllPermissionTreeWithChecked(services,functions);
    }


}
