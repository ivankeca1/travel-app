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
import com.travel.model.Expenses;
import com.travel.model.Warrant;
import com.travel.repository.ExpensesRepository;
import com.travel.repository.WarrantsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PdfCreator {

    @Value("${multithreading.pdf}")
    String multithreading;

    private static PdfFont font;
    private final WarrantsRepository warrantRepository;
    private final ExpensesRepository expensesRepository;

    public PdfCreator(WarrantsRepository warrantsRepository, ExpensesRepository expensesRepository) {
        this.warrantRepository = warrantsRepository;
        this.expensesRepository = expensesRepository;
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

            if(multithreading.toLowerCase(Locale.ROOT).equals("true")){

                long begin = System.currentTimeMillis();

                CompletableFuture<Table> constructCompanyNameTableCF = CompletableFuture.supplyAsync(this::constructCompanyNameTable);
                CompletableFuture<Table> constructWarrantNumberAndDateTableCF = CompletableFuture.supplyAsync(() -> this.constructWarrantNumberAndDateTable(optionalWarrant));
                CompletableFuture<Table> constructHeadingTableCF = CompletableFuture.supplyAsync(this::constructHeadingTable);
                CompletableFuture<Table> addExpensesCF = CompletableFuture.supplyAsync(() -> this.addExpeses(warrantId));

                try {
                    document.add(constructCompanyNameTableCF.get());
                    document.add(constructWarrantNumberAndDateTableCF.get());
                    document.add(constructHeadingTableCF.get());
                    document.add(addExpensesCF.get());

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                long end = System.currentTimeMillis();
                System.out.println("-------------------");
                System.out.println("Time spent: " + (double)(end - begin));
                System.out.println("-------------------");
            } else {

                long begin = System.currentTimeMillis();

                document.add(this.constructCompanyNameTable());
                document.add(this.constructWarrantNumberAndDateTable(optionalWarrant));
                document.add(this.constructHeadingTable());
                document.add(this.addExpeses(warrantId));

                long end = System.currentTimeMillis();
                System.out.println("-------------------");
                System.out.println("Time spent: " + (double)(end - begin));
                System.out.println("-------------------");
            }

            document.close();
            return true;
        } else {
            return false;
        }
    }

    private Table addExpeses(final long id) {
        Expenses expenses = this.expensesRepository.findById(id);
        if(expenses != null){
            final Table expensesTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

            final Cell expensesCell = new Cell();
            final Paragraph travelCostParagraph = new Paragraph("Ukupni trošak putovanja po ovom nalogu iznosi: " + expenses.getCostOfTravel() + "\n");
            final Paragraph dateOfTravelParagraph = new Paragraph("Datum putovanja: " + expenses.getDateOfTravel() + "\n");
            final Paragraph signature = new Paragraph("Ovaj nalog je generiran te je kao takav važeć bez potpisa.\n");

            expensesCell.add(travelCostParagraph);
            expensesCell.add(dateOfTravelParagraph);
            expensesCell.add(signature);

            expensesTable.addCell(expensesCell);

            return expensesTable;
        }

        return null;
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

    private Table constructWarrantNumberAndDateTable(Optional<Warrant> warrant) {
        final Table warrantNumberAndDate = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();

        final Cell warrantNumberCell = new Cell();
        final Paragraph warrantNumberLabel = new Paragraph();
        warrant.ifPresent(value -> warrantNumberLabel.add("Broj naloga: ").add(String.valueOf(value.getId())).setFont(font));
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
