package com.ldz.job.service;

import com.ldz.dao.biz.model.ClZdglReport;
import com.ldz.sys.base.BaseService;

import java.util.List;

public interface ClZdglReportService extends BaseService<ClZdglReport, String> {

     void saveBatch(List<ClZdglReport> list);

}