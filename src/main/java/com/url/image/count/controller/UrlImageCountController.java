package com.url.image.count.controller;

import com.url.image.count.exception.CustomErrorType;
import com.url.image.count.model.Url;
import com.url.image.count.model.UrlImageCount;
import com.url.image.count.service.UrlImageCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/imagecount")
public class UrlImageCountController {

    //to generate the unique jobId
    private final AtomicLong jobId = new AtomicLong();
    //to maintain the job specific details of url and image count
    private Hashtable<String, Hashtable> jobDetails = new Hashtable();
    @Autowired
    private UrlImageCountService imageCountService;

    public static final Logger logger = LoggerFactory.getLogger(UrlImageCountController.class);

    /**
     * This returns response of the image count for each Url
     * @param jobId the job id for retrieving the image count
     * @return the response of the image count for each Url
     */
    @RequestMapping(method = RequestMethod.GET, value = "/jobId/{jobId}")
    public ResponseEntity<UrlImageCount> getUrlImageCount(@PathVariable String jobId) {

        //Validate the JobId
        if (!jobDetails.containsKey(jobId)) {
            logger.error("JobId {} is not found.", jobId);
            return new ResponseEntity(new CustomErrorType("JobId " + jobId
                    + " is not found"), HttpStatus.NOT_FOUND);
        }
        List listUrl = new ArrayList();
        Hashtable<String, String> imageCountDetails = jobDetails.get(jobId);

        //prepare the response url list
        imageCountDetails.entrySet().stream().forEach(entry-> listUrl.add(new Url(entry.getKey(), entry.getValue())));

        //update the response object
        UrlImageCount urlImageCount = new UrlImageCount(jobId, listUrl);

        //set Hateos link
        addHateosLinksForGet(urlImageCount, jobId);
        return new ResponseEntity<UrlImageCount>(urlImageCount, HttpStatus.OK);
    }

    /**
     * This returns the immediate response with the JobId and the pending status for each Url.
     * And then it starts processing the image count for each Url in an asynchronous and multithreaded way.
     * @param input the list of Url
     * @return the the immediate response with the JobId and the pending status for each Url
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @RequestMapping(value = "/demourl", method = RequestMethod.POST)
    public @ResponseBody CompletableFuture<UrlImageCount> countUrlImage(@RequestBody UrlImageCount input) throws InterruptedException, ExecutionException {
        String newJobId = String.valueOf(jobId.incrementAndGet());
        logger.info("input: {}", input);
        Hashtable htUrlImgCount = new Hashtable();

        input.setJobId(newJobId);
        input.getImageCountUrls().stream().forEach(url->url.setImageCount("Pending"));
        //set Hateos link
        addHateosLinksForPost(input, newJobId);

        //prepare for the immediate response with JobId
        CompletableFuture<UrlImageCount> immediateResp = new CompletableFuture<UrlImageCount>();

        //send the immediate response with the JobId
        immediateResp.complete(input);

        //continue the process of counting the image in each url
        htUrlImgCount = imageCountService.processUrls(input, immediateResp);
        jobDetails.put(newJobId, htUrlImgCount);

        return immediateResp;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody CompletableFuture<UrlImageCount> createJob(@RequestBody UrlImageCount input) throws InterruptedException, ExecutionException, IOException {
        String newJobId = String.valueOf(jobId.incrementAndGet());
        logger.info("input: {}", input);
        Hashtable htUrlImgCount = new Hashtable();

        input.setJobId(newJobId);
        input.getImageCountUrls().stream().forEach(url->url.setImageCount("Pending"));
        //set Hateos link
        addHateosLinksForPost(input, newJobId);

        //prepare for the immediate response with JobId
        CompletableFuture<UrlImageCount> immediateResp = new CompletableFuture<UrlImageCount>();

        //send the immediate response with the JobId
        immediateResp.complete(input);

        //continue the process of counting the image in each url
        htUrlImgCount = imageCountService.processUrlsAndCountImage(input, immediateResp);
        jobDetails.put(newJobId, htUrlImgCount);
        logger.info("jobDetails: {}", jobDetails.toString());
        return immediateResp;
    }

    private void addHateosLinksForPost(UrlImageCount input, String jobId) {
        try {
        input.add(linkTo(methodOn(UrlImageCountController.class).countUrlImage(input)).withSelfRel());
        input.add(linkTo(methodOn(UrlImageCountController.class).getUrlImageCount(jobId)).withRel("get"));
        input.add(linkTo(methodOn(UrlImageCountController.class).getUrlImageCount(jobId)).withRel("update"));
        input.add(linkTo(methodOn(UrlImageCountController.class).getUrlImageCount(jobId)).withRel("delete"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void addHateosLinksForGet(UrlImageCount input, String jobId) {
        input.add(linkTo(methodOn(UrlImageCountController.class).getUrlImageCount(jobId)).withSelfRel());
        input.add(linkTo(methodOn(UrlImageCountController.class).getUrlImageCount(jobId)).withRel("update"));
        input.add(linkTo(methodOn(UrlImageCountController.class).getUrlImageCount(jobId)).withRel("delete"));
    }
}