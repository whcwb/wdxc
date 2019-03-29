package com.ldz.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysFw;
import com.ldz.sys.service.FwService;
import com.ldz.util.bean.ApiResponse;

/**
 * 平台服务
 */
@RestController
@RequestMapping("/api/fw")
public class FwController extends BaseController<SysFw, String> {
    @Autowired
    private FwService bizService;

    @Override
    protected BaseService<SysFw, String> getBaseService() {
        return bizService;
    }


    @Override
    public ApiResponse<String> save(SysFw entity) {
        return bizService.saveEntity(entity);
    }
    
    @RequestMapping(value="/update", method={RequestMethod.POST})
   	public ApiResponse<String> update(SysFw entity){
    	
   		return bizService.updateEntity(entity);
   	}
}
