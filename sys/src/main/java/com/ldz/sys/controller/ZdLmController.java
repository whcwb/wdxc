package com.ldz.sys.controller;

import com.github.pagehelper.PageInfo;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysFw;
import com.ldz.sys.model.SysGn;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.model.SysZdlm;
import com.ldz.sys.service.ZdlmService;
import com.ldz.sys.service.ZdxmService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台字典
 */
@RestController
@RequestMapping("/api/zd")
public class ZdLmController extends BaseController<SysZdlm, String> {
    @Autowired
    private ZdlmService zdlmService;

    @Override
    protected BaseService<SysZdlm, String> getBaseService() {
        return zdlmService;
    }

    @Override
    @RequestMapping(value="/save", method={RequestMethod.POST})
    public ApiResponse<String> save(SysZdlm entity) {
        this.zdlmService.add(entity);
        return ApiResponse.success();
    }


}
