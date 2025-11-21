package uk.vandelocht.website.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class PdfGenerationService {

    private static final Logger log = LoggerFactory.getLogger(PdfGenerationService.class);

    private final String typstCommand;

    public PdfGenerationService(@Value("${typst.command:typst}") String typstCommand) {
        this.typstCommand = typstCommand;
        log.info("Typst PDF service initialized with command: {}", typstCommand);
    }

    @Cacheable("cvPdf")
    public byte[] generateCvPdf() throws IOException, InterruptedException {
        log.info("Generating CV PDF from Typst source");

        // Create temporary directory for extraction
        Path tempDir = Files.createTempDirectory("cv-typst-");
        Path tempCvFile = tempDir.resolve("cv.typ");
        Path tempOutput = tempDir.resolve("cv.pdf");

        try {
            // Extract cv.typ from JAR resources to temp directory
            ClassPathResource cvResource = new ClassPathResource("cv/cv.typ");
            try (InputStream is = cvResource.getInputStream()) {
                Files.copy(is, tempCvFile, StandardCopyOption.REPLACE_EXISTING);
            }
            log.debug("Extracted cv.typ to: {}", tempCvFile);

            // Run Typst compilation
            ProcessBuilder pb = new ProcessBuilder(
                typstCommand,
                "compile",
                tempCvFile.toString(),
                tempOutput.toString()
            );

            // Set working directory to temp directory (for relative paths like pic.jpg)
            pb.directory(tempDir.toFile());
            pb.redirectErrorStream(true);

            log.debug("Executing Typst command: {}", pb.command());
            Process process = pb.start();

            // Capture output for debugging
            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                log.error("Typst compilation failed with exit code {}: {}", exitCode, output);
                throw new IOException("PDF generation failed: " + output);
            }

            log.info("Typst compilation successful");
            byte[] pdfBytes = Files.readAllBytes(tempOutput);
            log.info("Generated PDF size: {} bytes", pdfBytes.length);

            return pdfBytes;

        } finally {
            // Clean up temp directory and all files
            try {
                Files.deleteIfExists(tempOutput);
                Files.deleteIfExists(tempCvFile);
                Files.deleteIfExists(tempDir);
            } catch (IOException e) {
                log.warn("Failed to clean up temp directory: {}", tempDir, e);
            }
        }
    }
}
