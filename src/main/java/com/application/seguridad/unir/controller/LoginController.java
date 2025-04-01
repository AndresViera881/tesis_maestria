package com.application.seguridad.unir.controller;

import com.application.seguridad.unir.dto.AuthResponseDto;
import com.application.seguridad.unir.dto.LoginDto;
import com.application.seguridad.unir.dto.OtpDto;
import com.application.seguridad.unir.model.Auditoria;
import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.security.JwtGenerador;
import com.application.seguridad.unir.services.EmailService;
import com.application.seguridad.unir.services.IAuditoriaService;
import com.application.seguridad.unir.services.ILoginService;
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

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager _authenticationManager;
    private final JwtGenerador _jwtGenerador;
    private final IAuditoriaService auditoriaService;
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final ILoginService loginService;
    private final EmailService emailService;



    @Autowired
    private HttpServletRequest request;

    public LoginController(AuthenticationManager authenticationManager,
                           JwtGenerador jwtGenerador,
                           IAuditoriaService auditoriaService,
                           ILoginService loginService,               // 👈 agregar
                           EmailService emailService) {
        this._authenticationManager = authenticationManager;
        this._jwtGenerador = jwtGenerador;
        this.auditoriaService = auditoriaService;
        this.loginService = loginService;                           // 👈 asignar
        this.emailService = emailService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            // Generar OTP
            String otp = String.valueOf(new Random().nextInt(900000) + 100000);
            otpStore.put(loginDto.getUsername(), otp);

            // Obtener el correo y enviar OTP
            Optional<User> optionalUser = loginService.authenticate(loginDto.getUsername(), loginDto.getPassword());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado.");
            }

            String correo = optionalUser.get().getEmail();
            emailService.sendOtp(correo, otp);

            // Auditoría (solo del intento)
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(loginDto.getUsername());
            auditoria.setAccion("OTP_ENVIADO");
            auditoria.setModulo("AUTENTICACIÓN");
            auditoria.setDetalles("Se generó y envió un OTP al correo.");
            auditoria.setIpOrigen(request.getRemoteAddr());
            auditoriaService.registrarAuditoria(auditoria);

            return ResponseEntity.ok("OTP enviado correctamente al correo.");
        } catch (Exception ex) {
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(loginDto.getUsername());
            auditoria.setAccion("LOGIN_FALLIDO");
            auditoria.setModulo("AUTENTICACIÓN");
            auditoria.setDetalles("Fallo al iniciar sesión: " + ex.getMessage());
            auditoria.setIpOrigen(request.getRemoteAddr());
            auditoriaService.registrarAuditoria(auditoria);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpDto otpDto) {
        try {
            String otpGuardado = otpStore.get(otpDto.getUsername());

            if (otpGuardado == null || !otpGuardado.equals(otpDto.getOtp())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP inválido o expirado.");
            }

            // Autenticar nuevamente
            Authentication authentication = _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            otpDto.getUsername(),
                            otpDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = _jwtGenerador.generarToken(authentication);

            // Limpiar OTP
            otpStore.remove(otpDto.getUsername());

            // Auditoría
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(otpDto.getUsername());
            auditoria.setAccion("LOGIN_VERIFICADO");
            auditoria.setModulo("AUTENTICACIÓN");
            auditoria.setDetalles("Login verificado correctamente con OTP.");
            auditoria.setIpOrigen(request.getRemoteAddr());
            auditoriaService.registrarAuditoria(auditoria);

            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error al validar OTP: " + ex.getMessage());
        }
    }

}

