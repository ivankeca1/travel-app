package com.travel.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
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

    public List<WarrantDto> findAll() {
        return this.warrantsMapper.toDtoList(this.warrantsRepository.findALlWarrants());
    }

    public void saveWarrant(final WarrantDto warrantDto) {
        this.warrantsRepository.save(this.warrantsMapper.toModel(warrantDto));
    }

    public Warrant findById(final long id) {
        Optional<Warrant> warrant = this.warrantsRepository.findById(id);
        return warrant.orElse(null);
    }

    public void deleteById(final Long id) {
        this.warrantsRepository.deleteById(id);
    }

    public ByteArrayOutputStream generatePdf(long warrantId) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        final PdfDocument pdfDocument = new PdfDocument(writer);

        if (this.pdfCreator.constructDocument(pdfDocument, warrantId)) {
            return byteArrayOutputStream;
        } else {
            return null;
        }
    }
}