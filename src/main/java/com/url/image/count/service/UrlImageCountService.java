package com.url.image.count.service;

import com.url.image.count.model.UrlImageCount;
import com.url.image.count.model.UrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlImageCountService {

    private static final Logger logger = LoggerFactory.getLogger(UrlImageCountService.class);
    private RestTemplate restTemplate;
    private static final String HTML_IMG_TAG_PATTERN = "(?i)<img([^>]+)>(.+?)";
    private Pattern patternTag;
    private Matcher matcherTag;

    public UrlImageCountService(RestTemplateBuilder restTemplateBuilder) {
        patternTag = Pattern.compile(HTML_IMG_TAG_PATTERN);
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

    /**
     * This gets the content of the given url
     * @param url the input url for which the content need to be fetched
     * @return the image count of the given url
     * @throws InterruptedException
     */
    public int getUrlContentAndParse(String url) throws IOException {
        logger.info("Looking up {}", url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class, "1");
        int imgCount = 0;
        if (response.getStatusCode() == HttpStatus.OK) {
            String resp = new String(response.getBody());
            matcherTag = patternTag.matcher(resp);
            while (matcherTag.find()) {
                imgCount++;
            }
        logger.info("the iamge count for {} is {}", url, imgCount);
        }
        return imgCount;
    }

    /**
     * This process the url response after the immediate response is sent.
     * The image count for each url is persisted into a hashtable with the url as the key
     * @param input the input which has the url for which the content to be fetched
     * @param immediateResp the Future interface reference for the immediate response
     * @return the processed imagecount with the url in a hashtable
     */
    public Hashtable<String, String> processUrlsAndCountImage(UrlImageCount input, CompletableFuture<UrlImageCount> immediateResp) {
        Hashtable<String, String> htUrlImgCount = new Hashtable<String, String>();
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(10);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ExecutorService executor = new ThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS, queue, threadFactory, rejectedHandler);

        input.getImageCountUrls().stream().forEach(url-> {
            immediateResp.thenApplyAsync(urlImageCount -> {
                htUrlImgCount.put(url.getUrl(), "Pending");
                try {
                    //get url content and then count the image
                    int imageCount = getUrlContentAndParse(url.getUrl());
                    htUrlImgCount.put(url.getUrl(), String.valueOf(imageCount));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return urlImageCount;
            }, executor);
        });
        logger.info("the url and the iamge count: {}", htUrlImgCount);
        return htUrlImgCount;
    }

    /*
     * A handler for tasks that cannot be executed by a ThreadPoolExecutor.
     * The handler to use when execution is blocked
     */
    RejectedExecutionHandler rejectedHandler = new RejectedExecutionHandler() {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            logger.info("Task Rejected:" + r.toString() + "::" + executor.toString());
        }
    };
}
