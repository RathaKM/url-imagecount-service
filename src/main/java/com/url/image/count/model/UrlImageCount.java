package com.url.image.count.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class UrlImageCount extends ResourceSupport {
    private  String jobId;
    private  List<Url> imageCountUrls;

    @JsonCreator
    public UrlImageCount(@JsonProperty("jobId") String jobId,  @JsonProperty("imageCountUrls") List<Url> imageCountUrls) {
        this.jobId = jobId;
        this.imageCountUrls = imageCountUrls;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setImageCountUrls(List<Url> imageCountUrls) {
        this.imageCountUrls = imageCountUrls;
    }

    public List<Url> getImageCountUrls() {
        return imageCountUrls;
    }

    @Override
    public String toString() {
        return "UrlImageCount{" +
                "jobId='" + jobId + '\'' +
                ", imageCountUrls=" + imageCountUrls +
                '}';
    }
}
