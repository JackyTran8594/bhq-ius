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

@Getter
@Setter
@Entity
@Table(name = "tech_spec")
@AllArgsConstructor
@NoArgsConstructor
public class TechSpec extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mainboard")
    private String mainboard;

    @Column(name = "memory")
    private String memory;

    @Column(name = "cpu")
    private String cpu;

    @Column(name = "sensor")
    private String sensor;

    @Column(name = "measurement_range")
    private String measurementRange;

    @Column(name = "accuracy")
    private String accuracy;

    @Column(name = "power_supply")
    private String powerSupply;

    @Column(name = "operating")
    private String operating;

    @Column(name = "weight")
    private String weight;

    @Column(name = "dimension")
    private String dimension;

    @Column(name = "other", columnDefinition = "NVARCHAR(1000)")
    private String other;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_type_id", referencedColumnName = "id")
    private DeviceType deviceType;

    @Column(name = "note", columnDefinition = "VARCHAR(1000)")
    private String note;

}