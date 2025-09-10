
package szeliga71.pl.wp.galeriawnetrz_ver1.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/super/**").hasRole("SUPERADMIN")
                        .requestMatchers("/api/admin/**").hasAnyRole("SUPERADMIN","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
/*package szeliga71.pl.wp.galeriawnetrz_ver1.securityConfig;

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
       /* http
                .csrf(csrf -> csrf.disable()) // wyłącz CSRF dla REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").authenticated() // tylko admin
                        .anyRequest().permitAll()
                )
                .httpBasic(basic -> {});// Basic Auth

        return http.build();
    }

       http
                .csrf(csrf -> csrf.disable())          // wyłączenie CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()           // pozwól na wszystkie żądania
                );
        return http.build();
    }


        /*http
                // Wyłącz CSRF dla prostoty (możesz włączyć dla endpointów POST/PUT w produkcyjnym środowisku)
                .csrf(csrf -> csrf.disable())
                // Konfiguracja autoryzacji dla żądań HTTP
                .authorizeHttpRequests(auth -> auth
                        // Pozwól na publiczny dostęp do wszystkich endpointów GET
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        // Wymagaj roli ADMIN dla endpointów POST i PUT
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()//hasRole("ADMIN")
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
}*/