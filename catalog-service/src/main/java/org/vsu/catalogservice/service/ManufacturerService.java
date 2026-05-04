package org.vsu.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.Uuid;
import org.springframework.security.oauth2.jwt.Jwt;
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

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ApplicationService applicationService;

    public UUID apply(ManufacturerCreateRequest createRequest, Jwt jwt) {
        createRequest.setApplicantId(UUID.fromString(jwt.getSubject()));

        if (manufacturerRepository.existsByNameAndCompanyMail(
                createRequest.getName(),
                createRequest.getCompanyMail())
        ) {
            throw new ManufacturerAlreadyExistsException(createRequest.getName());
        }

        UUID applicationToken = UUID.randomUUID();

        applicationService.save(applicationToken, createRequest);

        return applicationToken;
    }

    @Transactional(readOnly = true)
    public Manufacturer getByName(String name) {
        Manufacturer entity = manufacturerRepository.findByName(name)
                .orElseThrow(() -> new ManufacturerNotFoundException(name));

        return entity;
    }

}
