package com.ldz.sys.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ExcelParams;
import com.ldz.util.commonUtil.ExcelUtil;
import com.ldz.util.spring.SpringContextUtil;

/**
 * @author chenwei
 * @copyright
 * @category
 * @since 2018/2/6
 */
@Controller
@RequestMapping("api")
public class ExportCtrl {

    /**
     * 通用导出方法
     * 如果子类没有实现 getSpecialVals 和 getSpecialCols 方法，则对每个字段直接导出 toString 的值
     * 如果需要自定义字段值映射，请在子类中实现上述两个方法
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/export",method = {RequestMethod.POST,RequestMethod.GET})
    public void export(String exportType,String cols,String keys, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExcelParams params = new ExcelParams();
        params.setType(exportType);
        params.setFileName(getFileName());
        keys = java.net.URLDecoder.decode(keys , "UTF-8");
        cols = java.net.URLDecoder.decode(cols , "UTF-8");
        String[] keysArray = keys.split(",");
        String[] heads = cols.split(",");
        params.setKeys(keysArray);
        response.setContentType("application/msexcel");
        response.setHeader("pragma", "no-cache");
        response.addHeader("Content-Disposition","attachment; filename="+ params.getFileName());
        OutputStream out = response.getOutputStream();
        ExcelUtil.createSheet(out,"统计",heads,getData(params));
    }

    private List<List<String>> getData(ExcelParams params){
        String beanName = params.getType() + "ServiceImpl";
        BaseService service = (BaseService) SpringContextUtil.getBean(beanName);
        return service.getSheetData(params);
    }

    private String getFileName(){
        try {
            return java.net.URLEncoder.encode(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss") + ".xls","UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
