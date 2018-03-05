package com.url.image.count.controller;

import com.url.image.count.model.UrlResponse;
import com.url.image.count.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @RequestMapping("/v1/url1/image")
    public ResponseEntity getUrl1() {
        UrlResponse urlResp = urlService.getUrlResponse("url1");
        return new ResponseEntity<UrlResponse>(urlResp, HttpStatus.OK);
    }

    @RequestMapping("/v1/url2/image")
    public ResponseEntity getUrl2() {
        UrlResponse urlResp = urlService.getUrlResponse("url2");
        return new ResponseEntity<UrlResponse>(urlResp, HttpStatus.OK);
    }

    @RequestMapping("/v1/url3/image")
    public ResponseEntity getUrl3() {
        UrlResponse urlResp = urlService.getUrlResponse("url3");
        return new ResponseEntity<UrlResponse>(urlResp, HttpStatus.OK);
    }

}