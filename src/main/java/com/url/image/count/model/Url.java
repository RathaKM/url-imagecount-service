package com.url.image.count.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Url {
    private String url;
    private String imageCount;

    @JsonCreator
    public Url(@JsonProperty("url") String url, @JsonProperty("imageCount") String imageCount) {
        this.imageCount = imageCount;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageCount() {
        return imageCount;
    }

    public void setImageCount(String imageCount) {
        this.imageCount = imageCount;
    }

    @Override
    public String toString() {
        return "Url{" +
                "url='" + url + '\'' +
                ", imageCount='" + imageCount + '\'' +
                '}';
    }
}
