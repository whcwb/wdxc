package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.model.Clyy;
import com.ldz.sys.base.BaseService;

import java.util.List;

public interface ClYyService extends BaseService<Clyy,String> {

    void saveBatch(List<Clyy> clyys);

}
