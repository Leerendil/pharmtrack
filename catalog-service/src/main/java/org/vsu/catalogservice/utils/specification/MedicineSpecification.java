package org.vsu.catalogservice.utils.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.vsu.catalogservice.entity.Medicine;

import java.math.BigDecimal;
import java.util.List;

public class MedicineSpecification {
    public static Specification<Medicine> nameContaining(String name) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"+name.toLowerCase()+"%");
    }

    public static Specification<Medicine> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("name"), categoryName);
        };
    }

    public static Specification<Medicine> hasManufacturer(String manufacturerName) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> manufacturerJoin = root.join("manufacturer");
            return criteriaBuilder.equal(manufacturerJoin.get("name"), manufacturerName);
        };
    }

    public static Specification<Medicine> hasManufacturersCountry(String manufacturerCountry) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> manufacturerJoin = root.join("manufacturer");
            return criteriaBuilder.equal(manufacturerJoin.get("country"), manufacturerCountry);
        };
    }

    public static Specification<Medicine> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Medicine> priceMoreThanOrEqual(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Medicine> commonSpecification(List<Object> filters) {
        Specification<Medicine> specification = Specification.where(null);

        String name = (String) filters.get(0);
        String categoryName = (String) filters.get(1);
        String manufacturerName = (String) filters.get(2);
        String manufacturerCountry = (String) filters.get(3);
        BigDecimal maxPrice = (BigDecimal) filters.get(4);
        BigDecimal minPrice = (BigDecimal) filters.get(5);

        if (name != null) {
            specification = specification.and(nameContaining(name));
        }

        if (categoryName != null) {
            specification = specification.and(hasCategory(categoryName));
        }

        if (manufacturerName != null) {
            specification = specification.and(hasManufacturer(manufacturerName));
        }

        if (manufacturerCountry != null) {
            specification = specification.and(hasManufacturersCountry(manufacturerCountry));
        }

        if (maxPrice != null) {
            specification = specification.and(priceLessThanOrEqual(maxPrice));
        }

        if (minPrice != null) {
            specification = specification.and(priceMoreThanOrEqual(minPrice));
        }

        return specification;
    }

}
