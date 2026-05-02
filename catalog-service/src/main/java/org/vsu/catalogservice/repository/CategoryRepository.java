package org.vsu.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vsu.catalogservice.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    Optional<Category> findByName(String name);
}
