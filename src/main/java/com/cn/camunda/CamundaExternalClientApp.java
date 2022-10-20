package com.cn.camunda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableProcessApplication("submit-payment-request")
public class CamundaExternalClientApp {

//  @Autowired
//  RuntimeService runtimeService;

  public static void main(String... args) {
    SpringApplication.run(CamundaExternalClientApp.class, args);
  }

  @Bean
  public Gson gson() {
    return new GsonBuilder().setPrettyPrinting().create();
  }


//   This is only used for testing purposes
//  @Bean
//  public CommandLineRunner createDemoProcessInstance(){
//
//    return (String[] args) -> runtimeService.startProcessInstanceByKey("submit-payment-request");
//
//  }
}
