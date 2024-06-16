package com.thanhbinh.dms.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "permission")
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 100)
    private String code;

    @Column(name = "name", columnDefinition = "NVARCHAR(500)")
    private String name;

    @Column(name = "url", columnDefinition = "VARCHAR(500)")
    private String url;

    @Column(name = "method", columnDefinition = "VARCHAR(20)")
    private String method;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(100)")
    private String parentCode;

    @Column(name = "is_action")
    private Boolean isAction;

    @Column(name = "description", columnDefinition = "NVARCHAR(500)")
    private String description;

    @Column(name = "note", columnDefinition = "VARCHAR(1000)")
    private String note;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission", joinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id")},
            inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

}