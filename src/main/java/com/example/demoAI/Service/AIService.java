package com.example.demoAI.Service;



import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AIService {

    @Value("classpath:/pdf/sample.pdf")
    private Resource pdfResource;

    public String extractTextFromPDF()
            throws Exception {

        InputStream inputStream =
                pdfResource.getInputStream();

        PDDocument document =
                PDDocument.load(inputStream);

        PDFTextStripper stripper =
                new PDFTextStripper();

        String text =
                stripper.getText(document);

        document.close();

        return text;
    }
}