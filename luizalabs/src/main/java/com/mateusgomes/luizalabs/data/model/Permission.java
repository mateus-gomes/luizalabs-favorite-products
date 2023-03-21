package com.mateusgomes.luizalabs.data.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "permissions")
public class Permission implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPermission;

    @Column(name = "description", length = 255)
    private String description;

    @Override
    public String getAuthority() {
        return description;
    }

    public String getDescription() {
        return description;
    }

    public Permission(Long idPermission) {
        this.idPermission = idPermission;
    }
}
