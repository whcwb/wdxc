package com.ldz.sys.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.ExcelParams;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;

/**
 * 单表通用CURD Service类，业务Service直接继承该Service
 * 注意：
 * 分页使用的是Mybatis-PageHelper项目插件，请确保该插件已被项目引用
 * <a href="https://pagehelper.github.io">PageHelper</a>
 * CURD方法是使用的Mybatis通用Mapper项目插件，请确保该插件已被项目引用
 * <a href="https://mapperhelper.github.io">Mapper</a>
 * @author Lee
 * @date 2017年7月26日
 * @param <T>
 * @param <PK>
 */
public interface BaseService<T, PK extends Serializable> {
	LimitedCondition getQueryCondition();

	boolean fillCondition(LimitedCondition condition);

	ApiResponse<List<T>> pager(Page<T> pager);

	List<List<String>> getSheetData(ExcelParams params);
	List<List<String>> getSheetData(List<T> list, ExcelParams params);
	/**
	 * 根据主键查询对象
	 * @param id
	 * @return
	 */
	public T findById(PK id);

	/**
	 * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
	 * @param entity
	 * @return
	 */
	public T findOneByEntity(T entity);

	/**
	 * 根据实体中的属性值进行多个对象结果查询，查询条件使用等号。
	 * @param entity
	 * @return
	 */
	public List<T> findByEntity(T entity);

	/**
	 * 通过Example条件查询结果。
	 * @return
	 */
	public List<T> findByCondition(Example condition);

	/**
	 * 查询表数据总数
	 * @return
	 */
	public Integer count();

	/**
	 * 根据对象查询数据总数
	 * @param entity
	 * @return
	 */
	public Integer countByEntity(T entity);

	/**
	 * 通过Example条件查询数据总数
	 * @param condition
	 * @return
	 */
	public Integer countByCondition(Example condition);

	/**
	 * 查询所有数据
	 * @return
	 */
	public List<T> findAll();

	/**
	 * 对象数据保存
	 * @param entity
	 * @return
	 */
	public int save(T entity);

	/**
	 * 对象数据更新
	 * @param entity
	 * @return
	 */
	public int update(T entity);
	/**
	 * 对象数据保存
	 * @param entity
	 * @return
	 */
	public ApiResponse<String> validAndSave(T entity);

	/**
	 * 对象数据更新
	 * @param entity
	 * @return
	 */
	public ApiResponse<String> validAndUpdate(T entity);

	/**
	 * 根据主键删除指定数据
	 * @param id
	 */
	public void remove(PK id);

	/**
	 * 根据对象条件删除数据，条件使用等号
	 * @param entity
	 */
	public void remove(T entity);

	/**
	 * 根据主键批量删除数据
	 * @param ids
	 * @return
	 */
	public ApiResponse<String> removeIds(PK[] ids);

	/**
	 * 根据条件进行分页查询。页码，从1开始
	 * @param page
	 * @param condition
	 * @return
	 */
	public PageInfo<T> findPage(Page page, Example condition);

	/**
	 * 根据条件进行分页查询。页码，从1开始
	 * @param page
	 * @param entity
	 * @return
	 */
	public PageInfo<T> findPageByEntity(Page page, T entity);

	public List<T> findByIds(Iterable iterable);

	/**
	 * 查询某个字段等于val的结果
	 * @param key
	 * @param val
	 * @return
	 */
	public List<T> findEq(String key, Object val);
	public List<T> findEq(Enum innercolum, Object val);

	/**
	 * 查询某个字段like
	 * @param key
	 * @param val
	 * @return
	 */
	public List<T> findLike(String key, String val);
	public List<T> findLike(Enum innercolum, String val);
	public List<T> findIn(String key, Iterable iterable);
	public List<T> findIn(Enum innercolum, Iterable iterable);
	public int countEq(String key, Object val);
	public int countLike(String key, String val);
	public int countIn(String key, Iterable iterable);

    public boolean ifExists(String key, String val);

    List<T> query(T entity);
}
