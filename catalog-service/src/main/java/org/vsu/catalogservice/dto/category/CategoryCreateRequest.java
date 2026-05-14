package org.vsu.catalogservice.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest {
    @NotBlank(message = "Field (name) must not be empty")
    private String name;

    @NotBlank(message = "Field (description) must not be empty")
    private String description;
}
