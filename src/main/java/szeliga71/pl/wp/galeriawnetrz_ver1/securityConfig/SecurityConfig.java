package szeliga71.pl.wp.galeriawnetrz_ver1.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Wyłącz CSRF dla prostoty (możesz włączyć dla endpointów POST/PUT w produkcyjnym środowisku)
                .csrf(csrf -> csrf.disable())
                // Konfiguracja autoryzacji dla żądań HTTP
                .authorizeHttpRequests(auth -> auth
                        // Pozwól na publiczny dostęp do wszystkich endpointów GET
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        // Wymagaj roli ADMIN dla endpointów POST i PUT
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        // Panel administracyjny dostępny tylko dla ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Wszystkie inne żądania wymagają uwierzytelnienia
                        .anyRequest().authenticated()
                )
                // Włącz Basic Authentication
                .httpBasic(httpBasic -> httpBasic.realmName("GaleriaWnetrz"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Przykładowy użytkownik ADMIN w pamięci (dla testów)
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("123")
                .roles("ADMIN")
                .build();

        // Możesz dodać więcej użytkowników, np. dla różnych ról
        return new InMemoryUserDetailsManager(admin);
    }
}