package com.syuyndukov.library.library_managemen.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 50)
    private String name;

    @Column(nullable = false,unique = true,length = 50)
    private String username;//login

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false,unique = true, length = 50)
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
        role.getUsers().add(this); // работает, если геттер сгенерирован Lombok
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }
}
