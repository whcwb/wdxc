package com.ldz.util.bean;

import org.apache.commons.lang.StringUtils;

/**
 * @author chenwei
 * @copyright
 * @category
 * @since 2018/2/8
 */
public class QueryCondition extends SimpleCondition {

    public QueryCondition(Class<?> entityClass,String unitCode) {
        super(entityClass);
        if (StringUtils.isNotBlank(unitCode)){
            int unitLevel = unitCode.charAt(0) - 48;
            String likeVal = "_" + unitCode.substring(1,unitLevel * 3+1);
            likeVal += getUnderlines((4 - unitLevel) * 3);
            like("jgdm",likeVal);
        }
    }

    private String getUnderlines(int n){
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < n; i++) {
            r.append("_");
        }
        return r.toString();
    }
}
