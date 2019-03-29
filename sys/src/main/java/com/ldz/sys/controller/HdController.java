package com.ldz.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.model.SysHdyx;
import com.ldz.sys.service.HdService;
import com.ldz.util.bean.ApiResponse;

/**
 * 活动营销
 */
@RestController
@RequestMapping("/api/hd")
public class HdController extends BaseController<SysHdyx,String>{
    @Autowired
    private HdService service;


    @RequestMapping("add")
    public ApiResponse<String> add(SysHdyx entity){
        return service.saveEntity(entity);
    }

    @RequestMapping("update")
    public ApiResponse<String> update(SysHdyx entity){
        return service.updateEntity(entity);
    }

    @RequestMapping("pager")
    public ApiResponse<List<SysHdyx>> pager(Page<SysHdyx> pager){
        return service.pager(pager);
    }


    @Override
    @RequestMapping(value="/save", method={RequestMethod.POST})
    public ApiResponse<String> save(SysHdyx entity) {
        return service.saveEntity(entity);
    }

    @Override
    protected HdService getBaseService() {
        return service;
    }
}
