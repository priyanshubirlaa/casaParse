package com.pdfParse.gemini.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pdfParse.gemini.service.GeminiAIService;
import com.pdfParse.gemini.service.PdfProcessingService;

@RestController
@RequestMapping("/api/pdf")
public class PdfReaderController {

    private final PdfProcessingService pdfProcessingService;
    private final GeminiAIService geminiAIService;

    public PdfReaderController(PdfProcessingService pdfProcessingService, GeminiAIService geminiAIService) {
        this.pdfProcessingService = pdfProcessingService;
        this.geminiAIService = geminiAIService;
    }

    @PostMapping("/parse")
    public ResponseEntity<String> parsePdf(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "firstname", required = false) String firstname,
        @RequestParam(value = "dob", required = false) String dob // Optional
    ) {
        try {
            String response = pdfProcessingService.processPdf(file, firstname, dob);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
