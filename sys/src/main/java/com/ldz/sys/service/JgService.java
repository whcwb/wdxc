package com.ldz.sys.service;

import com.ldz.sys.base.BaseService;
import com.ldz.sys.bean.TreeNode;
import com.ldz.sys.model.SysGn;
import com.ldz.sys.model.SysJg;
import com.ldz.util.bean.ApiResponse;

import java.util.List;

/**
 * auther chenwei
 * create at 2018/2/26
 */
public interface JgService extends BaseService<SysJg,String>{

    List<SysJg> getOrgTree(List<SysJg> orgList);
    List<SysJg> getOrgTreeByOrgCodes(List<String> orgCodes);
    List<TreeNode> convertToTreeNodeList(List<SysJg> orgList);


    /**
     * 根据机构层级获取机构列表
     * @param level 机构层级
     * @return 机构列表
     */
    List<SysJg> findByLevel(Integer level);
    /**
     * 新增机构
     * @param entity 参数
     * @return 操作结果
     */
    ApiResponse<String> saveEntity(SysJg entity);

    SysJg findByOrgCode(String orgCode);


    /**
     * 获取机构下面的所有子机构
     * @param orgCode
     * @return
     */
    List<SysJg> findAllSubOrg(String orgCode,String jgmc);

    ApiResponse<List<SysJg>>  getOrgTree(String jgmc);

    ApiResponse<List<TreeNode>> getTree();

    ApiResponse<List<SysJg>> getOrgPath(String orgCode);

    ApiResponse<String> editJgZdSl(int sl, String type,String jgdm);

    void removeEntities(String[] ids);


    ApiResponse<String> updateEntity(SysJg entity);
}
