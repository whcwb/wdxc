package com.ldz.znzp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.ldz.znzp.model.ClXlzd;

import tk.mybatis.mapper.common.Mapper;

public interface ClXlzdMapper extends Mapper<ClXlzd> {
	
	@Select("SELECT * FROM CL_XLzd WHERE xl_id = #{id}")
	@Results({
		@Result(property = "id", column = "ID"),   
		@Result(property = "zdId", column = "ZD_ID"),
		@Result(property = "cjsj", column = "CJSJ") 
	})   
    public List<ClXlzd> findStudents(String tid);

}