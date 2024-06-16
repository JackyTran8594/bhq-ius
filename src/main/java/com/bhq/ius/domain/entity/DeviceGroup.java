package com.thanhbinh.dms.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "device_group")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceGroup extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", columnDefinition = "NVARCHAR(500)")
    private String typeName;

    @Column(name = "description", columnDefinition = "NVARCHAR(1000)")
    private String description;

    @Column(name = "note", columnDefinition = "VARCHAR(1000)")
    private String note;

    @OneToMany(mappedBy = "deviceGroup")
    private Set<Device> devices = new HashSet<>();

}