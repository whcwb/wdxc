package com.ldz.sys.util;

import com.ldz.util.exception.RuntimeCheck;

public class OrgUtil {

    public static int getLevel(String orgCode){
        RuntimeCheck.ifBlank(orgCode,"机构代码不能为空");
        return orgCode.length()/3;
    }

    public static void main(String[] args) {
    }

}
