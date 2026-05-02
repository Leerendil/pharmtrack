package org.vsu.catalogservice.dto.medicie;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineCreateRequest {
    @NotBlank(message = "Field (name) must not be empty")
    private String name;

    @NotBlank(message = "Field (description) must not be empty")
    private String description;

    @NotBlank(message = "Field (price) must not be empty")
    @Min(message = "Min price exceeded", value = 1L)
    @Max(message = "Max price exceeded", value = 9999L)
    private BigDecimal price;

    @NotBlank(message = "Field (dosage) must not be empty")
    @Min(message = "Min dosage exceeded", value = 1L)
    @Max(message = "Max dosage exceeded", value = 999L)
    private Long dosage;

    @NotBlank(message = "Field (isPrescriptionRequired) must not be empty")
    private boolean isPrescriptionRequired;

    @NotBlank(message = "Field (categoryName) must not be empty")
    private String categoryName;

    @NotBlank(message = "Field (manufacturerName) must not be empty")
    private String manufacturerName;
}
