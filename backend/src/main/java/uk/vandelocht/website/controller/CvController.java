package uk.vandelocht.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.vandelocht.website.service.PdfGenerationService;

import java.io.IOException;

@RestController
@RequestMapping("/api/cv")
public class CvController {

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateCvPdf(
            @RequestParam(defaultValue = "attachment") String disposition) {
        try {
            byte[] pdfBytes = pdfGenerationService.generateCvPdf();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            
            // Support both inline viewing and download
            if ("inline".equals(disposition)) {
                headers.setContentDispositionFormData("inline", "Jan_van_de_Locht_CV.pdf");
            } else {
                headers.setContentDispositionFormData("attachment", "Jan_van_de_Locht_CV.pdf");
            }
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }
}