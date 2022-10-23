package com.cn.camunda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.camunda.community.rest.client.api.DeploymentApi;
import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableProcessApplication("submit-payment-request")
public class CamundaExternalClientApp {

//  @Autowired
//  RuntimeService runtimeService;

    @Value("${camunda.uri}")
    private String camundaBaseUri;

    public static void main(String... args) {
        SpringApplication.run(CamundaExternalClientApp.class, args);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

//    @Bean("apiClient")
//    public ApiClient apiClient() {
//        return new ApiClient().setBasePath(camundaBaseUri);
//    }
//
//    @Bean
//    public ProcessDefinitionApi processDefinitionApi(@Qualifier("apiClient") ApiClient apiClient) {
//        return new ProcessDefinitionApi();
//
//    }
//
//    @Bean
//    public DeploymentApi deploymentApi(@Qualifier("apiClient") ApiClient apiClient) {
//        return new DeploymentApi();
//
//    }

//    @Bean
//    public ApiClient apiClient() {
////        ApiClient client = new ApiClient(new OkHttpClient.Builder()
////                .addInterceptor(chain -> {
////                    Request request = chain.request();
////                    Request authenticatedRequest = request.newBuilder()
////                            .header("Authorization","Bearer asdas").build();
////                    return chain.proceed(authenticatedRequest);
////                })
////                .build());
//        ApiClient client = new ApiClient();
//        if (this.camundaBaseUri != null) {
//            client.setBasePath(this.camundaBaseUri);
//        }
//        return client;
////                .addDefaultHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huIiwicGFzc3dvcmQiOiJ7YmNyeXB0fSQyYSQxMCR4emNTbVp2cGYudFVPN0FQd3JzbXguTUdueXoxV2NJZjVnMExYR3ZpV0VncDdxNEdtM0ZFQyIsImF1dGhvcml0aWVzIjpbIlJPTEVfY2FtdW5kYS11c2VyIl0sImlhdCI6MTY2NjUyNzI3MSwiZXhwIjoxNjY2NTQ1MjcxfQ.7cPpIUfT_y29sZK0WUu6RGwb9zv-5_csWr3d746WnxfJxUSekeYHrVSoY5y88QsN279Iz7WrQRDIzzBTwNAZRw");
//    }

//  @Bean
//  public ProcessDefinitionApi processDefinitionApi() {
//    return new ProcessDefinitionApi();
//  }
//   This is only used for testing purposes
//  @Bean
//  public CommandLineRunner createDemoProcessInstance(){
//
//    return (String[] args) -> runtimeService.startProcessInstanceByKey("submit-payment-request");
//
//  }
}
