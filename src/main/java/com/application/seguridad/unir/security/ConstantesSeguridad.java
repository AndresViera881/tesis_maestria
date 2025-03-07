package com.application.seguridad.unir.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class ConstantesSeguridad {
    public static final long JWT_EXPIRATION_TOKEN = 3000000;
    public static final Key JWT_FIRMA = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}
