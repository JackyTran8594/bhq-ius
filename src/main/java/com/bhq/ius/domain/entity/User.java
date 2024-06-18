package com.bhq.ius.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", columnDefinition = "VARCHAR(1000)")
    private String password;

    @Column(name = "full_name", columnDefinition = "NVARCHAR(200)")
    private String fullName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 500)
    private String email;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

}