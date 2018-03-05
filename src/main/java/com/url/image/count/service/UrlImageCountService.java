package com.url.image.count.service;

import com.url.image.count.model.UrlImageCount;
import com.url.image.count.model.UrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UrlImageCountService {

    private static final Logger logger = LoggerFactory.getLogger(UrlImageCountService.class);

    private RestTemplate restTemplate;

    public UrlImageCountService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * This gets the content of the given url
     * @param url the input url for which the content need to be fetched
     * @return the fetched url response
     * @throws InterruptedException
     */
    @Async
    public CompletableFuture<UrlResponse> getUrlContent(String url) throws InterruptedException {
        logger.info("Looking up {}", url);
        UrlResponse results = restTemplate.getForObject(url, UrlResponse.class);
        //setting up delay for demonstration purposes
        Thread.sleep((long)(Math.random() * 20000));
        return CompletableFuture.completedFuture(results);
    }

    /**
     * This process the url response after the immediate response is sent.
     * The image count for each url is persisted into a hashtable with the url as the key
     * @param input the input which has the url for which the content to be fetched
     * @param immediateResp the Future interface reference for the immediate response
     * @return the processed imagecount with the url in a hashtable
     */
    public Hashtable<String, String> processUrls(UrlImageCount input, CompletableFuture<UrlImageCount> immediateResp) {
        Hashtable<String, String> htUrlImgCount = new Hashtable<String, String>();
        input.getImageCountUrls().stream().forEach(url-> {
            immediateResp.thenApplyAsync(urlImageCount -> {
                try {
                    htUrlImgCount.put(url.getUrl(), "Pending");
                    //get url content and then count the image
                    getUrlContent(url.getUrl()).thenApplyAsync(urlResponse -> {
                        final AtomicInteger totalImgCount= new AtomicInteger();
                        urlResponse.getImages().stream().forEach(image -> {
                            totalImgCount.addAndGet(image.getTotal());
                            htUrlImgCount.put(url.getUrl(), String.valueOf(totalImgCount));
                        });
                        return urlImageCount;
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return urlImageCount;
            });
        });

        return htUrlImgCount;
    }

}
