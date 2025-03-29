package com.application.seguridad.unir.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtGenerador {
    // Método para generar el token con username y rol
    public String generarToken(Authentication authentication) {
        // 👇 Obtenemos el usuario autenticado con ID
        CustomUserDetails usuario = (CustomUserDetails) authentication.getPrincipal();
        String username = usuario.getUsername();
        Integer id = usuario.getId();

        // 👇 Obtener los roles
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        // 👇 Claims personalizados
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", roles);
        claims.put("id", id); // 👈 Agregamos el ID al token

        // 👇 Construir el token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(tiempoActual)
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();
    }

    // Método para extraer el username desde el token
    public String obtenerUsernameJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                .parseClaimsJws(token)
                .getBody();
        System.out.println("CLAIMS: " + claims);
        return claims.getSubject(); // Retorna el username
    }

    // Método para extraer el rol desde el token
    public String obtenerRolDesdeToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("rol"); // Obtener el rol desde los claims
    }

    // Método para validar el token
    public Boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT ha expirado.");
        } catch (SignatureException ex) {
            throw new AuthenticationCredentialsNotFoundException("Firma de JWT incorrecta.");
        } catch (JwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT es incorrecto o malformado.");
        }
    }
}