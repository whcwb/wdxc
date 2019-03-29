package com.ldz.wechat.base;

import com.ldz.util.bean.SimpleCondition;
import com.ldz.wechat.exception.RuntimeCheck;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenwei
 * @copyright
 * @category
 * @since 2018/2/8
 */
public class LimitedCondition extends SimpleCondition {

    /**
     * 有些对象在查询的时候并不需要做机构权限处理
     */
    private static final List<String> excludeEntityName = Arrays.asList("SysZdlm","SysFw","SysGn","SysYjfk","SysRz","ClZdgl","ClZnzp");

    public LimitedCondition(Class<?> entityClass) {
        super(entityClass);
        if (excludeEntityName.contains(entityClass.getSimpleName()))return;
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String orgCode = (String) request.getAttribute("orgCode");
//        RuntimeCheck.ifBlank(orgCode,"未找到机构");
//        this.and().andLike("jgdm",orgCode+"%");
    }
}
