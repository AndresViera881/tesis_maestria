package com.application.seguridad.unir.security;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//La funcion de esta clase sera validar la informacion del token y si esto es exitoso,
//establecera la autenticacion de un usuario en la solicitud
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtGenerador _jwtGenerador;
    @Autowired
    private UserDetailsService _customUserDetailsService;

    private String obtenerTokenSolicitud(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = obtenerTokenSolicitud(request);
        if(StringUtils.hasText(token) && _jwtGenerador.validarToken(token)){
            String username = _jwtGenerador.obtenerUsernameJWT(token);
            UserDetails userDetails = _customUserDetailsService.loadUserByUsername(username);
            List<String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            if (userRoles.contains("INQUILINO") || userRoles.contains("ADMIN")){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.info("Autenticación exitosa para el usuario: " + username);
            }else{
                logger.warn("El usuario no tiene roles válidos: " + userRoles);
            }
        }else{
            logger.warn("Token inválido o expirado");
        }
        filterChain.doFilter(request, response);
    }
}
