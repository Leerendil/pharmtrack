package org.vsu.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vsu.catalogservice.service.CatalogService;

import static org.vsu.catalogservice.utils.constants.CatalogConstants.API_V1_CATALOGS;

@RestController
@RequestMapping(API_V1_CATALOGS)
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;
}
