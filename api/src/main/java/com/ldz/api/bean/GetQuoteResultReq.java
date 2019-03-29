package com.ldz.api.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetQuoteResultReq {
    private Integer Agent;
    private String CustKey;
    private String LicenseNo;
    private Long QuoteGroup;
    private String SecCode;
}
