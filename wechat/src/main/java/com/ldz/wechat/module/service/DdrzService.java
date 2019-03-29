package com.ldz.wechat.module.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.ClDd;
import com.ldz.wechat.module.model.ClDdrz;

import java.util.List;

public interface DdrzService extends BaseService<ClDdrz,String> {
    void log(ClDd order);
}
