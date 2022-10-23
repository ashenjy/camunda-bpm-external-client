//package com.cn.camunda.external.tasks;
//
//import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
//import org.camunda.bpm.client.task.ExternalTask;
//import org.camunda.bpm.client.task.ExternalTaskHandler;
//import org.camunda.bpm.client.task.ExternalTaskService;
//import org.springframework.stereotype.Component;
//import org.camunda.bpm.engine.variable.VariableMap;
//import org.camunda.bpm.engine.variable.Variables;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Component
//@ExternalTaskSubscription("submitPaymentRequest")
//public class SubmitPaymentRequest implements ExternalTaskHandler {
//
//    private final Logger log = Logger.getLogger(SubmitPaymentRequest.class.getName());
//
//    @Override
//    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
//        String candidateId = "IN-" + (Math.random() * 100);
//        int candidateScore = (int) (Math.random() * 11);
//
//        VariableMap variables = Variables.createVariables();
//        variables.put("candidateId", candidateId);
//        variables.put("candidateScore", candidateScore);
//
//        externalTaskService.complete(externalTask, variables);
//
//        log.log(Level.INFO, "NOPE Rating Genrator External task is completed for Candidate {0} with rating {1}", new Object[]{candidateId, candidateScore});
//    }
//}
