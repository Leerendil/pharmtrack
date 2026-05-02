package org.vsu.catalogservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerCreateRequest;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerResponse;
import org.vsu.catalogservice.entity.Manufacturer;
import org.vsu.catalogservice.service.ManufacturerService;

import static org.vsu.catalogservice.utils.constants.CatalogConstants.API_V1_MANUFACTURERS;
import static org.vsu.catalogservice.utils.constants.CatalogConstants.NAME;

@RestController
@RequestMapping(API_V1_MANUFACTURERS)
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @PostMapping
    public ResponseEntity<ManufacturerResponse> create(@RequestBody @Valid ManufacturerCreateRequest createRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(manufacturerService.create(createRequest));
    }

    @GetMapping(NAME)
    public ResponseEntity<Manufacturer> getByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(manufacturerService.getByName(name));
    }
}
