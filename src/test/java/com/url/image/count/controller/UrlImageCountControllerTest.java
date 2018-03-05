package com.url.image.count.controller;

import com.url.image.count.model.Url;
import com.url.image.count.model.UrlImageCount;
import com.url.image.count.service.UrlImageCountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlImageCountControllerTest {

    @InjectMocks
    private UrlImageCountController urlImageCountController;
    @Mock
    private Hashtable<String, String> imageCountDetails;
    @Mock
    private UrlImageCount input;
    @Mock
    private List<Url> listUrl;
    @Mock
    private UrlImageCountService imageCountService;
    @Mock
    private Hashtable jobDetails;
    @Mock
    private Hashtable htUrlImgCount;
    @Mock
    Stream<Url> stream;

    @Mock
    private CompletableFuture<UrlImageCount> immediateResp;

    @Test
    public void testGetUrlImageCount() throws Exception {
        Mockito.when(jobDetails.containsKey(Mockito.anyString())).thenReturn(true);
        Mockito.when(jobDetails.get(Mockito.anyString())).thenReturn(imageCountDetails);
        ResponseEntity<UrlImageCount> urlImageCountResp =  urlImageCountController.getUrlImageCount(Mockito.anyString());
        assertEquals(HttpStatus.OK, urlImageCountResp.getStatusCode());
    }

    @Test
    public void testGetUrlImageCountError() throws Exception {
        Mockito.when(jobDetails.containsKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(jobDetails.get(Mockito.anyString())).thenReturn(imageCountDetails);
        ResponseEntity<UrlImageCount> urlImageCountResp =  urlImageCountController.getUrlImageCount(Mockito.anyString());
        assertEquals(HttpStatus.NOT_FOUND, urlImageCountResp.getStatusCode());
    }

    @Test
    public void testCountUrlImage() throws Exception {
        Mockito.when(input.getImageCountUrls()).thenReturn(listUrl);
        Mockito.when(listUrl.stream()).thenReturn(stream);
        Mockito.when(imageCountService.processUrls(input, immediateResp)).thenReturn(htUrlImgCount);
        assertNotNull(urlImageCountController.countUrlImage(input));
    }
}
