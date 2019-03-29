package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;

import java.util.List;
import java.util.Map;

/**
 * auther chenwei
 * create at 2018/7/10
 */
public interface KvService {
    ApiResponse<List<Map<String,Object>>> list(String model, String[] cols, String extra);
}
