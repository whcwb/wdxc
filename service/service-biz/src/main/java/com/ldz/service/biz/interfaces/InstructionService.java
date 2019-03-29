package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.sys.model.SysYh;
import com.ldz.util.bean.ApiResponse;

public interface InstructionService {

	/*
	 * 用于业务系统对终端发送指令
	 *
	 */

	ApiResponse<String> sendinstruction(GpsInfo info);

    ApiResponse<String> batchUpdate(GpsInfo info, ClZdgl zdgl, SysYh user);
}
