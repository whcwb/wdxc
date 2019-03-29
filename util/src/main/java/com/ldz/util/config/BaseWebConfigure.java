package com.ldz.util.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring基础配置
 * @author 李彬彬
 *
 */
@Configuration
public class BaseWebConfigure extends WebMvcConfigurerAdapter {

	/**
	 * 扩展配置Spring消息转换器
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {		
		super.configureMessageConverters(converters);
		StringHttpMessageConverter string = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		List<MediaType> stringSupportedMediaTypes = new ArrayList<MediaType>();
		stringSupportedMediaTypes.add(MediaType.TEXT_PLAIN);
		string.setSupportedMediaTypes(stringSupportedMediaTypes);
		
		MappingJackson2HttpMessageConverter json = new MappingJackson2HttpMessageConverter();
		json.setDefaultCharset(Charset.forName("UTF-8"));
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		//supportedMediaTypes.add(MediaType.TEXT_PLAIN);
		json.setSupportedMediaTypes(supportedMediaTypes);
		//格式化日期对象。如果字段中有Date类型字段，自动转换为String格式的字符串，防止转成long类型传递至前台
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		// null替换为""
		mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
				arg1.writeString("");
			}
		});
		json.setObjectMapper(mapper);
		converters.add(json);
		converters.add(string);
	}
}
