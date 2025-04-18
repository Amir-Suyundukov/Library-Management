package com.syuyndukov.library.library_managemen.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "name")//сравниваем по имени
@ToString(of = {"id", "name"})//не влючаем user или permission
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

    @ManyToMany(fetch = FetchType.EAGER)//тип загрузки(начнем с EAGER для простаты , при надобности перейдем в LAZY)
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


//    @Override
//    public boolean equals(Object o){
//        if (this==o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Role role = (Role) o;
//        return Objects.equals(name,role.name);//сравниваем по имени
//    }
//
//    @Override
//    public int hashCode(){
//        return Objects.hash(name);
//    }
//
//    @Override
//    public String toString() {
//        // Избегаем рекурсии при выводе связанных сущностей (особенно users)
//        return "Role{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", permissionsCount=" + (permissions != null ? permissions.size() : 0) + // Просто количество
//                '}';
//    }
}
