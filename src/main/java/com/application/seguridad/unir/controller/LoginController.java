package com.application.seguridad.unir.controller;

import com.application.seguridad.unir.dto.AuthResponseDto;
import com.application.seguridad.unir.dto.LoginDto;
import com.application.seguridad.unir.model.Auditoria;
import com.application.seguridad.unir.security.JwtGenerador;
import com.application.seguridad.unir.services.IAuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager _authenticationManager;
    private final JwtGenerador _jwtGenerador;
    private final IAuditoriaService auditoriaService;

    @Autowired
    private HttpServletRequest request;

    public LoginController(AuthenticationManager authenticationManager,
                           JwtGenerador jwtGenerador,
                           IAuditoriaService auditoriaService) {
        this._authenticationManager = authenticationManager;
        this._jwtGenerador = jwtGenerador;
        this.auditoriaService = auditoriaService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = _jwtGenerador.generarToken(authentication);

            // ✅ Registrar auditoría de login exitoso
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(loginDto.getUsername());
            auditoria.setAccion("LOGIN");
            auditoria.setModulo("AUTENTICACIÓN");
            auditoria.setDetalles("Inicio de sesión exitoso.");
            auditoria.setIpOrigen(request.getRemoteAddr());
            auditoriaService.registrarAuditoria(auditoria);

            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);

        } catch (Exception ex) {
            // ❌ Registrar auditoría de login fallido
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(loginDto.getUsername());
            auditoria.setAccion("LOGIN_FALLIDO");
            auditoria.setModulo("AUTENTICACIÓN");
            auditoria.setDetalles("Fallo al iniciar sesión: " + ex.getMessage());
            auditoria.setIpOrigen(request.getRemoteAddr());
            auditoriaService.registrarAuditoria(auditoria);

            return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }
    }
}

