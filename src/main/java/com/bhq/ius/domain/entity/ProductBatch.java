package com.thanhbinh.dms.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "production_batch")
@AllArgsConstructor
@NoArgsConstructor
public class ProductBatch extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_number", unique = true)
    private Long batchNumber;

    @Column(name = "name", columnDefinition = "NVARCHAR(200)")
    private String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(500)")
    private String description;

    @Column(name = "value", scale = 2)
    private BigDecimal value;

    @Column(name = "batch_date")
    private LocalDateTime batchDate;

    @Column(name = "note", columnDefinition = "VARCHAR(1000)")
    private String note;

    @OneToMany(mappedBy = "productBatch")
    private Set<Device> devices = new HashSet<>();

}