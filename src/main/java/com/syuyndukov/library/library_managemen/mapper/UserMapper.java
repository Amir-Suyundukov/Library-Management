package com.syuyndukov.library.library_managemen.mapper;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.dto.UserCreateDto;
import com.syuyndukov.library.library_managemen.dto.UserResponseDto;
import com.syuyndukov.library.library_managemen.dto.UserUpdateDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserCreateDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(true);//пользователь в сети по умолчанию

        //роли после создания пользователя или в сервисе

        return user;
    }

    public UserResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEnabled(user.isEnabled());

        if (user.getRoles() != null) {
            Set<String> roleName = user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toSet());
            dto.setRoles(roleName);
        } else {
            dto.setRoles(new HashSet<>());//если нет ролей или null
        }
        //Пароль в (ХЕШ) не копируем в DTO
        return dto;
    }

    public List<UserResponseDto> toDtoList(List<User> list) {
        if (list == null) {
            return null;
        }
        return list.stream()
                .map(this::toDto)//для каждого User вызываем toDto
                .collect(Collectors.toList());//собираем в лист
    }

    public void updateEntityFromDto(UserUpdateDto dto, User user) {
        if (dto == null || user == null) {
            return;
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        user.setEnabled(dto.isEnabled());
    }

}
