package com.application.seguridad.unir.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtGenerador {
    //Metodo para generar el token por medio de la authentication
    public String generarToken(Authentication authentication){
        String username = authentication.getName();
        String rol = authentication.getAuthorities().toString();
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);
        //Linea para generar el token
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();
        System.out.println("TOKEN" + " " + token);
        return token;
    }

    //Metodo para extraer un username a partir de un token
    public String obtenerUsernameJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                .parseClaimsJws(token)
                .getBody();
        System.out.println("CLAIMS" + " " + claims);
        return claims.getSubject();
    }

    //Metodo para validar el token
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
