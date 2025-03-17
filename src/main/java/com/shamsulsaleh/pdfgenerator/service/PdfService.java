package com.shamsulsaleh.pdfgenerator.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.shamsulsaleh.pdfgenerator.dto.BasicRespDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PdfService {

    private static final Logger logger = Logger.getLogger(PdfService.class.getName());

    @Value("${pdf.storage-path}")
    private String storagePath;

    @Value("${pdf.backup-path}")
    private String backupPath;

    @Value("${pdf.template-path}")
    private String templatePath;

    private final TemplateEngine templateEngine;

    public PdfService(@Value("${pdf.template-path}") String templatePath, TemplateEngine templateEngine) {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix(templatePath + File.separator);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        templateResolver.setCheckExistence(true);
        templateEngine.addTemplateResolver(templateResolver);
        logger.info("Template resolver configured with prefix: " + templatePath + File.separator);
        this.templateEngine = templateEngine;
    }

    public BasicRespDto generatePdf(Map<String, Object> input, String templateName, String fileName, String id) {
        Context context = new Context();
        context.setVariables(input);
        String htmlContent;
        try {
            logger.info("Processing template: " + templateName + " with context: " + context);
            htmlContent = templateEngine.process(templateName, context);
        } catch (Exception e) {
            logger.severe("Error processing template: " + templateName + " - " + e.getMessage());
            return new BasicRespDto(500, "error", "Error processing template: " + e.getMessage());
        }

        File directory = new File(storagePath + "/" + id);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File pdfFile = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            HtmlConverter.convertToPdf(htmlContent, fos);
        } catch (IOException e) {
            return new BasicRespDto(500, "error", "Error generating PDF: " + e.getMessage());
        }

        return new BasicRespDto(200, "success", "PDF generated successfully");
    }

    public Resource retrievePdf(String id, String fileName) {
        File pdfFile = new File(storagePath + "/" + id, fileName);
        if (!pdfFile.exists()) {
            throw new RuntimeException("File not found");
        }
        return new FileSystemResource(pdfFile);
    }

    public void deletePdf(String id, List<String> fileNames) {
        File backupDirectory = new File(backupPath + "/" + id);
        if (!backupDirectory.exists()) {
            backupDirectory.mkdirs();
        }

        for (String fileName : fileNames) {
            File pdfFile = new File(storagePath + "/" + id, fileName);
            if (pdfFile.exists()) {
                try {
                    Files.move(pdfFile.toPath(), new File(backupDirectory, fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Error moving file to backup", e);
                }
            }
        }
    }
}