package com.url.image.count.service;

import com.url.image.count.model.Image;
import com.url.image.count.model.UrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Service
public class UrlService {

    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);

    public UrlResponse getUrlResponse(String url) {
        UrlResponse urlResp = new UrlResponse(getContent().get(url));
        return urlResp;
    }

    private Hashtable<String, List<Image>> getContent() {
        Image img1 = new Image(15, 0);
        Image img2 = new Image(20, 0);
        Image img3 = new Image(25, 0);
        Image img4 = new Image(30, 0);
        Image img5 = new Image(40, 0);
        Image img6 = new Image(60, 0);
        Image img7 = new Image(70, 0);
        List<Image> listImage1 = new ArrayList<Image>();
        List<Image> listImage2 = new ArrayList<Image>();
        List<Image> listImage3 = new ArrayList<Image>();

        listImage1.add(img1); listImage1.add(img2); listImage1.add(img3);
        listImage2.addAll(listImage1);listImage2.add(img4); listImage2.add(img5);
        listImage3.addAll(listImage2);listImage3.add(img6); listImage3.add(img7);

        Hashtable<String, List<Image>> imagesMap = new Hashtable<String, List<Image>>();
        imagesMap.put("url1", listImage1);
        imagesMap.put("url2", listImage2);
        imagesMap.put("url3", listImage3);
       return imagesMap;
    }

}
