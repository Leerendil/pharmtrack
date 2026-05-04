package org.vsu.catalogservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.vsu.catalogservice.entity.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "applications")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false, unique = true)
    private String companyMail;

    @Column(name = "applicant_id", nullable = false, unique = true)
    private UUID applicantId;

    @Column(name = "status")
    private ApplicationStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
