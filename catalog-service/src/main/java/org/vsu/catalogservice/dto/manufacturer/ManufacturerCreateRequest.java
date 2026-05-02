package org.vsu.catalogservice.dto.manufacturer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerCreateRequest {
    @NotBlank(message = "Field (name) must not be empty")
    private String name;

    @NotBlank(message = "Field (country) must not be empty")
    private String country;

    @NotBlank(message = "Field (companyMail) must not be empty")
    @Email
    private String companyMail;
}
