package com.ldz.znzp.base;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.Page;
import com.ldz.util.bean.ApiResponse;

/**
 * 单表通用CURD Control基础类，适用于单表有CURD业务操作的Controller继承，如果自己的业务不需要更新操作时（包含：新增、修改、删除）可以参考类方法，不继承该基础类
 * 该基础类是一个Rest方式接口，请注意里面提供方法的请求类型和参数内容，每个方法是可以重写的
 * 注意：
 * 1、该基础类提供的方法只返回json数据，不做页面ModelView跳转
 * 2、基础类要在类方法添加注解@RequestMapping，访问入口，防止访问路径冲突
 * @param <T>
 * @param <PK>
 */
public abstract class BaseController<T, PK extends Serializable> {
	//@Autowird通过Spring加载的Service基础类
    protected abstract BaseService<T, PK> getBaseService();

    /**
     * 根据主键获取对象基本信息
     * @param pkid
     * @return
     */
    @RequestMapping(value="/{pkid}", method={RequestMethod.GET})
	public ApiResponse<T> get(@PathVariable("pkid")PK pkid){
    	return ApiResponse.success(getBaseService().findById(pkid));
	}
    
    /**
     * 获取指定对象表中所有数据。
     * 如果对数据要求高，请重写该方法或是不直接继承该类，防止数据泄露
     * @return
     */
    @RequestMapping(value="/getAll", method={RequestMethod.GET})
	public ApiResponse<List<T>> getAll(){
		return ApiResponse.success(getBaseService().findAll());
	}
    
    /**
     * 根据对象字段值查询数据
     * @param entity
     * @return
     */
    @RequestMapping(value="/getCondition", method={RequestMethod.POST})
	public ApiResponse<List<T>> getCondition(@RequestBody T entity){
		return ApiResponse.success(getBaseService().findByEntity(entity));
	}
    
    /**
     * 分页查询。默认根据前台传递的值做精确搜索。需要其他搜索方式，请自行重新该方法
     * @param entity
     * @param pager
     * @return
     */
	@RequestMapping(value="/pager", method={RequestMethod.POST, RequestMethod.GET})
	public ApiResponse<List<T>> pager(T entity, Page<T> pager){
		return getBaseService().pager(pager);
	}

	/**
     * 数据新增方法
     * 如果对数据要求高，请重写该方法或是不直接继承该类，防止数据泄露
     * @param entity
     * @return
     */
    @RequestMapping(value="/save", method={RequestMethod.POST})
	public ApiResponse<String> save(T entity){
		getBaseService().save(entity);
		return ApiResponse.saveSuccess();
	}
    
    /**
     * 数据修改方法
     * 如果对数据要求高，请重写该方法或是不直接继承该类，防止数据泄露
     * @param entity
     * @return
     */
    @RequestMapping(value="/update", method={RequestMethod.POST})
	public ApiResponse<String> update(T entity){
		getBaseService().update(entity);
		return ApiResponse.saveSuccess();
	}
    
    /**
     * 数据删除方法
     * 如果对数据要求高，请重写该方法或是不直接继承该类，防止数据泄露
     * @param id
     * @return
     */
    @RequestMapping(value="/remove/{pkid}", method={RequestMethod.POST})
	public ApiResponse<String> remove(@PathVariable("pkid")PK id){
		getBaseService().remove(id);
		return ApiResponse.success();
	}
    
    /**
	 * 批量删除操作
	 * @param ids
	 * @return
	 */
    @RequestMapping(value="/removeIds", method={RequestMethod.POST})
	public ApiResponse<String> remove(PK[] ids){
		return getBaseService().removeIds(ids);
	}
    
    @InitBinder 
    public void initBinder(WebDataBinder binder) {  
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");    
		dateFormat.setLenient(false); 
		//true:允许输入空值，false:不能为空值 
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   
    }
}