package com.travel.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.travel.dto.WarrantDto;
import com.travel.exception.NoExpensesAttachedToWarrantException;
import com.travel.model.Warrant;
import com.travel.repository.ExpensesRepository;
import com.travel.service.WarrantsMapper;
import com.travel.service.WarrantsService;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("api/v1/warrants")
@AllArgsConstructor
public class WarrantController {

    private final ExpensesRepository expensesRepository;
    private final WarrantsService warrantsService;
    private final WarrantsMapper warrantsMapper;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity getAllWarrants() {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlMapper.writeValueAsString(this.warrantsService.findAll());
            return new ResponseEntity(this.warrantsService.findAll(), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/save-entity-from-file")
    public ResponseEntity saveLastEntityFromFile() {
        final ResponseEntity<StringBuilder> controllerResponse = this.readBinary();
        final String actualResponse = controllerResponse.getBody().toString();

        String lastLine = actualResponse.substring(actualResponse.lastIndexOf("\n"));
        final Warrant warrant = new Warrant();

        lastLine = lastLine.substring(lastLine.indexOf("(") + 1);
        lastLine = lastLine.substring(0, lastLine.indexOf(")"));

        if (this.validateTheObjectIsProperlyEntered(lastLine)) {
            String[] splitLine = lastLine.split(",");
            for (final String part : splitLine) {
                final String[] keyValues = part.split("=");
                if (keyValues[0].equals("id")) {
                    warrant.setId(Long.parseLong(keyValues[1]));
                } else if (keyValues[0].trim().equals("creationDate")) {
                    warrant.setCreationDate(keyValues[1]);
                } else if (keyValues[0].trim().equals("warrantStatus")) {
                    warrant.setWarrantStatus(keyValues[1]);
                } else if (keyValues[0].trim().equals("expensesId")) {
                    warrant.setExpenses(this.expensesRepository.findById(Long.parseLong(keyValues[1])));
                }
            }
        }

        this.warrantsService.saveWarrant(this.warrantsMapper.toDto(warrant));
        return new ResponseEntity(this.warrantsMapper.toDto(warrant), HttpStatus.OK);
    }

    private boolean validateTheObjectIsProperlyEntered(final String lastLine) {
        return lastLine.contains("id") && lastLine.contains("creationDate") && lastLine.contains("warrantStatus") && lastLine.contains("expensesId");
    }

    @GetMapping(value = "/read-from-file")
    public ResponseEntity readBinary() {
        try {
            final URL resource = this.getClass().getClassLoader().getResource("test.bin");
            final File file = new File(resource.toURI());

            final FileInputStream fis = new FileInputStream(file);
            final StringBuilder stringBuilder = new StringBuilder();

            int ch;
            while ((ch = fis.read()) != -1) {
                stringBuilder.append((char) ch);
            }

            fis.close();
            return new ResponseEntity(stringBuilder, HttpStatus.OK);
        } catch (final IOException | URISyntaxException ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "write-to-file")
    public ResponseEntity writeToBinary(@RequestBody final WarrantDto warrantDto) throws URISyntaxException {
        final URL resource = this.getClass().getClassLoader().getResource("test.bin");
        final File file = new File(resource.toURI());

        try (final FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write("\n".getBytes(StandardCharsets.UTF_8)); //new line
            fos.write(warrantDto.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("Successfully written data to the file");
            return new ResponseEntity(HttpStatus.OK);
        } catch (final IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity createWarrant(@RequestBody final WarrantDto warrantDto) {
        this.warrantsService.saveWarrant(warrantDto);
        return new ResponseEntity(warrantDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteWarrant(@PathVariable final String id) {
        this.warrantsService.deleteById(Long.parseLong(id));
        return new ResponseEntity("Successfully deleted warrant.", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/generate-pdf")
    public ResponseEntity generatePdf(@PathVariable final long id) {
        final ByteArrayOutputStream byteArrayOutputStream = this.warrantsService.generatePdf(id);
        final String headerValue = "attachment;filename=generated-pdf.pdf";
        return ResponseEntity
                .ok()
                .contentLength(byteArrayOutputStream.size())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(byteArrayOutputStream.toByteArray());

    }

}
