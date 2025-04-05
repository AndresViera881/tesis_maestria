package com.application.seguridad.unir.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://staging.d1igzzcys54nrg.amplifyapp.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Puedes agregar OPTIONS si no está
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
