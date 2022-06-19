package com.travel.controller;

import com.travel.client.DistantApiRestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchResultsFromDistantApi {

    private final DistantApiRestClient distantApiRestClient;

    public FetchResultsFromDistantApi(DistantApiRestClient distantApiRestClient) {
        this.distantApiRestClient = distantApiRestClient;
    }

    @GetMapping("api/v1/distant-api-result-fetcher/{param}")
    public ResponseEntity getStringFromDistantRestApi(@PathVariable final String param){
        return ResponseEntity.ok(this.distantApiRestClient.getResponseFromDistantRestApi(param));
    }

    @GetMapping("api/v1/distant-api-result-fetcher/warrant")
    public ResponseEntity getWarrantFromDistantRestApi(){
        return ResponseEntity.ok(this.distantApiRestClient.getWarrantFromDistantRestApi());
    }

    @GetMapping("api/v1/distant-api-result-fetcher/expenses")
    public ResponseEntity getExpensesFromDistantRestApi(){
        return ResponseEntity.ok(this.distantApiRestClient.getExpensesFromDistantRestApi());
    }


}
