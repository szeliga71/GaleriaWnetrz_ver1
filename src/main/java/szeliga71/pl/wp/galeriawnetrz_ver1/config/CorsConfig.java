package szeliga71.pl.wp.galeriawnetrz_ver1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // wszystkie endpointy
                        .allowedOriginPatterns("*") // pozwól na wszystkie domeny
                        .allowedMethods("*")        // GET, POST, PUT, DELETE, PATCH itd.
                        .allowedHeaders("*")        // wszystkie nagłówki
                        .allowCredentials(true);    // jeśli potrzebujesz cookies / autoryzacji
            }
        };
    }
}
