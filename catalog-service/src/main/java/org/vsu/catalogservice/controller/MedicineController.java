package org.vsu.catalogservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vsu.catalogservice.dto.medicie.MedicineCreateRequest;
import org.vsu.catalogservice.dto.medicie.MedicineResponse;
import org.vsu.catalogservice.service.MedicineService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.vsu.catalogservice.utils.constants.CatalogConstants.*;

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

    @GetMapping(SEARCH)
    public ResponseEntity<Page<MedicineResponse>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "manufacturerName", required = false) String manufacturerName,
            @RequestParam(value = "manufacturerCountry", required = false) String manufacturerCountry,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @PageableDefault(
                    page = 0,
                    size = 15,
                    sort = "price", direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        List<Object> filters = new ArrayList<>();
        filters.add(name);
        filters.add(categoryName);
        filters.add(manufacturerName);
        filters.add(manufacturerCountry);
        filters.add(maxPrice);
        filters.add(minPrice);

        return ResponseEntity.ok(medicineService.search(
                filters,
                pageable
        ));
    }
}
