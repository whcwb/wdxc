package com.ldz.job.service;

import com.ldz.dao.biz.model.ClCl;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

public interface ClService extends BaseService<ClCl, String> {
   
	ApiResponse<String> updateNianshen();
}
