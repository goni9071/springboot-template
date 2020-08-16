
package com.maumjido.springboot.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.maumjido.springboot.template.aop.ControllerAspect;
import com.maumjido.springboot.template.aop.DaoAspect;
import com.maumjido.springboot.template.aop.ServiceAspect;

@Configuration
public class AopConfig {

  @Bean
  public ControllerAspect controllerAspect() {
    return new ControllerAspect();
  }

  @Bean
  public DaoAspect daoAspect() {
    return new DaoAspect();
  }

  @Bean
  public ServiceAspect serviceAspect() {
    return new ServiceAspect();
  }
}
