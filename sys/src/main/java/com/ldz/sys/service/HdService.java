package com.ldz.sys.service;

import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysHdyx;
import com.ldz.util.bean.ApiResponse;
import org.springframework.boot.ApplicationPid;

import java.util.List;

/**
 * @author chenwei
 * @since 2018/2/26
 */
public interface HdService extends BaseService<SysHdyx,String>{
    /**
     * 新增活动
     * @param entity 参数
     * @return 操作结果
     */
    ApiResponse<String> saveEntity(SysHdyx entity);

    /**
     * 编辑活动
     * @param entity 参数
     * @return 操作结果
     */
    ApiResponse<String> updateEntity(SysHdyx entity);
}
