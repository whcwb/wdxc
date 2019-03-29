package com.ldz.biz.controller;

import com.ldz.dao.biz.bean.*;
import com.ldz.dao.biz.model.ClPb;
import com.ldz.service.biz.interfaces.PbService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/pb")
public class ClPbCtrl extends BaseController<ClPb, String> {

	@Autowired
	private PbService pbservice;

	@Override
	protected BaseService<ClPb, String> getBaseService() {
		return pbservice;
	}

	@Override
	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public ApiResponse<String> update(ClPb entity) {
		return pbservice.updateEntity(entity);
	}

	/**
	 * 排班请求操作
	 * 1、车辆已绑定司机
	 * 2、车辆状态属于正常状态
	 * 3、这台车，在今天这条线路上还没有排班信息
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/savepb", method = { RequestMethod.POST })
	public ApiResponse<String> save(ClPb entity) {
		return pbservice.saveEntity(entity);
	}

	/**
	 * 从线路上移除一个车辆
	 * @param xlId
	 * @param clId
	 * @param date
	 * @return
	 */
	@RequestMapping("deleteByXlAndCl")
	public ApiResponse<String> deleteByXlAndCl(String xlId,String clId,String date){
		return pbservice.deleteByXlAndCl(xlId,clId,date);
	}

	@PostMapping("/xbpb/")
	public ApiResponse<List<PbInfo>> getPbInfo(PbClXlmodel pbclxlmodel) {
		return pbservice.getPbInfo(pbclxlmodel);
	}

	/**
	 * 排班列表页面
	 * @param pbclxlmodel
	 * @return
	 */
	@PostMapping("/xbbjpb/")
	public ApiResponse<List<XbXlPb>> getAllPbInfo(PbClXlmodel  pbclxlmodel) {
		return pbservice.getAllPbInfo(pbclxlmodel);
	}

	/**
	 * 检查当前线路未排班的车辆列表
	 * @param xlId  线路ID
	 * @param date	 排班时间
	 * @return
	 */
	@RequestMapping(value="/getcllist", method={RequestMethod.POST})
	public ApiResponse<List<ClClModel>> getAllNotPbClList(String xlId, String date, String cx){
		return pbservice.getAllNotPbClList(xlId,date,cx);
	}

	/**
	 * 班车统计
	 * @param 
	 * @return
	 */
	@PostMapping("/banchetj")
	public ApiResponse<List<PbInfo>> bancheTj(PbClXlmodel pbclxlmodel){
		
		return pbservice.bancheTj(pbclxlmodel);
	}
	
	/**
	 * 今日校巴开班条形图
	 * @param 
	 * @return
	 */
	@PostMapping("/xbkb")
	public ApiResponse<JrXbKb> xbkb(PbClXlmodel pbclxlmodel){
		
		return pbservice.xbkb(pbclxlmodel);
	}


	/**
	 * 批量排班操作
	 */
	@PostMapping("/pbList")
	public ApiResponse<String> savePbList(ClPb entity){
		return pbservice.savePbList(entity);
	}

	/**
	 * 查询当前时间段，当前路线有哪些车辆有排班
	 */
	@PostMapping("/checkPbCl")
	public ApiResponse<List<PbInfo>> checkPbCl(ClPb entity){
		return pbservice.checkPbCl(entity);
	}

	/**
	 * 删除排班列表
	 */
	@PostMapping("/delPbList")
	public ApiResponse<String> delPbList(ClPb entity){
		return pbservice.delPbList(entity);
	}


}
