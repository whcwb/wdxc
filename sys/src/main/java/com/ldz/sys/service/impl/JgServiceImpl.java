package com.ldz.sys.service.impl;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.bean.TreeNode;
import com.ldz.sys.constant.Dict;
import com.ldz.sys.mapper.SysPtjgMapper;
import com.ldz.sys.model.JgZdLog;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.JgService;
import com.ldz.sys.service.JgZdLogService;
import com.ldz.sys.util.OrgUtil;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.EncryptUtil;
import com.ldz.util.exception.RuntimeCheck;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * auther chenwei create at 2018/2/26
 */
@Service
public class JgServiceImpl extends BaseServiceImpl<SysJg, String> implements JgService {
	@Autowired
	private SysPtjgMapper ptjgMapper;

	@Autowired
	private JgService jgService;

	@Autowired
	private JgZdLogService jgZdLogService;

	@Override
	protected Mapper<SysJg> getBaseMapper() {
		return ptjgMapper;
	}

	@Override
	public List<SysJg> getOrgTree(List<SysJg> orgList) {
		Map<String, SysJg> orgIdMap = orgList.stream().collect(Collectors.toMap(SysJg::getJgdm, p -> p));
		List<SysJg> root = new ArrayList<>();
		for (SysJg org : orgList) {
			if (StringUtils.isEmpty(org.getFjgdm()) || !orgIdMap.containsKey(org.getFjgdm())) {
				root.add(org);
				continue;
			}
			SysJg father = orgIdMap.get(org.getFjgdm());
			if (father == null)
				continue;
			if (father.getChildren() == null || father.getChildren().size() == 0) {
				List<SysJg> children = new ArrayList<>();
				children.add(org);
				father.setChildren(children);
			} else {
				father.getChildren().add(org);
			}
		}
		return root;
	}

	@Override
	public List<SysJg> getOrgTreeByOrgCodes(List<String> orgCodes) {
		List<SysJg> orgs = findIn(SysJg.InnerColumn.jgdm, orgCodes);
		return getOrgTree(orgs);
	}

	/**
	 * 根据机构层级获取机构列表
	 *
	 * @param level
	 *            机构层级
	 * @return 机构列表
	 */
	@Override
	public List<SysJg> findByLevel(Integer level) {
		RuntimeCheck.ifNull(level, "机构层级不能为空");
		return findEq(SysJg.InnerColumn.jgdj, level);
	}

	/**
	 * 新增机构
	 *
	 * @param entity
	 *            参数
	 * @return 操作结果
	 */
	@Override
	public ApiResponse<String> saveEntity(SysJg entity) {
		if (StringUtils.isEmpty(entity.getJglx())) {
			entity.setJglx("10");
		}
		Dict.OrgType orgType = Dict.OrgType.toEnum(entity.getJglx());
		RuntimeCheck.ifNull(orgType, "机构类型不存在");

		entity.setCjr(getOperateUser());
		entity.setCjsj(new Date());
		entity.setZt(Dict.CommonStatus.VALID.getCode());
		String orgCode = genOrgCode(entity);
		entity.setJgdm(orgCode);
		if (StringUtils.isEmpty(entity.getFjgdm())) { // 一级机构
			entity.setJgdj(1);
		} else {
			String fatherCode = entity.getFjgdm();
			int fatherLevel = OrgUtil.getLevel(fatherCode);
			entity.setJgdj(fatherLevel + 1);
		}
		if(StringUtils.isNotBlank(entity.getJkSq())){
			if(StringUtils.equals(entity.getJkSq(),"00")){
				RuntimeCheck.ifBlank(entity.getJkIp(), "请输入ip");
				String s = EncryptUtil.encryptUserPwd(entity.getJgdm() + entity.getJkIp());
				entity.setJkCode(s);
			}
		}else{
			entity.setJkSq("10");
		}
		ptjgMapper.insertSelective(entity);

		JgZdLog jgZdLog = new JgZdLog();
		jgZdLog.setId(genId());
		jgZdLog.setJgdm(entity.getJgdm());
		jgZdLog.setJgmc(entity.getJgmc());
		jgZdLog.setZdSl(entity.getZdsl());
		jgZdLog.setLx("+");
		jgZdLog.setCjsj(DateUtils.getNowTime());
		jgZdLog.setCjr(getCurrentUser().getYhid() + "-" + getCurrentUser().getXm());
		jgZdLogService.save(jgZdLog);

		ApiResponse<String> res = new ApiResponse<>();
		res.setResult(orgCode);
		return res;
	}

	private String genOrgCode(SysJg org) {
		String fatherCode = org.getFjgdm();
		if (fatherCode == null)
			fatherCode = "";
		int level = fatherCode.length() / 3 + 1;
		SimpleCondition condition = new SimpleCondition(SysJg.class);
		condition.eq(SysJg.InnerColumn.jgdj, level);
		condition.setOrderByClause(SysJg.InnerColumn.jgdm.desc());
		List<SysJg> orgs = findByCondition(condition);
		if (orgs.size() == 0)
			return fatherCode + "001";
		String orgCode = orgs.get(0).getJgdm();
		String maxJgdm = orgCode.substring(orgCode.length() - 3);
		int max = Integer.parseInt(maxJgdm) + 1;
		return fatherCode + StringUtils.leftPad("" + max, 3, "0");
	}

	@Override
	public SysJg findByOrgCode(String orgCode) {
		List<SysJg> jgs = findEq(SysJg.InnerColumn.jgdm, orgCode);
		if (jgs.size() == 0)
			return null;
		return jgs.get(0);
	}

	/**
	 * 获取机构下面的所有子机构
	 *
	 * @param orgCode
	 * @return
	 */
	@Override
	public List<SysJg> findAllSubOrg(String orgCode,String jgmc) {
		SimpleCondition condition = new SimpleCondition(SysJg.class);
		condition.startWith(SysJg.InnerColumn.jgdm,orgCode);
		if (StringUtils.isNotEmpty(jgmc)){
			condition.like(SysJg.InnerColumn.jgmc,jgmc);
		}
		return ptjgMapper.selectByExample(condition);
	}

	@Override
	public ApiResponse<List<SysJg>> getOrgTree(String jgmc) {
		ApiResponse<List<SysJg>> response = new ApiResponse<>();
		SysYh user = getCurrentUser();
		List<SysJg> orgs = jgService.findAllSubOrg(user.getJgdm(),jgmc);
		List<SysJg> orgTree = jgService.getOrgTree(orgs);
		response.setResult(orgTree);
		return response;
	}

	/**
	 * 根据机构代码获取当前机构的所有父机构
	 * @param orgCode
	 * @return
	 */
	@Override
	public ApiResponse<List<SysJg>> getOrgPath(String orgCode) {
		SysJg function = findById(orgCode);
		RuntimeCheck.ifNull(function,"未找到记录");
		List<SysJg> list = new ArrayList<>();
		list.add(function);
		findParent(function,list);
		list.sort(Comparator.comparingInt(SysJg::getJgdj));
		return ApiResponse.success(list);
	}

	@Override
	public ApiResponse<String> editJgZdSl(int sl, String type,String jgdm) {
		SysYh currentUser = getCurrentUser();
		SysJg sysJg = findByOrgCode(jgdm);
		RuntimeCheck.ifTrue(ObjectUtils.isEmpty(sysJg), "机构不存在");
		RuntimeCheck.ifFalse(currentUser.getLx().equals("su"), "抱歉,您没有权限操作");
		RuntimeCheck.ifTrue(sl < 0, "数量不能小于0");
		RuntimeCheck.ifFalse( StringUtils.equals(type,"+")||StringUtils.equals(type,"-"), "操作有误,只能加减");
		if(StringUtils.equals(type,"+")){
			sysJg.setZdsl(sysJg.getZdsl() + sl);

		}else if(StringUtils.equals(type,"-")){
			RuntimeCheck.ifTrue(sysJg.getZdsl() - sl < 0  , "当前机构的数量不足");
			sysJg.setZdsl(sysJg.getZdsl() - sl);
		}
		sysJg.setXgr(currentUser.getXm());
		sysJg.setXgsj(new Date());
		update(sysJg);

		// 记录日志
		JgZdLog zdLog = new JgZdLog();
		zdLog.setId(genId());
		zdLog.setJgdm(jgdm);
		zdLog.setJgmc(sysJg.getJgmc());
		zdLog.setZdSl(sl);
		zdLog.setLx(type);
		zdLog.setCjsj(DateUtils.getNowTime());
		zdLog.setCjr(currentUser.getXm());
		jgZdLogService.save(zdLog);


		return ApiResponse.success();
	}

	@Override
	public void removeEntities(String[] ids) {

		List<String> list = Arrays.asList(ids);
		if(CollectionUtils.isNotEmpty(list)){
			list.forEach(s -> {
				SimpleCondition condition = new SimpleCondition(SysJg.class);
				condition.startWith(SysJg.InnerColumn.jgdm, s);
				ptjgMapper.deleteByExample(condition);
			});
		}


	}



	@Override
	public ApiResponse<String> updateEntity(SysJg entity) {

		if(StringUtils.isNotBlank(entity.getJkSq()) && StringUtils.equals(entity.getJkSq(),"00")){
			// 更新为授权
			RuntimeCheck.ifBlank(entity.getJkIp(), "请输入ip");
			String code = EncryptUtil.encryptUserPwd(entity.getJgdm() + entity.getJkIp());
			entity.setJkCode(code);
		}else{
			entity.setJkSq("10");
		}
		update(entity);

		return ApiResponse.success();
	}


	private void findParent(SysJg function,List<SysJg> result){
		if (function == null){
			return;
		}
		if (StringUtils.isEmpty(function.getFjgdm())){
			return;
		}
		SysJg father = findById(function.getFjgdm());
		if (father == null){
			return;
		}
		result.add(father);
		findParent(father,result);
	}

	@Override
	public ApiResponse<List<TreeNode>> getTree() {
		SysYh user = getCurrentUser();
		List<SysJg> orgs = jgService.findAllSubOrg(user.getJgdm(),null);
		List<TreeNode> treeNodes = convertToTreeNodeList(orgs);
		treeNodes = TreeNode.buildTree(treeNodes);
		return ApiResponse.success(treeNodes);
	}



	@Override
	public List<TreeNode> convertToTreeNodeList(List<SysJg> orgList){
		List<TreeNode> treeNodes = new ArrayList<>(orgList.size());
		for (SysJg jg : orgList) {
			treeNodes.add(convertToTreeNode(jg));
		}
		return treeNodes;
	}
	private TreeNode convertToTreeNode(SysJg org){
		TreeNode node = new TreeNode();
		node.setLabel(org.getJgmc());
		node.setValue(org.getJgdm());
		node.setFather(org.getFjgdm());
		return node;
	}


}
