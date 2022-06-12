package com.travel.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import com.travel.dto.ExpensesDto;
import com.travel.dto.HnbDTO;
import com.travel.service.ExpensesService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("api/v1/expenses")
public class ExpensesController {

    private ExpensesService expensesService;

    private final ClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection()));

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAll(){
        return new ResponseEntity(this.expensesService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createExpenses(@RequestBody final ExpensesDto expensesDto){
        this.expensesService.saveExpenses(expensesDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteExpenses(@PathVariable final long id){
        this.expensesService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/converter")
    public ResponseEntity convertToHRK(@RequestParam final String currency){
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "https://api.hnb.hr/tecajn/v1?valuta=" + currency;
        return new ResponseEntity(restTemplate.getForEntity(url, ArrayList.class).getBody(), HttpStatus.OK);
    }

}
