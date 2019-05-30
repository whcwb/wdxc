package com.ldz.dao.biz.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ResModel implements Serializable {
    private List<SuppleModel> list;

}
