package Backend;

import Backend.User.Model.ERole;
import Backend.User.Model.UserEntity;
import Backend.User.Crud.UserRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BookingLensApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingLensApplication.class, args);
	}


	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Bean
	CommandLineRunner init(){
		return args -> {

			UserEntity userEntity1 = UserEntity.builder()
					.firstName("admin")
					.lastName("admin")
					.username("admin@mail")
					.password(passwordEncoder.encode("password"))
					.role(ERole.ADMIN)
					.build();

			userRepository.save(userEntity1);

		};
	}


	private SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer");
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().addSecurityItem(new SecurityRequirement().
						addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes
						("Bearer Authentication", createAPIKeyScheme()))
				.info(new Info().title("booking API")
						.description("Some custom description of API.")
						.version("1.0").contact(new Contact().name("Sallo Szrajbman")
								.email("www.baeldung.com").url("salloszraj@gmail.com"))
						.license(new License().name("License of API")
								.url("API license URL")));
	}
}

