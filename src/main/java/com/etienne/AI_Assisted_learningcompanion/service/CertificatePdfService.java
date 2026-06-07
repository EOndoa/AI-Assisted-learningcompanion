package com.etienne.AI_Assisted_learningcompanion.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class CertificatePdfService {

    public byte[] generateCertificatePdf(
            String studentName,
            String courseTitle,
            String certificateNumber,
            String completionDate
    ) throws Exception {

        PDDocument document = new PDDocument();

        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream content =
                new PDPageContentStream(document, page);

        PDType1Font font =
                new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

        content.beginText();
        content.setFont(font, 24);
        content.newLineAtOffset(120, 700);
        content.showText("Certificate of Completion");
        content.endText();

        content.beginText();
        content.setFont(font, 16);
        content.newLineAtOffset(100, 650);
        content.showText("Presented to:");
        content.endText();

        content.beginText();
        content.setFont(font, 20);
        content.newLineAtOffset(100, 620);
        content.showText(studentName);
        content.endText();

        content.beginText();
        content.setFont(font, 16);
        content.newLineAtOffset(100, 580);
        content.showText("For successfully completing:");
        content.endText();

        content.beginText();
        content.setFont(font, 18);
        content.newLineAtOffset(100, 550);
        content.showText(courseTitle);
        content.endText();

        content.beginText();
        content.setFont(font, 12);
        content.newLineAtOffset(100, 500);
        content.showText("Certificate No: " + certificateNumber);
        content.endText();

        content.beginText();
        content.setFont(font, 12);
        content.newLineAtOffset(100, 480);
        content.showText("Completion Date: " + completionDate);
        content.endText();

        content.close();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }
}