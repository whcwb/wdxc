package com.ldz.sys.service.impl;


import com.ldz.sys.mapper.JgZdLogMapper;
import com.ldz.sys.model.JgZdLog;
import com.ldz.sys.service.JgZdLogService;
import com.ldz.sys.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class JgZdLogServiceImpl extends BaseServiceImpl<JgZdLog, String> implements JgZdLogService {

	@Autowired
	private JgZdLogMapper baseMapper;
	
	@Override
	protected Mapper<JgZdLog> getBaseMapper() {
		return baseMapper;
	}
}