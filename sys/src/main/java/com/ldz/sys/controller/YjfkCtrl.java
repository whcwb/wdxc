package com.ldz.sys.controller;

import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysYjfk;
import com.ldz.sys.service.YjfkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/yjfk")
public class YjfkCtrl extends BaseController<SysYjfk,String> {

    @Autowired
    private YjfkService yjfkService;
    @Override
    protected BaseService<SysYjfk, String> getBaseService() {
        return yjfkService;
    }
}
