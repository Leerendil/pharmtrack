package org.vsu.catalogservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.catalogservice.dto.medicie.MedicineCreateRequest;
import org.vsu.catalogservice.dto.medicie.MedicineResponse;
import org.vsu.catalogservice.entity.Category;
import org.vsu.catalogservice.entity.Manufacturer;
import org.vsu.catalogservice.entity.Medicine;
import org.vsu.catalogservice.mapper.CatalogMapper;
import org.vsu.catalogservice.repository.MedicineRepository;
import org.vsu.catalogservice.utils.exceptions.MedicineAlreadyExistsException;
import org.vsu.catalogservice.utils.exceptions.MedicineNotFoundException;

import java.util.List;

import static org.vsu.catalogservice.utils.specification.MedicineSpecification.commonSpecification;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;
    private final CatalogMapper mapper;

    public MedicineResponse create(MedicineCreateRequest createRequest) {

        Manufacturer manufacturer = manufacturerService.getByName(createRequest.getManufacturerName());
        Category category = categoryService.getByName(createRequest.getCategoryName());

        if (medicineRepository.existsByNameAndDosageAndManufacturer(
                createRequest.getName(),
                createRequest.getDosage(),
                manufacturer
        )) {
            throw new MedicineAlreadyExistsException(createRequest.getName());
        }

        Medicine entity = mapper.mapToEntity(createRequest);
        entity.setManufacturer(manufacturer);
        entity.setCategory(category);

        return mapper.mapToResponse(medicineRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public MedicineResponse getByName(String name) {
        Medicine entity = medicineRepository.findByName(name)
                .orElseThrow(() -> new MedicineNotFoundException(name));

        return mapper.mapToResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<MedicineResponse> search(
            List<Object> filters,
            Pageable pageable
    ) {

        Specification<Medicine> specification = commonSpecification(filters);

        Page<Medicine> pageOfEntities = medicineRepository.findAll(specification, pageable);

        return pageOfEntities.map(mapper::mapToResponse);
    }
}
