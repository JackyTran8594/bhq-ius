package com.thanhbinh.dms.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "organization")
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(500)")
    private String name;

    @Column(name = "address", columnDefinition = "NVARCHAR(1000)")
    private String address;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "org_group_id", referencedColumnName = "id")
    private OrganizationGroup organizationGroup;

    @Column(name = "parent_org_id")
    private Long parentOrgId;

    @Column(name = "description", columnDefinition = "NVARCHAR(1000)")
    private String description;

    @Column(name = "note", columnDefinition = "VARCHAR(1000)")
    private String note;

    @OneToMany(mappedBy = "organization")
    private Set<Device> devices = new HashSet<>();

}