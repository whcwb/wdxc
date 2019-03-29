package com.ldz.biz.controller;

import com.ldz.service.biz.interfaces.KvService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * auther chenwei
 * create at 2018/7/10
 */
@RestController
@RequestMapping("/pub/kv")
public class KvListCtrl{
    @Autowired
    private KvService kvService;
    @RequestMapping("list")
    public ApiResponse<List<Map<String,Object>>> list(String model, String key, String val){
        return kvService.list(model,new String[]{key,val},null);
    }

}
