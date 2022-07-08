package com.travel.controller;

import com.travel.service.LoggingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/log")
public class LogsController {

    private final LoggingService loggingService;
    private final WarrantController warrantController;

    @Value("${log.file.xml.location}")
    private String xmlLogFileLocation;

    @Value("${log.file.json.location}")
    private String jsonLogFileLocation;

    public LogsController(LoggingService loggingService, WarrantController warrantController) {
        this.loggingService = loggingService;
        this.warrantController = warrantController;
    }

    @GetMapping
    public ResponseEntity formatLogs(){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(xmlLogFileLocation));
            this.loggingService.formatLogs(doc);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLog(@PathVariable final Long id){
        this.loggingService.deleteFromLogs(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLog(@PathVariable final long id, @RequestParam final String message){
        this.loggingService.updateSpecificLog(id, message);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/encrypt-logs/{key}")
    public ResponseEntity encryptLogs(@PathVariable final String key){
        this.loggingService.encryptLogs(key);
        try {
             return this.warrantController.writeToBinary(key);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
