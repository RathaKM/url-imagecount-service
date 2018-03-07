## Url Image Count Service

This repo contains working code for a __Url Image Count service (SpringBoot with Java8)__. 

There are two different implementations provided in this application.
### Implementation I (for Live Urls)
- In this implementation the application provides a REST API (__/v1/imagecount__) that will accept a list of live URLs as JSON input. This call would return a Job Id immediately after invoked.
- The application’s back-end would then get the html contents for each URL and then count the total number of images found in that content by parsing the _&lt;img src="" >_ tag using regular expression. This would be done asynchronously. 
- The application also provides another REST API (__v1/imagecount/jobId/{jobId}__) with a Job ID as path parameter. 
   - This API will return a JSON response with the number of images found in the contents of each URL supplied for that job id.
   - The total image count for each url content will be displayed if that particular url request is completed
   - Otherwise the status 'Pending' will be displayed
- The urls in the request payload could be any live urls.
- This implementation uses _Executor_ interface for multithreading and _CompletableFuture_ for asynchronous programming for processing the urls, parsing and counting the images.

### Implementation II (for Demo Urls)
- This implementation provides the REST endpoint (__/v1/imagecount/demourl__) that will accept a list of demo urls as JSON input. This call would also return a Job Id immediately after invoked.
- For the purpose of demonstration this application also contains REST API for providing the demo Url contents. The contents available thro' the uri's '_/v1/url1/image, /v1/url2/image and /v1/url3/image_'. The response of these urls are not html contents. They are JSON response with <image> tags.   
- The same REST endpoint (__v1/imagecount/jobId/{jobId}__) with a Job ID as path parameter can be used to retrieve the job details. 
   - This will also return a JSON response with the number of images found in the contents of each URL supplied for that job id.
   - The total image count for each url content will be displayed if that particular url request is completed
   - Otherwise the status 'Pending' will be displayed
- The urls in the request payload could be any of the three demo (url1, url2, url3) urls.
- This one uses _@Async_ of Spring for url processing and the image count.
   
## Project Features

- Multithreaded & Asynchronous Spring Boot and Java 8 based REST implementation
- Layered approach
- Dependency Injection using Spring
- Input data and Service validation
- Testing
  - Unit Testing
  - Curl
  - Postman

## Technology Stack

| Description               | Tool/Framework    |
| --------------------------|-------------------|
| Java version              | JDK 1.8           |
| JAX-RS Implementation     | Spring Boot       |
| IDE                       | IntelliJ IDEA 14  |
| Build tool                | Gradle            |
| Platform                  | iOS               |

## Application Design

![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/url-imagecount-service.png)


## UrlImageCount Service Resources

| Resource Type                         | Resource URI                                          |    HTTP Method |
| --------------------------------------|------------------------------------------------------ |----------------|
| Create a Job (for Live Urls)          | /v1/imagecount                                        | POST           |
| Create a Job (for Demo Urls)          | /v1/imagecount/demourl                                | POST           |
| Get a Job Detail                      | /v1/imagecount/jobId/{jobId}                          | GET            |
| Get Url1 content                      | /v1/url1/image                                        | GET            |
| Get Url2 content                      | /v1/url2/image                                        | GET            |
| Get Url3 content                      | /v1/url3/image                                        | GET            |   

## Project Setup

### Service Setup

- Clone/fork this Repo and open/import this Project into IntelliJ or Eclipse
- Open a Terminal window and go to the project directory, '_url-imagecount-service_'
- Build/Package the project using '_gradle build_' command (use _gradle build -x test_ to skip the test)
- Start SpringBoot application using '_java -jar build/libs/imagecount-service-0.1.0.jar_' command
- Test the server by http://localhost:8080/v1/imagecount/jobId/1. This may display _{"errorMessage":"JobId 1 is not found"}_, as you have not created any Job already. 
- For a quick deployment you can use the shared [imagecount-service-0.1.0.jar](../master/imagecount-service-0.1.0.jar) file, in case you didn't have time or ran into any issues.
   - Simply go to the root folder and run the command '_java -jar build/libs/imagecount-service-0.1.0.jar_'

## How to Consume/Test the Resources

There are 2 ways you can consume these resources. They are by using Postman, and Curl command. 

### Using Postman

The resources can be consumed by using below Postman collection
- https://www.getpostman.com/collections/856315de6d32166e268f

#### For Live Urls
##### Create A Job for Live Url Image Count processing
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/post-liveurl-imagecounts.png)

##### Retrive A Job Detail which is Pending for some Live Url
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/get-liveurl-imagecount-pending.png)

##### Retrive A Job Detail which is fully completed for Live Url
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/get-liveurl-imagecount-completed.png)

#### For Demo Urls
##### Create A Job for Demo Url Image Count processing
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/post-demourl-imagecount.png)

##### Retrive A Job Detail which is Pending for some Demo Url
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/get-demourl-imagecount-pending.png)

##### Retrive A Job Detail which is fully completed for Demo Url
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/get-demourl-imagecount-completed.png)

#### Demo Service Urls

##### Retrieve Url1 content
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/url1-response.png)

##### Retrieve Url2 content
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/url2-response.png)

##### Retrieve Url3 content
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/url3-response.png)

### Validation

#### Retrieve Job Detail - Invalid Job Id
If the JobId is not available then an error message will be displayed
```
{
    "errorMessage": "JobId 11 is not found"
}
```

### Using Curl command
We can test the resources using below Curl command. The data files are available in the root folder [_liveurl_requestX.json or demourl_requestX.json_]

Please make sure that the Application is running before running this command.

#### Create a Job (HTTP POST call)
- Command: You can try with different data file (demourl_request2.json, demourl_request3.json)
  ```
  curl -H "Content-Type:application/json" -X POST -d @demourl_request1.json http://localhost:8080/v1/imagecount/demourl
  ```
- response
  ```
  {
          "jobId":"2",
          "imageCountUrls":[
                {
                  "url":"http://localhost:8080/v1/url1/image",
                  "imageCount":"Pending"
                },
                {
                  "url":"http://localhost:8080/v1/url2/image",
                  "imageCount":"Pending"
                }
            ],
            "_links":{
                "self":{
                    "href":"http://localhost:8080/v1/imagecount"
                 },
                 "get":{
                    "href":"http://localhost:8080/v1/imagecount/jobId/2"
                  },
                 "update":{
                    "href":"http://localhost:8080/v1/imagecount/jobId/2"
                  },
                 "delete":{
                    "href":"http://localhost:8080/v1/imagecount/jobId/2"
                  }
              }
          }
    ```
    
#### Retrieve a Job (HTTP GET call)
- Command:
```curl -H "Content-Type:application/json" -X GET http://localhost:8080/v1/imagecount/jobId/1 ```
- Response
  ```
  {
  "jobId": "1",
  "imageCountUrls": [
    {
      "url": "http://localhost:8080/v1/url1/image",
      "imageCount": "60"
    },
    {
      "url": "http://localhost:8080/v1/url2/image",
      "imageCount": "130"
    }
  ],
  "_links": {
    "self": {
      "href": "http://localhost:8080/v1/imagecount/jobId/1"
    },
    "update": {
      "href": "http://localhost:8080/v1/imagecount/jobId/1"
    },
    "delete": {
      "href": "http://localhost:8080/v1/imagecount/jobId/1"
    }
  }
  ```

## Unit Tests

The unit tests are added mainly for Controller, and Service layers. These tests will be run as part of the '_gradle build_' command
 
