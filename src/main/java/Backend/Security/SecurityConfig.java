package Backend.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthorizationFilter authorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {


        return httpSecurity
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET,"/user/auth/verify").permitAll();
                    auth.requestMatchers("/swagger-ui/index.html").permitAll();
                    auth.requestMatchers("/api-docs/**", "/swagger-ui/**").permitAll();
                    auth.requestMatchers("/user/auth/login").permitAll();
                    auth.requestMatchers("/user/crud/register").permitAll();
<<<<<<< HEAD
                    auth.requestMatchers("/user/crud/users").permitAll();
//                    auth.requestMatchers("/user/crud/roleUpdate").permitAll();
//                    auth.requestMatchers("/user/crud/deleteUser").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN");
=======
                    auth.requestMatchers("/send-test-email").permitAll();
                    auth.requestMatchers(HttpMethod.GET,"/v1/**").permitAll();

                    auth.requestMatchers("/v1/**").permitAll();

                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
            
                    auth.requestMatchers("/user/**").permitAll();

>>>>>>> 1c438e51880c14d65b231a03c7a6ad6380760560
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }




}