
package com.maumjido.springboot.template.config;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.maumjido.springboot.template.auth.CommonInterceptor;
import com.maumjido.springboot.template.util.JsonUtil;
import com.maumjido.springboot.template.util.JsonUtil.MyMixIn;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Bean
  public CommonInterceptor commonInterceptor() {
    return new CommonInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(commonInterceptor()).excludePathPatterns("/res/**");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/res/" + DefaultConstants.RESOURCE_VERSION + "/**").addResourceLocations("/").setCachePeriod(60 * 60 * 24 * 365);
  }

  @Bean
  public MappingJackson2HttpMessageConverter jackson2Converter() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(jsonMapper());
    return converter;
  }

  public interface DateFormatConstants {
    public static final String DEFAULT_FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_MIDDLE_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_SHORT_DATE_FORMAT = "yyyy-MM-dd";
  }

  @Bean
  public ObjectMapper jsonMapper() {
    ObjectMapper jsonMapper = new ObjectMapper();
    SimpleDateFormat df = new SimpleDateFormat(DateFormatConstants.DEFAULT_FULL_DATE_FORMAT);
    jsonMapper.setDateFormat(df);
    jsonMapper.setSerializationInclusion(Include.NON_EMPTY);
    SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
    FilterProvider filters = simpleFilterProvider//
        .addFilter("myMixIn", SimpleBeanPropertyFilter.serializeAllExcept(JsonUtil.EXCLUDE_WORD_IN_JSON));
    // and then serialize using that filter provider:
    jsonMapper.addMixIn(Object.class, MyMixIn.class);
    jsonMapper.setFilterProvider(filters);
    // Lazy Fetch를 막는다.
    // jsonMapper.registerModule(new Hibernate4Module());
    return jsonMapper;
  }
}