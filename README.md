# Camunda Engine OpenAPI REST External Client Spring Boot App

## Tested on versions

* Spring Boot: 2.5.4

## Goal

Camunda Engine OpenAPI REST External Client Spring Boot App

## Principle

Uses `ContainerBasedAuthenticationProvider` and default Spring Boot security configuration.

## Features



## Required configuration

The camunda endpoint in the `ApiClient` is by default `http://localhost:8080/engine-rest`, if you use a diffrent path then set it through *`setBasePath`* method of the `ApiClient`.

### Spring Boot Starter

* Wires the ApiClient and provide all API's
* Autodeploys all BPMN, DMN and form resources it finds on the classpath during startup.

Add this dependency:

```
    <dependency>
      <groupId>org.camunda.community</groupId>
      <artifactId>camunda-engine-rest-client-openapi-springboot</artifactId>
      <version>7.17.0</version>
    </dependency>
```

You can configure the Camunda *endpoint* via your `application.properties` and initialize the client been like this:

```
camunda.bpm.client.base-url: http://localhost:8080/engine-rest
```

And then simply inject the API, for example:

```
@RestController
public class ExampleRestEndpoint {

    @Autowired
    private ProcessDefinitionApi processDefinitionApi;

    @PutMapping("/start")
    public ResponseEntity<String> startProcess(ServerWebExchange exchange) throws ApiException {
        // ...

        // start process instance
        ProcessInstanceWithVariablesDto processInstance = processDefinitionApi.startProcessInstanceByKey(
                ProcessConstants.PROCESS_KEY,
                new StartProcessInstanceDto().variables(variables));
        // ...
```
#### Auto Deployment

You can auto-deploy resources from your project, like BPMN processes. As default, all `.bpmn`, `.dmn`, and `.form` files are picked up and deployed. The pattern for resource files can be configured:

```
camunda.autoDeploy.bpmnResources: 'classpath*:**/*.bpmn'
camunda.autoDeploy.dmnResources: 'classpath*:**/*.dmn'
camunda.autoDeploy.formResources: 'classpath*:**/*.form'
```

You can disable auto deployment (which is enabled by default):

```
camunda.autoDeploy.enabled: false
```

#### wrong version of the okhttp version Issue
https://github.com/camunda-community-hub/camunda-platform-7-rest-client-java/issues/1
The following method did not exist:

    okhttp3.RequestBody.create(Ljava/lang/String;Lokhttp3/MediaType;)Lokhttp3/RequestBody;

```
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.squareup.okhttp3</groupId>
      <artifactId>okhttp</artifactId>
      <version>4.9.0</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## Paths and expected responses.

* http://localhost:8181/hello will retrieve protected information and will fail (401) if no token is provided in the request.
* http://localhost:8181/ will redirect to http://localhost:8181/camunda/app/
* http://localhost:8181/camunda/app/* will enforce the HTTP Basic Auth login and redirect back to the webapps (http://localhost:8181/camunda/app/welcome/).
* http://localhost:8181/engine-rest/engine will retrieve the name of the camunda engines initialized ("default") and will fail (401) if no token is provided in the request.


## Steps for Manual OpenApi/Swagger in Spring Boot Camunda

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
