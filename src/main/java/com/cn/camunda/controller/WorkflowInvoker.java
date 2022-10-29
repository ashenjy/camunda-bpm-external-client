package com.cn.camunda.controller;

import com.cn.camunda.auth.jwt.util.JwtTokenUtil;
import com.cn.camunda.services.CamundaDeploymentService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.community.rest.client.api.DeploymentApi;
import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.dto.ProcessInstanceWithVariablesDto;
import org.camunda.community.rest.client.dto.StartProcessInstanceDto;
import org.camunda.community.rest.client.dto.VariableValueDto;
import org.camunda.community.rest.client.invoker.ApiClient;
import org.camunda.community.rest.client.invoker.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/v1/{key}")
public class WorkflowInvoker {

    @Autowired
    private ProcessDefinitionApi processDefinitionApi;

    @Autowired
    private ApiClient apiClient;

    //    @Autowired
//    DeploymentApi deploymentApi;
    @Autowired
    private CamundaDeploymentService camundaDeploymentService;

    @Autowired
    private Gson gsonParser;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${camunda.uri}")
    private String camundaBaseUri;

    @PostMapping(
            value = "/start",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public String start(@PathVariable String key,
                        @RequestBody String requestBody,
//                        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
                        HttpServletRequest httpRequest) throws Exception {
        try {

            String jwtToken = (httpRequest.getHeader("Authorization") == null) ? jwtTokenUtil.parseJwtFromCookie(httpRequest) : httpRequest.getHeader("Authorization");

            if (jwtToken != null) {
                if (!jwtToken.startsWith("Bearer ")) {
                    log.warn("Client.WorkflowInvoker.start().JWT Token doesn't start with Bearer ");
                }
            }else{
                log.error("Client.WorkflowInvoker.start().JWT Token required!");
                //TODO: throw jwt exception - bad request
            }
            //set Basic HTTP Authentication
//            apiClient.addDefaultHeader("Authorization", getBasicAuthenticationHeader(username, password));
//            ApiClient apiClient = new ApiClient();
            apiClient.setBasePath(camundaBaseUri);
            //set JWT Authentication
            apiClient.addDefaultHeader("Authorization", jwtToken);

            //set
            processDefinitionApi.setApiClient(apiClient);

            //TODO: deploy specific process
            camundaDeploymentService.deployCamundaResources();

            Map jsonToMap = gsonParser.fromJson(requestBody, Map.class);

            VariableValueDto body = new VariableValueDto();
            body.setValue(jsonToMap);

            StartProcessInstanceDto startProcessInstanceDto = new StartProcessInstanceDto();
            //TODO: handle NULL response
            ProcessInstanceWithVariablesDto resDto = processDefinitionApi.startProcessInstanceByKey(key,
                    startProcessInstanceDto).putVariablesItem("NATIVE_REQUEST", body);
            return gsonParser.toJson(resDto);

        } catch (ApiException e) {
            //TODO: Response 401 message
            if (StringUtils.isEmpty(e.getMessage())) {
                if (e.getCode() == 401) {
                    return gsonParser.toJson("{\n" +
                            "    \"status\": 401,\n" +
                            "    \"Internal Camunda Engine Auth Error\": \"Unauthorized\"\n" +
                            "}");
                }
            }
            log.error(e.getMessage());
            return e.getMessage();
        }
    }

    private static String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
