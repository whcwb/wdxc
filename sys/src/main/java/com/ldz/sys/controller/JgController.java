package com.ldz.sys.controller;

import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.bean.TreeNode;
import com.ldz.sys.model.SysGn;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.JgService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/jg")
public class JgController extends BaseController<SysJg, String> {
	@Autowired
	private JgService jgService;

	@Override
	protected BaseService<SysJg, String> getBaseService() {
		return jgService;
	}

	@RequestMapping("getCurrentUserOrgTree")
	public ApiResponse<List<SysJg>> getCurrentUserOrgTree(){
		SysYh currentUser = getCurrentUser();
		List<SysJg> orgList =  jgService.findAllSubOrg(currentUser.getJgdm(),null);
		List<SysJg> orgTree = jgService.getOrgTree(orgList);
		return ApiResponse.success(orgTree);
	}
	@RequestMapping("getCurrentOrgTree")
	public ApiResponse<List<TreeNode>> getCurrentOrgTree(){
		SysYh currentUser = getCurrentUser();
		List<SysJg> orgList =  jgService.findAllSubOrg(currentUser.getJgdm(),null);
		List<TreeNode> orgNode = jgService.convertToTreeNodeList(orgList);
		List<TreeNode> orgTree = TreeNode.buildTree(orgNode);
		return ApiResponse.success(orgTree);
	}

	@RequestMapping("add")
	public ApiResponse<String> add(SysJg entity) {
		return jgService.saveEntity(entity);
	}

	@RequestMapping("getOrgTree")
	public ApiResponse<List<SysJg>> getOrgTree(String jgmc) {
		return jgService.getOrgTree(jgmc);
	}

	@RequestMapping("getTree")
	public ApiResponse<List<TreeNode>> getTree(){
		return jgService.getTree();
	}


	@GetMapping("getOrgPath")
	public ApiResponse<List<SysJg>> getOrgPath(String orgCode){
		return jgService.getOrgPath(orgCode);
	}

	@Override
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ApiResponse<String> save(SysJg jg) {
		return jgService.saveEntity(jg);
	}

	@RequestMapping("getSubOrgList")
	public ApiResponse<List<SysJg>> getSubOrgList(String jgmc){
		SysYh user = getCurrentUser();
		List<SysJg> orgList = jgService.findAllSubOrg(user.getJgdm(),jgmc);
		return ApiResponse.success(orgList);
	}

	@PostMapping("/editJgZdSl")
	public ApiResponse<String> editJgZdSl(int sl , String type,String jgdm){
		return jgService.editJgZdSl(sl,type,jgdm);
	}

	/**
	 * 批量删除操作
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/removeIds", method={RequestMethod.POST})
	public ApiResponse<String> remove(String[] ids){
		jgService.removeEntities(ids);
		return ApiResponse.success();
	}


	@RequestMapping(value="/update", method={RequestMethod.POST})
	public ApiResponse<String> update(SysJg entity){
		return jgService.updateEntity(entity);
	}

}
