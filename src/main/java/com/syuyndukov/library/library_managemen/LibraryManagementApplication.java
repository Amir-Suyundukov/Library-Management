package com.syuyndukov.library.library_managemen;

import com.syuyndukov.library.library_managemen.domain.Role;
import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.dto.UserCreateDto;
import com.syuyndukov.library.library_managemen.dto.UserResponseDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorCreationDto;
import com.syuyndukov.library.library_managemen.dto.lib.AuthorResponseDto;
import com.syuyndukov.library.library_managemen.service.AuthorService;
import com.syuyndukov.library.library_managemen.service.RoleService;
import com.syuyndukov.library.library_managemen.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@SpringBootApplication
public class LibraryManagementApplication {

	private final UserService userService;
	private final RoleService roleService;
	private final AuthorService authorService;

	public LibraryManagementApplication(UserService userService, RoleService roleService, AuthorService authorService) {
		this.userService = userService;
		this.roleService = roleService;
        this.authorService = authorService;
    }

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}

	// метод выполняется после старта приложения
	// Опционально: запускать этот код только в dev профиле, чтобы не создавать тестовых пользователей в проде
	@PostConstruct
	@Profile("dev")
	public void setupInitialData() {
		System.out.println("Создание начальных данных (тестового пользователя)...");

		createRoleIfNotFound("ADMIN");
		createRoleIfNotFound("LIBRARIAN");
		createRoleIfNotFound("READER");

		createAnonymousAuthorIfNotFound("Анонимный Автор");
		Optional<User> adminUser = userService.findByUsername("admin");

		if (adminUser.isEmpty()) {
			UserCreateDto adminCreate = new UserCreateDto();
			adminCreate.setUsername("admin");
			adminCreate.setPassword("admin_p");
			adminCreate.setEmail("admin@gmail.com");
			adminCreate.setFirstName("Super");
			adminCreate.setLastName("Admin");

			try {
				UserResponseDto userCreateDto = userService.createUser(adminCreate);//создаем пользователя через сервис и получаем dto и id

				//НАЗНАЧАЕМ РОЛЬ
				//получаем ID только что созданного пользователя из DTO
				Long adminUserId = userCreateDto.getId();

				userService.assignRoleToUser(adminUserId, "ADMIN");//назначаем роль

				System.out.println("Тестовый пользователь 'admin' создан и назначена роль ADMIN");

			} catch (RuntimeException e) {
				System.err.println("Ошибка при назначении роли ADMIN пользователю 'admin': " + e.getMessage());
			}

		} else {
			System.out.println("Тестовый пользователь 'admin' уже существует.");
			// TODO: Здесь можно добавить логику проверки и назначения ролей, если их нет у существующего пользователя.
		}

		// TODO: Повторить для тестового библиотекаря и читателя, назначая соответствующие роли

		System.out.println("--- Создание начальных данных завершено ---");

	}

	private void createRoleIfNotFound(String roleName) {
		Optional<Role> role = roleService.findByName(roleName);

		if (role.isEmpty()) {
			try {
				roleService.createRole(roleName);
				System.out.println("Роль'" + roleName + "'создана.");
			} catch (RuntimeException e) {
				System.err.println("Ошибка при создании роли '" + roleName + "': " + e.getMessage());
			}
		} else {
			System.out.println("Роль '" + roleName + "' уже существует.");
		}
	}

	private void createAnonymousAuthorIfNotFound(String fullName){
		Optional<AuthorResponseDto> author = authorService.findByFullName(fullName);

		if (author.isEmpty()){
			try {
				AuthorCreationDto authorCreationDto = new AuthorCreationDto(fullName);
				authorService.createAuthor(authorCreationDto);
				System.out.println("Анонимный автор '" + fullName + "' создан");
			}catch (RuntimeException e){
				System.err.println("Ошибка при создании анонимного автора '" + fullName + "': " + e.getMessage());
			}
		} else {
			System.out.println("Анонимный автор '" + fullName + "' уже существует.");
		}
	}
}
