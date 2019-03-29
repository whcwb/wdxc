package com.ldz.wechat.module.service.impl;


import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.module.mapper.SysPtjgMapper;
import com.ldz.wechat.module.model.SysJg;
import com.ldz.wechat.module.service.JgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * auther chenwei create at 2018/2/26
 */
@Service
public class JgServiceImpl extends BaseServiceImpl<SysJg, String> implements JgService {
	@Autowired
	private SysPtjgMapper ptjgMapper;

	@Autowired
	private JgService jgService;

	@Override
	protected Mapper<SysJg> getBaseMapper() {
		return ptjgMapper;
	}


	@Override
	public SysJg findByOrgCode(String orgCode) {
		List<SysJg> jgs = findEq(SysJg.InnerColumn.jgdm, orgCode);
		if (jgs.size() == 0)
			return null;
		return jgs.get(0);
	}

}
