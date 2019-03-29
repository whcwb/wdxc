package com.ldz.sys.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 验证码配置类
 * @author Lee
 *
 */
@Component
public class KaptchaConfig {

	@Bean
    public DefaultKaptcha getDefaultKaptcha(){  
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();  
        properties.setProperty("kaptcha.border", "yes");  
        properties.setProperty("kaptcha.border.color", "105,179,90");  
        properties.setProperty("kaptcha.textproducer.font.color", "red");  
        properties.setProperty("kaptcha.image.width", "100");  
        properties.setProperty("kaptcha.image.height", "36");  
        properties.setProperty("kaptcha.textproducer.font.size", "32");  
        properties.setProperty("kaptcha.textproducer.char.length", "4");  
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,黑体,微软雅黑");
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        
        Config config = new Config(properties);  
        defaultKaptcha.setConfig(config);  
          
        return defaultKaptcha;  
    }  
}
