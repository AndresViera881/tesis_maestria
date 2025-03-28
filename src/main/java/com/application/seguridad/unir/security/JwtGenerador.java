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
        String username = authentication.getName();

        // Obtener los roles del usuario como una lista separada por comas
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // Convierte la lista en una cadena separada por comas

        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        // Crear claims personalizados para incluir los roles
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", roles);

        // Generar el token con claims
        String token = Jwts.builder()
                .setClaims(claims) // Agregar claims personalizados
                .setSubject(username) // Establecer el usuario como subject
                .setIssuedAt(tiempoActual) // Fecha de emisión
                .setExpiration(expiracionToken) // Fecha de expiración
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA) // Firmar el token
                .compact();

        System.out.println("TOKEN: " + token);
        return token;
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