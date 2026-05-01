package org.vsu.catalogservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false, unique = true)
    private String companyMail;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY)
    private List<Medicine> medicines = new ArrayList<>();
}
