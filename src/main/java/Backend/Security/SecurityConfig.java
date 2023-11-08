package Backend.Config;
import Backend.Security.Filters.JwtAuthorizationFilter;
import Backend.Security.Jwt.JwtUtils;
import Backend.User.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

//    public final static String[] PUBLIC_REQUEST_MATCHERS = {"v1/user/deleteUser","v1/auth/login","/api-docs/**", "/swagger-ui/**","v1/user/createUser" };
    @Autowired
    JwtAuthorizationFilter authorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {


        return httpSecurity
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/swagger-ui/index.html").permitAll();
                    auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll();
                    auth.requestMatchers("/v1/auth/login").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "CUSTOMER");
                    auth.requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN");
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

//    @Bean
//    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
//        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder)
//                .and().build();
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
//        corsConfiguration.setAllowedOrigins(Arrays.asList("localhost:8080"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST"));
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return source;
//    }
}
