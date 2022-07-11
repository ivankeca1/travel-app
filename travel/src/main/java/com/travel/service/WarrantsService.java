package com.travel.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.travel.dto.Calculated;
import com.travel.jdbc.DatabaseJdbcClient;
import org.springframework.stereotype.Service;

import com.travel.dto.WarrantDto;
import com.travel.model.Warrant;
import com.travel.repository.WarrantsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarrantsService {

    private WarrantsRepository warrantsRepository;
    private WarrantsMapper warrantsMapper;
    private PdfCreator pdfCreator;
    private DatabaseJdbcClient jdbcClient;
    private LoggingService loggingService;

    public List<Calculated> findAllWarrantsAndTheirCostOfTravel(final String sort) {
        if (sort.equals("ascending")) {
            return this.jdbcClient.findAllWarrantsAndTheirCostOfTravelASC();
        } else if (sort.equals("descending")) {
            return this.jdbcClient.findAllWarrantsAndTheirCostOfTravelDESC();
        } else {
            return null;
        }
    }

    public List<Calculated> findAllWarrantsAndTheirCostOfTravelWithFilter(final String costOfTravel, final String mode, final String order) {
        return this.jdbcClient.findAllWarrantsAndTheirCostOfTravelASCWithFilter(costOfTravel, mode, order);
    }

    public List<WarrantDto> findAll() {
        return this.warrantsMapper.toDtoList(this.warrantsRepository.findALlWarrants());
    }

    public Warrant saveWarrant(final WarrantDto warrantDto) {
        try {
            this.loggingService.writeToLogs("Warrant saved.");
            return this.warrantsRepository.save(this.warrantsMapper.toModel(warrantDto));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Warrant findById(final long id) {
        Optional<Warrant> warrant = this.warrantsRepository.findById(id);
        return warrant.orElse(null);
    }

    public void deleteById(final Long id) {
        try {
            this.loggingService.writeToLogs("Warrant with id " + id + " deleted.");
            this.warrantsRepository.deleteById(id);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ByteArrayOutputStream generatePdf(long warrantId) {
        try {
            this.loggingService.writeToLogs("Started generating PDF.");
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            final PdfDocument pdfDocument = new PdfDocument(writer);

            if (this.pdfCreator.constructDocument(pdfDocument, warrantId)) {
                this.loggingService.writeToLogs("Finished generating PDF.");
                return byteArrayOutputStream;
            } else {
                this.loggingService.writeToLogs("Finished generating PDF.");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
