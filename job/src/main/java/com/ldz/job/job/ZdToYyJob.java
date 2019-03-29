package com.ldz.job.job;

import com.ldz.dao.biz.mapper.ClZdglMapper;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.util.bean.YingyanResponse;
import com.ldz.util.bean.YyEntity;
import com.ldz.util.yingyan.GuiJIApi;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ZdToYyJob implements Job {

    @Autowired
    private ClZdglMapper clzdglmapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<ClZdgl> zdNotYy = clzdglmapper.getZDNotYy();
        YyEntity yyEntity = new YyEntity();
        yyEntity.setAk(GuiJIApi.AK);
        yyEntity.setService_id(GuiJIApi.SERVICE_ID);
        for (ClZdgl clZdgl : zdNotYy){
            yyEntity.setEntity_name(clZdgl.getZdbh());
            YingyanResponse changeEntity = GuiJIApi.changeEntity(yyEntity, GuiJIApi.saveEntityuRL);
            if (StringUtils.equals(changeEntity.getStatus(), "0") || StringUtils.equals(changeEntity.getStatus(),"3005")) {
                clZdgl.setSfyy("已上传鹰眼服务器");
                clzdglmapper.updateByPrimaryKeySelective(clZdgl);
            }
        }

    }
}
