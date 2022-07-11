package com.travel.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DistantApiCrypterClient {

    public String encryptLogs(final String key, final String inputFilePath, final String outputFilePath){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8083/encrypt?key=" + key + "&inputFilePath=" + inputFilePath + "&outputFilePath=" + outputFilePath, String.class);
    }

    public String decryptLogs(final String key, final String inputFilePath, final String outputFilePath){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8083/decrypt?key=" + key + "&inputFilePath=" + inputFilePath + "&outputFilePath=" + outputFilePath, String.class);
    }

}
