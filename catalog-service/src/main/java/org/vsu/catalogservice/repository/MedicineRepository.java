package org.vsu.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vsu.catalogservice.entity.Manufacturer;
import org.vsu.catalogservice.entity.Medicine;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    boolean existsByNameAndDosageAndManufacturer(String name, Long dosage, Manufacturer manufacturer);

    Optional<Medicine> findByName(String name);
}
