package com.ldz.obd.service;

import com.ldz.obd.common.bean.ApiResponse;
import com.ldz.obd.common.bean.MessageBean;
import com.ldz.obd.util.NettyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    private NettyUtil nettyUtil;
    @Override
    public ApiResponse<String> send(MessageBean bean) {
        return nettyUtil.sendMessage(bean);
    }
}
