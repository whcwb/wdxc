package com.ldz.sys.mapper;

import com.ldz.sys.model.SysRz;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface SysPtrzMapper extends Mapper<SysRz> , InsertListMapper<SysRz> {
}