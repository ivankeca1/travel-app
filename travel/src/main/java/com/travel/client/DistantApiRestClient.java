package com.travel.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DistantApiRestClient {

    public String getResponseFromDistantRestApi(final String param){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8082/api/v1/distant-rest-api/" + param, String.class);
    }
}
