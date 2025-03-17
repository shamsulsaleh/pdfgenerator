package com.shamsulsaleh.pdfgenerator.controller;

import com.shamsulsaleh.pdfgenerator.dto.BasicRespDto;
import com.shamsulsaleh.pdfgenerator.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate")
    public BasicRespDto generatePdf(@RequestBody Map<String, Object> input) {
        String templateName = (String) input.get("templateName");
        String fileName = (String) input.get("fileName");
        String id = (String) input.get("id");
        return pdfService.generatePdf(input, templateName, fileName, id);
    }

    @GetMapping("/retrieve")
    public ResponseEntity<Resource> retrievePdf(@RequestParam String id, @RequestParam String fileName) {
        Resource pdf = pdfService.retrievePdf(id, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(pdf);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePdf(@RequestParam String id, @RequestParam List<String> fileNames) {
        pdfService.deletePdf(id, fileNames);
        return ResponseEntity.noContent().build();
    }
}