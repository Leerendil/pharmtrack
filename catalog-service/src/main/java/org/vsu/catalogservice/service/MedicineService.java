package org.vsu.catalogservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.catalogservice.dto.medicie.MedicineCreateRequest;
import org.vsu.catalogservice.dto.medicie.MedicineResponse;
import org.vsu.catalogservice.entity.Medicine;
import org.vsu.catalogservice.mapper.CatalogMapper;
import org.vsu.catalogservice.repository.MedicineRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final CatalogMapper mapper;

    public MedicineResponse create(MedicineCreateRequest createRequest) {

        if (medicineRepository.existsByNameAndDosageAndManufacturer(
                createRequest.getName(),
                createRequest.getDosage(),
                createRequest.getManufacturer())
        ) {
            //TODO: Заменить на свой MedicineAlreadyExistsException();
            throw new RuntimeException("409");
        }

        Medicine entity = mapper.mapToEntity(createRequest);

        return mapper.mapToResponse(medicineRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public MedicineResponse getByName(String name) {
        Medicine entity = medicineRepository.findByName(name)
                //TODO: Заменить на свой MedicineNotFoundException()
                .orElseThrow(() -> new RuntimeException("404"));

        return mapper.mapToResponse(entity);
    }
}
