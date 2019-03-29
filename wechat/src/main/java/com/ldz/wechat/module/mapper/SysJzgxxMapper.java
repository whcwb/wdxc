package com.ldz.wechat.module.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ldz.wechat.module.model.SysJzgxx;

import tk.mybatis.mapper.common.Mapper;

public interface SysJzgxxMapper extends Mapper<SysJzgxx> {
	
	@Select("SELECT * from SYS_JZGXX t WHERE t.XM= #{xm} AND t.ZJHM= #{zjhm} ")
	SysJzgxx findJzg(@Param("xm") String xm,@Param("zjhm") String zjhm);

	@Select("SELECT * from SYS_JZGXX t WHERE t.ID= #{id} ")
	SysJzgxx findById(@Param("id") String id);

}