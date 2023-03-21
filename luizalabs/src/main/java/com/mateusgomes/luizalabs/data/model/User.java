package com.mateusgomes.luizalabs.data.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUser;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "account_non_locked")
    public Boolean accountNonLocked;

    @Column(name = "account_non_expired")
    public Boolean accountNonExpired;

    @Column(name = "credentials_non_expired")
    public Boolean credentialsNonExpired;

    @Column(name = "enabled")
    public Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_permissions",
            joinColumns = { @JoinColumn(name = "id_user")},
            inverseJoinColumns = @JoinColumn(name = "id_permission")
    )
    public List<Permission> permissions;

    public List<String> roles;

    public List<String> getRoles() {
        List<String> rolesList = new ArrayList<>();
        for(int i = 0; i < permissions.size()-1; i++){
            roles.add(permissions.get(i).getDescription());
        }
        return rolesList;
    }

    public User(String userName, String fullName, String password) {
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public User() {
    }

    public UUID getIdUser() {
        return idUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
