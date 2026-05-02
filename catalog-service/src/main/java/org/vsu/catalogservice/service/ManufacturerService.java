package org.vsu.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerCreateRequest;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerResponse;
import org.vsu.catalogservice.entity.Manufacturer;
import org.vsu.catalogservice.entity.Medicine;
import org.vsu.catalogservice.mapper.CatalogMapper;
import org.vsu.catalogservice.repository.ManufacturerRepository;
import org.vsu.catalogservice.repository.MedicineRepository;
import org.vsu.catalogservice.utils.exceptions.ManufacturerAlreadyExistsException;
import org.vsu.catalogservice.utils.exceptions.ManufacturerNotFoundException;
import org.vsu.catalogservice.utils.exceptions.MedicineNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final MedicineRepository medicineRepository;
    private final CatalogMapper mapper;

    public ManufacturerResponse create(ManufacturerCreateRequest createRequest) {

        if (manufacturerRepository.existsByNameAndCompanyMail(
                createRequest.getName(),
                createRequest.getCompanyMail())
        ) {
            throw new ManufacturerAlreadyExistsException(createRequest.getName());
        }

        Manufacturer entity = mapper.mapToEntity(createRequest);

        return mapper.mapToResponse(manufacturerRepository.save(entity));
    }

    public void delete(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new MedicineNotFoundException("id: "+id));

    }

    @Transactional(readOnly = true)
    public Manufacturer getByName(String name) {
        Manufacturer entity = manufacturerRepository.findByName(name)
                .orElseThrow(() -> new ManufacturerNotFoundException(name));

        return entity;
    }

}
