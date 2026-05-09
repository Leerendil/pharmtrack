package org.vsu.catalogservice.dto.medicie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineBasicInfo {
    private Long id;

    private String name;

    private BigDecimal price;

}
