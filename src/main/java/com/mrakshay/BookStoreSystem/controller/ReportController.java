package com.mrakshay.BookStoreSystem.controller;


import com.mrakshay.BookStoreSystem.dto.BookReportDto;
import com.mrakshay.BookStoreSystem.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Report APIs", description = "Book reporting operations")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Generate report for book store")
    @GetMapping("/books")
    public ResponseEntity<BookReportDto> generateReport() {

        return ResponseEntity.ok(
                reportService.generateReport());
    }
}
