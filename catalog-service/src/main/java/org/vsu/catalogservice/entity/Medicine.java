package org.vsu.catalogservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicines")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long dosage;

    @Column(name = "is_prescription_required", nullable = false)
    private boolean isPrescriptionRequired;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @JoinColumn(name = "manufacturer_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Manufacturer manufacturer;
}
