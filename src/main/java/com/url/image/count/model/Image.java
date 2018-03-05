package com.url.image.count.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {
    private int total;
    private long offset;


    @JsonCreator
    public Image(@JsonProperty("total")int total, @JsonProperty("offset") long offset) {
        this.total = total;
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}

