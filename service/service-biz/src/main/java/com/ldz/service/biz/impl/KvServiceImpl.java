package com.ldz.service.biz.impl;

import com.ldz.service.biz.interfaces.KvService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.exception.RuntimeCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * auther chenwei
 * create at 2018/7/10
 */
@Slf4j
@Service
public class KvServiceImpl implements KvService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ApiResponse<List<Map<String,Object>>> list(String model, String[] cols,String extra) {
        RuntimeCheck.ifBlank(model,"请输入 model");
        String tableName = "SYS_CLK_PTYH";
//        try {
//            Class<?> o = Class.forName(model);
//            RuntimeCheck.ifFalse(o.isAnnotationPresent(Table.class),"未找到Table注解");
//            Table table = o.getAnnotation(Table.class);
//            tableName = table.name();
//        } catch (ClassNotFoundException e) {
//            log.error("获取 tableName 异常");
//            return new ApiResponse<>();
//        }

        StringBuilder sql = new StringBuilder("select ");
        String[] colNames = new String[]{"k","v","v1","v2","v3"};
        int c = 0;
        for (String col : cols) {

            sql.append(col).append(" ").append(colNames[c++]).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" from ").append(tableName).append(" ");
        if (StringUtils.isNotEmpty(extra)){
            sql.append(extra);
        }
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString());
        return ApiResponse.success(list);
    }
}
