package com.url.image.count.service;

import com.url.image.count.model.Url;
import com.url.image.count.model.UrlImageCount;
import com.url.image.count.model.UrlResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlImageCountServiceTest {

//    @InjectMocks
//    private UrlImageCountService urlImageCountService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    RestTemplateBuilder restTemplateBuilder;
    @InjectMocks @Spy UrlImageCountService urlImageCountService;
    @Mock
    private UrlResponse result;

    @Before
    public void before() {
        urlImageCountService = new UrlImageCountService(restTemplateBuilder);

        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetUrlCount() throws Exception {
        Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.anyObject())).thenReturn(result);
        assertNotNull(urlImageCountService.getUrlContent(Mockito.anyString()));
    }
}
