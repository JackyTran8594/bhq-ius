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
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_code", length = 50)
    private String roleCode;

    @Column(name = "role_name", columnDefinition = "NVARCHAR(500)")
    private String roleName;

    @Column(name = "description", columnDefinition = "NVARCHAR(500)")
    private String description;

    @Column(name = "note", columnDefinition = "VARCHAR(1000)")
    private String note;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_group_id", referencedColumnName = "id")
    private RoleGroup roleGroup;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> user;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Permission> permissions = new HashSet<>();


}