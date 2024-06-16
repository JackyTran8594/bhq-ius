package com.thanhbinh.dms.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "sim")
@AllArgsConstructor
@NoArgsConstructor
public class Sim extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "provider", columnDefinition = "NVARCHAR(200)")
    private String provider;

    @Column(name = "value", scale = 2)
    private BigDecimal value;

    @Column(name = "actived_date")
    private LocalDateTime activedDate;

    @Column(name = "note", columnDefinition = "VARCHAR(1000)")
    private String note;

    @OneToOne(mappedBy = "sim")
    private Device devices;

}