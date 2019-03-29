package com.ldz.obd.service;

import com.ldz.obd.common.bean.ApiResponse;
import com.ldz.obd.common.bean.MessageBean;

public interface ApiService {

    ApiResponse<String> send(MessageBean bean);
}
