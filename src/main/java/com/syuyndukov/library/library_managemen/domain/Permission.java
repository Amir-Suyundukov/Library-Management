package com.syuyndukov.library.library_managemen.domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@EqualsAndHashCode(of = "name")//связываем по имени
@ToString(of = {"id","name"})//не влючаем связные сущьности
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    // Мы НЕ определяем здесь поле для связи с Role.
    // Связь ManyToMany будет определена на стороне Role с помощью mappedBy.
    // Это помогает избежать двунаправленной зависимости, где обе стороны управляют связью.

    // Важно переопределить equals и hashCode для корректной работы с коллекциями (например, Set в Role)
    // Lombok @EqualsAndHashCode(of = "name") может быть альтернативой, но сделаем вручную для ясности
//
//    @Override
//    public boolean equals(Object o){
//        if(this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Permission that = (Permission) o;
//        return Objects.equals(name,that.name);//сравниваем по имени а не по ID (ID может быть null до сохранения)
//    }
//
//    @Override
//    public int hashCode(){
//        //исплользуем хеш уникального имени
//        return Objects.hash(name);
//    }
//
//    @Override
//    public String toString(){
//        return "Permission{"
//                + "id=" +
//                ", name='" + name + '\'' + '}';
//    }
}
