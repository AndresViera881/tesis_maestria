package com.application.seguridad.unir.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration: le indica al contenedor de spring que esta es una clase de seguridad al momento de arrancar la aplicacion
@Configuration
//Indicamos que se activa la seguridad web en nuestra aplicacion y ademas esta sera una clase la
// cual contendra toda la configuracion referente a la seguridad
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService _userDetailsService;

    private final JwtAuthenticationFilter _jwtAuthenticationFilter;
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this._userDetailsService = userDetailsService;
        this._jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

        //@Bean para establecer una cadena de filtros de seguridad en nuestra aplicacion
    //Y es aqui donde determinaremos los permisos segun los roles de usuarios para acceder a la aplicacion.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        // Permitir acceso sin token para los inquilinos
                        authorizeRequests
                                .requestMatchers("/api/inquilinos/**").permitAll()  // Permite acceso sin token
                                // Otras rutas de autenticación
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/users/**").authenticated()
                                .anyRequest().authenticated()  // Requiere token para cualquier otra ruta
                )
                .httpBasic(Customizer.withDefaults())
                        .addFilterBefore(_jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(14);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider();
        provider.setUserDetailsService(_userDetailsService);
        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
