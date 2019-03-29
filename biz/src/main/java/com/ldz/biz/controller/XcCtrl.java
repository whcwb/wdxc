package com.ldz.biz.controller;

import com.ldz.dao.biz.model.ClXc;
import com.ldz.service.biz.interfaces.XcService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/xc")
public class XcCtrl extends BaseController<ClXc,String> {
    @Autowired
    private XcService xcService;
    @Override
    protected BaseService<ClXc, String> getBaseService() {
        return xcService;
    }

    @RequestMapping("history")
    public ApiResponse<List<Map<String,Object>>> history(String zdbh,String startTime,String endTime){
        return xcService.history(zdbh,startTime,endTime);
    }
}
