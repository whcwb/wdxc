package com.ldz.sys.base;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.util.ContextUtil;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.ExcelParams;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用Service，业务Service直接继承，只需重新getBaseMapper()方法，将对象所对应的Mapper对象返回即可
 *
 * @param <T>
 * @param <PK>
 * @author Lee
 * @date 2017年7月26日
 */
@Slf4j
public abstract class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

    @Autowired
    public SnowflakeIdWorker idGenerator;
    //排序关键字
    public final static String ORDERBYNAME = "orderBy";

    protected Class <T> entityClass;

    /**
     * 生成id方法
     * @return
     */
    public String genId(){
        long id = idGenerator.nextId();
        return String.valueOf(id);
    }

    protected Class<?> getEntityCls() {
        if (entityClass == null){
            entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return entityClass;
    }

    public List<T> query(T t){
        LimitedCondition condition = getQueryCondition();
        if (!fillQueryCondition(condition)){
            return new ArrayList<>();
        }
        List<T> list = findByCondition(condition);
        afterQuery(list);
        return list;
    }
    public ApiResponse<List<T>> pager(Page<T> pager){
        ApiResponse<List<T>> result = new ApiResponse<>();
        LimitedCondition condition = getQueryCondition();
        if (!fillPagerCondition(condition)){
            return new ApiResponse<List<T>>().emptyPage();
        }

        PageInfo<T> resultPage = findPage(pager, condition);
        afterPager(resultPage);
        result.setPage(resultPage);
        return result;
    }

    protected void afterPager(PageInfo<T> resultPage){
        return;
    }
    protected void afterQuery(List<T> list){
        return;
    }

    /**
     * 查询条件补充
     * @param condition
     * @return
     */
    public boolean fillCondition(LimitedCondition condition){
        return fillPagerCondition(condition);
    }
    public boolean fillPagerCondition(LimitedCondition condition){
        return true;
    }
    public boolean fillQueryCondition(LimitedCondition condition){
        return true;
    }

    protected abstract Mapper<T> getBaseMapper();

    public Object getRequestParamter(String key){
        HttpServletRequest request = getRequset();
        return request.getParameter(key);
    }
    public String getRequestParamterAsString(String key){
        HttpServletRequest request = getRequset();
        return request.getParameter(key);
    }

    public HttpServletRequest getRequset(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    /**
     * 根据前台传值封装条件查询对象Example，属性名必须要和查询bean对象中的属性名要一致。
     * 也可实现前台自定义指定排序方式，排序关键字固定为：orderBy，排序可以多值组合
     * orderBy格式：
     * 单个排序格式：field desc
     * 多个值排序：field1 desc,field2 asc
     * 实现查询对象非侵入式搜索条件扩展，不改动原有bean结构，扩展多种方式搜索条件
     * 如果要精确查询，则字段名后面不需要追加搜索类型关键字；
     * 非精确查询，在字段名后面追加搜索类型关键字。
     * 前台数据格式：字段名+搜索类型
     * 搜索类型目前支持，注意大小写：
     * In:多值匹配。前台传值使用逗号分隔
     * Like:模糊匹配
     * Gte:大于等于
     * Lte:小于等于
     * InRange:范围搜索（大于等于和小于等于搜索）。前台传值格式为：大于等于值,小于等于值。中间使用逗号分隔，实现范围搜索
     * IsNull:等于NULL
     * IsNotNull:不等于NULL
     * @return
     */
    public LimitedCondition getQueryCondition() {
        LimitedCondition condition = new LimitedCondition(getEntityCls());
        //从请求中获取到所有参数名称，对参数名称进行解析，将符合搜索条件的参数取出来，参与到查询条件中
        HttpServletRequest request = getRequset();
        Enumeration<String> params = request.getParameterNames();
        Pattern pattern = Pattern.compile("(In$)|(Like$)|(Endwith$)|(Startwith$)|(Gte$)|(Lte$)|(InRange$)|(IsNull$)|(IsNotNull$)");
        //排序关键字
        String orderBy = request.getParameter(ORDERBYNAME);

        while (params.hasMoreElements()) {
            String pName = "", fieldName = "", value = "";
            Field existField = null;
            try {
                pName = params.nextElement();
                if (pName.equals(ORDERBYNAME)) {
                    continue;
                }
                fieldName = pName;
                value = request.getParameter(pName);
                if (StringUtils.isEmpty(value)) {
                    continue;
                }

                value = value.trim();
                Matcher matcher = pattern.matcher(pName);
                if (matcher.find()) {
                    String optType = matcher.group();
                    fieldName = pName.replaceAll(optType, "");
                    //查找条件字段是否存在于bean属性中，只有存在于属性中才参与条件计算
                    existField = getEntityCls().getDeclaredField(fieldName);
                    //字段类型不能有Transient注解，有该注解表示不是数据库属性字段
                    if (existField.getAnnotation(Transient.class) == null) {
                        if ("In".equals(optType)) {
                            String[] valueArray = StringUtils.split(value, ",");
                            condition.in(fieldName, Arrays.asList(valueArray));
                        } else if ("Like".equals(optType)) {
                            condition.and().andLike(fieldName, "%" + value + "%");
                        } else if ("Startwith".equals(optType)) {
                            condition.and().andLike(fieldName, value + "%");
                        } else if ("Endwith".equals(optType)) {
                            condition.and().andLike(fieldName, "%" + value);
                        } else if ("Gte".equals(optType)) {
                            if (existField.getType().isInstance(new Date())) {
                                condition.gte(fieldName, DateUtils.getDate(value, "yyyy-MM-dd"));
                            } else {
                                condition.gte(fieldName, value);
                            }
                        } else if ("Lte".equals(optType)) {
                            if (existField.getType().isInstance(new Date())) {
                                condition.lte(fieldName, DateUtils.getDate(value, "yyyy-MM-dd"));
                            } else {
                                condition.lte(fieldName, value);
                            }
                        } else if ("InRange".equals(optType)) {
                            if (StringUtils.isNotBlank(value.replaceAll(",","").trim())){
                                String[] arrs = value.split(",");
                                if (existField.getType().isInstance(new Date())) {
                                    condition.gte(fieldName, DateUtils.getDate(arrs[0], "yyyy-MM-dd"));
                                } else {
                                    condition.gte(fieldName, arrs[0]);
                                }

                                if (arrs.length == 2) {
                                    if (existField.getType().isInstance(new Date())) {
                                        condition.lte(fieldName, DateUtils.getDate(arrs[1], "yyyy-MM-dd"));
                                    } else {
                                        condition.lte(fieldName, arrs[1]);
                                    }
                                }
                            }
                        } else if ("IsNull".equals(optType)) {
                            condition.and().andIsNull(fieldName);
                        } else if ("IsNotNull".equals(optType)) {
                            condition.and().andIsNotNull(fieldName);
                        }
                    }
                } else {
                    //查找条件字段是否存在于bean属性中，只有存在于属性中才参与条件计算
                    existField = getEntityCls().getDeclaredField(fieldName);
                    //字段类型不能有Transient注解，有该注解表示不是数据库属性字段
                    if (existField.getAnnotation(Transient.class) == null) {
                        condition.eq(fieldName, value);
                    }
                }
            } catch (NoSuchFieldException | ParseException e) {
                //属性不存在bean中
            }
        }
        //排序关键字不为空
        if (StringUtils.isNotEmpty(orderBy)) {
            createOrderBy(orderBy, condition);
        }

        return condition;
    }


    /**
     * 为查询对象创建搜索条件
     *
     * @param orderBy
     * @param condition
     */
    public void createOrderBy(String orderBy, Example condition) {
        String orderByStr = "";
        //排序字段名为bean属性名，如果字段有@Column注解，则通过注解提取出数据库字段名；如果没有注解按驼峰规则转换为数据库字段名
        String[] arrays = orderBy.split(",");
        for (int i = 0; i < arrays.length; i++) {
            if (i > 0) {
                orderByStr += ",";
            }
            String[] tmp = arrays[i].split(" ");
            String fieldName = tmp[0];
            String underlineStr = "";
            try {
                Column tColumn = condition.getEntityClass().getDeclaredField(fieldName).getAnnotation(Column.class);
                underlineStr = tColumn.name();
            } catch (Exception e) {
                underlineStr = StringUtil.camelhumpToUnderline(fieldName);
            }
            orderByStr += underlineStr + " " + tmp[1];
        }
        condition.setOrderByClause(orderByStr);
    }


    /**
     * 设置需要特殊处理的字段映射方式，默认值为空
     * 如果需要设置其中部分字段需要特殊映射，比如产品id，需要输出产品名称，则请在子类中自行实现该方法
     *
     * @param list
     * @return
     */
    public List<Map<String, String>> getSpecialVals(List<T> list) {
        return new ArrayList<>();
    }

    /**
     * 设置特殊字段名称，默认值为空
     * 如果需要设置其中部分字段需要特殊映射，比如产品id，需要输出产品名称，则请在子类中自行实现该方法
     *
     * @return
     */
    public List<String> getSpecialCols() {
        return new ArrayList<>();
    }


    public List<List<String>> getSheetData(ExcelParams params) {
        LimitedCondition example = getQueryCondition();
        fillCondition(example);
        List<T> list = findByCondition(example);
        return getSheetData(list,params);
    }
    /**
     * 根据查询数据生成导出所需数据，如果字段没有声明映射方式，则直接输出 toString 的值
     * 如果有自定义映射方式，则使用自定义映射值
     *
     * @return
     */
    public List<List<String>> getSheetData(List<T> list, ExcelParams params) {
        List<List<String>> data = new ArrayList<>(list.size());
        if (list.size() == 0) return data;
        String[] keysArray = params.getKeys();
        Map<String, Field> fieldMap = new HashMap<>();
        try {
            for (String s : keysArray) {
                Field field = list.get(0).getClass().getDeclaredField(s);
                if (field == null) continue;
                field.setAccessible(true);
                fieldMap.put(s, field);
            }
            List<String> specialCols = getSpecialCols();
            int i = 0;
            for (T t : list) {
                List<String> row = new ArrayList<>(keysArray.length);
                for (String s : keysArray) {
                    if (specialCols.contains(s)) {
                        row.add(getSpecialVals(list).get(i).get(s));
                    } else {
                        Field field = fieldMap.get(s);
                        if (field == null) {
                            row.add("");
                        } else {
                            String val = field.get(t) == null ? " " : field.get(t).toString();
                            row.add(val);
                        }
                    }
                }
                data.add(row);
                i++;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static SysYh getCurrentUser() {
        return getCurrentUser(true);
    }
    public static SysYh getCurrentUser(boolean require) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysYh userInfo = (SysYh) request.getAttribute("userInfo");
        RuntimeCheck.ifTrue(require && userInfo == null,"当前登录用户未空！");
        return userInfo;
    }
    public static String getOperateUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysYh userInfo = (SysYh) request.getAttribute("userInfo");
        if (userInfo == null)return null;
        return userInfo.getYhid()+"-"+userInfo.getXm();
    }

    @Override
    public T findById(PK id) {
        return getBaseMapper().selectByPrimaryKey(id);
    }

    @Override
    public T findOneByEntity(T entity) {
        return getBaseMapper().selectOne(entity);
    }

    @Override
    public List<T> findByEntity(T entity) {
        return getBaseMapper().select(entity);
    }

    @Override
    public List<T> findByCondition(Example condition) {
        return getBaseMapper().selectByExample(condition);
    }

    @Override
    public Integer count() {
        return getBaseMapper().selectCount(null);
    }

    @Override
    public Integer countByEntity(T entity) {
        return getBaseMapper().selectCount(entity);
    }

    @Override
    public Integer countByCondition(Example condition) {
        return getBaseMapper().selectCountByExample(condition);
    }

    @Override
    public List<T> findAll() {
        return getBaseMapper().selectAll();
    }

    @Override
    public ApiResponse<String> validAndSave(T entity) {
        getBaseMapper().insertSelective(entity);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<String> validAndUpdate(T entity) {
        getBaseMapper().updateByPrimaryKeySelective(entity);
        return ApiResponse.success();
    }

    @Override
    public int save(T entity) {
        return getBaseMapper().insertSelective(entity);
    }

    @Override
    public int update(T entity) {
        return getBaseMapper().updateByPrimaryKeySelective(entity);
    }

    @Override
    public void remove(PK id) {
        getBaseMapper().deleteByPrimaryKey(id);
    }

    @Override
    public void remove(T entity) {
        getBaseMapper().delete(entity);
    }

    @Override
    public ApiResponse<String> removeIds(PK[] ids) {
        for (PK id : ids) {
            getBaseMapper().deleteByPrimaryKey(id);
        }
        return ApiResponse.deleteSuccess();
    }

    @Override
    public PageInfo<T> findPage(Page page, Example condition) {
        if (page.getPageSize() == 0){
            page.setPageSize(8);
        }
        if (page.getPageNum() == 0){
            page.setPageNum(1);
        }
        if (StringUtils.isEmpty(condition.getOrderByClause())){
            log.debug("该分页没有设置排序字段，默认按创建时间倒序，如果有其他需求，请在 fillCondition 方法中申明");
            condition.setOrderByClause("cjsj desc");
        }
        PageInfo<T> resultPage = PageHelper.startPage(page.getPageNum(), page.getPageSize()).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                getBaseMapper().selectByExample(condition);
            }
        });

        return resultPage;
    }

    @Override
    public PageInfo<T> findPageByEntity(Page page, T entity) {
        PageInfo<T> resultPage = PageHelper.startPage(page.getPageNum(), page.getPageSize()).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                getBaseMapper().select(entity);
            }
        });

        return resultPage;
    }


    @Override
    public List<T> findByIds(Iterable iterable) {
        return findIn("id", iterable);
    }

    @Override
    public List<T> findEq(String key, Object val) {
        return find(key, val, "eq");
    }

    @Override
    public List<T> findEq(Enum innercolum, Object val) {
        return findEq(innercolum.name(), val);
    }

    @Override
    public List<T> findLike(String key, String val) {
        if (StringUtils.isEmpty(val)) return Collections.EMPTY_LIST;
        return find(key, val, "like");
    }

    @Override
    public List<T> findLike(Enum innercolum, String val) {
        return findLike(innercolum.name(), val);
    }

    @Override
    public List<T> findIn(String key, Iterable iterable) {
        return find(key, iterable, "in");
    }

    @Override
    public List<T> findIn(Enum innercolum, Iterable iterable) {
        return findIn(innercolum.name(), iterable);
    }

    private List<T> find(String key, Object val, String option) {
        return getBaseMapper().selectByExample(getExample(key, val, option));
    }

    @Override
    public int countEq(String key, Object val) {
        return count(key, val, "eq");
    }

    @Override
    public int countLike(String key, String val) {
        return count(key, val, "like");
    }

    @Override
    public int countIn(String key, Iterable iterable) {
        return count(key, iterable, "in");
    }

    private int count(String key, Object val, String option) {
        return getBaseMapper().selectCountByExample(getExample(key, val, option));

    }

    @Override
    public boolean ifExists(String key, String val) {
        int count = countEq(key,val);
        return count != 0;
    }

    private Example getExample(String key, Object val, String option) {
        Example example = new Example(getEntityCls());
        switch (option) {
            case "like":
                example.and().andLike(key, "%" + val + "%");
                break;
            case "in":
                Iterable it = (Iterable) val;
                example.and().andIn(key, it);
                break;
            case "eq":
            default:
                example.and().andEqualTo(key, val);
                break;
        }
        return example;
    }
}
