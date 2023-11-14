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


//	@Autowired
//	PasswordEncoder passwordEncoder;
//
//	@Autowired
//	UserRepository userRepository;

//	@Bean
//	CommandLineRunner init(){
//		return args -> {
//
//			UserEntity userEntity1 = UserEntity.builder()
//					.firstName("henry")
//					.lastName("martinez")
//					.username("henry@mail")
//					.password(passwordEncoder.encode("1234"))
//					.role(ERole.ADMIN)
//					.build();
//
//			UserEntity userEntity2 = UserEntity.builder()
//					.firstName("anyi")
//					.lastName("rojas")
//					.username("anyi@mail.com")
//					.password(passwordEncoder.encode("1234"))
//					.role(ERole.USER)
//					.build();
//
//			UserEntity userEntity3 = UserEntity.builder()
//					.firstName("julian")
//					.lastName("perez")
//					.username("julian@mail.com")
//					.password(passwordEncoder.encode("1234"))
//					.role(ERole.USER)
//					.build();
//
//			UserEntity userEntity4 = UserEntity.builder()
//					.firstName("carmen ")
//					.lastName("cristo")
//					.username("anyi@mail.com")
//					.password(passwordEncoder.encode("1234"))
//					.role(ERole.USER)
//					.build();
//
//			userRepository.save(userEntity1);
//			userRepository.save(userEntity2);
//			userRepository.save(userEntity3);
//			userRepository.save(userEntity4);
//
//		};
//	}


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

