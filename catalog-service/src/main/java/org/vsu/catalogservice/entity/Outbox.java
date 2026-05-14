package org.vsu.catalogservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "outbox")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Outbox {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String payload;
}
