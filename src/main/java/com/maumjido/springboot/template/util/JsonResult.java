package com.maumjido.springboot.template.util;

import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class JsonResult extends MappingJacksonValue {
  public JsonResult(Object value) {
    super(value);
    SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
    FilterProvider filters = simpleFilterProvider//
        .addFilter("dataFilter", SimpleBeanPropertyFilter.serializeAllExcept(JsonUtil.EXCLUDE_WORD_IN_JSON))//
        .addFilter("myMixIn", SimpleBeanPropertyFilter.serializeAllExcept(JsonUtil.EXCLUDE_WORD_IN_JSON));
    super.setFilters(filters);
  }

  public JsonResult() {
    super("");
    super.setValue(this);
    SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
    FilterProvider filters = simpleFilterProvider//
        .addFilter("dataFilter", SimpleBeanPropertyFilter.serializeAllExcept(JsonUtil.EXCLUDE_WORD_IN_JSON))//
        .addFilter("myMixIn", SimpleBeanPropertyFilter.serializeAllExcept(JsonUtil.EXCLUDE_WORD_IN_JSON));
    super.setFilters(filters);
  }

  @Override
  @JsonIgnore
  public Object getValue() {
    return super.getValue();
  }

  @Override
  @JsonIgnore
  public Class<?> getSerializationView() {
    return super.getSerializationView();
  }

  @Override
  @JsonIgnore
  public FilterProvider getFilters() {
    return super.getFilters();
  }

  /**
   * 해당 키워드만 포함
   * 
   * @param keyword
   */
  public void includeFilter(String... keyword) {
    SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
    FilterProvider filters = simpleFilterProvider//
        .addFilter("dataFilter", SimpleBeanPropertyFilter.filterOutAllExcept(keyword))//
        .addFilter("myMixIn", SimpleBeanPropertyFilter.serializeAllExcept(JsonUtil.EXCLUDE_WORD_IN_JSON));
    super.setFilters(filters);
  }

  /**
   * 해당 키워드 제외
   * 
   * @param keyword
   */
  public void excludeFilter(String... keyword) {
    SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
    FilterProvider filters = simpleFilterProvider//
        .addFilter("dataFilter", SimpleBeanPropertyFilter.serializeAllExcept(keyword))//
        .addFilter("myMixIn", SimpleBeanPropertyFilter.serializeAllExcept(JsonUtil.EXCLUDE_WORD_IN_JSON));
    super.setFilters(filters);
  }

  public enum Code {
    SUCC, FAIL, EXPIRE
  }

  private Code code;
  private String msg;
  private String url;
  @JsonFilter("dataFilter")
  private Object data;

  public Code getCode() {
    return code;
  }

  public void setCode(Code code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
