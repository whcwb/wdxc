package com.ldz.biz.controller;

import com.ldz.dao.biz.model.ClBxjz;
import com.ldz.service.biz.interfaces.BxjzService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/bxjz")
@RestController
public class BxjzCtrl extends BaseController<ClBxjz,String> {
    @Autowired
    private BxjzService service;
    @Override
    protected BaseService<ClBxjz, String> getBaseService() {
        return service;
    }
}
