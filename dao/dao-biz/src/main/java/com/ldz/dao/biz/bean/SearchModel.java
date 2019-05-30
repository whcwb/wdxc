package com.ldz.dao.biz.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchModel implements Serializable {

    @JsonProperty(value = "List")
    private List<EventModel> List;

}
