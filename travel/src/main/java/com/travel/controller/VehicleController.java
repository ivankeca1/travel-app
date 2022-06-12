package com.travel.controller;

import javax.print.attribute.standard.Media;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.dto.VehicleDto;
import com.travel.service.VehicleService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/vehicles")
@AllArgsConstructor
public class VehicleController {

    private VehicleService vehicleService;

    @GetMapping("/all")
    public ResponseEntity findAll(){
        return new ResponseEntity(this.vehicleService.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/json-example", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVehicleJsonExample(@RequestBody final VehicleDto vehicleDto){
        this.vehicleService.saveVehicle(vehicleDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/xml-example", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity createVehicleXmlExample(@RequestBody final VehicleDto vehicleDto){
        this.vehicleService.saveVehicle(vehicleDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteVehicle(@PathVariable final long id){
        this.vehicleService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("decrypt-registration-plate/{id}")
    public ResponseEntity decryptRegistrationPlate(@PathVariable final long id){
        return new ResponseEntity(this.vehicleService.decryptRegistrationPlateById(id), HttpStatus.OK);
    }

}
