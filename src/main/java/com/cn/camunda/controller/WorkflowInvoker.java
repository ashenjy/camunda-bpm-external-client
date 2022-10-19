package com.cn.camunda.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ProcessDefinitionApi;
import org.openapitools.client.model.ProcessInstanceWithVariablesDto;
import org.openapitools.client.model.StartProcessInstanceDto;
import org.openapitools.client.model.VariableValueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/{key}")
public class WorkflowInvoker {

    @Autowired
    ProcessDefinitionApi processDefinitionApi;

    @Autowired
    ApiClient apiClient;

    @Autowired
    private Gson gsonParser;

    @PostMapping(
            value = "/start",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public String start(@PathVariable String key,
                        @RequestBody String requestBody,
                        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authToken) throws Exception {
        try {

            //set Basic HTTP Authentication
//            apiClient.addDefaultHeader("Authorization", getBasicAuthenticationHeader(username, password));
//            processDefinitionApi.setApiClient(apiClient);

            //set JWT Authentication
            apiClient.addDefaultHeader("Authorization", authToken);
            processDefinitionApi.setApiClient(apiClient);

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
