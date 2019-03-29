package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.bean.ObdMessageBean;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

public interface ObdService extends BaseService<ObdMessageBean,String>{

    ApiResponse<Object> getObdTimely(String obdId);
}
