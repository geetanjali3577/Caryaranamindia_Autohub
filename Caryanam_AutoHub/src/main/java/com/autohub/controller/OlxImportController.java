package com.autohub.controller;

import com.autohub.dto.CarResponse;
import com.autohub.entity.OlxCar;
import com.autohub.service.OlxImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/olx")
@RequiredArgsConstructor
public class OlxImportController {

    private final OlxImportService service;

    @PostMapping(
            value = "/import",
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE)
    public String importExcel(
            @RequestParam MultipartFile excel,
            @RequestParam MultipartFile zip)
            throws Exception {

        service.importData(excel, zip);

        return "Imported Successfully";
    }

    @GetMapping("/{id}")
    public CarResponse getCar(
            @PathVariable Long id) {

        return service.getCar(id);
    }

    @GetMapping("/all")
    public List<CarResponse> getAllCars() {

        return service.getAllCars();
    }

}
