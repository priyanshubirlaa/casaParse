package com.pdfParse.gemini.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PdfProcessingService {
    @Autowired
    private GeminiAIService geminiAIService;

    public String processPdf(MultipartFile file, String firstname, String dob) throws Exception {
        String password = (firstname != null && dob != null) ? firstname + dob : null;  // 🔑 Generate password only if needed

        // 🔍 Extract text from PDF
        String extractedText = extractTextFromPdf(file, password);

        // 🤖 Call Gemini AI to get structured data
        return geminiAIService.extractDataFromText(extractedText);
    }

    private String extractTextFromPdf(MultipartFile file, String password) throws Exception {
        PDDocument document = null;
        File tempFile = null;
        try {
            // Create a temp file to handle the PDF
            tempFile = File.createTempFile("tempPdf", ".pdf");
            file.transferTo(tempFile);

            // First, check if the PDF is encrypted by attempting to load without a password
            try {
                document = PDDocument.load(tempFile);
            } catch (InvalidPasswordException e) {
                if (password == null) {
                    throw new RuntimeException("PDF is password-protected. Please provide firstname and DOB in Postman.");
                }
                // Try loading with the provided password
                try {
                    document = PDDocument.load(tempFile, password);
                } catch (InvalidPasswordException ex) {
                    throw new RuntimeException("Cannot decrypt PDF. The password '" + password + "' is incorrect.");
                }
            }

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();
            return text;

        } catch (IOException e) {
            e.printStackTrace(); // Print full stack trace for detailed error
            throw new RuntimeException("Failed to process PDF. File may be corrupted or an I/O error occurred.");
        } finally {
            if (document != null && !document.getCurrentAccessPermission().isReadOnly()) {
                try {
                    document.close();
                } catch (IOException e) {
                    System.err.println("Error closing document: " + e.getMessage());
                }
            }
            // Clean up temp file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}