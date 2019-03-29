package com.ldz.sys.controller;

import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysRz;
import com.ldz.sys.service.RzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志管理
 */
@RestController
@RequestMapping("/api/rz")
public class RzController extends BaseController<SysRz,String>{
    @Autowired
    private RzService service;

    @Override
    protected BaseService<SysRz, String> getBaseService() {
        return service;
    }
}
