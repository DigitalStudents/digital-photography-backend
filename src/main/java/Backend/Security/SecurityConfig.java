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

//    public final static String[] PUBLIC_REQUEST_MATCHERS = {"v1/user/**","v1/auth/**","api-docs/**", "swagger-ui/**","v1/user/createUser" };
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
                    auth.requestMatchers("/send-test-email").permitAll();

                    auth.requestMatchers(HttpMethod.POST, "/user/crud/agregarFavorito/**").hasAnyRole("USER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/user/crud/removerFavorito/**").hasAnyRole("USER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET,"/v1/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/v1/reservations").hasRole("USER");


                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    auth.requestMatchers(HttpMethod.GET, "/user/**").permitAll();

                    auth.requestMatchers(HttpMethod.POST, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/v1/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/v1/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/v1/**").hasRole("ADMIN");



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