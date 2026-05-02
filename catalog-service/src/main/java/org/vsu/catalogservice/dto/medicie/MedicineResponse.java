package org.vsu.catalogservice.dto.medicie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vsu.catalogservice.entity.Category;
import org.vsu.catalogservice.entity.Manufacturer;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineResponse {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Long dosage;

    private boolean isPrescriptionRequired;

    private Category category;

    private Manufacturer manufacturer;
}
