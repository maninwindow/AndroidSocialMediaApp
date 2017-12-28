package com.tn.utils;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
	
	
	
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        
        /**
         * Parse JsonString to Json Object
         * JSONObject jsonObject = JSON.parseObject(JsonString);
         * 
         * Parse Json Object to JsonString
         * String jsonString = JSON.toJSONString(Object); 
         */
        
        //自定义配置...
        //FastJsonConfig config = new FastJsonConfig();
        //config.set ...
        //converter.setFastJsonConfig(config);
        converters.add(converter);
    }
}
