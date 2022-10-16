# Steps for OpenApi/Swagger in Spring Boot Camunda
Step 1: Create a client
Step 2: Download jar from jfrog: https://camunda.jfrog.io/ui/native/camunda-bpm/org/camunda/bpm/camunda-engine-rest-openapi/
Step 3: Extract the jar and add openapi json file to client resources folder
Step 4: Add open-api-generator-maven-plugin with require input directory path and other configurations
Step 5: Add dependencies required by openapi json, and test related libraries - refer pom.xml of camunda-client
Step 6: Do : mvn clean install, verify the code (api, model, auth, apiclient) is generated
Step 7: Define port and camunda engine base-url/rest or base-url/engine-rest in application.yml, will be utilised in Step 8
Step 8: Create configuration with Bean of api client, and require utility, example - CamundaClientConfiguration.java
Step 9: write required methods and in our use case we expose it in ProcessDefinitionUtilityController in the client
Step 10: Do clean install, then run the client application and the camunda engine
Step 11: Test the functionality by hitting /client/start api of client and verify the instance is created in camunda engine for testCaseSample workflow