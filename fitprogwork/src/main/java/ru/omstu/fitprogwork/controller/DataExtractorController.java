package ru.omstu.fitprogwork.controller;

import ru.omstu.fitprogwork.dto.ExtractionRequest;
import ru.omstu.fitprogwork.service.DataProcessingService;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataExtractorController {

    private final DataProcessingService processingService;

    public DataExtractorController(DataProcessingService processingService) {
        this.processingService = processingService;
    }

    @PostMapping("/extract")
    public Map<String, String> extractData(@RequestBody ExtractionRequest request) {
        try {
            String value = processingService.extract(request.getType(), request.getData(), request.getPath());
            Map<String, String> response = new HashMap<>();
            response.put("value", value);
            return response;
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }
}