package com.cn.camunda;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableProcessApplication("bpm-run")
public class ClientApplication {

  @Autowired
  RuntimeService runtimeService;

  public static void main(String... args) {
    SpringApplication.run(ClientApplication.class, args);
  }

//   This is only used for testing purposes
  @Bean
  public CommandLineRunner createDemoProcessInstance(){

    return (String[] args) -> runtimeService.startProcessInstanceByKey("bpm-run");

  }
}
