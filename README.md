## Url Image Count Service

This repo contains working code for a __Url Image Count service (SpringBoot with Java8)__. 

- This application provides a REST API (__v1/imagecount__) that will accept a list of URLs as JSON input. This call would return a Job Id immediately after invoked.
- The application’s back-end would then get the contents for each URL and count the total number of images found in that content by parsing for all the image tags.This would be done asynchronously.
- The application also provides another REST API (__v1/imagecount/jobId/10__) with a Job ID as input parameter. This API will return a JSON response with the number of images found in the contents of each URL supplied for that job id.
   - The total image count for each url will be displayed if that particular url request is completed
   - Otherwise the status 'Pending' will be displayed

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


## Reminder Service Resources

| Resource Type                         | Resource URI                                          |    HTTP Method |
| --------------------------------------|------------------------------------------------------ |----------------|
| Create a Job                          | /v1/imagecount                                        | POST           |
| Get a Job Detail                      | /v1/imagecount/jobId/1                                | GET            |
| Get Url1 content                      | /v1/url1/image                                        | GET            |
| Get Url2 content                      | /v1/url2/image                                        | GET            |
| Get Url3 content                      | /v1/url3/image                                        | GET            |   

## Project Setup

### Service Setup

- Clone/fork this Repo and open/import this Project into IntelliJ or Eclipse
- Go to the project directory, '_url-imagecount-service_'
- Build/Package the project using '_gradle build_' command
- Start SpringBoot application using '_java -jar build/libs/imagecount-service-0.1.0.jar_' command
- Test the server by http://localhost:8080/v1/imagecount/jobId/1. This may display _{"errorMessage":"JobId 1 is not found"}_, if you have not created any Job already. 

## How to Consume Resources

There are 2 ways you can consume these resources. They are using Postman, and Curl command. 

### Using Postman

The resources can be consumed by using below Postman collection
- https://www.getpostman.com/collections/856315de6d32166e268f

#### Create A Job for Url Image Count processing
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/post-imagecount.png)


#### Retrive A Job Detail which is Pending for some Url
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/get-imagecount-pending.png)

#### Retrive A Job Detail which is fully completed
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/get-imagecount-completed.png)

#### Retrieve Url1 content
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/url1-response.png)

#### Retrieve Url2 content
![alt txt](https://github.com/RathaKM/url-imagecount-service/blob/master/src/main/resources/images/url2-response.png)

#### Retrieve Url3 content
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
We can test the resources using below Curl command. The data files are available in the root folder [_requestX.json_]

Please make sure that the Application is running before running this command.

#### Create a Job (HTTP POST call)
- Command: You can try with different data file (request2.json, request3.json)
  ```
  curl -H "Content-Type:application/json" -X POST -d @request1.json http://localhost:8080/v1/imagecount
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

## How to Run Tests

There are some unit tests added into this repo

### Unit Tests

The unit tests are added mainly for Controller, and Service layers. These tests will be run as part of the '_gradle build_' command
 
