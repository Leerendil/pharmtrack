package org.vsu.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerCreateRequest;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerResponse;
import org.vsu.catalogservice.entity.Manufacturer;
import org.vsu.catalogservice.mapper.CatalogMapper;
import org.vsu.catalogservice.repository.ManufacturerRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final CatalogMapper mapper;

    public ManufacturerResponse create(ManufacturerCreateRequest createRequest) {

        if (manufacturerRepository.existsByNameAndCompanyMail(
                createRequest.getName(),
                createRequest.getCompanyMail())
        ) {
            //TODO: Заменить на свой ManufacturerAlreadyExistsException()
            throw new RuntimeException("409");
        }

        Manufacturer entity = mapper.mapToEntity(createRequest);

        return mapper.mapToResponse(manufacturerRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public ManufacturerResponse getByName(String name) {
        Manufacturer entity = manufacturerRepository.findByName(name)
                //TODO: Заменить на свой ManucaturerNotFoundException()
                .orElseThrow(() -> new RuntimeException("404"));

        return mapper.mapToResponse(entity);
    }
}
