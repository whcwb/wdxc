package com.ldz.dwq.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.pagehelper.PageInfo;

/**
 * 接口返回对象封装bean结构
 * @author 李彬彬
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ApiResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1272926047811431195L;
	//成功
	public final static int SUCCESS = 200;
	//失败
	public final static int FAILED = 500;
	//授权认证失败
	public final static int AUTH_FAILED = 999;
	// 无权限访问
	public final static int FORBIDDEN = 403;

	/**
	 * 返回消息code编码
	 * 200：表示接口请求成功
	 * 201：表示接口请求成功，但是有新的版本更新，可做更新操作，会返回请求数据
	 * 202：表示接口请求成功，但是有新的版本更新，必须做版本更新操作，不会返回请求数据
	 * 999：表示用户身份验证失败，需要重新登陆授权
	 * 500和其它值：表示接口请求失败
	 * 
	 */
	private int code = SUCCESS;
	//返回消息内容
	private String message = "请求成功！";
	//返回结果对象
	@JsonInclude(value=Include.NON_EMPTY)
	private T result;
	//如果是分页查询，则返回分页对象
	@JsonInclude(value=Include.NON_EMPTY)
	private PageInfo<T> page;
	//有新的APP版本时，该字段有值
	@JsonInclude(value=Include.NON_EMPTY)
	private Object newVer;

	public ApiResponse() {
	}

	public ApiResponse(T result) {
		this.result = result;
	}

	public ApiResponse<T> emptyPage(){
		List<T> list = new ArrayList<>();
		PageInfo<T> pageInfo = new PageInfo<>(list);
		this.setPage(pageInfo);
		return this;
	}

	public static ApiResponse<String> success(String msg){
		ApiResponse<String> res = new ApiResponse<>();
		res.setMessage(msg);
		return res;
	}
	public static <T> ApiResponse<T> success(T result){
		ApiResponse<T> res = new ApiResponse<>();
		res.setMessage("操作成功");
		res.setResult(result);
		return res;
	}
	public static ApiResponse<String> saveSuccess(){
		return success("数据保存成功！");
	}
	public static ApiResponse<String> deleteSuccess(){
		return success("数据删除成功！");
	}
	public static ApiResponse<String> updateSuccess(){
		return success("数据修改成功！");
	}

	public static ApiResponse<String> success(){
		return success("操作成功");
	}
	public static ApiResponse<String> fail(String msg){
		ApiResponse<String> res = new ApiResponse<>();
		res.setCode(FAILED);
		res.setMessage(msg);
		return res;
	}

	public static ApiResponse<String> fail(){
		return fail("操作失败");
	}
	public static ApiResponse<String> error(){
		return fail("未知错误");
	}
	public static ApiResponse<String> forbidden(){
		ApiResponse<String> res = new ApiResponse<>();
		res.setCode(FORBIDDEN);
		res.setMessage("无权限访问");
		return res;
	}
	public boolean isSuccess(){
		return this.code == 200;
	}
	public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Object getNewVer() {
		return newVer;
	}

	public void setNewVer(Object newVer) {
		this.newVer = newVer;
	}

	@Override
	public String toString() {
		return "ApiResponse{" +
				"code=" + code +
				", message='" + message + '\'' +
				", result=" + result +
				", page=" + page +
				", newVer=" + newVer +
				'}';
	}
}