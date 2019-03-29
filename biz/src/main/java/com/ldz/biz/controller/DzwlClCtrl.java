package com.ldz.biz.controller;

import com.ldz.dao.biz.model.ClDzwlCl;
import com.ldz.service.biz.interfaces.DzwlClService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 电子围栏
 */
@RestController
@RequestMapping("api/dzwlCl")
public class DzwlClCtrl extends BaseController<ClDzwlCl,String>{
    @Autowired
    private DzwlClService service;

    @Override
    protected BaseService<ClDzwlCl, String> getBaseService() {
        return service;
    }
    @Override
    @RequestMapping(value="/save", method={RequestMethod.POST})
    public ApiResponse<String> save(ClDzwlCl entity){
        return service.saveEntity(entity);
    }


}
