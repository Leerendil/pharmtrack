package org.vsu.catalogservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vsu.catalogservice.entity.enums.ApplicationStatus;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEvent {
    private String name;

    private String country;

    private String companyMail;

    private UUID applicantId;

    private ApplicationStatus status;
}
