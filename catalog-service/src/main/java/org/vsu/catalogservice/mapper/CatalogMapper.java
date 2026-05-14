package org.vsu.catalogservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.vsu.catalogservice.dto.category.CategoryCreateRequest;
import org.vsu.catalogservice.dto.category.CategoryResponse;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerCreateRequest;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerResponse;
import org.vsu.catalogservice.dto.medicie.MedicineCreateRequest;
import org.vsu.catalogservice.dto.medicie.MedicineResponse;
import org.vsu.catalogservice.entity.Category;
import org.vsu.catalogservice.entity.Manufacturer;
import org.vsu.catalogservice.entity.Medicine;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogMapper {
    Medicine mapToEntity(MedicineCreateRequest createRequest);
    Manufacturer mapToEntity(ManufacturerCreateRequest createRequest);
    Category mapToEntity(CategoryCreateRequest createRequest);

    MedicineResponse mapToResponse(Medicine entity);
    ManufacturerResponse mapToResponse(Manufacturer entity);
    CategoryResponse mapToResponse(Category entity);
}
