package com.ldz.biz;

import com.ldz.sys.service.GnService;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

public class ServletInitializer extends SpringBootServletInitializer {


	private GnService gnService;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BizApplication.class);
	}

	// 全局缓存角色权限
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		this.logger = LogFactory.getLog(this.getClass());
		final WebApplicationContext rootAppContext = this.createRootApplicationContext(servletContext);
		if(rootAppContext != null) {
			servletContext.addListener(new ContextLoaderListener(rootAppContext) {
				public void contextInitialized(ServletContextEvent event) {
					gnService = rootAppContext.getBean(GnService.class);
					gnService.initPermission();
				}
			});
		} else {
			this.logger.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
		}
	}

}
