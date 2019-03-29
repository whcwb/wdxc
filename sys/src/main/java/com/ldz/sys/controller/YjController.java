package com.ldz.sys.controller;

import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysYjfk;
import com.ldz.sys.service.YjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 意见
 */
@RestController
@RequestMapping("/api/yj")
public class YjController extends BaseController<SysYjfk,String>{
    @Autowired
    private YjService service;

    @Override
    protected BaseService<SysYjfk, String> getBaseService() {
        return service;
    }
}
