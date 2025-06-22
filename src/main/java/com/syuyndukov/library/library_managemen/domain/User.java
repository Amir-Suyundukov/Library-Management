package com.syuyndukov.library.library_managemen.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 50)
    private String name;

    @Column(nullable = false,unique = true,length = 50)
    private String username;//login

    @Column(nullable = false ,name = "password")
    private String passwordHash;

    @Column(unique = true, length = 50)
    private String email;

    @Column(name = "first_name",length = 50)
    private String firstName;

    @Column(name = "last_name",length = 50)
    private String lastName;

    @Column(nullable = false)
    private boolean enabled = true;//флаг для секюрити (активен ли пользователь, по умолчанию да)

    // Связь Многие-ко-Многим с Role (управляющая сторона)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Role> roles = new HashSet<>();

    //методы для добавления/удаления ролей
    public void addRole(Role role) {
        this.roles.add(role);
        if (role.getUsers() != null) {
            role.getUsers().add(this); // работает, если геттер сгенерирован Lombok
        }
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        if (role.getUsers() != null) {
            role.getUsers().remove(this);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 1. Добавляем роли как GrantedAuthority. Spring Security часто использует префикс "ROLE_"
        if (this.roles != null){
            this.roles.forEach(role -> {authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            // 2. Добавляем права (permissions) как GrantedAuthority.
            role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        });
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
}
