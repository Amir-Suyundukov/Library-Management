package com.syuyndukov.library.library_managemen.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false, unique = true, length = 50)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)//тип загрузки(начнем с EAGER для простаты, при надобности перейдем в LAZY)
    @JoinTable(//описываем связующую таблицу для ManyToMany(JPA)
    name = "role_permission",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id"))

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "roles") // 'mappedBy = "roles"' указывает,
    // что управление этой связью (JoinTable) находится в поле 'roles' класса User
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> users = new HashSet<>();

    public Role(String name){//конструктор для создании роли только с именем
        this.name=name;
    }

    public void addPermission(Permission permission){//метод для удобного добовления/удаления разрешений
        this.permissions.add(permission);
        // Если бы связь была двунаправленной и управляемой с обеих сторон,
        // здесь нужно было бы добавить this к коллекции ролей в permission
    }

    public void removePermission(Permission permission){
        this.permissions.remove(permission);
    }

    public Set<User> getUsers() {
        return users;
    }

}
