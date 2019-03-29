package com.ldz.api.module.app.controller;

import com.ldz.service.biz.interfaces.XcService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geo")
public class GeoController {

    @Autowired
    private XcService xcService;

    @PostMapping("/getAddress")
    public ApiResponse<String> getAddress(String lat , String lng , String type , String area){
        String address = xcService.getAddress(lat, lng, type, area);
        return ApiResponse.success(address);
    }

}
