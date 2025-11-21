package uk.vandelocht.website.service;

import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.helper.W3CDom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfGenerationService {

    @Autowired
    private TemplateEngine templateEngine;

    public byte[] generateCvPdf() throws DocumentException, IOException {
        // Create context with resume data
        Context context = new Context(Locale.GERMAN);
        context.setVariable("name", "Jan van de Locht");
        context.setVariable("title", "Ingenieur Maschinenbau");
        context.setVariable("email", "jan@vandelocht.uk");
        context.setVariable("github", "github.com/JvandeLocht");
        context.setVariable("codeberg", "codeberg.org/JvandeLocht");
        context.setVariable("location", "Oberhausen, Deutschland");
        context.setVariable("generatedDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        // Process the template
        String html = templateEngine.process("cv-template", context);

        // Clean and prepare HTML for PDF rendering
        Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        
        // Convert to W3C Document
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);

        // Generate PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        
        // Set document from W3C DOM
        renderer.setDocument(w3cDocument, null);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        return outputStream.toByteArray();
    }
}