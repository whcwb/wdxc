package com.ldz.job.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.dao.biz.mapper.ClClMapper;
import com.ldz.dao.biz.model.ClCl;
import com.ldz.job.service.ClService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.bean.ApiResponse;

import tk.mybatis.mapper.common.Mapper;

@Service
public class ClServiceImpl extends BaseServiceImpl<ClCl, String> implements ClService {
	@Autowired
	private ClClMapper entityMapper;

	@Override
	protected Mapper<ClCl> getBaseMapper() {
		return entityMapper;
	}

	@Override
	public ApiResponse<String> updateNianshen() {

		List<ClCl> allcl = entityMapper.selectAll();
		for (ClCl clCl : allcl) {
			Date ccdjrq = clCl.getCcdjrq();
			if (ccdjrq!=null) {
				if (clCl.getZkl() < 7) {
					int pareYear = pareYear(ccdjrq);
					if (pareYear > 6) {
						Date paseDate = paseDate(ccdjrq, -1);
						clCl.setNssj(paseDate);
						update(clCl);
					}
					if (pareYear <= 6) {
						Date paseDate = paseDate(ccdjrq, pareYear);
						clCl.setNssj(paseDate);
						update(clCl);
					}

				} else {
					Date paseDate = paseDate(ccdjrq, -1);
					clCl.setNssj(paseDate);
					update(clCl);
				}
			}
	

		}

		return ApiResponse.success();
	}

	public Date paseDate(Date ccdjrq, int year) {
		Calendar calendar = Calendar.getInstance();
		int i = calendar.get(Calendar.YEAR);
        //标识每年都要年审
		if (year == -1) {
			year = i;
		} else {
			if (year == 0) {
				year = i + 2;
			}
			if (year == 1) {
				year = i;
			}
			if (year == 2) {
				year = i;
			}
			if (year == 3) {
				year = i - 2;
			}
			if (year == 4) {
				year = i - 2;
			}
			if (year == 5) {
				year = i - 4;
			}
			if (year == 6) {
				year = i - 4;
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(ccdjrq);

		String substring = date.substring(0, 4);

		String replace = date.replace(substring, String.valueOf(year));

		Date parse = null;
		try {
			parse = dateFormat.parse(replace);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		calendar.setTime(parse);

		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		String format = dateFormat.format(calendar.getTime());
		Date datetime = null;
		try {
			datetime = dateFormat.parse(format);
		} catch (ParseException e) {
		}

		return datetime;
	}

	public  int pareYear(Date ccdjrq) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

		String format = dateFormat.format(ccdjrq);
		String format1 = dateFormat.format(new Date());
		DateTime start = formatter.parseDateTime(format);

		DateTime end = formatter.parseDateTime(format1);

		Years year = Years.yearsBetween(start, end);
		return year.getYears();
	}
/*
	public static void main(String[] args) throws ParseException {
		
		String aString = " 2015-05-05 ";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date parse = dateFormat.parse(aString);
		int pareYear = pareYear(parse);
		System.out.println(pareYear);
		
	}*/
	
}
