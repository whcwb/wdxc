package com.ldz.job.service.impl;

import com.ldz.dao.biz.model.ClZdglReport;
import com.ldz.dao.biz.mapper.ClZdglReportMapper;
import com.ldz.job.service.ClZdglReportService;
import com.ldz.sys.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Service
public class ClZdglReportServiceImpl extends BaseServiceImpl<ClZdglReport, String> implements ClZdglReportService {

	@Autowired
	private ClZdglReportMapper baseMapper;
	
	@Override
	protected Mapper<ClZdglReport> getBaseMapper() {
		return baseMapper;
	}

	@Override
	public void saveBatch(List<ClZdglReport> list) {
		list.forEach(clZdglReport -> {
			clZdglReport.setId(genId());
		});
		baseMapper.insertList(list);
	}
}