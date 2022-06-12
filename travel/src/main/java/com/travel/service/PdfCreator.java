package com.travel.service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.text.pdf.BaseFont;
import com.travel.model.Warrant;
import com.travel.repository.WarrantsRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class PdfCreator {

    private static PdfFont font;
    private final WarrantsRepository warrantRepository;

    public PdfCreator(WarrantsRepository warrantsRepository) {
        this.warrantRepository = warrantsRepository;
    }

    public boolean constructDocument(final PdfDocument pdfDocument, final long warrantId) {

        final Optional<Warrant> optionalWarrant = this.warrantRepository.findById(warrantId);

        if (optionalWarrant.isPresent()) {
            try {
                font = PdfFontFactory.createFont(BaseFont.COURIER, PdfEncodings.CP1250);
            } catch (final IOException e) {
                e.getMessage();
            }

            final Document document = new Document(pdfDocument);

            document.add(this.constructCompanyNameTable());
            document.add(this.constructWarrantNumberAndDateTable());
            document.add(this.constructHeadingTable());

            document.close();
            return true;
        } else {
            return false;
        }
    }


    private Table constructCompanyNameTable() {
        final Table companyName = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        final Table textField = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        final Cell companyNameCell = new Cell();
        final Paragraph companyNameValue = new Paragraph("IVAN KECA TRAVEL & CO").setFont(font);
        companyNameCell.add(companyNameValue);
        companyNameCell.setBorderTop(Border.NO_BORDER);
        companyNameCell.setBorderLeft(Border.NO_BORDER);
        companyNameCell.setBorderRight(Border.NO_BORDER);
        companyNameCell.setHeight(25);

        companyNameCell.setBorderBottom(new DashedBorder(ColorConstants.GRAY, (float) 0.5));

        final Cell labelCell = new Cell();
        final Paragraph labelValue = new Paragraph("(Company name)").setFont(font);
        labelCell.add(labelValue);
        labelCell.setBorder(Border.NO_BORDER);
        labelCell.setHeight(20);

        textField.addCell(companyNameCell);
        textField.addCell(labelCell);

        textField.setTextAlignment(TextAlignment.CENTER);
        textField.setMargin(5);
        textField.setBorder(Border.NO_BORDER);

        companyName.setTextAlignment(TextAlignment.CENTER);
        companyName.addCell(textField);
        companyName.setMarginBottom(5);

        return companyName;
    }

    private Table constructWarrantNumberAndDateTable() {
        final Table warrantNumberAndDate = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();

        final Cell warrantNumberCell = new Cell();
        final Paragraph warrantNumberLabel = new Paragraph();
        warrantNumberLabel.add("Broj naloga: ").add(" 134").setFont(font);
        warrantNumberCell.add(warrantNumberLabel);
        warrantNumberCell.setTextAlignment(TextAlignment.CENTER);

        final Cell locationCell = new Cell();
        final Paragraph location = new Paragraph("U").add(" Zagrebu, ").setFont(font);
        locationCell.add(location);
        locationCell.setBorder(Border.NO_BORDER);
        locationCell.setTextAlignment(TextAlignment.RIGHT);

        final Cell dateCell = new Cell();
        final Paragraph date = new Paragraph();
        date.add(" 21.03.2021. ").setFont(font);
        dateCell.add(date);
        dateCell.setBorder(Border.NO_BORDER);
        dateCell.setTextAlignment(TextAlignment.LEFT);

        warrantNumberAndDate.addCell(warrantNumberCell);
        warrantNumberAndDate.addCell(locationCell);
        warrantNumberAndDate.addCell(dateCell);
        warrantNumberAndDate.setMarginBottom(5);

        return warrantNumberAndDate;
    }

    private Table constructHeadingTable() {
        final Table heading = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        final Cell headingCell = new Cell();
        final Paragraph headingParagraph = new Paragraph("PUTNI NALOG").setFont(font);
        headingCell.setTextAlignment(TextAlignment.CENTER);
        headingCell.setFontSize(30);
        headingCell.setBold();
        headingCell.setBorder(Border.NO_BORDER);

        headingCell.add(headingParagraph);

        heading.addCell(headingCell);

        return heading;
    }

}
