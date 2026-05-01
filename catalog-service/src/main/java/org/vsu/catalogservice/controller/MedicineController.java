package org.vsu.catalogservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vsu.catalogservice.dto.medicie.MedicineCreateRequest;
import org.vsu.catalogservice.dto.medicie.MedicineResponse;
import org.vsu.catalogservice.service.MedicineService;

import static org.vsu.catalogservice.utils.constants.CatalogConstants.API_V1_MEDICINES;
import static org.vsu.catalogservice.utils.constants.CatalogConstants.NAME;

@RestController
@RequestMapping(API_V1_MEDICINES)
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @PostMapping
    public ResponseEntity<MedicineResponse> create(@RequestBody @Valid MedicineCreateRequest createRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicineService.create(createRequest));
    }

    @GetMapping(NAME)
    public ResponseEntity<MedicineResponse> getByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(medicineService.getByName(name));
    }
}
