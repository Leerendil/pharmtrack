package org.vsu.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vsu.catalogservice.entity.Manufacturer;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    boolean existsByNameAndCompanyMail(String name, String companyMail);

    Optional<Manufacturer> findByName(String name);

    boolean existsByName(String name);
}
