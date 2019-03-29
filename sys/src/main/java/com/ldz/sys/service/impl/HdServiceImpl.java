package com.ldz.sys.service.impl;

import com.github.pagehelper.PageInfo;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.constant.Dict;
import com.ldz.sys.mapper.SysHdyxMapper;
import com.ldz.sys.mapper.SysYxhdwjMapper;
import com.ldz.sys.model.SysHdyx;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.model.SysYxhdwj;
import com.ldz.sys.service.HdService;
import com.ldz.sys.util.ContextUtil;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.exception.RuntimeCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chenwei
 * @copyright
 * @category
 * @since 2018/2/26
 */
@Slf4j
@Service
public class HdServiceImpl extends BaseServiceImpl<SysHdyx,String> implements HdService{
    @Autowired
    private SysHdyxMapper hdyxMapper;
    @Autowired
    private SysYxhdwjMapper yxhdwjMapper;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Value("${znzpurl: http://47.98.39.45:9888/api}")
    private String updateMediaUrl;
    @Override
    protected Mapper<SysHdyx> getBaseMapper() {
        return hdyxMapper;
    }


    @Override
    public boolean fillCondition(LimitedCondition condition){

        return true;
    }

    public void afterPager(PageInfo<SysHdyx> resultPage){
        setFiles(resultPage.getList());
    }

    private void setFiles(List<SysHdyx> list){
        if (list.size() == 0)return;
        List<String> hdIds = list.stream().map(SysHdyx::getHdId).collect(Collectors.toList());
        SimpleCondition condition = new SimpleCondition(SysYxhdwj.class);
        condition.in(SysYxhdwj.InnerColumn.hdId,hdIds);
        List<SysYxhdwj> files = yxhdwjMapper.selectByExample(condition);
        if (files.size() == 0)return;
        Map<String,SysHdyx> hdMap = list.stream().collect(Collectors.toMap(SysHdyx::getHdId,p->p));
        for (SysYxhdwj file : files) {
            String hdId = file.getHdId();
            if (StringUtils.isEmpty(hdId))continue;
            SysHdyx hd = hdMap.get(hdId);
            if (hd == null)continue;
            if (StringUtils.isEmpty(hd.getFilePaths())){
                hd.setFilePaths(file.getWjlj()+",");
            }else{
                hd.setFilePaths(hd.getFilePaths() + file.getWjlj()+",");
            }
        }
    }

    /**
     * 新增活动
     *
     * @param entity 参数
     * @return 操作结果
     */
    @Override
    public ApiResponse<String> saveEntity(SysHdyx entity) {
        // 参数检查
        RuntimeCheck.ifBlank(entity.getHdbt(),"请输入活动标题");
        RuntimeCheck.ifBlank(entity.getHdlx(),"请选择活动类型");
        // 执行新增操作
        SysYh yh = ContextUtil.getCurrentUser();
        entity.setCjr(getOperateUser());
        entity.setCjsj(new Date());
        entity.setJgdm(yh.getJgdm());
        entity.setHdId(genId());
        entity.setZt(Dict.CommonStatus.VALID.getCode());
        saveFiles(entity);
        save(entity);
        if ("10".equals(entity.getHdlx())){
            updateMdeia(yh.getJgdm());
        }
        return ApiResponse.success();
    }

    private void updateMdeia(String jgdm){
        log.info("================准备向znzp发送updateMedia请求==================");
        Map<String,Object> params = new HashMap<>();
        params.put("jgdm",jgdm);
        try{
            ResponseEntity<String> responseEntity = restTemplateBuilder.build().postForEntity(updateMediaUrl + "/updateMedia",params,String.class);
            log.info("updateMedita responst:",responseEntity.toString());
        }catch (Exception e){
            log.error("updateMedia请求异常",e);
        }
    }

    private void saveFiles(SysHdyx hdyx){
        if (hdyx == null)return;
        if (StringUtils.isEmpty(hdyx.getFilePaths()))return;
        String[] paths = hdyx.getFilePaths().split(",");
        Date now = new Date();
        String operator = getOperateUser();
        for (String file : paths) {
            int index = file.lastIndexOf(".");
            if (index <= 0)continue;
            String fileType = file.substring(index+1);
            SysYxhdwj yxhdwj = new SysYxhdwj();
            yxhdwj.setCjr(operator);
            yxhdwj.setCjsj(now);
            yxhdwj.setHdId(hdyx.getHdId());
            yxhdwj.setWjlx(fileType);
            yxhdwj.setId(genId());
            yxhdwj.setWjlj(file);
            yxhdwj.setWllj(file);
            yxhdwjMapper.insertSelective(yxhdwj);
        }
    }

    /**
     * 编辑活动
     *
     * @param entity 参数
     * @return 操作结果
     */
    @Override
    public ApiResponse<String> updateEntity(SysHdyx entity) {
    	 // 参数检查
        RuntimeCheck.ifBlank(entity.getHdbt(),"请输入活动标题");
        RuntimeCheck.ifBlank(entity.getHdlx(),"请选择活动类型");
        // 参数检查
        RuntimeCheck.ifBlank(entity.getHdId(),"请选择活动");
        SysHdyx hd = findById(entity.getHdId());
        RuntimeCheck.ifNull(hd,"活动不存在");

        // 修改数据
        hdyxMapper.updateByPrimaryKeySelective(entity);
        // 删除旧文件
        SimpleCondition condition = new SimpleCondition(SysYxhdwj.class);
        condition.eq(SysYxhdwj.InnerColumn.hdId,entity.getHdId());
        yxhdwjMapper.deleteByExample(condition);

        if ("10".equals(entity.getHdlx())){
            updateMdeia(entity.getJgdm());
        }
        saveFiles(entity);
        return ApiResponse.success();
    }
}
