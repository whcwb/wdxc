package com.ldz.ticserver.service;

import com.ldz.util.bean.RequestCommonParamsDto;

public interface BizApiService {

	public void pushData(RequestCommonParamsDto dto);
	
	public void pushFileData(RequestCommonParamsDto dto);
	
	//public void gpsData(RequestCommonParamsDto dto);
}
