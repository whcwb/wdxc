package com.ldz.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClSg;
import com.ldz.service.biz.interfaces.SgService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;

/**
 * 事故管理
 */
@RestController
@RequestMapping("api/sg")
public class SgCtrl extends BaseController<ClSg,String>{
    @Autowired
    private SgService service;

    @Override
    protected BaseService<ClSg, String> getBaseService() {
        return service;
    }

}
