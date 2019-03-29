package com.ldz.api.module.app.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ldz.dao.biz.mapper.ClSpkMapper;
import com.ldz.dao.biz.model.ClSpk;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/media")
public class MediaCtrl extends BaseController<ClSpk,String> {


    @Override
    protected BaseService<ClSpk, String> getBaseService() {
        return null;
    }
}
