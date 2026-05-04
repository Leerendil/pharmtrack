package org.vsu.catalogservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.vsu.catalogservice.dto.manufacturer.ManageApplication;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerCreateRequest;
import org.vsu.catalogservice.entity.Manufacturer;
import org.vsu.catalogservice.service.ApplicationService;
import org.vsu.catalogservice.service.ManufacturerService;

import java.util.UUID;

import static org.vsu.catalogservice.utils.constants.CatalogConstants.*;

@RestController
@RequestMapping(API_V1_MANUFACTURERS)
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerService manufacturerService;
    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<UUID> apply(
            @RequestBody @Valid ManufacturerCreateRequest createRequest,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(manufacturerService.apply(createRequest, jwt));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(APPLICATION_MANAGE)
    public ResponseEntity<String> manage(@RequestBody @Valid ManageApplication manageApplication) {
        return ResponseEntity.ok(applicationService.manage(manageApplication.getApplicationToken(), manageApplication.getVerdict()));
    }

    @GetMapping(NAME)
    public ResponseEntity<Manufacturer> getByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(manufacturerService.getByName(name));
    }
}
